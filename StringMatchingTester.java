import java.util.List;


public class StringMatchingTester {
	public static void main(String[] args) {
		String input = "ABC ABCDAB ABCDABCDABDE";
		String pattern = "ABCDABD";
		List<Integer> l = new MatchingBasedOnFiniteAutomaton().initialize(pattern).getIndices(input);
		//List<Integer> l = new SimpleStringMatchingAlgorithm().initialize(pattern).getIndices(input);
		System.out.println(l);
		int i = 1;
		for (int idx : l) {
			int i1 = idx-2;
			int i2 = pattern.length()+idx+2;
			int pad = 2;
			String extract = null;
			do {
				try {
					extract = input.substring(i1, i2);
				} catch (StringIndexOutOfBoundsException e) {
					i1++;
					i2--;
					pad--;
					continue;
				}
				break;
			} while (true);
			System.out.format("Result %3d: ...%s...\n", i++, extract);
			System.out.format("               %s%s\n", new String(new char[(pad<0?0:pad)]).replace("\0", " "), new String(new char[pattern.length()]).replace("\0", "^"));
		}
	}
}
