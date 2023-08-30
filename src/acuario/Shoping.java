/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acuario;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import static utiles.Aleatorio.intAleatorio;
/**
 *
 * @author Joaqu
 */
public class Shoping{
    
    private Semaphore mutexEntrarShoping;
    private Semaphore mutexSalirShoping;
    private Semaphore cajeroA;
    private Semaphore cajeroB;
    Reloj hora;
    
    
    public Shoping(Reloj h) {
        this.mutexEntrarShoping=new Semaphore(1,true);
        this.mutexSalirShoping=new Semaphore(1,true);
        this.cajeroA=new Semaphore(1,true);
        this.cajeroB=new Semaphore(1,true);
        this.hora=h;
    }
    
    public void irAlShoping(Persona p){
        if(hora.getHora()<=17){
            try {
                this.mutexEntrarShoping.acquire();
                System.out.println(p.getId()+ " se encuentra en el shoping");
                Thread.sleep(1000);
                this.mutexEntrarShoping.release();
                comprar(p);
                salir(p);
            } catch (InterruptedException e){}
        }else{
            System.out.println("el SHOPING esta cerrado, la persona "+p.getId()+ " se va");
        }
    }
    public void comprar(Persona p){
            try {
        switch (intAleatorio(0,1)) {
            case 0:
                    this.cajeroA.acquire();
                    System.out.println("la persona "+p.getId()+" esta abonando su compra en el cajero A");
                    Thread.sleep(2000);
                    this.cajeroA.release();
                    break;
            case 1:
                    this.cajeroB.acquire();
                    System.out.println("la persona "+p.getId()+" esta abonando su compra en el cajero B");
                    Thread.sleep(2000);
                    this.cajeroB.release();
                    break;
            default:
                break;
        }
            } catch (InterruptedException ex) {
                Logger.getLogger(Restaurante.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    private void salir(Persona p) {
        //
        try {
            System.out.println("la persona "+p.getId()+" termino de comprar en el shoping");
            Thread.sleep(1000);
            this.mutexSalirShoping.acquire();
            System.out.println(p.getId()+" esta  saliendo del Shoping");
            Thread.sleep(1000);
            this.mutexSalirShoping.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(Restaurante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
