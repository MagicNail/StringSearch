import java.io.*;
import java.util.*;

/**
 * A class with a main method that searches through a list of files provided in the command 
 * line as arguments for the count of each phrase in a list of phrases in a file called phrases
 * and print the number of times each phrase occurs in a text, then compares them to see which 
 * are the most similar.
 * 
 * @author Jeff Burns
 *
 */
public class StringSearch {
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
		//Making table of phrases
		int tables[][] = new int[phraseCount][50];
		for(int j=0;j<phraseCount;j++){
			tables[j] = kmpTable(phrases[j].toCharArray(),phrases[j].length());
		}
		//timer to calculate speed
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
			for(int j=0;j<phraseCount;j++){
				//printFile(data);
				counts.get(i).put(phrases[j],match(phrases[j],data,docsize,tables[j]));
			}
			time += System.currentTimeMillis()-t;
		}
		//printCounts(phrases,phraseCount,counts);
		//Compare all the collected data
		double comparisons[][] = new double[args.length][args.length];
		for(int i=0;i<args.length;i++){
			for(int j=0;j<args.length;j++){
				//Cosine Similarity check
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
	public static int match(String phrase,char text[],int len,int table[]){
		int count = 0;
		int i = 0;
		int m = 0;
		while(m+i<len){
			if(phrase.charAt(i) == text[m+i]){
				//if a match is found
				i++;
				if(i==phrase.length()){
					count++;
					m++;
					i = 0;
					continue;
				}
			}
			//text does not match
			else{
				m = m + i - table[i];
				if(table[i]>-1) i = table[i];
				else i = 0;
			}
			
		}
		return count;
	}
	/**
	 * Generate a kmp table according to the algorithm on this page:
	 * http://en.wikipedia.org/wiki/Knuth%E2%80%93Morris%E2%80%93Pratt_algorithm
	 * 
	 * @param text to generate from
	 * @param len, number of chars in array
	 * @return kmp table
	 */
	public static int[] kmpTable(char text[],int len){
		if(len<2) return new int[2];
		int result[] = new int[len];
		int pos = 2;
		int cnd = 0;
		result[0] = -1;
		result[1] = 0;
		while(pos<len){
			if(text[pos] == text[cnd]){
				cnd++;
				result[pos++] = cnd;
			}
			else if(cnd > 0){
				cnd = result[cnd];
			}
			else{
				result[pos++] = 0;
			}
		}
		return result;
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
