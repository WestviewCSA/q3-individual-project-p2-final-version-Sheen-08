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
 * Got help from google for the .poll() method and how to get timing working for queues 
 * (which i then implemented in stacks as well) and command line arguments
 */


/*
 * Stuff I still have left in my project:
 * 1. Path method to represent the path with '+' signs
 * 2. "--Opt" method where i need to use the optimal search method
 * 3. Open walkways
 * 4. "--Incoordinate" and "--Outcoordinate" methods
 */

public class p1 {
	
	public static String[][] mapArray; //creates the 2D array to store the map
	public static int rows; 
	public static int cols;
	public static int nums; //number of sections
	public static boolean showTime = false;
    public static boolean isOutcoordinate = false;
    public static boolean isIncoordinate = false;
	
	public static void main(String[] args) {
        /*
         * How to run the code for testing with command line arguments:
         * Click on the arrow by the run button, "Run Configurations...", "Arguments" tab
         */
        String fileName = "";
        boolean useStack = false;
        boolean useQueue = false;
        boolean useOpt = false; 

        try {
            //check if we have any arguments
            if (args.length == 0) {
                throw new IllegalCommandLineInputsException("No arguments!");
            }

            //loop through args to set our "switches"
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];

                if (arg.equals("--Stack")) {
                    useStack = true;
                } else if (arg.equals("--Queue")) {
                    useQueue = true;
                } else if (arg.equals("--Opt")) {
                    useOpt = true; 
                } else if (arg.equals("--Time")) {
                    showTime = true;
                } else if (arg.equals("--Incoordinate")) {
                    isIncoordinate = true;
                } else if (arg.equals("--Outcoordinate")) {
                    isOutcoordinate = true;
                } else if (arg.equals("--help")) {
                    printHelp();
                    return;
                } else if (!arg.startsWith("--")) {
                    fileName = arg;
                } else {
                    throw new IllegalCommandLineInputsException("Unknown switch: " + arg);
                }
            }

            //Validation: Ensure only one search method is picked
            int count = 0;
            if (useStack) count++;
            if (useQueue) count++;
            if (useOpt) count++;

            if (count > 1) {
                throw new IllegalCommandLineInputsException("Cannot use more than one search method at the same time.");
            }
            if (count == 0) {
                throw new IllegalCommandLineInputsException("You must specify either --Stack, --Queue, or --Opt.");
            }
            if (fileName.isEmpty()) {
                throw new IllegalCommandLineInputsException("No map file specified.");
            }

            //Run the actual program logic
            // Use the correct read method based on the switch
            if (isIncoordinate) {
                readCoordinateFile(fileName);
            } else {
                readFile(fileName);
            }
            
            // Run search (Queue and Opt both use BFS)
            if (useQueue || useOpt) {
                solveQueue(fileName); 
            } else {
                solveStack(fileName);
            }

            // Print final results based on Output format
            if (isOutcoordinate) {
                printCoordinates();
            } else {
                printMap(fileName);
            }
            
        } catch (IllegalMapCharacterException e){
            System.out.println(e.getMessage());
        } catch (IncompleteMapException e) {
            System.out.println(e.getMessage());
        } catch (IncorrectMapFormatException e) {
            System.out.println(e.getMessage());
        } catch (IllegalCommandLineInputsException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("unexpected error: " + e.getMessage());
        }
	}

	// A helper method for the '--help' switch
	public static void printHelp() {
	    System.out.println("Wolverine Maze Solver Help:");
	    System.out.println("Usage: java Runner [switches] [filename]");
	    System.out.println("Switches: ");
	    System.out.println("--Stack: Use stack-based searching");
	    System.out.println("--Queue: Use queue-based searching");
	    System.out.println("--Time: Show how long the search took");
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
	    String[][] cameFrom = new String[rows * nums][cols];
	    
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
	        	if(showTime) {
		            double duration = (endTime - startTime) / 1_000_000_000.0; // Convert to seconds
		            System.out.println("Total Runtime (Queues): " + duration + " seconds");
	        	}
	            path(cameFrom, startR, startC, currR, currC, fileName);
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
	            	if (!mapArray[nextR][nextC].equals("@") && !visited[nextR][nextC]) {
	                    visited[nextR][nextC] = true;
	                    cameFrom[nextR][nextC] = currR + "," + currC;
	                    queue.add(new int[]{nextR, nextC});
	                    
	                    if (mapArray[nextR][nextC].equals("|")) {
	                        int jumpR = (nextR + rows) % (rows * nums); 
	                        
	                        if (!visited[jumpR][nextC] && !mapArray[jumpR][nextC].equals("@")) {
	                            visited[jumpR][nextC] = true;
	                            cameFrom[jumpR][nextC] = nextR + "," + nextC; 
	                            queue.add(new int[]{jumpR, nextC});
	                        }
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
	}
	
	public static void solveStack(String fileName) {
	    //stack of places to go search
	    Stack<int[]> stack = new Stack<>();
	    long startTime = System.nanoTime(); //for timer
	    String[][] cameFrom = new String[rows * nums][cols];
	    
	    //track spots visited so I don't walk in circles
	    boolean[][] visited = new boolean[rows * nums][cols];
	    
	    // 3. Find 'w' (Start Position)
	    int startR = -1;
	    int startC = -1;
	    for (int r = 0; r < rows * nums; r++) {
	        for (int c = 0; c < cols; c++) {
	            if (mapArray[r][c].equals("w")) {
	                startR = r; startC = c;
	            }
	        }
	    }
	    if (startR == -1) {
	        System.out.println("Error: Wolverine ('W') was not found on the map!");
	        return;
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
	            if(showTime) {
	            	double duration = (endTime - startTime) / 1_000_000_000.0; // Convert to seconds
		            
		            System.out.println("Stack approach: Found it!");
		            System.out.println("Total Runtime (Stacks): " + duration + " seconds");
	            }
	            path(cameFrom, startR, startC, currR, currC, fileName);
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
	            	    cameFrom[nextR][nextC] = currR + "," + currC;
	            	    
	            	    // Add the "normal" move to the stack
	            	    stack.push(new int[]{nextR, nextC});
	            	    
	            	    // Check if the spot we just stepped on is a walkway
	            	    if (mapArray[nextR][nextC].equals("|")) {
	            	        // Find the same (r, c) on the next floor
	            	        int jumpR = (nextR + rows) % (rows * nums); 
	            	        
	            	        if (!visited[jumpR][nextC] && !mapArray[jumpR][nextC].equals("@")) {
	            	            visited[jumpR][nextC] = true;
	            	            
	            	            // Record that we got to the new floor FROM the walkway tile
	            	            cameFrom[jumpR][nextC] = nextR + "," + nextC; 
	            	            
	            	            // Add the "jumped" position to the stack
	            	            stack.push(new int[]{jumpR, nextC});
	            	        }
	            	    }
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
	    // Start from the tile that led to the coin
	    String leadToCoin = cameFrom[coinR][coinC];
	    if (leadToCoin == null) return; // No path found

	    String[] parts = leadToCoin.split(",");
	    int currR = Integer.parseInt(parts[0]);
	    int currC = Integer.parseInt(parts[1]);

	    // Backtrack until we hit Wolverine's start
	    while (!(currR == startR && currC == startC)) {
	        // Mark the current spot with +
	        if (!mapArray[currR][currC].equals("|")) {
	            mapArray[currR][currC] = "+";
	        }

	        // Move to the previous tile
	        String nextStep = cameFrom[currR][currC];
	        parts = nextStep.split(",");
	        currR = Integer.parseInt(parts[0]);
	        currC = Integer.parseInt(parts[1]);
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
	public static void printCoordinates() {
	    for (int r = 0; r < rows * nums; r++) {
	        for (int c = 0; c < cols; c++) {
	            if (!mapArray[r][c].equals(".")) {
	                int level = r / rows;
	                int floorRow = r % rows;
	                System.out.println(level + " " + floorRow + " " + c + " " + mapArray[r][c]);
	            }
	        }
	    }
	}
	/**
	 * Reads the maze from a coordinate-style file.
	 * Format: 
	 * rows cols levels
	 * char row col level
	 */
	public static void readCoordinateFile(String fileName) {
	    try {
	        File file = new File(fileName);
	        Scanner scanner = new Scanner(file);
	        
	        if (!scanner.hasNextInt()) {
	        	return;
	        }
	        rows = scanner.nextInt();
	        cols = scanner.nextInt();
	        nums = scanner.nextInt();
	        
	        mapArray = new String[rows * nums][cols];
	        for (int r = 0; r < rows * nums; r++) {
	            Arrays.fill(mapArray[r], ".");
	        }

	        while (scanner.hasNext()) {
	            String value = scanner.next();
	            if (scanner.hasNextInt()) {
	                int r = scanner.nextInt();
	                int c = scanner.nextInt();
	                int n = scanner.nextInt();
	                int actualRow = (n * rows) + r;
	                if (actualRow < rows * nums && c < cols) {
	                    mapArray[actualRow][c] = value;
	                }
	            }
	        }
	        scanner.close();
	        

	    } catch (FileNotFoundException e) {
	        System.out.println("File not found!");
	    } catch (Exception e) {
	        System.out.println("Error in readCoordinateFile: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

}





