package AEDS3;

import java.io.RandomAccessFile;
import java.util.TreeMap;
import java.util.Map;
import java.io.File;

public class Deletados { // tamanho, posicao
    private String nome;
    private RandomAccessFile arq;
    private TreeMap<Short, Long> arvore; /// dict{tamanho : posicao}

    Deletados(String nomeArqPrincipal) {
        try {
            nome = nomeArqPrincipal + "Deletados";
            arq = new RandomAccessFile(nome + ".txt", "rw");
            arvore = new TreeMap<>();

            if (arq.length() > 0) {
                PreencherArovre();
            }

        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private void PreencherArovre() throws Exception {
        short tamanho;
        long posicao;
        long tamArq = arq.length();

        while (arq.getFilePointer() < tamArq) {
            tamanho = arq.readShort();
            posicao = arq.readLong();
            arvore.put(tamanho, posicao);
        }
    }

    public void inserir(short tam, long pos) throws Exception {
        arvore.put(tam, pos);
        atualizarArquivo();
    }

    public long posReaproveitada(short tamReg) throws Exception {
        if (arvore.isEmpty()) {
            return -1;
        }
        Short teto = Stats.teto(tamReg);
        Short tamMaiorIgual = arvore.ceilingKey(tamReg);

        if (tamMaiorIgual == null || tamMaiorIgual > teto) {
            return -1;
        } else {
            long resp = arvore.get(tamMaiorIgual);
            arvore.remove(tamMaiorIgual);
            atualizarArquivo();
            return resp;
        }
    }

    public void close() throws Exception {
        atualizarArquivo();
        arq.close();
    }

    private void atualizarArquivo() throws Exception {
        arq.close();
        File deletarArquivo = new File(nome + ".txt");
        deletarArquivo.delete();
        arq = new RandomAccessFile(nome + ".txt", "rw");

        for (Map.Entry<Short, Long> no : arvore.entrySet()) {
            arq.writeShort(no.getKey());
            arq.writeLong(no.getValue());
        }
    }

    /*
     * public void imprimirArvore() throws Exception {
     * for (Map.Entry<Short, Long> entry : arvore.entrySet()) {
     * System.out.println("Chave: " + entry.getKey() + ", Valor: " +
     * entry.getValue());
     * }
     * }
     */
}