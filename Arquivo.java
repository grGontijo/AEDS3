package AEDS3;

import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;

public class Arquivo<T extends interfaceObjeto> {
    final String nomeArq = "arquivoPrincipal";
    RandomAccessFile arq;
    Deletados deletados;
    Constructor<T> construtor;
    final int SKIP_METADADO = tamInt();
    Stats stats;

    public int tamInt() {
        return Integer.SIZE / 8; // integer.size retorna em bits, por isso divide-se por 8.
    }

    public Arquivo(Constructor<T> construtor) {
        try {
            arq = new RandomAccessFile(nomeArq + ".txt", "rw");
            deletados = new Deletados(nomeArq);
            // nesse momento a classe deletados lerá o arquivo de deletados, e
            // jogará as infomações presentes para memoria principal.
            this.construtor = construtor;
            if (arq.length() <= 0) {
                arq.seek(0);
                arq.writeInt(0);
            }
            stats = new Stats();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public int create(T obj) throws Exception {
        arq.seek(0);
        int id_criado = arq.readInt() + 1; // visto que o inteiro lido é o ultimo ID criado, por isso somamos +1.
        obj.setId(id_criado);
        byte[] ba = obj.toByteArray();
        // agora preciso encontrar a posição para inserir meu registro:
        long pos = deletados.posReaproveitada((short) ba.length);
        if (pos == -1) {
            arq.seek(arq.length());
            arq.writeShort((short) ba.length);
        } else {
            arq.seek(pos);
        }
        arq.write(ba);
        arq.seek(0);
        arq.writeInt(id_criado);

        stats.somar(ba.length);
        return id_criado;
    }

    public boolean delete(int id) throws Exception {
        boolean CHECK_deletado = false;
        arq.seek(SKIP_METADADO); // salta-se o metadado que indica o último id craido. Obs.: na
        // implementação a lápide é indicada através do índice NEGATIVO.
        int id_lido;
        short tamReg;
        long tamArq = arq.length();

        while (arq.getFilePointer() < tamArq) {

            tamReg = arq.readShort();
            id_lido = arq.readInt();

            if (id_lido == id) {
                arq.seek(arq.getFilePointer() - tamInt());
                // retornando os 4 bytes que foram "avançados" pela
                // referência ao ler o id (int).
                long posDeletada = arq.getFilePointer();

                arq.writeInt(id * -1);
                CHECK_deletado = true;

                deletados.inserir(tamReg, posDeletada);
                arq.seek(tamArq); // para encerrar o while, evitando a utilização do comando "break".
            } else {
                arq.skipBytes(tamReg - tamInt());
            }
        }

        return CHECK_deletado;
    }

    public boolean update(T obj) throws Exception {
        boolean CHECK_atualizado = false;
        arq.seek(SKIP_METADADO);
        int id = obj.getId();
        long tamArq = arq.length();

        short tamReg;
        int id_lido;
        while (arq.getFilePointer() < tamArq) {
            tamReg = arq.readShort();
            id_lido = arq.readInt();

            if (id_lido == id) {

                // """ Achei que essa seria a solução mais eficiente quanto ao não "
                // disperdício" de bytes, visto que mesmo que o registro atualizado ainda caiba
                // na posição atual, talvez exista um espaço/"lixo" em que possa haver
                // menos/nenhum
                // disperdício de bytes. E partindo do ponto que essa
                // "análise" de uma posição ideal ocorrerá através de uma busca em memória
                // PRINCIPAL de uma lista ORDENADA, creio eu que não será lenta, valendo a
                // pena esta implementação! """

                byte[] ba = obj.toByteArray();
                arq.seek(arq.getFilePointer() - 4);

                if ((short) ba.length == tamReg) { // se o registro não aprensetar "lixo", certamente, esta posição já é
                                                   // a melhor
                                                   // possível!
                    arq.write(ba);
                } else {
                    deletados.inserir(tamReg, arq.getFilePointer());
                    arq.writeInt(id * -1);
                    long pos = deletados.posReaproveitada((short) ba.length);// O interessante desta implementação é que
                                                                             // mesmo que o registro mantenha o mesmo
                                                                             // tamanho, talvez tenha algum espaço no
                                                                             // arquivo principal onde
                                                                             // apresente menos "lixo" sendo uma
                                                                             // posição mais "otimizada". Com isso,
                                                                             // aproveitamos o update para tentar
                                                                             // otimizar ainda mais nosso conjunto de
                                                                             // registros checkando na nossa arvore de
                                                                             // deletados!
                    if (pos == -1) {
                        arq.seek(arq.length()); // se retornar -1 é porque foi considerado mais vantajoso
                                                // inserir no final, visto que o "lixo" que seria deixado
                                                // nas posições "disponíveis" seria considerado alto na
                                                // minha implementação, ou não tenha posição disponível para esse
                                                // tamanho de registro na árvore de deletados!
                                                // No primeiro momento talvez seria mais interessante ocupar algum
                                                // espaço
                                                // disponível, mesmo havendo um "grande lixo", mas creio eu que a "longo
                                                // prazo"
                                                // esta impletanção tornará o arquivo principal mais otimizado - quanto
                                                // ao
                                                // espaço de momória secundária!
                        arq.writeShort(ba.length);
                    } else {
                        arq.seek(pos);
                    }
                    arq.write(ba);
                }

                arq.seek(tamArq); // encerrar o while, evitando o uso do "break".
                CHECK_atualizado = true;
            } else {
                arq.skipBytes(tamReg - tamInt());
            }
        }
        return CHECK_atualizado;
    }

    public T read(int id) throws Exception {
        T obj_lido = null;
        arq.seek(SKIP_METADADO);

        short tamReg;
        int id_lido;
        long tamArq = arq.length();
        while (arq.getFilePointer() < tamArq) {
            tamReg = arq.readShort();
            id_lido = arq.readInt();
            if (id_lido == id) {
                arq.seek(arq.getFilePointer() - tamInt());
                byte[] ba = new byte[tamReg];
                arq.read(ba);
                obj_lido = construtor.newInstance();
                obj_lido.fromByteArray(ba);
                arq.seek(tamArq); // encerrar o while, sem utilizar o "break".
            } else {
                arq.skipBytes(tamReg - tamInt());
            }
        }
        return obj_lido;
    }

    public void close() throws Exception {
        // deletados.imprimirArvore();
        deletados.close();
        arq.close();
    }
}