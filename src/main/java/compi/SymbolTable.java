package compi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class SymbolTable {
    // hashmap with the SymbolTable, the key is the lexema and has a lot of information
    private HashMap<Integer, SymbolTableEntry> symbolTable;
    private int nextPtr;

    public class SymbolTableEntry {
        private int tokenId;
        private String lexema;
        private HashMap<String, String> attributes;

        public SymbolTableEntry() {
            attributes = new HashMap<String, String>();
        }

        public SymbolTableEntry copy() {
            SymbolTableEntry entry = new SymbolTableEntry();
            entry.tokenId = tokenId;
            entry.lexema = lexema;
            entry.attributes = new HashMap<String, String>(attributes);
            return entry;
        }
    }

    public SymbolTable() {
        symbolTable = new HashMap<Integer, SymbolTableEntry>();
        nextPtr = 1;
    }

    public SymbolTableEntry getEntry(Integer ptr) {
        return symbolTable.get(ptr);
    }

    public Integer addEntry(String lexema, int tokenId) {
        SymbolTableEntry entry = new SymbolTableEntry();
        entry.tokenId = tokenId;
        entry.lexema = lexema;
        symbolTable.put(nextPtr, entry);
        return nextPtr++;
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

    public boolean contains(Integer ptr) {
        return symbolTable.containsKey(ptr);
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

    public Integer getPtr(String lexema, String uso) {
        //iterate over entrySet
        for (HashMap.Entry<Integer, SymbolTableEntry> entry : symbolTable.entrySet()) {
            // check lexema, check that its an attribute called uso and check that its value is uso
            if (entry.getValue().lexema.equals(lexema) &&
                    entry.getValue().attributes.containsKey("uso") &&
                    entry.getValue().attributes.get("uso").equals(uso)) {
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

    public Integer getPtr(String lexema, Ambito ambito) {
        if (lexema == null) return 0;
        if (lexema.contains(":")) {
            Integer ptr = getPtr(lexema);
            return ptr;
        }
        while (!ambito.isEmpty()) {
            Integer ptr = getPtr(lexema + ":" + ambito.toString());
            if (ptr != 0)
                return ptr;

            ambito.pop();
        }
        return 0;
    }

    public Integer getPtr(String lexema, Ambito ambito, String uso) {
        if (lexema.contains(":")) {
            Integer ptr = getPtr(lexema, uso);
            return ptr;
        }
        while (!ambito.isEmpty()) {
            Integer ptr = getPtr(lexema + ":" + ambito.toString(), uso);
            if (ptr != 0)
                return ptr;

            ambito.pop();
        }
        return 0;
    }
    public void delEntry(Integer ptr) {
        System.out.println("Borrando " + getLexema(ptr));
        symbolTable.remove(ptr);
    }

    public void print() {
        System.out.println("Semantic Table:");
        System.out.println("Ptr\tTokenID\tLexema\tAttributes");
        for (HashMap.Entry<Integer, SymbolTableEntry> entry : symbolTable.entrySet()) {
            String valid = getAttribute(entry.getKey(), "valid");
            if (valid != null && valid.equals("0"))
                continue;
            System.out.print(entry.getKey() + "\t");
            System.out.print(entry.getValue().tokenId + "\t");
            System.out.print(entry.getValue().lexema + "\t");
            for (HashMap.Entry<String, String> attribute : entry.getValue().attributes.entrySet())
                System.out.print(attribute.getKey() + ": " + attribute.getValue() + "\t");
            System.out.println();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Semantic Table:\n");
        sb.append("Ptr\tTokenID\tLexema\tAttributes\n");

        for (HashMap.Entry<Integer, SymbolTableEntry> entry : symbolTable.entrySet()) {
            String valid = getAttribute(entry.getKey(), "valid");
            if (valid != null && valid.equals("0")) {
                continue;
            }
            sb.append(entry.getKey()).append("\t");
            sb.append(entry.getValue().tokenId).append("\t");
            sb.append(entry.getValue().lexema).append("\t");

            for (HashMap.Entry<String, String> attribute : entry.getValue().attributes.entrySet()) {
                sb.append(attribute.getKey()).append(": ").append(attribute.getValue()).append("\t");
            }
            sb.append("\n");
        }

        return sb.toString();
    }


    // implemetar un metodo para que puede ser iterada con un foreach
    public Set<Integer> keySet() {
        return symbolTable.keySet();
    }
    public static void main(String[] args) {
        String lexema = "class";
        String[] lexemaSplit = lexema.split("\\.");

        for (int i = 0; i < lexemaSplit.length; i++)
            System.out.println(lexemaSplit[i]);
    }
}
