/*****************
 * Derek Yee
 * Cs4200 Project 1 
 * Due: 3/3/2019 by 11:59pm
 ****************/
import java.util.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PuzzleEightBuilder 
{
	private int[] puzzle;
	private PriorityQueue<node>frontier=new PriorityQueue<node>();//from algorithm
	private ArrayList<node>answer=new ArrayList<node>();
	//using hash sets to take advantage of O(1) lookup as we are constantly checking to see if we already have seen it
	private HashSet<Integer>frontierCheck=new HashSet<Integer>(),explored=new HashSet<Integer>();
	private int nodesCreated=0; // will keep track of how many nodes created as says by prompt
	private double time=0;
	public PuzzleEightBuilder() //default constructor 
	{
		puzzle=new int[9];
	}
	public int[] getPuzzle()
	{
		return puzzle.clone();
	}
	public void createRandomPuzzle()
	{
		int[] temp=new int[]{0,1,2,3,4,5,6,7,8};
		temp=shufflePuzzle(temp);
		while(!checkPuzzle(temp))
		{
			temp=shufflePuzzle(temp);
		}
		puzzle=temp;
		System.out.println("Resulting Puzzle Created: ");
		System.out.println();
		printPuzzle(puzzle);
		
	}
	private int[] shufflePuzzle(int[] arrayToBeShuffled)
	{
		    Random rnd = ThreadLocalRandom.current();
		    for (int i = arrayToBeShuffled.length - 1; i > 0; i--)
		    {
		      int index = rnd.nextInt(i + 1);
		      // Simple swap
		      int a = arrayToBeShuffled[index];
		      arrayToBeShuffled[index] = arrayToBeShuffled[i];
		      arrayToBeShuffled[i] = a;
		    }
		    return arrayToBeShuffled;
	}
		
	
	public void createCustomizedPuzzle(int[] selection)
	{
		puzzle=selection;
		System.out.println("Resulting Puzzle Created: ");
		System.out.println();
		printPuzzle(puzzle);
		
	}
	public void solveUsingH1()
	{
		//initial setup for algorithm
		double start=System.nanoTime()/1000000; //taking time in milliseconds
		node initial=new node();
		initial.state=puzzle;
		addToFrontierh1(initial);
		node curr=null; //node we are looking at
		
		while(!reachGoalState(curr))
		{
			curr=frontier.remove();//get lowest cost in queue
			frontierCheck.remove(curr.ID);
			explored.add(curr.ID); // we are exploring it so put in explored set
			explore1(curr);
						
		}
		//reached end of solution
		answer.add(curr);
		while(curr.previous!=null) //going backwards to get final solution
		{
			answer.add(curr.previous);
			curr=curr.previous;
		}
		double end=System.nanoTime()/1000000;
		time=end-start;
		printSolution();
		
	}
	public void solveUsingH2()
	{
		//initial setup for algorithm
		double start=System.nanoTime()/1000000;		
		node initial=new node();
		initial.state=puzzle;
		addToFrontierh2(initial);
		node curr=null; //node we are looking at
		
		while(!reachGoalState(curr))
		{
			curr=frontier.remove();//get lowest cost in queue
			frontierCheck.remove(curr.ID);
			explored.add(curr.ID); // we are exploring it so put in explored set
			explore2(curr);
						
		}
		//reached end of solution
		answer.add(curr);
		while(curr.previous!=null) //going backwards to get final solution
		{
			answer.add(curr.previous);
			curr=curr.previous;
		}
		double end=System.nanoTime()/1000000;	
		time=end-start;
		printSolution();
		
	}
	public int getD()
	{
		return answer.size()-1;
	}
	public int getNumNodes()
	{
		return nodesCreated;
	}
	public double getTime()
	{
		return time;
	}
	public void printPuzzle(int[] puzzle)
	{
		
		for(int i=0;i<puzzle.length;i++)
		{
			System.out.print(puzzle[i]+"  ");
			if(i==2||i==5)
			{
				System.out.println();
			}
		}
	}
	private void printSolution()
	{
		System.out.println("\n");
		node curr=null;
		int counter=0;
		for(int i=answer.size()-1;i>=0;i--)
		{
			System.out.println("Step: "+counter+": \n================ \n");
			counter++;
			curr=answer.get(i);
			printPuzzle(curr.state);
			System.out.println("\n");
		
		}
	}
	
	public int numMisplacedTiles(int[] board) //heuristic 1 
	{
		int misplacedTiles=0;
		for(int i=1;i<board.length;i++) //
		{
			if(board[i]!=i)  //tile should coordinate to its index in array if in "proper"order
			{
				misplacedTiles++;
			}
		}
		return misplacedTiles;
	}
	
	public int sumTotalDistanceMisplacedTiles(int[] board)//heuristic 2
	{
		int totalDistance=0;
		for(int i=0;i<board.length;i++) //going through board
		{
			int totalXDistance=(i/3)-(board[i]/3);  //calculating how far away form true x position it is
			if(totalXDistance<0)
			{
				totalXDistance=Math.abs(totalXDistance); // distance cant be negative
			}
			int totalYDistance=(i%3)-(board[i]/3);
			if(totalYDistance<0)
			{
				totalYDistance=Math.abs(totalYDistance);
			}
			totalDistance=totalDistance+totalXDistance+totalYDistance;
			
		}
		return totalDistance;
	}
	 
	public boolean checkPuzzle(int[] inputPuzzle)
	{
		int numOfInversion=0;
		for(int i=0;i<inputPuzzle.length-1;i++)
		{
			for(int j=i+1;j<inputPuzzle.length;j++)
			{
				if(inputPuzzle[i]>inputPuzzle[j]) //check for inversion
				{
					numOfInversion+=1;
				}
			}
			if(inputPuzzle[i]==0 && i%2==1)  //check for if the element is 0 the "hole"
			{
				numOfInversion+=1;
			}
		}
		if(numOfInversion%2==0)  //even number of inversions are solvable
		{
			return true;
		}
		else  //odd number of inversions are not
		{
			return false;
		}
	}
	private int getHole(node currNode) //will be used to find where can move with curr node
	{									//returns index of hole
		for(int i=0;i<currNode.state.length;i++)
		{
			if(currNode.state[i]==0)
			{
				return i;   
			}
		}
		return 0;
	}
		
	//actions to perform**************
	private node moveUp(node curr)
	{
		int hole=getHole(curr); 
		if(hole<3)      //top= index 0-2 so if on this level cant move up
		{
			return null;
		}
		else
		{
			int newPosition=hole-3;      //will be index of "above"position
			int[] update=curr.state.clone();  //need to create new board
			swap(update,hole,newPosition);  //swap positions for new board
			node newNode=new node();
			newNode.state=update;
			newNode.previous=curr;
			newNode.totalMoves=curr.totalMoves+1;
			generateID(newNode);
			return newNode;
		}
	}
	
	private node moveDown(node curr)
	{
		int hole=getHole(curr); 
		if(hole>=6)      //bottom= index 6-8 so if on this level cant move down
		{
			return null;
		}
		else
		{
			int newPosition=hole+3;     //index of new position below
			int[] update=curr.state.clone();  //need to create new board
			swap(update,hole,newPosition);  //swap positions for new board
			node newNode=new node();
			newNode.state=update;
			newNode.previous=curr;
			newNode.totalMoves=curr.totalMoves+1;
			generateID(newNode);
			return newNode;
		}
	}
	
	private node moveLeft(node curr)
	{
		int hole=getHole(curr); 
		if(hole==0||hole==3||hole==6)      //left= index 0,3,6 so if on this level cant move left
		{
			return null;
		}
		else
		{
			int newPosition=hole-1;     //index of new position below move one unit left
			int[] update=curr.state.clone();  //need to create new board
			swap(update,hole,newPosition);  //swap positions for new board
			node newNode=new node();
			newNode.state=update;
			newNode.previous=curr;
			newNode.totalMoves=curr.totalMoves+1;
			generateID(newNode);
			return newNode;
		}
	}
	private node moveRight(node curr)
	{
		int hole=getHole(curr); 
		if(hole==2||hole==5||hole==8)      //right=index 2,5,8 so if on this level cant move right
		{
			return null;
		}
		else
		{
			int newPosition=hole+1;     //index of new position one unit to the right
			int[] update=curr.state.clone();  //need to create new board
			swap(update,hole,newPosition);  //swap positions for new board
			node newNode=new node();
			newNode.state=update;
			newNode.previous=curr;
			newNode.totalMoves=curr.totalMoves+1;
			generateID(newNode);  //helps keep track of if we have seen it or not
			return newNode;
		}
	}
	
	private boolean reachGoalState(node curr)
	{
		if(curr==null) //in case for initial state in while loop
		{
			return false;
		}
		for(int i=0;i<9;i++)
		{
			if(curr.state[i]!=i)
			{
				return false;
			}
		}
		return true;
	}
	//********************************
	
	private void addToFrontierh1(node curr)  //before adding to frontier have to check if we have seen this node already
	{
		if(curr!=null)  
		{
			if(!frontierCheck.contains(curr.ID))
			{
				if(!explored.contains(curr.ID))
				{
					curr.estimatedCost=numMisplacedTiles(curr.state)+curr.totalMoves; //the f(n)+g(n)
					frontier.add(curr);
					frontierCheck.add(curr.ID);
					nodesCreated++;
				}
			}
		}
	}
	
	private void addToFrontierh2(node curr)  //before adding to frontier have to check if we have seen this node already
	{
		if(curr!=null)  
		{
			if(!frontierCheck.contains(curr.ID))
			{
				if(!explored.contains(curr.ID))
				{
					curr.estimatedCost=sumTotalDistanceMisplacedTiles(curr.state)+curr.totalMoves; //the f(n)+g(n)
					frontier.add(curr);
					frontierCheck.add(curr.ID);
					nodesCreated++;
				}
			}
		}
	}
	//in algorithm we explore/expand the node we are looking at
	private void explore1(node curr)
	{
		addToFrontierh1(moveLeft(curr));
		addToFrontierh1(moveRight(curr));
		addToFrontierh1(moveUp(curr));
		addToFrontierh1(moveDown(curr));
	}
	private void explore2(node curr)
	{
		addToFrontierh2(moveLeft(curr));
		addToFrontierh2(moveRight(curr));
		addToFrontierh2(moveUp(curr));
		addToFrontierh2(moveDown(curr));
	}
	//function used to update state more easily
	private void swap(int[] array,int index1,int index2)
	{
		int value=array[index1];
		array[index1]=array[index2];
		array[index2]=value;
	}
	//will be usefull for storing saved state in hash sets for lookup
	private void generateID(node curr)
	{
		String id="";
		for(int i=0;i<curr.state.length;i++)
		{
			id+=curr.state[i];
		}
		curr.ID=Integer.parseInt(id);
	}
	//node class to keep track of all states and moves
	private class node implements Comparable<node>
	{
		int[] state; //keeps track of recorded state
		int totalMoves=0,estimatedCost=0,ID;
		node previous=null;
		
		@Override //override to use java's priority queue to organize nodes by least cost first
		public int compareTo(node otherNode) 
		{
			int result=estimatedCost-otherNode.estimatedCost; //checks which one is greater
			return result;
		}
	}
	public void reset()
	{
		frontier=new PriorityQueue<node>();
		answer=new ArrayList<node>();
		frontierCheck=new HashSet<Integer>();
		explored=new HashSet<Integer>();
		nodesCreated=0;
		time=0;
	}
}