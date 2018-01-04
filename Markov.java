package sync;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Markov {
    public static void main(String[] args) {
    //just the tester to see how the transition matrix would look like given some initial events 
     /*
      * 1.initialize the arrays
      * 2. loop though the file and increamant the arrays counters 
      * 3. compute the probabiilties  
      */
    
    //read the number of different events in the file 
    System.out.println("in mAIn");
    int[][] counts = new int[4][4];    
    int[] outDegree = new int[4];     
    
    //build the event , index map as we want them 
    HashMap<String, Integer> events = new HashMap<String, Integer>() {{
	    put("CreateEvent",0);
	    put("PushEvent", 1);
	    put("PullRequestEvent", 2);
	    put("DeleteEvent", 4);
	    put("PullRequestReviewCommentEvent",3);
	}};

	//File file = new File("/Synchronicity/SyncIO/results.txt");
	File file = new File(Markov.class.getResource("../SyncIO/results.txt").getFile());
	 
	  BufferedReader br = null;
	try {
	    br = new BufferedReader(new FileReader(file));
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	 
	  String st;
	  try {
	    while ((st = br.readLine()) != null)
	      {
	          String[] sequence =  st.split(",");
	          for(int i =0; i< sequence.length-1 ; i++)
	          {
	       String first = sequence[i].trim().replaceAll("\'","");
	       //System.out.println(first);
	       String second = sequence[i+1].trim().replaceAll("\'","");
	       //System.out.println(second);
	    	 int p =  events.get(first);
	    	 int q = events.get(second);
	    	 counts[p][q]++;
	    	 outDegree[p]++;
	          }
	      }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
        for (int i = 0; i < 5; i++)  {
    
            // Print probability for column j. 
            for (int j = 0; j < 5; j++) {
                double p = 0.90*counts[i][j]/outDegree[i] + 0.10/3; 
                System.out.printf("%7.5f ", p); 
            } 
            System.out.println(); 
        } 
  }

}
