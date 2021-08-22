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
	
	public List<Vertex> fleury() {

		List<Vertex> circuito_euleriano = new ArrayList<Vertex>(); // lista que vai conter os vertices em ordem


		Vertex v1 = vertex_set.get(1); // vou começar no vertice 1 (assumindo que ele existe)
		circuito_euleriano.add(v1); // adiciono esse vertice
		print(); //debug

		int quantidade_vizinhos = -1; //inicio com um valor absurdo
		while(quantidade_vizinhos!=0) { //se o vertice nao tem vizinho, significa que terminei o algoritmo
			int indice=0;
			quantidade_vizinhos = v1.degree();
			// System.out.println(v1.id);
			// System.out.println("quantidade_vizinhos: "+quantidade_vizinhos);
			Object[] vizinhos = v1.nbhood.values().toArray(); // array para iterar pelos vizinhos
			for(Object vizinho : vizinhos) { // para cada vizinho
				Vertex v2 = (Vertex) vizinho;
				indice++;
				del_edge(v1.id, v2.id); //removo a aresta para o vizinho
				if(!this.is_connected() && indice!=quantidade_vizinhos) { //removi uma aresta que nao é conexa e nao é a ultima, preciso reverter
					// System.out.println("removi uma aresta que nao é conexa e nao é a ultima, vou reverter"+v1.id+v2.id);
					this.add_edge(v1.id, v2.id);
				}
				else { // removi uma aresta certa
					circuito_euleriano.add(v2); // adiciono o novo vertice
					if(v1.degree()==0) { // vertice isolado
						vertex_set.remove(v1.id); // removo o vertice se ele estiver isolado
					}
					// print();
					v1 = v2; // o vertice principal passa a ser v2
					break; // quebro a iteração pelos vizinhos pois ja achei o que eu queria
				}
			}
		}

		System.out.println("Circuito Euleriano: "); // imprime o circuito conforme o enunciado
		for(Vertex vertice:circuito_euleriano) {
			System.out.printf("%d ",vertice.id);
		}

		return circuito_euleriano; // retorna a lista com os vertices em ordem para possiveis usos futuros

	}
		
}
