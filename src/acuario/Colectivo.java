/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acuario;

import java.util.concurrent.BrokenBarrierException;

/**
 * @author Joaqu
 */
public class Colectivo implements Runnable {
    /*
    hilo encargado de llevar las personas al parque
     */
    private AcuarioNatural acuario;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    
    public Colectivo(AcuarioNatural a) {
        this.acuario = a;
    }
    @Override
    public void run() { 
        while (true) {
            arrancarRecorridoColectivo();
            finalizarRecorridoColectivo();
        }
        
    }
    public void arrancarRecorridoColectivo() {
        try {
            acuario.getColectivoBarrera().await();
        } catch (InterruptedException | BrokenBarrierException ex) {}
        System.out.println(ANSI_CYAN+"***EL COLECTIVO INICIO EL RECORRIDO HACIA EL PARQUE***"+ANSI_RESET);
    }
    public void finalizarRecorridoColectivo() {
        try {
            acuario.getFinColectivoBarrera().await();
        } catch (InterruptedException | BrokenBarrierException ex) {}
        System.out.println(ANSI_CYAN+"***EL COLECTIVO LLEGO AL ESTACIONAMIENTO DESTINADO***"+ANSI_RESET);
    }     
}
