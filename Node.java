//Author:Pablo Gonzalez Martinez
//Language: Java 
//Runtime Environment:Java-SE 1.8 (Java-SE 1.8.0_73)

//This class represents a node in the search tree
//Each instance of a Node includes:
// parent, The node that generated this one
// action: The action applied to the parent node to generate it
// state: a representation of the problem state
// cost: the cost up to this node
// estimated: the estimated cost to the solution

public class Node {
	private int cost;
	private double estimateh;
	private Node parent;
	private int[][] state;
	private String action;
	private Problem problem;
	private Boolean root;
	private int depth;
	 
	//Constructor for non root nodes
	public Node (Node parentIN,String actionIN,int[][] stateIN,Problem problemIN,int costIN ){
		setParent(parentIN);
		setAction(actionIN);
		setState(stateIN);
		setRoot(false);
		setCost(costIN);
		this.problem = problemIN;
		setEstimateh(problem.getEstimate(this));
		setDepth(parent.getDepth()+1);
	}
	//Constructor for root nodes
	public Node (int[][] stateIN,Problem problemIN ){
		setRoot(true);
		setParent(null);
		setAction(null);
		setState(stateIN);
		setCost(0);
		setDepth(0);
		this.problem = problemIN;
		setEstimateh(problem.getEstimate(this));
	}
	
	public Node[] generateSuccessorList (){
		Node[] list = problem.generateSuccessorList (this);
		
		return list;
		
		
	}
	
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public double getEstimateh() {
		return estimateh;
	}
	public void setEstimateh(double d) {
		this.estimateh = d;
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Boolean getRoot() {
		return root;
	}
	public void setRoot(Boolean root) {
		this.root = root;
	}
	public int[][] getState() {
		return state;
	}
	public void setState(int[][] state) {
		this.state = state;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public double getFvalue(){
		return cost+estimateh;
	}
	
	
	
	
}
