package lyc.compiler.terceto;

public class Terceto {

    static int TERCETO_UNIQUE_ID = 0;

    private String first;
    private String second = "_";
    private String third = "_";
    private int number = ++TERCETO_UNIQUE_ID;

    public Terceto(String first, String second, String third) {
        this.first = first;
        this.second = second;
        this.third = third;

        TercetoManager.tercetoList.add(this);
    }

    public Terceto(String first, int second, String third) {
        this.first = first;
        this.second = "[" + second + "]";
        this.third = third;

        TercetoManager.tercetoList.add(this);

    }

    public Terceto(String first, String second, int third) {
        this.first = first;
        this.second = second;
        this.third = "[" + third + "]";

        TercetoManager.tercetoList.add(this);
    }

    public Terceto(String first, int second, int third) {
        this.first = first;
        this.second = "[" + second + "]";
        this.third = "[" + third + "]";

        TercetoManager.tercetoList.add(this);
    }

    public Terceto(String first) {
        this.first = first;

        TercetoManager.tercetoList.add(this);
    }

    public String getTerceto(){
        return "(" + first + "," + second + "," + third + ")";
    }

    public int getNumber() {
        return number;
    }

    public String getFirst() {
        return first;
    }

    public String getSecond() {
        return second;
    }

    public String getThird() {
        return third;
    }
}
