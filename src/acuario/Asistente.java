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
hilo encargado de dar el equipo de snorkel a los visitantes
*/
public class Asistente implements Runnable {

    private int idAsistente;
    private AcuarioNatural acuario;

    public Asistente (int id, AcuarioNatural recursoCompartido) {
        idAsistente = id;
        acuario = recursoCompartido;
    }

    @Override
    public void run() {
        while (true) {
            acuario.snorkel.darEquipoDeSnorkel(this);
        }
    }
    public int getId() {
        return idAsistente;
    }
}
