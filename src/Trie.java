import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public class Trie implements Iterable<String>{
	
	private Character data;
	private Map<Character,Trie> children = new HashMap<Character,Trie>();
	private boolean isWord = false;
	private Set<String> strings;
	
	public Trie(){
		strings = new TreeSet<String>();
	}
	private Trie(String s){
		if(s.length() == 0) return;
		this.data = s.charAt(0);
		isWord = s.length() == 1;
		if(s.length()>1) children.put(s.charAt(1),new Trie(s.substring(1)));
	}
	/*
	public Trie(String s,boolean isWord){
		if(s.length() == 0) return;
		this.data = s.charAt(0);
		this.isWord = isWord;
		if(s.length()>1) children.put(s.charAt(1),new Trie(s.substring(1)));
	}*/
	
	public boolean add(String s) {
		if(s.length() == 0) return false;
		if(contains(s)) return false;
		if(s.length()>0 && !children.containsKey(s.charAt(0))) children.put(s.charAt(0),new Trie(s));
		else{
			children.get(s.charAt(0)).add(s.substring(1));
			if(s.length() == 1) children.get(s.charAt(0)).isWord = true;
		}
		if(data == null) strings.add(s);
		return true;
	}
	
	public boolean contains(String s) {
		//System.out.println(data + " " +s+" " +isWord);
		//System.out.println(children.keySet()+" : "+children.values());
		if(s.length() == 0) return true;
		if(s.length() == 1){
			if(data != null) return data == s.charAt(0) && isWord;
			return children.keySet().contains(s.charAt(0)) && children.get(s.charAt(0)).contains(s);
		}
		if(data != null) return children.keySet().contains(s.charAt(1)) && children.get(s.charAt(1)).contains(s.substring(1));
		return children.keySet().contains(s.charAt(0)) && children.get(s.charAt(0)).contains(s);
	}
	public Trie child(char c){
		return children.get(c);
	}
	public boolean isEmpty() {
		return children.size() == 0;
	}
	public Set<String> asSet(){
		return strings;
	}
	public boolean isWord(){
		return isWord;
	}
	public char getChar(){
		return data;
	}
	public Iterator<String> iterator() {
		return strings.iterator();
	}

	
	public boolean remove(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public int size() {
		return strings.size();
	}
	public String toString(){
		return "Trie = "+data+" : "+children.keySet();
	}
}
