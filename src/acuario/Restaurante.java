/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acuario;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Joaqu
 */

public class Restaurante {

    private Reloj hora;
    private String nombre;
    private Semaphore puedeEntrar;

    public Restaurante(String id, int capacidadMax,Reloj h) {
        this.nombre=id;
        this.hora=h;
        this.puedeEntrar=new Semaphore(capacidadMax,true);
    }
    public void irARestaurante(Persona p){
        entrarRestaurante(p);
    }
    public void entrarRestaurante(Persona p){
        if(hora.getHora()<=18){
            if(p.getPaseAlmorzar()||p.getPaseMerendar()){
                try {
                    this.puedeEntrar.acquire();
                    System.out.println("la persona "+p.getId()+" esta entrando al restaurante "+nombre);
                    comer(p);
                    salir(p);
                } catch (InterruptedException e) {}
            }else{
               // System.out.println("la persona ya comio y desayuno");
            }  
        }else{
            System.out.println("el restaurante esta cerrado"+p.getId()+ " se va");
        } 
    }
    public void comer(Persona p){
        
        if(p.getPaseMerendar()){
            System.out.println("la persona "+p.getId()+" esta por merendar");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Restaurante.class.getName()).log(Level.SEVERE, null, ex);
            }
                System.out.println(p.getId()+" esta desayunando");
                p.setPaseMerendar(false);
            }else{
            if(p.getPaseAlmorzar()){
                System.out.println("la persona "+p.getId()+" esta por almorzar");
                try {
                    Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Restaurante.class.getName()).log(Level.SEVERE, null, ex);
            }
                System.out.println(p.getId()+" esta almorzando");
                p.setPaseAlmorzar(false);
            }
            this.puedeEntrar.release();
        }
    }
    public void salir(Persona p){
        //
        try {
            System.out.println("persona "+p.getId()+" termino de comer");
            Thread.sleep(1000);
            System.out.println(p.getId()+" esta  saliendo del restaurante");
        } catch (InterruptedException ex) {
            Logger.getLogger(Restaurante.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.puedeEntrar.release();
    }
}
