
public class Token {
    private String code;
    private String attribute;
    int line;
    
    public Token(String code, String attribute, int line) {
    		this.code = code;
    		this.attribute = attribute;
    		this.line = line;
    }
    
    public String toString() {
    		return this.code + ":" + this.attribute + " at line:" + this.line;
    }
}
