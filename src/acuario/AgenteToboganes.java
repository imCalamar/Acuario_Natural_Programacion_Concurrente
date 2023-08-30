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
/*
hilo encargado de asignarle un tobogan a los visitantes
*/
public class AgenteToboganes implements Runnable { 
    protected FaroToboganes fb; 
    
    public AgenteToboganes(FaroToboganes fb){   
        this.fb = fb;    
    }

    @Override
    public void run() {
        while(true){
            fb.agenteAdministrarTobogan();
        }
    }
}
