/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acuario;
/**
 *
 * @author Joaqu
 */
/**
 *
 */
public class Gomon {

    public static final String ANSI_GREEN ="\u001b[32m";
    public static final String ANSI_RESET ="\u001b[0m";
    private int id;
    private int capacidad;//va de 1(simple) a 2(doble)
    private int asiento;//indica donde se ubicara
    protected boolean lleno;//indica si ya no le quedan lugares
    protected boolean llegoALaMeta;
    private int puesto;
    private Persona[] lugar; //arreglo para la cantidad de personas en en gomon, 1 o 2
    
    public Gomon(int id, int capacidad) {
        this.id = id;
        this.capacidad = capacidad;
        asiento = 0;
        lleno = false;
        llegoALaMeta = false;
        puesto = 0;
        lugar = new Persona[capacidad];
    }
    public synchronized void subirse(Persona p) {
        lugar[asiento] = p;//la persona se sube al gomon
        asiento++;
        System.out.println( ANSI_GREEN+ p.getId() + " se subio al gomon " + id + " capacidad " + capacidad +ANSI_RESET);
        if (capacidad == asiento) {
            lleno = true;
        }
    }
    public void llegoALaMeta() {
        String ganadores="";
        llegoALaMeta=true;
        if (capacidad==2) {
            for(int i=0; i<2; i++) {
                ganadores+=lugar[i].getId()+", ";
            }
            System.out.println(ganadores+" llegaron en el puesto "+puesto+" en el gomon "+this.id);
        }else{
            System.out.println(lugar[0].getId() +" llego en el puesto "+puesto+" en el gomon "+this.id);
        }
    }
    public synchronized void bajarse(int idPersona) {
        //la persona se sube al gomon
        lleno = false;
        System.out.println("la persona "+idPersona+" se bajo del gomon "+id);
        asiento--;   
    }
//
//    public boolean isLlego() {
//        return llegoALaMeta;
//    }
    public int getPuesto() {
        return puesto;
    }
    public void setPuesto(int puesto) {
        this.puesto = puesto;
    }
    public void setLlego(boolean llego) {
        this.llegoALaMeta = llego;
    }
    public synchronized boolean seEncuentraLleno() {
        return (capacidad == asiento);
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getL() {
        return asiento;
    }
    public void setL(int l) {
        this.asiento = l;
    }
    public int getCapacidad() {
        return capacidad;
    }
    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }
    public synchronized boolean isLleno() {
        return lleno;
    }
    public void setLleno(boolean lleno) {
        this.lleno = lleno;
    }
    public Persona[] getLugar() {
        return lugar;
    }
    public void setLugar(Persona[] lugar) {
        this.lugar = lugar;
    }
}