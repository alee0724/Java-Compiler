import java.util.ArrayList;
import java.util.List;

class SymbolTable {
    private List<SymbolTableEntry> entries;

    public SymbolTable() {
        this.entries = new ArrayList<>();
    }

    public void addEntry(String name, String type, int scope, int line) {
        entries.add(new SymbolTableEntry(name, type, scope, line));
    }

    public void printSymbolTable() {
        System.out.println("Program Symbol Table");
        System.out.println(" -------------------------------------");
        for (SymbolTableEntry entry : entries) {
            System.out.println("[Name: " + entry.getName() + 
                               ", Type: " + entry.getType() + 
                               ", Scope: " + entry.getScope() + 
                               ", Line: " + entry.getLine() + "]");
        }
    }

    private class SymbolTableEntry {
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
}