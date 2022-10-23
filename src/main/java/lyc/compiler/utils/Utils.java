package lyc.compiler.utils;

import lyc.compiler.model.UnknownCharacterException;
import lyc.compiler.table.DataType;
import lyc.compiler.table.SymbolEntry;
import lyc.compiler.table.SymbolTableManager;

import java.util.Stack;

public class Utils {

    public static boolean checkExpresionTypes(Stack<DataType> dataTypes, Stack<String> ids){

        DataType d1 = dataTypes.pop();
        DataType d2 = dataTypes.pop();

        if(d1 == DataType.ID && d2 == DataType.ID){
            String id1 = ids.pop();
            String id2 = ids.pop();

            SymbolEntry symbol1 = SymbolTableManager.symbolTable.get(id1);
            SymbolEntry symbol2 = SymbolTableManager.symbolTable.get(id2);

            if(symbol1.getDataType() == symbol2.getDataType())
                return true;

            if(symbol1.getDataType() == DataType.INTEGER_TYPE && symbol2.getDataType() == DataType.FLOAT_TYPE)
                return true;

            if(symbol1.getDataType() == DataType.FLOAT_TYPE && symbol2.getDataType() == DataType.INTEGER_TYPE)
                return true;

            throw new InvalidExpressionValuesException("Invalid expression value types: " + id1 + " type is not compatible with " + id2 + "type");
        }

        if(d1 == DataType.ID){
            String id1 = ids.pop();

            SymbolEntry symbol1 = SymbolTableManager.symbolTable.get(id1);

            if(symbol1.getDataType() == DataType.INTEGER_TYPE && d2 == DataType.INTEGER_CONS)
                return true;

            if(symbol1.getDataType() == DataType.FLOAT_TYPE && d2 == DataType.FLOAT_CONS)
                return true;

            if(symbol1.getDataType() == DataType.FLOAT_TYPE && d2 == DataType.INTEGER_CONS)
                return true;

            if(symbol1.getDataType() == DataType.INTEGER_TYPE && d2 == DataType.FLOAT_CONS)
                return true;

            throw new InvalidExpressionValuesException("Invalid expression value types: " + id1 + " type is not compatible with " + d2.getName());
        }

        if(d2 == DataType.ID){
            String id2 = ids.pop();

            SymbolEntry symbol2 = SymbolTableManager.symbolTable.get(id2);

            if(symbol2.getDataType() == DataType.INTEGER_TYPE && d1 == DataType.INTEGER_CONS)
                return true;

            if(symbol2.getDataType() == DataType.FLOAT_TYPE && d1 == DataType.FLOAT_CONS)
                return true;

            if(symbol2.getDataType() == DataType.FLOAT_TYPE && d1 == DataType.INTEGER_CONS)
                return true;

            if(symbol2.getDataType() == DataType.INTEGER_TYPE && d1 == DataType.FLOAT_CONS)
                return true;

            throw new InvalidExpressionValuesException("Invalid expression value types: " + id2 + " type is not compatible with " + d1.getName());
        }

        if(d1 == d2)
            return true;

        if(d1 == DataType.INTEGER_CONS && d2 == DataType.FLOAT_CONS)
            return true;

        if(d1 == DataType.FLOAT_CONS && d2 == DataType.INTEGER_CONS)
            return true;

        throw new InvalidExpressionValuesException("Invalid expression value types: " + d1.getName() + " type is not compatible with " + d2.getName());
    }

}
