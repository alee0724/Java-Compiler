import java.util.HashMap;
import java.util.Map;

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