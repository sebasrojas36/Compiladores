package com.py.compiladores.analisis;



public enum TokenExprReg {
    /**
     * Paréntesis derecho, ")".
     */
    PARENTESIS_DER,
    
    /**
     * Paréntesis izquierdo, "(".
     */
    PARENTESIS_IZQ,
    
    /**
     * Operador de unión, "|".
     */
    UNION,
    
    /**
     * Operador de cerradura de Kleene, "*".
     */
    KLEENE,
    
    /**
     * Operador de cerradura positiva, "+".
     */
    CER_POSITIVA,
    
    /**
     * Operador de opción, "?".
     */
    OPCION,
    
    /**
     * Operador de concatenación (no tiene dibujo).
     */
    CONCATENACION,
    
    /**
     * Un símbolo del alfabeto.
     */
    ALFABETO,
    
    /**
     * Finalizador de una expresión regular (EOF).
     */
    FINAL,
    
    /**
     * Token desconocido (inválido).
     */
    DESCONOCIDO
}
