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
        
        /* Introducción del Alfabeto y de la Expresión Regular de entrada */

        System.out.printf("Introduzca el alfabeto: ");
        Scanner sc = new Scanner(System.in);
        String cadena1 = sc.nextLine();
        Alfabeto alfa = new Alfabeto(cadena1);
        System.out.printf("Introduzca la expresión regular: ");
        String cadena2 = sc.nextLine();
        String er = cadena2;

        /* Imprimir la Tabla de Simbolos */

        System.out.printf("\nTabla de Simbolos: \n");
        AnalizadorSintactico as = new AnalizadorSintactico(alfa, er);
        
        /* Conversión de la entrada a AFN por el algoritmo de Thompson */ 
       
        AFN salida = as.analizar();        
                       
        /* Conversión de AFN a AFD por el algoritmo de Subonjunto */
        
        AFD afd = Subconjuntos.getAFD(salida);
       
        /* Conversión de AFD a AFD Minimizado por el algoritmo de Minimización */
        
        AFDMin afdMin = Minimizacion.getAFDminimo(afd);
        
        /* Imprimir la Tabla de Transicion del AFD Minimizado */
        
        System.out.println();
        System.out.printf("Tabla de transición del AFD Minimizado:\n");
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
