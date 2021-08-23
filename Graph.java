import java.util.ArrayList;
import java.util.List;

// undirected graph
public class Graph extends Digraph {
	
  public Graph() {
		super();
  }
  
  @Override
	public void add_arc( Integer id1, Integer id2) {
		System.out.println("Operação não permitida: Adição de arco.");
	}
		
	public void del_edge(Integer id1, Integer id2) {
		Vertex v1 = vertex_set.get( id1 );
		Vertex v2 = vertex_set.get( id2 );
		v1.nbhood.remove(v2.id);
		v2.nbhood.remove(v1.id);
	}
	
	public void remove_vertices_isolados() {
		Object[] vertices = vertex_set.values().toArray(); // Itera pelos vertices
		for(Object vertice:vertices) {
			Vertex v1 = (Vertex) vertice;
			if(v1.degree()==0) {
				vertex_set.remove(v1.id);
			}
		}
	}

	public List<Vertex> fleury(Digraph original) { // Algoritmo principal

		List<Vertex> circuito_euleriano = new ArrayList<Vertex>(); // lista que vai conter os vertices em ordem

		Vertex v1 = vertex_set.get(1); // vou começar no vertice 1 (assumindo que ele existe) mas poderia ser qualquer outro
		circuito_euleriano.add(original.vertex_set.get(1)); // adiciono o vertice correspondente a esse no grafo original
		// print(); //debug

		int quantidade_vizinhos = -1; //inicio com um valor absurdo
		while(quantidade_vizinhos!=0) { //se o vertice nao tem vizinho, significa que terminei o algoritmo
			int indice=0;
			quantidade_vizinhos = v1.degree();
			Object[] vizinhos = v1.nbhood.values().toArray(); // array para iterar pelos vizinhos
			for(Object vizinho : vizinhos) { // para cada vizinho
				Vertex v2 = (Vertex) vizinho;
				indice++;
				del_edge(v1.id, v2.id); //removo a aresta para o vizinho
				if(!this.is_connected() && indice!=quantidade_vizinhos) { //removi uma aresta de corte quando não era a única opção, vou reverter
					this.add_edge(v1.id, v2.id); // restauro a aresta removida
				}
				else { // removi uma aresta certa
					circuito_euleriano.add(original.vertex_set.get(v2.id)); // adiciono o novo vertice
					if(v1.degree()==0) { // vertice isolado
						vertex_set.remove(v1.id); // removo o vertice se ele estiver isolado
					}
					v1 = v2; // o vertice principal passa a ser v2
					break; // quebro a iteração pelos vizinhos pois ja achei o que eu queria
				}
			}
		}

		System.out.println("\n\nCircuito Euleriano: "); // imprime o circuito conforme o enunciado
		for(Vertex vertice:circuito_euleriano) {
			System.out.printf("%d ",vertice.id);
		}

		return circuito_euleriano; // retorna a lista com os vertices em ordem para possíveis usos futuros

	}
		
}
