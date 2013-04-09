import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;


public class MatchingBasedOnFiniteAutomaton implements StringMatcher {

	List<Integer> R;
	TreeMap<Integer,TreeMap<Character,Integer>> delta;
	int q;
	int m;
	
	@Override
	public StringMatcher initialize(String pattern) {
		m = pattern.length();
		R = new LinkedList<Integer>();
		q = 0;
		delta = new TreeMap<Integer, TreeMap<Character,Integer>>();
		Set<Character> alphabet = new HashSet<Character>();
		for (char i=32; i<=126; i++) alphabet.add(i);
//		for (char i : pattern.toCharArray()) {
//			alphabet.add(i);
//		}
		
		for (int j=0; j<=m; j++) {
			for (char c : alphabet) {
				int k = Math.min(m, j+1);
				for (;;) {

					StringBuilder part2builder = new StringBuilder();
					
					for (int i = j-k+2; i<=j; i++) {
						part2builder.append(pattern.charAt(i-1));
					}
					part2builder.append(c);
					
					CharSequence p1 = pattern.subSequence(0, k);
					CharSequence p2 = part2builder.toString();
					
					if (k>0 && !p1.equals(p2)) {
						k--;
						continue;
					}
					break;
				}
				
				if (!delta.containsKey((Integer) j)) {
					delta.put(j,new TreeMap<Character, Integer>());
				}
				delta.get(j).put(c, k);
			}
		}
		
		return this;
	}

	@Override
	public List<Integer> getIndices(String text) {
		for (int i = 0; i < text.length(); i++) {
//			try {
				TreeMap<Character,Integer> temp = delta.get(q);
				Integer w = temp.get(text.charAt(i));
				if (w == null) throw new RuntimeException(String.format("\"%c\" not in alphabet!",text.charAt(i)));
				q = (int) w;
//			} catch (Exception e) {
//				break;
//			}
			if (q == m) {
				R.add(i-m+1);
			}
		}
		return R;
	}
	
	public static void main(String[] args) {
		String pattern = "ABCDABD";
		MatchingBasedOnFiniteAutomaton ins = ((MatchingBasedOnFiniteAutomaton) new MatchingBasedOnFiniteAutomaton().initialize(pattern));
		for (Integer i : ins.getDelta().keySet()) {
			System.out.format("%d: %s\n", i, ins.getDelta().get(i));
		}
	}

	private TreeMap<Integer,TreeMap<Character,Integer>> getDelta() {
		return delta;
	}

}
