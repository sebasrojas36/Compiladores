/**
 * 
 */
package com.py.compiladores.algoritmos;

import com.py.compiladores.estructuras.Automata;
import com.py.compiladores.estructuras.Conjunto;

/**
 * Esta clase representa los datos obtenidos
 * como resultado de un proceso de validacion
 * de una cadena de entrada contra un Automata.
 * @author Sebastián Rojas
 */
public abstract class ResultadoValidacion {
   
    /**
     * Automata asociado a este resultado
     * de validacion.
     */
    protected Automata automata;
   
    /**
     * Cadena de entrada asociada a este
     * resultado de validacion.
     */
    protected String entrada;
   
    /**
     * Simbolos de entrada que no pudieron
     * ser consumidos.
     */
    protected String entradaFaltante;

    /**
     * Obtiene el Automata asociado
     * a este resultado de validacion.
     * @return El Automata asociado
     * a este resultado de validacion.
     */
    public Automata getAutomata() {
        return automata;
    }

    /**
     * Obtiene la cadena de entrada asociada
     * a este resultado de validacion.
     * @return La cadena de entrada asociada
     * a este resultado de validacion.
     */
    public String getEntrada() {
        return entrada;
    }

    /**
     * El camino de Estados o Conjunto
     * de Estados que resulta de validar la cadena
     * de entrada.
     * @return Un Conjunto de Estados
     * o de Conjunto de Estados
     * alcanzados durante la validacion.
     */
    public abstract Conjunto getCamino();

    /**
     * Obtiene los simbolos de entrada que no
     * pudieron ser consumidos.
     * @return Un String que contiene
     * los simbolos de entrada que no pudieron ser
     * consumidos, o la cadena vacia si todos los
     * simbolos fueron consumidos.
     */
    public String getEntradaFaltante() {
        return entradaFaltante;
    }
   
    /**
     * Determina si el resultado de la validacion
     * es valido o no. Es decir, si la cadena de
     * entrada es aceptada o no por el Automata.
     * @return true si la cadena de entrada
     * es aceptada por el Automata, false
     * en caso contrario.
     */
    public abstract boolean esValido();
}
