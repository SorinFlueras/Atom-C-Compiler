
public class Token {
    private String code;
    private Object attribute;
    int line;
    
    public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}



	public Token(String code, Object attribute, int line) {
    		this.code = code;
    		this.attribute = attribute;
    		this.line = line;
    }
    
    
    
    public String getCode() {
		return code;
    }
    
	public void setCode(String code) {
		this.code = code;
	}



	public String toString() {
    		return this.code + ":" + this.attribute + " " + this.line;
    }
}
