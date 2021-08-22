import java.util.HashMap;

public class Vertex implements Comparable<Vertex> {
    protected Integer id;
    // outgoing neighbors
    protected HashMap< Integer, Vertex> nbhood;
    protected Vertex parent, root;
    protected Integer dist, d, f;
    // usado na Ordenação topológica
    protected int size;
    // componentes conexas: menor tempo de descoberta alcançavel
    // por no máximo uma aresta de retorno entre os descendentes
    protected Integer low;
    // (id do vizinho, peso do arco)
    protected HashMap< Integer, Integer > arc_weights;

    public Vertex ( int id ) {
		// id >= 1
        this.id = id;
        nbhood = new HashMap<Integer,Vertex>();
        arc_weights = new HashMap<Integer,Integer>();
        parent = null;
        dist = d = null;
    }
 
    public void print() {
        System.out.print("\nId do vértice " + id + ", Vizinhança: " );
        for( Vertex v : nbhood.values())
            System.out.print(" " + v.id );
        if( parent != null)
            System.out.print(", pai " + parent.id + " distância: " + dist );
        else if ( dist == null )
            System.out.print(", não alcançável pela raiz");
        else
            System.out.print(", raiz, distância: " + dist);
    }
    
     // usado na Ordenação topológica
	@Override public int compareTo( Vertex otherVertex ) {
		if( otherVertex.size > this.size)
			return 1;
		else
			return -1;
	}

	protected void reset() {
		parent = null;
		d = null;
		f = null;
		dist = null;
	}

    public void add_neighbor( Vertex viz ) {
        nbhood.put(viz.id, viz);
    }
   
    protected void add_weight( Integer id_nb, Integer weight ) {
        arc_weights.put( id_nb, weight );
    }
    
    public int degree() {
        return nbhood.size();
    }
    
    public void discover( Vertex parent ) {
        this.parent = parent;
        this.dist = parent.dist + 1;
    }

    protected Vertex get_root( ) {
		if( parent == null )
			root = this;
		else
			root = parent.get_root( );
		return root;
	}
}

