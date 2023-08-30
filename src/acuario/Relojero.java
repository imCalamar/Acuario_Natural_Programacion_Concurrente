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
 * hilo se encarga de abrir, cerrar el acuario y de actualizar la hora del reloj
 */
public class Relojero implements Runnable {
    protected AcuarioNatural acuario;
    protected Reloj r;

    public Relojero(Reloj r, AcuarioNatural recComp) {
        this.r = r;
        this.acuario=recComp;
    }
    @Override
    public void run() {
        try {
            while (true) {
                System.err.println("LA HORA ES: " + r.obtenerHora());
                if (r.getHora()==9){
                    acuario.abrirAcuario();
                }
                if (r.getHora()==17){
                    acuario.cerrarAcuario();    
                }
                Thread.sleep(10000);
                r.pasarHora();
            }
        } catch (InterruptedException e){}
    }
}
