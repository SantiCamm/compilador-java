package lyc.compiler.terceto;

public class Terceto {

    static int TERCETO_UNIQUE_ID = 0;

    private String first;
    private String second = "_";
    private String third = "_";
    private final int number = ++TERCETO_UNIQUE_ID;

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

    public Terceto(String first, String second) {
        this.first = first;
        this.second = second;

        TercetoManager.tercetoList.add(this);
    }

    public Terceto(String first, int second) {
        this.first = first;
        this.second = "[" + second + "]";

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

    public void setFirst(String first) {
        this.first = first;
    }

    public void setSecond(String second) {
        this.second = "[" + second + "]";
    }

    public void setThird(String third) {
        this.third = "[" + third + "]";
    }

    @Override
    public String toString() {
        return "Terceto{" +
                "first='" + first + '\'' +
                ", second='" + second + '\'' +
                ", third='" + third + '\'' +
                '}';
    }

    public void updateTerceto (int index, String updatedNumber) {
        switch (index) {
            case 1 -> this.setFirst(updatedNumber);
            case 2 -> this.setSecond(updatedNumber);
            case 3 -> this.setThird(updatedNumber);
        }
    }

    public String negateComparator (String comparator) {
        String negatedComparator = null;
        switch (comparator) {
            case "BLE" -> negatedComparator = "BGT";
            case "BGE" -> negatedComparator = "BLT";
            case "BLT" -> negatedComparator = "BGE";
            case "BGT" -> negatedComparator = "BLE";
            case "BNE" -> negatedComparator = "BQE";
            case "BQE" -> negatedComparator = "BNE";
        }
        return negatedComparator;
    }

    public void negateTerceto() {
        String comparator = this.getFirst();
        String negatedComparator = this.negateComparator(comparator);
        this.updateTerceto(1, negatedComparator);
    }
}
