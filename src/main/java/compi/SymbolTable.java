package compi;

import java.util.HashMap;

public class SymbolTable {
    // hashmap with the SymbolTable, the key is the lexema and has a lot of information
    private HashMap<Integer, SymbolTableEntry> symbolTable;
    private int nextPtr;

    private class SymbolTableEntry {
        private int tokenId;
        private String lexema;
        private HashMap<String, String> attributes;

        public SymbolTableEntry() {
            attributes = new HashMap<String, String>();
        }
    }

    public SymbolTable() {
        symbolTable = new HashMap<Integer, SymbolTableEntry>();
        nextPtr = 1;
    }

    public Integer addEntry(String lexema, int tokenId, String description) {
        SymbolTableEntry entry = new SymbolTableEntry();
        entry.tokenId = tokenId;
        entry.lexema = lexema;
        entry.attributes.put("description", description);
        symbolTable.put(nextPtr, entry);
        return ++nextPtr;
    }

    public int getTokenId(Integer ptr) {
        SymbolTableEntry entry = symbolTable.get(ptr);
        if (entry == null)
            return -1;
        return entry.tokenId;
    }

    public String getLexema(Integer ptr) {
        SymbolTableEntry entry = symbolTable.get(ptr);
        if (entry == null)
            return null;
        return entry.lexema;
    }

    public String getAttribute(Integer ptr, String attribute) {
        SymbolTableEntry entry = symbolTable.get(ptr);
        if (entry == null)
            return null;
        return entry.attributes.get(attribute);
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

    public boolean setAttribute(Integer ptr, String attribute, String value) {
        SymbolTableEntry entry = symbolTable.get(ptr);
        if (entry == null)
            return false;

        entry.attributes.put(attribute, value);
        return true;
    }

    public boolean setLexema(Integer ptr, String lexema) {
        SymbolTableEntry entry = symbolTable.get(ptr);
        if (entry == null)
            return false;

        entry.lexema = lexema;
        return true;
    }

    public void print() {
        System.out.println("Semantic Table:");
        System.out.println("Ptr\tLexema\tTokenId\tDescription");
        for (HashMap.Entry<Integer, SymbolTableEntry> entry : symbolTable.entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue().lexema + "\t" + entry.getValue().tokenId + "\t" + entry.getValue().attributes.get("description"));
        }
    }
}
