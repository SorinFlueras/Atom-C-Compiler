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
							err(currentToken.getLine(), " missing while statement");
						}
					}
					else {
						err(currentToken.getLine(), " missing )");
					}
				}
				else {
					err(currentToken.getLine(), " invalid expression after (");
				}
			}
			else {
				err(currentToken.getLine(), " missing ( after while");
			}
		}
		currentToken = startToken;
		return false;
	}
	
	public void err(int tokenLine, String message) {
		System.err.println(message + " at line " + tokenLine);
		System.exit(1);
	}
	
	public boolean ruleExprCast() {
		Token startToken = currentToken;
		System.out.println("rule: exprMull1 | starting token: " + startToken);
		if(consumeToken("LPAR")) {
			if(ruleTypeName()) {
				if(consumeToken("RPAR")) {
					if(ruleExprCast()) {
						return true;
					}
				}
				else {
					err(currentToken.getLine(), "missing )");
				}
			}
		}
		else if(ruleExprUnary()) {
			return true;
		}
		else {
			err(currentToken.getLine(), "missing (");
		}
		currentToken = startToken;
		return false;
	}	
	
	public boolean ruleExprMul1() {
		Token startToken = currentToken;
		System.out.println("rule: exprMull1 | starting token: " + startToken);
		if(consumeToken("MUL") || consumeToken("DIV")) {
			if(ruleExprCast()) {
				if(ruleExprMul1()) {
					return true;
				}
			}
		}
		else {
			err(currentToken.getLine(), "missing * or /");
		}
		return true;
	}
	
	public boolean ruleExprMul() {
		Token startToken = currentToken;
		System.out.println("rule: exprMull | starting token: " + startToken);
		if(ruleExprCast()) {
			if(ruleExprMul1()) {
				return true;
			}
		}
		currentToken = startToken;
		return false;
	}
	
	public boolean ruleExprAdd1() {
		Token startToken = currentToken;
		System.out.println("rule: exprRel1 | starting token: " + startToken);
		if(consumeToken("ADD") || consumeToken("SUB")) {
			if(ruleExprMul()) {
				if(ruleExprAdd1()) {
					return true;
				}
			}
		}
		else {
			err(currentToken.getLine(), "missing + or -");
		}
		return true;
	}
	
	public boolean ruleExprAdd() {
		Token startToken = currentToken;
		System.out.println("rule: exprAdd | starting token: " + startToken);
		if(ruleExprMul()) {
			if(ruleExprAdd1()) {
				return true;
			}
		}
		currentToken = startToken;
		return false;
	}
	
	public boolean ruleExprRel1() {
		Token startToken = currentToken;
		System.out.println("rule: exprRel1 | starting token: " + startToken);
		if(consumeToken("LESS") || consumeToken("LESSEQ") || consumeToken("GREATER") || consumeToken("GREATEREQ")) {
			if(ruleExprAdd()) {
				if(ruleExprRel1()) {
					return true;
				}
			}
		}
		else {
			err(currentToken.getLine(), "missing < or <= or > or >=");
		}
		return true;
	}
	
	public boolean ruleExprRel() {
		Token startToken = currentToken;
		System.out.println("rule: exprRel | starting token: " + startToken);
		if(ruleExprAdd()) {
			if(ruleExprRel1()) {
				return true;
			}
		}
		currentToken = startToken;
		return false;
	}
	
	public boolean ruleExprEq1() {
		Token startToken = currentToken;
		System.out.println("rule: exprEq1 | starting token: " + startToken);
		if(consumeToken("EQUAL") || consumeToken("NOTEQ")) {
			if(ruleExprRel()) {
				if(ruleExprEq1()) {
					return true;
				}
			}
			else {
				err(currentToken.getLine(), "missing = or !=");
			}
		}
		//currentToken = startToken;
		return true;
	}
	
	public boolean ruleExprEq() {
		Token startToken = currentToken;
		System.out.println("rule: exprEq | starting token: " + startToken);
		if(ruleExprRel()) {
			if(ruleExprEq1()) {
				return true;
			}
		}
		currentToken = startToken;
		return false;
	}
	
	public boolean ruleExprAnd1() {
		Token startToken = currentToken;
		System.out.println("rule: exprAnd1 | starting token: " + startToken);
		if(consumeToken("AND")) {
			if(ruleExprEq()) {
				if(ruleExprAnd1()) {
					return true;
				}
			} 
			else {
				err(currentToken.getLine(), "missing &&");
			}
		}
		return true;
	}
	
	public boolean ruleExprAnd() {
		Token startToken = currentToken;
		System.out.println("rule: exprAnd | starting token: " + startToken);
		if(ruleExprEq()) {
			if(ruleExprAnd1()) {
				return true;
			}
		}
		currentToken = startToken;
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
			else {
				err(currentToken.getLine(), "missing ||");
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
	
	public boolean ruleExprPrimary() {
		return false;
	}
	
	/*
	 * exprPostfix: exprPostfix LBRACKET expr RBRACKET
           | exprPostfix DOT ID 
           | exprPrimary ;
           
       exprPostfix: exprPrimary exprPostfix1
       exprPostfix1: LBRACKET expr RBRACKET exprPostfix1
       	   | DOT ID exprPostfix1
       	   | eps
  * */
	
	public boolean ruleExprPostfix1() {
		if(consumeToken("LBRACKET")) {
			if(ruleExpr()) {
				if(consumeToken("RBRACKET")) {
					if(ruleExprPostfix1()) {
						return true;
					}
				} else {
					err(currentToken.getLine(), "missing [");
				}
			}
		}
		else {
			err(currentToken.getLine(), "missing ]");
		}
		return true;
	}
	
	public boolean ruleExprPostfix() {
		Token startToken = currentToken;
		System.out.println("rule: exprUnary | starting token: " + startToken);
		if(ruleExprPrimary()) {
			if(ruleExprPostfix1()) {
				return true;
			}
		}
		currentToken = startToken;
		return false;
	}
	
	public boolean ruleExprUnary() {
		Token startToken = currentToken;
		System.out.println("rule: exprUnary | starting token: " + startToken);
		if(consumeToken("SUB") || consumeToken("NOT")) {
			if(ruleExprUnary()) {
				return true;
			}
		}
		else if (ruleExprPostfix()) {
			return true;
		}
		else {
			err(currentToken.getLine(), "missing - or !");
		}
		currentToken = startToken;
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
					err(currentToken.getLine(), " exprAssign error");
					
				}
			}
		}
		//SAU exprOr
		currentToken = startToken;
		if(ruleExprOr()) {
			return true;
		}
		else {
			err(currentToken.getLine(), " exprOr or exprUnary error");
		}
		
		return false;
	}
	
	public boolean ruleExpr() {
		Token startToken = currentToken;
		System.out.println("rule: expr | starting token: " + startToken);
		if(ruleExprAssign()) {
			return true;
		}
		else {
			err(currentToken.getLine(), "exprAssign error");
		}
		currentToken = startToken;
		return false;
	}
	
	public boolean ruleArrayDecl() {
		Token startToken = currentToken;
		System.out.println("rule: arrayDecl | starting token: " + startToken);
		if(consumeToken("LBRACKET")) {
			if(ruleExpr()) {
				if(consumeToken("RBRACKET")) {
					return true;
				}
			}
			// INTREBAREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE!!!!!!!!!!!!!!!!!!!!!!!!!
			else {
				err(currentToken.getLine(), "invalid expression after [");
			}
			if(consumeToken("RBRACKET")) {
				return true;
			}
			else { 
				err(currentToken.getLine(), " missing ]");
			}
		}
		else {
			err(currentToken.getLine(), " missing [");
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
				err(currentToken.getLine(), " invalid ID");
			}
		}
		else {
			err(currentToken.getLine(), " invalid type before ID");
		}
		currentToken = startToken;
		return false;
	}
	
	public boolean ruleDeclVar() {
		Token startToken = currentToken;
		System.out.println("rule: declVar | starting token: " + startToken);
		if(ruleTypeBase()) {
			if(consumeToken("ID")) {
				if(ruleArrayDecl()) {
					
				}
				for(;;) {
					if(consumeToken("COMMA")) {
						if(consumeToken("ID")) {
							if(ruleArrayDecl()) {
							
							}		
						}
					}
					else {
						break;
					}
				}
				if(consumeToken("SEMICOLON")) {
					return true;
				}
				else {
					err(currentToken.getLine(), "missing ; or ,");
				}
			}
		}
		currentToken = startToken;
		return false;
	}
	
	// 0 or more implementation
	public boolean ruleDeclStruct() {
		Token startToken = currentToken;
		System.out.println("rule: declStruct | starting token: " + startToken);
		if(consumeToken("STRUCT")) {
			if(consumeToken("ID")) {
				if(consumeToken("LACC")) {
					for(;;) {
					if(ruleDeclVar()) {}
					else break;
					}
					if(consumeToken("RACC")){
						if(consumeToken("SEMICOLON")) {
							return true;
						}
						else {
							err(currentToken.getLine(), "missing ;");
						}
					}
					else {
						err(currentToken.getLine(), "missing }");
					}
				}
			}
		}
		else {
			err(currentToken.getLine(), "missing ID");
		}
		currentToken = startToken;
		return false;
	}
	
	public boolean ruleFuncArg() {
		return false;
	}
	
	public boolean ruleStmCompound() {
		return false;
	}
	
	public boolean ruleDeclFunc() {
		Token startToken = currentToken;
		System.out.println("rule: declFunc | starting token: " + startToken);
		if(ruleTypeBase()) {
			if(consumeToken("MUL")) {
				
			}
		}
		else if(consumeToken("VOID")) {
			
		}
		else {
			currentToken = startToken;
			return false;
		}
		if(consumeToken("ID")) {
			if(consumeToken("LPAR")) {
				if(ruleFuncArg()) {
					for(;;) {
						if(consumeToken("COMMA")) {
							if(ruleFuncArg()) {
								
							}
						}
						else {
							break;
						}
					}
				}
				if(consumeToken("RPAR")) {
					if(ruleStmCompound()) {
						return true;
					}
					else {
						err(currentToken.getLine(), "missing function body");
					}
				}
				else {
					err(currentToken.getLine(), "missing )");
				}
			}
		}
		currentToken = startToken;
		return false;
	}
	
	public boolean exprPrimary() {
		Token startToken = currentToken;
		System.out.println("rule: exprPrimary | starting token: " + startToken);
		if(consumeToken("ID")) {
			if(consumeToken("LPAR")) {
				if(ruleExpr()) {
					for(;;) {
						if(consumeToken("COMMA")) {
							if(ruleExpr()) {
								
							}
						}
						else {
							break;
						}
					}
				}
				if(consumeToken("RPAR")) {
					
				}
			}
			if(consumeToken("CT_INT") || consumeToken("CT_REAL") || consumeToken("CT_CHAR") || consumeToken("CT_STRING")) {
				return true;
			}
			if(consumeToken("LPAR")) {
				if(ruleExpr()) {
					if(consumeToken("RPAR")) {
						return true;
					}
				}
			}
		}
		currentToken = startToken;
		return false;
	}
	
	public boolean ruleUnit() {
		Token startToken = currentToken;
		System.out.println("rule: unit | starting token: " + startToken);
		for(;;) {
			if(ruleDeclStruct() || ruleDeclFunc() || ruleDeclVar()) {
				
			}
			else {
				break;
			}
		}
		if(consumeToken("END")) {
			return true;
		}
		currentToken = startToken;
		return false;
	}

}
