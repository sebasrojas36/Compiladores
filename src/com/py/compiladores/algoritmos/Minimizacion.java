package com.py.compiladores.algoritmos;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import com.py.compiladores.analisis.Alfabeto;
import com.py.compiladores.estructuras.AFD;
import com.py.compiladores.estructuras.AFDMin;
import com.py.compiladores.estructuras.Automata;
import com.py.compiladores.estructuras.Conjunto;
import com.py.compiladores.estructuras.Estado;
import com.py.compiladores.estructuras.Transicion;

/**
 * @author Sebasti�n Rojas
 */
public class Minimizacion {

	/**
	 * Obtiene un AFD minimo a partir de un AFD determinado.
	 */
	public static AFDMin getAFDminimo(AFD afdOriginal) {
		/* Eliminamos los estados inalcanzables */
		AFD afdPostInalcanzables = new AFD();
		copiarAutomata(afdOriginal, afdPostInalcanzables);
		eliminarInalcanzables(afdPostInalcanzables);

		/* Proceso de minimizacion */
		AFD afdPostMinimizacion = minimizar(afdPostInalcanzables);

		/* Eliminamos estados identidades no finales */
		AFD afdPostIdentidades = new AFD();
		copiarAutomata(afdPostMinimizacion, afdPostIdentidades);
		eliminarIdentidades(afdPostIdentidades);

		return new AFDMin(afdOriginal, afdPostInalcanzables,
				afdPostMinimizacion, afdPostIdentidades);
	}

	/**
	 * Elimina los estados inalcanzables de un AFD.
	 */
	private static void eliminarInalcanzables(AFD afd) {
		/* Conjunto de estados alcanzados desde el estado inicial */
		Conjunto<Estado> alcanzados = recuperarAlcanzados(afd);

		/* Eliminamos los estados no alcanzados */
		afd.getEstados().retener(alcanzados);

		/*
		 * No se actualizan los identificadores para que pueda notarse cuales
		 * fueron eliminados, si los hay.
		 */
	}

	/**
	 * A partir del estado inicial de un AFD recupera los estados alcanzados,
	 * realizando un recorrido DFS no recursivo (utiliza una pila).
	 */
	private static Conjunto<Estado> recuperarAlcanzados(AFD afd) {
		/* Estado inicial del recorrido */
		Estado actual = afd.getEstadoInicial();

		/* Conjunto de estados alcanzados */
		Conjunto<Estado> alcanzados = new Conjunto<Estado>();

		/* Agregamos el estado actual */
		alcanzados.agregar(actual);

		/* Pila para almacenar los estados pendientes */
		Stack<Estado> pila = new Stack<Estado>();

		/* Meter el estado actual como el estado inicial */
		pila.push(actual);

		while (!pila.isEmpty()) {
			actual = pila.pop();

			for (Transicion t : actual.getTransiciones()) {
				Estado e = t.getEstado();

				if (!alcanzados.contiene(e)) {
					alcanzados.agregar(e);
					pila.push(e);
				}
			}
		}

		return alcanzados;
	}

	/**
	 * Implementacion del algoritmo de minimizacion de estados.
	 */
	private static AFD minimizar(AFD afd) {
		/* Tablas Hash auxiliares */
		Hashtable<Estado, Conjunto<Integer>> tabla1;
		Hashtable<Conjunto<Integer>, Conjunto<Estado>> tabla2;

		/* Conjunto de las particiones del AFD */
		Conjunto<Conjunto<Estado>> particion = new Conjunto<Conjunto<Estado>>();

		/*
		 * Paso 1: Separar el AFD en dos grupos, los estados finales y
		 * los estados no finales.
		 */
		particion.agregar(afd.getEstadosNoFinales());
		particion.agregar(afd.getEstadosFinales());

		/*
		 * Paso 2: Construccion de nuevas particiones
		 */
		Conjunto<Conjunto<Estado>> nuevaParticion;

		while (true) {
			/* Conjunto de nuevas particiones en cada pasada */
			nuevaParticion = new Conjunto<Conjunto<Estado>>();

			for (Conjunto<Estado> grupo : particion) {
				if (grupo.cantidad() == 0) {
					/*
					 * Los grupos vacios no deben ser tenidos en cuenta. Un
					 * grupo vacio puede resultar en el caso de que todos los
					 * estados del AFD sean de aceptacion (finales), por lo que
					 * en la particion inicial, uno de los grupos estara vacio.
					 */
					continue;
				} else if (grupo.cantidad() == 1) {
					/*
					 * Los grupos unitarios se agregan directamente, debido a
					 * que ya no pueden ser particionados.
					 */
					nuevaParticion.agregar(grupo);
				} else {
					/*
					 * Paso 2.1: Hallamos los grupos alcanzados por cada estado del grupo actual.
					 */
					tabla1 = new Hashtable<Estado, Conjunto<Integer>>();
					for (Estado e : grupo)
						tabla1.put(
								e,
								getGruposAlcanzados(e, particion,
										afd.getAlfabeto()));

					/*
					 * Paso 2.2: Calculamos las nuevas particiones
					 */
					tabla2 = new Hashtable<Conjunto<Integer>, Conjunto<Estado>>();
					for (Estado e : grupo) {
						Conjunto<Integer> alcanzados = tabla1.get(e);
						if (tabla2.containsKey(alcanzados))
							tabla2.get(alcanzados).agregar(e);
						else {
							Conjunto<Estado> tmp = new Conjunto<Estado>();
							tmp.agregar(e);
							tabla2.put(alcanzados, tmp);
						}
					}

					/*
					 * Paso 2.3: Copiar las nuevas particiones al conjunto de nuevas particiones.
					 */
					for (Conjunto<Estado> c : tabla2.values())
						nuevaParticion.agregar(c);
				}
			}

			/* Ordenamos la nueva particion */
			nuevaParticion.ordenar();

			/*
			 * Paso 2.4: Si las particiones son iguales, significa que
			 * no hubo cambios y debemos terminar. En caso contrario, seguimos
			 * particionando.
			 */
			if (nuevaParticion.equals(particion))
				break;
			else
				particion = nuevaParticion;
		}

		/*
		 * Paso 3:  Debemos crear el nuevo AFD, con los nuevos estados
		 * producidos.
		 */
		AFD afdPostMinimizacion = new AFD(afd.getAlfabeto(), afd.getExprReg());

		/*
		 * Paso 3.1: Agregamos los estados al nuevo AFD. Para los
		 * estados agrupados, colocamos una etiqueta distintiva, para que pueda
		 * notarse resultado de cuales estados es.
		 */
		for (int i = 0; i < particion.cantidad(); i++) {
			Conjunto<Estado> grupo = particion.obtener(i);
			boolean esFinal = false;

			/*
			 * El grupo actual tiene un estado final, el estado correspondiente
			 * en el nuevo AFD tambien debe ser final.
			 */
			if (tieneEstadoFinal(grupo))
				esFinal = true;

			/*
			 * Si el estado es resultado de la union de dos o mas estados, su
			 * etiqueta sera del tipo e1.e2.e3, donde e1, e2 y e3 son los
			 * estados agrupados (aparecen separados por un punto).
			 */
			String etiqueta = obtenerEtiqueta(grupo);

			/*
			 * Agregamos efectivamente el estado al nuevo AFD.
			 */
			Estado estado = new Estado(i, esFinal);
			estado.setEtiqueta(etiqueta);
			afdPostMinimizacion.agregarEstado(estado);
		}

		/*
		 * Paso 3.2: Generamos un mapeo de grupos (estados del nuevo
		 * AFD) a estados del AFD original, de manera a que resulte sencillo
		 * obtener los estados adecuados en el momento de agregar las
		 * transiciones al nuevo AFD.
		 */
		Hashtable<Estado, Estado> mapeo = new Hashtable<Estado, Estado>();
		for (int i = 0; i < particion.cantidad(); i++) {
			/* Grupo a procesar */
			Conjunto<Estado> grupo = particion.obtener(i);

			/* Estado del nuevo AFD */
			Estado valor = afdPostMinimizacion.getEstado(i);

			/* Guardar mapeo */
			for (Estado clave : grupo)
				mapeo.put(clave, valor);
		}

		/*
		 * Paso 3.3: Agregamos las transiciones al nuevo AFD
		 * utilizando el mapeo de estados entre dicho AFD y el AFD original,
		 * realizado en el paso 3.2.
		 */
		for (int i = 0; i < particion.cantidad(); i++) {
			/* Estado representante del grupo actual */
			Estado representante = particion.obtener(i).obtenerPrimero();

			/* Estado del nuevo AFD */
			Estado origen = afdPostMinimizacion.getEstado(i);

			/* Agregamos las transciones */
			for (Transicion trans : representante.getTransiciones()) {
				Estado destino = mapeo.get(trans.getEstado());
				origen.getTransiciones().agregar(
						new Transicion(destino, trans.getSimbolo()));
			}
		}

		return afdPostMinimizacion;
	}

	/**
	 * Para un estado dado, busca los grupos en los que caen las transiciones del mismo.
	 */
	private static Conjunto<Integer> getGruposAlcanzados(Estado origen,
			Conjunto<Conjunto<Estado>> particion, Alfabeto alfabeto) {
		/* Grupos alcanzados por el estado */
		Conjunto<Integer> gruposAlcanzados = new Conjunto<Integer>();

		/*
		 * Paso 1: Para cada simbolo del alfabeto obtenemos los estados
		 * alcanzados. Si para un simbolo dado el estado no posee transicion,
		 * colocamos null.
		 */
		HashMap<String, Estado> transiciones = origen
				.getTransicionesSegunAlfabeto(alfabeto);

		/*
		 * Paso 2: Para cada simbolo del alfabeto obtenemos el estado
		 * alcanzado por el estado origen y buscamos en que grupo de la
		 * particion esta.
		 */
		for (String s : alfabeto) {
			/* Estado destino de la transicion */
			Estado destino = transiciones.get(s);

			if (destino == null) {
				/*
				 * Si el estado destino es "null", no pertenecera a ningun
				 * grupo, por lo que colocamos el grupo ficticio con indice -1.
				 */
				gruposAlcanzados.agregar(-1);
			} else {
				for (int pos = 0; pos < particion.cantidad(); pos++) {
					Conjunto<Estado> grupo = particion.obtener(pos);

					if (grupo.contiene(destino)) {
						gruposAlcanzados.agregar(pos);

						/* El estado siempre estara en un solo grupo */
						break;
					}
				}
			}
		}

		return gruposAlcanzados;
	}

	/**
	 * Elimina los estados identidad, aquellos que para todos los simbolos del
	 * alfabeto tienen transiciones a si mismos. Este tipo de estados solo deben
	 * ser eliminados si no son estados de aceptacion.
	 */
	private static void eliminarIdentidades(AFD afd) {
		/* Conjunto de estados a eliminar */
		Conjunto<Estado> estadosEliminados = new Conjunto<Estado>();

		/* Seleccionamos los estados identidad no finales */
		for (Estado e : afd.getEstados())
			if (e.getEsIdentidad() && !e.getEsFinal())
				estadosEliminados.agregar(e);

		if (estadosEliminados.estaVacio()) {
			return;
		}

		/* Eliminamos los estados identidad no finales */
		for (Estado e : estadosEliminados)
			afd.getEstados().eliminar(e);

		/* Transiciones a eliminar */
		Vector<List> transEliminadas = new Vector<List>();

		/* Seleccionamos las transiciones colgadas */
		for (Estado e : afd.getEstados())
			for (Transicion t : e.getTransiciones())
				if (estadosEliminados.contiene(t.getEstado()))
					transEliminadas.add(Arrays.asList(t, e.getTransiciones()));

		/* Eliminamos las transiciones colgadas */
		for (List a : transEliminadas) {
			Transicion t = (Transicion) a.get(0);
			Conjunto<Transicion> c = (Conjunto<Transicion>) a.get(1);
			c.eliminar(t);
		}
	}

	/**
	 * Realiza la copia de un automata origen a otro de destino.
	 */
	private static void copiarAutomata(Automata origen, Automata destino) {
		/* Copiamos los estados y transiciones */
		Automata.copiarEstados(origen, destino, 0);

		/* Copiamos los estados finales y las etiquetas */
		for (int i = 0; i < origen.cantidadEstados(); i++) {
			Estado tmp = origen.getEstado(i);

			destino.getEstado(i).setEsFinal(tmp.getEsFinal());
			destino.getEstado(i).setEtiqueta(tmp.getEtiqueta());
		}

		/* Copiamos el alfabeto y la expresion regular */
		destino.setAlfabeto(origen.getAlfabeto());
		destino.setExprReg(origen.getExprReg());
	}

	/**
	 * Determina si un grupo de estados tiene un estado final.
	 */
	private static boolean tieneEstadoFinal(Conjunto<Estado> grupo) {
		for (Estado e : grupo)
			if (e.getEsFinal())
				return true;

		return false;
	}

	/**
	 * Calcula la nueva etiqueta para un estado del nuevo AFD, segun los estados agrupados.
	 */
	private static String obtenerEtiqueta(Conjunto<Estado> grupo) {
		String etiqueta = "";
		String pedazo;

		for (Estado e : grupo) {
			/* Eliminamos la "i" y/o "f" en caso de que exista */
			if (e.toString().endsWith("if"))
				pedazo = e.toString().substring(0, e.toString().length() - 2);
			else if (e.toString().endsWith("i") || e.toString().endsWith("f"))
				pedazo = e.toString().substring(0, e.toString().length() - 1);
			else
				pedazo = e.toString();

			/* Agregamos */
			etiqueta += pedazo + " ";
		}

		if (etiqueta.endsWith(" "))
			etiqueta = etiqueta.substring(0, etiqueta.length() - 1);

		return "(" + etiqueta + ")";
	}
}
