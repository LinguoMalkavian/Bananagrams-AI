import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

//Author:Pablo Gonzalez Martinez
//Language: Java 
//Runtime Environment:Java-SE 1.8 (Java-SE 1.8.0_73)
//Class to represent a Bananagrams Problem for the General purpose solver


public class BananagramsProblem extends Problem {
	//An array with the characters in the problem, in order
	private char[] tiles;
	//The number of tiles
	private int order;
	//The first node of the problem
	private Node startNode;
	//The dictionary with all valid words
	private ArrayList<String> dictionary;
	//The relative path to the file with the dictionary
	private static final String DICTIONARY_PATH="data/dictionary.txt";
	//The end of the name  for word frequency files
	private static final String FREQ_FILE="-gramfreq.txt";
	//the location of data files
	private static final String DATA_FOLDER="data/";
	//Arraylist to store the n-gram frequency tables
	private ArrayList<Hashtable<String,Double>> freqTables;

	//Constructor for the Bananagrams Problem
	//Takes the name of the problem and a string with the characters for the tiles
	//Creates all the necessary initial values and loads the dictionary
	public BananagramsProblem (String name,String characters ) {
		super(name);
		order=characters.length();
		tiles=new char[characters.length()];
		//Initialize the character list with the given string
		characters=characters.toUpperCase();
		for (int i=0 ;i <characters.length(); i++){
			tiles[i]=characters.charAt(i);
		}
		//create the starting state the representation has the maximum integer for both coordinates if a tile is unplaced
		//if the tile is placed it has it's x and y coordinates respectively  
		int[][] startState=new int[order][2];
		for(int i=0 ;i<order;i++){
			startState[i][0]=Integer.MAX_VALUE;
			startState[i][1]=Integer.MAX_VALUE;
		}
		startNode=new Node(startState,this);
		//load the dictionary into memory
		this.dictionary=loadDictionary();
		this.freqTables=loadTables(order);
	}

	//Loads the dictionary contained in the default location
	//Prints an error message if the dictionary can not be found at the default location
	private ArrayList<String> loadDictionary() {
		//initialize the container and the reader
		ArrayList<String> dict=new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(DICTIONARY_PATH));
			String line="";
			//read each line (every line contains a single valid word in all caps)
			while ((line = reader.readLine()) != null) {
				//add the line to the dictionary
				dict.add(line);
			}

		} catch (IOException e) {
			System.out.println("Error while reading dictionary file, file not fount at "+DICTIONARY_PATH);
		}
		return dict;
	}

	//This method unpacks the integer matrix representation into a matrix of characters that will be used to operate
	//every time a state is unpacked room is left in the grid representation to add tiles at the frontiers
	//if this room is unused that packing routine will eliminate it
	//This adaptative system of coordinates ensures that we always have enough room to operate and that
	//states that might be translations of each other converge into a single representation after
	//unpacking and repacking thus helping the process of duplicate detection
	public char[][] unpackState(int[][]compactRep){
		//Initialize variables for the extremes of the coordinate system
		int maxX=Integer.MIN_VALUE;
		int minX=Integer.MAX_VALUE;
		int maxY=Integer.MIN_VALUE;
		int minY=Integer.MAX_VALUE;
		//look for extreme values in both dimensions
		for(int i=0;i<order;i++){
			int X=compactRep[i][0];
			int Y=compactRep[i][1];
			//Only assigned tiles count
			if(X!=Integer.MAX_VALUE && Y != Integer.MAX_VALUE){
				//New extrema replace the old ones if found
				if(X>maxX){
					maxX=X;
				}
				if(Y>maxY){
					maxY=Y;
				}
				if(X<minX){
					minX=X;
				}
				if(Y<minY){
					minY=Y;
				}
			}
		}
		//define the coordinate changes needed for both coordinates to be positive
		//notice we leave room for one extra tile in all directions so the action can be carried out on the
		//unpacked representation
		int adaptX=-minX+1; 
		int adaptY=-minY+1;
		//define the needed dimensions for the grid with the safety buffer
		int boundaryX= maxX+adaptX+2;
		int boundaryY= maxY+adaptY+2;

		char[][] grid= new char[boundaryX][boundaryY];
		//Go through the compact representation and place the tiles that have already been determined
		for (int i=0;i<order;i++){
			//Only assigned tiles will be placed
			if(compactRep[i][0]!=Integer.MAX_VALUE && compactRep[i][1] != Integer.MAX_VALUE){
				//Do the change of coordinates before placing the tile
				int X=compactRep[i][0]+adaptX;
				int Y=compactRep[i][1]+adaptY;
				grid[X][Y]=tiles[i];
			}
		}
		return grid;
	}


	//This method packs the character grid as a two dimensional array of coordinates,
	//the compact representation has an x and y values for each tile so the ith tile
	//in the tiles array will be assigned to the space defined by answer[i][0],answer[i][1]
	public int[][] packState(char[][] fullRep){
		//initialize
		int[][] answer= new int[order][2] ;
		char emptyChar=0;
		//Create an arraylist of the tileset so we can cross off the ones whose position has been
		//found, this will make the conversion well defined since duplicates of the same letter
		//are chosen starting from the left and each letter can only be picked once
		ArrayList<Character> tileset= new ArrayList<Character>();
		//This loop does two things at a time, it copies the characters in tiles to the tileset
		//and also sets the initial values of the answer to the unasigned constant (Integer.MAX_VALUE)
		for(int i=0;i<order;i++){
			//add the character to the tileset
			tileset.add(tiles[i]);
			//set the coordinates in the answer to unasigned (max value)
			answer[i][0]=Integer.MAX_VALUE;
			answer[i][1]=Integer.MAX_VALUE;
		}

		//Traverse the matrix, and store the coordinates of each character found in the corresponding
		//space in the compact representation
		//Since we traverse in order and always take the tiles in the set in order two equivalent grids
		//will be translated into identical compact representations
		for(int i=0;i<fullRep.length;i++){
			for(int j=0; j<fullRep[i].length;j++){
				//Get the value in the grid position
				char thisTile=fullRep[i][j];
				//If the value is not the empty character
				if(thisTile!=0){
					//find which index the tile corresponds to in the tileset
					int tileInd =tileset.indexOf(thisTile);
					//fill in the answer
					answer[tileInd][0]=i;
					answer[tileInd][1]=j;
					//remove the tile from the tileset, so future instances of the same character
					//will be assigned to later appearances of the character in the tile list
					//we replace it instead of popping it so the indices remain the same
					tileset.set(tileInd, emptyChar);
				}
			}
		}
		return answer;
	}

	//This method tests a node to see if it is a solution by checking two of the three constraints
	//That all tiles have been placed
	//That every maximal vertical and horizontal string is a word
	//The third constraint (conectivity) is not checked because the node generation process takes care of it
	//since any tile must be placed in contact with a previous tile
	public Boolean testGoal(Node targetNode){
		int[][] state= targetNode.getState();
		//check if all tiles have been placed
		for(int i=0;i<state.length;i++){
			if (state[i][0]==Integer.MAX_VALUE && state[i][1]==Integer.MAX_VALUE){
				//if there is at least one unplaced tile the state is not a solution
				return false;
			}
		}

		ArrayList<String> wordlist= getWords(targetNode);
		//iterate over the available words (full left to right or up to down strings with more than one character)
		for (String word : wordlist){
			if(!checkWord(word)){
				//if any is not in the dictionary then the state is not a valid solution
				return false;
			}
		}
		//if no violations were found then the state is a solution
		return true;
	}

	//Gets a node and extracts all strings that can be read from left to right or top to bottom on the grid
	public ArrayList<String> getWords(Node node){
		//unpack the state
		int[][] state=node.getState();
		char[][] grid= unpackState(state);
		//Initialize empty list
		ArrayList<String> words= new ArrayList<String>();

		//check that all maximal continuous strings of more than 1 character in horizontal or vertical direction and store them
		//traverse the array horizontally and vertically appending characters to a current word,
		//once the word is complete (next character is empty) store it

		//Check vertically

		for (int i=0; i<grid.length;i++){
			//initialize the current word as empty at the beginning of each column
			String currentword="";
			for(int j=0;j<grid[i].length;j++){
				char tile=grid[i][j];
				if(tile!=0){
					//if the character is not empty then add it to the current word
					currentword+=tile;
				}else if (currentword.length()>1){
					//if the character is empty and the current word is more than 1 character long then a word has been completed
					words.add(currentword);
					//reset the current word
					currentword="";
				}
			}
		}

		//check horizontally
		for (int j=0; j<grid[0].length;j++){
			//initialize the current word as empty at the beginning of each row
			String currentword="";
			for(int i=0;i<grid.length;i++){
				char tile=grid[i][j];
				if(tile!=0){
					//if the character is not empty then add it to the current word
					currentword+=tile;
				}else if (currentword.length()>1){
					//if the character is empty and the current word is more than 1 character long then a word has been completed
					words.add(currentword);
					//reset the current word
					currentword="";
				}
			}
		}
		return words;
	}

	//This method takes an input word and returns true if it is found in the dictionary, false otherwise
	//Since the dictionary is ordered the method uses binary search to optimize the speed of the search
	private Boolean checkWord(String currentword) {
		//use the Collections implementation of binary search for optimal speed
		int index = Collections.binarySearch(dictionary,currentword);
		//interpret the index as true or false
		if (index>=0){
			return true;
		}else{
			return false;
		}
	}

	//This method returns all possible legal actions that can be performed to a parent node
	//It does this by collecting all unassigned tiles
	//and then checking which spaces in the grid are empty and orthogonally adjacent to a placed tile
	//(this solves the connectivity problem which then does not have to be checked)
	//it generates an action instruction to put each unassigned tile in each legal space
	//The action commands are in the format: "i>x,y" where i is the tile index
	//and x,y are the coordinates to place the tile in.
	public String[] getPossibleActions(Node parent){
		//initialize the actions arrayList
		ArrayList<String> actions=new ArrayList<String>();
		//see which letters are unassigned
		int[][] state=parent.getState();
		ArrayList<Integer> availableTiles=new ArrayList<Integer>();
		for(int i=0;i<state.length;i++){
			if(state[i][0]==Integer.MAX_VALUE && state[i][1]==Integer.MAX_VALUE){
				availableTiles.add(i);
			}
		}
		//get the grid representation
		char[][] grid = unpackState(state);
		//find empty valid spots(i.e positions in the grid that are orthogonally adjacent to an assigned position but empty)
		//initialize a list of empty spot strings
		ArrayList<String> availablePositions = new ArrayList<String>();
		for(int i=0; i<grid.length;i++){
			for(int j=0;j<grid[i].length;j++){
				char thisTile=grid[i][j];
				if(thisTile!=0){
					//the grid has a tile on i,j
					//check the four adjacent spaces, if they don't have tiles add them to the available spaces
					//this should never be out of bounds because of the margin left in the unpacking
					if(grid[i+1][j]==0){
						String coords= (i+1)+","+(j);
						availablePositions.add(coords);
					}
					if(grid[i][j+1]==0){
						String coords= (i)+","+(j+1);
						availablePositions.add(coords);
					}
					if(grid[i-1][j]==0){
						String coords= (i-1)+","+(j);
						availablePositions.add(coords);
					}
					if(grid[i][j-1]==0){
						String coords= (i)+","+(j-1);
						availablePositions.add(coords);
					}
				}
			}
		}
		//if no tiles have been placed no available positions will be found, in this case make 0,0 the only available position
		if (availablePositions.isEmpty()){
			availablePositions.add("0,0");
		}
		//the possible actions are putting any available tile in any available space
		for(int tileInd=0; tileInd<availableTiles.size();tileInd++){
			for(int positionInd=0; positionInd<availablePositions.size();positionInd++){
				String actionString=availableTiles.get(tileInd)+">"+availablePositions.get(positionInd);
				actions.add(actionString);
			}
		}
		//convert the arraylist into an array 
		String[] actionarray=new String[actions.size()];
		actionarray=actions.toArray(actionarray);
		return actionarray;
	}

	//Takes a node and an action and executes the action on the node
	//Input: parent, the node to execute the action on
	//Input: action a String defining the action in the format "i>x,y" where i is the tile index
	//and x,y are the coordinates to place the tile in.
	//Output: the new node
	public Node executeAction(Node parent, String action) {
		//get the state and unpack it
		int[][] state=parent.getState();
		char[][] grid=unpackState(state);
		//read the action instruction
		String[] elems= action.split(">");
		int tileInd=Integer.parseInt(elems[0]);
		String[] coords=elems[1].split(",");
		int x= Integer.parseInt(coords[0]);
		int y=Integer.parseInt(coords[1]);
		char tileChar=tiles[tileInd];
		//place the tile in the indicated position
		grid[x][y]=tileChar;
		//repack the representation
		int[][] childState= packState(grid);

		Node childNode= new Node(parent,action,childState,this,parent.getCost()+1);

		return childNode;
	}

	//Method that takes a state representation and prints it in a human readable format
	//It represents empty spaces with - and fills in the letters in the grid
	//It prints the representation to standard output
	public void printState(int[][] state,PrintStream outfile) {
		char[][] grid= unpackState(state);
		for(int j=0;j<grid[0].length;j++){
			String line="";
			for(int i=0;i<grid.length;i++){
				if(grid[i][j]==0){
					line+='-';
				}else{
					line+=grid[i][j];
				}
			}
			outfile.println(line);
		}
	}

	//Method to return the start node.
	public Node getStartNode() {
		return startNode;
	}

	//This method takes two nodes and checks whether they have equivalent states
	//Two states are equivalent if they have the same compact representation after repacking of one is a reflexion of the other degrees rotation of the other
	public boolean equivalentNodes(Node node1, Node node2) {
		//two nodes can only be equivalent if they are at the same depth, to improve speed we only run the full comparison if this is the case
		if(node1.getDepth()==node2.getDepth()){
			int[][]state1= packState(unpackState(node1.getState()));
			int[][]state2= packState(unpackState(node2.getState()));
			//asume the states are both identical and reflections
			boolean identical=true;
			boolean reflection=true;
			//go over the position for every tile while it is still possible that the states are either identical or reflected
			for (int i=0;i<state1.length && (identical || reflection);i++){
				//if the same tile is placed differently in at least one coordinate the states are not identical
				if(state1[i][0]!=state2[i][0] || state1[i][1]!=state2[i][1]  ){
					identical=false;
				}
				//if it is placed anywhere but in the reflection of the other in one then they are not reflections
				if(state1[i][1]!=state2[i][0] || state1[i][0]!=state2[i][1]  ){
					reflection=false;
				}
			}
			return identical || reflection;
		} else{
			return false;
		}
	}

	//This method calculates the score for the node
	//The score is derived from the frequency of the strings formed in the following way 
	// N is the total number of tiles
	// A non solved state has a score computed as N + crossings - sum  for each word w (wordscore[w]*wordlength[w])
	// wordscore is computed in the following way
	// a full valid word (one that is in the dictionary has score 1)
	// a partial string of length n (a character n-gram) has it's relative frequency in the corpus (precomputed) as score
	// if any word in the representation has frequency 0 then the full score of the node becomes infinity (no solution can be reached from there)
	// Crossings is how many words are in more than one word (in fact in 2) this factor is responsible for solutions having estimate 0
	//  and makes the search favor longer words (less crossings)
	//--------
	//This scoring gives estimates under N  to solutions and favors solutions with completed long words and frquent partial words, 
	//The infinity estimate prunes undesired paths
	public double getEstimate(Node node ){
		Boolean impossible=false;
		int crossings= getCrossings(node);
		double score=order+crossings;
		ArrayList<String> words = getWords(node);
		for (String word : words){
			double wordscore=0;
			//the index of the corresponding frequency table id the length of the ngram -2
			int ngramOrder=word.length();
			int ngramTableInd=ngramOrder-2;
			if(checkWord(word)){
				wordscore=1;
			}else if(freqTables.get(ngramTableInd).containsKey(word)){
				wordscore=freqTables.get(ngramTableInd).get(word);
			}else{
				impossible=true;
				break;
			}
			score-= wordscore*ngramOrder;
		}
		if (impossible){
			return Double.POSITIVE_INFINITY;
		}else{
			return score;
		}

	}

	//Method that counts how many letters have neighbors in both directions;
	private int getCrossings(Node node) {
		int[][] state=node.getState();
		int crossings=0;
		//go over every tile
		for (int tile1ind=0;tile1ind<state.length;tile1ind++){
			boolean neighborV=false;
			boolean neighborH=false;
			int[] tile1=state[tile1ind];
			for (int tile2ind=0;tile2ind<state.length;tile2ind++){
				//only compare if it is not the same tile
				if(tile1ind!=tile2ind){
					int tile2[]=state[tile2ind];
					
					if (tile1[0]==tile2[0] && (tile1[1]==tile2[1]+1 || tile1[1]==tile2[1]-1)){
						//the two tiles are horizontal neighbors
						neighborH=true;
					}else if (tile1[1]==tile2[1] && (tile1[0]==tile2[0]+1 || tile1[0]==tile2[0]-1)){
						//the two tiles are vertical neighbors
						neighborV=true;
					}
				}	
			}
			//if the tile had both a horizontal and a vertical neighbor it is a crossing
			if (neighborV && neighborH){
				crossings++;
			}
		}
		return crossings;
	}

	//Deprecated
	//This method calculates the score for the node
	//The score is derived from the frequency of the strings formed in the following way 
	// N is the total number of tiles
	// A non solved state has a score computed as 2N - sum for each word w (wordscore[w]*wordlength[w])
	// wordscore is computed in the following way
	// a full valid word (one that is in the dictionary has score 1)
	// a partial string of length n (a character n-gram) has it's relative frequency in the corpus (precomputed) as score
	// if any word in the representation has frequency 0 then the full score of the node becomes infinity (no solution can be reached from there)
	// --------
	//This scoring gives estimates under N  to solutions and favors solutions with completed long words and frquent partial words, 
	//The infinity estimate prunes undesired paths
	public double getEstimateOld(Node node ){
		Boolean impossible=false;
		ArrayList<String> words = getWords(node);
		double score=2*order;
		for (String word : words){
			double wordscore=0;
			//the index of the corresponding frequency table id the length of the ngram -2
			int ngramOrder=word.length();
			int ngramTableInd=ngramOrder-2;
			if(checkWord(word)){
				wordscore=1;
			}else if(freqTables.get(ngramTableInd).containsKey(word)){
				wordscore=freqTables.get(ngramTableInd).get(word);
			}else{
				impossible=true;
				break;
			}
			score-= wordscore*ngramOrder;
		}
		if (impossible){
			return Double.POSITIVE_INFINITY;
		}else{
			return score;
		}

	}
	//Deprecated
	//gets the score for a move
	//	public double scoreMove(Node child){
	//		char[][]grid=unpackState(child.getState());
	//		String[] actionElements=child.getAction().split(">");
	//		int tilePlaced=Integer.parseInt(actionElements[0]);
	//		String[]coordinateStr=actionElements[1].split(",");
	//		int x=Integer.parseInt(coordinateStr[0]);
	//		int y=Integer.parseInt(coordinateStr[1]);
	//		String horizontalword=""+tiles[tilePlaced];
	//		//explore in each direction adding letters to the corresponding strings
	//		//search left of the tile
	//		for(int toLeft=1;toLeft>0 ;toLeft++){
	//			if(grid[x-toLeft][y]!=0){
	//				horizontalword=grid[x-toLeft][y]+horizontalword; 
	//			}else{
	//				break;
	//			}
	//		}
	//		//search right of the tile
	//		for(int toRight=1;toRight<grid.length ;toRight++){
	//			if(grid[x+toRight][y]!=0){
	//				horizontalword=horizontalword+ grid[x+toRight][y]; 
	//			}else{
	//				break;
	//			}
	//		}
	//		
	//		String verticalword=""+tiles[tilePlaced];
	//		//look up from the tile
	//		for(int up=1;up>0 ;up++){
	//			if(grid[x][y-up]!=0){
	//				verticalword=grid[x][y-up]+verticalword; 
	//			}else{
	//				break;
	//			}
	//		}
	//
	//		for(int down=1;down<grid[0].length ;down++){
	//			if(grid[x][y+down]!=0){
	//				verticalword=horizontalword+ grid[x+down][y]; 
	//			}else{
	//				break;
	//			}
	//		}
	//		boolean horizontalIsWord=checkWord(horizontalword);
	//		int hwordbonus=0;
	//		if (horizontalIsWord){
	//			hwordbonus=1;
	//		}else{
	//			hwordbonus= freqTables.get(horizontalword.length()).get(horizontalword);
	//		}
	//		boolean verticalIsWord=checkWord(verticalword);
	//		int vwordbonus=0;
	//		if (verticalIsWord){
	//			vwordbonus=1;
	//		}
	//		
	//		double freqvert= freqTables.get(verticalword.length()).get(verticalword);
	//		
	//		double score= freqhor +freqvert +hwordbonus +vwordbonus; 
	//		return 0;
	//	}

	//Loads a single frequency file
	public Hashtable<String,Double> loadFrequencyFile(int order) {
		String filename=DATA_FOLDER+order+FREQ_FILE;
		//initialize the container and the reader
		Hashtable<String,Double> table=new Hashtable<String,Double>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line="";
			//read each line (every line contains an NGRAM followed by its count in the corpus)
			while ((line = reader.readLine()) != null) {
				//split the key and value
				String[] elems=line.split(",");
				String ngram=elems[0];
				double freq=Double.parseDouble(elems[1]);
				table.put(ngram, freq);
			}

		} catch (IOException e) {
			System.out.println("Error while reading dictionary file, file not fount at "+filename);
		}
		return table;
	}

	//Reads the frequency tables needed for the problem (those od orders less than the number of tiles )
	public ArrayList<Hashtable<String, Double>> loadTables(int problemSize){
		ArrayList<Hashtable<String,Double>> answer= new ArrayList<Hashtable<String,Double>>();
		for (int i=2;i<=problemSize && i<14;i++){
			answer.add(loadFrequencyFile(i));
		}
		return answer;
	}

	//Retruns the order of the problem (number of tiles)
	public int getOrder() {
		return order;
	}



}
