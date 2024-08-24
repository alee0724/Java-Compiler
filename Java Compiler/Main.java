import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter code:");
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

        if (tokens.isEmpty() || tokens.get(tokens.size() - 1).getType() != TokenType.EOF) {
            lexer.errors.add("Missing '$' at the end of the code.");
            // System.out.println("Error: Missing '$' at the end of the code.");
        }

        if (!lexer.errors.isEmpty()) {
            System.out.println("Errors:");
            for (String error : lexer.errors) {
                System.out.println(error);
            }
        } else {
            Parser parser = new Parser(tokens);
            parser.parse();

            if (parser.error == null) { // Only create CST if there are no parser errors
                CSTBuilder cstBuilder = new CSTBuilder(tokens);
                ProgramNode cstProgram = cstBuilder.parseProgram();
                cstProgram.print("");

                AST ast = new AST(tokens); // Pass tokens here
                ProgramASTNode astProgram = ast.Program();
                astProgram.print("");

                if (!ast.getErrors().isEmpty()) {
                    System.out.println("Errors/Warnings:");
                    for (String error : ast.getErrors()) {
                        System.out.println(error);
                    }
                }
                else {
                    ast.getSymbolTable().printSymbolTable();
                }
            }
        }

        scanner.close();
    }
}