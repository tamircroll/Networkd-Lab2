import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Tal Kashi & Tamir Croll
 *
 */
public class Main {
	private final static String CONFIG_FILE = "config.ini";
	
	private static String defaultPage = null;
	private static int port = 0;
	private static int maxThreads = 0;
	private static File root = null;
	

	public static void main(String[] args) {
		try {
			BufferedReader input = new BufferedReader(new FileReader(CONFIG_FILE));
			
			String line = null;
			while((line = input.readLine()) != null) {
				String[] strArray = line.split("=");
				
				// If split does not return 2 values the pattern is not good
				if(strArray.length != 2)
					continue;
				try {
					// Assign only the first correct value
					if(strArray[0].equalsIgnoreCase("port")) {
						if(port <= 0 || port >= 65536)
							port = Integer.parseInt(strArray[1]);
					} else if(strArray[0].equalsIgnoreCase("root")) {
						if(root == null) {
							root = new File(strArray[1]);
							if (!root.isDirectory()) {
								root = null;
								System.out.println("ERROR: The given root folder '" + strArray[1] + "' does not exists or not a folder!");
							}								
						}
					} else if (strArray[0].equalsIgnoreCase("defaultPage")) {
						if(defaultPage == null)
							defaultPage = strArray[1];
					} else if (strArray[0].equalsIgnoreCase("maxThreads")) {
						if(maxThreads <= 0)
							maxThreads = Integer.parseInt(strArray[1]);
					}
				} catch(NumberFormatException e) {
					System.out.println("ERROR: Failed to parse the number given to port/maxThreads!");
					// Continue to next line, maybe a correct format
					continue;
				}				
			}
			input.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: File '" + CONFIG_FILE + "' was not found! Exiting program.");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("ERROR: Failed to read the config file! Exiting program.");
			System.exit(1);
		}
		
		if(root == null || defaultPage == null || port <= 0 || port >= 65536 || maxThreads <= 0) {
			System.out.println("ERROR: One of the given parameters in the config file is worng or missing! Exiting program.");
			System.exit(1);
		}
		
		WebServer server;
		try {
			server = new WebServer(root, defaultPage, port, maxThreads);
			server.run();
		} catch(IOException e) {
			System.out.println("ERROR: Failed to create ServerSocket! Exiting program.");
			System.exit(1);
		}
		
	}
}
