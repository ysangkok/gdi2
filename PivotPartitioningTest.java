import java.util.Arrays;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;


public class PivotPartitioningTest {
	
	PivotPartitioningByScanning<Integer> sorter;
	
	@Before
	public void init() {
		sorter = new PivotPartitioningByScanning<Integer>();
	}
	
	@Test
	public void testD120() {
		Integer[] A = new Integer[] {2,5,7,1,8,3, 5,4};
		System.out.format("Returned: %s\n",Arrays.toString(sorter.partition(A, 5)));
	}
	
	@Test
	public void testPivotFolie() {
		Integer[] A = new Integer[] {44,75,23,43,55,12,64,77,33};
		System.out.format("Org Array: %s\n",Arrays.toString(A));
		System.out.format("Returned: %s\n",Arrays.toString(sorter.partition(A, A[0])));
		System.out.format("New Array: %s\n",Arrays.toString(A));
	}
	
	@Test
	public void testLots() {
		
		Random r = new Random();
		
		for (int i=0; i<100000; i++) {
			
			Integer[] Aarr = new Integer[10];
			
			for (int j=0; j<Aarr.length; j++) {
				Aarr[j] = (int) (short) r.nextInt();
			}

			//System.out.println(Arrays.toString(Aarr));
			sorter.partition(Aarr, Aarr[r.nextInt(Aarr.length-1)]);
			//System.out.println(Arrays.toString(Aarr));
		}
	}
	@Test
	public void testSmallest() {
		Integer[] Aarr = new Integer[] {60, 50};
		sorter.partition(Aarr, Aarr[1]);
		System.out.println(Arrays.toString(Aarr));
	}
	
	@Test
	public void testRandom1() {
		Integer[] Aarr = new Integer[] {-27425, -5275, 14086, 4030, -141, -11026, -10382, -20510, -27661, 18114};
		sorter.partition(Aarr, Aarr[4]);
		System.out.println(Arrays.toString(Aarr));
	}
	
	@Test
	public void testDuplicate() {
		Integer[] Aarr = new Integer[] {20, 240, 240, 10, 22};
		sorter.partition(Aarr, Aarr[1]);
		System.out.println(Arrays.toString(Aarr));
	}
	
    @Test
    public void testCustomApril() {
        Integer[] Aarr = new Integer[] {3,8,1,5,6,4,0,2};
        sorter.partition(Aarr, Aarr[3]);
        System.out.println(Arrays.toString(Aarr));
    }
	
	public static void main(String[] args) {
		PivotPartitioningTest b = new PivotPartitioningTest();
		b.init();
		b.testLots();
	}
}
