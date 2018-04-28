import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LexicalAnalyzer {
	private ArrayList<Token> tokens;
	private Map<Integer, String> linesMap;
	
	public LexicalAnalyzer() {
		tokens = new ArrayList<>();
		linesMap = new HashMap<Integer, String>();
	}
	
	public ArrayList<Token> getTokens() {
		return tokens;
	}

	public void setTokens(ArrayList<Token> tokens) {
		this.tokens = tokens;
	}

	public Map<Integer, String> getLinesMap() {
		return linesMap;
	}

	public void setLinesMap(Map<Integer, String> linesMap) {
		this.linesMap = linesMap;
	}

	//reads file line by line and stores every line with it's content in linesMap
	public void fileReader(String fileLocation) {
		int lineCount = 1;
		
		File file = new File(fileLocation);
	    if (!file.exists()) {
	    	  System.err.println(fileLocation + " does not exist.");
	    }
	    if (!(file.isFile() && file.canRead())) {
	      System.err.println(file.getName() + " cannot be read from.");
	    }
	    try {
	    		FileReader fileReader = new FileReader(file);
	    		BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				line += '\n';
				linesMap.put(lineCount, line);
				lineCount++;
			}
			fileReader.close();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	}
	
	public static String getAtom(String s, int i) {
        int j = i;
        for( ; j < s.length(); ) {
            if(Character.isLetter(s.charAt(j))) {
                j++;
            } else {
                return s.substring(i, j);
            }
        }
        return s.substring(i, j);
    }
	
	public void addToken(String code, Object attribute, int line) {
		Token tk = new Token(code,attribute,line);
		tokens.add(tk);
	}
	
	public void getNextToken() {
		int state = 0;
		String content;
		String contentForComment = "";
		for(Entry<Integer, String> entry : linesMap.entrySet()) {
			/*if(contentForComment == "") {
				state = 0;
			}*/
			content = "";
			//System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			for(int i = 0; i < entry.getValue().length(); i++) {
				switch(state) {
					case 0: 
						content = "";
						if(entry.getValue().charAt(i) == ' ' || entry.getValue().charAt(i) == '\n' || entry.getValue().charAt(i) == '\r' || entry.getValue().charAt(i) == '\t') {
							//addToken("SPACE","-",entry.getKey());
							break;
						}
						if(Character.isLetter(entry.getValue().charAt(i)) || entry.getValue().charAt(i) == '_') {
							content += entry.getValue().charAt(i);
							state = 1;
							break;
						}
						if(Character.isDigit(entry.getValue().charAt(i)) && entry.getValue().charAt(i) != '0' ) {
							content += entry.getValue().charAt(i);
							state = 3;
							break;
						}
						if(entry.getValue().charAt(i) == '0') {
							content += entry.getValue().charAt(i);
							state = 10;
							break;
						}
						if(entry.getValue().charAt(i) == '\'') {
							//content += entry.getValue().charAt(i); PENTRU CHAR FARA '
							state = 20;
							break;
						}
						if(entry.getValue().charAt(i) == '"') {
							//content += entry.getValue().charAt(i); // PENTRU STRING FARA ""
							state = 16;
							break;
						}
						if(entry.getValue().charAt(i) == ',') {
							content += entry.getValue().charAt(i);
							state = 24;
							break;
						}
						if(entry.getValue().charAt(i) == ';') {
							content += entry.getValue().charAt(i);
							state = 25;
							break;
						}
						if(entry.getValue().charAt(i) == '(') {
							content += entry.getValue().charAt(i);
							state = 26;
							break;
						}
						if(entry.getValue().charAt(i) == ')') {
							content += entry.getValue().charAt(i);
							state = 27;
							break;
						}
						if(entry.getValue().charAt(i) == '[') {
							content += entry.getValue().charAt(i);
							state = 28;
							break;
						}
						if(entry.getValue().charAt(i) == ']') {
							content += entry.getValue().charAt(i);
							state = 29;
							break;
						}
						if(entry.getValue().charAt(i) == '{') {
							content += entry.getValue().charAt(i);
							state = 30;
							break;
						}
						if(entry.getValue().charAt(i) == '}') {
							content += entry.getValue().charAt(i);
							state = 31;
							break;
						}
						if(entry.getValue().charAt(i) == '+') {
							content += entry.getValue().charAt(i);
							state = 32;
							break;
						}
						if(entry.getValue().charAt(i) == '-') {
							content += entry.getValue().charAt(i);
							state = 33;
							break;
						}
						if(entry.getValue().charAt(i) == '*') {
							content += entry.getValue().charAt(i);
							state = 34;
							break;
						}
						if(entry.getValue().charAt(i) == '/') {
							contentForComment += entry.getValue().charAt(i);
							state = 35;
							break;
						}
						if(entry.getValue().charAt(i) == '.') {
							content += entry.getValue().charAt(i);
							state = 37;
							break;
						}
						if(entry.getValue().charAt(i) == '&') {
							content += entry.getValue().charAt(i);
							state = 38;
							break;
						}
						if(entry.getValue().charAt(i) == '|') {
							content += entry.getValue().charAt(i);
							state = 40;
							break;
						}
						if(entry.getValue().charAt(i) == '!') {
							content += entry.getValue().charAt(i);
							state = 42;
							break;
						}
						if(entry.getValue().charAt(i) == '=') {
							content += entry.getValue().charAt(i);
							state = 44;
							break;
						}
						if(entry.getValue().charAt(i) == '<') {
							content += entry.getValue().charAt(i);
							state = 48;
							break;
						}
						if(entry.getValue().charAt(i) == '>') {
							content += entry.getValue().charAt(i);
							state = 51;
							break;
						}
					case 1:  
						if(Character.isLetterOrDigit(entry.getValue().charAt(i)) || entry.getValue().charAt(i) == '_') {
							content += entry.getValue().charAt(i);
							break;
						}
						else {
							state = 2;
							i--;
							break;
						}
						//break;
					case 2: 
						if(content.equals("break")) {
							addToken("BREAK", "-", entry.getKey());
							state = 0;
							i = i-1;
							break;
						}
						if(content.equals("char")) {
							addToken("CHAR", "-", entry.getKey());
							state = 0;
							i = i-1;
							break;
						}
						if(content.equals("double")) {
							addToken("DOUBLE", "-", entry.getKey());
							state = 0;
							i = i-1;
							break;
						}
						if(content.equals("else")) {
							addToken("ELSE", "-", entry.getKey());
							state = 0;
							i = i-1;
							break;
						}
						if(content.equals("for")) {
							addToken("FOR", "-", entry.getKey());
							state = 0;
							i = i-1;
							break;
						}
						if(content.equals("if")) {
							addToken("IF", "-", entry.getKey());
							state = 0;
							i = i-1;
							break;
						}
						if(content.equals("int")) {
							addToken("INT", "-", entry.getKey());
							state = 0;
							i = i-1;
							break;
						}
						if(content.equals("return")) {
							addToken("RETURN", "-", entry.getKey());
							state = 0;
							i = i-1;
							break;
						}
						if(content.equals("struct")) {
							addToken("STRUCT", "-", entry.getKey());
							state = 0;
							i = i-1;
							break;
						}
						if(content.equals("void")) {
							addToken("VOID", "-", entry.getKey());
							state = 0;
							i = i-1;
							break;
						}
						if(content.equals("while")) {
							addToken("WHILE", "-", entry.getKey());
							state = 0;
							i = i-1;
							break;
						}
						else {
							addToken("ID", content, entry.getKey());
							state = 0;
							i = i-1;
							break;
						}
					case 3: 
						if(Character.isDigit(entry.getValue().charAt(i))) {
							content += entry.getValue().charAt(i);
							break;
						}
						else if(entry.getValue().charAt(i) == '.') {
							content += entry.getValue().charAt(i);
							state = 4;
							break;
						}
						else if(entry.getValue().charAt(i) == 'e' || entry.getValue().charAt(i) == 'E') {
							content += entry.getValue().charAt(i);
							state = 6;
							break;
						}
						else {
							state = 15;
							i--; //!!!!!!!
							break;
						}
					case 4: 
						if(Character.isDigit(entry.getValue().charAt(i))) {
							content += entry.getValue().charAt(i);
							state = 5;
							break;
						}
					case 5: 
						if(Character.isDigit(entry.getValue().charAt(i))) {
							content += entry.getValue().charAt(i);
							break;
						}
						else if(entry.getValue().charAt(i) == 'e' || entry.getValue().charAt(i) == 'E') {
							content += entry.getValue().charAt(i);
							state = 6;
							break;
						}
						else {
							state = 9;
							break;
						}
					case 6: 
						if(entry.getValue().charAt(i) == '+' || entry.getValue().charAt(i) == '-') {
							content += entry.getValue().charAt(i);
							state = 7;
							break;
						}
						else {
							state = 7;
							break;
						}
					case 7: 
						if(Character.isDigit(entry.getValue().charAt(i))) {
							content += entry.getValue().charAt(i);
							state = 8;
							break;
						}
					case 8: 
						if(Character.isDigit(entry.getValue().charAt(i))) {
							content += entry.getValue().charAt(i);
							break;
						}
						else {
							state = 9;
							i--;
							break;
						}
					case 9: 
						addToken("CT_REAL", Double.parseDouble(content), entry.getKey());
						state = 0;
						i--;
						break;
					case 10: 
						if(entry.getValue().charAt(i) == 'x') {
							content += entry.getValue().charAt(i);
							state = 13;
							break;
						}
						else {
							state = 11;
							i--;
							break;
						}
						
					case 11: 
						if(Character.isDigit(entry.getValue().charAt(i)) && entry.getValue().charAt(i) != '8' && entry.getValue().charAt(i) != '9' ) {
							content += entry.getValue().charAt(i);
							break;
						}
						else if(entry.getValue().charAt(i) == '8' || entry.getValue().charAt(i) == '9') {
							content += entry.getValue().charAt(i);
							state = 12;
							break;
						}
						else if(entry.getValue().charAt(i) == '.') {
							content += entry.getValue().charAt(i);
							state = 4;
							break;
						}
						else if(entry.getValue().charAt(i) == 'e' || entry.getValue().charAt(i) == 'E') {
							content += entry.getValue().charAt(i);
							state = 6;
							break;
						}
						else {
							state = 15;
							i--;
							break;
						}
					case 12: 
						if(entry.getValue().charAt(i) == '8' || entry.getValue().charAt(i) == '9') {
							content += entry.getValue().charAt(i);
							break;
						}
						else if(entry.getValue().charAt(i) == '.') {
							content += entry.getValue().charAt(i);
							state = 4;
							break;
						}
						else if(entry.getValue().charAt(i) == 'e' || entry.getValue().charAt(i) == 'E') {
							content += entry.getValue().charAt(i);
							state = 6;
							break;
						}
					case 13: 
						if(Character.isLetterOrDigit(entry.getValue().charAt(i))){
							content += entry.getValue().charAt(i);
							state = 14;
							break;
						}
					case 14:
						if(Character.isLetterOrDigit(entry.getValue().charAt(i))){
							content += entry.getValue().charAt(i);
							break;
						}
						else {
							state = 15;
							i--;
							break;
						}
					case 15: 
						addToken("CT_INT",Integer.decode(content),entry.getKey());
						state = 0;
						i--;
						break;
					case 16: 
						if(entry.getValue().charAt(i) == '\\') {
							content += entry.getValue().charAt(i);
							state = 17;
							break;
						}
						else if(entry.getValue().charAt(i) == '"') {
							content += entry.getValue().charAt(i);
							state = 19;
							break;
						}
						else {
							content += entry.getValue().charAt(i);
							state = 18;
							break;
						}
					case 17: 
						if(entry.getValue().charAt(i) == 'a' || entry.getValue().charAt(i) == 'b' || entry.getValue().charAt(i) == 'f' || entry.getValue().charAt(i) == 'n' || entry.getValue().charAt(i) == 'r' || entry.getValue().charAt(i) == 't' || entry.getValue().charAt(i) == 'v' || entry.getValue().charAt(i) == '\'' || entry.getValue().charAt(i) == '?' || entry.getValue().charAt(i) == '\"'|| entry.getValue().charAt(i) == '\\' || entry.getValue().charAt(i) == '0') {
							content += entry.getValue().charAt(i);
							state = 18;
							break;
						}
					case 18: 
						if(entry.getValue().charAt(i) == '\"') {
							//content += entry.getValue().charAt(i); // PENTRU STRING FARA ""
							state = 19;
							break;
						}
						else if(entry.getValue().charAt(i) == '\\') {
							//content += entry.getValue().charAt(i); // PENTRU STRING FARA ""
							state = 16;
							i--;
							break;
						}
						else {
							content += entry.getValue().charAt(i);
							state = 16;
							break;
						}
					case 19: 
						content = content.replace("\\t","\t");
						content = content.replace("\\n","\n");
						content = content.replace("\\b","\b");
						content = content.replace("\\r","\r");
						content = content.replace("\\f","\f");
						content = content.replace("\\\"","\"");
						content = content.replace("\\\\","\\");
						addToken("CT_STRING",content,entry.getKey()); state = 0; i--; break;
					case 20: 
						if(entry.getValue().charAt(i) == '\\') {
							content += entry.getValue().charAt(i);
							state = 21;
							break;
						}
						else {
							content += entry.getValue().charAt(i);
							state = 22;
							break;
						}
					case 21: 
						if(entry.getValue().charAt(i) == 'a' || entry.getValue().charAt(i) == 'b' || entry.getValue().charAt(i) == 'f' || entry.getValue().charAt(i) == 'n' || entry.getValue().charAt(i) == 'r' || entry.getValue().charAt(i) == 't' || entry.getValue().charAt(i) == 'v' || entry.getValue().charAt(i) == '\'' || entry.getValue().charAt(i) == '?' || entry.getValue().charAt(i) == '"'|| entry.getValue().charAt(i) == '\\' || entry.getValue().charAt(i) == '\0') {
							content += entry.getValue().charAt(i);
							state = 22;
							break;
						}
					case 22: 
						if(entry.getValue().charAt(i) == '\'') {
							//content += entry.getValue().charAt(i);
							state = 23;
							break;
						}
					case 23: 
						switch(content) {
							case "\\a": addToken("CT_CHAR", new Character('a'), entry.getKey());state = 0; i--; break;
							case "\\b": addToken("CT_CHAR", new Character('\b'), entry.getKey());state = 0; i--; break;
							case "\\f": addToken("CT_CHAR", new Character('\f'), entry.getKey());state = 0; i--; break;
							case "\\n": addToken("CT_CHAR", new Character('\n'), entry.getKey());state = 0; i--; break;
							case "\\r": addToken("CT_CHAR", new Character('\r'), entry.getKey());state = 0; i--; break;
							case "\\t": addToken("CT_CHAR", new Character('\t'), entry.getKey());state = 0; i--; break;
							case "\\v": addToken("CT_CHAR", new Character('v'), entry.getKey());state = 0; i--; break;
							case "\\'": addToken("CT_CHAR", new Character('\''), entry.getKey());state = 0; i--; break;
							case "\\?": addToken("CT_CHAR", new Character('?'), entry.getKey());state = 0; i--; break;
							case "\\\"": addToken("CT_CHAR", new Character('\"'), entry.getKey());state = 0; i--; break;
							case "\\": addToken("CT_CHAR", new Character('\\'), entry.getKey());state = 0; i--; break;
							case "\\0": addToken("CT_CHAR", new Character('\0'), entry.getKey());state = 0; i--; break;
							default: addToken("CT_CHAR", new Character(content.charAt(0)), entry.getKey());state = 0; i--; break;
						}
						break;
					case 24: addToken("COMMA","-",entry.getKey()); state = 0; i--; break;
					case 25: addToken("SEMICOLON","-",entry.getKey()); state = 0; i--; break;
					case 26: addToken("LPAR","-",entry.getKey()); state = 0; i--; break;
					case 27: addToken("RPAR","-",entry.getKey()); state = 0; i--; break;
					case 28: addToken("LBRACKET","-",entry.getKey()); state = 0; i--; break;
					case 29: addToken("RBRACKET","-",entry.getKey()); state = 0;i--; break;
					case 30: addToken("LACC","-",entry.getKey()); state = 0; i--; break;
					case 31: addToken("RACC","-",entry.getKey()); state = 0; i--; break;
					case 32: addToken("ADD","-",entry.getKey()); state = 0; i--; break;
					case 33: addToken("SUB","-",entry.getKey()); state = 0; i--; break;
					case 34: addToken("MUL","-",entry.getKey()); state = 0; i--; break;
					case 35:
						if(entry.getValue().charAt(i) == '/') {
							contentForComment += entry.getValue().charAt(i);
							state = 54;
							break;
						}
						if(entry.getValue().charAt(i) == '*') {
							contentForComment += entry.getValue().charAt(i);
							state = 56;
							break;
						}
						else {
							state = 36;
							i--;
							break;
						}
						
					case 36: addToken("DIV","-",entry.getKey());	i--; state = 0; break;
					case 37: addToken("DOT","-",entry.getKey()); state = 0; i--; break;
					case 38: 
						if(entry.getValue().charAt(i) == '&') {
							content += entry.getValue().charAt(i);
							state = 39;
							break;
						}
					case 39: addToken("AND","-",entry.getKey()); state = 0; i--; break;
					case 40: 
						if(entry.getValue().charAt(i) == '|') {
							content += entry.getValue().charAt(i);
							state = 41;
							break;
						}
					case 41: addToken("OR","-",entry.getKey()); state = 0; i--; break;
					case 42: 
						if(entry.getValue().charAt(i) == '=') {
							content += entry.getValue().charAt(i);
							state = 47;
							break;
						}
						else {
							state = 43;
							break;
						}
					case 43: addToken("NOT","-",entry.getKey()); state = 0; i--; break;
					case 44: 
						if(entry.getValue().charAt(i) == '=') {
							content += entry.getValue().charAt(i);
							state = 46;
							break;
						}
						else {
							state = 45;
							i--;
							break;
						}
					case 45: addToken("ASSIGN","-",entry.getKey()); state = 0; i--; break;
					case 46: addToken("EQUAL","-",entry.getKey()); state = 0; i--; break;
					case 47: addToken("NOTEQ","-",entry.getKey()); state = 0; i--; break;
					case 48: 
						if(entry.getValue().charAt(i) == '=') {
							content += entry.getValue().charAt(i);
							state = 50;
							break;
						}
						else {
							state = 49;
							i--;
							break;
						}
					case 49: addToken("LESS","-",entry.getKey()); state = 0; i--; break;
					case 50: addToken("LESSEQ","-",entry.getKey()); state = 0; i--; break;
					case 51: 
						if(entry.getValue().charAt(i) == '=') {
							content += entry.getValue().charAt(i);
							state = 53;
							break;
						}
						else {
							state = 52;
							break;
						}
					case 52: addToken("GREATER","-",entry.getKey()); state = 0; i--; break;
					case 53: addToken("GREATEREQ","-",entry.getKey()); state = 0; i--; break;
					case 54: 
						if(entry.getValue().charAt(i) != '\n') {
							contentForComment += entry.getValue().charAt(i);
							break;
						}
						else {
							state = 55;
							break;
						}
					case 55: 
						//addToken("LINECOMMENT", contentForComment, entry.getKey());
						contentForComment = "";
						state = 0;
						break;
					case 56: 
						if(entry.getValue().charAt(i) != '*') {
							contentForComment += entry.getValue().charAt(i);
							break;
						}
						else {
							state = 57;
							i--;
							break;
						}
					case 57: 
						if(entry.getValue().charAt(i) == '*') {
							contentForComment += entry.getValue().charAt(i);
							break;
						}
						else if(entry.getValue().charAt(i) == '/') {
							contentForComment += entry.getValue().charAt(i);
							state = 58;
							break;
						}
						else {
							state = 56;
							i--;
							break;
						}
					case 58:
						i++;
						//addToken("COMMENT",contentForComment, entry.getKey());
						contentForComment = "";
						state = 0;
						break;
					
					default: System.err.println("Invalid char in linesMap");
					}
			}
			
		}
	}
	
}
