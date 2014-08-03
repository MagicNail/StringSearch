import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;


public class RarityMapTest {

	@Test
	public void test() {
		String s = "banana";
		char text[] = "applebananaorange".toCharArray();
		Map<Character,List<Integer>> index = new HashMap<Character,List<Integer>>();
		for(int j=0;j<text.length;j++){
			if(index.containsKey(text[j])) index.get(text[j]).add(j);
			else {
				List<Integer> list = new ArrayList<Integer>();
				list.add(j);
				index.put(text[j],list);
			}
		}
		RarityMap r = new RarityMap(s,index);
		while(r.hasNext()){
			System.out.println(r.next());
		}
	}

}
