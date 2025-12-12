public class DeckAVL {
    private No raiz;

    public No getRaiz() {
        return raiz;
    }

    private int altura(No N) {
        if (N == null) return 0;
        return N.altura;
    }

    private int max(int a, int b) {
        return (a > b) ? a : b;
    }

    private No rotacaoDireita(No y) {
        No x = y.esquerda;
        No T2 = x.direita;
        x.direita = y;
        y.esquerda = T2;
        y.altura = max(altura(y.esquerda), altura(y.direita)) + 1;
        x.altura = max(altura(x.esquerda), altura(x.direita)) + 1;
        return x;
    }

    private No rotacaoEsquerda(No x) {
        No y = x.direita;
        No T2 = y.esquerda;
        y.esquerda = x;
        x.direita = T2;
        x.altura = max(altura(x.esquerda), altura(x.direita)) + 1;
        y.altura = max(altura(y.esquerda), altura(y.direita)) + 1;
        return y;
    }

    private int getFatorBalanceamento(No N) {
        if (N == null) return 0;
        return altura(N.esquerda) - altura(N.direita);
    }

    public void inserir(Carta carta) {
        raiz = inserirRec(raiz, carta);
    }

    private No inserirRec(No no, Carta carta) {
        if (no == null) return new No(carta);

        // Ordena pelo PODER da carta
        if (carta.poder < no.carta.poder)
            no.esquerda = inserirRec(no.esquerda, carta);
        else if (carta.poder > no.carta.poder)
            no.direita = inserirRec(no.direita, carta);
        else return no; // Não permite poderes duplicados (simplificação)

        no.altura = 1 + max(altura(no.esquerda), altura(no.direita));
        int balance = getFatorBalanceamento(no);

        if (balance > 1 && carta.poder < no.esquerda.carta.poder)
            return rotacaoDireita(no);
        if (balance < -1 && carta.poder > no.direita.carta.poder)
            return rotacaoEsquerda(no);
        if (balance > 1 && carta.poder > no.esquerda.carta.poder) {
            no.esquerda = rotacaoEsquerda(no.esquerda);
            return rotacaoDireita(no);
        }
        if (balance < -1 && carta.poder < no.direita.carta.poder) {
            no.direita = rotacaoDireita(no.direita);
            return rotacaoEsquerda(no);
        }
        return no;
    }

    public void remover(int poder) {
        raiz = removerRec(raiz, poder);
    }

    private No menorValorNo(No no) {
        No atual = no;
        while (atual.esquerda != null) atual = atual.esquerda;
        return atual;
    }

    private No removerRec(No no, int poder) {
        if (no == null) return no;
        if (poder < no.carta.poder) no.esquerda = removerRec(no.esquerda, poder);
        else if (poder > no.carta.poder) no.direita = removerRec(no.direita, poder);
        else {
            if ((no.esquerda == null) || (no.direita == null)) {
                No temp = (no.esquerda != null) ? no.esquerda : no.direita;
                if (temp == null) { temp = no; no = null; }
                else no = temp;
            } else {
                No temp = menorValorNo(no.direita);
                no.carta = temp.carta;
                no.direita = removerRec(no.direita, temp.carta.poder);
            }
        }
        if (no == null) return no;

        no.altura = max(altura(no.esquerda), altura(no.direita)) + 1;
        int balance = getFatorBalanceamento(no);

        if (balance > 1 && getFatorBalanceamento(no.esquerda) >= 0)
            return rotacaoDireita(no);
        if (balance > 1 && getFatorBalanceamento(no.esquerda) < 0) {
            no.esquerda = rotacaoEsquerda(no.esquerda);
            return rotacaoDireita(no);
        }
        if (balance < -1 && getFatorBalanceamento(no.direita) <= 0)
            return rotacaoEsquerda(no);
        if (balance < -1 && getFatorBalanceamento(no.direita) > 0) {
            no.direita = rotacaoDireita(no.direita);
            return rotacaoEsquerda(no);
        }
        return no;
    }
}