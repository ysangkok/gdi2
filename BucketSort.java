import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;


class Multiset<T> extends ArrayList<T> {
	private static final long serialVersionUID = 1L;

	public Multiset() {
		super();
	}
}

public class BucketSort<Y extends Comparable<? super Y>, Str extends List<Y>> {

	int N;
	List<Str> S_quote;
	TreeMap<Y,List<Str>> B;
	List<Multiset<Str>> A;
	
	BucketSort(int N) {
		this.N = N;
	}
	
	void initB(Set<Y> alphabet) {
		B = new TreeMap<Y, List<Str>>();
		for (Y c : alphabet) {
			B.put(c, new ArrayList<Str>());
		}
	}
	
	void basis(Set<Y> alphabet, List<Str> S) {
		// 1
		S_quote = new ArrayList<Str>();
		
		// 2
		initB(alphabet);
		
		// 3
		A = new ArrayList<Multiset<Str>>();
		for (int i=1; i<=N; i++) {
			A.add(i-1, new Multiset<Str>());
		}
		
		// 4
		for (Str str : S) {
			A.get(str.size()-1).add(str);
		}
		
		System.out.println(String.format("N=%s\nS_quote=%s\nB=%s\nA=%s\n---\n",N,S_quote,B,A));
	}
	
	List<Str> sort(Set<Y> alphabet, List<Str> S) {
		// induction basis
		basis(alphabet, S);
		
		for (int i=0; i<N; i++) {
			// induction step
			initB(alphabet);
			
			// 1
			Iterator<Str> ite = A.get(N-i-1).iterator();
			while (ite.hasNext()) {
				Str str = ite.next();
				System.out.format("From A to B: %s\n",str);
				
				ite.remove();
				B.get(str.get(N-i-1)).add(str);
			}
			
			// 2
			System.out.println("S_quote Z 75: " + S_quote.toString());
			
			ite = S_quote.iterator();
			while (ite.hasNext()) {
				Str str = ite.next();

				ite.remove();

				//System.out.println(str.get(N-i));
				//System.out.println(String.format("%s\n", str));
				B.get(str.get(N-i)).add(0,str);
			}
			
			assert S_quote.size() == 0;
			
			List<Y> orderedalphabet = new ArrayList<Y>();
			orderedalphabet.addAll(alphabet);
			Collections.sort(orderedalphabet);
			
			// 3
			for (Y c : orderedalphabet) {
				S_quote.addAll(B.get(c));
			}
			
//			for (Entry<Y, List<Str>> l : B.entrySet()) {
//				assert l.getValue().size() == 0;
//			}
			System.out.format("---\ni=%d\nB=%s\nS_quote=%s\n",i,B,S_quote);
		}
		
		return S_quote;
	}

}
