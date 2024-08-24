import java.util.ArrayList;
import java.util.List;

public class SymbolTable {
    private List<SymbolTableEntry> table = new ArrayList<>();

    public void addEntry(String name, String type, int scope, int line) {
        table.add(new SymbolTableEntry(name, type, scope, line));
    }

    public boolean contains(String name, int scope) {
        return table.stream()
                    .anyMatch(entry -> entry.getName().equals(name) && entry.getScope() == scope);
    }

    public SymbolTableEntry getEntry(String name, int scope) {
        return table.stream()
                    .filter(entry -> entry.getName().equals(name) && entry.getScope() == scope)
                    .findFirst()
                    .orElse(null);
    }

    public SymbolTableEntry getEntryAcrossScopes(String name) {
        return table.stream()
                    .filter(entry -> entry.getName().equals(name))
                    .findFirst()
                    .orElse(null);
    }

    public void printSymbolTable() {
        for (SymbolTableEntry entry : table) {
            System.out.println(entry);
        }
    }
}