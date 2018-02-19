package markov;
/*
 * This file contains the main event sequence simulation of events 
 * There are two extra java libraries that needs to be added for the simulation to run 
 * First is Apache commons math for statistic and sampling http://commons.apache.org/proper/commons-math/
 * Second is jgrapht which is an optimal graph builder library  http://jgrapht.org/
 * for the graph builder we are still also considering our own lab's graph class to see if it gives 
 * more optimal results compared to jgrapht  
 */
 
import java.util.*;
import java.io.*;
import java.nio.file.*;
 
import org.jgrapht.graph.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.csv.*;
 
 
public class SimulationRunner {
     
    public static final int TOTAL_NUM_EVENTS = 12611; 
    private static final String Output_CSV_FILE = "markov_tuple_generation_output.csv";
    /*
     * the pseudocode as described in the slides 
     * 
     * UNTIL (K events generated)
     Repeat
         R <-- Sample PR  , E <-- Sample PE , , U <-- Sample Pu|R 
         S <-- E , Su <-- U
         WHILE (the crawler not halted in a saturated state):
    En <-- Sample GE, E 
    E <-- En 
    S <-- S + En
    WHILE (|Su| < |S|)
        Un <-- Sample Gu , U        
         Add ( S, Su, R, Et ) 
 
     */
     
     
    /**
     * this method is to simulate the sequence of events, their related users and repository 
     * 
     * @param graphE related to the transition matrix computed based on Markov Process
     * @param graphU the user-user collaboration for the training data time-frame 
     * @param prioriE the event priori distribution (computed from transition matrix) 
     * @param prioriR the repository priori distribution 
     * @param prioriU the user distribution (given a specific repository)  
     * 
     * after creating each sequence it would write the resulted line to a csv file (using the helper method) 
     * 
     * the distributions types are subject to change given the actual distributions we get from the data
     * @throws IOException 
     */
    public static void Simulate(BufferedWriter writer, DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> graphE, DefaultDirectedWeightedGraph<Integer, DefaultWeightedEdge> graphU, EnumeratedDistribution<String> prioriE , EnumeratedDistribution<Integer> prioriR, EnumeratedDistribution<Integer> prioriU) throws IOException
    {
    int eventCounter = 0;
    int sequenceID = 0;
     
     
    while (eventCounter < TOTAL_NUM_EVENTS)
    {
                //create the sample from the priori distributions 
                int sampleRepository = prioriR.sample();
                String sampleEvent = prioriE.sample();
                 
                while (sampleEvent.equals("DeleteEvent") || sampleEvent.equals("ReleaseEvent") || sampleEvent.equals("RestEvent"))
                        sampleEvent = prioriE.sample();
                 
                //sample the user graph based on the given sampleRepository
                int sampleUser = prioriU.sample();
                 
                //add the samples values (user and the related event) to a treeMap  
                TreeMap<Integer, ArrayList<String>> sequence = new TreeMap<>();
                 
                //using a treemap helps us to keep track of number of events and user at the same time 
                //System.out.println("Sample user is "+ sampleUser+ "Sample Event is "+ sampleEvent);
            ArrayList<String> events = new ArrayList<>();
                events.add(sampleEvent);
                sequence.put(sampleUser, events);
                eventCounter++;
                 
                //get the halting states from the transition matrix using a threshold 
                LinkedList<String> haltingStates = new LinkedList<>();
                haltingStates.add("DeleteEvent");
                haltingStates.add("ReleaseEvent");
                haltingStates.add("RestEvent");
                //start the crawler and loop until it halts in a saturated state 
                while(!haltingStates.contains(sampleEvent))
                {
                     
                    //sample graphE based on sampeEvent to get the next step 
                    Set<DefaultWeightedEdge> outEdgesEvent = graphE.outgoingEdgesOf(sampleEvent);
                    List<Pair<String, Double>> probabilities =  new ArrayList<>(); 
                    //System.out.println("Sample Event is "+ sampleEvent);
                    //loop through the set of outedges and add them all to the list of probabilities, then sample from them "
                    for (DefaultWeightedEdge edge : outEdgesEvent)
                    {
                    Double weight = graphE.getEdgeWeight(edge);
                    String target = graphE.getEdgeTarget(edge);
                    //System.out.println(target +" "+ weight);
                    probabilities.add(new Pair<String,Double>(target, weight));
                    }
                    EnumeratedDistribution<String> distribution = new EnumeratedDistribution<>(probabilities);
                    String nextEvent = distribution.sample();
                    //System.out.println(nextEvent);
                    //assign the result of next step to the sampleEvent
                    if (sampleEvent == "WatchEvent" || sampleEvent == "ForkEvent" || sampleEvent == "MemberEvent")
                            nextEvent = "RestEvent";
                     
                    sampleEvent = nextEvent;
                     
                    //sample graphU based on sampleUser to get the next user 
                    Set<DefaultWeightedEdge> outEdgesUsers = graphU.outgoingEdgesOf(sampleUser);
                    //System.out.print(sampleUser);
                    List<Pair<Integer, Double>> UserProbabilities =  new ArrayList<>();
                    //loop through the set of out-edges and add them all to the list of probabilities, then sample from them 
                    for (DefaultWeightedEdge edge : outEdgesUsers)
                    {
                     
                    Double weight = graphU.getEdgeWeight(edge);
                    Integer target = graphU.getEdgeTarget(edge);
                    //System.out.println(target +" "+ weight);
                    UserProbabilities.add(new Pair<Integer,Double>(target, weight));
                    }
                    EnumeratedDistribution<Integer> dist = new EnumeratedDistribution<>(UserProbabilities);
                    Integer nextUser = dist.sample();
                     
                    //assign the result of next user to the sampleUser 
                    if(sampleUser == nextUser)
                    {
                            events.add(nextEvent); 
                            sequence.put(sampleUser, events);
                            eventCounter++;
                    }
                    else {
                            ArrayList<String> newEvents = new ArrayList<>();
                            newEvents.add(nextEvent);
                            sequence.put(sampleUser, newEvents);
                            eventCounter++;
                    }
                    sampleUser = nextUser;
                     
                    //add the next user mapped to next step to the sequence
                    //System.out.println("Sample user is "+ sampleUser+ "Sample Event is "+ sampleEvent);
                     
                }
                 
                //System.out.println("we have "+sequence.size()+" events in our sequence");
                CSVPrinter csvWrite = new CSVPrinter(writer, CSVFormat.DEFAULT);
                //loop through the sequence treeMap and write them to the output file 
                  
                for (Integer user: sequence.keySet())
                {
                   ArrayList<String> linkedEvents = sequence.get(user);
                   for(String ev : linkedEvents)
                   {
                   csvWrite.print("(");
                   csvWrite.printRecord(user,sampleRepository,ev,sequenceID+")");
                   sequenceID++;
                   }
                }
                csvWrite.flush();
    }
     
     
    }
 
 
    
    public static void main (String[] args) throws IOException 
    {
    //setting up the trainer
        train train = new train();
         
    // Retrieving parameters based on what we have from in Training data 
         
    DefaultDirectedWeightedGraph<String, DefaultWeightedEdge>  Eventgraph = 
                train.getEventGraph();
     
     
    //the following can give us the transition matrix 
     
//  Set <DefaultWeightedEdge> set = Eventgraph.edgeSet();
//  for (DefaultWeightedEdge edge : set) {
//  System.out.println("edge is"+ edge + " probability is " + Eventgraph.getEdgeWeight(edge)*100);  
//  }
                 
    DefaultDirectedWeightedGraph<Integer, DefaultWeightedEdge>  Usergraph = 
            train.getUserGraph();
                    
    EnumeratedDistribution<Integer> repoDistribution = train.getRepoPriori();
       
     
    EnumeratedDistribution<Integer> userDistribution = train.getUserPriori();
     
     
    EnumeratedDistribution<String> EventDistribution = train.getEventPriori();
     
     
    //initialize the buffered writer 
     
    BufferedWriter writer = Files.newBufferedWriter(Paths.get(Output_CSV_FILE));
 
    //call the simulate on the estimated parameters
    Simulate(writer, Eventgraph, Usergraph, EventDistribution, repoDistribution, userDistribution);
     
    }
}