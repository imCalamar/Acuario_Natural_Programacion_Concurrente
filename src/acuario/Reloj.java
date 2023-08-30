/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acuario;

import java.util.concurrent.Semaphore;
/**
 *
 * @author Joaqu
 */
/*
recurso compartido que tiene la variable hora, se sincroniza con monitores
 */

public class Reloj {

    protected int hora;

    private Semaphore mutex;

    public Reloj(int hora) {
        this.hora = hora;
        mutex = new Semaphore(1,true);
    }

    public void pasarHora() {
        try {
            mutex.acquire();
            hora = (hora + 1)%24;
            mutex.release();
        } catch (InterruptedException e) {
        }
    }

    public int getHora(){
        int salida =0;
        try {
            mutex.acquire();
            salida = hora;
            mutex.release();
        } catch (InterruptedException e) {
        }
        return salida;
    }
    public String obtenerHora() {
        return (this.hora +" hrs");
    }

}
