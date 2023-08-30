/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acuario;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Joaqu
 */
public class PiletaNadoConDelfines {
    
    protected CyclicBarrier espectaculo;
    protected CyclicBarrier finEspectaculo;
    private Semaphore mutexEntrarPileta;
    private Semaphore mutexSalirPileta;
    Reloj reloj;
    private int cantMaxGrupo;
    private int cantDelfinesEnPileta;
    int id;

    public PiletaNadoConDelfines(int id, Reloj reloj) {
        this.mutexEntrarPileta=new Semaphore(1);
        this.mutexSalirPileta=new Semaphore(1);
        this.espectaculo=new CyclicBarrier(12);
        this.finEspectaculo=new CyclicBarrier(12);
        this.reloj=reloj;
        this.id=id;
        this.cantMaxGrupo=0;
        this.cantDelfinesEnPileta=0;
    }
    public void entrarAlaPileta(Persona p){
        if(reloj.getHora()<=18){
            try {
                    System.out.println("lapersona "+p.getId()+"entro al grupo del acuario "+ id);
                    Thread.sleep(1000);
                    
                    iniciarEspectauloDelfines(p);
                    terminarEspectauloDelfines(p);

            }catch (InterruptedException e) {}
        }else{
            System.out.println("esta cerrado, "+p.getId()+" se va");
        } 
    }
    
    public void iniciarEspectauloDelfines(Persona p){
         try {
            this.espectaculo.await(20, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Logger.getLogger(CarreraPorElRio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BrokenBarrierException ex) {
//            System.out.println(p.getId()+" el espectaculo de delfines comienza porque llegaron todos");
        } catch (TimeoutException ex) {
//            System.out.println(p.getId()+"el espectaculo de delfines comienza porque ya paso el tiempo");
            this.espectaculo.reset();
        } 
    }
    public void nadarConDlefines(Persona p){
        System.out.println("La persona "+p.getId()+" esta nadando con los delfines");
        try {
            Thread.sleep(8500);//como 10000 seria una hora en el reloj tomaria 8500 como unos 45 min
        } catch (InterruptedException ex) {
            Logger.getLogger(PiletaNadoConDelfines.class.getName()).log(Level.SEVERE, null, ex);
        }
        terminarEspectauloDelfines(p);
    }
    public void terminarEspectauloDelfines(Persona p){
        try {
            this.finEspectaculo.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Logger.getLogger(CarreraPorElRio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BrokenBarrierException ex) {
        } catch (TimeoutException ex) {
            this.finEspectaculo.reset();
        }
        try {
            this.mutexSalirPileta.acquire();
            System.out.println("temino el nado con los delfines, la persona "+p.getId()+" seva a realizar otras acividades");
            this.cantMaxGrupo--;
            Thread.sleep(1500);
        } catch (InterruptedException ex) {
            Logger.getLogger(PiletaNadoConDelfines.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.mutexSalirPileta.release();
    }
    
    public int getCapMax() {
        return this.cantMaxGrupo;
    } 
    public void asignarCupo() {
        try {
            this.mutexEntrarPileta.acquire();
            this.cantMaxGrupo++;
        } catch (InterruptedException ex) {
            Logger.getLogger(PiletaNadoConDelfines.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.mutexEntrarPileta.release();
    }
    public int getCapDeDelfines() {
        return this.cantDelfinesEnPileta;
    }
    public void asignarDelfin() {
        try {
            this.mutexEntrarPileta.acquire();
            this.cantDelfinesEnPileta++;
        } catch (InterruptedException ex) {
            Logger.getLogger(PiletaNadoConDelfines.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.mutexEntrarPileta.release();
    }
    public CyclicBarrier getEspectaculo() {
        return espectaculo;
    }
    public CyclicBarrier getfinEspectaculo() {
        return finEspectaculo;
    }
}
