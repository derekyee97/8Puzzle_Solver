
import java.io.IOException;
import java.util.*;
public class puzzle_search_UI
{
	public static void main(String[] args)
	{
		PuzzleEightBuilder builder=new PuzzleEightBuilder();
		int[]totalh1Nodes=new int[200],totalh2Nodes=new int[200];
		double[]totalh1Time=new double[200],totalh2Time=new double[200];
		Scanner s=new Scanner(System.in);
		int choice=menu(s);    //does all checking 
		if(choice==1)
		{
			int loop=getNumTimes();
			System.out.println("Testing randomely generated puzzles.");
			
			for(int i=0;i<loop;i++)
			{
				System.out.println("\nIteration: "+(i+1)+" of solving with heuristic 1 ");
				builder.createRandomPuzzle();
				builder.solveUsingH1();
				if(totalh1Nodes[builder.getD()]!=0) //if not 0 take average
				{
					
					totalh1Nodes[builder.getD()]=(totalh1Nodes[builder.getD()]+builder.getNumNodes())/2;
					totalh1Time[builder.getD()]=(totalh1Time[builder.getD()]+builder.getTime())/2;
				}
				else
				{
					
					totalh1Nodes[builder.getD()]=builder.getNumNodes();
					totalh1Time[builder.getD()]=builder.getTime();
				}
				builder.reset();
				
				System.out.println("\nIteration: "+(i+1)+" of solving with heuristic 2 ");
				builder.solveUsingH2();
				if(totalh2Nodes[builder.getD()]!=0) //if not 0 take average
				{
					
					totalh2Nodes[builder.getD()]=(totalh2Nodes[builder.getD()]+builder.getNumNodes())/2;
					totalh2Time[builder.getD()]=(totalh2Time[builder.getD()]+builder.getTime())/2;
				}
				else
				{
					System.out.println("H2 NODE GENERATED: "+builder.getNumNodes());
					totalh2Nodes[builder.getD()]=builder.getNumNodes();
					totalh2Time[builder.getD()]=builder.getTime();
				}
				builder.reset();
				
			}
			System.out.println("\nRESULTS: ========================================================");
			for(int i=2;i<40;i++)
			{
				System.out.println("FOR DEPTH: "+i);
				System.out.println("\nHeuristic 1 nodes generated: "+totalh1Nodes[i]);
				System.out.println("Time to solve using H1:" +totalh1Time[i]+" ms");
				System.out.println("Heuristic 2 nodes generated: "+totalh2Nodes[i]);
				System.out.println("Time to solve using H2:" +totalh2Time[i]+" ms\n");
				System.out.println("==============================");
			}
		}
		else //choice 2
		{
			int[] input=createUserInputPuzzle(builder);
			builder.createCustomizedPuzzle(input);
			builder.solveUsingH2();
			
			
		}
		
	}
	private static int[] createUserInputPuzzle(PuzzleEightBuilder builder) 
	{
		Scanner s=new Scanner(System.in);
		int[] holder=new int[]{9,9,9,9,9,9,9,9,9}; //initialized int array with not allowed value
		System.out.println("Please enter the sequence of integers to represent the puzzle going from left to right\nand top to bottom, where 0 represents the hole in the 3x3 puzzle.");
		System.out.println("An example output could be ' 1 3 4 2 5 6 0 7 8	'");
		String entered=s.nextLine();
		String[] splittedInput=entered.split("\\s+");
		if(splittedInput.length!=9)
		{
			System.out.println("Invalid Input length, try again");
			createUserInputPuzzle(builder);
			
		}
		for(int i=0;i<splittedInput.length;i++) //loop through entire string array to validate and place input
		{
			if(isInt(splittedInput[i])) //if correct formatted 
			{
				if(Integer.parseInt(splittedInput[i])>8)
				{
					System.out.println("Invalid input, please try again");
				}
				else
				{
					
					for(int j=0;j<holder.length;j++)  
					{
						if(Integer.parseInt(splittedInput[i])==holder[j]) //checking for duplicates
						{
							System.out.println("Duplicate number found, please enter a valid input.");
							createUserInputPuzzle(builder);
						}
						else
						{
							if(holder[j]==9) //if reach here then dont have duplicate
							{
								
								holder[j]=Integer.parseInt(splittedInput[i]);
								break;
							}
						}
					}
					
				}
				
			}
			else
			{
				System.out.println("Invalid input, please try again.");
				createUserInputPuzzle(builder);
			}
		}
		if(builder.checkPuzzle(holder))
		{
			return holder;
		}
		else
		{
			System.out.println("Puzzle not solvable, try again.");
			createUserInputPuzzle(builder);
		}
		return null;
	}
	
	private static int menu(Scanner s)
	{
		System.out.println("Welcome to the puzzle 8 search menu!\nPlease enter one the integer corresponding to the action you would like to perform: ");
		System.out.println("1) Generate 8-puzzle and solve using both heuristics 200 times");
		System.out.println("2) Input your own 8-puzzle configuration and solve using both heuristics");
		System.out.println();
		
			String choice=s.next();
			if(isInt(choice))
			{
				if(Integer.parseInt(choice)==1||Integer.parseInt(choice)==2)
				{
					return Integer.parseInt(choice);
				}
				System.out.println("Invalid choice");
				menu(s);
			}
			else
			{
				System.out.println("Invalid input");
				menu(s);
			}
			return 0;
	}
	private static boolean isInt(String s)
	{
		try  
		  {  
		    int d = Integer.parseInt(s);  
		  }  
		  catch(NumberFormatException nfe)  
		  {  
		    return false;  
		  }  
		  return true;  
	}
	private static int getNumTimes()
	{
		Scanner s=new Scanner(System.in);
		System.out.println("Please type in an integer corresponding to how many times you want to run the program.");
		String result=s.next();
		if(isInt(result))
		{
			if(Integer.parseInt(result)<1)
				{
					System.out.println("Invalid.");
					getNumTimes();
				}
			else
			{
				return Integer.parseInt(result);
			}
		}
		else
		{
			System.out.println("Invalid.");
			getNumTimes();
		}
		return 100;
	}
	
}
	