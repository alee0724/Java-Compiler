import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Parser {
    private List<Token> tokens;
    private int pos;
    private int length;
    String error;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.length = tokens.size();
        this.pos = 0;
        //this.errors = new ArrayList<>();
        this.error = null;
    }

    public void parse() {
        System.out.println("Parsing program...");
        parseProgram();

        if (error == null && !isAtEnd()) {
            consume(TokenType.EOF, "Expected '$' to end");
        }
        if (error != null) {
            System.out.println("Error:");
            System.out.println(error);
        } else {
            System.out.println("Parsing finished.");
        }
    }

    private void parseProgram() {
        System.out.println("parseProgram()");
        parseBlock();
    }

    private void parseBlock() {
        System.out.println("parseBlock()");
        consume(TokenType.LEFT_BRACE, "Expected statement to start with left brace");
        parseStatementList();
        consume(TokenType.RIGHT_BRACE, "Expected statement to end at right brace");
    }

    private void parseStatementList() {
        System.out.println("parseStatementList()");
        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            if (check(TokenType.LEFT_BRACE) || check(TokenType.PRINT) || check(TokenType.WHILE) || check(TokenType.IF) || 
                check(TokenType.ID) || check(TokenType.INT) || check(TokenType.STRING) || check(TokenType.BOOLEAN)) {
                parseStatement();
                parseStatementList();
            } 
            else {
                error("Cannot start statement with");
                break;
            }
        }
    }

    private void parseStatement() {
        System.out.println("parseStatement()");
        if (check(TokenType.INT)) {
            parseVarDecl();
        } 
        else if (check(TokenType.STRING)) {
            parseVarDecl();
        } 
        else if (check(TokenType.BOOLEAN)) {
            parseVarDecl();
        } 
        else if (match(TokenType.ID)) {
            parseAssignmentStatement();
        } 
        else if (match(TokenType.PRINT)) {
            parsePrintStatement();
        } 
        else if (match(TokenType.IF)) {
            parseIfStatement();
        } 
        else if (match(TokenType.WHILE)) {
            parseWhileStatement();
        } 
        else if (check(TokenType.LEFT_BRACE)) {
            parseBlock();
        }
        else {
            error("Expected a statement");
        }
    }

    private void parseVarDecl() {
        System.out.println("parseVarDecl()");
        parseType();
        consume(TokenType.ID, "Expected identifier after type");
    }

    private void parseAssignmentStatement() {
        System.out.println("parseAssignmentStatement()");
        consume(TokenType.ASSIGN, "Expected '=' after identifier");
        parseExpr();
    }

    private void parseType() {
        System.out.println("parseType()");
        if (!match(TokenType.INT, TokenType.BOOLEAN, TokenType.STRING)) {
            error("Expected type");
        }
    }

    private void parseExpr() {
        System.out.println("parseExpr()");
        if (check(TokenType.ID)) {
            advance();
        } 
        else if (check(TokenType.DIGIT)) {
            parseIntExpr();
        } 
        else if (check(TokenType.LEFT_PARENTHESIS)) {
            parseBooleanExpr();
        } 
        else if (check(TokenType.CHAR)) {
            parseStringExpr();
        } 
        else if (check(TokenType.BOOLEAN_VAL)) {
            parseBooleanExpr();
        } 
        else {
            error("Expected expression");
            return;
        }
    }

    private void parseBooleanExpr() {
        System.out.println("parseBooleanExpr()");
        if (match(TokenType.BOOLEAN_VAL)) {
            return;
        } 
        else if (match(TokenType.LEFT_PARENTHESIS)) {
            parseExpr();
            parseBoolOp();
            parseExpr();
            consume(TokenType.RIGHT_PARENTHESIS, "Expected ')' after boolean expression");
            return;
        }
        error("Expected boolean expression");
    }

    private void parseBoolOp() {
        System.out.println("parseBooleanOp()");
        if (!match(TokenType.BOOLEAN_OP)) {
            error("Expected boolean operator");
        }
    }

    private void parseIntOp() {
        System.out.println("parseIntOp()");
        if (!match(TokenType.INT_OP)) {
            error("Expected integer operator");
        }
    }

    private void parseIfStatement() {
        System.out.println("parseIfStatement()");
        parseBooleanExpr();
        parseBlock();
    }

    private void parseWhileStatement() {
        System.out.println("parseWhileStatement()");
        parseBooleanExpr();
        parseBlock();
    }

    private void parseIntExpr() {
        System.out.println("parseIntExpr()");
        if (match(TokenType.DIGIT)) {
            if (check(TokenType.INT_OP)) {
                parseIntOp();
                parseExpr();
            }
        } 
        else {
            error("Expected integer expression");
        }
    }

    private void parseStringExpr() {
        System.out.println("parseStringExpr()");
        if (!match(TokenType.CHAR)) {
            error("Expected string expression");
        }
    }

    private void parsePrintStatement() {
        System.out.println("parsePrintStatement");
        consume(TokenType.LEFT_PARENTHESIS, "Expected '(' after 'print'");
        parseExpr();
        consume(TokenType.RIGHT_PARENTHESIS, "Expected ')' after expression");
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type))
            return advance();
        error(message);
        return null;
    }

    private void error(String message) {
        if (error == null) {
            error = message + " at " + (pos < length ? tokens.get(pos) : "EOF");
        }
    }

    private Token advance() {
        if (!isAtEnd()) {
            pos++;
        }
        return previous();
    }

    private boolean check(TokenType type) {
        if (isAtEnd())
            return false;
        return tokens.get(pos).getType() == type;
    }

    private Token previous() {
        return tokens.get(pos - 1);
    }

    private boolean isAtEnd() {
        return pos >= length || tokens.get(pos).getType() == TokenType.EOF;
    }

    /*public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your code:");
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
        for (Token token : tokens) {
            System.out.println(token);
        }

        Parser parser = new Parser(tokens);
        parser.parse();

        scanner.close();
    }*/
}