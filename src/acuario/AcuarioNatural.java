/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acuario;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import static utiles.Aleatorio.intAleatorio;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utiles.Aleatorio;
/**
 *
 * @author Joaqu
 */
/*
el acuario se encuentra abierto de 9 a 17
los cupones que se dan en la entrada son implementados como dos booleanos
 */
public class AcuarioNatural {
    protected CarreraPorElRio carrera; //espera 5 gomones e inicia
    private Restaurante[] restaurantes;// 3 restaurantes
    private Reloj r;
    private Semaphore mutexIngresar;//semaforo para controlar el ingreso al acuario
    private Semaphore mutexColectivo;
    private Semaphore mutexParticular;
    private Semaphore mutexSalir;
    protected FaroToboganes faro;
    protected SnorkelLaguna snorkel;
    protected PiletaNadoConDelfines[] colPiletaNadoDelfines;
    private CyclicBarrier colectivoBarrera;
    private CyclicBarrier finColectivoBarrera;
    protected Shoping shoping;

    public AcuarioNatural(Reloj r, CarreraPorElRio c) {
        mutexColectivo=new Semaphore(0, true);//el primer permiso lo da RelojManagement
        mutexParticular=new Semaphore(0, true);//el primer permiso lo da RelojManagement
        mutexIngresar= new Semaphore(0, true);//el primer permiso lo da RelojManagement
        colectivoBarrera=new CyclicBarrier(26);
        finColectivoBarrera=new CyclicBarrier(26);
        this.r = r; //reloj
        carrera =c;
        mutexSalir = new Semaphore(1, true);
        /*
        creo los distintos restaurantes
         */
        restaurantes = new Restaurante[3];
        
        restaurantes[0] = new Restaurante("Americano", 3, r);
        restaurantes[1] = new Restaurante("Japones", 4, r);
        restaurantes[2] = new Restaurante("Aleman", 5, r);
        /*
        creo el faro
         */
        faro= new FaroToboganes(r); 
        /*
        creo el lago Snorkel
         */
        snorkel= new SnorkelLaguna(r);
        /*
        creo las piletas con delfines
         */
        colPiletaNadoDelfines=new PiletaNadoConDelfines[4];
        for (int i = 0; i < colPiletaNadoDelfines.length; i++) {
            colPiletaNadoDelfines[i] = new PiletaNadoConDelfines(i, r);//creo cada pileta condelfines con su correspondiente id y recurso compartido
        }
        /*
        creo el shoping
         */
        shoping=new Shoping(r);
        /*
         *
         */
    }
    public void irAlParqueParticlarOColectivo(Persona p) throws InterruptedException{
        /*
        
        */
        switch(Aleatorio.intAleatorio(0, 1)){
            case 0:
                mutexParticular.acquire();
                System.out.println(p.getId() + " se dirige al Parque de forma particular");
                mutexParticular.release();
                try {
                Thread.sleep(6000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AcuarioNatural.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case 1:
                mutexColectivo.acquire();
                System.out.println(p.getId() + " se subio al Colectivo");
                mutexColectivo.release();
                try {
                    this.colectivoBarrera.await(8, TimeUnit.SECONDS);
//                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CarreraPorElRio.class.getName()).log(Level.SEVERE, null, ex);
                } catch (BrokenBarrierException ex) {
                } catch (TimeoutException ex) {
                    System.out.println("EL COLECTIVO ARRANCO porque paso el tiempo");
                    this.colectivoBarrera.reset();
                }
                try {
                    this.finColectivoBarrera.await(5, TimeUnit.SECONDS);
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CarreraPorElRio.class.getName()).log(Level.SEVERE, null, ex);
                } catch (BrokenBarrierException ex) {
                } catch (TimeoutException ex) {
                    this.finColectivoBarrera.reset();
                }
                mutexColectivo.acquire();
                System.out.println(p.getId() + " se bajó del COLECTIVO");
                mutexColectivo.release();  
                break;
            default:
                break;            
        }
    }
    public void ingresar(Persona p) {
        try {
            mutexIngresar.acquire();
            System.out.println(p.getId()+ " INGRESO AL ACUARIO " + r.obtenerHora());
            p.setPaseAlmorzar(true);
            p.setPaseMerendar(true);
//            ingresaron++;
            mutexIngresar.release();
        } catch (InterruptedException e){}
    }
    public void salir(Persona p) {
        try {
            mutexSalir.acquire();
            System.err.println(p.getId()+ " SALIO DEL ACUARIO a las " + r.obtenerHora());
//            ingresaron--;
            mutexSalir.release();
        } catch (InterruptedException e) {
        }
    }
    public void actividadesDelParque(Persona p) {
        Restaurante restaurante;
        do {
            switch (intAleatorio(5,5)) {
                case 1:
                    //Faro Mirador con vista a 4 m de altura y descenso en tobogán
                    faro.subirEscaleras(p);
                    break;
                case 2:
                    //Carreras de Gomones por el Rio
                    carrera.realizarCarrera(p);
                    break;
                case 3:
                    //ir al restaurante
                    restaurante=elegirRestaurante(p.getId());
                    restaurante.irARestaurante(p);
                    break;
                case 4:
                    //ir al nadar con snorkel
                    snorkel.entrarAlStand(p);
                    break;
                case 5:
                    // ir al nad con delfines
//                    if (r.getHora()>=9 && r.getHora()<=12) {
                    quererNadarConDelfines(p);
                        
//                    }
                    break;
                case 6:
                    // ir al shoping 
                    shoping.irAlShoping(p);
                    break;
                default:
                    break;
            }
        } while(r.getHora()>=9 && r.getHora()<=18);
    }
    public Restaurante elegirRestaurante(int idPersona) {
        //elige un restaurante 
        return restaurantes[intAleatorio(0, 2)];
    }
    public void quererNadarConDelfines(Persona p){
        //se le asiginauna pileta, pero antes se averigua si alguna de las  piletas esta disponible 
        PiletaNadoConDelfines nadocondefines;

            if(hayPiletaDisponible()){
                nadocondefines=asignarPileta();
                nadocondefines.entrarAlaPileta(p);
            }else{
                System.out.println("no hay piletas disponibles, la eprsona "+p.getId()+" va a hacer otras actividades");
            }     
    }
    public boolean hayPiletaDisponible(){
        //busca si ha alguna pieta disponible
        boolean res=false;
        int n=0;
        while(!res && n!=3){
            if(this.colPiletaNadoDelfines[n].getCapMax()!=10){
                res=true;  
            }else{
                n++;
            }   
        }
        System.out.println("ahu pileta");
        return res;
    }
    public PiletaNadoConDelfines asignarPileta () {
        //se le asigna la pileta al hilo persona
        PiletaNadoConDelfines nd=null;
        boolean encontroLugar=false;
        int n=0;
        while(!encontroLugar){
            if(this.colPiletaNadoDelfines[n].getCapMax()!=10){
                
                this.colPiletaNadoDelfines[n].asignarCupo();
                
                nd=colPiletaNadoDelfines[n];
                encontroLugar=true;
            }else{
                n++;
            }   
        }
        return nd;
    }
    public PiletaNadoConDelfines asignarPiletaParaElDelfin () {
        //se le asigna una pileta al hilo delfin
        PiletaNadoConDelfines nd=null;
        boolean res=false;
        int n=0;
        while(!res){
            if(this.colPiletaNadoDelfines[n].getCapDeDelfines()==2){
                n++;
            }else{
                this.colPiletaNadoDelfines[n].asignarDelfin();
                nd=colPiletaNadoDelfines[n];
                res=true;
            }   
        }
        return nd;
    }
    public void abrirAcuario() {
        //metodo del hilo Relojero encargado de abrir el acuario dando los permisos
        try {
            System.out.println("***EL ACUARIO ACABA DE ABRIR***");
            Thread.sleep(1000);
            mutexIngresar.release();
            mutexParticular.release();
            mutexColectivo.release();
        } catch (InterruptedException e) {}
    }
    public void cerrarAcuario() {
        //metodo del hilo Relojero encargado de abrir el acuario dando los permisos
        try {
            System.out.println("***AH CERRADO EL ACUARIO***");
            Thread.sleep(1000);
            mutexIngresar.acquire();
            mutexParticular.acquire();
            mutexColectivo.acquire();
            this.mutexSalir.release();
        } catch (InterruptedException e) {
        }
    }
    public CyclicBarrier getColectivoBarrera(){
        return colectivoBarrera;
    }
    public CyclicBarrier getFinColectivoBarrera(){
        return finColectivoBarrera;
    }
//    public boolean hayColectivosDisponible(){
//        boolean res=false;
//        int n=0;
//        while(!res && n!=2){
//            if(this.colPiletaNadoDelfines[n].getCapMax()!=10){
//                res=true;  
//            }else{
//                n++;
//            }   
//        }
//        return res;
//    }
}
