package com.py.compiladores.analisis;



public enum TokenExprReg {
    /**
     * Par�ntesis derecho, ")".
     */
    PARENTESIS_DER,
    
    /**
     * Par�ntesis izquierdo, "(".
     */
    PARENTESIS_IZQ,
    
    /**
     * Operador de uni�n, "|".
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
     * Operador de opci�n, "?".
     */
    OPCION,
    
    /**
     * Operador de concatenaci�n (no tiene dibujo).
     */
    CONCATENACION,
    
    /**
     * Un s�mbolo del alfabeto.
     */
    ALFABETO,
    
    /**
     * Finalizador de una expresi�n regular (EOF).
     */
    FINAL,
    
    /**
     * Token desconocido (inv�lido).
     */
    DESCONOCIDO
}
