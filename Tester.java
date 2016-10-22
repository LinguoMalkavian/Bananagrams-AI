
public class Tester {

	public static void main(String[] args) {
		testHannoi();
		//testNpuzzle();
		
	}
	
	public static void testNpuzzle(){
		String npuz="1,2,3,4,5,6,7,9,8";
		Problem theProblem = new NpuzzleProblem("eight puzzle",8,npuz);
		
		theProblem.printState(theProblem.getStartNode().getState());
		Node[] successors=theProblem.generateSuccessorList(theProblem.getStartNode());
		for (int i =0; i<successors.length;i++){
			System.out.println("---------------");
			theProblem.printState(successors[i].getState());
			Boolean solved= theProblem.testGoal(successors[i]);
			System.out.println(solved);
		}
	}
	
	public static void testHannoi(){
		Problem theProblem =new HannoiProblem("Four by six",4,6);
		Node[]successors=theProblem.generateSuccessorList(theProblem.getStartNode());
		System.out.println("Start");
		theProblem.printState(theProblem.getStartNode().getState());
		for(int i=0;i<successors.length;i++){
			System.out.println("Successor "+(i+1));
			theProblem.printState(successors[i].getState());
		}
	}

}
