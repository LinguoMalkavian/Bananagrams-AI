import java.util.ArrayList;
import java.util.Arrays;

public class Experiment {


	public static void main(String[] args) {
		//Test node generation
//		HanoiProblem theProblem =new HanoiProblem("Four by six",4,6);
//		
//		int[][] state1=new int[4][6];
//		state1[2][0]=1;
//		state1[2][1]=2;
//		state1[1][0]=3;
//		state1[1][1]=4;
//		state1[1][2]=5;
//		state1[1][3]=6;
//		Node node1=new Node(state1,theProblem);
//		
//		int[][] state2=new int[4][6];
//		state2[1][0]=1;
//		state2[1][1]=2;
//		state2[2][0]=3;
//		state2[2][1]=4;
//		state2[2][2]=5;
//		state2[2][3]=6;
//		Node node2=new Node(state2,theProblem);
//		
//		int[][] state3=new int[4][6];
//		state3[1][0]=1;
//		state3[3][0]=2;
//		state3[2][0]=3;
//		state3[2][1]=4;
//		state3[2][2]=5;
//		state3[2][3]=6;
//		Node node3=new Node(state3,theProblem);
//		
//		if (theProblem.equivalentNodes(node1, node2)&& !theProblem.equivalentNodes(node1, node3)){
//			System.out.println("Esta vivoooo");
//		}else{
//			System.out.println("Noope");
//		}
		
//		Problem theProblem=new BananagramsProblem("Banana9grams","NAGODSEEP");
//		
//		//build a solved state for this problem
//		int[][] solvedState = new int[9][2];
//		//N
//		solvedState[0][0]=0;
//		solvedState[0][1]=0;
//		//A
//		solvedState[1][0]=0;
//		solvedState[1][1]=1;
//		//G
//		solvedState[2][0]=0;
//		solvedState[2][1]=2;
//		//O
//		solvedState[3][0]=1;
//		solvedState[3][1]=2;
//		//D
//		solvedState[4][0]=2;
//		solvedState[4][1]=2;
//		//S
//		solvedState[5][0]=3;
//		solvedState[5][1]=2;
//		//E
//		solvedState[6][0]=2;
//		solvedState[6][1]=3;
//		//E
//		solvedState[7][0]=2;
//		solvedState[7][1]=4;
//		//P
//		solvedState[8][0]=2;
//		solvedState[8][1]=5;
//		
//		Node solvedNode= new Node(solvedState,theProblem);
//		theProblem.printState(solvedState, System.out);
//		
//		int[][] reflectsolved= new int[9][2];
//		for (int i=0;i<solvedState.length;i++){
//			reflectsolved[i][0]= solvedState[i][1];
//			reflectsolved[i][1]= solvedState[i][0];					
//		}
//		Node refsolvedNode= new Node(reflectsolved,theProblem);
//		theProblem.printState(reflectsolved, System.out);		
//		
//		System.out.println(theProblem.equivalentNodes(solvedNode, refsolvedNode));
//		
//		int [][]state1= new int[9][2];
//		int [][]state2= new int[9][2];
//		for (int i=0;i<9;i++){
//			state1[i][0]=Integer.MAX_VALUE;
//			state2[i][1]=Integer.MAX_VALUE;
//		}
//		state1[0][0]=1;
//		state1[0][1]=1;
//		state1[1][0]=2;
//		state1[1][1]=1;
//		
//		state2[0][0]=1;
//		state2[0][1]=1;
//		state2[1][0]=1;
//		state2[1][1]=2;
//		Node node1=new Node(state1,theProblem);
//		Node node2=new Node(state2,theProblem);
//		System.out.println(theProblem.equivalentNodes(node1, node2));
		
		ArrayList<Double> prueba= new  ArrayList<Double>(Arrays.asList(1.0, 4.0, 5.2));
		prueba.add(binaryInsert(3.1,prueba),3.1);
		prueba.add(binaryInsert(4.2,prueba),4.2);
		prueba.add(binaryInsert(5.2,prueba),5.2);
		prueba.add(binaryInsert(4.0,prueba),4.0);
		prueba.add(binaryInsert(0.5,prueba),0.5);
		prueba.add(binaryInsert(6.3,prueba),6.3);
		prueba.add(binaryInsert(4.1,prueba),4.1);
		System.out.println(prueba.toString());
	}
	
	public static int binaryInsert(double fvalue,ArrayList<Double> list){
		if(list.size()==0 || fvalue<list.get(0)){
			return 0;
		}
		if(fvalue>list.get(list.size()-1)){
			return list.size();
		}
		int upperbound=list.size()-1;
		int lowerbound=0;
		while (upperbound-lowerbound>1){
			int midindex= (upperbound+lowerbound)/2;
			//double midvalue=list.get(midindex).getFvalue();
			double midvalue=list.get(midindex);
			if(fvalue<=midvalue){
				upperbound=midindex;
			}else{
				lowerbound=midindex;
			}
		}
		
		return upperbound;
	}
}
