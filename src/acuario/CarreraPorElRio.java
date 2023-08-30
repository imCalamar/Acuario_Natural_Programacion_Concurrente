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
import utiles.Aleatorio;
import static utiles.Aleatorio.intAleatorio;
/**
 *
 * @author Joaqu
 */
/**
 *     clase encargada de la carrera por el rio
 *     MECANISMO DE SINCRONIZACION: SEMAFOROS + BARRERA CICLICA
 */
public class CarreraPorElRio {

    private int cantGomones;
    private Reloj reloj;

    private Semaphore mutexTren;//para generar consistencia entre la cantidad de personas a bordo
    private Semaphore mutexCarrera;//para generar consistencia entre la cantidad de colGomones
    private Semaphore mutexPosicion;
    private Semaphore mutexCantGomones;
    protected CyclicBarrier carrera;//barrera para iniciar la carrera al mismo tiempo
    protected CyclicBarrier retirarPertenencias;//todos retiran las pertenencias cuando la camioneta llega
    protected CyclicBarrier trenBarrera;//barrera para iniciar el el recorrido del tren

    private CyclicBarrier subirseAlTren;//barrera para esperar 15 pasajeros
    private CyclicBarrier bajarseDelTren;//barrera para esperar 15 pasajeros

    private int cantPersABordoDelTren;
    private int puesto;
    private Gomon[] colGomones;//cantidad de gomones
    private int correspondiente;
    private Semaphore gomonesListos;

    public CarreraPorElRio(int cantGomones, Reloj r) {
        /*
         cantGomones es la cantidad de gomonoes listos para iniciar la carrera,
         no puede ser mayor a la suma de ambos tipos de colGomones
         la barrera se inicializa en cantGomones+1 ya que la caminioneta va a esperar
         a que la carrera se inicie para llevar las pertenencias al final
         */
        this.cantGomones = cantGomones;
        reloj = r;
        this.mutexTren = new Semaphore(1);
        this.mutexCarrera = new Semaphore(1);
        this.mutexPosicion = new Semaphore(1);
        this.mutexCantGomones= new Semaphore(cantGomones-1);
        carrera = new CyclicBarrier(cantGomones +1);//para los cantGomones colGomones listos, 1 para la camioneta
        retirarPertenencias = new CyclicBarrier(cantGomones + 1);
        subirseAlTren= new CyclicBarrier(15+1);//15 para los pasajeros, 1 para el tren
        bajarseDelTren = new CyclicBarrier(15 + 1);//15 para los pasajeros, 1 para la camioneta
        cantPersABordoDelTren = 0;
        //instancio y creo los colGomones
        colGomones = new Gomon[cantGomones];
        for (int i=0; i<colGomones.length; i++) {
            if (i%2==0) {
                colGomones[i]=new Gomon(i+1, 2);//creo un gomon para dos con su id
            } else {
                colGomones[i]=new Gomon(i+1, 1);//creo un gomon para uno con su id
            }
        }
        correspondiente=0;
        puesto=1;
    }
    public void realizarCarrera(Persona p) {
//            if (reloj.getHora() <= 14) {
                try {
                    Thread.sleep(2000);
                    irAlInicioYTomarTransporte(p);
                    llegarAlInicio(p);
                    llegarAlFinal(p);
                }catch (InterruptedException e) {}
//            } else {
//                System.out.println("la persona "+p.getId() + " se va a buscar otra atraccion por que no da el tiempo para la carrera");
//            }
    }
    public void irAlInicioYTomarTransporte(Persona p) throws InterruptedException{
        switch(Aleatorio.intAleatorio(0, 1)){
            case 0:
                System.out.println("la persona "+ p.getId() + " se dirige al inicio EN BICICLETA");
                Thread.sleep(3000);
                break;
            case 1:
                mutexTren.acquire();
                System.out.println(p.getId() + " se subio al tren");
                mutexTren.release();
                try {
                    this.subirseAlTren.await(15, TimeUnit.SECONDS);
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CarreraPorElRio.class.getName()).log(Level.SEVERE, null, ex);
                } catch (BrokenBarrierException ex) {
                } catch (TimeoutException ex) {
                    System.out.println("EL TREN INICIO porque paso el tiempo");
                    this.subirseAlTren.reset();
                }
                try {
                    this.bajarseDelTren.await(15, TimeUnit.SECONDS);
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CarreraPorElRio.class.getName()).log(Level.SEVERE, null, ex);
                } catch (BrokenBarrierException ex) {
                } catch (TimeoutException ex) {
                    this.bajarseDelTren.reset();
                }
                mutexTren.acquire();
                System.out.println(p.getId() + " se bajó del TREN");
                mutexTren.release();    
                break;
            default:
                break;            
        }
    }
    public void llegarAlInicio(Persona p) {
        /*
         una vez llegado al inicio de la carrera las personas guardan sus pertenencias
         */
        this.guardarPertenencias(p);//guarda sus pertenencias
        try {
            this.mutexCantGomones.acquire();
            mutexCarrera.acquire();
            if (colGomones[correspondiente].seEncuentraLleno()) {
                /*si se encuentra lleno se sube a otro gomon*/
                correspondiente++;
            }
            colGomones[correspondiente].subirse(p);
            p.setAsignacionDelGomon(correspondiente);//asigno a la persona en cual gomon se sento para luego poder saber de cual bajarlo
            mutexCarrera.release();
            
            iniciarCarrera(p);
        } catch (InterruptedException e) {}
    }
    public void iniciarCarrera(Persona p){

        try {
            this.carrera.await(20, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Logger.getLogger(CarreraPorElRio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BrokenBarrierException ex) {
            System.out.println(p.getId()+" partio la carrera porque llegaron todos");
        } catch (TimeoutException ex) {
            System.out.println(p.getId()+" partio la carrera porque Paso el tiempo");
            this.carrera.reset();
        } 
    }
    public void llegarAlFinal(Persona p) {
        /*
         una vez llegado al inicio las personas guardan sus pertenencias
         */
        try {
            this.mutexCarrera.acquire();
            if(!colGomones[p.getAsignacionDelGomon()].llegoALaMeta) {
                colGomones[p.getAsignacionDelGomon()].setPuesto(puesto);
                colGomones[p.getAsignacionDelGomon()].llegoALaMeta();
                puesto++;
            }
            colGomones[p.getAsignacionDelGomon()].bajarse(p.getId());
            this.mutexCarrera.release(); 
        } catch (InterruptedException e) {}
        try {
            this.mutexPosicion.acquire();
            if (puesto ==this.correspondiente) {
                //el ultimo  hilo habilita la sig carrera reseteando los valores
                System.out.println("***LLEVANDO GOMONES AL INICIO***");
                correspondiente=0;
            }
            this.mutexPosicion.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(CarreraPorElRio.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.mutexCantGomones.release();
        try {
            this.retirarPertenencias.await(6, TimeUnit.SECONDS);
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(CarreraPorElRio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BrokenBarrierException ex) {
            System.out.println(p.getId()+" partio la carrera porque llegaron todos");
        } catch (TimeoutException ex) {
            System.out.println(p.getId()+" partio la carrera porque Paso el tiempo");
            this.retirarPertenencias.reset();
        }
            this.sacarPertenencias(p);//se va a retirar las pertenencias
    }

    public void guardarPertenencias(Persona p) {
        System.out.println(p.getId() + " guardando sus pertenencias");
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(CarreraPorElRio.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println(p.getId() + " se prepara para la carrera");
    }
    public void sacarPertenencias(Persona p) {
            System.out.println(p.getId() + " retirando sus pertenencias");
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(CarreraPorElRio.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(p.getId() + " se va a disfrutar el resto del acuario");

    }
    public CyclicBarrier getCarrera() {
        return carrera;
    }
    public void setCarrera(CyclicBarrier carrera) {
        this.carrera = carrera;
    }
    public CyclicBarrier getRetirarPertenencias() {
        return retirarPertenencias;
    }
    public void setRetirarPertenencias(CyclicBarrier retirarPertenencias) {
        this.retirarPertenencias = retirarPertenencias;
    }
    public CyclicBarrier getSubirseAlTren() {
        return subirseAlTren;
    }
    public void setSubirseAlTren(CyclicBarrier subirseAlTren) {
        this.subirseAlTren = subirseAlTren;
    }
    public CyclicBarrier getBajarseDelTren() {
        return bajarseDelTren;
    }
    public void setBajarseDelTren(CyclicBarrier bajarseDelTren) {
        this.bajarseDelTren = bajarseDelTren;
    }
    public int getH() {
        return cantGomones;
    }
    public void setH(int h) {
        this.cantGomones = h;
    }
    public Reloj getReloj() {
        return reloj;
    }
    public void setReloj(Reloj reloj) {
        this.reloj = reloj;
    }
    public Semaphore getMutexTren() {
        return mutexTren;
    }
    public void setMutexTren(Semaphore mutexTren) {
        this.mutexTren = mutexTren;
    }
    public Semaphore getMutexCarrera() {
        return mutexCarrera;
    }
    public void setMutexCarrera(Semaphore mutexCarrera) {
        this.mutexCarrera = mutexCarrera;
    }
    public int getCantPersABordoDelTren() {
        return cantPersABordoDelTren;
    }
    public void setCantPersABordoDelTren(int cantPersABordoDelTren) {
        this.cantPersABordoDelTren = cantPersABordoDelTren;
    }
    public int getPuesto() {
        return puesto;
    }
    public void setPuesto(int puesto) {
        this.puesto = puesto;
    }
    public Gomon[] getGomones() {
        return colGomones;
    }
    public void setGomones(Gomon[] gomones) {
        this.colGomones = gomones;
    }
    public int getCorrespondiente() {
        return correspondiente;
    }
    public void setCorrespondiente(int correspondiente) {
        this.correspondiente = correspondiente;
    }
    public Semaphore getGomonesListos() {
        return gomonesListos;
    }
    public void setGomonesListos(Semaphore gomonesListos) {
        this.gomonesListos = gomonesListos;
    }
}
