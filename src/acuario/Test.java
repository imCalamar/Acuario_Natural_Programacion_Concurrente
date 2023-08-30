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
 * testeo del programa
 */
public class Test {

    public static void main(String[] args) {
        
        Reloj r = new Reloj(8);// clase reloj cuyo recurso compartido es la hora que las demas clases llaman
        
        CarreraPorElRio c = new CarreraPorElRio(16, r);//espera a 15 gomones que esten listos
        
        AcuarioNatural acuarioNatural = new AcuarioNatural(r, c);// el acuario natural
        
        Thread[] colPersonas = new Thread[30];//colecion de personasvisitantes que etraran al parque
        
        Thread[] colAsistentes = new Thread[2]; //coleccion de hilos asistentes para el lago con esnorkel

        Thread[] colDelfines = new Thread[8];//coleccion de hilos delfines, dos delfines para cada pileta
        
        Thread relojero = new Thread(new Relojero(r,acuarioNatural));
        relojero.start();//pasa a ejecutarse

        Camioneta camio = new Camioneta(c);
        Thread cam = new Thread(camio);
        cam.start();//pasa a ejecutarse
        
        Tren tren= new Tren(c);
        Thread trenn = new Thread(tren);
        trenn.start();//pasa a ejecutarse
        
        Thread colectivo = new Thread(new Colectivo(acuarioNatural));
        colectivo.start();//pasa a ejecutarse
        
        Thread agente = new Thread(new AgenteToboganes(acuarioNatural.faro));
        agente.start();//pasa a ejecutarse
        
        for (int m = 0; m < colDelfines.length; m++) {
            colDelfines[m] = new Thread(new Delfin(m, acuarioNatural));//creo cada delfin con su correspondiente id y el recurso compartido acuarionatural
            colDelfines[m].start();//pasa a ejecutarse
        }
        for (int j = 0; j < colAsistentes.length; j++) {
            colAsistentes[j] = new Thread(new Asistente(j, acuarioNatural));//creo cada asistente con su correspondiente id y el recurso compartido acuarionatural
            colAsistentes[j].start();//pasa a ejecutarse
        }
        for (int i = 0; i < colPersonas.length; i++) {
            colPersonas[i] = new Thread(new Persona(i, acuarioNatural));//creo cada persona con su correspondiente id y el recurso compartido acuarionatural
            colPersonas[i].start();//pasa a ejecutarse
        }
    }
}
