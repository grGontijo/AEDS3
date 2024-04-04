package AEDS3;
import java.util.Scanner;

public class Main {
        public static Scanner sc = new Scanner(System.in);

        public static void main(String[] args) throws Exception {
                Arquivo<Livro> arquivoPrincipal = new Arquivo<>(Livro.class.getConstructor());

                Livro livro1 = new Livro("The data science", "Gabriel Rangel", 12.3f);
                Livro livro2 = new Livro("Te amo", "Julia Godim", 100.50F);

                System.out.printf("Livro criado! ID: %d\n", arquivoPrincipal.create(livro1));
                System.out.printf("Livro criado! ID: %d\n", arquivoPrincipal.create(livro2));

                System.out.println(arquivoPrincipal.delete(1) ? "registro com id: " + 1 + " deletado!"
                                : "Não foi possível deletar!");

                Livro livro3 = new Livro("Joao e o pé de feijão", "Maquiavel", 23.50F);
                System.out.printf("Livro criado! ID: %d\n", arquivoPrincipal.create(livro3));

                Livro livro4 = new Livro("Aventura chile", "Bortot", 89.4F);

                System.out.printf("Livro criado! ID: %d\n", arquivoPrincipal.create(livro4));

                livro4.setAutor("Juliana Bortot qualquer coisa para ficar grande!");
                arquivoPrincipal.update(livro4);

               // exibirMenuTesteLivros();

                arquivoPrincipal.close();
        }

        public static void exibirMenuTesteLivros() {
                System.out.println("============== Livros ==============");
                System.out.println("[1] - Create");
                System.out.println("[2] - Read");
                System.out.println("[3] - Update");
                System.out.println("[4] - Read");
                System.out.println("[5] - Encerrar programa");
                System.out.println("Digite a opção desejada:");
        }

}

/*
 * int pub_in = Integer.parseInt(sc.nextLine());
 * int id;
 * String titulo, autor;
 * float preco;
 * switch (pub_in) {
 * 
 * case 1:
 * 
 * System.out.println("Digite o título do livro:");
 * titulo = sc.nextLine();
 * System.out.println("Digite o autor do livro:");
 * autor = sc.nextLine();
 * System.out.println("Digite o preço do livro:");
 * preco = Float.parseFloat(sc.nextLine());
 * 
 * Livro livro = new Livro(titulo, autor, preco);
 * arquivoPrincipal.create(livro);
 * break;
 * case 2:
 * System.out.println("Digite o id do elemento que quer ler:"); // vou mudar
 * isso quando eu
 * // tiver arquivo indexados
 * // de 2 níveis,
 * // para que o usuario do
 * // programa digite outros
 * // atributos ao invés do id
 * // :)
 * id = Integer.parseInt(sc.nextLine());
 * arquivoPrincipal.read(id);
 * break;
 * 
 * case 3:
 * }
 */