import java.util.List;


public interface StringMatcher {
	StringMatcher initialize(String pattern);
	List<Integer> getIndices(String text);
}
