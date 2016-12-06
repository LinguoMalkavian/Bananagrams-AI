//Author:Pablo Gonzalez Martinez
//Language: Java 
//Runtime Environment:Java-SE 1.8 (Java-SE 1.8.0_73)
//Class that runs tests to show the functionality of the Problem and Node classes.


public class Tester {
	public static String HANOI="Hanoi";
	public static String BANANAGRAMS="Bananagrams";
	public static String NPUZZLE="NPuzzle";
	public static String MAZE="Maze";
	
	//Main method, takes the argument passed and runs the apropriate test
	public static void main(String[] args) {
		
		if(args[0].equals(HANOI)){
			testHanoi();
		}else if (args[0].equals(BANANAGRAMS)){
			testBananagrams();
		}else if (args[0].equals(NPUZZLE)){
			testNpuzzle();
		}else if (args[0].equals(MAZE)){
			testMaze();
		}
		
	}
	
	//Method to test the functionalities of the npuzzle program
	public static void testNpuzzle(){
		String npuz="1,2,4,3,9,6,5,7,8";
		//we initialize the problem in a position where it is nearly solved to test our solution checker
		Problem theProblem = new NpuzzleProblem("eight puzzle",8,npuz);
		System.out.println("Heuristic:");
		System.out.println(theProblem.getEstimate(theProblem.getStartNode()));
		testNodeGeneration(theProblem);
		//Generate a solved probelm
		String solvedpuz="1,2,3,4,5,6,7,8,9";
		//reinitialize the problem with a solved state
		theProblem = new NpuzzleProblem("eight puzzle",8,solvedpuz);
		//apply the test
		testGoalTest(theProblem,theProblem.getStartNode());
		System.out.println("Heuristic:");
		System.out.println(theProblem.getEstimate(theProblem.getStartNode()));
		
	}
	
	//MEthod to test the functionalities of the HanoiProblem
	public static void testHanoi(){
		//Test node generation
		Problem theProblem =new HanoiProblem("Four by six",4,6);
		testNodeGeneration(theProblem);
		//generate a solved node
		int[][] solvedState=new int[4][6];
		solvedState[3][0]=1;
		solvedState[3][1]=2;
		solvedState[3][2]=3;
		solvedState[3][3]=4;
		solvedState[3][4]=5;
		solvedState[3][5]=6;
		
		Node solvedNode=new Node(solvedState,theProblem);
		testGoalTest(theProblem,solvedNode);
	}
	
	//Method to test the functionalities of the BananagramsProblem

	public static void testBananagrams(){
		//Initialize a 9 tile bananagrams problem
		String [] letterBag={"N","A","G","O","D","S","E","E","P"};
		Problem theProblem=new BananagramsProblem("Banana9grams",letterBag);
		testNodeGeneration(theProblem);
		
		//build a solved state for this problem
		int[][] solvedState = new int[9][2];
		//N
		solvedState[0][0]=0;
		solvedState[0][1]=0;
		//A
		solvedState[1][0]=0;
		solvedState[1][1]=1;
		//G
		solvedState[2][0]=0;
		solvedState[2][1]=2;
		//O
		solvedState[3][0]=1;
		solvedState[3][1]=2;
		//D
		solvedState[4][0]=2;
		solvedState[4][1]=2;
		//S
		solvedState[5][0]=3;
		solvedState[5][1]=2;
		//E
		solvedState[6][0]=2;
		solvedState[6][1]=3;
		//E
		solvedState[7][0]=2;
		solvedState[7][1]=4;
		//P
		solvedState[8][0]=2;
		solvedState[8][1]=5;
		
		Node solvedNode= new Node(solvedState,theProblem);
		//print the solved state and the result of the goal test
		testGoalTest(theProblem,solvedNode);
		
	}
	
	//Method to test the functionalities of the MazeProblem
	public static void testMaze(){
		int[] start={1,3};
		int[] goal={5,0};
		int[][] obstacles=new int[5][2];
		obstacles[0][0]=2;
		obstacles[0][1]=0;
		obstacles[1][0]=2;
		obstacles[1][1]=1;
		obstacles[2][0]=3;
		obstacles[2][1]=3;
		obstacles[3][0]=4;
		obstacles[3][1]=1;
		obstacles[4][0]=5;
		obstacles[4][1]=2;
		
		Problem theProblem=new MazeProblem(6,4,start,goal,obstacles);
		
		testNodeGeneration(theProblem);
		int[][]solvedState= new int[1][2];
		solvedState[0][0]=5;
		solvedState[0][1]=0;
		Node solvedNode= new Node(solvedState,theProblem);
		testGoalTest(theProblem,solvedNode);
	}
	//Gets a problem and generates all possible nodes at depth one, prints them and then generates one random node at each depth
	//from 2 to 4
	public static void testNodeGeneration(Problem theProblem){
		Node[]successors=theProblem.generateSuccessorList(theProblem.getStartNode());
		System.out.println("Start");
		theProblem.printState(theProblem.getStartNode().getState(),System.out);
		for(int i=0;i<successors.length;i++){
			System.out.println("Successor "+(i+1));
			theProblem.printState(successors[i].getState(),System.out);
		}
		//Generate some successors at bigger depths:
		Node depth2=theProblem.generateSuccessorList(successors[0])[0];
		Node depth3=theProblem.generateSuccessorList(depth2)[0];
		Node depth4=theProblem.generateSuccessorList(depth3)[0];
		Node depth5=theProblem.generateSuccessorList(depth4)[0];
		System.out.println("Depth 2 node");
		theProblem.printState(depth2.getState(),System.out);
		System.out.println("Depth 3 node");
		theProblem.printState(depth3.getState(),System.out);
		System.out.println("Depth 4 node");
		theProblem.printState(depth4.getState(),System.out);
		System.out.println("Depth 5 node");
		theProblem.printState(depth5.getState(),System.out);
		System.out.println();
	}
	public static void testGoalTest(Problem theProblem, Node solvedNode){
		System.out.println("Solved state: ");
		theProblem.printState(solvedNode.getState(),System.out);
		Boolean solvedBool= theProblem.testGoal(solvedNode);
		System.out.println("The test returns: "+solvedBool);
	}

}
