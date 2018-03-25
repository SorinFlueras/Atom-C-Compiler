
public class Token {
    private String code;
    private Object attribute;
    int line;
    
    public Token(String code, Object attribute, int line) {
    		this.code = code;
    		this.attribute = attribute;
    		this.line = line;
    }
    
    public String toString() {
    		return this.code + ":" + this.attribute + " " + this.line;
    }
}
