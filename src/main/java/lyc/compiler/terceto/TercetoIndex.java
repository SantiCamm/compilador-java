package lyc.compiler.terceto;

public class TercetoIndex {

    private Terceto terceto;

    public TercetoIndex(Terceto terceto) {
        this.terceto = terceto;
    }

    public int getTercetoNumber() {
        return terceto.getNumber();
    }

    public String getTerceto() {
        return terceto.getTerceto();
    }
}
