import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Scanner;

abstract class Node {
    String name;
    List<Node> children = new ArrayList<>();

    Node(String name) {
        this.name = name;
    }

    void addChild(Node node) {
        children.add(node);
    }

    void print(String indent) {
        System.out.println(indent + name);
        for (Node child : children) {
            child.print(indent + "  ");
        }
    }
}

class ProgramNode extends Node {
    ProgramNode() {
        super("Program");
    }
}

class EOFNode extends Node {
    EOFNode() {
        super("$");
    }
}

class BlockNode extends Node {
    BlockNode() {
        super("Block");
    }
}

class LeftBraceNode extends Node {
    LeftBraceNode() {
        super("{");
    }
}

class RightBraceNode extends Node {
    RightBraceNode() {
        super("}");
    }
}

class LeftParenthesisNode extends Node {
    LeftParenthesisNode() {
        super("(");
    }
}

class RightParenthesisNode extends Node {
    RightParenthesisNode() {
        super(")");
    }
}

class StatementListNode extends Node {
    StatementListNode() {
        super("StatementList");
    }
}

class StatementNode extends Node {
    StatementNode() {
        super("Statement");
    }
}

class VariableDeclarationNode extends Node {
    VariableDeclarationNode() {
        super("VarDeclaration");
    }
}

class VariableTypeNode extends Node {
    VariableTypeNode(String value) {
        super("Type: " + value);
    }
}

class AssignmentStatementNode extends Node {
    AssignmentStatementNode() {
        super("AssignStatement");
    }
}

class IfStatementNode extends Node {
    IfStatementNode() {
        super("IfStatement");
    }
}

class PrintStatementNode extends Node {
    PrintStatementNode() {
        super("PrintStatement");
    }
}

class WhileStatementNode extends Node {
    WhileStatementNode() {
        super("WhileStatement");
    }
}

class ExpressionNode extends Node {
    ExpressionNode() {
        super("Expr");
    }
}

class IntegerExpressionNode extends Node {
    IntegerExpressionNode() {
        super("IntExpr");
    }
}

class IntegerLiteralNode extends Node {
    IntegerLiteralNode(String value) {
        super("Number: " + value);
    }
}

class IntOpNode extends Node {
    IntOpNode() {
        super("IntOp: +");
    }
}

class BooleanExpressionNode extends Node {
    BooleanExpressionNode() {
        super("BoolExpr");
    }
}

class BooleanOperatorNode extends Node {
    BooleanOperatorNode(String value) {
        super("BoolOp: " + value);
    }
}

class BooleanValueNode extends Node {
    BooleanValueNode(String value) {
        super("BoolVal: " + value);
    }
}

class StringExpressionNode extends Node {
    StringExpressionNode() {
        super("StrgExpr");
    }
}

class StringNode extends Node {
    StringNode(String value) {
        super("String: " + value);
    }
}

class IdentifierNode extends Node {
    IdentifierNode(String value) {
        super("ID: " + value);
    }
}

public class CSTBuilder {
    private List<Token> tokens;
    private int position = 0;
    private List<String> errors = new ArrayList<>();

    public CSTBuilder(List<Token> tokens) {
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

    public ProgramNode parseProgram() {
        ProgramNode programNode = new ProgramNode();
        programNode.addChild(parseBlock());
        programNode.addChild(new EOFNode());
        return programNode;
    }

    private BlockNode parseBlock() {
        BlockNode blockNode = new BlockNode();
        expectToken(TokenType.LEFT_BRACE);
        blockNode.addChild(new LeftBraceNode());
        blockNode.addChild(parseStatementList());
        expectToken(TokenType.RIGHT_BRACE);
        blockNode.addChild(new StatementListNode());
        blockNode.addChild(new RightBraceNode());
        return blockNode;
    }

    private StatementListNode parseStatementList() {
        StatementListNode statementListNode = new StatementListNode();
        while (currentToken() != null && currentToken().getType() != TokenType.RIGHT_BRACE) {
            statementListNode.addChild(parseStatement());
        }
        return statementListNode;
    }

    private StatementNode parseStatement() {
        Token token = currentToken();
        StatementNode statementNode = new StatementNode();

        if (token.getType() == TokenType.INT) {
            statementNode.addChild(parseVariableDeclaration());
        } 
        else if (token.getType() == TokenType.STRING) {
            statementNode.addChild(parseVariableDeclaration());
        }
        else if (token.getType() == TokenType.BOOLEAN) {
            statementNode.addChild(parseVariableDeclaration());
        }
        else if (token.getType() == TokenType.ID) {
            statementNode.addChild(parseAssignmentStatement());
        } 
        else if (token.getType() == TokenType.IF) {
            statementNode.addChild(parseIfStatement());
        } 
        else if (token.getType() == TokenType.PRINT) {
            statementNode.addChild(parsePrintStatement());
        } 
        else if (token.getType() == TokenType.WHILE) {
            statementNode.addChild(parseWhileStatement());
        }
        else if (token.getType() == TokenType.LEFT_BRACE) {
            statementNode.addChild(parseBlock());
        } 
        else {
            error("Unexpected token: " + token.getType());
        }
        return statementNode;
    }

    private VariableDeclarationNode parseVariableDeclaration() {
        VariableDeclarationNode varDeclNode = new VariableDeclarationNode();
        Token token = currentToken();
        if (token != null && (token.getType() == TokenType.INT || token.getType() == TokenType.STRING || token.getType() == TokenType.BOOLEAN)) {
            varDeclNode.addChild(new VariableTypeNode(token.getValue())); // Variable type (int, string, boolean)
            nextToken(); // Consume the type token
            varDeclNode.addChild(new IdentifierNode(nextToken().getValue())); // Variable name
        } 
        else {
            error("Expected variable type but got '" + (token != null ? token.getValue() : "EOF") + "'");
        }
        
        return varDeclNode;
    }    

    private AssignmentStatementNode parseAssignmentStatement() {
        AssignmentStatementNode assignStmtNode = new AssignmentStatementNode();
        assignStmtNode.addChild(new IdentifierNode(nextToken().getValue())); // Variable name
        expectToken(TokenType.ASSIGN);
        assignStmtNode.addChild(parseExpression());
        return assignStmtNode;
    }

    private IfStatementNode parseIfStatement() {
        expectToken(TokenType.IF);
        IfStatementNode ifStmtNode = new IfStatementNode();
        ifStmtNode.addChild(parseBooleanExpression());
        ifStmtNode.addChild(parseBlock());
        return ifStmtNode;
    }

    private PrintStatementNode parsePrintStatement() {
        expectToken(TokenType.PRINT);
        PrintStatementNode printStmtNode = new PrintStatementNode();
        expectToken(TokenType.LEFT_PARENTHESIS);
        printStmtNode.addChild(parseExpression());
        expectToken(TokenType.RIGHT_PARENTHESIS);
        return printStmtNode;
    }

    private WhileStatementNode parseWhileStatement() {
        expectToken(TokenType.WHILE);
        WhileStatementNode whileStmtNode = new WhileStatementNode();
        whileStmtNode.addChild(parseBooleanExpression());
        whileStmtNode.addChild(parseBlock());
        return whileStmtNode;
    }

    private ExpressionNode parseExpression() {
        Token token = currentToken();
        ExpressionNode exprNode = new ExpressionNode();
    
        if (token.getType() == TokenType.ID) {
            exprNode.addChild(new IdentifierNode(token.getValue()));
            nextToken();
        }
        else if (token.getType() == TokenType.DIGIT) {
            exprNode.addChild(parseIntegerExpression());
        }
        else if (token.getType() == TokenType.LEFT_PARENTHESIS) {
            //exprNode.addChild(new LeftParenthesisNode());
            exprNode.addChild(parseBooleanExpression());
        }
        else if (token.getType() == TokenType.CHAR) {
            exprNode.addChild(parseStringExpression());
        }
        else if (token.getType() == TokenType.BOOLEAN_VAL) {
            exprNode.addChild(new BooleanValueNode(token.getValue()));
            nextToken();
        }
        else {
            error("Unexpected token: " + token.getType());
            return exprNode; // Return the expression node, possibly empty, on error
        }
    
        return exprNode;
    }    

    private BooleanExpressionNode parseBooleanExpression() {
        Token token = currentToken();
        BooleanExpressionNode boolExprNode = new BooleanExpressionNode();
        if (token.getType() == TokenType.BOOLEAN_VAL) {
            boolExprNode.addChild(new BooleanValueNode(nextToken().getValue()));
        }
        else if (token.getType() == TokenType.LEFT_PARENTHESIS) {
            expectToken(TokenType.LEFT_PARENTHESIS);
            boolExprNode.addChild(new LeftParenthesisNode());
            boolExprNode.addChild(parseExpression());
            boolExprNode.addChild(parseBoolOpExpression());
            expectToken(TokenType.BOOLEAN_OP);
            boolExprNode.addChild(parseExpression());
            expectToken(TokenType.RIGHT_PARENTHESIS);
            boolExprNode.addChild(new RightParenthesisNode());
        }
        return boolExprNode;
    }

    private IntegerExpressionNode parseIntegerExpression() {
        Token token = currentToken();
        IntegerExpressionNode integerExpressionNode = new IntegerExpressionNode();
    
        if (token.getType() == TokenType.DIGIT) {
            integerExpressionNode.addChild(new IntegerLiteralNode(token.getValue())); // Add digit as literal
            nextToken(); // Move to next token
            token = currentToken(); // Get updated token
            if (token != null && token.getType() == TokenType.INT_OP) {
                integerExpressionNode.addChild(parseIntOp()); 
                expectToken(TokenType.INT_OP);
                integerExpressionNode.addChild(parseExpression()); // Recursively parse next int expr
            }
        } 
        else {
            error("Expected integer expression but got '" + token.getValue() + "'");
        }
    
        return integerExpressionNode;
    }    

    private IntOpNode parseIntOp() {
        IntOpNode intOpNode = new IntOpNode();
        return intOpNode;
    }

    private StringExpressionNode parseStringExpression() {
        Token token = currentToken();
        StringExpressionNode stringExpressionNode = new StringExpressionNode();
        if (token.getType() == TokenType.CHAR) {
            stringExpressionNode.addChild(new StringNode(token.getValue()));
            expectToken(TokenType.CHAR);
        }
        else {
            error("Expected string expression");
        }
        return stringExpressionNode;
    }

    private BooleanOperatorNode parseBoolOpExpression() {
        Token token = currentToken();
        BooleanOperatorNode boolOpNode = new BooleanOperatorNode(token.getValue());
        return boolOpNode;
    }

    public List<String> getErrors() {
        return errors;
    }

    /*public static void main(String[] args) {
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

        CSTBuilder cstBuilder = new CSTBuilder(tokens); // Pass tokens here
        ProgramNode program = cstBuilder.parseProgram();
        program.print("");

        if (!cstBuilder.getErrors().isEmpty()) {
            System.out.println("Errors:");
            for (String error : cstBuilder.getErrors()) {
                System.out.println(error);
            }
        }

        scanner.close();
    }*/
}