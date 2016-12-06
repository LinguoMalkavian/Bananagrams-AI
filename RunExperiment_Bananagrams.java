import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

public  class RunExperiment_Bananagrams {

	public static final String[] STRATEGIES={"BFS","DFS","Greedy","Astar","SMAstar"};

	//hashtables to store results per run
	private static Hashtable<String,Double> avgmaxCandidateSizeLocal=new Hashtable<String,Double>();
	private static Hashtable<String,Double> avgexpandedNodesLocal= new Hashtable<String,Double>();
	private static Hashtable<String,Double> avgdepthOfSolutionLocal= new Hashtable<String,Double>();
	private static Hashtable<String,Double> avgtimeElapsedLocal=new Hashtable<String,Double>();
	private static Hashtable<String,Double> avgmaxDepthLocal= new Hashtable<String,Double>();
	private static Hashtable<String,Double> maxNodesExpandedLocal= new Hashtable<String,Double>();
	private static Hashtable<String,Double> maxTimeElapsedLocal= new Hashtable<String,Double>();
	
	public static void main(String[] args) {
		runExperiment(Integer.parseInt(args[0]));
		
		
	}
	
	public static void runExperiment(int order){
		
		String resultsfilename= "results/Bananagrams"+order+"_results.txt";
		String problemsfilename="data/problems/"+ order+"-bananagramProblems.txt";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(problemsfilename));
			String line="";
			//read each line (every line contains a bananagrams problem specification
			double counter=1;
			while ((line = reader.readLine()) != null) {
				//initialize the problem
				String probname="Order"+order+"Bananagrams"+counter;
				String[] tiles= line.split(",");
				Problem thisProblem= new BananagramsProblem(probname,tiles);
				runSearches(thisProblem,counter, order);
				counter++;
			}
			reader.close();
			//print summary file
			printSummary(resultsfilename,order,(int)counter-1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void runSearches(Problem thisProblem, double counter, int order){
		//Initialize the search engines
		ArrayList<SearchEngine> engines= new ArrayList<SearchEngine>();
		engines.add(new BFS_Engine(thisProblem));
		engines.add(new DFS_Engine(thisProblem));
		engines.add(new Astar_Engine(thisProblem));
		engines.add(new Greedy_Engine(thisProblem));
		engines.add(new SMAstar_Engine(thisProblem));
		for (SearchEngine engine : engines){
			//run each search
			String strategyKey=engine.getStrategy()+order;
			Node goal=engine.runSearch();
			String filename="results/experimentBananagrams/"+thisProblem.name+"_"+engine.getStrategy()+".txt";
			Node solution = engine.runSearch();
			engine.printResults(filename, solution);
			//Collect the statistics
			updateLocalStats(strategyKey,engine,counter);
		}
	}
	
	public static void updateLocalStats(String key, SearchEngine engine,double n ){
		updateAverage(avgmaxCandidateSizeLocal,(double)engine.getMaxCandidateSize(),n,key);
		updateAverage(avgexpandedNodesLocal,(double)engine.getExpandedNodes(),n,key);
		updateAverage(avgdepthOfSolutionLocal,(double)engine.getDepthOfSolution(),n,key);
		updateAverage(avgtimeElapsedLocal,(double)engine.getTimeElapsed(),n,key);
		updateAverage(avgmaxDepthLocal,(double)engine.getMaxCandidateSize(),n,key);
		
		if (! maxNodesExpandedLocal.containsKey(key)){
			maxNodesExpandedLocal.put(key, (double)engine.getExpandedNodes());
		}else if(engine.getExpandedNodes()>maxNodesExpandedLocal.get(key)){
			maxNodesExpandedLocal.put(key, (double)engine.getExpandedNodes());
		}
		
		if (! maxTimeElapsedLocal.containsKey(key)){
			maxTimeElapsedLocal.put(key, (double)engine.getTimeElapsed());
		}else if(engine.getExpandedNodes()>maxTimeElapsedLocal.get(key)){
			maxTimeElapsedLocal.put(key, (double)engine.getTimeElapsed());
		}
		
	}
	
	public static void printSummary(String resultsfilename, int order,int nproblems){
		try{
		    PrintStream outfile = new PrintStream(resultsfilename, "UTF-8");
		    outfile.println("Bananagrams order "+order);
		    outfile.println(nproblems+" problems solved");
		    
		    for(String strategy : STRATEGIES ){
		    	outfile.println(strategy+ "for order" +order+ " Bananagrams");
		    	String key=strategy+order;
		    	String line="Average Max Candidate Size:"+avgmaxCandidateSizeLocal.get(key);
		    	outfile.println(line);
		    	
		    	line="Average Expanded Nodes:"+avgexpandedNodesLocal.get(key);
		    	outfile.println(line);
		    	
		    	line="Average depth of solution:"+avgdepthOfSolutionLocal.get(key);
		    	outfile.println(line);
		    	
		    	line="Average time Elapsed:"+avgtimeElapsedLocal.get(key);
		    	outfile.println(line);
		    	
		    	line="Average Max Depth:"+avgmaxDepthLocal.get(key);
		    	outfile.println(line);
		    	
		    	line="Maximum Nodes Expanded:"+maxNodesExpandedLocal.get(key);
		    	outfile.println(line);
		    	
		    	line="Maximum Time elapsed:"+maxTimeElapsedLocal.get(key);
		    	outfile.println(line);
		    	
		    	outfile.println("---------------------------------------------");
		    	
		    }
		    
		    
		    outfile.close();
		    
		} catch (Exception e) {
		   System.out.println("Error while printing results, please check output parameters" );
		   e.printStackTrace();
		}
	}
	public static void extractGlobalStats(){
		
	}
	
	public static void updateAverage(Hashtable<String,Double> table,double newValue,double n,String key){
		if (! table.containsKey(key)){
			table.put(key,  newValue);
		}else {
			double avg = ((table.get(key)*(n-1))+newValue)/n;
			table.put(key,avg);
		}
	}

}
