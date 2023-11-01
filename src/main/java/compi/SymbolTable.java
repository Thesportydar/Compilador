package compi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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

    public Integer getPtr(String lexema, Ambito ambito) {
        System.out.println("Voy a buscar " + lexema + " en ambito " + ambito.toString());
        String[] lexemaSplit = lexema.split("\\.");
        lexema = lexemaSplit[lexemaSplit.length - 1];

        System.out.println("lexema: " + lexema);
        for (int i = 0; i < lexemaSplit.length; i++)
            System.out.println(lexemaSplit[i]);

        for (int i = 0; i < lexemaSplit.length-1; i++){
            Ambito ambitoCopy = ambito.copy();
            if (i > 0)
                ambitoCopy.push(lexemaSplit[i-1]);

            System.out.println("Buscando " + lexemaSplit[i] + " en ambito " + ambitoCopy.toString());
            Integer ptr = getPtr(lexemaSplit[i], ambitoCopy);
            if (ptr != 0) {
                String padre = getLexema(Integer.parseInt(getAttribute(ptr, "tipo")));
                lexemaSplit[i] = padre.split(":")[0];
                System.out.println("Encontre " + lexemaSplit[i] + " en instancia " + ptr);
            } else {
                return null;
            }
        }

        while (!ambito.isEmpty()) {
            if (lexemaSplit.length > 1)
                ambito.push(lexemaSplit[lexemaSplit.length - 2]);

            Integer ptr = getPtr(lexema + ":" + ambito.toString());
            if (ptr != 0)
                return ptr;

            ambito.pop();
            if (lexemaSplit.length > 1)
                ambito.pop();
        }
        return null;
    }

    public void delEntry(Integer ptr) {
        System.out.println("Borrando " + getLexema(ptr));
        symbolTable.remove(ptr);
    }

    public Integer upgradeLexema(Integer ptr, String lexema) {
        SymbolTableEntry entry = getEntry(ptr);
        if (entry == null)
            return null;
        
        entry = entry.copy();
        entry.lexema += lexema;
        symbolTable.put(nextPtr, entry);
        return nextPtr++;
    }

    public void print() {
        System.out.println("Semantic Table:");
        System.out.println("Ptr\tTokenID\tLexema\tAttributes");
        for (HashMap.Entry<Integer, SymbolTableEntry> entry : symbolTable.entrySet()) {
            System.out.print(entry.getKey() + "\t");
            System.out.print(entry.getValue().tokenId + "\t");
            System.out.print(entry.getValue().lexema + "\t");
            for (HashMap.Entry<String, String> attribute : entry.getValue().attributes.entrySet())
                System.out.print(attribute.getKey() + ": " + attribute.getValue() + "\t");
            System.out.println();
        }
    }

    public static void main(String[] args) {
        String lexema = "class";
        String[] lexemaSplit = lexema.split("\\.");

        for (int i = 0; i < lexemaSplit.length; i++)
            System.out.println(lexemaSplit[i]);
    }
}
