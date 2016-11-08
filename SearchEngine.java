
import java.util.ArrayList;
import java.util.Date;
import java.io.PrintStream;
import java.io.PrintWriter;

public abstract class SearchEngine {

	private ArrayList<Node> closed;
	private ArrayList<Node> open;
	private Problem problem;
	private int maxCandidateSize;
	private int expandedNodes;
	private int depthOfSolution;
	private Date startTime;
	private long timeElapsed=0;
	private int maxDepth;
	
	
	public SearchEngine(Problem prob) {
		this.problem=prob;
		this.setClosed(new ArrayList<Node>());
		this.setOpen(new ArrayList<Node>());
		this.getOpen().add(prob.getStartNode());
		this.maxCandidateSize=0;
		this.expandedNodes=0;
		this.depthOfSolution=Integer.MAX_VALUE;
		this.startTime=new Date();
		
		
	}
	
	public abstract Node remove();
	
	public abstract void insert(Node newnode);
	
	public abstract void improve(Node child);
	
	//Standard loop for search processes, each specific search strategy's
	//functionality is implemented through the remove,insert and improve
	//methods 
	//This method also collects the statistics on expanded nodes
	public Node runSearch(){
		while( ! getOpen().isEmpty()){
			Node expanded=this.remove();
			//this.getProblem().printState(expanded.getState(),System.out);
			int depth=expanded.getDepth();
			this.insert(expanded);
			if (problem.testGoal(expanded)){
				endChrono();
				updateStatistics(depth);
				return expanded;
			}
			Node[] successors=problem.generateSuccessorList(expanded);
			for (int i =0;i<successors.length;i++){
				improve(successors[i]);
			}
			updateStatistics(depth);
			//print progress
			if (expandedNodes%1000==0){
				System.out.println(""+expandedNodes+" nodes expanded.");
			}
		}
		endChrono();
		return null;
	}
	//Takes the finished search and prints everything stored to a file
	public void printResults(String filename,Node finalNode){
		try{
		    PrintStream outfile = new PrintStream(filename, "UTF-8");
		    outfile.println(problem.name);
		    String line="Nodes Expanded:"+ expandedNodes;
		    outfile.println(line);
		    line="Maximum Size of candidate set:"+ maxCandidateSize;
		    outfile.println(line);
		    line="Time elapsed:"+ timeElapsed+"ms";
		    outfile.println(line);
		    line="Maximum Depth searched:"+ maxDepth;
		    outfile.println(line);
		    if(finalNode!=null){
		    	printPath(outfile,finalNode);
		    	line="Final state:";
		    	problem.printState(finalNode.getState(),outfile);
		    }else {
		    	outfile.print("No solution was found");
		    }
		    outfile.close();
		    
		} catch (Exception e) {
		   System.out.println("Error while printing results, please check output parameters" );
		   e.printStackTrace();
		}
	}
	
	public void printPath(PrintStream outfile,Node finalNode){
		String path="";
		int depth=0;
		Node thisNode=finalNode;
		while(!thisNode.getRoot()){
			depth+=1;
			path= thisNode.getAction()+" " + path;
			thisNode=thisNode.getParent();
		}
		
		String line="Depth of solution:"+ depth;
		outfile.println(line);
		line="Path to solution:";
		outfile.println(line);
		outfile.println(path);
	}
	
	public void updateStatistics(int depth){
		if (getOpen().size()>maxCandidateSize){
			maxCandidateSize=getOpen().size();
		}
		if(depth>maxDepth){
			maxDepth=depth;
		}
		expandedNodes++;
	}

	public Node isClosed(Node newNode){
	
		for(Node oldNode : this.getClosed()){
			if (problem.equivalentNodes(oldNode,newNode)){
				return oldNode;
			}
		}
		return null;
	}
	public ArrayList<Node> getOpen() {
		return open;
	}

	public void setOpen(ArrayList<Node> open) {
		this.open = open;
	}

	public ArrayList<Node> getClosed() {
		return closed;
	}

	public void setClosed(ArrayList<Node> closed) {
		this.closed = closed;
	}
	
	public Problem getProblem() {
		return problem;
	}
	

	//Method to start counting the time
	public void startChrono(){
		Date now=new Date();
		startTime=now;
	}
	
	
	//Method to stop counting the time and store the total time of the search in the timeElapsed atribute  
	public void endChrono(){
		Date now=new Date();
		timeElapsed=now.getTime()-startTime.getTime();
	}
	
	
	
	
}
