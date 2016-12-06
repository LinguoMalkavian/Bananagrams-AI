import java.io.PrintStream;
import java.util.ArrayList;

public class NpuzzleProblem extends Problem {
	//Constants for the action commands
	public static final String UP_ACTION= "up";
	public static final String DOWN_ACTION= "down";
	public static final String RIGHT_ACTION= "right";
	public static final String LEFT_ACTION= "left";
	
	//The size of the puzzle represented by the number of pieces
	//An 8 puzzle for instance has order 8 and the gap is the tile with
	//the number 9 on it
	private int order;
	
	//the side of the board (square rood of order+1)
	private int side;
	
	//The starting nod for the problem
	private Node startNode;
	
	//This is the constructor for the NPuzzle Problem class
	//it takes the name, order (number of non empty tiles, the 8 puzzle for example is played on a 3 by 3 grid with one gap)
	// the order must be such that order+1 is a perfect square for the puzzle to be useable
	public NpuzzleProblem(String name, int order, String tileOrder) {
		super(name);
		this.order=order;
		this.side= (int) Math.sqrt(order+1);
		//initialize the state matrix for the start node
		int[][] startState=new int[side][side];
		String[] tiles= tileOrder.split(",");
		int indexTileOrder=0;
		//fill the state matrix with the handed list of tiles
		for (int i=0;i<side;i++){
			for(int j=0;j<side;j++){
				startState[j][i]=Integer.parseInt(tiles[indexTileOrder]);
				indexTileOrder++;
			}
			
		}
		//Create the starting node
		this.startNode=new Node(startState,this);
		
	}
	
	//This method test whether the state of a node is a solution state
	//In the solution state the numbers go in ascending order when read from left to right and top to bottom
	public Boolean testGoal (Node targetNode){
		//assume the state is correct
		Boolean correct=true;
		//get the state
		int[][] state = targetNode.getState();
		//store the last tile(start at 0)
		int lastTile=state[0][0];
		//traverse by rows
		for (int i=0;i<state.length;i++){
			for (int j=0; j<state[i].length;j++){
				//get the new tile
				int newTile=state[j][i];
				if (newTile<lastTile){
					//if the tile number descends the puzzle is incorrect
					correct=false;
				}
				//store the last tile checked
				lastTile=newTile;
			}
		}
		return correct;
	}
	
	//This method returns all possible legal actions that can be performed to a parent node
	//It does so by looking for the gap and then checking which of the positions orthogonally adjacent to it
	//are in the grid, it returns possible actions as a list of strings with the defined constants for up, down, left and right movement of the gap
	public String[] getPossibleActions(Node parent){
		int[][] parentState = parent.getState();
		//Find the position of the gap (the tile with order+1 as its value)
		int[] gapPosition=new int[2];
		for (int i=0; i<side;i++){
			for(int j=0;j<side;j++){
				if (parentState[i][j]==order+1){
					gapPosition[0]=i;
					gapPosition[1]=j;
				}
			}
		}
		
		//Determine which actions are legal in this position:
		//Legal actions are moving the gap to any orthogonally adjacent position that is inside the board
		ArrayList<String> actions =new ArrayList<String>();
		if (gapPosition[0]>0){
			actions.add(LEFT_ACTION+":"+gapPosition[0]+","+gapPosition[1]);
		}
		if (gapPosition[0]<side-1){
			actions.add(RIGHT_ACTION+":"+gapPosition[0]+","+gapPosition[1]);
		}
		if (gapPosition[1]>0){
			actions.add(UP_ACTION+":"+gapPosition[0]+","+gapPosition[1]);
		}
		if (gapPosition[1]<side-1){
			actions.add(DOWN_ACTION+":"+gapPosition[0]+","+gapPosition[1]);
		}
		String[] actionArray= new String[actions.size()];
		actionArray=actions.toArray(actionArray);
		return actionArray;
	}
	
	//This method takes an action string with the coordinates of the gap and a direction to move it
	//The method then performs the specified action on the node handed as input and returns
	//the child node resulting from the operation
	public Node executeAction (Node parent,String action){
		//Unpack the action command
		String [] actionElements= action.split(":");
		String actionKeyword=actionElements[0];
		String[] gapStr= actionElements[1].split(",");
		int[] gapPosition=new int[2];
		gapPosition[0]=Integer.parseInt(gapStr[0]);
		gapPosition[1]=Integer.parseInt(gapStr[1]);
		//initialize the state for the child node
		int[][] parentState = parent.getState();
		
		int[][] newstate = copyState(parentState);
		
		//Choose the action and modify the child state appropriately
		if (actionKeyword.equals(UP_ACTION)){
			newstate[gapPosition[0]][gapPosition[1]]=newstate[gapPosition[0]][gapPosition[1]-1];
			newstate[gapPosition[0]][gapPosition[1]-1]=order+1;
		}
		else if (actionKeyword.equals(DOWN_ACTION)){
			newstate[gapPosition[0]][gapPosition[1]]=newstate[gapPosition[0]][gapPosition[1]+1];
			newstate[gapPosition[0]][gapPosition[1]+1]=order+1;
		}
		else if (actionKeyword.equals(RIGHT_ACTION)){
			newstate[gapPosition[0]][gapPosition[1]]=newstate[gapPosition[0]+1][gapPosition[1]];
			newstate[gapPosition[0]+1][gapPosition[1]]=order+1;
		}
		else if (actionKeyword.equals(LEFT_ACTION)){
			newstate[gapPosition[0]][gapPosition[1]]=newstate[gapPosition[0]-1][gapPosition[1]];
			newstate[gapPosition[0]-1][gapPosition[1]]=order+1;
		}
		//create the new node and return it
		Node childNode= new Node(parent,action,newstate,this,parent.getCost()+1);
		return childNode; 
	}

	//Method to return the starting node
	public Node getStartNode() {
		return startNode;
	}
	
	//Method that takes a state representation and prints it in a human readable format
	//It represents empty spaces with - and fills in the letters in the grid
	//It prints the representation to standard output
	public void printState(int[][] state, PrintStream outfile){
		for(int j=0;j<side;j++){
			//Start each line with the boundary (merely aesthetic)
			String line="|";
			for(int i=0;i<side;i++){
				//add each number to the line (with whitespaces for readability)
				line+= " "+state[i][j] + " ";
			}
			//close the line and print it
			line+="|";
			outfile.println(line);
		}
		
	}

	//This method checks if two nodes have the same state
	//In this problem two nodes have the same state if all tiles are in the same positions
	public boolean equivalentNodes(Node node1, Node node2) {
		int[][] state1= node1.getState();
		int[][] state2= node2.getState();
		for (int i=0;i<state1.length;i++){
			for(int j=0;j<state1[i].length;j++){
				if(state1[i][j]!=state2[i][j]){
					return false;
				}
			}
		}
		return true;
	}

	public int getOrder() {
		return order;
	}

	//returns the calculated value of the heuristic for a node
	//We use the sum of manhattan distances between tiles and their intended place as heuristic
	public double getEstimate(Node node) {
		int[][] state=node.getState();
		int distance=0;
		for(int i =0;i<state.length;i++){
			for(int j=0;j<state[i].length;j++){
				int value=state[i][j];
				int targetX=(value-1)%3;
				int targetY=(value-1)/3;
				distance+=Math.abs(targetX-i)+Math.abs(targetY-j);
			}
		}
		return distance;
	}
	

}
