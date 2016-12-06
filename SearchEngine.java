
import java.util.ArrayList;
import java.util.Date;
import java.io.PrintStream;

//This abstract class implements the methods necessary for all search strategies and interfaces 
//It also acts as an interface to call the functionalities of the different search methods
//Its many functionality is to run a search and collect data on the performance of the search algorythm
public abstract class SearchEngine {
	//A list containing the nodes that have already been explored
	private ArrayList<Node> closed;
	//A list of nodes to explore
	private ArrayList<Node> open;
	//The problem the search is being performed on
	private Problem problem;
	//The maximum size of the candidate set during the search
	private int maxCandidateSize;
	//Th
	private int expandedNodes;
	private int depthOfSolution;
	private Date startTime;
	private long timeElapsed=0;
	private int maxDepth;
	private String strategy;
	
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
		this.startChrono();
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
		Node thisNode=finalNode;
		while(!thisNode.getRoot()){
			path= thisNode.getAction()+" " + path;
			thisNode=thisNode.getParent();
		}
		
		String line="Depth of solution:"+ finalNode.getDepth();
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

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public int getMaxCandidateSize() {
		return maxCandidateSize;
	}

	public int getExpandedNodes() {
		return expandedNodes;
	}

	public int getDepthOfSolution() {
		return depthOfSolution;
	}

	public long getTimeElapsed() {
		return timeElapsed;
	}

	public int getMaxDepth() {
		return maxDepth;
	}
	
	
	
	
}
