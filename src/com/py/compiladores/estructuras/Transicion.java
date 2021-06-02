package com.py.compiladores.estructuras;


/**
 * Implementa la transicion de un automata, representada
 * por el simbolo y el estado destino. El estado inicial
 * esta dado por el estado en el que esta contenida esta
 * transicion.
 *
 * @see Estado
 * @author Sebastián Rojas
 */
public class Transicion implements Comparable<Transicion> {
   
    /**
     * Estado destino de esta Transicion.
     */
    private Estado estado;
   
    /**
     * Simbolo del alfabeto para esta Transicion.
     */
    private String simbolo;

    /**
     * Construye una Transicion especificando los dos
     * atributos de la misma.
     *
     * @param estado El Estado destino para esta Transicion.
     * @param simbolo El simbolo para esta Transicion.
     */
    public Transicion(Estado estado, String simbolo) {
        this.estado  = estado;
        this.simbolo = simbolo;
    }

    /**
     * Contruye una Transicion sin Estado
     * ni simbolo.
     */
    public Transicion() {
        this(null, null);
    }

    /**
     * Obtiene el Estado destino de esta Transicion.
     * @return El Estado destino de esta Transicion.
     */
    public Estado getEstado() {
        return estado;
    }

    /**
     * Establece el Estado destino de esta Transicion.
     * @param estado El nuevo Estado destino de esta Transicion.
     */
    public void setEstado(Estado estado) {
        this.estado = estado;
    }
   
    /**
     * Obtiene el simbolo para esta Transicion.
     * @return El simbolo para esta Transicion.
     */
    public String getSimbolo() {
        return simbolo;
    }
   
    /**
     * Establece el simbolo para esta Transicion.
     * @param simbolo El nuevo simbolo para esta Transicion.
     */
    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }
   
    @Override
    public String toString() {
        return "(" + getEstado() + ", " + getSimbolo() + ")";
    }

    public int compareTo(Transicion obj) {
        Estado e1 = this.getEstado();
        Estado e2 = obj.getEstado();
       
        int diferencia = e1.getIdentificador() - e2.getIdentificador();
       
        if (diferencia != 0)
            return diferencia;
       
        String s1 = this.getSimbolo();
        String s2 = obj.getSimbolo();
       
        return s1.compareTo(s2);
    }
}
