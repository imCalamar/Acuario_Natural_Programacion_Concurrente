/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acuario;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import utiles.Aleatorio;
import static utiles.Aleatorio.intAleatorio;
/**
 *
 * @author Joaqu
 */
public class FaroToboganes {

    
    private int cantEnEscalera;
    private int cantidadArriba;
    private int porCualTirarse; 
    private boolean tobogan1;
    private boolean tobogan2;
    protected ReentrantLock lock;
    protected Condition tirarseTobogan;
    protected Condition esperaAbajo;
    protected Condition administrarTobogan; //espera del agente
    private Reloj hora;


    public FaroToboganes(Reloj h){
        cantEnEscalera=0;
        cantidadArriba=0;
        porCualTirarse=-1;
        tobogan1=true;
        tobogan2=true;
        lock = new ReentrantLock(true);
        tirarseTobogan = lock.newCondition();
        esperaAbajo = lock.newCondition();
        administrarTobogan = lock.newCondition();
        this.hora=h;
    }
    
    public void subirEscaleras(Persona p){
        if(hora.getHora()<=18){
        lock.lock();
        try {
            while(cantEnEscalera==3){
                this.esperaAbajo.await();
            }
            this.cantEnEscalera++;
        } catch (InterruptedException e) {} 
        finally {
            System.out.println("presona "+p.getId()+" sube escaleras");
            this.cantidadArriba++;
            lock.unlock();
            mirarPaisaje(p);
        } 
        }else{
            System.out.println("esta cerrado, "+p.getId()+" se va");
        } 
    }
    public void mirarPaisaje(Persona p){
        System.out.println("la persona "+p.getId()+" esta en el mirador apreciando la vista");
        try {
            Thread.sleep(1000 *intAleatorio(1,4));
        } catch (InterruptedException ex) {
            Logger.getLogger(FaroToboganes.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("la persona "+p.getId()+" termino de mirar y se dirije a tirarse por los toboganes");
        desenderPorTobogan(p);
    }
    public void desenderPorTobogan(Persona p){
        //
        lock.lock();
        this.cantEnEscalera--;
        esperaAbajo.signal();
        administrarTobogan.signal(); //se quiere tirar
        try {
            while (this.porCualTirarse==0){
                System.out.println("la persona "+p.getId()+" espera para tirarse por algun tobogan");
                this.tirarseTobogan.await();
            }
            if (this.porCualTirarse==1){
                tirarsePorTobogan1(p);
                this.cantidadArriba--;
            } else if (this.porCualTirarse==2) {
                tirarsePorTobogan2(p);
                this.cantidadArriba--;
            }  
        }catch (InterruptedException e) {
        }finally{
            lock.unlock();
        }
    }
    public void tirarsePorTobogan1(Persona p){
        lock.lock();
        System.out.println("la persona "+p.getId()+" se tira por el tobogan 1");
        this.tobogan1=false;
        lock.unlock();
        try {
            System.out.println(p.getId()+" se esta tirando por el tobogan 1");
            Thread.sleep(2000);
            System.out.println("la persona "+p.getId()+" se tiro por el tobogan 1 y le avisa al agente te el tobogan esta disponible");
        } catch (InterruptedException e) {}
        lock.lock();
        this.tobogan1=true;
        this.porCualTirarse=-1;
        lock.unlock();
        this.administrarTobogan.signal();
    }
     public void tirarsePorTobogan2(Persona p){
        lock.lock();
        System.out.println("persona "+p.getId()+" se tira por el tobogan 2");
        this.tobogan2=false;
        lock.unlock();
        try {
            System.out.println("presona "+p.getId()+" se esta tirando por el tobogan 2");
            Thread.sleep(2000);
            System.out.println("persona "+p.getId()+" se tiro por el tobogan 2 y le avisa al agente te el tobogan esta disponible");
        } catch (InterruptedException e) {}
        lock.lock();
        this.tobogan2=true;
        this.porCualTirarse=-1;
        lock.unlock();
        this.administrarTobogan.signal();
    }
    public void agenteAdministrarTobogan(){
        lock.lock();
        try {
            if(!this.tobogan1 && !this.tobogan2){
                this.porCualTirarse=0;
                administrarTobogan.await();
            }
            if(this.tobogan1 &&this.tobogan2 && this.cantidadArriba!=0){
                this.porCualTirarse=Aleatorio.intAleatorio(1, 2);
                System.out.println("agente asigna tobogan al azar");
                this.tirarseTobogan.signal();
            }else if(this.tobogan1 && this.cantidadArriba!=0){
                this.porCualTirarse=1;
                System.out.println("agente asigna tobogan 1");
                this.tirarseTobogan.signal();
            }else if(this.tobogan2 && this.cantidadArriba!=0){
                this.porCualTirarse=2;
                System.out.println("agente asigna tobogan 2");
                this.tirarseTobogan.signal();
            }
            this.administrarTobogan.await();
        } catch (InterruptedException e) {
        }finally{
            lock.unlock();
        }
    }
}