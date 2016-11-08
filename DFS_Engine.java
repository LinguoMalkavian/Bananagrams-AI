import java.io.PrintWriter;
import java.util.ArrayList;

public class DFS_Engine extends SearchEngine {
	
	private int depthCutoff;
	
	public DFS_Engine(Problem prob) {
		//super constructor
		super(prob);
		
		//determine the depth cutoff based on the kind of problem
		if(prob instanceof BananagramsProblem){
			depthCutoff=((BananagramsProblem) prob).getOrder();
		}else if (prob instanceof HanoiProblem){
			int disks=((HanoiProblem) prob).getDisks();
			depthCutoff=(int) Math.pow(2, disks);
		}else if (prob instanceof MazeProblem){
			int width= ((MazeProblem)prob).getWidth();
			int height= ((MazeProblem)prob).getHeight();
			depthCutoff=width*height;
		}else if (prob instanceof NpuzzleProblem){
			//estimation
			int order=((NpuzzleProblem)prob).getOrder();
			depthCutoff=order*order;
		}else{
			//arbitrary
			depthCutoff=100;
		}
		
	}

	//Takes the next node to expand from the top of the pile
	public Node remove() {
		Node nodeToExpand= this.getOpen().remove(0); 
		return nodeToExpand;
	}

	//puts node into visited list
	public void insert(Node newnode) {
		this.getClosed().add(newnode);
	}

	//Check if new nodes had already been visited and insert them at the top of the pile of they had not
	public void improve(Node child) {
		Node visited=this.isClosed(child);
		if(visited==null && child.getDepth()<=depthCutoff){
			this.getOpen().add(0,child);
		}
		
	}

}
