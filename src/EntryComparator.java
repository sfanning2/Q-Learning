import java.util.Comparator;
import java.util.Map.Entry;


public class EntryComparator implements Comparator<Entry<?,?>> {

	@Override
	public int compare(Entry<?, ?> o1, Entry<?, ?> o2) {
		// TODO Auto-generated method stub
		return o1.getKey().toString().compareTo(o2.getKey().toString());
	}
}
