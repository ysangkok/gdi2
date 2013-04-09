
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;


public class BucketSortMain {
	static List<List<Character>> strings = new ArrayList<List<Character>>();

	private static Character[] toBoxed(char[] input) {
		Character[] boxed = new Character[input.length];
		int i=0;
		for (char value : input) {
			boxed[i++] = value;
		}
		return boxed;
	}


	public static void main(String[] args) {
		
		BucketSort<Character, List<Character>> bs = new BucketSort<Character, List<Character>>(3);
		
		for (String i : new String[] {"c", "bcc", "aba", "ab", "aa", "b"}) {
			strings.add(Arrays.asList(toBoxed(i.toCharArray())));
		}
		
		List<List<Character>> res = bs.sort(
				new HashSet<Character>(Arrays.asList(new Character[] {'a', 'b', 'c'})),
				strings
		);
		System.out.println(res);
/*
		assertEquals(Arrays.asList(
				Arrays.asList(toBoxed("aa".toCharArray())),
				Arrays.asList(toBoxed("ab".toCharArray())),
				Arrays.asList(toBoxed("aba".toCharArray())),
				Arrays.asList(toBoxed("b".toCharArray())),
				Arrays.asList(toBoxed("bcc".toCharArray())),
				Arrays.asList(toBoxed("c".toCharArray()))
		), res);
*/

	}
}
