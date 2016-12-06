import java.util.ArrayList;

public class Greedy_Engine extends SearchEngine {

	public Greedy_Engine(Problem prob) {
		super(prob);
		this.setStrategy("Greedy");
	}

	
	//Takes the next node to expand from the front of the open list
	//Since the list is ordered this is the node with the lowest f value
	public Node remove() {
		Node nodeToExpand= this.getOpen().remove(0); 
		return nodeToExpand;
	}

	//Adds a node to the closed list
	public void insert(Node newnode) {
		this.getClosed().add(newnode);

	}

	//This method checks a newly generated node to insert it in the corresponding place
	// If the state had not been visited (no equivalent nodes are in closed) the node gets added to open
	// If the state has infinite estimateh, thus making a solution unreachable from there it will not be added
	public void improve(Node child) {
		//check if the node is equivalent to anything in the closed list
		Node visited=this.isClosed(child);
		//if an equivalent state had not been visited and the state has a non infinite h add it to the open list
		if(visited==null && child.getEstimateh()<Double.POSITIVE_INFINITY){
			binaryInsert(child,this.getOpen());
		}

	}
	
	//This method inserts a new node into the given list in the correct position to keep the list ordered
	public void binaryInsert(Node node,ArrayList<Node> list ){
		double hvalue=node.getEstimateh();
		int index=binaryInsertInd(hvalue,list);
		list.add(index,node);
	}
	
	//This method finds the index at which a new node has to be inserted in list to keep the list ordered
	//in ascending order of fvalue
	public int binaryInsertInd(double hvalue,ArrayList<Node> list){
		//base cases
		//if the list is empty or the value to insert has h lower than the first value return 0
		if(list.size()==0 || hvalue<list.get(0).getEstimateh()){
			return 0;
		}
		//if the hvalue is higher than all values in the list
		if(hvalue>list.get(list.size()-1).getEstimateh()){
			return list.size();
		}
		//initialize upper and lower bounds
		int upperbound=list.size()-1;
		int lowerbound=0;
		while (upperbound-lowerbound>1){
			//check the middle element
			int midindex= (upperbound+lowerbound)/2;
			double midvalue=list.get(midindex).getEstimateh();
			if(hvalue<=midvalue){
				//if the middle is over our target hvalue then the target spot is under it
				upperbound=midindex;
			}else{
				//if the middle is under our target hvalue then the target spot is over it
				lowerbound=midindex;
			}
		}
		return upperbound;
	}
}