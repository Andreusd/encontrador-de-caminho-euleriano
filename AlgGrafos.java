import java.util.Scanner;

public class AlgGrafos {

    public static void menu(Graph g1) {
        Scanner s1 = new Scanner(System.in);
        String textoMenu = "\n\n0 Sair\n1 Print\n2 Ler o arquivo novamente\n3 Encontrar circuito euleriano novamente";
        while(true) {
            System.out.println(textoMenu);
            int escolha = s1.nextInt();
            switch(escolha) {
                case 0:
                    return;
                case 1:
                    g1.BFS(1);
                    g1.print();
                    break;
                case 2:
                    String arq_ent = "myfiles/grafo01.txt";
                    g1 = new Graph();
                    g1.open_text( arq_ent );
                    break;
                case 3:
                    g1.encontra_circuito_euleriano();
                    break;
            }
        }
    }

    public static void main(String args[]) {
        
        Graph g1 = new Graph(); // Cria o grafo

        String arq_ent = "myfiles/grafo01.txt"; // Nome do arquivo

        g1.open_text( arq_ent ); // Abre o arquivo

        g1.BFS(1); // Necessario para imprimir

        g1.print(); // Imprime o grafo para o usuario

        g1.encontra_circuito_euleriano(); // Executa o algoritmo

        menu(g1);
    }
}
