package com.py.compiladores.algoritmos;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import com.py.compiladores.analisis.Alfabeto;
import com.py.compiladores.estructuras.AFD;
import com.py.compiladores.estructuras.Automata;
import com.py.compiladores.estructuras.Conjunto;
import com.py.compiladores.estructuras.Estado;
import com.py.compiladores.estructuras.Transicion;

/**
 * Esta clase implementa los algoritmos para realizar
 * la conversion de un AFN a un AFD.
 * @see Automata
 * @author Sebasti·n Rojas
 */
public class Subconjuntos {

    /**
     * Realiza la conversion de un AFN a un AFD, aplicando el algoritmo de subconjuntos
     */
    public static AFD getAFD(Automata afn) {
        Estado estadoOrigen, estadoDestino;
       
        /* AFD resultante */
        AFD afd = new AFD(afn.getAlfabeto(), afn.getExprReg());
       
        /* Conjunto de estados finales del AFD */
        Conjunto<Conjunto<Estado>> estadosD = new Conjunto<Conjunto<Estado>>();
       
        /*
         * En esta cola se iran almacenando temporalmente los conjuntos de
         * estados y la operacion de marcarlos consistira en quitarlos la misma
         * y guardarlos en estadosD.
         * La condicion de que no haya estados marcados se dara cuando la cola
         * este vacia.
         */
        Queue<Conjunto<Estado>> colaTemp = new LinkedList<Conjunto<Estado>>();
       
        /* Contador de estados procesados del AFD */
        int estadosProcesados = 0;
       
        /* Calculamos la Cerradura Epsilon del estado inicial */
        Conjunto<Estado> resultado = cerraduraEpsilon(afn.getEstadoInicial());
       
       
         /*
          * Agregamos la Cerradura Epsilon del estado
          * inicial del AFN a estadosD sin marcar
          */
        estadosD.agregar(resultado);
        colaTemp.add(resultado);
       
        /*
         * Iniciamos el ciclo principal del algoritmo
         */
        while (!colaTemp.isEmpty()) {
            /* Marcar T */
            Conjunto<Estado> T = colaTemp.remove();
           
            /* Agregamos el correspondiente estado al AFD */
            if (afd.cantidadEstados() < estadosD.cantidad())
                afd.agregarEstado(new Estado(afd.cantidadEstados()));
           
            /* Estado del AFD a procesar */
            estadoOrigen = afd.getEstado(estadosProcesados++);
           
            /* Buscar transiciones por cada simbolo */
            for (String simbolo : afn.getAlfabeto()) {
                /* Aplicar cerraduraEpsilon(mueve(T, simbolo)) */
                Conjunto<Estado> M = mover(T, simbolo);
                Conjunto<Estado> U = cerraduraEpsilon(M);
               
                if (estadosD.contiene(U)) {
                    int posicion  = estadosD.obtenerPosicion(U);
                    estadoDestino = afd.getEstado(posicion);
                }
                else if (!U.estaVacio()) {
                    estadoDestino = new Estado(afd.cantidadEstados());
                    afd.agregarEstado(estadoDestino);
                   
                    /* Agregar U a estadosD (sin marcar) si no esta a√∫n */
                    estadosD.agregar(U);
                    colaTemp.add(U);
                }
                else {
                    /*
                     * Encontramos un conjunto vacio, por tanto,
                     * no debemos agregar ninguna transicion y
                     * debemos saltar directamente a evaluar el
                     * siguiente simbolo del alfabeto.
                     */
                    continue;
                }
               
                // Agregamos la transicion al AFD
                Transicion trans = new Transicion(estadoDestino, simbolo);
                estadoOrigen.getTransiciones().agregar(trans);
            }
           
        }
       
        /* Establecemos los estados finales del AFD */
        for (int i=0; i < estadosD.cantidad(); i++) {
            Estado estadoAFD = afd.getEstado(i);
           
            for (Estado e : estadosD.obtener(i)) {
                if (e.getEsFinal()) {
                    estadoAFD.setEsFinal(true);
                    break;
                }
            }
        }
       
        afd.setEstadosD(estadosD);
        return afd;
    }
   
    /**
     * Implementa la operacion Cerradura Epsilon sobre un Estado.
     * @param estado Estado sobre el cual aplicar la operacion.
     * @return El Conjunto de estados alcanzados.
     */
    public static Conjunto<Estado> cerraduraEpsilon(Estado estado) {
        Conjunto<Estado> resultado = new Conjunto<Estado>();
        recorrido(estado, resultado, Alfabeto.VACIO);
        resultado.ordenar();
        return resultado;
    }
   
    /**
     * Implementa la operacion Cerradura Epsilon sobre un Conjunto
     * de Estados.
     * @param estados Conjunto de Estados sobre el
     * cual aplicar operacion.
     * @return El Conjunto de estados alcanzados.
     */
    public static Conjunto<Estado> cerraduraEpsilon(Conjunto<Estado> estados) {
        Conjunto<Estado> resultado = new Conjunto<Estado>();
        recorrido(estados, resultado, Alfabeto.VACIO);
        resultado.ordenar();
        return resultado;
    }
   
    /**
     * Implementa la operacion Mueve.
     * @param estados Estados sobre los cuales aplicar la operacion.
     * @param simbolo Simbolo que debe seguirse en las Transicions.
     * @return El Conjunto de estados alcanzados.
     */
    public static Conjunto<Estado> mover(Conjunto<Estado> estados, String simbolo) {
        Conjunto<Estado> resultado = new Conjunto<Estado>();
        recorrido(estados, resultado, simbolo);
        resultado.ordenar();
        return resultado;
    }
   
    /**
     * Realiza un recorrido del automata a partir de un Estado
     * inicial y pasando por todas las Transicions que coincidan
     * con determinado simbolo.
     */
    private static void recorrido(Estado actual, Conjunto<Estado> alcanzados, String simboloBuscado) {
        /* Pila para almacenar los estados pendientes */
        Stack<Estado> pila = new Stack<Estado>();
       
        /*
         * Cuando el simbolo buscado es igual al simbolo
         * vacio, el estado desde donde se empieza el
         * recorrido debe incluirse entre los estados
         * alcanzados.
         */
        if (simboloBuscado.equals(Alfabeto.VACIO))
            alcanzados.agregar(actual);
       
        /* Meter el estado actual como el estado inicial */
        pila.push(actual);
       
        while (!pila.isEmpty()) {
            actual = pila.pop();
            for (Transicion t : actual.getTransiciones()) {
                Estado e = t.getEstado();
                String s = t.getSimbolo();
               
                if (s.equals(simboloBuscado) && !alcanzados.contiene(e)) {
                    alcanzados.agregar(e);
                   
                    /*
                     * Debido a que solo cuando el simbolo buscado
                     * es igual a vacio se debe recorrer recursivamente
                     * los estados alcanzados, agregamos dichos estados
                     * a la pila solo si se da esa condicion.
                     */
                    if (simboloBuscado.equals(Alfabeto.VACIO))
                        pila.push(e);
                }
            }
        }
    }
   
    /**
     * Realiza un recorrido del automata a partir de un Estado
     * inicial y pasando por todas las Transicions que coincidan
     * con determinado simbolo.
     * @param inicios El Conjunto Estados a partir de los cuales
     * se realiza el recorrido.
     * @param alcanzados Conjunto donde se guardan los Estados alcanzados.
     * @param simboloBuscado Simbolo que debe seguirse en las Transicions.
     */
    private static void recorrido(Conjunto<Estado> inicios, Conjunto<Estado> alcanzados, String simboloBuscado) {
        for (Estado e : inicios)
            recorrido(e, alcanzados, simboloBuscado);
    }
}

