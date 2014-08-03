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
public class TrieSearch {
	public static void main(String[] args){
		//List of maps to store data collected
		List<HashMap<String, Integer>> counts = new ArrayList<HashMap<String,Integer>>();
		//Reading Phrases file
		Trie phrases;
		int phraseCount = 0;
		try {
			Scanner sc = new Scanner(new File("phrases"));
			phrases = generateTrie(sc);
		} catch (IOException e1) {
			System.out.println("Cant find phrases file"); 
			return;
		}
		//Making table of phrases
		int tables[][] = new int[phraseCount][50];
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
			counts.add(search(phrases,data,docsize));
			time += System.currentTimeMillis()-t;
			/*System.out.println("---- "+counts.get(i));
			for(String s:counts.get(i).keySet()){
				System.out.println(s+" : "+counts.get(i).get(s));
			}*/
		}
		//printCounts(phrases,phraseCount,counts);
		//Compare all the collected data
		double comparisons[][] = new double[args.length][args.length];
		for(int i=0;i<args.length;i++){
			for(int j=0;j<args.length;j++){
				//Cosine Similarity check
				Iterator<String> p = phrases.asSet().iterator();
				int a[] = new int[phrases.size()];
				int b[] = new int[phrases.size()];
				for(int k=0;k<phrases.size();k++){
					String s = p.next();
					//System.out.println(s);
					a[k] = counts.get(i).get(s);
					b[k] = counts.get(j).get(s);
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
	public static HashMap<String,Integer> search(Trie S,char T[],int len){
		HashMap<String,Integer> ans = new HashMap<String,Integer>();
		for(String s:S){
			ans.put(s,0);
		}
		for(int pos=0;pos<len;pos++){
			Trie node = S;
			int s = 0;
			String word = "";
			while(pos+s<len){
				Trie child = node.child(T[pos+s]);
				if(child == null) break;
				word += child.getChar();
				if(child.isWord()){
					ans.put(word,ans.get(word)+1);
				}
				node = child;
				s++;
			}
		}
		return ans;
	}
	/**
	 * Create a Trie from file
	 * @param scanner of file
	 * @return Trie
	 */
	private static Trie generateTrie(Scanner sc){
		Trie t = new Trie();
		while(sc.hasNextLine()){
			t.add(sc.nextLine());
		}
		return t;
	}
	private static void printCounts(Trie phrases,int phraseCount,List<HashMap<String, Integer>> counts){
		for(String s:phrases){
			System.out.printf("%-30s",s);
			for(HashMap<String,Integer> map:counts){
				System.out.printf(" %2s",map.get(s));
			}
			System.out.println();
		}
	}
	//Test Code
	private static void printFile(char data[]){
		for(char c:data) System.out.print(c);
	}
}
