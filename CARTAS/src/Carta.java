public class Carta {
    int poder;
    String nome;
    String elemento;

    public Carta(int poder, String nome, String elemento) {
        this.poder = poder;
        this.nome = nome;
        this.elemento = elemento;
    }

    @Override
    public String toString() {
        return nome + " (" + poder + ")";
    }
}