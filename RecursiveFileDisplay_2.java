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

class Global {
	public final static File currentDir = new File(""); // test directory
}

/* to run threads first and populate all the dynamic arrays once. Only then we can search key for it. */
// A Thread Class
class MyRunnableThread implements Runnable {
	public MyRunnableThread(){}
	
 public void run() 		
 {

	try {
		RecursiveFileDisplay.fileAttributes(Global.currentDir);
	} catch (IOException e) {
		e.printStackTrace();
	}
  }
}

// Second Thread Class
class MyRunnableThread_2 implements Runnable {
	public MyRunnableThread_2(){}
	
public void run() {
	RecursiveFileDisplay.displayDirectoryContents(Global.currentDir);
	}
}


public class RecursiveFileDisplay {
	
	 public static List<Pair<String, File>> List = new ArrayList<Pair<String, File>>();	// for containing directories and files
	 public static List<Pair<String, File>> Parsed = new ArrayList<Pair<String, File>>();	// for containing parsed directories and files
	 public static List<Pair<String, File>> Keywords = new ArrayList<Pair<String, File>>(); // for containing keywords and file paths
	 public static List<Pair<String, File>> Paths = new ArrayList<Pair<String, File>>();	// for storing path keywords against full path
	 public static List<Pair<String, File>> Attributes = new ArrayList<Pair<String, File>>();	// for storing attribute keys against path

	public static void main(String[] args) throws IOException {
		
		//Creating an object of the first thread
	     MyRunnableThread   firstThread = new MyRunnableThread();
	     MyRunnableThread_2   firstThread_2 = new MyRunnableThread_2();


		//Starting the first thread
	     Thread thread1 = new Thread(firstThread);
	     thread1.start();		// file attributes
	               
        try {
       	 thread1.join();
        } catch (InterruptedException e) {
       	 e.printStackTrace();
        }
	        
	    Thread thread2 = new Thread(firstThread_2);
	    thread2.start();	// file display
        try {
          	 thread2.join();
        } catch (InterruptedException e) {
          	 e.printStackTrace();
        }	
		
 		/*for (Pair<String, File> pair : Attributes)  		
 			System.out.println (pair.getKey()+" -> "+pair.getValue()+"\n");*/

		
	    String sub, options;
		Scanner in = new Scanner(System.in);
		Scanner file_name;
		String input = "";
		int count = 0;
		//File file_input = null;
	      while (true) {
	      
		      System.out.println("Please select an option. Press 'q' to exit.");
		      System.out.println("1. Display Subdirectories and files in the Parent Directory\n");
		      System.out.println("2. Search for a file by its name\n");	
		      System.out.println("3. Search for a file by a content keyword\n");
		      System.out.println("4. Search for a file by its attributes\n");		
		      System.out.println("5. Search for a keyword in path\n");				
	
		      options = in.nextLine();
		      
		      // Display Sub-directories and files in a Directory
		      if (options.equals("1")) {
			 		for (Pair<String, File> pair : List)  		
			 			System.out.println (pair.getKey()+" -> "+pair.getValue()+"\n");
	  		  }
		      
		     // Search for a file by its name
		      else if (options.equals("2")) {
			    System.out.println("Please enter the file name you want to look for: ");				
		  		file_name = new Scanner(System.in);
		  		input = file_name.nextLine();		
		    	  
		  		// A filename cannot contain any of the following characters: \ / : * ? " < > |  
		  	    Pattern p = Pattern.compile("[\\\\/:*?\"<>|]");
		  	    Matcher m = p.matcher(input);

		  	    // matches() checks all the string and find() finds it in any part of the string 
		  	    if (m.find())
		  	        throw new IllegalArgumentException("Sorry! You've entered an Invalid String");
		  		else {
		  			// Then proceed to matching input with key 
		  		    Pattern p2 = Pattern.compile(input);
		  		    Matcher m2;
		  		
		  	 		for (Pair<String, File> pair : List) 		
		  	 		{
		  	 			m2 = p2.matcher(pair.getKey());
		  				if (m2.find()) {		
		  					System.out.println("Found a '" + m2.group() + "'.");
		  					count ++;
		  					System.out.println (pair.getKey()+" -> "+pair.getValue()+"\n");
		  	 			}
		  	 
		  	 		}
		   			if (count == 0)
		  				System.out.println("Sorry! No search results found.");			 
		  		}
		      }
		      
		      // Search for a file by a content keyword
		      else if (options.equals("3")) {
				  System.out.println("Please enter a file's content keyword you want to look for: ");				
			  	  file_name = new Scanner(System.in);		    	  
		    	  input = file_name.nextLine();
		    	  for (Pair<String, File> pair : Keywords) {
		    		  if (pair.getKey().contains(""+input+""))
			 			System.out.println (pair.getKey()+" -> "+pair.getValue()+"\n");
		    	  }
		    	  
		      }
	      
		      // Search for a file by its attributes
		      else if (options.equals("4")) {
			      while (true) {
				      System.out.println("Please specify your option. Press q to quit from this option menu:\n");
				     // System.out.println("4.1 Display file's attributes\n");
				      System.out.println("4.1 Search for number of subdirectories in a directory\n");
				      System.out.println("4.2 Search a file size\n");	
				      System.out.println("4.3 Search a file's creation/modification time \n");
				      System.out.println("4.4 Search a file's creation/modification date\n");		
				      System.out.println("4.5 Search a file's ownership\n");
				      System.out.println("4.6 Search a file's permissions\n");
			 		      
				      Scanner in_2 = new Scanner(System.in);
				      sub = in_2.nextLine();
				      
				      // Display file's attributes
				     /* if (sub.equals("4.1")) {
				        System.out.println ("Please enter the file's path whose attributes you want to view:");  
				    	file_name = new Scanner(System.in);
				    	input = file_name.nextLine();
				    	file_input = new File(input);
				    	
				    	for (Pair<String, File> pair : Attributes) {
				    	  if ((pair.getValue()).getName().equals(file_input)) 
				    		 System.out.println (pair.getKey()+" -> "+pair.getValue()+"\n");				    	  
				    	}
				      } */
				      
				      // Search for number of subdirectories in a directory
				      if (sub.equals("4.1")){ 
				    	  System.out.println ("Please enter (a single digit) no. of subdirectories you want to look for:");  
					      file_name = new Scanner(System.in);
					      input = file_name.nextLine();
					      Pattern p = Pattern.compile("\\d");			// can accept 0-9			
						  Matcher m = p.matcher(input);
						  
						  if (!m.matches())
						        throw new IllegalArgumentException("Sorry! You've entered an Invalid String");

						  else {
						  	  for (Pair<String, File> pair : Attributes) 		
						 	  {
									if ((pair.getKey().startsWith("Count of subdirectory:", 0))) {
										if (pair.getKey().contains(input)) {
						  					System.out.println("Found a '" + input + "'.");
						  					System.out.println (pair.getKey()+" -> "+pair.getValue()+"\n");
						  					count++;
										}									
						 			}
						 	  }
						  	  
						  	  if (count == 0)
				  					System.out.println ("Sorry! No search results found."); 						  	  
							  }
  				      }
				      // Search a file size
					  else if (sub.equals("4.2")){

						    System.out.println ("Please enter a file size (in the format: 00.00) you want to look for: ");  
					        file_name = new Scanner(System.in);
					        input = file_name.nextLine(); 
						    Pattern p = Pattern.compile("\\d{2}.\\d{2}"); 		
						    Matcher m = p.matcher(input);
						    if (!m.matches())
						        throw new IllegalArgumentException("Sorry! You've entered an Invalid String");
						    else {
						  		for (Pair<String, File> pair : Attributes) 		
						 		{
									if ((pair.getKey().startsWith("File size (kB):", 0))) {
										if (pair.getKey().contains(input)) {
						  					System.out.println("Found a '" + input + "'.");
						  					System.out.println (pair.getKey()+" -> "+pair.getValue()+"\n");
						  					count ++;
										}
										
						 			}
						 
						 		}
						  		if (count == 0)
				  					System.out.println ("Sorry! No search results found."); 			
						    }
				  	 		
					  }
				      // Search a file's creation/modification time 
					  else if (sub.equals("4.3")){
						    System.out.println ("Please enter time (in the 24-hr format: hh:mm:ss) you want to look for: ");  
					        file_name = new Scanner(System.in);
					        input = file_name.nextLine();
						    //String input = "14:41:40";							
							Pattern p = Pattern.compile("(\\d{2}:\\d{2}:\\d{2})");		//	\d matches a one digit numeric string, \d\d matches upto 99 (max 2 digits)
							Matcher m = p.matcher(input);
							
							if (!m.matches()) 					
						        throw new IllegalArgumentException("Sorry! You've entered an Invalid String");
							else {
							    Pattern p2 = Pattern.compile(input, Pattern.CASE_INSENSITIVE+Pattern.LITERAL);
							    Matcher m2;
						 		for (Pair<String, File> pair : Attributes) 		
						 		{
						 			if ((pair.getKey().startsWith("Creation Date/Time is: ", 0)) || (pair.getKey().startsWith("Modification Date/Time is: ", 0)))
						 			{
						 	 			m2 = p2.matcher(pair.getKey());
						 				if (m2.find())
										{		
											System.out.println("Found a '" + m2.group() + "'.");
								        	System.out.println (pair.getKey()+" -> "+pair.getValue()+"\n");	
											count ++;
							 			}
						 			}

								}
					 			if (count == 0)
									System.out.println("Sorry! No search results found.");
							}
						  
					  }
				      // Search a file's creation/modification date 
					  else if (sub.equals("4.4")){
						    System.out.println ("Please enter date (in the format: dd:mm:yyyy) you want to look for: ");  
					        file_name = new Scanner(System.in);
					        input = file_name.nextLine();
						    //String input = "04-04-2016";	
							Pattern p = Pattern.compile("(\\d{2}-\\d{2}-\\d{4})");	
							Matcher m = p.matcher(input);	
							
							if (!m.matches()) 					
						        throw new IllegalArgumentException("Sorry! You've entered an Invalid String");
							else {
								
							    Pattern p2 = Pattern.compile(input);
							    Matcher m2;
								
						 		for (Pair<String, File> pair : Attributes) 		
						 		{
						 			if ((pair.getKey().startsWith("Creation Date/Time is: ", 0)) || (pair.getKey().startsWith("Modification Date/Time is: ", 0)))
						 			{
						 	 			m2 = p2.matcher(pair.getKey());
						 				if (m2.find())
										{		
											System.out.println("Found a '" + m2.group() + "'.");
								        	System.out.println (pair.getKey()+" -> "+pair.getValue()+"\n");	
											count ++;
							 			}
						 			}

								}
					 			if (count == 0)
									System.out.println("Sorry! No search results found.");
							}
						  
					  }
				      // Search a file's ownership
					  else if (sub.equals("4.5")){
						    System.out.println ("Please enter the owner whose files to look for: ");  
					        file_name = new Scanner(System.in);
					        input = file_name.nextLine();
							Pattern p = Pattern.compile(input, Pattern.CASE_INSENSITIVE+Pattern.LITERAL);
							Matcher m;
							
					 		for (Pair<String, File> pair : Attributes) 		
					 		{
					 			if (pair.getKey().startsWith("Owner: ", 0))
					 			{
					 	 			m = p.matcher(pair.getKey());
					 				if (m.find()) {						
										count ++;
										System.out.println("Found a '" + input + "'.");
							        	System.out.println (pair.getKey()+" -> "+pair.getValue()+"\n");	
						 			}
					 			}

							}
							if (count == 0)
								System.out.println("Sorry! No search results found.");
						  
					  }
				      // Search a file's permissions
					  else if (sub.equals("4.6")){
						    System.out.println ("Please enter the file permissions to look for: ");  
					        file_name = new Scanner(System.in);
					        input = file_name.nextLine();
							Pattern p = Pattern.compile(input, Pattern.CASE_INSENSITIVE);
							Matcher m;
							
					 		for (Pair<String, File> pair : Attributes) 		
					 		{
					 			if (pair.getKey().startsWith("Permissions for ", 0))
					 			{
					 	 			m = p.matcher(pair.getKey());
					 				if (m.find()) {						
										count ++;
										System.out.println("Found a '" + input + "'.");
							        	System.out.println (pair.getKey()+" -> "+pair.getValue()+"\n");	
						 			}
					 			}

							}
							if (count == 0)
								System.out.println("Sorry! No search results found.");						  
					  }
				      
					  else if (sub.equals("q"))
						  break;
		
					  else
					      System.out.println("Please select a valid option");
	
			      }
		      }
		      
		     // Search for a keyword in path
		      else if (options.equals("5"))
		      {
				  System.out.println ("Please enter a keyword in file path to look for: ");  
			      file_name = new Scanner(System.in);
			      input = file_name.nextLine();
		    	  for (Pair<String, File> pair : Paths) {
		    		  if (pair.getKey().contains(""+input+""))
			 			System.out.println (pair.getKey()+" -> "+pair.getValue()+"\n");
		    	  }
		      }
		      
			  else if (options.equals("q"))
				  break;	
		      else 
			      System.out.println("Please select a valid option");	      
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
					//System.out.println("DIRECTORY PATH: " + dir_path);
					fileAttributes(file);									
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
					//System.out.println("\nFILE PATH: " + dir_path);				
					fileAttributes(file);					
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
 		String path = file.getCanonicalPath();
 				
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
		
		else if (file.isFile()) {		
		 		//File Size 
		 	    Double fileSize = (double) file.length();				// in Bytes		
		 	    Double fileSize_K = (double) (fileSize/1024);			// convert to kilobytes
		 	    NumberFormat formatter = new DecimalFormat("#00.00"); 	// round off to two decimal places    
		 	    String round_off = formatter.format(fileSize_K);
				Attributes.add(new Pair("File size (kB): "+round_off, path));	
		}
 		
 	 	// Creation Time
		BasicFileAttributes view = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class).readAttributes();
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
