package com.py.compiladores.estructuras;

import com.py.compiladores.analisis.Alfabeto;

/**
 * @author Sebastián Rojas
 */
public class AFD extends Automata {
   
    private Conjunto<Conjunto<Estado>> estadosD;
   
    public AFD() {
       this(null, "");
    }
   
    /**
     * Construye un AFD a partir de un Alfabeto y una expresion regular.
     */
    public AFD(Alfabeto alfabeto, String exprReg) {
        super(alfabeto, exprReg);
        estadosD = null;
    }
   
    /**
     * Obtiene el Conjunto de Estados del AFN contenidos en cada uno de los Estados de este AFD.
     */
    public Conjunto<Conjunto<Estado>> getEstadosD() {
        return estadosD;
    }

    /**
     * Establece el Conjunto de Estados del AFN contenidos en cada uno de los Estados de este AFD.
     */
    public void setEstadosD(Conjunto<Conjunto<Estado>> estadosD) {
        this.estadosD = estadosD;
    }
   
    public String estadosDtoString() {
        String str = "";
       
        for (int i=0; i < estadosD.cantidad(); i++) {
            Conjunto<Estado> conj = estadosD.obtener(i);
            Estado actual = getEstado(i);
           
            str += actual + " --> " + conj + "\n";
        }
       
        return str;
    }
   
    /**
     * Retorna la tabla de transicion de estados.
     */
    public TablaTransicion getTablaTransicion() {
        TablaTransicion tabla;
       
        if (getEstadosD() != null) {
            int cantFil = getEstados().cantidad();
            int cantCol = getAlfabeto().getCantidad() + 2;

            tabla = cargarTablaTransicion(cantFil, cantCol, 1);
            tabla.setHeaderAt("Estados del AFN", 0);

            for (int i=0; i < estadosD.cantidad(); i++)
                tabla.setValueAt(estadosD.obtener(i), i, 0);
        }
        else {
            int cantFil = getEstados().cantidad();
            int cantCol = getAlfabeto().getCantidad() + 1;

            tabla = cargarTablaTransicion(cantFil, cantCol, 0);
        }
       
        return tabla;
    }
}

