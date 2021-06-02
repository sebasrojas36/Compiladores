/**
 * 
 */
package com.py.compiladores.estructuras;

import java.util.HashMap;

import com.py.compiladores.analisis.Alfabeto;

/**
 * Esta clase representa un estado para un Automata Finito.
 * @author Sebasti·n Rojas
 */
public class Estado implements Comparable<Estado> {

    /**
     * Identificador de este Estado.
     */
    private int identificador;
   
    /**
     * Variable que indica si el Estado es final.
     */
    private boolean esFinal;
   
    /**
     * Etiqueta de este estado.
     */
    private String etiqueta;
   
    /**
     * Conjunto de transiciones del Estado.
     */
    private Conjunto<Transicion> transiciones;
   
    /**
     * Indica si este Estado ya fue visitado
     * durante alg√∫n recorrido realizado sobre
     * el Automata que contiene a este Estado.
     */
    private boolean visitado;
   
    /**
     * Crea un Estado no final con un identificador determinado.
     * @param identificador El identificador del nuevo estado.
     */
    public Estado(int identificador) {
        this(identificador, false);
    }
   
    /**
     * Crea un Estado con un identificador determinado,
     * que sera final o no de acuerdo al valor de esFinal.
     * @param identificador El identificador para este Estado.
     * @param esFinal Determina si el Estado es final o no.
     */
    public Estado(int identificador, boolean esFinal) {
        setIdentificador(identificador);
        setEsFinal(esFinal);
        setEtiqueta(String.valueOf(identificador));
        transiciones = new Conjunto<Transicion>();
    }
   
    /**
     * Establece un nuevo identificador para este Estado.
     * @param identificador El nuevo identificador para este Estado.
     */
    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }
   
    /**
     * Obtiene el identificador para este Estado.
     * @return El identificador del Estado.
     */
    public int getIdentificador() {
        return identificador;
    }
   
    /**
     * Obtiene la etiqueta de este Estado.
     * @return Un objeto String representando
     * la etiqueta de este Estado.
     */
    public String getEtiqueta() {
        return etiqueta;
    }

    /**
     * Establece una nueva etiqueta para este Estado.
     * @param etiqueta La nueva etiqueta para este Estado.
     */
    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }
   
    /**
     * Obtiene el estado de aceptacion del Estado.
     * @return true si el Estado es final,
     * false en caso contrario.
     */
    public boolean getEsFinal() {
        return esFinal;
    }

    /**
     * Establece un nuevo estado de aceptacion para este Estado.
     * @param esFinal El nuevo estado de aceptacion para este Estado.
     * Si el valor true el Estado se convertira en
     * final, si es false el Estado se convertira
     * en no final.
     */
    public void setEsFinal(boolean esFinal) {
        this.esFinal = esFinal;
    }
   
    /**
     * Obtiene el estado de inicio del Estado.
     * @return true si el Estado es inicial,
     * false en caso contrario.
     */
    public boolean getEsInicial() {
        return identificador == 0;
    }
   
    /**
     * Obtiene el conjunto de transiciones de este Estado.
     * @return El conjunto de transiciones del Estado.
     */
    public Conjunto<Transicion> getTransiciones() {
        return transiciones;
    }
   
    /**
     * Obtiene una tabla hash con las transiciones de este Estado
     * a traves de los simbolos de un Alfabeto dado.
     * @param alfabeto El Alfabeto sobre el cual buscar transiciones
     * en el Estado.
     * @return Un Hashtable que contiene el Estado
     * alcanzado para cada simbolo de alfabeto. Si no existe transicion
     * para un simbolo dado, el valor sera igual a null.
     */
    public HashMap<String, Estado> getTransicionesSegunAlfabeto(Alfabeto alfabeto) {
        HashMap<String, Estado> trans = new HashMap<String, Estado>();
       
        /* Rellenamos todo con null */
        for (String s : alfabeto)
            trans.put(s, null);
       
        /* Reemplazamos las transiciones existentes */
        for (Transicion t : getTransiciones())
            trans.put(t.getSimbolo(), t.getEstado());
       
        return trans;
    }
   
    /**
     * Obtiene es estado de visitado de este Estado.
     * @return true si este Estado ya
     * ha sido visitado, false en caso contrario.
     */
    public boolean getVisitado() {
        return visitado;
    }
   
    /**
     * Establece el nuevo estado de visitado de este Estado.
     * @param visitado Nuevo estado de visitado de este Estado.
     */
    public void setVisitado(boolean visitado) {
        this.visitado = visitado;
    }
   
    /**
     * Verifica si el Estado es un estado identidad.
     * @return true si este Estado es un
     * estado identidad, false en caso contrario.
     */
    public boolean getEsIdentidad() {
        for (Transicion trans : getTransiciones())
            if (!this.equals(trans.getEstado()))
                return false;
       
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
       
        if (getClass() != obj.getClass())
            return false;
       
        final Estado other = (Estado) obj;
        if (this.identificador == other.identificador)
            return true;
       
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + this.identificador;
        return hash;
    }

    public int compareTo(Estado obj) {
        return this.identificador - obj.identificador;
    }
   
    @Override
    public String toString() {
        String valor;
       
        if (getEtiqueta().equals(""))
            valor = String.valueOf(identificador);
        else
            valor = getEtiqueta();
       
        if (getEsInicial())
            valor += "i";
       
        if (getEsFinal())
            valor += "f";
               
        return valor;
    }
}

