/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utiles;

import java.util.ArrayDeque;

/**
 *
 */
public class Cola {
    private ArrayDeque c;

    public Cola() {
        this.c = new ArrayDeque(16);
    }

    public boolean poner(Object elem) {
        /*
        addLast dispara NullPointerException si el elemento es null
        por eso se controla en el if
        ArrayDeque no tiene restricciones de capacidad
         */
        boolean resultado = true;

        if (elem == null) {
            resultado = false;

        } else {
            c.addLast(elem);
        }

        return resultado;
    }

    public Object obtener() {
        /*
        devuelve el primer elemento sin sacarlo
        si la cola se encuentra vacia devuelve null
         */
        return (c.peek());
    }

    public Object sacar() {
        /*
        obtiene y saca el primer elemento de la cola
        si la cola se encuentra vacia devuelve null
         */
        return (c.pollFirst());
    }

    public boolean seEncuentra(Object buscado) {
        boolean resultado = false;
        if (c.contains(buscado)) {
            resultado = true;
        }
        return resultado;
    }

    public boolean esVacia() {
        return (c.isEmpty());
    }

    public int cantElts() {
        /*
        devuelve la cantidad de elementos ingresados
         */
        return (c.size());
    }

    public static void main(String[] args) {
        Cola c = new Cola();
        for (int i = 1; i <= 5; i++) {
            c.poner(i);
            System.out.println("cant elementos: " + c.cantElts());
        }
        if (c.poner(null)) {
            System.out.println("se coloco");
        } else {
            System.out.println("el programa sigue");
        }
        for (int i = 1; i <= 5; i++) {
            System.out.println("Se retiro " + c.obtener());
            c.sacar();
            System.out.println("cant elementos: " + c.cantElts());
        }
        System.out.println("se retiro " + c.sacar());

    }

}
