/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acuario;

import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Joaqu
 */

/**
 *
 */
public class Persona implements Runnable {

    private int idPersona;
    private AcuarioNatural acuario;
    private boolean paseAlmorzar;
    private boolean paseMerendar;
    private int asignacionDelGomon;

    public Persona(int id, AcuarioNatural recursoCompartido) {
        idPersona = id;
        acuario = recursoCompartido;
        paseMerendar=true;
        paseAlmorzar =true;
    }

    @Override
    public void run() {
        
        while (true) {
            try {
                acuario.irAlParqueParticlarOColectivo(this);
            } catch (InterruptedException ex) {
                Logger.getLogger(Persona.class.getName()).log(Level.SEVERE, null, ex);
            }
            acuario.ingresar(this);
            acuario.actividadesDelParque(this);
            acuario.salir(this);
        }
    }

    public int getId() {
        return idPersona;
    }

    public void setId(int idPersona) {
        this.idPersona = idPersona;
    }

    public boolean getPaseAlmorzar() {
        return paseAlmorzar;
    }

    public void setPaseAlmorzar(boolean puedeAlmorzar) {
        this.paseAlmorzar = puedeAlmorzar;
    }
    public boolean getPaseMerendar() {
        return paseMerendar;
    }

    public void setPaseMerendar(boolean puedeMerendar) {
        this.paseMerendar = puedeMerendar;
    }

  
    public int getAsignacionDelGomon() {
        return asignacionDelGomon;
    }

    public void setAsignacionDelGomon(int asignacionDelGomon) {
        /*
        cuando se sube se le asigna el correspondiente
        */
        this.asignacionDelGomon = asignacionDelGomon;
    }

    
    
}
