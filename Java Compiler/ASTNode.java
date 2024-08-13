import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Scanner;

abstract class ASTNode {
    String name;
    List<ASTNode> children = new ArrayList<>();

    ASTNode(String name) {
        this.name = name;
    }

    void addChild(ASTNode ASTnode) {
        children.add(ASTnode);
    }

    void print(String indent) {
        System.out.println(indent + name);
        for (ASTNode child : children) {
            child.print(indent + "  ");
        }
    }
}

class ProgramASTNode extends ASTNode {
    ProgramASTNode() {
        super("Program");
    }
}

class EOFASTNode extends ASTNode {
    EOFASTNode() {
        super("$");
    }
}

class BlockASTNode extends ASTNode {
    BlockASTNode() {
        super("Block");
    }
}

class LeftBraceASTNode extends ASTNode {
    LeftBraceASTNode() {
        super("{");
    }
}

class RightBraceASTNode extends ASTNode {
    RightBraceASTNode() {
        super("}");
    }
}

class LeftParenthesisASTNode extends ASTNode {
    LeftParenthesisASTNode() {
        super("(");
    }
}

class RightParenthesisASTNode extends ASTNode {
    RightParenthesisASTNode() {
        super(")");
    }
}

class VariableDeclarationASTNode extends ASTNode {
    VariableDeclarationASTNode() {
        super("VarDeclaration");
    }
}

class VariableTypeASTNode extends ASTNode {
    VariableTypeASTNode(String value) {
        super("Type: " + value);
    }
}

class AssignmentStatementASTNode extends ASTNode {
    AssignmentStatementASTNode() {
        super("AssignStatement");
    }
}

class IfStatementASTNode extends ASTNode {
    IfStatementASTNode() {
        super("IfStatement");
    }
}

class PrintStatementASTNode extends ASTNode {
    PrintStatementASTNode() {
        super("PrintStatement");
    }
}

class WhileStatementASTNode extends ASTNode {
    WhileStatementASTNode() {
        super("WhileStatement");
    }
}

class IntegerExpressionASTNode extends ASTNode {
    IntegerExpressionASTNode() {
        super("IntExpr");
    }
}

class IntegerLiteralASTNode extends ASTNode {
    IntegerLiteralASTNode(String value) {
        super("Number: " + value);
    }
}

class IntOpASTNode extends ASTNode {
    IntOpASTNode() {
        super("IntOp: +");
    }
}

class BooleanExpressionASTNode extends ASTNode {
    BooleanExpressionASTNode() {
        super("BoolExpr");
    }
}

class BooleanOperatorASTNode extends ASTNode {
    BooleanOperatorASTNode(String value) {
        super("BoolOp: " + value);
    }
}

class BooleanValueASTNode extends ASTNode {
    BooleanValueASTNode(String value) {
        super("BoolVal: " + value);
    }
}

class StringExpressionASTNode extends ASTNode {
    StringExpressionASTNode() {
        super("StrgExpr");
    }
}

class StringASTNode extends ASTNode {
    StringASTNode(String value) {
        super("String: " + value);
    }
}

class IdentifierASTNode extends ASTNode {
    IdentifierASTNode(String value) {
        super("ID: " + value);
    }
}