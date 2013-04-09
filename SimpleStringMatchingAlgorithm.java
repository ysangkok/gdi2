import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class SimpleStringMatchingAlgorithm implements StringMatcher {

	Queue<Integer> I;
	List<Integer> R;
	String pattern;
	
	@Override
	public StringMatcher initialize(String pattern) {
		I = new LinkedList<Integer>();
		R = new LinkedList<Integer>();
		this.pattern = pattern;
		
		return this;
	}

	@Override
	public List<Integer> getIndices(String input) {
		final int m = pattern.length();
		for (int iidx = 0; iidx < input.length(); iidx++) {
			Iterator<Integer> ite = I.iterator();
			while (ite.hasNext()) {
				int idx = ite.next();
				boolean stillCandidate = input.charAt(iidx) == pattern.charAt(iidx-idx);
				if (!stillCandidate) {
					ite.remove();
				}
			}
			
			//afterwards
			
			if (I.peek() != null && I.peek() == iidx-m+1) {
				R.add(I.poll());
			}
			
			if (input.charAt(iidx) == pattern.charAt(0)) {
				I.add(iidx);
			}
		}
		
		return R;
	}

}
