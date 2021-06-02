/**
 * 
 */
package com.py.compiladores.estructuras;

import com.py.compiladores.analisis.Alfabeto;


/**
 * Clase que representa la abstraccion para un Automata Finito
 * No deterministico (AFN). Un AFN es contruido a partir de una
 * expresion regular a traves de las construcciones de Thompson.
 * @author Sebastián Rojas
 */
public class AFN extends Automata {
       
    /**
     * Constructor por defecto.
     */
    public AFN() {
       super();
    }
   
    /**
     * Construye un AFN con un determinado Alfabeto
     * y una determinada expresion regular.
     * @param alfabeto El Alfabeto de este AFN.
     * @param exprReg La expresion regular para este AFN.
     */
    public AFN(Alfabeto alfabeto, String exprReg) {
        super(alfabeto, exprReg);
    }
   
    /**
     * Retorna la tabla de transicion de estados.
     * @return La tabla de transicion de estados.
     */
    public TablaTransicion getTablaTransicion() {
        int cantFil = getEstados().cantidad();
        int cantCol = getAlfabeto().getCantidad() + 2;
       
        return cargarTablaTransicion(cantFil, cantCol, 0);
    }
}

