package compi;

public class Terceto {
    private String operador;
    private Integer operando1, operando2;
    private boolean esEtiqueta1, esEtiqueta2;

    public Terceto(String operador, Integer operando1, Integer operando2, boolean esEtiqueta1, boolean esEtiqueta2) {
        this.operador = operador;
        this.operando1 = operando1;
        this.operando2 = operando2;
        this.esEtiqueta1 = esEtiqueta1;
        this.esEtiqueta2 = esEtiqueta2;
    }

    public String getOperador() {
        return operador;
    }

    public Integer getOperando1() {
        return operando1;
    }

    public Integer getOperando2() {
        return operando2;
    }

    public boolean esEtiqueta1() {
        return esEtiqueta1;
    }

    public boolean esEtiqueta2() {
        return esEtiqueta2;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public void setOperando1(Integer operando1, boolean esEtiqueta1) {
        this.operando1 = operando1;
        this.esEtiqueta1 = esEtiqueta1;
    }

    public void setOperando2(Integer operando2, boolean esEtiqueta2) {
        this.operando2 = operando2;
        this.esEtiqueta2 = esEtiqueta2;
    }

    @Override
    public String toString() {
        String op1, op2;
        op1 = operando1.toString();
        op2 = operando2.toString();
        if (esEtiqueta1)
            op1 = "[" + operando1 + "]";
        if (esEtiqueta2)
            op2 = "[" + operando2 + "]";
        return "(" + operador + ", " + op1 + ", " + op2 + ")";
    }
}
