package compi;
import java.util.ArrayList;

public class Ambito
{
    ArrayList<String> ambitos;

    public Ambito(String ambito)
    {
        ambitos = new ArrayList<>();
        ambitos.add(ambito);
    }
    public Ambito() { ambitos = new ArrayList<>(); }

    public void push(String ambito) { ambitos.add(ambito); }
    public String pop() { return ambitos.remove(ambitos.size() - 1); }
    public String get(int i) { return ambitos.get(i); }
    public boolean isEmpty() { return ambitos.isEmpty(); }
    public Ambito copy() {
        Ambito a = new Ambito();
        for (String s : ambitos) {
            a.push(s);
        }
        return a;
    }
    @Override
    public String toString() {
        String ambito = "";
        for (String a : ambitos) {
            ambito += a + ":";
        }
        if (ambito.contains(":"))
            return ambito.substring(0, ambito.length() - 1);
        return ambito;
    }
}
