import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;

//Author:Pablo Gonzalez Martinez
//Language: Java 
//Runtime Environment:Java-SE 1.8 (Java-SE 1.8.0_73)
//Class to represent a Tower of Hanoi problem
public class HanoiProblem extends Problem {
	
	//The starting node for the problem
	private Node startNode;
	
	//The number of disks
	private int disks;
	
	//The number of pegs
	private int pegs;
	
	//
	public ArrayList<int[]> permutationsList;
	
	//Constructor for the HanoiProblem class, it generates a new problem with  pegsIN pegs and disks in disks
	//pegs must be bigger than 2 for the problem to be solvable
	public HanoiProblem(String name,int pegsIN, int disksIN ) {
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
		//create the starting node
		startNode = new Node(startState,this);
		
		//create the permutation list for the number of pegs
		ArrayList<Integer> pegArray=new ArrayList<Integer>();
			for(int i=0;i<pegs-1;i++){
				pegArray.add(i);
			}
		permutationsList=generatePermutations(pegArray);
				
		
	}



	//This method returns all possible legal actions that can be performed to a parent node
	//legal actions are moving the topdisk of any peg into a peg that is either empty 
	//or has a larger top disk 
	//The format of action strings is originpeg>targetpeg, where originpeg and targedpeg are the indices of the pegs
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
	
	
	// This method finds the height of the top disk in a peg
	// Gets a peg as a list of integers, returns the index of the first non zero disk
	//If the peg is empty, returns -1
	public int getTopDisk(int[] peg){
		//Start checking from the top
		for(int i=0; i<peg.length;i++){
			if (peg[i]!=0){
				//as soon as you find a disk (non 0 value) return the index
				return i;
			}
		}
		//if no disk was found return -1
		return -1;
	}
	
	//Takes a node and an action and executes the action on the node
	// Returns the chils node resulting from executing the action on the parent
	// Actions are to be given in the format originpegIndex>targetpegIndex
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
		//Erase the disk from the origin peg 
		newState[origin][originTop]=0;
		//Create the target node
		Node child= new Node(parent,action,newState,this,parent.getCost()+1);
		return child;
	}

	//This method tests if a node is a solution
	//The goal peg is the last peg in the representation, all disks must be in it arranged from smaller to larger
	public Boolean testGoal(Node targetNode) {
		int[][] state=targetNode.getState();
		//assume the state is a solution
		Boolean correct=true;
		//start at 0 for the last disk
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
	public Node getStartNode() {
		// TODO Auto-generated method stub
		return startNode;
	}



	//This method takes two nodes and checks whether they have equivalent states
	//Two states are equivalent if any permutation of the non-goal pegs gives the same distribution of disks for both
	public boolean equivalentNodes(Node node1, Node node2) {
		int[][] state1=node1.getState();
		int[][] state2=node2.getState();

			boolean equivalent=true;
			for (int i=0;i<pegs && equivalent ;i++){
				for(int j=0;j<disks && equivalent;j++){
					if(state1[i][j]!=state2[i][j]){
						equivalent=false;
					}
				}
			}
			if(equivalent){
				return true;
			}
		
		
//		for (int[]permutation : permutationsList){
//			boolean equivalent=true;
//			for (int i=0;i<pegs-1 && equivalent ;i++){
//				for(int j=0;j<disks && equivalent;j++){
//					if(state1[i][j]!=state2[permutation[i]][j]){
//						equivalent=false;
//					}
//				}
//			}
//			if(equivalent){
//				return true;
//			}
//		}
		return false;
	}
	
	//A recursive method to generate all possible permutations of the non goal pegs
	public ArrayList<int[]> generatePermutations(ArrayList<Integer> items){
		
		ArrayList<int[]> allPermutations=new ArrayList<int[]>();
		if (items.size()>1){
			//iterate over all values in the element list
			for (int i=0;i<items.size() ; i++){
				//copy the list and remove the value
				ArrayList<Integer>listWithout=(ArrayList<Integer>) items.clone();
				listWithout.remove(i);
				//generate all permutations of the list without that element
				ArrayList<int[]>truncatedPermutations=generatePermutations(listWithout);
				for (int[] partial:truncatedPermutations){
					//generate all permutations that start with that value by prepending it to the permutations without it
					int[] newPerm= new int[partial.length+1];
					newPerm[0]=items.get(i);
					for(int ind=0;ind<partial.length;ind++){
						newPerm[ind+1]=partial[ind];
					}
					//add the new permutation
					allPermutations.add(newPerm);
				}
			}
		}else{
			//Base case, the only permutation of only one element is just the element
			int[]onlyPermutation={items.get(0)};
			allPermutations.add(onlyPermutation);
		}
		return allPermutations;
	}



	public int getDisks() {
		return disks;
	}
	
	
	//Prints a state in a human readable format
	public void printState(int[][] state, PrintStream outfile) {
		//print the top of the pegs (aesthetic)
		String line="";
		for(int j=0;j<pegs;j++){
			line+=" | ";
		}
		outfile.println(line);
		//print the disks and empty spaces
		//traverse rows first
		for(int i=0;i<disks;i++){
			//start a new line
			line="";
			for(int j=0;j<pegs;j++){
				//add disk numbers if available (non 0)
				if (state[j][i]!=0){
					line+= " "+ state[j][i] + " ";
				}else{
					//add empty peg if disk is not there
					line+= " | ";
				}
			}
			outfile.println(line);
		}
		//print the bases (aesthetic)
		line="";
		for(int j=0;j<pegs;j++){
			line+=" _ ";
		}
		outfile.println(line);
	}



	@Override
	public double getEstimate(Node node) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
