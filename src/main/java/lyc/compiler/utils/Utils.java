package lyc.compiler.utils;

import lyc.compiler.model.CompilerException;
import lyc.compiler.model.InvalidExpressionValuesException;
import lyc.compiler.table.DataType;
import lyc.compiler.table.SymbolEntry;
import lyc.compiler.table.SymbolTableManager;

import java.util.Stack;

public class Utils {

    public static boolean checkIdExpresionType(String id, boolean isAssignation, boolean isString) throws CompilerException {
        SymbolEntry symbol = SymbolTableManager.symbolTable.get(id);

        if(!isString) {
            if (symbol.getDataType() == DataType.FLOAT_TYPE || symbol.getDataType() == DataType.INTEGER_TYPE)
                return true;
        } else {
            if (symbol.getDataType() == DataType.STRING_TYPE)
                return true;
        }

        if(isAssignation)
            throw new InvalidExpressionValuesException("Invalid assignation of an expression to variable");
        else
            throw new InvalidExpressionValuesException("Invalid expression value types for id: '" + id + "' type is not compatible for an expression ");

    }
}
