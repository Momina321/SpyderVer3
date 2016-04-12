/* Momina */

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclEntryPermission;
import java.nio.file.attribute.AclFileAttributeView;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/* Logic is to run threads first and populate all the dynamic arrays once. Only then we can search key for it. */

public class RecursiveFileDisplay {
	
	 public static List<Pair<String, File>> List = new ArrayList<Pair<String, File>>();	// for containing directories and files
	 public static List<Pair<String, File>> Parsed = new ArrayList<Pair<String, File>>();	// for containing parsed directories and files
	 public static List<Pair<String, File>> Keywords = new ArrayList<Pair<String, File>>(); // for containing keywords and file paths
	 public static List<Pair<String, File>> Paths = new ArrayList<Pair<String, File>>();	// for storing path keywords against full path
	 public static List<Pair<String, File>> Attributes = new ArrayList<Pair<String, File>>();	// for storing attribute keys against path

	public static void main(String[] args) throws IOException {
		

		File currentDir = new File("E:\\Crawler_TEST\\Crawler_TEST_inner"); // test directory
		//displayDirectoryContents(currentDir);
		fileAttributes(currentDir);
		
		
		
		// given a directory, call fileattributes method on all that;'s inside. then do matching
		// for example input:  count of sub directories = 2		// exact ho gaya pattern, no need to do 21 or 22, bc exact number
		
		// place a check that it should be a +ve integer.
		
		// do something like if input matches \\d{}, then proced
				
		//Lab7_AP_Backup.txt
		
		//Pattern p = Pattern.compile("rawl", Pattern.CASE_INSENSITIVE);			// correct this should consist of only words or hyphens
		//Matcher m;
		//System.out.println("Found a '" + m.group() + "'.");
	    String input = "14:41:40";							// enter in format hh:mm:ss
	    String valid;
		Pattern p = Pattern.compile("(\\d{2}:\\d{2}:\\d{2})");		//	\d matches a one digit numeric string, \d\d matches upto 99 (max 2 digits)
		Matcher m = p.matcher(input);				
		if (!m.matches()) 						
	        throw new IllegalArgumentException("Sorry! You've entered an Invalid String");
		else {
			valid = "Creation Date/Time is: " +input;				// bc pehlay dd:mm:yyyy phir hh:mm:ss
			int count = 0;
			
	 		for (Pair<String, File> pair : Attributes) 		
	 		{
	 				if (pair.getKey().contains(valid)) {		// not working properly.
	 				count++;
	 				System.out.println("Found at:");
		        	System.out.println (pair.getKey()+" -> "+pair.getValue()+"\n");
	 			}
			}
	 		if (count == 0)
				System.out.println("Sorry! No search results found.");
 		
		}
 
 		

	
	
	
	for (Pair<String, File> pair : Attributes) 		
		{
        	System.out.println (pair.getKey()+" -> "+pair.getValue()+"\n");
			}

	}
	/////////////////////////////////////////////////////////////////////////
	
	/* Displays all what is stored in a test directory */	
	public static void displayDirectoryContents(File dir) {
		// further divide it into search directory search file
		String str, dir_path; 

		if(!dir.isDirectory())
			System.out.println("Please specify a correct directory path.");
		
		try {
			File[] files = dir.listFiles(); 
			for (File file : files) {
				str = file.getName();
				dir_path = file.getCanonicalPath();

				if (file.isDirectory()) {
					List.add(new Pair(str, dir));			// adds directory's name to the List 
					//System.out.println("directory path: " + dir_path);
					String[] path_tokens = dir_path.trim().split("\\\\");		// split by backslash
					//System.out.println("\nDir Path Tokens are:");
					for (int i=0; i<path_tokens.length; i++) {
						//System.out.println(path_tokens[i]);
						Paths.add(new Pair(path_tokens[i], dir_path));					
					}
					
					//System.out.println("directory name: " + str);
					String[] splited = str.trim().split("\\s+");
					//System.out.println("\nSubstrings are:");
					for (int i=0; i<splited.length; i++) {
						//System.out.println(splited[i]);
						Parsed.add(new Pair(splited[i], dir_path));					
					}
					//System.out.println("\n");
					displayDirectoryContents(file);		// recursive call
				} 
			
				else {
					List.add(new Pair(str, dir));			// adds file's name to the List 
					//System.out.println("\nfile path: " + dir_path);
					String[] path_tokens = dir_path.trim().split("\\\\");
					//System.out.println("\nFile Path Tokens are:");
					for (int i=0; i<path_tokens.length; i++) {
						//System.out.println(path_tokens[i]);
						Paths.add(new Pair(path_tokens[i], dir_path));					
					}
										
					//System.out.println("file name: " + str);					
					String[] splited = str.trim().split("\\s+");	// trim and separate by space \s+: groups all unicode spaces ('',\t,\n etc)
					//System.out.println("\nSubstrings are:");
					for (int i=0; i<splited.length; i++) {
						//System.out.println(splited[i]);
						Parsed.add(new Pair(splited[i], dir_path));
					}
					
					IndexContent(dir_path);			// adds the keywords existing in files against the file's path
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	
	/* Reads a file content, tokenizes it and pushes it to the Keywords pair */
	public static void IndexContent(String filepath) {
        // This will reference one line at a time
        String line = null; String[] splited_2 = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(filepath);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);
 
            while((line = bufferedReader.readLine()) != null) {
               // System.out.println("Printing File's Content:");
                //System.out.println(line);
                splited_2 = line.trim().split("\\s+");		// tokenizing file's contents on space
            	line = bufferedReader.readLine();

                //System.out.println("\nTokenizing File's Content:");
    			for (int i=0; i<splited_2.length; i++) {
    				//System.out.println(splited_2[i]);
        			Keywords.add(new Pair(splited_2[i], filepath));	// pushing to the second ArrayList
        			//result = Keywords.contains("This");
            	 }
			} 

         // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println( "Unable to open file '" +  filepath + "'\n");                
        }
        catch(IOException ex) {
            System.out.println( "Error reading file '" + filepath+ "'\n");                  
             // ex.printStackTrace();
        }
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	
	/* given a file/directory keyword looks it up in the root directory */
	public static boolean checkExistFile (String s) {
 		System.out.println("Within the check Exist File method:");
 		for (Pair<String, File> pair : List) 		// Pair <Parsed dir/file name, File Path>
 		{
 			if (pair.getKey().equals(s)) {
	        	   System.out.println (pair.getKey()+" -> "+pair.getValue()+"\n");
	        }
		}
	
		return false;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	
	/* given a String keyword looks it up in the file's contents */
	public static boolean checkExistKeyword (String s) { 
 		System.out.println("Within check Exist Keyword method:");
 		for (Pair<String, File> pair : Keywords) 		// Pair <Parsed content, File Path>
 		{
	           if(pair.getKey().equals(s)) 
	        	   System.out.println (pair.getKey()+" -> "+pair.getValue()+"\n");

 		}
		return false;
	}
		
	/////////////////////////////////////////////////////////////////////////////////////////
		
	/* given a path chunk; looks it up in the root to see if it exists */
	public static boolean checkExistPath (String s) {						
 		System.out.println("Within the check Exist Path method:");
 		for (Pair<String, File> pair : Paths) 		// Pair <Parsed path, File Path>
 		{
	           if (pair.getKey().equals(s)) 
	        	   System.out.println (pair.getKey()+" -> "+pair.getValue()+"\n");

		}
		return false;
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////
	
	/* given a file find out its attributes */
	public static void fileAttributes (File file) throws IOException {	
		
 		//System.out.println("Within the File Attributes method:");
 		String path = "";
 		// get file path
		path = file.getCanonicalPath();
		
		if (file.isDirectory()) {					
	 		//number of sub directories in a directory 
			File[] files = file.listFiles(new FileFilter() {
	 	    @Override
	 	    public boolean accept(File f) {
	 	        return f.isDirectory();
	 	    }
	 		});
			String subdir = Integer.toString(files.length);
	 		//System.out.println("Sub Directories count: " + subdir);
			Attributes.add(new Pair("Count of subdirectory: "+subdir, path));			
		}
		
		else {			
	 		//File Size 
	 	    Double fileSize = (double) file.length();				// in Bytes		
	 	    Double fileSize_K = (double) (fileSize/1024);			// convert to kilobytes
	 	    NumberFormat formatter = new DecimalFormat("#0.00"); 	// round off to two decimal places    
	 	    String round_off = formatter.format(fileSize_K);
			Attributes.add(new Pair("File size (kB): "+round_off, path));			
		}
 		
 	 	// Creation Time
		BasicFileAttributes view;
		view = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class).readAttributes();
 	    FileTime fileTime = view.creationTime();				    
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateCreated = df.format(fileTime.toMillis());
        //System.out.println("Creation time: " + dateCreated);
		Attributes.add(new Pair("Creation Date/Time is: "+dateCreated, path));		

 
		// Last Modified
	 	String last_modified = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(
	 			    new Date(file.lastModified()) 
	 			); 
 		
 		//System.out.println ("Last Modified Time: " +last_modified+"\n");
		Attributes.add(new Pair("Modification Date/Time is: "+last_modified, path)); 


 		// Owner name	
	    UserPrincipal owner;
		owner = Files.getOwner(file.toPath());
		String Owner = owner.toString();
	    //System.out.println("Owner Name: " +  Owner); 
		Attributes.add(new Pair("Owner: "+Owner, path));

	 	
 
         // File Permissions using Access Control List (Acl)    
	     AclFileAttributeView aclView = Files.getFileAttributeView(file.toPath(), AclFileAttributeView.class);
	     if (aclView == null) {
	       System.out.format("ACL view  is not  supported.%n");
	       return;
	     }
	     List<AclEntry> aclEntries;
		aclEntries = aclView.getAcl();		
        for (AclEntry entry : aclEntries) {
		       Attributes.add(new Pair("Principal: "+entry.principal().toString(), path));
		       Attributes.add(new Pair("Type for "+entry.principal().toString()+": "+entry.type().toString(), path)); 
		       
		       //System.out.format("Permissions are:%n");
		       Set<AclEntryPermission> permissions = entry.permissions();
		       for (AclEntryPermission p : permissions) {
		        // System.out.format("%s %n", p);
		 		 Attributes.add(new Pair("Permissions for "+entry.principal().toString()+": "+p.toString(), path));
		       }
	     }        
	}		
}


///////////////
/*

String input = "23";
		Pattern p = Pattern.compile("(\\d)|(\\d{2})");		//	\d matches a one digit numeric string (0-9), \d{2} matches 2 digits (upto 99)
		Matcher m = p.matcher(input);				
		if (!m.matches()) 						
	        throw new IllegalArgumentException("Sorry! You've entered an Invalid String");
		else {
			input = "Count of subdirectory: " +input;
			int count = 0;
			
	 		for (Pair<String, File> pair : Attributes) 		
	 		{
	 				if (pair.getKey().equals(input)) {
	 				count++;
	 				System.out.println("Found at:");
		        	System.out.println (pair.getKey()+" -> "+pair.getValue()+"\n");
	 			}
			}
	 		if (count == 0)
				System.out.println("Sorry! No search results found.");
 		
		}
 
 		
	}
	*/