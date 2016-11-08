import java.util.ArrayList;

public class BFS_Engine extends SearchEngine {

	public BFS_Engine(Problem prob) {
		super(prob);
		// TODO Auto-generated constructor stub
	}

	@Override
	//Takes the next node to expand from the front of the queue
	public Node remove() {
		Node nodeToExpand= this.getOpen().remove(0); 
		return nodeToExpand;
	}

	@Override
	//inserts previously expanded node onto the closed list
	public void insert(Node newnode) {
		this.getClosed().add(newnode);
		
	}

	//add new node to the top of the pile
	public void improve(Node child) {
		
		Problem thisProblem= this.getProblem();
		Node visited=isClosed(child);
		if(visited==null){
			this.getOpen().add(child);
		}
		
	}

}
