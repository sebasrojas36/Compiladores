/**
 * 
 */
package com.py.compiladores.algoritmos;

import com.py.compiladores.algoritmos.ResultadoValidacion;
import com.py.compiladores.estructuras.AFD;
import com.py.compiladores.estructuras.Conjunto;
import com.py.compiladores.estructuras.Estado;
import com.py.compiladores.estructuras.Par;

/**
 * Esta clase representa los datos obtenidos
 * como resultado de un proceso de validacion
 * de una cadena de entrada contra un AFD.
 * @author Sebastián Rojas
 */
public class ResultadoValidacionAFD extends ResultadoValidacion {
   
    /**
     * Camino producido
     */
    private Conjunto<Par<Estado, String>> camino;
   
    /**
     * Construye el resultado de una validacion de
     * una cadena de entrada contra un Automata.
     * @param automata El AFD relacionado a la validacion.
     * @param entrada La cadena de entrada relacionada a la validacion.
     * @param camino El camino resultante de la validacion.
     * @param entradaFaltante Simbolos de entrada que no pudieron ser consumidos.
     */
    public ResultadoValidacionAFD(AFD automata, String entrada,
        Conjunto<Par<Estado, String>> camino, String entradaFaltante) {
       
        this.automata = automata;
        this.entrada = entrada;
        this.camino = camino;
        this.entradaFaltante = (entradaFaltante == null) ? "" : entradaFaltante;
    }
   
    /**
     * El camino de Estados que
     * resulta de validar la cadena de entrada.
     * @return Un Conjunto de Estados
     * alcanzados durante la validacion.
     */
    public Conjunto<Par<Estado, String>> getCamino() {
        return camino;
    }
   
    /**
     * Determina si el resultado de la validacion
     * es valido o no. Es decir, si la cadena de
     * entrada es aceptada o no por el Automata.
     * @return true si la cadena de entrada
     * es aceptada por el Automata, false
     * en caso contrario.
     */
    public boolean esValido() {
        if (!entradaFaltante.equals(""))
            return false;
       
        if (camino.obtenerUltimo().getPrimero().getEsFinal())
            return true;
        else
            return false;
    }
}

