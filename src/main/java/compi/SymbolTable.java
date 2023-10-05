package compi;

import java.util.HashMap;

public class SymbolTable {
    // hashmap with the SymbolTable, the key is the lexema and has a lot of information
    private HashMap<Integer, SymbolTableEntry> symbolTable;
    private int nextPtr;

    private class SymbolTableEntry {
        private int tokenId;
        private String lexema;
        private String description;
    }

    public SymbolTable() {
        symbolTable = new HashMap<Integer, SymbolTableEntry>();
        nextPtr = 1;
    }

    public Integer addEntry(String lexema, int tokenId, String description) {
        SymbolTableEntry entry = new SymbolTableEntry();
        entry.tokenId = tokenId;
        entry.lexema = lexema;
        entry.description = description;
        symbolTable.put(nextPtr, entry);
        return ++nextPtr;
    }

    public int getTokenId(Integer ptr) {
        return symbolTable.get(ptr).tokenId;
    }

    public String getLexema(Integer ptr) {
        return symbolTable.get(ptr).lexema;
    }

    public String getDescription(String lexema) {
        return symbolTable.get(lexema).description;
    }

    public boolean contains(String lexema) {
        //iterate over entrySet
        for (HashMap.Entry<Integer, SymbolTableEntry> entry : symbolTable.entrySet()) {
            if (entry.getValue().lexema.equals(lexema)) {
                return true;
            }
        }
        return false;
    }

    public Integer getPtr(String lexema) {
        //iterate over entrySet
        for (HashMap.Entry<Integer, SymbolTableEntry> entry : symbolTable.entrySet()) {
            if (entry.getValue().lexema.equals(lexema)) {
                return entry.getKey();
            }
        }
        return 0;
    }

    public void print() {
        System.out.println("Semantic Table:");
        System.out.println("Ptr\tLexema\tTokenId\tDescription");
        for (HashMap.Entry<Integer, SymbolTableEntry> entry : symbolTable.entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue().lexema + "\t" + entry.getValue().tokenId + "\t" + entry.getValue().description);
        }
    }
}
