package compi;

import java.util.Stack;

public class PilaTercetos {
    private Stack<Terceto> pila;
    private Integer contador;

    public PilaTercetos() {
        pila = new Stack<Terceto>();
        contador = 0;
    }

    public void apilar(Terceto t) {
        pila.push(t);
        contador++;
    }

    public Terceto tope() {
        return pila.peek();
    }

    public Terceto pop() {
        contador--;
        return pila.pop();
    }

    public Integer getContador() {
        return contador;
    }

    public void print() {
        System.out.println("Pila de Tercetos:");
        for (Terceto t : pila) {
            System.out.println(t);
        }
    }
}
