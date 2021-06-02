package com.py.compiladores.analisis;

import java.util.Enumeration;
import java.util.Hashtable;



/**
 * @author Sebastián Rojas
 */
public class AnalizadorLexico {
   
    /**
     * Conjunto de simbolos que pueden ser utilizados para
     * escribir la entrada (exprReg) para el
     * Analizador Lexico.
     */
    private Alfabeto alfabeto;
   
    /**
     * La cadena de entrada sobre el cual debe trabajar el
     * Analizador Lexico. La misma consiste en una expresion
     * regular definida sobre el alfabeto con los siguientes
     * simbolos |, *, +, ?, (, ), mas todos los
     * simbolos de alfabeto.
     */
    private String exprReg;
   
    /**
     * Variable utilizada como buffer de entrada. Inicialmente,
     * consiste en la expresion regular de entrada.
     */
    private StringBuffer buffer;
   
    /**
     * Tabla de simbolos validos esperados por el analizador lexico.
     */
    private Hashtable<String, TokenExprReg> tablaSimbolos;
   
    /**
     * Constructor de la clase.
     * @param alfabeto El alfabeto de simbolos posibles sobre
     * la cual se puede definir una expresion regular.
     * @param exprReg La expresion regular (entrada) sobre la que se trabaja.
     */
    public AnalizadorLexico(Alfabeto alfabeto, String exprReg) {
       this.alfabeto = alfabeto;
       this.exprReg  = exprReg;
       this.buffer   = new StringBuffer(exprReg);
       crearTablaSimbolos();
    }
   
    /**
     * Este metodo se encarga de consumir caracteres del buffer
     * de entrada, convertirlos a tokens y retornarlos al Analizador
     * Sintactico.
     * @return El siguiente token recuperado de la entrada.
     * @throws Exception Si se intenta construir un token invalido.
     */
    public Token sgteToken() throws Exception {
        String lexema = sgteLexema();

        // Omitimos cualquier tipo de espacio en blanco
        if (lexema.matches("\\s"))
            return sgteToken();

        TokenExprReg tipoToken = tablaSimbolos.get(lexema);

        if (tipoToken == null)
            return new Token(TokenExprReg.DESCONOCIDO, lexema);
        else if (tipoToken == TokenExprReg.ALFABETO)
            return new Token(TokenExprReg.ALFABETO, lexema);
        else
            return new Token(tipoToken);
    }
   
    /**
     * Obtiene el Alfabeto asociado a este
     * AnalizadorLexico.
     * @return El Alfabeto asociado a este
     * AnalizadorLexico.
     */
    public Alfabeto getAlfabeto() {
        return alfabeto;
    }
   
    /**
     * Obtiene la expresion regular asociada a este
     * AnalizadorLexico.
     * @return La expresion regular asociada a este
     * AnalizadorLexico.
     */
    public String getExpresionRegular() {
        return exprReg;
    }
   
    /**
     * Este metodo consume cada uno de los caracteres del buffer
     * de entrada, hasta agotarlos. En dicho momento, retorna la
     * cadena vacia, que representara al token FINAL.
     * @return Siguiente caracter en el buffer de entrada.
     */
    private String sgteLexema() {
        String salida = "";
       
        if (buffer.length() > 0) {
            salida += buffer.charAt(0);
            buffer.deleteCharAt(0);
        }
       
        return salida;
    }
   
    private void crearTablaSimbolos() {
        tablaSimbolos = new Hashtable<String, TokenExprReg>();

        tablaSimbolos.put("*", TokenExprReg.KLEENE);
        tablaSimbolos.put("+", TokenExprReg.CER_POSITIVA);
        tablaSimbolos.put("?", TokenExprReg.OPCION);
        tablaSimbolos.put("|", TokenExprReg.UNION);
        tablaSimbolos.put("(", TokenExprReg.PARENTESIS_IZQ);
        tablaSimbolos.put(")", TokenExprReg.PARENTESIS_DER);
        tablaSimbolos.put("", TokenExprReg.FINAL);

        for (int i=0; i < alfabeto.getCantidad(); i++) {
            String simbolo = alfabeto.getSimbolo(i);
            tablaSimbolos.put(simbolo, TokenExprReg.ALFABETO);
        }
        Enumeration enumeration = tablaSimbolos.elements();
        Enumeration llaves = tablaSimbolos.keys();
        while (llaves.hasMoreElements()) {
            System.out.println("<" + llaves.nextElement()+ ", " + enumeration.nextElement() + ">");
        }
    }
}


