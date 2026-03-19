import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Queue;

//if normal committing doesn's work: 
//to commit to github I need to right-click the project and then click "team" and then "push branch master"
//then click the "force override" option then "ok" (or whatever there is to commit it)

/*
 * Worked with Aarna P, Anya Y, Emily S for file reading and exceptions
 */

public class Runner {
	
	public static String[][] mapArray; //creates the 2D array to store the map
	public static int rows; 
	public static int cols;
	public static int nums; //number of sections
	
	public static void main(String[] args) {
		//change this value to change the text file and run the code
		try {
			readFile("mediumMap2");	
			solveQueue("mediumMap2");
			solveStack("mediumMap2");
			//backtrack("mediumMap2");
			printMap("mediumMap2");
		} catch (IllegalMapCharacterException e){
			System.out.println(e.getMessage());
		} catch (IncompleteMapException e) {
			System.out.println(e.getMessage());
		} catch (IncorrectMapFormatException e) {
			System.out.println(e.getMessage());
		} catch (IllegalCommandLineInputsException e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	public static void readFile(String fileName) throws IllegalMapCharacterException,  IncompleteMapException, IncorrectMapFormatException, IllegalCommandLineInputsException{
		try {
			File file = new File(fileName);
			Scanner scanner = new Scanner(file);

			//getting the amount of rows, cols, and nums and saving them to variables
			rows = Integer.parseInt(scanner.next());
			cols = Integer.parseInt(scanner.next());
			nums = Integer.parseInt(scanner.next());
			
			//check integers for the first row
			if(rows <= 0 || cols <= 0 || nums <= 0){
				throw new IncorrectMapFormatException("IncorrectMapFormatException");
			}
			
			mapArray = new String[rows*nums][cols];
			
				for(int r = 0; r < mapArray.length; r++) {
					String newRow = scanner.next();
					
					if(newRow.length() != cols) {
						throw new IncompleteMapException("IncompleteMapException");
					}
					
					for(int c = 0; c < cols; c++) {
						//attempting the IllegalMapCharacterException
						if(!(newRow.substring(c, c+1).equals("w")) && !(newRow.substring(c, c+1).equals("$")) && !(newRow.substring(c, c+1).equals("|")) && !(newRow.substring(c, c+1).equals("@")) && !(newRow.substring(c, c+1).equals("."))) {
							throw new IllegalMapCharacterException("IllegalMapCharacterException");
						}else {
							mapArray[r][c] = newRow.substring(c, c+1); //getting the specific value through a substring

						}
					}
				}
				
//				for(int r = 0; r < mapArray.length; r++) {
//					String newRow = scanner.next();
//					for(int c = 0; c < cols; c++) {
//						mapArray[r][c] = newRow.substring(c, c+1); //getting the specific value through a substring
//					}
//				}
				
			System.out.println(Arrays.deepToString(mapArray)); //printing map
			scanner.close();

			
		
		}catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File Not Found!");
		}
	}
	
	public static void readQueueFile(String fileName) {
		File file = new File(fileName);
		try {
			Scanner scanner = new Scanner(file);
			
			//getting the amount of rows, cols, and nums and saving them to variables
			rows = Integer.parseInt(scanner.next());
			cols = Integer.parseInt(scanner.next());
			nums = Integer.parseInt(scanner.next());
			
			mapArray = new String[rows*nums][cols];
			
			
			while(scanner.hasNext()) {
				String value = scanner.next();
				//save the row, col, and num sections to variables
				int row = Integer.parseInt(scanner.next());
				int col = Integer.parseInt(scanner.next());
				int num = Integer.parseInt(scanner.next());
				//the row location is equal to the row in the file plus the total amount of rows already read
				
				mapArray[row + (rows*num)][col] = value;
				
			}
			//iterates through map and fills all null values with a period
			
			for(int r = 0; r < mapArray.length; r++) {
				for(int c = 0; c < cols; c++) {
					if(mapArray[r][c] == null) {
						mapArray[r][c] = ".";
					}
				}
			}
			
			System.out.println(Arrays.deepToString(mapArray));
			scanner.close();
			

			
		
		}catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File Not Found!");
		}
	}
	
	public static void solveQueue(String fileName) {
	    //the queue - AKA. where I need to search
	    Queue<int[]> queue = new LinkedList<>();
	    long startTime = System.nanoTime(); //for timer
	    
	    //keeping track (in a 2D array) of where I have been so I don't walk in circles
	    boolean[][] visited = new boolean[rows * nums][cols];

	    //Find Wolverine's start position
	    int startR = 0;
	    int startC = 0;
	    for (int r = 0; r < rows * nums; r++) {
	        for (int c = 0; c < cols; c++) {
	            if (mapArray[r][c].equals("w")) {
	                startR = r;
	                startC = c;
	            }
	        }
	    }

	    //start searching
	    
	    //create a new coordinate pair for the starting point and put it at the end of my queue.
	    queue.add(new int[]{startR, startC});
	    visited[startR][startC] = true;

	    while (!queue.isEmpty()) {
	    	//the .poll() method grabs the item from the front of the queue (can only be used in queues)
	    	//and removes it from the queue at the same time so we don't have to use 2 different methods (add and remove)
	    	//got assistance from google for the .poll() method
	    	//tip: use .pop() instead of .poll() for stacks
	        int[] current = queue.poll();
	        int currR = current[0];
	        int currC = current[1];

	        //see if we found the buck
	        if (mapArray[currR][currC].equals("$")) {
	        	long endTime = System.nanoTime();
	            double duration = (endTime - startTime) / 1_000_000_000.0; // Convert to seconds
	            
	            System.out.println("Queue approach: Found it!");
	            System.out.println("Total Runtime (Queues): " + duration + " seconds");
	            return; 
	        }

	        //check North(up), South(down), East(right), West(left)
	        //check the immediate locations, so one up, one down, one left, one right
	        int[][] directions = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};
	        
	        for (int[] dir : directions) {
	            int nextR = currR + dir[0];
	            int nextC = currC + dir[1];

	            //check if it is in the map
	            if (nextR >= 0 && nextR < rows * nums && nextC >= 0 && nextC < cols) {
	                //check if it is walkable and hasn't already been visited
	                if (!mapArray[nextR][nextC].equals("@") && !visited[nextR][nextC]) {
	                    visited[nextR][nextC] = true;
	                    queue.add(new int[]{nextR, nextC});
	                }
	            }
	        } 
	    }
	    // If the list is empty and no coin was found
	    long endTime = System.nanoTime();
	    double duration = (endTime - startTime) / 1_000_000_000.0;
	    
	    System.out.println("Queue approach: The Wolverine Store is closed.");
	    System.out.println("Total Runtime (Queues): " + duration + " seconds");
	}
	
	public static void solveStack(String fileName) {
	    //stack of places to go search
	    Stack<int[]> stack = new Stack<>();
	    long startTime = System.nanoTime(); //for timer
	    
	    //track spots visited so I don't walk in circles
	    boolean[][] visited = new boolean[rows * nums][cols];
	    
	    // 3. Find 'w' (Start Position)
	    int startR = 0, startC = 0;
	    for (int r = 0; r < rows * nums; r++) {
	        for (int c = 0; c < cols; c++) {
	            if (mapArray[r][c].equals("W")) {
	                startR = r; startC = c;
	            }
	        }
	    }

	    //put my stating value in the stack
	    stack.push(new int[]{startR, startC});
	    visited[startR][startC] = true;
	    
	    //begin searching
	    while (!stack.isEmpty()) {
	        int[] current = stack.pop(); 
	        int currR = current[0];
	        int currC = current[1];

	        //check if we found the buck
	        if (mapArray[currR][currC].equals("$")) {
	            long endTime = System.nanoTime();
	            double duration = (endTime - startTime) / 1_000_000_000.0; // Convert to seconds
	            
	            System.out.println("Stack approach: Found it!");
	            System.out.println("Total Runtime (Stacks): " + duration + " seconds");
	            return; 
	        }

	        //check neighbors in order: North(up), South(down), East(right), West(left)
	        int[][] directions = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};
	        
	        for (int[] dir : directions) {
	            int nextR = currR + dir[0];
	            int nextC = currC + dir[1];

	            // Boundary Check
	            if (nextR >= 0 && nextR < rows * nums && nextC >= 0 && nextC < cols) {
	                //make sure it isn't a wall '@'
	                if (!mapArray[nextR][nextC].equals("@") && !visited[nextR][nextC]) {
	                    visited[nextR][nextC] = true;
	                    stack.push(new int[]{nextR, nextC}); // .push() adds to the top
	                }
	            }
	        }
	    }
	    long endTime = System.nanoTime();
	    double duration = (endTime - startTime) / 1_000_000_000.0;
	    
	    System.out.println("Stack approach: The Wolverine Store is closed.");
	    System.out.println("Total Runtime (Stacks): " + duration + " seconds");
	}
	
	/*
	 * Still work in progress
	 */
	//path marking
	//this is where we draw out the path taken using plus signs '+'
	public static void path(String[][] cameFrom, int startR, int startC, int coinR, int coinC, String fileName) {
	    int currR = coinR;
	    int currC = coinC;

	    // Follow the trail until we hit the 'W' coordinates
	    while (!(currR == startR && currC == startC)) {
	        String str = cameFrom[currR][currC];
	        
	        //Split it into row and col
	        String[] parts = str.split(",");
	        currR = Integer.parseInt(parts[0]);
	        currC = Integer.parseInt(parts[1]);

	        //Mark with '+' if it's not the starting 'w'
	        if (!mapArray[currR][currC].equals("w")) {
	            mapArray[currR][currC] = "+";
	        }
	    }
	}
	//method to print out my map to console
	public static void printMap(String fileName) {
	    for (int r = 0; r < rows * nums; r++) {
	        for (int c = 0; c < cols; c++) {
	            System.out.print(mapArray[r][c]);
	        }
	        System.out.println();
	    }
	}

}
