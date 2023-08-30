/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acuario;

import java.util.concurrent.BrokenBarrierException;
/**
 *
 * @author Joaqu
 */
public class Tren implements Runnable {
    /*
    hilo encargado de llevar las personas al inicio de la carrera
     */
    private CarreraPorElRio c;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    
    public Tren(CarreraPorElRio c) {
        this.c = c;
    }
    
    @Override
    public void run() {
        while (true) {
            arrancarRecorrido();
            finalizarRecorrido();
        }
    }   
    public void arrancarRecorrido() {
        try {
            c.getSubirseAlTren().await();
        } catch (InterruptedException | BrokenBarrierException ex) {}
        System.out.println(ANSI_GREEN+"***EL TREN INICIO EL RECORRIDO***"+ANSI_RESET);
    }
    public void finalizarRecorrido() {
        try {
            c.getBajarseDelTren().await();
        } catch (InterruptedException | BrokenBarrierException ex) {}
        System.out.println(ANSI_GREEN+"***EL TREN LLEGO A DESTINO***"+ANSI_RESET);
    }   
}
