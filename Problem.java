
public abstract class Problem {
	public String name;
	
	public Problem (String name){
		this.name=name;
	}
	
	
	public abstract Node[] generateSuccessorList (Node parent);
	
	//Takes a node and an action and executes the action on the node
	//Input: parent, the node to execute the action on
	//Input: action a String defining the action
	//Output: the new node
	public abstract Node executeAction (Node parent,String action);
	
	public abstract Boolean testGoal (Node targetNode);
	
	public abstract void printState(int[][] state);
	public abstract Node getStartNode();
	
	
}
