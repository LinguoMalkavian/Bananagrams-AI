import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class BananagramsProblem extends Problem {
	private char[] tiles;
	private int order;
	private Node startNode;
	private ArrayList<String> dictionary;
	//
	public BananagramsProblem (String name,String characters ) {
		super(name);
		order=characters.length();
		tiles=new char[characters.length()];
		//Initialize the character list with the given string
		for (int i=0 ;i <characters.length(); i++){
			tiles[i]=characters.charAt(i);
		}
		//create the starting state the representation has the maximum integer for both coordinates if a tile is umplaced
		//if the tile is placed it has it's x and y coordinates respectively  
		int[][] startState=new int[order][2];
		for(int i=0 ;i<order;i++){
			startState[i][0]=Integer.MAX_VALUE;
			startState[i][1]=Integer.MAX_VALUE;
		}
		startNode=new Node(startState,this);
		this.dictionary=loadDictionary();
	}
	
	//Loads the dictionary contained in the default location
	private ArrayList<String> loadDictionary() {
		String filepath="data/dictionary.txt";
		ArrayList<String> dict=new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filepath));
			String line="";
			while ((line = reader.readLine()) != null) {
				dict.add(line);
			}

		} catch (IOException e) {
			System.out.println("Error while reading dictionary file, file not fount at "+filepath);
		}
		return dict;
	}
	//This unpacks the integer matrix representation into a matrix of characters that will be used to operate
	//every time a state is unpacked room is left in the grid representation to add tiles at the frontiers
	//if this room is unused that packing routine will eliminate it
	//This adaptative system of coordinates ensures that we always have enough room to operate and that
	//states that might be translations of each other converge into a single representation after
	//unpacking and repacking thus helping the process of duplicate detection
	public char[][] unpackState(int[][]compactRep){
		//Initialize borders of the grid
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
		//this defines the coordinate changes needed for both coordinates to be positive
		//notice we leave room for one extra tile in all directions so the action can be caried out on the
		//unpacked representation
		int adaptX=-minX+1; 
		int adaptY=-minY+1;
		int boundaryX= maxX+adaptX+2;
		int boundaryY= maxY+adaptY+2;

		char[][] grid= new char[boundaryX][boundaryY];
		//Go through the compact representation and place the tiles that have already been determined
		for (int i=0;i<order;i++){

			//Only assigned tiles will be placed
			if(compactRep[i][0]!=Integer.MAX_VALUE && compactRep[i][1] != Integer.MAX_VALUE){
				int X=compactRep[i][0]+adaptX;
				int Y=compactRep[i][1]+adaptY;
				grid[X][Y]=tiles[i];
			}
		}
		return grid;
	}
	//Pack the character grid as a two dimensional array of coordinates,
	//the compact representation has an x and y values for each 
	public int[][] packState(char[][] fullRep){
		int[][] answer= new int[order][2] ;
		char emptyChar=0;
		//Create an arraylist of the tileset so we can cross of the ones whose position has been
		//found, this will make the conversion well defined
		ArrayList<Character> tileset= new ArrayList<Character>();
		for(int i=0;i<order;i++){
			//add the character to the tileset
			tileset.add(tiles[i]);
			//set the coordinates in the answer to unasigned (max value)
			answer[i][0]=Integer.MAX_VALUE;
			answer[i][1]=Integer.MAX_VALUE;
		}
		//Traverse the matrix, and store the coordinates of each character found in the corresponding
		//Space in the compact representation
		//Since we traverse in order and allways take the tiles in the set in order two equivalent grids
		//will be translated into identical compact representations
		for(int i=0;i<fullRep.length;i++){
			for(int j=0; j<fullRep[i].length;j++){
				char thisTile=fullRep[i][j];
				if(thisTile!=0){
					int tileInd =tileset.indexOf(thisTile);
					answer[tileInd][0]=i;
					answer[tileInd][1]=j;
					//remove the tile from the tileset, so future intances of the same character
					//will be assigned to later appearances of the character in the tile list
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
		//check that all tiles have been placed
		for(int i=0;i<state.length;i++){
			if (state[i][0]==Integer.MAX_VALUE && state[i][1]==Integer.MAX_VALUE){
				return false;
			}
		}
		char[][] grid= unpackState(state);
		
		//check that all maximal continuous strings in horizontal or vertical direction are in the dictionary
		//traverse the array horizontally and vertically appending characters to a current word,
		//once the word is complete (next character is empty) check for it in the dictionary, if it is illegal return false
		
		//Check vertically
		
		for (int i=0; i<grid.length;i++){
			String currentword="";
			for(int j=0;j<grid[i].length;j++){
				char tile=grid[i][j];
				if(tile!=0){
					//if the character is not empty then add it to the current word
					currentword+=tile;
				}else if (currentword.length()>1){
					//if the character is empty and the current word is not empty then a word has been completed
					Boolean exists=checkWord(currentword);
					if(!exists){
						return false;
					}
					currentword="";
				}
			}
		}
		//check horizontally
		for (int j=0; j<grid[0].length;j++){
			String currentword="";
			for(int i=0;i<grid.length;i++){
				char tile=grid[i][j];
				if(tile!=0){
					//if the character is not empty then add it to the current word
					currentword+=tile;
				}else if (currentword.length()>1){
					//if the character is empty and the current word is not empty then a word has been completed
					Boolean exists=checkWord(currentword);
					if(!exists){
						
						return false;
					}
					currentword="";
				}
			}
		}
		//if no violations were found then the state is a solution
		return true;
	}
	//This method takes an input word and returns true if it is found in the dictionary, false otherwise
	//Since the dictionary is ordered the method uses binary search to optimize the speed of the search
	private Boolean checkWord(String currentword) {
		int index = Collections.binarySearch(dictionary,currentword);
		if (index>=0){
			return true;
		}else{
			return false;
		}
	}

	
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
					System.out.println("this tile:" +thisTile);
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
		//if no tiles have been place no available positions will be found, in this case make 0,0 the only available position
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

	@Override
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

	@Override
	public void printState(int[][] state) {
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
			System.out.println(line);
		}
	}

	@Override
	public Node getStartNode() {
		// TODO Auto-generated method stub
		return startNode;
	}
	
	

}
