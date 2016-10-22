import java.util.ArrayList;

public class NpuzzleProblem extends Problem {
	
	public static final String UP_ACTION= "up";
	public static final String DOWN_ACTION= "down";
	public static final String RIGHT_ACTION= "right";
	public static final String LEFT_ACTION= "left";
	//The size of the puzzle represented by the number of pieces
	//An 8 puzzle for instance has order 8 and the gap is the tile with
	//the number 9 on it
	private int order;
	private int side;
	private Node startNode;
	
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
	
	
	public Boolean testGoal (Node targetNode){
		Boolean correct=true;
		int[][] state = targetNode.getState();
		int lastTile=state[0][0];
		for (int i=0;i<state.length;i++){
			for (int j=0; j<state[i].length;j++){
				int newTile=state[j][i];
				if (newTile<lastTile){
					correct=false;
				}
				lastTile=newTile;
			}
		}
		return correct;
	}
	
	public Node[] generateSuccessorList(Node parent){
		int[][] parentState = parent.getState();
		//Find the position of the gap (the tile with order+1 )
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
		
		//Initialize the response list and generate the child nodes
		Node[] succesorList=new Node[actions.size()];
		for(int i=0;i<actions.size();i++){
			succesorList[i]=executeAction(parent,actions.get(i));
		}
		
		return succesorList;
		
	}
	
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


	public Node getStartNode() {
		return startNode;
	}
	
	public void printState(int[][] state){
		for(int j=0;j<side;j++){
			String line="|";
			for(int i=0;i<side;i++){
				line+= " "+state[i][j]+ " ";
			}
			line+="|";
			System.out.println(line);
		}
		
	}
	

}
