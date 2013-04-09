import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

import org.junit.Test;


public class BucketSortTest {

	List<List<Character>> strings = new ArrayList<List<Character>>();

	private Character[] toBoxed(char[] input) {
		Character[] boxed = new Character[input.length];
		int i=0;
		for (char value : input) {
			boxed[i++] = value;
		}
		return boxed;
	}

	
	@Test
	public void testCharacter() {
		BucketSort<Character, List<Character>> bs = new BucketSort<Character, List<Character>>(3);
		
		for (String i : new String[] {"c", "bcc", "aba", "ab", "aa", "b"}) {
			strings.add(Arrays.asList(toBoxed(i.toCharArray())));
		}
		
		List<List<Character>> res = bs.sort(
				new HashSet<Character>(Arrays.asList(new Character[] {'a', 'b', 'c'})),
				strings
		);
		assertEquals(Arrays.asList(
				Arrays.asList(toBoxed("aa".toCharArray())),
				Arrays.asList(toBoxed("ab".toCharArray())),
				Arrays.asList(toBoxed("aba".toCharArray())),
				Arrays.asList(toBoxed("b".toCharArray())),
				Arrays.asList(toBoxed("bcc".toCharArray())),
				Arrays.asList(toBoxed("c".toCharArray()))
		), res);
	}
	
	@Test
	public void testNibble() {
		assertEquals(new Nibble((byte) 15), new Nibble((byte) 15));
		assertFalse(new Nibble((byte) 15).equals(new Nibble((byte) 14)));
	}
	@Test(expected=RuntimeException.class)
	public void testNibbleInvalid() {
		new Nibble((byte) 16);
	}
	@Test(expected=RuntimeException.class)
	public void testNibbleInvalid2() {
		new Nibble((byte) 255);
	}
	@Test
	public void testNibbleSort() {
		ShortEmulatorList[] arr = new ShortEmulatorList[] {new ShortEmulatorList(0), new ShortEmulatorList(213), new ShortEmulatorList(13), new ShortEmulatorList(6)};
		List<ShortEmulatorList> l = Arrays.asList(arr);
		Collections.sort(l);
		assertTrue( l.get(0).value == 0);
		assertTrue( l.get(1).value == 6);
		assertTrue( l.get(2).value == 13);
		assertTrue( l.get(3).value == 213);
	}
	
	@Test
	public void testShortEmulaterListNibble() {
		ShortEmulatorList sel = new ShortEmulatorList(0x00001234);
		List<String> l = new ArrayList<String>();
		l.add(sel.get(0).toBinaryString());
		l.add(sel.get(1).toBinaryString());
		l.add(sel.get(2).toBinaryString());
		l.add(sel.get(3).toBinaryString());
		assertEquals(Arrays.asList(new String[] {"0001","0010","0011","0100"}), l);
	}
	
	@Test
	public void testShort() {
		BucketSort<Nibble, ShortEmulatorList> bs = new BucketSort<Nibble, ShortEmulatorList>(ShortEmulatorList.size); // 4*nibble(4 bits) = 16 bits = short
		List<ShortEmulatorList> values = new ArrayList<ShortEmulatorList>();
		values.add(new ShortEmulatorList((short) 0));
		values.add(new ShortEmulatorList((short) 32767));
		values.add(new ShortEmulatorList((short) 1));
		values.add(new ShortEmulatorList((short) 32766));
		values.add(new ShortEmulatorList((short) 2));
		assertEquals(Arrays.asList(new ShortEmulatorList[] {new ShortEmulatorList((short) 0), new ShortEmulatorList((short) 1), new ShortEmulatorList((short) 2), new ShortEmulatorList((short) 32766), new ShortEmulatorList((short) 32767)}), bs.sort(Nibble.getAll(), values));
		
	}
	
	public static void main(String[] args) {
		System.err.println(Integer.valueOf(new Nibble((byte) 15).hashCode()).equals(15));
		System.err.println(new Nibble((byte) 15).equals(new Nibble((byte) 15)));
	}
	
	@Test
	public void testShortEmulatorList() {
		assertEquals(new ShortEmulatorList((short) 32767), new ShortEmulatorList((short) 32767));
	}

}

class Nibble implements Comparable<Nibble> {

	
	byte value;
	
	Nibble(byte v) {
		if (v >> 4 != 0) throw new RuntimeException("too large for a nibble!");
		value = (byte) (v & 0x0000000F);
	}
	
	public static Set<Nibble> getAll() {
		Set<Nibble> set = new TreeSet<Nibble>();
		for (byte i=0; i<=15; i++) {
			set.add(new Nibble(i));
		}
		return set;
	}

	@Override
	public int compareTo(Nibble other) {
		return new Byte(this.value).compareTo(other.value);
	}
	
	public String toBinaryString() {
		return String.format("%4s", Integer.toBinaryString(value & 0x0000000F)).replace(' ', '0');
	}
	
	@Override
	public String toString() {
		return Byte.valueOf(value).toString();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Nibble)
		return value == ((Nibble) other).value;
		else return false;
	}
	
	@Override
	public int hashCode() {
		return Byte.valueOf(value).hashCode();
	}
}
class ShortEmulatorList implements List<Nibble>, Comparable<ShortEmulatorList> {

	@Override
	public int hashCode() {
		return Short.valueOf(value).hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof ShortEmulatorList)
		return value == ((ShortEmulatorList) other).value;
		else return false;
	}
	
	public String toString() {
		//return Short.valueOf(value).toString();
		
		return Integer.toBinaryString(0x10000 | value).substring(1);
	}
	
	short value;

	public ShortEmulatorList(short v) {
		this.value = v;
	}
	
	public ShortEmulatorList(int i) {
		this((short) i);
		assert ((int) ((short) i)) == i;
	}

	final static int size = 4;
	
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public boolean contains(Object o) {
		throw new RuntimeException();
	}

	@Override
	public Iterator<Nibble> iterator() {
		throw new RuntimeException();
	}

	@Override
	public Object[] toArray() {
		throw new RuntimeException();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new RuntimeException();
	}

	@Override
	public boolean add(Nibble e) {
		throw new RuntimeException();
	}

	@Override
	public boolean remove(Object o) {
		throw new RuntimeException();
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new RuntimeException();
	}

	@Override
	public boolean addAll(Collection<? extends Nibble> c) {
		throw new RuntimeException();
	}

	@Override
	public boolean addAll(int index, Collection<? extends Nibble> c) {
		throw new RuntimeException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new RuntimeException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new RuntimeException();
	}

	@Override
	public void clear() {
		throw new RuntimeException();
	}

	@Override
	public Nibble get(int index) {
		assert index <= 3 && index >= 0;
		return new Nibble((byte) (((value >> ((3-index)*4))) & 0x0000000F));
	}

	@Override
	public Nibble set(int index, Nibble element) {
		throw new RuntimeException();
	}

	@Override
	public void add(int index, Nibble element) {
		throw new RuntimeException();
	}

	@Override
	public Nibble remove(int index) {
		throw new RuntimeException();
	}

	@Override
	public int indexOf(Object o) {
		throw new RuntimeException();
	}

	@Override
	public int lastIndexOf(Object o) {
		throw new RuntimeException();
	}

	@Override
	public ListIterator<Nibble> listIterator() {
		throw new RuntimeException();
	}

	@Override
	public ListIterator<Nibble> listIterator(int index) {
		throw new RuntimeException();
	}

	@Override
	public List<Nibble> subList(int fromIndex, int toIndex) {
		throw new RuntimeException();
	}

	@Override
	public int compareTo(ShortEmulatorList o) {
		return Short.valueOf(value).compareTo(o.value);
	}
	
}
