/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acuario;

import java.util.concurrent.BrokenBarrierException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joaqu
 */
public class Delfin implements Runnable {

    private int idDelfin;
    private AcuarioNatural acuario;

    public Delfin(int id, AcuarioNatural recursoCompartido) {
        idDelfin = id;
        acuario = recursoCompartido;
    }
    @Override
    public void run() {
        PiletaNadoConDelfines nadocondelfines;
        nadocondelfines=this.acuario.asignarPiletaParaElDelfin();
        while(true) {
            delfinPreparado(nadocondelfines, this);
            terminarDelfines(nadocondelfines,this);
        }
    }
    public void delfinPreparado (PiletaNadoConDelfines nadocondelfines, Delfin d){
         try {
            nadocondelfines.getEspectaculo().await();
            System.out.println("***EL DELFIN "+d.idDelfin+" COMIENZA CON EL ESPECTACULO EN LA PILETA "+nadocondelfines.id+ "***");
            Thread.sleep(8500);
        } catch (InterruptedException | BrokenBarrierException e) {}
    }
    
    public void terminarDelfines(PiletaNadoConDelfines nadocondelfines,Delfin d){
        try {
           nadocondelfines.getfinEspectaculo().await();
        } catch (InterruptedException ex) {
            Logger.getLogger(CarreraPorElRio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BrokenBarrierException ex) {}
        System.out.println("***EL DELFIN "+d.idDelfin+" TERMINA CON EL ESPECTACULO EN LA PILETA "+nadocondelfines.id+"***");
    }
    public int getIdDelfin() {
        return idDelfin;
    }    
}
