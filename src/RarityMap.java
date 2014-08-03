import java.util.*;
import java.util.*;


public class RarityMap {
	
	private Map<Integer,List<IndexKey>> map = new TreeMap<Integer,List<IndexKey>>();
	private int nextIndex = 0;
	private int listIndex = 0;
	private int maxCount = 0;
	
	public RarityMap(String s,Map<Character,List<Integer>> index){
		for(int i=0;i<s.length();i++){
			if(!index.containsKey(s.charAt(i))) throw new NoSuchElementException();
			if(map.containsKey(index.get(s.charAt(i)).size())){
				map.get(index.get(s.charAt(i)).size()).add(new IndexKey(s.charAt(i),i));
			}
			else{
				List<IndexKey> list = new ArrayList<IndexKey>();
				list.add(new IndexKey(s.charAt(i),i));
				map.put(index.get(s.charAt(i)).size(),list);
			}
			if(index.get(s.charAt(i)).size()>maxCount) maxCount = index.get(s.charAt(i)).size();
		}
	}
	public IndexKey next(){
		while(!map.containsKey(nextIndex)) nextIndex++;
		if(map.get(nextIndex).size()<=listIndex){
			nextIndex++;
			listIndex = 0;
		}
		while(!map.containsKey(nextIndex)) nextIndex++;
		IndexKey ans = map.get(nextIndex).get(listIndex++);
		return ans;
	}
	public void reset(){
		nextIndex = 0;
		listIndex = 0;
	}
	public boolean hasNext(){
		return nextIndex<maxCount || map.get(nextIndex).size()>listIndex;
	}
	
	
}
