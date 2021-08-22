import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.FileReader;
import java.io.BufferedReader;

public class Digraph {
	// vertex set
    protected HashMap< Integer, Vertex> vertex_set;
    protected int time;
    private Boolean acyclic;

    public Digraph() {
        vertex_set = new HashMap< Integer, Vertex>();
    }

    public void print() {
        System.out.printf("\n\nDados do Grafo, grau máximo %d", this.max_degree());

        for( Vertex v : vertex_set.values())
            v.print();

        if( this.is_undirected() )
            System.out.println("\nNão direcionado: verdadeiro");
        else
            System.out.println("\nNão direcionado: falso");
    }
    
	public void open_text( String arq_ent ) {
		String thisLine = null;
        vertex_set = new HashMap<Integer,Vertex>();
		String pieces[ ];

		try {
		    FileReader file_in = new FileReader( arq_ent );
		    BufferedReader br1 = new BufferedReader( file_in );
		    while ( (thisLine = br1.readLine( )) != null) {
			    // retira excessos de espaços em branco
			    thisLine = thisLine.replaceAll("\\s+", " ");
			    pieces = thisLine.split(" ");
			    int v1 = Integer.parseInt( pieces[0] );
			    this.add_vertex( v1 );
			    for( int i = 2; i < pieces.length; i++ ) {
   					int v2 = Integer.parseInt( pieces[ i ] );
   					// pode ser a primeira ocorrência do v2
					this.add_vertex( v2 );
					this.add_arc( v1, v2 );
				}
		    }       
			System.out.print("Arquivo lido com sucesso.");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
    
    public void add_vertex( int id ) {
        if ( id < 1 || this.vertex_set.get( id ) == null ) {
            Vertex v = new Vertex( id );
            vertex_set.put( v.id, v );
            reset();
        }
        else
            System.out.println("Id inválido ou já utilizado!");
    }
    
    public void add_arc( Integer id1, Integer id2) {
        Vertex v1 = vertex_set.get( id1 );
        Vertex v2 = vertex_set.get( id2 );
        if ( v1 == null || v2 == null ) {
            System.out.println("Vértice inexistente!");
            return;
        }
        v1.add_neighbor( v2 );
        reset();
    }

    public void add_warc( Integer id1, Integer id2, Integer weight ) {
		add_arc( id1, id2 );
		Vertex v1 = vertex_set.get(id1);
		if( v1 != null)
			v1.add_weight( id2, weight );
    }

    public void add_edge( Integer id1, Integer id2) {
        Vertex v1 = vertex_set.get( id1 );
        Vertex v2 = vertex_set.get( id2 );
        if ( v1 == null || v2 == null ) {
            System.out.printf("Vértice inexistente!");
            return;
        }
        v1.add_neighbor( v2 );
        v2.add_neighbor( v1 );
        reset();

// ou
//        add_arc( id1, id2 );
//        add_arc( id2, id1 );
    }
    
    public void del_vertex( int id ) {
        for( Vertex v1 : vertex_set.values()) {
			v1.nbhood.remove( id );
        }
        vertex_set.remove( id );
        reset();
    }


    // maximum outdegree
    public int max_degree() {
        int max = -1;
        for( Vertex v1 : vertex_set.values()) {
            if( v1.degree() > max )
                max = v1.degree();
		}
        return max;
    }

	public void del_edge(Integer id1, Integer id2) {
		Vertex v1 = vertex_set.get( id1 );
		Vertex v2 = vertex_set.get( id2 );
		v1.nbhood.remove(v2.id);
		v2.nbhood.remove(v1.id);
	}




  public boolean is_undirected() {
        for( Vertex v1 : vertex_set.values()) {
            for( Vertex v2 : v1.nbhood.values()) {
                if (v2.nbhood.get(v1.id) == null)
                    return false;
            }
        }
        return true;
  }

	protected void reset() {
		acyclic = null;
		time = 0;
		for( Vertex v1 : vertex_set.values() )
			v1.reset();
	}

    // O(m)
    public void BFS( Integer id_root ) {
			reset();
        Vertex root = vertex_set.get( id_root );
        root.dist = 0;

        Queue<Vertex> q1 = new LinkedList<Vertex>();
        q1.add( root );
        Vertex current;

        while ((current = q1.poll()) != null) {
            for( Vertex neig : current.nbhood.values() ) {
                if( neig.dist == null ) {
                    neig.discover( current );
                    q1.add( neig );
                }
            }
        }
    }

	public boolean is_connected() {
		BFS(1);
		for(Vertex vertice:vertex_set.values()) {
			if(vertice.dist==null) { // Se algum vertice nao for alcançavel pela raiz, então o grafo não é conexo
				return false;
			}
		}
		return true;
  }

	public boolean contem_vertice_de_grau_impar() {
		for(Vertex vertice:vertex_set.values())
			if(vertice.degree() % 2 != 0)
				return true;
		return false;
	}





	
	public List<Vertex> fleury() {

		List<Vertex> circuito_euleriano = new ArrayList<Vertex>();

		Vertex v1 = vertex_set.get(1); // vou começar no vertice 1
		circuito_euleriano.add(v1); // adiciono esse vertice
		print();

		while(true) {
			int indice=0;
			System.out.println(v1.id);
			int quantidade_vizinhos = v1.degree();
			System.out.println("quantidade_vizinhos: "+quantidade_vizinhos);
			if(quantidade_vizinhos==0) {//terminei o algoritmo
				break;
			}
			Object[] vizinhos = v1.nbhood.values().toArray();
			for(Object vizinho : vizinhos) {
				Vertex v2 = (Vertex) vizinho;
				indice++;
				del_edge(v1.id, v2.id);
				if(!this.is_connected() && indice!=quantidade_vizinhos) { //removi uma aresta que nao é conexa e nao é a ultima, vou reverter
					System.out.println("removi uma aresta que nao é conexa e nao é a ultima, vou reverter"+v1.id+v2.id);
					this.add_edge(v1.id, v2.id);
				}
				else {
					circuito_euleriano.add(v2);
					if(v1.degree()==0) {
						vertex_set.remove(v1.id);
					}
					// print();
					v1 = v2;
					break;
				}
			}
		}
		System.out.println("Circuito Euleriano: ");
		for(Vertex vertice:circuito_euleriano) {
			System.out.printf("%d ",vertice.id);
		}

		return circuito_euleriano;
	}

	public void remove_vertices_isolados() {
		Object[] vertices = vertex_set.values().toArray();
		for(Object vertice:vertices) {
			Vertex v1 = (Vertex) vertice;
			if(v1.degree()==0) {
				vertex_set.remove(v1.id);
			}
		}
	}

	public List<Vertex> encontra_circuito_euleriano() {
		this.remove_vertices_isolados();

		if(!this.is_undirected()) {
			System.out.println("\n\nEntrada inválida! esse grafo é direcionado.");
			return null;
		}
		if(!this.is_connected()||this.contem_vertice_de_grau_impar()) {
			System.out.println("\n\nEsse grafo não contem circuito euleriano");
			return null;
		}

		return fleury();

	}
}
