package com.py.compiladores.claseprincipal;

import java.util.Scanner;
import com.py.compiladores.algoritmos.Minimizacion;
import com.py.compiladores.algoritmos.Subconjuntos;
import com.py.compiladores.analisis.Alfabeto;
import com.py.compiladores.analisis.AnalizadorSintactico;
import com.py.compiladores.estructuras.AFD;
import com.py.compiladores.estructuras.AFDMin;
import com.py.compiladores.estructuras.AFN;
import com.py.compiladores.estructuras.TablaTransicion;

public class Main {
	
    public static void main(String args[]) throws Exception{
        
        /* Introducci�n del Alfabeto y de la Expresi�n Regular de entrada */

        System.out.printf("Introduzca el alfabeto: ");
        Scanner sc = new Scanner(System.in);
        String cadena1 = sc.nextLine();
        Alfabeto alfa = new Alfabeto(cadena1);
        System.out.printf("Introduzca la expresi�n regular: ");
        String cadena2 = sc.nextLine();
        String er = cadena2;

        /* Imprimir la Tabla de Simbolos */

        System.out.printf("\nTabla de Simbolos: \n");
        AnalizadorSintactico as = new AnalizadorSintactico(alfa, er);
        
        /* Conversi�n de la entrada a AFN por el algoritmo de Thompson */ 
       
        AFN salida = as.analizar();        
                       
        /* Conversi�n de AFN a AFD por el algoritmo de Subonjunto */
        
        AFD afd = Subconjuntos.getAFD(salida);
       
        /* Conversi�n de AFD a AFD Minimizado por el algoritmo de Minimizaci�n */
        
        AFDMin afdMin = Minimizacion.getAFDminimo(afd);
        
        /* Imprimir la Tabla de Transicion del AFD Minimizado */
        
        System.out.println();
        System.out.printf("Tabla de transici�n del AFD Minimizado:\n");
        TablaTransicion tabla = afdMin.getAfdPostMinimizacion().getTablaTransicion();
       
        for (int i=0; i < tabla.getColumnCount(); i++){
            System.out.printf("%s\t\t", tabla.getColumnName(i));
        }
       
        System.out.println();
        for (int i=0; i < tabla.getRowCount(); i++) {
            for (int j=0; j < tabla.getColumnCount(); j++) {
                System.out.printf("%s\t\t", tabla.getValueAt(i, j));
            }
            System.out.println();
        }
    }
}
