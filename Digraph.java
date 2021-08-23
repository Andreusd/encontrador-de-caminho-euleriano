import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.io.FileReader;
import java.io.BufferedReader;

public class Digraph { // grafo direcionado
    protected HashMap<Integer, Vertex> vertex_set; // Armazena os vertices do grafo

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
    
	public void open_text( String arq_ent ) { // parser de arquivos para grafo
		String thisLine = null;
		String pieces[ ];

		try {
		    FileReader file_in = new FileReader( arq_ent );
		    BufferedReader br1 = new BufferedReader( file_in );
		    while ( (thisLine = br1.readLine( )) != null) {
			    // retira excessos de espaços em branco
			    thisLine = thisLine.replaceAll("\\s+", " ");
			    pieces = thisLine.split(" ");
			    int v1 = Integer.parseInt( pieces[0] );
                if(vertex_set.get(v1)==null) // evitar aviso desnecessario de vertice ja existente
			        this.add_vertex( v1 );
			    for( int i = 2; i < pieces.length; i++ ) {
   					int v2 = Integer.parseInt( pieces[ i ] );
   					// pode ser a primeira ocorrência do v2
                    if(vertex_set.get(v2)==null) // evitar aviso desnecessario de vertice ja existente
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

	public Graph subjacente() { // obtem o grafo subjacente 

		Graph g2 = new Graph();		
		for( Vertex v1 : this.vertex_set.values()) {
				g2.add_vertex( v1.id );
		}
		
		for( Vertex v1 : this.vertex_set.values()) {
				for( Vertex v2 : v1.nbhood.values()) {
						g2.add_edge(v1.id, v2.id);
				}
		}
		return g2;
}


  public boolean is_undirected() { // determina se o grafo é não direcionado
        for( Vertex v1 : vertex_set.values()) {
            for( Vertex v2 : v1.nbhood.values()) {
                if (v2.nbhood.get(v1.id) == null)
                    return false;
            }
        }
        return true;
    }

	protected void reset() {
		for( Vertex v1 : vertex_set.values() )
			v1.reset();
	}

    // O(m)
    public void BFS( Integer id_root ) { // Busca em largura
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

	public boolean is_connected() { // essa função executa uma busca em largura para descobrir se o grafo é conexo
		BFS(1); // a busca pode ser em qualquer vertice, vou assumir que existe o vertice 1
		for(Vertex vertice:vertex_set.values()) {
			if(vertice.dist==null) { // Se algum vertice nao for alcançavel pela raiz, então o grafo não é conexo
				return false;
			}
		}
		return true;
    }

	public boolean contem_vertice_de_grau_impar() { // essa função determina se há algum vertice de grau ímpar
		for(Vertex vertice:vertex_set.values()) // itera pelos vertices
			if(vertice.degree() % 2 != 0) // se algum vertice tiver grau impar, retorna verdadeiro
				return true;
		return false;
	}


	public List<Vertex> encontra_circuito_euleriano() {
		if(!this.is_undirected()) { // O enunciado diz para considerar um grafo não-direcionado
			System.out.println("\n\nEntrada inválida! esse grafo é direcionado.");
			return null;
		}

		Graph g2 = this.subjacente(); // crio um grafo subjacente novo para não alterar o original

		g2.remove_vertices_isolados(); // removo os vertices isolados, pois eles nao alteram o circuito euleriano (nao tem arestas)

		if(!g2.is_connected()) { // para conter um circuito euleriano, todos os vertices não isolados precisam ser conexos
			System.out.println("\n\nEsse grafo não contêm circuito euleriano pois há vertice de grau não-nulo isolado"); 
			return null;
		}

        if(g2.contem_vertice_de_grau_impar()) { // para conter um circuito euleriano, todos os vertices devem ter grau par
			System.out.println("\n\nEsse grafo não contem circuito euleriano pois algum vertice tem grau ímpar"); 
			return null;
        }

		return g2.fleury(this);

	}
}
