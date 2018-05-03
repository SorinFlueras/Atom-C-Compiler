import java.util.ArrayList;

public class SyntacticAnalyzer {
	private ArrayList<Token> tokens;
	private int tokensIndex = 0;
	private Token currentToken;
	
	public SyntacticAnalyzer(ArrayList<Token> tokens) {
		this.tokens = tokens;
	}
	
	public boolean consumeToken(String TokenCode) {
		//display the given token code in contrast with the found one
		System.out.println("Given code: " + TokenCode + " | Found code: " + tokens.get(tokensIndex).getCode());
		//if the code that we look for is equal to the one that we provided
		if(tokens.get(tokensIndex).getCode().equals(TokenCode)) {
			//copy it
			currentToken = tokens.get(tokensIndex);
			//move to the next Token from the ArrayList of Tokens
			tokensIndex++;
			//and return true
			return true;
		}
		//otherwise return false
		return false;
	}
	
	public boolean stm() {
		return true;
	}
	
	public boolean ruleWhile() {
		Token startToken = currentToken;
		if(consumeToken("WHILE")) {
			if(consumeToken("LPAR")) {
				if(ruleExpr()) {
					if(consumeToken("RPAR")) {
						if(stm()) {
							return true;
						}
						else {
							System.err.println(currentToken + " missing while statement");
						}
					}
					else {
						System.err.println(currentToken + " missing )");
					}
				}
				else {
					System.err.println(currentToken + " invalid expression after (");
				}
			}
			else {
				System.err.println(currentToken + " missing ( after while");
			}
		}
		currentToken = startToken;
		return false;
	}
	
	public boolean ruleExprAnd() {
		return false;
	}
	
	public boolean ruleExprOr1() {
		Token startToken = currentToken;
		System.out.println("rule: exprOr1 | starting token: " + startToken);
		if(consumeToken("OR")) {
			if(ruleExprAnd()) {
				if(ruleExprOr1()) {
					return true;
				}
			}
		}
		//currentToken = startToken;
		return true;
	}
	
	public boolean ruleExprOr() {
		Token startToken = currentToken;
		System.out.println("rule: exprOr | starting token: " + startToken);
		if(ruleExprAnd()) {
			if(ruleExprOr1()) {
				return true;
			}
		}
		currentToken = startToken;
		return false;
	}
	
	public boolean ruleExprUnary() {
		return false;
	}
	
	public boolean ruleExprAssign() {
		Token startToken = currentToken;
		System.out.println("rule: exprAssign | starting token: " + startToken);
		//exprUnary ASSIGN exprAssign
		if(ruleExprUnary()) {
			if(consumeToken("ASSIGN")) {
				if(ruleExprAssign()) {
					return true;
				}
				else {
					System.err.println(currentToken + " exprAssign error");
				}
			}
			System.err.println(currentToken + " missing =");
		}
		//SAU exprOr
		else if(ruleExprOr()) {
			return true;
		}
		else {
			System.err.println(currentToken + " exprOr or exprUnary error");
		}
		currentToken = startToken;
		return false;
	}
	
	public boolean ruleExpr() {
		Token startToken = currentToken;
		System.out.println("rule: expr | starting token: " + startToken);
		if(ruleExprAssign()) {
			return true;
		}
		else {
			System.err.println(currentToken + "exprAssign error");
		}
		currentToken = startToken;
		return false;
	}
	
	public boolean ruleArrayDecl() {
		Token startToken = currentToken;
		System.out.println("rule: arrayDecl | starting token: " + startToken);
		if(consumeToken("LBRACKET")) {
			if(ruleExpr()) {
				//no idea what to do here
			}
			else {
				System.err.println(currentToken + "invalid expression after [");
			}
			if(consumeToken("RBRACKET")) {
				return true;
			}
			else { 
				System.err.println(currentToken + " missing ]");
			}
		}
		else {
			System.err.println(currentToken + " missing [");
		}
		currentToken = startToken;
		return false;
	}
	
	public boolean ruleTypeName() {
		Token startToken = currentToken;
		System.out.println("rule: typeName | starting token: " + startToken);
		if(ruleTypeBase()) {
			if(ruleArrayDecl()) {
				return true;
			}
			return true;
		}
		currentToken = startToken;
		return false;
	}
	
	public boolean ruleTypeBase() {
		Token startToken = currentToken;
		System.out.println("rule: typeBase | starting token: " + startToken);
		if(consumeToken("INT") || consumeToken("DOUBLE") ||consumeToken("CHAR") || consumeToken("STRUCT")) {
			if(consumeToken("ID")) {
				return true;
			}
			else {
				System.err.println(currentToken + " invalid ID");
			}
		}
		else {
			System.err.println(currentToken + " invalid type before ID");
		}
		currentToken = startToken;
		return false;
	}
	
}
