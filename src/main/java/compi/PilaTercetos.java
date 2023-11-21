package compi;

import java.util.Iterator;
import java.util.Stack;

public class PilaTercetos extends Stack<Terceto> {
    private Integer contador;

    public PilaTercetos() {
        super();
        contador = 0;
    }

    public void apilar(Terceto t) {
        this.add(t);
        contador++;
    }

    public Terceto tope() {
        return this.peek();
    }

    @Override
    public Terceto pop() {
        contador--;
        return super.pop();
    }

    public Integer getContador() {
        return contador;
    }

    public void print() {
        System.out.println("Pila de Tercetos:");
        Iterator<Terceto> it = this.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }
    }

    public boolean isEmpty() {
        return this.empty();
    }
}
