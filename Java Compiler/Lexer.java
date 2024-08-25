import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Lexer {
    private String input;
    private int pos;
    private int length;
    List<String> errors;
    List<String> warnings;
    private int line = 1;

    public Lexer(String input) {
        this.input = input;
        this.length = input.length();
        this.pos = 0;
        this.errors = new ArrayList<>();
        this.warnings = new ArrayList<>();
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (pos < length) {
            char current = input.charAt(pos);

            if (Character.isWhitespace(current)) {
                if (current == '\n') {
                    line++;
                }
                pos++;
                continue;
            }

            // single-line comments
            if (current == '/' && pos + 1 < length && input.charAt(pos + 1) == '/') {
                pos += 2;
                while (pos < length && input.charAt(pos) != '\n') {
                    pos++;
                }
                continue;
            }

            // multi-line comments
            if (current == '/' && pos + 1 < length && input.charAt(pos + 1) == '*') {
                pos += 2;
                while (pos < length) {
                    if (input.charAt(pos) == '*' && pos + 1 < length && input.charAt(pos + 1) == '/') {
                        pos += 2;
                        break;
                    }
                    if (input.charAt(pos) == '\n') {
                        line++;
                    }
                    pos++;
                }
                continue;
            }
            
            if (Character.isLetter(current)) {
                String identifier = readIdentifier();
                tokens.addAll(processIdentifier(identifier));
                continue;
            }

            if (Character.isDigit(current)) {
                tokens.add(new Token(TokenType.DIGIT, String.valueOf(current), line));
                pos++;
                continue;
            }

            if (current == '"') {
                String string = readString();
                tokens.add(new Token(TokenType.CHAR, string, line));
                continue;
            }

            if (current == '=' || current == '!') {
                if (pos + 1 < length) {
                    char next = input.charAt(pos + 1);
                    if (current == '=' && next == '=') {
                        // Handle '=='
                        tokens.add(new Token(TokenType.BOOLEAN_OP, "==", line));
                        pos += 2;
                        continue;
                    }
                    if (current == '!' && next == '=') {
                        // Handle '!='
                        tokens.add(new Token(TokenType.BOOLEAN_OP, "!=", line));
                        pos += 2;
                        continue;
                    }
                }
                tokens.add(new Token(TokenType.ASSIGN, String.valueOf(current), line));
                pos++;
                continue;
            }

            TokenType operatorType = getOperatorType(current);
            if (operatorType != TokenType.UNKNOWN) {
                tokens.add(new Token(operatorType, String.valueOf(current), line));
                pos++;
                continue;
            }

            if (current != '$') {
                tokens.add(new Token(TokenType.UNKNOWN, String.valueOf(current), line));
                errors.add("Unknown token '" + current + "' at line " + line);
                break;
            } 
            else {
                tokens.add(new Token(TokenType.EOF, "$", line));
                pos++;
            }

            tokens.add(new Token(TokenType.UNKNOWN, String.valueOf(current), line));
            pos++;
        }

        if (tokens.isEmpty() || tokens.get(tokens.size() - 1).getType() != TokenType.EOF) {
            warnings.add("Warning: Missing '$' at the end of the code. Automatically adding '$'.");
            tokens.add(new Token(TokenType.EOF, "$", line));
        }

        return tokens;
    }

    private List<Token> processIdentifier(String identifier) {
        List<Token> tokens = new ArrayList<>();
        int start = 0;

        while (start < identifier.length()) {
            boolean matchedKeyword = false;

            for (String keyword : getKeywords()) {
                if (identifier.startsWith(keyword, start)) {
                    tokens.add(new Token(getKeywordOrIdentifierType(keyword), keyword, line));
                    start += keyword.length();
                    matchedKeyword = true;
                    break;
                }
            }

            if (!matchedKeyword) {
                tokens.add(new Token(TokenType.ID, String.valueOf(identifier.charAt(start)), line));
                start++;
            }
        }

        return tokens;
    }

    /*private boolean isKeywordStart(String part) {
        for (String keyword : getKeywords()) {
            if (keyword.startsWith(part)) {
                return true;
            }
        }
        return false;
    }*/

    private List<String> getKeywords() {
        List<String> keywords = new ArrayList<>();
        for (TokenType type : TokenType.values()) {
            if (type != TokenType.ID && type != TokenType.UNKNOWN) {
                // keywords.add(type.name().toLowerCase());
                keywords.add("print");
                keywords.add("if");
                keywords.add("else");
                keywords.add("while");
                keywords.add("int");
                keywords.add("for");
                keywords.add("boolean");
                keywords.add("string");
                keywords.add("true");
                keywords.add("false");
            }
        }
        return keywords;
    }

    private String readIdentifier() {
        int start = pos;
        while (pos < length && (Character.isLetterOrDigit(input.charAt(pos)) || input.charAt(pos) == '_')) {
            pos++;
        }
        return input.substring(start, pos);
    }

    /*private String readNumber() {
        int start = pos;
        while (pos < length && Character.isDigit(input.charAt(pos))) {
            pos++;
        }
        return input.substring(start, pos);
    }*/

    private String readString() {
        pos++; // Skip the opening quote
        int start = pos;
        boolean hasNumber = false;
        StringBuilder result = new StringBuilder();
    
        while (pos < length && input.charAt(pos) != '"') {
            char current = input.charAt(pos);
    
            if (current == '\n') {
                errors.add("Line break found in a string at line " + line + ", position " + start);
                break; 
            }

            // digits
            if (Character.isDigit(current)) {
                hasNumber = true;
            }
    
            // single-line comments
            if (current == '/' && pos + 1 < length && input.charAt(pos + 1) == '/') {
                pos += 2;
                while (pos < length && input.charAt(pos) != '\n') {
                    pos++;
                }
                continue; 
            }
    
            // multi-line comments
            if (current == '/' && pos + 1 < length && input.charAt(pos + 1) == '*') {
                pos += 2;
                while (pos < length) {
                    if (input.charAt(pos) == '*' && pos + 1 < length && input.charAt(pos + 1) == '/') {
                        pos += 2;
                        break;
                    }
                    if (input.charAt(pos) == '\n') {
                        line++;
                    }
                    pos++;
                }
                continue;
            }
    
            result.append(current);
            pos++;
        }
    
        if (hasNumber) {
            errors.add("Numbers are not allowed in a string at line " + line + ", position " + start);
        }
    
        pos++; 
        return result.toString();
    }    

    private TokenType getKeywordOrIdentifierType(String identifier) {
        switch (identifier) {
            case "print":
                return TokenType.PRINT;
            case "if":
                return TokenType.IF;
            case "else":
                return TokenType.ELSE;
            case "while":
                return TokenType.WHILE;
            case "int":
                return TokenType.INT;
            case "for":
                return TokenType.FOR;
            case "boolean":
                return TokenType.BOOLEAN;
            case "string":
                return TokenType.STRING;
            case "true":
                return TokenType.BOOLEAN_VAL;
            case "false":
                return TokenType.BOOLEAN_VAL;
            default:
                return TokenType.ID;
        }
    }

    private TokenType getOperatorType(char current) {
        switch (current) {
            case '+':
                return TokenType.INT_OP;
            case '=':
                return TokenType.ASSIGN;
            case '(':
                return TokenType.LEFT_PARENTHESIS;
            case ')':
                return TokenType.RIGHT_PARENTHESIS;
            case '{':
                return TokenType.LEFT_BRACE;
            case '}':
                return TokenType.RIGHT_BRACE;
            case '$':
                return TokenType.EOF;
            default:
                return TokenType.UNKNOWN;
        }
    }

    public List<String> getErrors() {
        return errors;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    /*public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter code to lex:");
        // String input = scanner.nextLine();

        StringBuilder input = new StringBuilder();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals(""))
                break; // stop reading when an empty line is encountered
            input.append(line).append("\n");
        }

        Lexer lexer = new Lexer(input.toString());
        List<Token> tokens = lexer.tokenize();

        int programCount = 1;
        System.out.println("Program " + programCount + ":");

        for (Token token : tokens) {
            if (token.getType() == TokenType.EOF) {
                programCount++;
            }
            System.out.println(token);
        }

        if (!lexer.errors.isEmpty()) {
            System.out.println("Errors:");
            for (String error : lexer.errors) {
                System.out.println(error);
            }
        }
        else if (tokens.isEmpty() || tokens.get(tokens.size() - 1).getType() != TokenType.EOF) {
            lexer.errors.add("Error: Missing '$' at the end of the code.");
            //System.out.println("Error: Missing '$' at the end of the code.");
        }

        scanner.close();
    }*/
}