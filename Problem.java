import java.io.PrintStream;


//Author:Pablo Gonzalez Martinez
//Language: Java 
//Runtime Environment:Java-SE 1.8 (Java-SE 1.8.0_73)
//Abstract class to represent a Problem
public abstract class Problem {
	public String name;
	
	public Problem (String name){
		this.name=name;
	}
	//This method copies a state matrix into a new state matrix
	//the method only works the the array is non empty
	public int[][] copyState(int[][] original){
		int width= original.length;
		int length= original[0].length;
		int[][] copy= new int[width][length];
		for(int i=0;i<width;i++){
			for(int j=0;j<length;j++){
				copy[i][j]=original[i][j];
			}
		}
		return copy;
	}
	//This method takes a parent node as argument
	//it then gets all legal actions at that node and generates all its successors
	//it returns an array o successor nodes
	public Node[] generateSuccessorList(Node parent) {
		//Get legal moves
		String[] actionList= getPossibleActions(parent);
		
		//Execute the legal moves and store them
		Node[] succesors= new Node[actionList.length];
		for (int i=0;i<actionList.length;i++){
			succesors[i]=executeAction(parent, actionList[i]);
		}
		return succesors;
	}
	//Takes a node and an action and executes the action on the node
	//Input: parent, the node to execute the action on
	//Input: action a String defining the action
	//Output: the new node
	public abstract Node executeAction (Node parent,String action);
	
	public abstract String[] getPossibleActions(Node parent);
	
	public abstract Boolean testGoal (Node targetNode);
	

	public abstract Node getStartNode();
	
	public abstract boolean equivalentNodes(Node node1, Node node2);
	public abstract void printState(int[][] state, PrintStream out);
	public abstract double getEstimate(Node node) ;
	
	

	
	
}
