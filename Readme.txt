//Pablo Gonzalez Martinez
//Language:Java
//Runtime Environment:Java-SE 1.8 (Java-SE 1.8.0_73)
Readme for stage 1 submission of the final project for Artificial Inteligence

This submission contains:
Problem.java and Problem.bin: source and compiled files for the Problem class
BananagramsProblem.java and BananagramsProblem.bin: source and compiled files for the BananagramsProblem class
NPuzzleProblem.java and NPuzzleProblem.bin: source and compiled files for the NpuzzleProblem class
MazeProblem.java and MazeProblem.bin: source and compiled files for the MazeProblem class
HanoiProblem.java and HanoiProblem.bin: source and compiled files for the HanoiProblem class
Node.java and Node.bin: source and compiled files for the Node class
Tester.java and Tester.bin: source and compiled files for the Tester class, thsi is the class to be run, it tests the functionality of the different prblem classes.
data/dictionary.txt: a text file with the scrabble dictionary of all valid words to be used by the bananagrams problem
Readme.txt: this file

I have included both the source files (*.java) and the compiled versions (*.class)
To run the program navigate to the containing directory within the terminal and run:
	
	$java Tester <problemnanme>
	
The <problemname> variable can take four values, it chooses which problem to test
Bananagrams
Hanoi
NPuzzle
Maze

The response is printed to console standard output so it will appear in the terminal
If modifications are made the code will have to be recompiled for them to take effect, run:

The tester app makes three tests and post the results:
	It first generates and prints all successors of the root node
	It then generates nodes at depths 2 through 4 (by taking always the first available action) and prints then
	It then takes a solution to the problem prints it and puts it throught the goal test

All tests worked as desired.
I present here the sample output for all of the problems

Pablo (master *) BananagramsAI $ java Tester Bananagrams
Start
----
----
----
----
Successor 1
---
-N-
---
Successor 2
---
-A-
---
Successor 3
---
-G-
---
Successor 4
---
-O-
---
Successor 5
---
-D-
---
Successor 6
---
-S-
---
Successor 7
---
-E-
---
Successor 8
---
-E-
---
Successor 9
---
-P-
---
Depth 2 node
----
-NA-
----
Depth 3 node
----
-NA-
-G--
----
Depth 4 node
-----
-ONA-
--G--
-----
Depth 5 node
-----
-ONA-
-DG--
-----

Solved state: 
------
-N----
-A----
-GODS-
---E--
---E--
---P--
------
The test returns: true
Pablo (master *) BananagramsAI $ java Tester Hanoi
Start
 |  |  |  | 
 1  |  |  | 
 2  |  |  | 
 3  |  |  | 
 4  |  |  | 
 5  |  |  | 
 6  |  |  | 
 _  _  _  _ 
Successor 1
 |  |  |  | 
 |  |  |  | 
 2  |  |  | 
 3  |  |  | 
 4  |  |  | 
 5  |  |  | 
 6  1  |  | 
 _  _  _  _ 
Successor 2
 |  |  |  | 
 |  |  |  | 
 2  |  |  | 
 3  |  |  | 
 4  |  |  | 
 5  |  |  | 
 6  |  1  | 
 _  _  _  _ 
Successor 3
 |  |  |  | 
 |  |  |  | 
 2  |  |  | 
 3  |  |  | 
 4  |  |  | 
 5  |  |  | 
 6  |  |  1 
 _  _  _  _ 
Depth 2 node
 |  |  |  | 
 |  |  |  | 
 |  |  |  | 
 3  |  |  | 
 4  |  |  | 
 5  |  |  | 
 6  1  2  | 
 _  _  _  _ 
Depth 3 node
 |  |  |  | 
 |  |  |  | 
 |  |  |  | 
 |  |  |  | 
 4  |  |  | 
 5  |  |  | 
 6  1  2  3 
 _  _  _  _ 
Depth 4 node
 |  |  |  | 
 |  |  |  | 
 |  |  |  | 
 1  |  |  | 
 4  |  |  | 
 5  |  |  | 
 6  |  2  3 
 _  _  _  _ 
Depth 5 node
 |  |  |  | 
 |  |  |  | 
 |  |  |  | 
 |  |  |  | 
 4  |  |  | 
 5  |  |  | 
 6  1  2  3 
 _  _  _  _ 

Solved state: 
 |  |  |  | 
 |  |  |  1 
 |  |  |  2 
 |  |  |  3 
 |  |  |  4 
 |  |  |  5 
 |  |  |  6 
 _  _  _  _ 
The test returns: true
Pablo (master *) BananagramsAI $ java Tester NPuzzle
Start
| 1  2  4 |
| 3  9  6 |
| 5  7  8 |
Successor 1
| 1  2  4 |
| 9  3  6 |
| 5  7  8 |
Successor 2
| 1  2  4 |
| 3  6  9 |
| 5  7  8 |
Successor 3
| 1  9  4 |
| 3  2  6 |
| 5  7  8 |
Successor 4
| 1  2  4 |
| 3  7  6 |
| 5  9  8 |
Depth 2 node
| 1  2  4 |
| 3  9  6 |
| 5  7  8 |
Depth 3 node
| 1  2  4 |
| 9  3  6 |
| 5  7  8 |
Depth 4 node
| 1  2  4 |
| 3  9  6 |
| 5  7  8 |
Depth 5 node
| 1  2  4 |
| 9  3  6 |
| 5  7  8 |

Solved state: 
| 1  2  3 |
| 4  5  6 |
| 7  8  9 |
The test returns: true
Pablo (master *) BananagramsAI $ java Tester Maze
Start
00100&
001010
000001
0*0100
Successor 1
00100&
001010
000001
00*100
Successor 2
00100&
001010
0*0001
000100
Successor 3
00100&
001010
000001
*00100
Depth 2 node
00100&
001010
00*001
000100
Depth 3 node
00100&
001010
000*01
000100
Depth 4 node
00100&
001010
0000*1
000100
Depth 5 node
00100&
001010
000001
0001*0

Solved state: 
00100*
001010
000001
000100
The test returns: true