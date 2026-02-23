import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Runner {
	
	public static String[][] mapArray;
	public static int rows;
	public static int cols;
	public static int nums;
	
	public static void main(String[] args) {
		readFile("hardMap1"); //change this value to change the text file
	}
	
	
	public static void readFile(String fileName) {
		try {
			File file = new File(fileName);
			Scanner scanner = new Scanner(file);
			
			
			rows = Integer.parseInt(scanner.next());
			cols = Integer.parseInt(scanner.next());
			nums = Integer.parseInt(scanner.next());
			mapArray = new String[rows*nums][cols];
			
				for(int r = 0; r < mapArray.length; r++) {
					String newRow = scanner.next();
					for(int c = 0; c < cols; c++) {
						mapArray[r][c] = newRow.substring(c, c+1);
					}
				}
			System.out.println(Arrays.deepToString(mapArray));
			scanner.close();

			
		
		}catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File Not Found!");
		}
	}
	
	public static void readQueueFile(String fileName) {
		try {
			File file = new File(fileName);
			Scanner scanner = new Scanner(file);
			
			
			rows = Integer.parseInt(scanner.next());
			cols = Integer.parseInt(scanner.next());
			nums = Integer.parseInt(scanner.next());
			mapArray = new String[rows*nums][cols];
			
			
			
			
			
			System.out.println(Arrays.deepToString(mapArray));
			scanner.close();

			
		
		}catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File Not Found!");
		}
	}

}
