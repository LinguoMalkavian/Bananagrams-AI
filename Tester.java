
public class Tester {
	public static String HANOI="Hanoi";
	public static String BANANAGRAMS="Bananagrams";
	public static String NPUZZLE="NPuzzle";
	public static String MAZE="Maze";
	
	public static void main(String[] args) {
		
		if(args[0].equals(HANOI)){
			testHannoi();
		}else if (args[0].equals(BANANAGRAMS)){
			testBananagrams();
		}else if (args[0].equals(NPUZZLE)){
			testNpuzzle();
		}else if (args[0].equals(MAZE)){
			testMaze();
		}
		
		
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
	public static void testBananagrams(){
		Problem theProblem=new BananagramsProblem("Banana9grams","NAGODSEEP");
		Node[]successors=theProblem.generateSuccessorList(theProblem.getStartNode());
		theProblem.printState(theProblem.getStartNode().getState());
		for(int i=0;i<successors.length;i++){
			System.out.println("Successor "+(i+1));
			theProblem.printState(successors[i].getState());
		}
		Node depth2=theProblem.generateSuccessorList(successors[0])[0];
		Node depth3=theProblem.generateSuccessorList(depth2)[1];
		Node depth4=theProblem.generateSuccessorList(depth3)[2];
		Node depth5=theProblem.generateSuccessorList(depth4)[2];
		System.out.println();
		theProblem.printState(depth2.getState());
		System.out.println();
		theProblem.printState(depth3.getState());
		System.out.println();
		theProblem.printState(depth4.getState());
		System.out.println();
		theProblem.printState(depth5.getState());
		System.out.println();
		//build a solved state
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
		theProblem.printState(solvedNode.getState());
		System.out.println(theProblem.testGoal(solvedNode));
		
	}
	
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
		Node[]successors=theProblem.generateSuccessorList(theProblem.getStartNode());
		System.out.println("Initial State");
		theProblem.printState(theProblem.getStartNode().getState());
		for(int i=0;i<successors.length;i++){
			System.out.println("Successor "+(i+1));
			theProblem.printState(successors[i].getState());
		}
		
		
	}

}
