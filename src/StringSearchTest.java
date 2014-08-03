import static org.junit.Assert.*;

import org.junit.Test;


public class StringSearchTest {

	@Test
	public void testTable() {
		StringSearch.kmpTable("PARTICIPATE IN PARACHUTE".toCharArray(), 24);
	}

}
