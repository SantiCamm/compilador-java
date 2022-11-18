package lyc.compiler.files;

import lyc.compiler.table.DataType;
import lyc.compiler.table.SymbolEntry;
import lyc.compiler.table.SymbolTableManager;
import lyc.compiler.terceto.Terceto;
import lyc.compiler.terceto.TercetoManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static lyc.compiler.table.SymbolTableManager.symbolTable;

public class AsmCodeGenerator implements FileGenerator {

    private HashMap<String, String> constansByValue = new HashMap<>();

    @Override
    public void generate(FileWriter fileWriter) throws IOException {

        insertHeader(fileWriter);
        insertVariables(fileWriter);
        insertCode(fileWriter);
        insertFooter(fileWriter);
    }

    private void insertHeader(FileWriter fileWriter) throws IOException {
        fileWriter.write("include number.asm\n");
        fileWriter.write(".MODEL LARGE\n");
        fileWriter.write(".386\n");
        fileWriter.write(".STACK 200h\n\n");
    }

    private void insertFooter(FileWriter fileWriter) throws IOException{
        fileWriter.write("mov ax,4c00h\n");
        fileWriter.write("int 21h\n");
        fileWriter.write("End\n");
    }

    private void insertVariables(FileWriter fileWriter) throws IOException {
        List<String> keys = new ArrayList<>(symbolTable.keySet());
        for (String key : keys) {

            SymbolEntry entry = symbolTable.get(key);
            String asmType;
            String varValue;

            if(entry.getDataType() == DataType.STRING_CONS){
                asmType = "db";
                varValue = "\"" + entry.getValue() + "\", 0";
                constansByValue.put(entry.getValue(), key);
            }
            else if(entry.getDataType() == DataType.INTEGER_CONS || entry.getDataType() == DataType.FLOAT_CONS){
                asmType = "dd";
                varValue = Double.valueOf(entry.getValue()).toString();
                constansByValue.put(entry.getValue(), key);
            }else{
                asmType = "dd";
                varValue = "?";
            }

            fileWriter.write(key + tabCalculator(key, 32) + asmType + "\t\t" + varValue + "\n");
        }

        fileWriter.write("\n\n");
        fileWriter.write(".CODE\n");
        fileWriter.write("mov AX,@DATA\n");
        fileWriter.write("mov .DS,AX\n");
        fileWriter.write("mov es,ax\n");
        fileWriter.write("\n\n");
    }

    private void insertCode(FileWriter fileWriter) throws IOException {
        for(Terceto terceto : TercetoManager.tercetoList){

            switch (terceto.getFirst()){
                case "=" :
                    writeAsignation(fileWriter, terceto);
                    break;
                case "+":
                    writeExpression(fileWriter, terceto, "+");
                    break;
                case "*":
                    writeExpression(fileWriter, terceto, "*");
                    break;
                case "/":
                    writeExpression(fileWriter, terceto, "/");
                    break;
                case "-":
                    writeExpression(fileWriter, terceto, "-");
                    break;
            }
        }
    }

    private void writeAsignation(FileWriter fileWriter, Terceto terceto) throws IOException {

        String third = terceto.getThird();
        char openBracket = '[';

        String aux;

        if(third.charAt(0) == openBracket) { //indica que es el numero de un terceto
            int pos = Integer.valueOf(third.replace("[", "").replace("]", "")) - 1;

            String first = TercetoManager.tercetoList.get(pos).getFirst();
            aux = constansByValue.get(TercetoManager.tercetoList.get(pos).getFirst());

            if(aux == null)
                aux = first;
        }
        else
            aux = constansByValue.get(third.replace("\"", ""));

        if(aux != "+" && aux != "-" && aux != "*" && aux != "/" && aux != null){
            fileWriter.write("fld " + aux + "\n");
        }

        fileWriter.write("fstp " + terceto.getSecond() + "\n");
        fileWriter.write("ffree\n\n");
    }

    private void writeExpression(FileWriter fileWriter, Terceto terceto, String operator) throws IOException {

        String asmOperator = "";

        switch (operator){
            case "+" -> asmOperator = "fadd";
            case "-" -> asmOperator = "fsub";
            case "*" -> asmOperator = "fmul";
            case "/" -> asmOperator = "fdiv";
        }

        int pos1 = Integer.valueOf(terceto.getSecond().replace("[", "").replace("]", "")) - 1;
        int pos2 = Integer.valueOf(terceto.getThird().replace("[", "").replace("]", "")) - 1;

        String aux1 = constansByValue.get(TercetoManager.tercetoList.get(pos1).getFirst());
        if(aux1 == null)
            aux1 = TercetoManager.tercetoList.get(pos1).getFirst();

        String aux2 = constansByValue.get(TercetoManager.tercetoList.get(pos2).getFirst());
        if(aux2 == null)
            aux2 = TercetoManager.tercetoList.get(pos1).getFirst();

        fileWriter.write("fld " + aux1 + "\n");
        fileWriter.write(asmOperator + " " + aux2 + "\n");
    }

    private String tabCalculator(String value, int columnWidth){
        String tab = "\t";
        int repeatTimes = roundNumber((float)(columnWidth - value.length()) / 4);

        return tab.repeat(repeatTimes);
    }

    private int roundNumber(float num){
        float diff = num - (int)num;
        if(diff >= 0.5)
            return (int)num+1;
        else
            return (int)num;
    }
}
