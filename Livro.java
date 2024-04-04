package AEDS3;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.text.NumberFormat;

public class Livro implements interfaceObjeto {
    int id;
    String name;
    String autor;
    float preco;

    public Livro() {
        this("", "", 0.0F);
    }

    public Livro(String name, String autor, float preco) {
        this.name = name;
        this.autor = autor;
        this.preco = preco;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public byte[] toByteArray() throws Exception { // "transformar" o objeto em um array de bytes
        ByteArrayOutputStream ba_outs = new ByteArrayOutputStream();/*
                                                                     * vai criar uma Stream (sequência de bytes), no
                                                                     * buffer, para dps criar um array de bytes já
                                                                     * sabendo o tamanho da sequência
                                                                     */
        DataOutputStream d_outs = new DataOutputStream(ba_outs); // funciona como uma extensao da ByteArrayOutputStream

        d_outs.writeInt(id);
        d_outs.writeUTF(name);
        d_outs.writeUTF(autor);
        d_outs.writeFloat(preco);

        return ba_outs.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws Exception { /*
                                                             * atribuir valor aos atributos do objeto através da
                                                             * leitura do array de bytes
                                                             */
        ByteArrayInputStream ba_ins = new ByteArrayInputStream(ba);
        DataInputStream d_ins = new DataInputStream(ba_ins);

        id = d_ins.readInt();
        name = d_ins.readUTF();
        autor = d_ins.readUTF();
        preco = d_ins.readFloat();
    }

    public String toString() {
        return "\nID: " + id + "\nTitulo: " + name + "\nAutor: " + autor + "\nPreço: "
                + NumberFormat.getCurrencyInstance().format(preco);
    }
}
