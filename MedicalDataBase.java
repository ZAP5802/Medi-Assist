// importing the important packages 
import java.util.*;
import java.io.*;
import java.lang.*;

class UnrecognizedSymptomException extends Exception // creating the class of the userdefined exception
{
   public UnrecognizedSymptomException(String s)
    {
        // Call constructor of parent Exception
        super(s);
    }
}

class UnrecognizedDiseaseException extends Exception // creating the class of the userdefined exception
{
   public UnrecognizedDiseaseException(String s)
    {
        // Call constructor of parent Exception
        super(s);
    }
}

class MedicalDataBase
{
  // because we want to access these variables in all the methods
	static int i = 0 , k =0;
	static String p_symptoms[] = new String[3] ;

   // main body of the code 
	public static void main(String[] args) 
	{
		// variable declaration
		Scanner in = new Scanner(System.in);
		String p_name ;
		int p_id ;
	
   	// main body and getting inputs
		System.out.println("-----------------------------\nWelcome to Medi-Assist :-)\n-----------------------------\nPlease Enter your Name below :");
		p_name = in.nextLine(); // because the input of the name can have spaces in it for last name or middle name
		System.out.println("Please Enter your ID :");
		p_id = in.nextInt();
	   System.out.println("Please Enter any 3 Symptoms that might help us diagnose (no spaces required) :"); 
	 
     // calling the addSymptom method to store the symptom of the patient in the array
	  for(k =0; k<3 ; k++) 
	  {
	  	String userinput = in.next();  
	  	addSymptom(userinput.toLowerCase()); // we want to keep the words to a definite case because (capital is not equal to non capital) 
	  }
	 
  	// to display HastSet of disease, we are also calling the method diagnosis 
   System.out.println("-------------------------------------------\nFrom the symptoms entered,Possible diseases :\n-------------------------------------------\n" +diagnosis() );
   // to write the record in the csv file 
   databaseupdater(p_id, p_name, diagnosis()); 

   	}	
      //to add the symptoms in the array and validate the entered symptom
   	public static void addSymptom(String symptoms) 
   	{
      try{
        
      String word = symptoms;
      boolean flag = false;
      //Reading the contents of the symptom file
      Scanner sc2 = new Scanner(new FileInputStream("Symptom.txt")); // contains more than 220 different symptoms 
      while(sc2.hasNextLine()) {
         String line = sc2.nextLine();
         // The indexOf() method returns the position of the first occurrence of specified character(s) in a string. It returns -1 if the word is not found
         if(line.indexOf(word)!=-1) { 
            flag = true;
         }
      }
      
      if(flag) 
      {  
         p_symptoms[i] = word;
         i++; // TO PREVENT OVERWRITING 
      } 
      else 
      {
         // if the symptom is not found then the user have to enter other symptom, since if the value of the elements would be null, we don't get what we want
        k--;
        throw new UnrecognizedSymptomException("The symptom is not found"); // ATQ , the exception is thrown if symptom is not identified 
      }  
   
   }catch(IOException ie)
   {
      System.out.println("The file [symptoms.txt] was not found");
   }
   catch(UnrecognizedSymptomException ue)
   {
   	System.out.println("The symptom was not found in the symptom list , Please enter once again");
   }

   	}

      // method to get the diagnosis 
   	public static HashSet<String> diagnosis()
   	{
   	    ArrayList<String> Disease = new ArrayList<String>(); // an arraylist is used because we don't know how many disease would come as an output 
   		 for(int v = 0 ; v<3 ; v++) 
        {
        	try{
        	 String word = p_symptoms[v];
      	  boolean flag = false;
      //Reading the contents of the file Disease.txt
       Scanner sc2 = new Scanner(new FileInputStream("Disease.txt"));  // contains data for more than 50 disease
       while(sc2.hasNextLine()) {
         String line = sc2.nextLine();
         if(line.indexOf(word)!=-1) 
         {
          flag = true;
          Disease.add(sc2.nextLine());  // to add the disease to the arraylist
         }
      }

      if(!flag)
      {
        throw new UnrecognizedDiseaseException("The disease was not found"); // ATQ , the exception is thrown if disease is not identified 
      }  
   
   } catch(IOException ie)
   {
      System.out.println("The file [Disease.txt] was not found");
   }
   catch(UnrecognizedDiseaseException ue)
   {
   	System.out.println("The disease could not be diagnosed");
   }

     } 

  HashSet<String> hset = new HashSet<String>(Disease); // because the disease might repeat if common symptoms, we are making a hashset (set of unique values of the arraylist)
  
  return(hset); 		 
   }
      // to write the csv file 
   	public static void databaseupdater( int id ,String name , HashSet<String> hset) 
   	{
   	
      try (PrintWriter writer = new PrintWriter(new FileOutputStream(new File("MedicalDataBase.csv"), true))) {

      StringBuilder sb = new StringBuilder(); // to use the append method we use the object of StringBuilder(used to create modifiable strings)

      sb.append(" "+id +","); // append = add = write 
      sb.append(name +",");

      for(int l = 0 ; l<3 ; l++)
      {
      	sb.append(p_symptoms[l] +"  ");
      }
      
      sb.append(" , ");
      List<String> list = new ArrayList<String>(hset); // converting the hashset to arraylist to get it appended in the csv file
      for(int p =0 ; p<hset.size() ; p++)
  		{
  			sb.append(list.get(p)+"  ");
  		} 
      sb.append("\n");
      //The toString() method of StringBuilder class is inbuilt method used to returns a string representing data contained by StringBuilder Object.(To kind  of clean the plate )
      writer.write(sb.toString()); 

    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
    }

   	}
}
