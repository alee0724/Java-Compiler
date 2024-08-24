import java.util.HashMap;
import java.util.Map;

/*public class SymbolTable {
    private Map<String, SymbolTableEntry> symbolTable;

    public SymbolTable() {
        this.symbolTable = new HashMap<>();
    }

    public void addEntry(String name, String type, int scope, int line) {
        symbolTable.put(name, new SymbolTableEntry(name, type, scope, line));
    }

    public SymbolTableEntry getEntry(String name) {
        return symbolTable.get(name);
    }

    public boolean contains(String name) {
        return symbolTable.containsKey(name);
    }

    public void printSymbolTable() {
        System.out.println("Program Symbol Table");
        System.out.println(" -------------------------------------");
        for (SymbolTableEntry entry : symbolTable.values()) {
            System.out.println("[Name: " + entry.getName() + 
                               ", Type: " + entry.getType() + 
                               ", Scope: " + entry.getScope() + 
                               ", Line: " + entry.getLine() + "]");
        }
    }

    public class SymbolTableEntry {
        private String name;
        private String type;
        private int scope;
        private int line;

        public SymbolTableEntry(String name, String type, int scope, int line) {
            this.name = name;
            this.type = type;
            this.scope = scope;
            this.line = line;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public int getScope() {
            return scope;
        }

        public int getLine() {
            return line;
        }
    }
}*/

public class SymbolTableEntry {
    private String name;
    private String type;
    private int scope;
    private int line;

    public SymbolTableEntry(String name, String type, int scope, int line) {
        this.name = name;
        this.type = type;
        this.scope = scope;
        this.line = line;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getScope() {
        return scope;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Type: " + type + ", Scope: " + scope + ", Line: " + line;
    }
}