
import java.util.ArrayList;
  


public class Astar_Engine extends SearchEngine {

	public Astar_Engine(Problem prob) {
		super(prob);
		this.setStrategy("Astar");
	}

	
	//Takes the next node to expand from the front of the open list
	//Since the list is ordered this is the node with the lowest f value
	public Node remove() {
		Node nodeToExpand= this.getOpen().remove(0); 
		return nodeToExpand;
	}

	// Get the first 
	public void insert(Node newnode) {
		this.getClosed().add(newnode);

	}

	//This method checks a newly generated node to insert it in the corresponding place
	// If the state had not been visited (no equivalent nodes are in closed) the node gets added to open
	// If the state has infinite estimateh, thus making a solution unreachable from there it will not be added
	// If the state had already been visited the node only gets added if it represents a cheaper way to get there
	public void improve(Node child) {
		//check if the node is equivalent to anything in the closed list
		Node visited=this.isClosed(child);
		//if it is has a non infinite h consider it
		if(child.getEstimateh()<Double.POSITIVE_INFINITY){
			if(visited==null ){
				//if the state has not been visited
				binaryInsert(child,this.getOpen());
			}else if (child.getCost()<visited.getCost()){
				//if the state had been visited but at a higher cost remove the older node from closed and add the new one to open
				getClosed().remove(visited);
				binaryInsert(child,this.getOpen());
			}
		}
	}
	
	//This method inserts a new node into the given list in the correct position to keep the list ordered
	public void binaryInsert(Node node,ArrayList<Node> list ){
		double fvalue=node.getFvalue();
		int index=binaryInsertInd(fvalue,list);
		list.add(index,node);
	}
	
	//This method finds the index at which a new node has to be inserted in list to keep the list ordered
	//in ascending order of fvalue
	public int binaryInsertInd(double fvalue,ArrayList<Node> list){
		//base cases
		//if the list is empty or the value to insert has f lower than the first value return 0
		if(list.size()==0 || fvalue<list.get(0).getFvalue()){
			return 0;
		}
		//if the fvalue is higher than all values in the list
		if(fvalue>list.get(list.size()-1).getFvalue()){
			return list.size();
		}
		//initialize upper and lower bounds
		int upperbound=list.size()-1;
		int lowerbound=0;
		while (upperbound-lowerbound>1){
			//check the middle element
			int midindex= (upperbound+lowerbound)/2;
			double midvalue=list.get(midindex).getFvalue();
			if(fvalue<=midvalue){
				//if the middle is over our target fvalue then the target spot is under it
				upperbound=midindex;
			}else{
				//if the middle is under our target fvalue then the target spot is over it
				lowerbound=midindex;
			}
		}
		return upperbound;
	}
}
