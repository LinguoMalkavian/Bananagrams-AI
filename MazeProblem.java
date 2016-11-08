import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;

//Author:Pablo Gonzalez Martinez
//Language: Java 
//Runtime Environment:Java-SE 1.8 (Java-SE 1.8.0_73)
//Class to represent a simple Maze problem

public class MazeProblem extends Problem{
	
	public int width;
	public int height;
	public int[][] grid;
	public Node startNode;
	private int[] targetSpace;
	public static final String UP_ACTION= "up";
	public static final String DOWN_ACTION= "down";
	public static final String RIGHT_ACTION= "right";
	public static final String LEFT_ACTION= "left";
	
	//THis is the constructor for the maze problem
	//It takes the width and height of the maze, an array with the start coordinates
	//an array with the target coordinates and an array of pairs of coordinates for the obstacles
	public MazeProblem(int width, int height, int[]start, int[] finish, int[][] obstacles) {
		super("maze");
		this.width=width;
		this.height=height;
		this.targetSpace=finish;
		//Initialize the grid that represents the maze, a matrix of integers with 0 in free spaces and 1 in barriers
		this.grid=new int[width][height];
		//Place the obstacles in the grid
		for(int i=0;i<obstacles.length;i++){
			grid[obstacles[i][0]][obstacles[i][1]]=1;
		}
		// Create the start state representation (it is a matrix instead of one dimentional because of the specifications of the node class)
		int [][] startstate= new int[1][2];
		startstate[0][0]=start[0];
		startstate[0][1]=start[1];
		//Store the root node
		startNode=new Node(startstate,this);
		
	}

	//Method to execute an action handed via a string on a parent node als handed as parameter
	//The string must be one of the movement constants (up,down,left, right)
	//The method returns a node created from moving in said direction from the position in the parent node
	//The cost of movement is taken to be always 1
	public Node executeAction(Node parent, String action) {
		//get the position in the parent node
		int[][] parentPosition=parent.getState();
		//initialize a new state for the child
		int[][] newPosition=new int[1][2];
		//see which action command was handed in and represent the child state correspondingly
		if(action.equals(UP_ACTION)){
			newPosition[0][0]=parentPosition[0][0];
			newPosition[0][1]=parentPosition[0][1]-1;
		}else if(action.equals(DOWN_ACTION)){
			newPosition[0][0]=parentPosition[0][0];
			newPosition[0][1]=parentPosition[0][1]+1;
		}else if(action.equals(RIGHT_ACTION)){
			newPosition[0][0]=parentPosition[0][0]+1;
			newPosition[0][1]=parentPosition[0][1];
		}else if(action.equals(LEFT_ACTION)){
			newPosition[0][0]=parentPosition[0][0]-1;
			newPosition[0][1]=parentPosition[0][1];
		}
		//Create the child node, the cost of movement is 1
		Node child=new Node(parent,action,newPosition,this,parent.getCost()+1);
		
		return child;
	}

	//Method to evaluate which actions are posible in a given state
	//Takes a Node as argument and checks which movements are legal ie do not get out of the grid or into a barrier
	//Returns a list with the actions that can be carried out on the node
	public String[] getPossibleActions(Node parent) {
		int[][] parentPosition=parent.getState();
		//store coordinates for easy reference
		int X=parentPosition[0][0];
		int Y=parentPosition[0][1];
		//Initialize a variable size container for the actions
		ArrayList<String> actions=new ArrayList<String>();
		//Check if each corresponding orthogonally adjacent space is valid to move to (neither out of bounds not a barrier)
		if(X+1<width && grid[X+1][Y]!=1){
			//if the space is valid add the direction to the list of possible actions
			actions.add(RIGHT_ACTION);
		}
		if(Y+1<height && grid[X][Y+1]!=1){
			actions.add(DOWN_ACTION);
		}
		if(Y-1>=0 && grid[X][Y-1]!=1){
			actions.add(UP_ACTION);
		}
		if(X-1>=0 && grid[X-1][Y]!=1){
			actions.add(LEFT_ACTION);
		}
		//Transform the Arraylist into a fixed size array and return it
		String[] actionArray= new String[actions.size()];
		actionArray=actions.toArray(actionArray);
		return actionArray;
	}

	//Method to test whether a node is an end node
	//Takes the node as argument
	//Returns true if and only if the character is in the target position.
	public Boolean testGoal(Node targetNode) {
		int [][]position=targetNode.getState();
		if(position[0][0]==targetSpace[0] && position[0][1]==targetSpace[1]){
			return true;
		}else{
			return false;
		}
	
	}
	
	//Method that takes a state representation and prints out the maze in human readable format
	//0 are empty spaces
	//*is the adventurer
	//1 are barriers
	//& is the goal space
	//It prints the representation to standard output
	public void printState(int[][] state, PrintStream outfile) {
		//Names for easy access to coordinates
		int X=state[0][0];
		int Y=state[0][1];
		//Traverse the array by rows
		for(int j=0;j<grid[0].length;j++){
			//initialize line for the row
			String line="";
			for(int i=0;i<grid.length;i++){
				//chose the character and append it to th line
				if(X==i && Y==j){
					//if the explorer is in the space
					line+="*";
				}else if(i==targetSpace[0]&& j==targetSpace[1]){
					//if this is the target space
					line+="&";
				}
				else{
					//if this is a regular space (empty or barrier)
					line+=grid[i][j];
				}
			}
			//print each line
			outfile.println(line);
		}
		
	}

	//Method to return the starting node
	public Node getStartNode() {
		return startNode;
	}

	//Method that checks if two nodes have equivalent states
	//Two states in this problem are equivalent iff the explorer is in the same position
	public boolean equivalentNodes(Node node1, Node node2) {
		//get the states form both nodes
		int[][] state1=node1.getState();
		int[][] state2=node2.getState();
		if(state1[0][0]==state2[0][0] && state1[0][1]==state2[0][1] ){
			return true;
		}
		return false;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	//gets a heuristic by using the manhattan distance between the point and the target
	public double getEstimate(Node node) {
		int[][] state=node.getState();
		double distance=Math.abs(targetSpace[0] - state[0][0]) + Math.abs(targetSpace[1] -state[0][1]);
		return distance;
	}

}
