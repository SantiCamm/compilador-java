package lyc.compiler.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SymbolTableManager {

    public static HashMap<String, SymbolEntry> symbolTable = new HashMap<>();

    public HashMap<String, SymbolEntry> getSymbolTable() {
        return symbolTable;
    }

    public static void insertInTable(SymbolEntry entry){
            symbolTable.put(entry.getName().replace(" ", "_"), entry);
    }

    public static void setDataTypeInTable(String key, DataType dataType){
        SymbolEntry entry = symbolTable.get(key);
        entry.setDataType(dataType);
    }

    public static boolean existsInTable(String entryName){
        return symbolTable.containsKey(entryName);
    }

}


