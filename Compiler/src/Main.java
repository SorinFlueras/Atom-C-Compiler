
public class Main {

	public static void main(String[] args) {
		LexicalAnalyzer lex = new LexicalAnalyzer();
		lex.fileReader("C:\\Users\\Sorin\\Downloads\\tests\\8.c");
		lex.getNextToken();
		lex.addToken("END", "END", -1);
		System.out.println(lex.getTokens());
	}

}
