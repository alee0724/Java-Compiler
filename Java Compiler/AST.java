import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Scanner;

public class AST {
    private List<Token> tokens;
    private int position = 0;
    private List<String> errors = new ArrayList<>();
    private SymbolTable symbolTable = new SymbolTable();  // Initialize the SymbolTable
    private int currentScope = 0;

    public AST(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token currentToken() {
        return position < tokens.size() ? tokens.get(position) : null;
    }

    private Token nextToken() {
        return position < tokens.size() ? tokens.get(position++) : null;
    }

    private void expectToken(TokenType expectedType) {
        Token token = nextToken();
        if (token == null || token.getType() != expectedType) {
            error("Expected '" + expectedType + "' but got '" + (token != null ? token.getType() : "EOF") + "'");
        }
    }

    private void error(String message) {
        errors.add(message + " at token index " + position);
    }

    public ProgramASTNode Program() {
        ProgramASTNode programASTNode = new ProgramASTNode();
        programASTNode.addChild(Block());
        programASTNode.addChild(new EOFASTNode());
        return programASTNode;
    }

    private BlockASTNode Block() {
        BlockASTNode blockASTNode = new BlockASTNode();
        expectToken(TokenType.LEFT_BRACE);
        blockASTNode.addChild(new LeftBraceASTNode());
        currentScope++;  // Increase scope level
        StatementList(blockASTNode);
        currentScope--;  // Decrease scope level
        expectToken(TokenType.RIGHT_BRACE);
        blockASTNode.addChild(new RightBraceASTNode());
        return blockASTNode;
    }

    private void StatementList(BlockASTNode blockASTNode) {
        while (currentToken() != null && currentToken().getType() != TokenType.RIGHT_BRACE) {
            ASTNode statementASTNode = Statement();
            //if (statementASTNode != null) {
            blockASTNode.addChild(statementASTNode);
            //}
        }
    }

    private ASTNode Statement() {
        Token token = currentToken();

        if (token.getType() == TokenType.INT) {
            return VariableDeclaration();
        } 
        else if (token.getType() == TokenType.STRING) {
            return VariableDeclaration();
        }
        else if (token.getType() == TokenType.BOOLEAN) {
            return VariableDeclaration();
        }
        else if (token.getType() == TokenType.ID) {
            return AssignmentStatement();
        } 
        else if (token.getType() == TokenType.IF) {
            return IfStatement();
        } 
        else if (token.getType() == TokenType.PRINT) {
            return PrintStatement();
        } 
        else if (token.getType() == TokenType.WHILE) {
            return WhileStatement();
        } 
        else if (token.getType() == TokenType.LEFT_BRACE) {
            return Block();
        } 
        else {
            error("Unexpected token: " + token.getType());
        }
        return null;
    }

    private VariableDeclarationASTNode VariableDeclaration() {
        VariableDeclarationASTNode varDeclASTNode = new VariableDeclarationASTNode();
        Token token = currentToken();
        if (token != null && (token.getType() == TokenType.INT || token.getType() == TokenType.STRING || token.getType() == TokenType.BOOLEAN)) {
            /*varDeclASTNode.addChild(new VariableTypeASTNode(token.getValue())); // Variable type (int, string, boolean)
            nextToken(); // Consume the type token
            varDeclASTNode.addChild(new IdentifierASTNode(nextToken().getValue())); // Variable name*/
            String type = token.getValue(); // Variable type (int, string, boolean)
            nextToken(); // Consume the type token
            Token idToken = nextToken(); // Get the identifier token
            String name = idToken.getValue(); // Variable name
            varDeclASTNode.addChild(new VariableTypeASTNode(type));
            varDeclASTNode.addChild(new IdentifierASTNode(name));
            symbolTable.addEntry(name, type, currentScope, idToken.getLine()); // Add entry to symbol table
        } 
        else {
            error("Expected variable type but got '" + (token != null ? token.getValue() : "EOF") + "'");
        }
        
        return varDeclASTNode;
    }    

    private AssignmentStatementASTNode AssignmentStatement() {
        AssignmentStatementASTNode assignStmtASTNode = new AssignmentStatementASTNode();
        assignStmtASTNode.addChild(new IdentifierASTNode(nextToken().getValue())); // Variable name
        expectToken(TokenType.ASSIGN);
        assignStmtASTNode.addChild(Expression());
        return assignStmtASTNode;
    }

    private IfStatementASTNode IfStatement() {
        expectToken(TokenType.IF);
        IfStatementASTNode ifStmtASTNode = new IfStatementASTNode();
        ifStmtASTNode.addChild(BooleanExpression());
        ifStmtASTNode.addChild(Block());
        return ifStmtASTNode;
    }

    private PrintStatementASTNode PrintStatement() {
        expectToken(TokenType.PRINT);
        PrintStatementASTNode printStmtASTNode = new PrintStatementASTNode();
        expectToken(TokenType.LEFT_PARENTHESIS);
        printStmtASTNode.addChild(Expression());
        expectToken(TokenType.RIGHT_PARENTHESIS);
        return printStmtASTNode;
    }

    private WhileStatementASTNode WhileStatement() {
        WhileStatementASTNode whileStmtASTNode = new WhileStatementASTNode();
        expectToken(TokenType.WHILE);
        whileStmtASTNode.addChild(BooleanExpression());
        whileStmtASTNode.addChild(Block());
        return whileStmtASTNode;
    }

    private ASTNode Expression() {
        Token token = currentToken();
    
        if (token.getType() == TokenType.ID) {
            expectToken(TokenType.ID);
            return new IdentifierASTNode(token.getValue());
        }
        else if (token.getType() == TokenType.DIGIT) {
            return IntegerExpression();
        }
        else if (token.getType() == TokenType.LEFT_PARENTHESIS) {
            return BooleanExpression();
        }
        else if (token.getType() == TokenType.CHAR) {
            return StringExpression();
        }
        else if (token.getType() == TokenType.BOOLEAN_VAL) {
            expectToken(TokenType.BOOLEAN_VAL);
            return new BooleanValueASTNode(token.getValue());
        }
        else {
            error("Unexpected token: " + token.getType());
            //return exprNode; // Return the expression node, possibly empty, on error
        }
    
        return null;
    }

    private BooleanExpressionASTNode BooleanExpression() {
        Token token = currentToken();
        BooleanExpressionASTNode boolExprASTNode = new BooleanExpressionASTNode();
        if (token.getType() == TokenType.BOOLEAN_VAL) {
            boolExprASTNode.addChild(new BooleanValueASTNode(nextToken().getValue()));
            //return boolExprNode;
        }
        else if (token.getType() == TokenType.LEFT_PARENTHESIS) {
            expectToken(TokenType.LEFT_PARENTHESIS);
            boolExprASTNode.addChild(new LeftParenthesisASTNode());
            boolExprASTNode.addChild(Expression());
            boolExprASTNode.addChild(BoolOpExpression());
            expectToken(TokenType.BOOLEAN_OP);
            boolExprASTNode.addChild(Expression());
            expectToken(TokenType.RIGHT_PARENTHESIS);
            boolExprASTNode.addChild(new RightParenthesisASTNode());
        }
        return boolExprASTNode;
    }

    private IntegerExpressionASTNode IntegerExpression() {
        Token token = currentToken();
        IntegerExpressionASTNode integerExpressionASTNode = new IntegerExpressionASTNode();
    
        if (token.getType() == TokenType.DIGIT) {
            integerExpressionASTNode.addChild(new IntegerLiteralASTNode(token.getValue())); // Add the digit as a literal node
            nextToken(); // Move to the next token
            token = currentToken(); // Get the updated current token
            if (token != null && token.getType() == TokenType.INT_OP) {
                integerExpressionASTNode.addChild(IntOp()); // Add the operator node
                expectToken(TokenType.INT_OP);
                integerExpressionASTNode.addChild(Expression()); // Recursively parse the next integer expression
            }
        } 
        else {
            error("Expected integer expression but got '" + token.getValue() + "'");
        }
    
        return integerExpressionASTNode;
    }

    private IntOpASTNode IntOp() {
        IntOpASTNode intOpASTNode = new IntOpASTNode();
        return intOpASTNode;
    }

    private StringExpressionASTNode StringExpression() {
        Token token = currentToken();
        StringExpressionASTNode stringExpressionASTNode = new StringExpressionASTNode();
        if (token.getType() == TokenType.CHAR) {
            stringExpressionASTNode.addChild(new StringASTNode(token.getValue()));
            expectToken(TokenType.CHAR);
        }
        else {
            error("Expected string expression");
        }
        return stringExpressionASTNode;
    }

    private BooleanOperatorASTNode BoolOpExpression() {
        Token token = currentToken();
        BooleanOperatorASTNode boolOpASTNode = new BooleanOperatorASTNode(token.getValue());
        return boolOpASTNode;
    }

    public List<String> getErrors() {
        return errors;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your code:");
        StringBuilder input = new StringBuilder();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals(""))
                break;
            input.append(line).append("\n");
        }

        Lexer lexer = new Lexer(input.toString());
        List<Token> tokens = lexer.tokenize();
        for (Token token : tokens) {
            System.out.println(token);
        }

        Parser parser = new Parser(tokens);
        parser.parse();

        AST ast = new AST(tokens); // Pass tokens here
        ProgramASTNode program = ast.Program();
        program.print("");

        if (!ast.getErrors().isEmpty()) {
            System.out.println("Errors:");
            for (String error : ast.getErrors()) {
                System.out.println(error);
            }
        }

        ast.getSymbolTable().printSymbolTable();

        scanner.close();
    }
}