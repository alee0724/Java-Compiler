import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter code:");

        StringBuilder currentProgram = new StringBuilder();
        int programCount = 1;
        boolean hasDollarSign = false;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (line.isEmpty()) {
                break;
            }

            if (line.startsWith("//")) {
                continue;
            }

            currentProgram.append(line).append("\n");

            // Check if line contains `$`
            if (line.contains("$")) {
                hasDollarSign = true;
                processProgram(currentProgram.toString(), programCount);
                currentProgram.setLength(0);
                programCount++;
            }
        }

        if (currentProgram.length() > 0) {
            String program = currentProgram.toString().trim();

            if (!hasDollarSign) {
                System.out.println("Warning: Missing '$' at the end of the code. Adding '$'...");
                program += "$";
            }

            processProgram(program, programCount);
        }

        scanner.close();
    }

    private static void processProgram(String program, int programCount) {
        System.out.println("Program " + programCount + ":");

        // Lexical Analysis
        Lexer lexer = new Lexer(program);
        List<Token> tokens = lexer.tokenize();

        for (Token token : tokens) {
            System.out.println(token);
        }

        if (!lexer.errors.isEmpty()) {
            System.out.println("Errors:");
            for (String error : lexer.errors) {
                System.out.println(error);
            }
        } 
        else {
            if (!lexer.warnings.isEmpty()) {
                System.out.println("Warnings:");
                for (String warning : lexer.warnings) {
                    System.out.println(warning);
                }
            }

            // Parsing
            Parser parser = new Parser(tokens);
            System.out.println("Parser for program " + programCount);
            parser.parse();

            if (parser.error == null) {
                // CST Building
                CSTBuilder cstBuilder = new CSTBuilder(tokens);
                System.out.println("CST for program " + programCount);
                ProgramNode cstProgram = cstBuilder.parseProgram();
                cstProgram.print("");

                // AST Building
                AST ast = new AST(tokens);
                ProgramASTNode astProgram = ast.Program();
                System.out.println("AST for program " + programCount);
                astProgram.print("");

                if (!ast.getErrors().isEmpty()) {
                    System.out.println("Errors:");
                    for (String error : ast.getErrors()) {
                        System.out.println(error);
                    }
                } 
                else if (!ast.getWarnings().isEmpty()) {
                    System.out.println("Warnings:");
                    for (String warning : ast.getWarnings()) {
                        System.out.println(warning);
                    }
                    System.out.println("Symbol table for program " + programCount);
                    ast.getSymbolTable().printSymbolTable();
                }
                else {
                    ast.getSymbolTable().printSymbolTable();
                }
            }
        }
    }
}