import static org.junit.Assert.*;
import org.junit.Test;

public class TrieTest {

	@Test
	public void testConstruct() {
		Trie t = new Trie();
		assertFalse(t.contains("s"));
	}
	@Test
	public void testAdd(){
		Trie t = new Trie();
		t.add("s");
		assertTrue(t.contains("s"));
		t.add("cake");
		assertTrue(t.contains("cake"));
		assertFalse(t.contains("banana"));
		assertFalse(t.contains("ca"));
		assertFalse(t.contains("cakes"));
		t.add("ca");
		assertTrue(t.contains("cake"));
		assertTrue(t.contains("ca"));
		assertFalse(t.contains("cak"));
		t.add("cakestuff");
		assertTrue(t.contains("cake"));
		assertTrue(t.contains("ca"));
		assertTrue(t.contains("cakestuff"));
		assertFalse(t.add("cake"));
		for(Object s:t){
			System.out.println("K: "+s);
		}
	}

}
