
public class PivotPartitioningByScanning<T extends Comparable<? super T>> {
	int m1,m2,i1,i2,i3,n;
	
	private static void DEBUG(String s) {
		DEBUG_C(s + "\n");
	}
	
	private static void DEBUG_C(String s) {
		System.out.print(s);
	}
	
	private static void DEBUG_F(String format, Object... args) {
		System.out.format(format, args);
	}
	
	int[] partition(T[] S, T p) {
		
		DEBUG_F("Partition\nPivot: %d\n", p);
		
		n=S.length;
		
		// basis
		
		m1 = 0;
		
		for (int j=0; j<n; j++) {
			if (S[j].compareTo(p) < 0) {
				m1++;
			}
		}
		
		m2 = m1;
		
		for (int j=0; j<n; j++) {
			if (S[j].compareTo(p) == 0) {
				m2++;
			}
		}
		
		DEBUG_F("m1, m2 = %d, %d\n",m1,m2);
		
		i1 = 0;
		i2 = m1;
		i3 = m2;
		
		while (i1< m1 && S[i1].compareTo(p)< 0) i1++;
		while (i2< m2 && S[i2].compareTo(p)==0) i2++;
		while (i3<=n-1&& S[i3].compareTo(p)> 0) i3++;
		
		// step
		int i = 0;
		while (true) {
			DEBUG_F("Induction step\ni1, i2, i3 = %d, %d, %d\n",i1,i2,i3);
			printVisual(S);
			
			
			i++;
			if (i>S.length) throw new RuntimeException("too much");
			if (i1 >= m1 && i2 >= m2 && i3 >= n) break;
			asserts(S,p);
			DEBUG_F("Break condition not met: i1 >= m1 && i2 >= m2 && i3 >= n <=> %s && %s && %s\n", i1 >= m1, i2 >= m2, i3 >= n);
			
			// 2
			if (i1 < m1) {
				DEBUG("  2.1: i1 < m1");
				// 2.1
				if (S[i1].equals(p)) {
					DEBUG("    2.1.1: S[i1] == p");
					
					DEBUG("    Swapping i1 and i2");
					
					// 2.1.1
					T tmp = S[i1];
					S[i1] = S[i2];
					S[i2] = tmp;
					
					// 2.1.2
					while (!(i2 >= m2 || !S[i2].equals(p))) { i2++; };
				} else {
					DEBUG("    2.2.1: S[i1] > p");
					assert S[i1].compareTo(p) == 1;
					
					DEBUG("    Swapping i1 and i3");
					
					// 2.2.1
					T tmp = S[i1];
					S[i1] = S[i3];
					S[i3] = tmp;
					
					// 2.2.2
					while (!(i3 >= n || S[i3].compareTo(p) <= 0)) { i3++; }
				}
				DEBUG("  2.3");
				// 2.3
				while (i1<m1 && S[i1].compareTo(p) < 0) { i1++; }
			} else {
				assert i1 == m1;
				DEBUG("  3: i1 == m1");
				
				DEBUG("  Swapping i2 and i3");
				T tmp = S[i2];
				S[i2] = S[i3];
				S[i3] = tmp;				
				
				while (!(i2 >= m2 || S[i2].compareTo(p) != 0)) { i2++; }
				
				while (!(i3 >= n || S[i3].compareTo(p) <= 0)) { i3++; }
			}
			
			asserts(S,p);
		}
		return new int[] {m1, m2-1};
	}
	
	private void printVisual(T[] S) {
		final int v = 6;
		final int w = v + 1;
		for (T i : S) {
			DEBUG_F("%6d",i);
			DEBUG_C(",");
		}
		DEBUG_C("\n");
		DEBUG_C(new String(new char[v-1]).replace("\0", " "));
		for (int i=0; i<S.length*w; i++) {
			double pos = Double.valueOf(i)/w;
			if (pos == Double.valueOf(i1) || pos == Double.valueOf(i2) || pos == Double.valueOf(i3) ) {
				DEBUG_C("^");
			} else {
				DEBUG_C(" ");
			}
		}
		DEBUG_C("\n");
		for (int i=0; i<S.length; i++) {
			
			if (i == i1) {
				DEBUG_F("%"+w+"s", "i1");
			} else if (i == i2) {
				DEBUG_F("%"+w+"s", "i2");
			} else if (i == i3) {
				DEBUG_F("%"+w+"s", "i3");
			} else {
				DEBUG_F("%"+w+"s", "");
			}
		}
		DEBUG_C("\n");
	}

	void asserts(T[] S, T p) {
		assert 0 <= i1;
		assert i1 <= m1;
		assert m1 <= i2;
		assert i2 <= m2;
		assert i3 <= n;

		for (int j=0; j<= i1-1; j++) {
			assert S[j].compareTo(p) < 0;
		}

		for (int j=m1; j<= i2-1; j++) {
			assert S[j].compareTo(p) == 0;
		}

		for (int j=m2; j<= i3-1; j++) {
			assert S[j].compareTo(p) > 0;
		}

	}
	
//	void quicksort(T[] A, int left, int right) {
//		if (!(left < right)) return;
//		
//		int q = partition(A, left, right);
//		
//	}
}
