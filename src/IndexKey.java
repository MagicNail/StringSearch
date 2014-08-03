
public class IndexKey {
	
	public final char key;
	public final int index;
	
	public IndexKey(char key,int index){
		this.index = index;
		this.key = key;
	}
	public String toString(){
		return key+" "+index;
	}
	
}
