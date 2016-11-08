import java.util.ArrayList;

public class SearchTester {
	public static String HANOI="Hanoi";
	public static String BANANAGRAMS="Bananagrams";
	public static String NPUZZLE="NPuzzle";
	public static String MAZE="Maze";
	public static String BFS="BFS";
	public static String DFS="DFS";
	public static String ASTAR="Astar";
	public static String GREEDY="Greedy";
	//Main method, takes the argument passed and runs the apropriate test
	public static void main(String[] args) {
		Problem theProblem=null;
		try{
			if(args[0].equals(HANOI)){
				theProblem=initializeHanoi();
			}else if (args[0].equals(BANANAGRAMS)){
				theProblem=initializeBananagrams(Integer.parseInt(args[2]));
			}else if (args[0].equals(NPUZZLE)){
				theProblem=initializeNpuzzle(Integer.parseInt(args[2]));
			}else if (args[0].equals(MAZE)){
				theProblem=initializeMaze();
			}
		}
		catch(Exception e){
			System.out.println("There was a problem initializing the problem, please check your inputs" );
			e.printStackTrace();
		}
		SearchEngine engine=null;
		if(args[1].equals(BFS)){
			engine=new BFS_Engine(theProblem);
		}else if(args[1].equals(DFS)){
			engine=new DFS_Engine(theProblem);
		}else if(args[1].equals(ASTAR)){
			engine=new Astar_Engine(theProblem);
		}else if(args[1].equals(GREEDY)){
			engine=new Greedy_Engine(theProblem);
		}
		String filename="results/"+theProblem.name+"_"+args[1]+".txt";
		Node solution = engine.runSearch();
		engine.printResults(filename, solution);
		System.out.println("Search is over");
		String outLine="You can see the results in the file:"+ filename;
		System.out.println(outLine);
		
	}
	
	//Method to test the functionalities of the npuzzle program
	public static Problem initializeNpuzzle(int order){
		if(order==3 || order==8 || order==15 ){
			ArrayList<Integer> unshuffledTiles=new ArrayList<Integer>();
			for (int i=1; i<=order+1;i++){
				unshuffledTiles.add(i);
			}
			String npuz="";
			for(int i=1; i<=order+1;i++){
				int index= (int) Math.floor(Math.random() * unshuffledTiles.size());
				if(npuz.length()==0){
					npuz+=""+unshuffledTiles.remove(index);
				}
				else{
					npuz+=","+unshuffledTiles.remove(index);
				}
			}
			String problemname= ""+order+"-puzzle_"+npuz; 
			Problem theProblem = new NpuzzleProblem(problemname,order,npuz);
			return theProblem;	
		}
		return null;
		//we initialize the problem in a position where it is nearly solved to test our solution checker
		
	}
	
	//MEthod to test the functionalities of the HanoiProblem
	public static Problem initializeHanoi(){
		//Test node generation
		Problem theProblem =new HanoiProblem("Hanoi_3by5",3,5);
		return theProblem;
	}
	
	//Method to test the functionalities of the BananagramsProblem

	public static Problem initializeBananagrams(int order){
		//Initialize a 9 tile bananagrams problem
		//Problem theProblem=new BananagramsProblem("Banana9grams","NAGODSEEP");
		String [] letterBag={"E","B","S","C","O","C","U","N","Y"};
		String letters="";
		for (int i=0;i<order;i++){
			letters+=letterBag[i];
		}
		String name="Banana"+order+"grams_"+letters;
		Problem theProblem=new BananagramsProblem(name,letters);
		return theProblem;	
	}
	
	//Method to test the functionalities of the MazeProblem
	public static Problem initializeMaze(){
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
		return theProblem;
	}



}
