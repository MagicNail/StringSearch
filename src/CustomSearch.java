import java.io.*;
import java.util.*;

/**
 * A class with a main method that searches through a list of files provided in the command 
 * line as arguments for the count of each phrase in a list of phrases in a file called phrases
 * and print the number of times each phrase occurs in a text, then compares them to see which 
 * are the most similar.
 * 
 * Uses the slow string search algorithm. I made this to test if my other ones were working correctly
 * 
 * @author Jeff Burns
 *
 */
public class CustomSearch {
	public static void main(String[] args){
		//List of maps to store data collected
		List<HashMap<String, Integer>> counts = new ArrayList<HashMap<String,Integer>>();
		for(int i=0;i<args.length;i++){
			counts.add(new HashMap<String,Integer>());
		}
		//Reading Phrases file
		String phrases[] = new String[1000];
		int phraseCount = 0;
		try {
			Scanner sc = new Scanner(new File("phrases"));
			for(int i=0;sc.hasNextLine();i++){
				phrases[i] = sc.nextLine();
				phraseCount++;
			}
		} catch (IOException e1) {System.out.println("Cant find phrases file");}
		//timer to measure search time
		long time = 0;
		//Loop to read files from arguments and check each one for number 
		//of occurrences of each phrase
		for(int i=0;i<args.length;i++){
			char data[] = new char[250000];
			int docsize=0;
			try{
				FileReader fr = new FileReader(args[i]);
				docsize = fr.read(data,0,200000);
			}
			catch(IOException e){
				System.out.println("File Error on "+args[i]+": "+e);
			}
			long t = System.currentTimeMillis();
			Map<Character,List<Integer>> index = new HashMap<Character,List<Integer>>();
			for(int j=0;j<docsize;j++){
				if(index.containsKey(data[j])) index.get(data[j]).add(j);
				else {
					List<Integer> list = new ArrayList<Integer>();
					list.add(j);
					index.put(data[j],list);
				}
			}
			counts.add(i,search(phrases,index,data,docsize,phraseCount));
			time += System.currentTimeMillis()-t;
		}
		System.out.println(time);
		//printCounts(phrases,phraseCount,counts);
		//Compare all the collected data
		double comparisons[][] = new double[args.length][args.length];
		for(int i=0;i<args.length;i++){
			for(int j=0;j<args.length;j++){
				//Cosine Similarity check
				//System.out.println(i + " " + counts.get(i));
				int a[] = new int[phraseCount];
				int b[] = new int[phraseCount];
				for(int k=0;k<phraseCount;k++){
					a[k] = counts.get(i).get(phrases[k]);
					b[k] = counts.get(j).get(phrases[k]);
				}
				comparisons[i][j] = calculateCosineSimilarity(a,b);
			}
		}
		//Print the results in table form
		printComparisonTable(comparisons,args);
		//System.out.println(time);
	}
	private static double calculateCosineSimilarity(int a[], int b[]){
		double c = 0;
		for(int i=0;i<a.length;i++){
			c += a[i]*b[i];
		}
		double d = arraySquareSum(a)*arraySquareSum(b);
		return c/d;
	}
	private static void printComparisonTable(double data[][],String files[]){
		System.out.printf("%22s","");
		for(int i=0;i<data.length;i++){
			System.out.printf("%22s",files[i]);
		}
		System.out.println();
		for(int i=0;i<data.length;i++){
			System.out.printf("%22s",files[i]);
			for(int j=0;j<data.length;j++){
				if(i<j) System.out.printf("%22s","");
				else if(i==j) System.out.printf("%22s","1.0");
				else System.out.printf("%22s",data[i][j]);
			}
			System.out.println();
		}
	}
	/**
	 * Sum all elements of an integer array
	 * 
	 * @param array
	 * @return sum
	 */
	public static double arraySum(int a[]){
		int sum = 0;
		for(int i:a) sum += i;
		return sum;
	}
	/**
	 * Returns square root of sum of all elements in the array squared individually
	 * 
	 * @param array
	 * @return sum
	 */
	public static double arraySquareSum(int a[]){
		int sum = 0;
		for(int i:a) sum += i*i;
		return Math.sqrt(sum);
	}
	/**
	 * Search for matches and return the number found
	 * 
	 * @param phrase to search for
	 * @param text to search in
	 * @param len, number of chars in array
	 * @return number of matches found
	 */
	public static HashMap<String,Integer> search(String phrases[],Map<Character,List<Integer>> index,char text[],int len,int phraseCount){
		HashMap<String,Integer> ans = new HashMap<String,Integer>();
		RarityMap maps[] = new RarityMap[phraseCount];
		for(int i=0;i<phraseCount;i++){
			ans.put(phrases[i],0);
			maps[i] = new RarityMap(phrases[i],index);
			IndexKey k = maps[i].next();
			int pos = 0;
			if(!index.containsKey(k.key)) continue;
			else{
				pos = index.get(k.key).get(0)-k.index;
			}
			//System.out.println(pos);
			while(maps[i].hasNext()){
				k = maps[i].next();
				if(!index.containsKey(k.key))
				maps[i].reset();
				break;
			}
		}
		return ans;
	}
	
	private static void printCounts(String phrases[],int phraseCount,List<HashMap<String, Integer>> counts){
		for(int i=0;i<phraseCount;i++){
			System.out.printf("%-30s",phrases[i]);
			for(HashMap<String,Integer> map:counts){
				System.out.printf(" %2s",map.get(phrases[i]));
			}
			System.out.println();
		}
	}
	//Test Code
	private static void printFile(char data[]){
		for(char c:data) System.out.print(c);
	}
}
