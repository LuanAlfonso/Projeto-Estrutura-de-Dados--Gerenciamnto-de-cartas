public class No {
    Carta carta;
    No esquerda, direita;
    int altura;

    public No(Carta carta) {
        this.carta = carta;
        this.altura = 1;
    }
}