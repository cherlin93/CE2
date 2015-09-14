/* The program TextBuddy takes in the name of a text file as a command line argument.
 * A user can execute various commands to manipulate the file; for instance, by typing add tasks to be done,
 * the text "tasks to be done" will be written into the text file given by the user.
 *
 * In this program, we assume that a user will only delete content in the file by giving the line number and not
 * by any other ways;
 * e.g. will only execute command delete 1(any number) but not delete two or delete jumped over the moon.
 * 
 * At the same time, it is also assumed that commands e.g. add, display, delete, clear, exit are only given in lower case characters.
 *
 * @ Cher Lin
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;


public class TextBuddy {
    
    private static final String WELCOME_TEXT = "Welcome to TextBuddy, %s is ready for use (:";
    private static final String ADD_MESSAGE = "added to %s:%s";
    private static final String DISPLAY_MESSAGE = "%d. %s";
    private static final String DELETE_MESSAGE = "deleted from %s,%s.";
    private static final String CLEAR_ALL_MESSAGE = "All content has been deleted from %s.";
    private static final String INVALID_ACCESS = "Line number/content does not exist!";
    private static final String EMPTY_FILE = "%s is empty.";
    
  //private static final String FILE_DOES_NOT_EXIST = "Text file is not found.";
    
    private static ArrayList<String> fileContent;
    
    public static void main(String[] args) throws IOException {
        
    	String fileName = args[0];
        
        fileContent = new ArrayList<String>();
        
        printWecomeMessage(fileName);
        
        loadContentFromFile(fileName);
        
        readUserInput(fileName);
    }
    
    private static void printWecomeMessage(String textFile) {
    	
        showToUser(WELCOME_TEXT, textFile);
    }
    
    private static void loadContentFromFile(String textFile) {
    	
    	String line = null;
    	BufferedReader bw = null;
    	
    	try { 
    		bw = new BufferedReader(new FileReader(textFile));
    		
    		while ((line = bw.readLine()) != null) {
    			fileContent.add(line);
    		}
    	} catch (IOException e) {
             e.printStackTrace();
    	}
    }
    

    private static void readUserInput(String textFile) throws IOException {
        
        boolean result = true;
        
        String userInput;
        
        while (result) {
            System.out.print("command: ");
            userInput = getUserInput();
            result = parseInputAndExecute(textFile, userInput);
        }
        
        System.exit(0);
    }
    
    public static String getUserInput() {
        
    	Scanner sc = new Scanner(System.in);
   		return sc.nextLine();
    }
    
    public static boolean parseInputAndExecute(String textFile, String input) throws IOException{
        
        String content = null;
        String command = null;
        
        if (input.contains(" ")) {
            command = input.substring(0, input.indexOf(" "));
            content = input.substring(input.indexOf(" "), input.length());
        } else {
            command = input; //for cases where the user inputs only the command e.g. display
        }
        
        return executeCommand(textFile, content, command);
    }
    
    private static boolean executeCommand(String textFile, String content,String command) throws IOException,
    FileNotFoundException {
        
        boolean result = true;
        
        switch (command) {
                
            case "add":
                addContent(textFile, content);
                saveToFile(textFile);
                break;
                
            case "display":
                displayContent(textFile);
                break;
                
            case "delete":
                deleteContent(textFile, content);
                saveToFile(textFile);
                break;
                
            case "clear":
                clearAllContent(textFile);
                saveToFile(textFile);
                break;
                
            case "exit":
                result = exitProgram(result);
                break;
                
            default:
                System.out.println("Invalid Command");
        }
        
        return result;
    }
   
    
    public static void addContent(String textFile, String content) throws IOException {
        
        fileContent.add(content);
        
        showToUser(ADD_MESSAGE, textFile, content);
    }
    
    public static void displayContent(String textFile) { // (String textFile, String command) {
        
        checkIfFileIsEmpty(textFile);
    }
    
    private static void checkIfFileIsEmpty(String textFile) {
    	
    	if (fileContent.size() == 0) {
            showToUser(EMPTY_FILE, textFile);
        }
        
        for (int i = 0; i < fileContent.size(); i++) {
            showToUser(DISPLAY_MESSAGE, i + 1, fileContent.get(i));
        }
    }
    
    public static void deleteContent(String textFile, String content) {
        
        String trimContent;
        int lineNum;
        
        trimContent = content.trim(); //omit trailing white spaces
        lineNum = Integer.parseInt(trimContent) - 1; //Array list index starts from 0
        
        if ((lineNum + 1 > fileContent.size()) || lineNum < 0) {
        	
            showToUser(INVALID_ACCESS);
            return;
        }
        
        String message = fileContent.get(lineNum);
        fileContent.remove(lineNum);
        
        showToUser(DELETE_MESSAGE, textFile, message);
    }
    
    public static void clearAllContent(String textFile) throws FileNotFoundException{
    	
        fileContent.clear();
        showToUser(CLEAR_ALL_MESSAGE, textFile);
    }
    
    public static boolean exitProgram(boolean result){
        
        return false;
    }
    
    private static void saveToFile(String textFile) {
        
        PrintWriter pw = null;
        
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(textFile, false)));
            
            for (String message : fileContent) {
                pw.println(message);  
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        	pw.close();
        }
    }
    
    private static void showToUser(String message, Object... args) {
        System.out.println(String.format(message, args));
    }
}
    