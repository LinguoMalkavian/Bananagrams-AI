import java.util.ArrayList;

//author Pablo Gonzalez Martinez
public class HannoiProblem extends Problem {
	private Node startNode;
	private int disks;
	private int pegs;
	
	public HannoiProblem(String name,int pegsIN, int disksIN ) {
		super(name);
		this.disks=disksIN;
		this.pegs=pegsIN;
		//Initialize the start state with all disks in order on the first peg
		int[][] startState= new int [pegs][disks];
		int disksize=1;
		for (int j=0;j<disks;j++){
			startState[0][j]=disksize;
			disksize++;
		}
		startNode = new Node(startState,this);
		
	}




	public String[] getPossibleActions(Node parent){
		int[][] state=parent.getState();
		ArrayList<String> actions =new ArrayList<String>();
		for(int origin=0;origin<pegs;origin++){
			int originTopInd=getTopDisk(state[origin]);
			//If the peg is empty originTopInd is -1 and moving the top disk from that peg is not authorized
			// we only consider targets if the top disk exists
			if(originTopInd!=-1){
				//Get the size of the top disk
				int originTopDisk=state[origin][originTopInd];
				//check all possible target pegs
				for(int target=0;target<pegs;target++){
					//target peg must be different from origin peg
					if(target!=origin){
						int targetTopInd=getTopDisk(state[target]);
						//If the peg is empty the move is always legal
						if(targetTopInd==-1){
							actions.add(""+origin+">"+target);
						}
						else{
							int targetTopDisk=state[target][targetTopInd];
							//In case of an occupied peg move is only legal if top disk is bigger than the moving disk
							if(targetTopDisk>originTopDisk){
								actions.add(""+origin+">"+target);
							}
						}
					}
				}
			}
		}
		//convert the arraylist into an array 
		String[] actionarray=new String[actions.size()];
		actionarray=actions.toArray(actionarray);
		return actionarray;
	}
	// Gets a peg as a list of integers, returns the index of the first non zero disk
	//If the peg is empty, returns -1
	public int getTopDisk(int[] peg){
		for(int i=0; i<peg.length;i++){
			if (peg[i]!=0){
				return i;
			}
		}
		return -1;
	}
	@Override
	public Node executeAction(Node parent, String action) {
		//Get a copy of the parent node's state
		int[][] newState=copyState(parent.getState());
		//Unpack the action
		String[] actionElements= action.split(">");
		int origin=Integer.parseInt(actionElements[0]);
		int target=Integer.parseInt(actionElements[1]);
		//Find the positions to be modified
		int originTop=getTopDisk(newState[origin]);
		int targetTop=getTopDisk(newState[target]);
		//Move the disk
		if(targetTop!=-1){
			newState[target][targetTop-1]=newState[origin][originTop];
			newState[origin][originTop]=0;
		}else{
			newState[target][disks-1]=newState[origin][originTop];
		}
		newState[origin][originTop]=0;
		//Create the target node
		Node child= new Node(parent,action,newState,this,parent.getCost()+1);
		return child;
	}

	@Override
	public Boolean testGoal(Node targetNode) {
		int[][] state=targetNode.getState();
		Boolean correct=true;
		int lastDisk=0;
		for(int i=0;i<state[pegs-1].length;i++){
			int currentDisk=state[pegs-1][i];
			if (currentDisk==0 || currentDisk<=lastDisk){
				correct=false;
			}
			lastDisk= currentDisk;
		}
		return correct;
	}

	@Override
	public void printState(int[][] state) {
		for(int j=0;j<pegs;j++){
			String line="-";
			for(int i=0;i<disks;i++){
				if (state[j][i]!=0){
					line+= state[j][i];
				}else{
					line+= "-";
				}
			}
			line+="|";
			System.out.println(line);
		}
	}

	@Override
	public Node getStartNode() {
		// TODO Auto-generated method stub
		return startNode;
	}
	
	
	
	
}
