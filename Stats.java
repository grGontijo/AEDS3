package AEDS3;

public class Stats {
    private int sum;
    private int quantidade_registro;

    public Stats() {
        sum = 0;
        quantidade_registro = 0;
    }

    public void somar(int tam) {
        sum += tam;
        quantidade_registro += 1;
    }

    public float mediaTamRegistro() {
        return (float) sum / quantidade_registro;
    }

    public static short teto(short tam) {
        return (short) Math.ceil(tam * 1.35f);
    }
}
