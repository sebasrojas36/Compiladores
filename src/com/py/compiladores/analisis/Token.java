package com.py.compiladores.analisis;


/**
 * Clase que representa un token de una expresi√≥n regular. El token
 * tiene dos atributos: identificador y valor.
 * @author Sebasti·n Rojas
 * @see TokenExprReg
 */
public class Token {
   
    /**
     * Identificador del token.
     */
    private TokenExprReg ident;
   
    /**
     * Valor del token.
     */
    private String valor;
   
    /**
     * Constructor por defecto.
     * @param token El tipo de token que deseamos crear.
     * @throws Exception En caso de que token sea un tipo invalido.
     */
    public Token(TokenExprReg token) throws Exception {
        switch (token) {
            case KLEENE:
                ident = TokenExprReg.KLEENE;
                valor = "*";
                break;
            case CER_POSITIVA:
                ident = TokenExprReg.CER_POSITIVA;
                valor = "+";
                break;
            case OPCION:
                ident = TokenExprReg.OPCION;
                valor = "?";
                break;
            case CONCATENACION:
                ident = TokenExprReg.CONCATENACION;
                valor = "#";
                break;
            case UNION:
                ident = TokenExprReg.UNION;
                valor = "|";
                break;
            case PARENTESIS_IZQ:
                ident = TokenExprReg.PARENTESIS_IZQ;
                valor = "(";
                break;
            case PARENTESIS_DER:
                ident = TokenExprReg.PARENTESIS_DER;
                valor = ")";
                break;
            case FINAL:
                ident = TokenExprReg.FINAL;
                valor = "";
                break;
            default:
                throw new Exception("Token invalido");
        }
    }
   
    /**
     * Constructor para simbolos del alfabeto y para simbolos desconocidos.
     * @param token El tipo de token que deseamos crear.
     * @param simbolo Simbolo del alfabeto o desconocido para el token.
     * @throws Exception En caso de que token sea un tipo invalido.
     */
    public Token(TokenExprReg token, String simbolo) throws Exception {
        switch (token) {
            case ALFABETO:
                ident = TokenExprReg.ALFABETO;
                valor = simbolo;
                break;
            case DESCONOCIDO:
                ident = TokenExprReg.DESCONOCIDO;
                valor = simbolo;
                break;
            default:
                throw new Exception("Token invalido");
        }
    }
   
    /**
     * Devuelve el atributo <i>identificador</i> del token.
     * @return Identificador del token.
     */
    public TokenExprReg getIdentificador() {
        return ident;
    }
   
    /**
     * Devuelve el atributo <i>valor</i> del token.
     * @return El atributo <i>valor</i> del token.
     */
    public String getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return valor;
    }
   
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
       
        if (getClass() != obj.getClass()) {
            return false;
        }
       
        final Token other = (Token) obj;
        if (this.ident != other.ident) {
            return false;
        }
       
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.ident != null ? this.ident.hashCode() : 0);
        hash = 59 * hash + (this.valor != null ? this.valor.hashCode() : 0);
        return hash;
    }
}


