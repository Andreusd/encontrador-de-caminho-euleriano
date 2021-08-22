public class AlgGrafos {
    public static void main(String args[]) {
        
        Digraph g1 = new Digraph(); // Cria o grafo

        String arq_ent = "myfiles/grafo01.txt"; // Nome do arquivo

        g1.open_text( arq_ent ); // Abre o arquivo

        g1.encontra_circuito_euleriano();

    }
}
