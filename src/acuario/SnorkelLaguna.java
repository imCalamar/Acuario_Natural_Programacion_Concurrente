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
public class SnorkelLaguna {
    private Reloj hora;
    private Semaphore semEntrar;
    private Semaphore mutexPedirEquipo;//
    private Semaphore semAsistente;//
    private Semaphore semSnorkel;
    private Semaphore semSalvavidas;
    private Semaphore semParDePatasDeRana;
    
    public SnorkelLaguna(Reloj h ){
        hora=h;
         
        semEntrar=new Semaphore(15);
        mutexPedirEquipo=new Semaphore(2);//
        semAsistente=new Semaphore(0);
        semSnorkel=new Semaphore(0);
        semSalvavidas=new Semaphore(0);
        semParDePatasDeRana=new Semaphore(0);
    }
    
    public void entrarAlStand(Persona p){
        if(hora.getHora()<=18){
                try {
                    this.semEntrar.acquire();
                    System.out.println("la persona "+p.getId()+" esta entrando al stand ");
                    pedirEquipoDeSnorkel(p);
                    
                } catch (InterruptedException e) {}
        }else{
            System.out.println("el Snorkel esta cerrado"+p.getId()+ " se va");
        }
    }
    public void pedirEquipoDeSnorkel(Persona p){
        try {
            this.mutexPedirEquipo.acquire();
            System.out.println("la persona "+p.getId()+" pide un equipo de snorkel ");
            this.semAsistente.release();
            this.semSnorkel.acquire();
            this.semSalvavidas.acquire();
            this.semParDePatasDeRana.acquire();
            
            this.mutexPedirEquipo.release();
            nadarEnElLago(p);
        } catch (InterruptedException e) {}
    }
    public void nadarEnElLago(Persona p){
        try {
            System.out.println("la persona "+p.getId()+" esta nadando en el lago");
            Thread.sleep(3000);
            System.out.println("la persona "+p.getId()+" termino de nadar en el lago y devuelve el equipo");
            
            this.semSnorkel.release();
            this.semSalvavidas.release();
            this.semParDePatasDeRana.release();
            Thread.sleep(1000);
            
            this.semEntrar.release();
        } catch (InterruptedException e) {}
        System.out.println("la persona "+p.getId()+" dejo elquipo y se va a otras atracciones");      
    }
    public void darEquipoDeSnorkel(Asistente a){
        try {
            this.semAsistente.acquire();
            
            this.semSnorkel.release();
            this.semSalvavidas.release();
            this.semParDePatasDeRana.release();
        } catch (InterruptedException e) {}        
            System.out.println("el asistente "+a.getId()+" le da el equipo a la persona");
            
            
    }
}
