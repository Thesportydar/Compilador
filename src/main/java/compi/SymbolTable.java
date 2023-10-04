package compi;

import java.util.HashMap;

public class SymbolTable {
    // hashmap with the SymbolTable, the key is the lexema and has a lot of information
    private HashMap<String, SymbolTableEntry> symbolTable;

    private class SymbolTableEntry {
        private int tokenId;
        private String description;
    }

    public SymbolTable() {
        symbolTable = new HashMap<String, SymbolTableEntry>();
    }

    public void addEntry(String lexema, int tokenId, String description) {
        SymbolTableEntry entry = new SymbolTableEntry();
        entry.tokenId = tokenId;
        entry.description = description;
        symbolTable.put(lexema, entry);
    }

    public int getTokenId(String lexema) {
        return symbolTable.get(lexema).tokenId;
    }

    public String getDescription(String lexema) {
        return symbolTable.get(lexema).description;
    }

    public boolean contains(String lexema) {
        return symbolTable.containsKey(lexema);
    }

    public void print() {
        System.out.println("Semantic Table:");
        for (String key : symbolTable.keySet()) {
            System.out.println(key + " -> " + symbolTable.get(key).tokenId + " " + symbolTable.get(key).description);
        }
    }
}
