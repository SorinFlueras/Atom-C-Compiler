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
	
	public void addToken(String code, String attribute, int line) {
		Token tk = new Token(code,attribute,line);
		tokens.add(tk);
	}
	
	public void idToken(String buffer) {
		int state = 0;
		for(int i = 0; i < buffer.length(); i++) {
			
		}
	}
	
	public void getNextToken() {
		int j;
		for(Entry<Integer, String> entry : linesMap.entrySet()) {
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			/*for(int i = 0; i < entry.getValue().length(); i++) {
				switch(entry.getValue().charAt(i)) {
					case ',': addToken("COMMA","-",entry.getKey()); break;
					case '{': addToken("LACC","-",entry.getKey()); break;
					case '}': addToken("RACC","-",entry.getKey()); break;
					case ' ': addToken("SPACE","-",entry.getKey()); break;
					default: System.err.println("Invalid char in linesMap");
					}
			}*/
			
		}
	}
	
}
