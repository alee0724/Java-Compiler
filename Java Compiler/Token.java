public class Token {
    private TokenType type;
    private String value;
    private int line;

    public Token(TokenType type, String value, int line) {
        this.type = type;
        this.value = value;
        this.line = line;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public int getLine() {  // Implement the getLine() method
        return line;
    }

    @Override
    public String toString() {
        return "Token [type=" + type + ", value=" + value + ", line=" + line + "]";
    }

    public String getLexeme() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLexeme'");
    }
}