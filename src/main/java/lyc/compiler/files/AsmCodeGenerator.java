package lyc.compiler.files;

import lyc.compiler.table.DataType;
import lyc.compiler.table.SymbolEntry;
import lyc.compiler.terceto.Terceto;
import lyc.compiler.terceto.TercetoManager;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static lyc.compiler.table.SymbolTableManager.symbolTable;

public class AsmCodeGenerator implements FileGenerator {

    private HashMap<String, String> constansByValue = new HashMap<>();
    private Stack<String> conditionLabels = new Stack<>();
    private int conditionLabelsCont = 1;

    private Stack<String> thenLabels = new Stack<>();
    private int thenLabelsCont = 1;

    private HashMap<String, String> jumpLabels = new HashMap<String, String>();

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
                varValue = "\"" + entry.getValue() + "\", '$', 4 dup (?)";
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
                case "CMP":
                    writeDecition(fileWriter, terceto);
                    break;
                case "THEN":
                    writeThenLabel(fileWriter);
                    break;
                case "BI":
                    writeInconditionalJump(fileWriter, terceto);
                    break;
                case "endif":
                    writeLabel(fileWriter);
                    break;
                case "write":
                    writePrint(fileWriter, terceto);
                    break;
            }
        }
    }

    private void writePrint(FileWriter fileWriter, Terceto terceto) throws IOException {

        String value = constansByValue.get(terceto.getSecond().replace("\"", ""));
        if(value == null)
            value = terceto.getSecond();

        fileWriter.write("MOV ax,@DATA\n");
        fileWriter.write("MOV ds,ax\n");
        fileWriter.write("MOV es,ax\n");
        fileWriter.write("MOV dx,OFFSET " + value + "\n");
        fileWriter.write("MOV ah,9\n");
        fileWriter.write("int 21h\n");
        fileWriter.write("new\n\n");

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
            fileWriter.write("FLD " + aux + "\n");
        }

        fileWriter.write("FSTP " + terceto.getSecond() + "\n");
        fileWriter.write("FFREE\n\n");
    }

    private void writeExpression(FileWriter fileWriter, Terceto terceto, String operator) throws IOException {

        String asmOperator = "";

        switch (operator){
            case "+" -> asmOperator = "FADD";
            case "-" -> asmOperator = "FSUB";
            case "*" -> asmOperator = "FMUL";
            case "/" -> asmOperator = "FDIV";
        }

        int pos1 = Integer.valueOf(terceto.getSecond().replace("[", "").replace("]", "")) - 1;
        int pos2 = Integer.valueOf(terceto.getThird().replace("[", "").replace("]", "")) - 1;

        String aux1 = constansByValue.get(TercetoManager.tercetoList.get(pos1).getFirst());
        if(aux1 == null)
            aux1 = TercetoManager.tercetoList.get(pos1).getFirst();

        String aux2 = constansByValue.get(TercetoManager.tercetoList.get(pos2).getFirst());
        if(aux2 == null)
            aux2 = TercetoManager.tercetoList.get(pos2).getFirst();


        if(aux1 != "+" && aux1 != "-" && aux1 != "*" && aux1 != "/")
            fileWriter.write("FLD " + aux1 + "\n");
        fileWriter.write(asmOperator + " " + aux2 + "\n");
    }

    public void writeDecition(FileWriter fileWriter, Terceto terceto) throws IOException {
        fileWriter.write("FLD " + terceto.getSecond() + "\n");
        fileWriter.write("FCOMP " + terceto.getThird() + "\n");

        fileWriter.write("FSTSW ax\n");
        fileWriter.write("SAHF \n");


        Terceto jumpTerceto = TercetoManager.tercetoList.get(terceto.getNumber());
        String jumpType = getJump(jumpTerceto.getFirst());

        Terceto nextToJump = TercetoManager.tercetoList.get(terceto.getNumber() + 1);



        if(nextToJump.getFirst() == "OR" || nextToJump.getFirst() == "THEN"){
            String thenLabel = "then_" + thenLabelsCont;
            thenLabels.push(thenLabel);
            thenLabelsCont++;
        }

        String  conditionLabel = jumpLabels.get(jumpTerceto.getSecond());
            if(conditionLabel == null){
                conditionLabel = "else_" + conditionLabelsCont;
                jumpLabels.put(jumpTerceto.getSecond(),conditionLabel);
                conditionLabelsCont++;
                conditionLabels.push(conditionLabel);
            }


        fileWriter.write(jumpType + " " + conditionLabel + "\n");

    }

    private void writeInconditionalJump(FileWriter fileWriter, Terceto terceto) throws IOException {

        String conditionLabel = "endif_" + conditionLabelsCont;

        fileWriter.write("JMP " + conditionLabel + "\n\n");
        fileWriter.write(conditionLabels.pop() + ":\n");

        conditionLabelsCont++;
        conditionLabels.push(conditionLabel);
    }

    private void writeLabel(FileWriter fileWriter) throws IOException {
        String label = conditionLabels.pop();
        fileWriter.write("\n"+ label + ":\n");
    }

    private void writeThenLabel(FileWriter fileWriter) throws IOException {
        String label = thenLabels.pop();
        fileWriter.write("\n"+ label + ":\n");
    }
    private String getJump(String comparator) {
        String jump = null;
        switch (comparator){
            case "BLE" -> jump = "JLE";
            case "BGE" -> jump = "JGE";
            case "BLT" -> jump = "JB";
            case "BGT" -> jump = "JA";
            case "BNE" -> jump = "JNE";
            case "BQE" -> jump = "JE";
        }
        return jump;
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
