package lyc.compiler.terceto;

import java.util.ArrayList;
import java.util.List;

public class TercetoManager {

    public static List<Terceto> tercetoList = new ArrayList<>();

    public static int getLastTerceto () {
        return tercetoList.get(tercetoList.size() - 1 ).getNumber();
    }

    public static TercetoIndex programIdx = new TercetoIndex();
    public static TercetoIndex blockIdx = new TercetoIndex();
    public static TercetoIndex sentenceIdx = new TercetoIndex();
    public static TercetoIndex var_declarationIdx = new TercetoIndex();
    public static TercetoIndex var_sencente_decIdx = new TercetoIndex();
    public static TercetoIndex data_typeIdx = new TercetoIndex();
    public static TercetoIndex id_listIdx = new TercetoIndex();
    public static TercetoIndex decitionIdx = new TercetoIndex();
    public static TercetoIndex conditionIdx = new TercetoIndex();
    public static TercetoIndex comparisonIdx = new TercetoIndex();
    public static TercetoIndex comparatorIdx = new TercetoIndex();
    public static TercetoIndex iteratorIdx = new TercetoIndex();
    public static TercetoIndex assignmentIdx  = new TercetoIndex();
    public static TercetoIndex s_writeIdx = new TercetoIndex();
    public static TercetoIndex s_readIdx = new TercetoIndex();
    public static TercetoIndex write_paramIdx = new TercetoIndex();
    public static TercetoIndex read_paramIdx = new TercetoIndex();
    public static TercetoIndex expressionIdx = new TercetoIndex();
    public static TercetoIndex termIdx = new TercetoIndex();
    public static TercetoIndex factorIdx = new TercetoIndex();
    public static TercetoIndex all_equalIdx = new TercetoIndex();
    public static TercetoIndex repeat_inlineIdx = new TercetoIndex();
    public static TercetoIndex comparable_listIdx = new TercetoIndex();
    public static TercetoIndex comparableIdx = new TercetoIndex();
    public static TercetoIndex obj_listIdx = new TercetoIndex();

}
