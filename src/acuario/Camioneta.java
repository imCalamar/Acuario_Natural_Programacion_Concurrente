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
public class Camioneta implements Runnable {

    /*
    hilo encargado de llevar las pertenencias de las personas al final de la carrera
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

    public Camioneta(CarreraPorElRio c) {
        this.c = c;
    }
    @Override
    public void run() {
        while (true) {
            llevarPertenencias();
            depositarPertenencias();
        }
    }
    public void llevarPertenencias() {
        try {
            c.getCarrera().await();
        } catch (InterruptedException | BrokenBarrierException e) {}
        System.out.println(ANSI_CYAN+"***LA CAMIONETA SE ENCUENTRA LLEVANDO LAS PERTENENCIAS***"+ANSI_RESET);
    }
    public void depositarPertenencias() {
        try {
            c.carrera.await();
        } catch (InterruptedException | BrokenBarrierException e) {}
        System.out.println(ANSI_CYAN+"***LA CAMIONETA DEPOSITO LAS PERTENENCIAS***"+ANSI_RESET);
    }
}