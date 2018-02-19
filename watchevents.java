// to observe watch events in a repo for a user

package markov;

import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class watchevents {

	public static void main(String[] args) throws FileNotFoundException {
		try {

			File file2 = new File(user_transition.class.getResource("userGraph.txt").getFile());
			File file = new File(user_transition.class.getResource("trainingData.txt").getFile());
			BufferedReader br = null;
			BufferedReader br2 = null;
			Multimap<String, String> map = ArrayListMultimap.create();

			String newLine = System.getProperty("line.separator");

			FileWriter fileWriter = new FileWriter("temp.txt");
			PrintWriter printWriter = new PrintWriter(fileWriter);

			String st, st2;
			br = new BufferedReader(new FileReader(file));
			while ((st = br.readLine()) != null) {
				// System.out.println(st);
				String[] sequence = st.split(",");
				map.put(sequence[2], sequence[1] + "," + sequence[3]);
			}

			//System.out.println(map);
			List<String> neighbors = new ArrayList<>();

			for (String key : map.keySet()) {
				
				Collection values = (Collection) map.get(key);
				Iterator valuesIterator = values.iterator();
				while (valuesIterator.hasNext()) {
					float watchcount = 0;
					float othercount = 0;
					String uservalue = (String) valuesIterator.next();
					String[] split = uservalue.split(",");
					String user = split[0];
					long user_time = Instant.parse(split[1]).getEpochSecond();
					// System.out.println(user);
					br2 = new BufferedReader(new FileReader(file2));
					while ((st2 = br2.readLine()) != null) {
						String[] sequence2 = st2.split(",");
						// System.out.println(key+","+user+","+sequence2[0]);
						if (sequence2[0].equals(user)) {
							neighbors.add(sequence2[1]);
							// System.out.println("matched"+user+","+sequence2[0]);
						}
					}
					br = new BufferedReader(new FileReader(file));
					float countevents = 0;
					while ((st = br.readLine()) != null) {
						
						

						String[] sequence = st.split(",");
						if (sequence[2].equals(key)) {
							countevents++;

							if (!sequence[1].equals(user) && (neighbors.contains(sequence[1]))&& sequence[0].equals("WatchEvent")) {
								long neighbor_time = Instant.parse(sequence[3]).getEpochSecond();
								if (neighbor_time < user_time) {
									watchcount++;
									//System.out.println(key+","+user+","+sequence[1]+","+(neighbor_time-user_time)+","+sequence[0]);
								} 
								else {
									continue;
								}
							} 
							else if (!sequence[1].equals(user) && (neighbors.contains(sequence[1]))&& !sequence[0].equals("WatchEvent"))

							{
								long neighbor_time = Instant.parse(sequence[3]).getEpochSecond();
								if (neighbor_time < user_time) {
									othercount++;
									//System.out.println(key+","+user+","+sequence[1]+","+(neighbor_time-user_time)+","+sequence[0]);
								}
								else {
									continue;
								}
							}
						}
					}
					//System.out.println(countevents);
					//System.out.println(key + "," + user+","+user_time+ "," +watchcount+","+ (watchcount/countevents *100) + "," +othercount+","+ othercount/countevents *100);
					printWriter.print(key + "," + user + "," + watchcount/countevents *100 + "," + othercount/countevents *100);
					printWriter.println();
					// System.out.println(key);
					// System.out.println("Watch events are"+watchcount);
					// System.out.println("other events are"+othercount);
					// break;
				}
				// break;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
