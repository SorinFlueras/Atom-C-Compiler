
public class Main {

	public static void main(String[] args) {
		LexicalAnalyzer lex = new LexicalAnalyzer();
		lex.fileReader("/Users/sorinalexandruflueras/Downloads/tests 2/test.c");
		lex.getNextToken();
		lex.addToken("END", "END", -1);
		System.out.println(lex.getTokens());
		SyntacticAnalyzer syn = new SyntacticAnalyzer(lex.getTokens());
		System.out.println(syn.ruleTypeBase());
	}

}
