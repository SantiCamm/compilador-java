package lyc.compiler.terceto;

public class Terceto {

    private String first;
    private String second = "_";
    private String third = "_";

    private static int number = 1;

    public Terceto(String first, String second, String third) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.number++;
    }

    public Terceto(String first, int second, String third) {
        this.first = first;
        this.second = "[" + second + "]";
        this.third = third;
        this.number++;
    }

    public Terceto(String first, String second, int third) {
        this.first = first;
        this.second = second;
        this.third = "[" + third + "]";
        this.number++;
    }

    public Terceto(String first, int second, int third) {
        this.first = first;
        this.second = "[" + second + "]";
        this.third = "[" + third + "]";
        this.number++;
    }

    public Terceto(String first) {
        this.first = first;
        this.number++;
    }

    public String getTerceto(){
        return "(" + first + "," + second + "," + third + ")";
    }

    public int getNumber() {
        return number;
    }
}
