import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/*
 * Worked with Aarna P, Anya Y, Emily S, Purvi J
 */

public class Runner {
	
	public static String[][] mapArray; //creates the 2D array to store the map
	public static int rows; 
	public static int cols;
	public static int nums; //number of sections
	
	public static void main(String[] args) {
		//change this value to change the text file and run the code
		//readFile("hardMap1");
		readQueueFile("hardMap3c");
	}
	
	
	public static void readFile(String fileName) {
		try {
			File file = new File(fileName);
			Scanner scanner = new Scanner(file);
			
			//getting the amount of rows, cols, and nums and saving them to variables
			rows = Integer.parseInt(scanner.next());
			cols = Integer.parseInt(scanner.next());
			nums = Integer.parseInt(scanner.next());
			mapArray = new String[rows*nums][cols];
			
				for(int r = 0; r < mapArray.length; r++) {
					String newRow = scanner.next();
					for(int c = 0; c < cols; c++) {
						mapArray[r][c] = newRow.substring(c, c+1); //getting the specific value through a substring
					}
				}
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

}
