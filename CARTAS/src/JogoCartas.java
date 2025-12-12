import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class JogoCartas extends JFrame {

    private DeckAVL deckMao = new DeckAVL();
    private PainelMesa painelMesa = new PainelMesa();
    private Random random = new Random();

    // Elementos do jogo
    private String[] elementos = {"Fogo", "Agua", "Terra", "Ar"};
    private String[] nomes = {"Dragão", "Mago", "Golem", "Grifo", "Espectro", "Cavaleiro", "Fada"};

    // Campos de input
    private JTextField txtPoderRemover = new JTextField(5);

    public JogoCartas() {
        setTitle("Elemental Duel - Gerenciador de Deck AVL");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Painel de Controles (Topo) ---
        JPanel painelControles = new JPanel();
        painelControles.setBackground(new Color(50, 50, 50)); // Cinza escuro

        JButton btnComprar = new JButton("Comprar Carta Aleatória");
        btnComprar.setBackground(new Color(34, 139, 34)); // Verde
        btnComprar.setForeground(Color.WHITE);

        JButton btnJogar = new JButton("Jogar Carta (Remover pelo Poder)");
        btnJogar.setBackground(new Color(178, 34, 34)); // Vermelho
        btnJogar.setForeground(Color.WHITE);

        JLabel lblInfo = new JLabel("Poder da Carta:");
        lblInfo.setForeground(Color.WHITE);

        painelControles.add(btnComprar);
        painelControles.add(Box.createHorizontalStrut(20)); // Espaçamento
        painelControles.add(lblInfo);
        painelControles.add(txtPoderRemover);
        painelControles.add(btnJogar);

        add(painelControles, BorderLayout.NORTH);
        add(new JScrollPane(painelMesa), BorderLayout.CENTER);

        // Botão Gera carta aleatória
        btnComprar.addActionListener(e -> {
            int poder = random.nextInt(99) + 1; // Poder entre 1 e 99
            String elemento = elementos[random.nextInt(elementos.length)];
            String nome = nomes[random.nextInt(nomes.length)];


            deckMao.inserir(new Carta(poder, nome, elemento));
            painelMesa.repaint();
        });

        // Botão Jogar (Remove a carta da árvore)
        btnJogar.addActionListener(e -> {
            try {
                int poder = Integer.parseInt(txtPoderRemover.getText());
                deckMao.remover(poder);
                painelMesa.repaint();
                txtPoderRemover.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Digite o valor de Poder da carta!");
            }
        });

        // Iniciar com algumas cartas
        deckMao.inserir(new Carta(50, "Dragão", "Fogo"));
        deckMao.inserir(new Carta(20, "Mago", "Agua"));
        deckMao.inserir(new Carta(80, "Golem", "Terra"));
    }


    private class PainelMesa extends JPanel {

        private final int LARGURA_CARTA = 60;
        private final int ALTURA_CARTA = 90;

        public PainelMesa() {
            setBackground(new Color(30, 30, 30)); // Fundo "Mesa de Jogo" escura
            setPreferredSize(new Dimension(2000, 1000));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (deckMao.getRaiz() != null) {
                desenharCartaNo(g2d, deckMao.getRaiz(), getWidth() / 2, 50, getWidth() / 4);
            }
        }

        private void desenharCartaNo(Graphics2D g, No no, int x, int y, int xOffset) {

            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(2));

            if (no.esquerda != null) {
                int xEsq = x - xOffset;
                int yEsq = y + 120;
                g.drawLine(x + LARGURA_CARTA/2, y + ALTURA_CARTA, xEsq + LARGURA_CARTA/2, yEsq);
                desenharCartaNo(g, no.esquerda, xEsq, yEsq, xOffset / 2);
            }

            if (no.direita != null) {
                int xDir = x + xOffset;
                int yDir = y + 120;
                g.drawLine(x + LARGURA_CARTA/2, y + ALTURA_CARTA, xDir + LARGURA_CARTA/2, yDir);
                desenharCartaNo(g, no.direita, xDir, yDir, xOffset / 2);
            }

            // 2. Definir cor baseada no Elemento
            Color corFundo;
            switch (no.carta.elemento) {
                case "Fogo": corFundo = new Color(255, 100, 100); break; // Vermelho claro
                case "Agua": corFundo = new Color(100, 150, 255); break; // Azul claro
                case "Terra": corFundo = new Color(100, 200, 100); break; // Verde claro
                case "Ar": corFundo = new Color(240, 230, 140); break; // Amarelo
                default: corFundo = Color.LIGHT_GRAY;
            }

            // 3. Desenhar o Retângulo da Carta
            g.setColor(corFundo);
            g.fillRoundRect(x, y, LARGURA_CARTA, ALTURA_CARTA, 10, 10);

            g.setColor(Color.WHITE); // Borda
            g.drawRoundRect(x, y, LARGURA_CARTA, ALTURA_CARTA, 10, 10);

            // 4. Texto da Carta
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 14));

            // Desenhar PODER no centro
            String txtPoder = String.valueOf(no.carta.poder);
            FontMetrics fm = g.getFontMetrics();
            int textoX = x + (LARGURA_CARTA - fm.stringWidth(txtPoder)) / 2;
            g.drawString(txtPoder, textoX, y + ALTURA_CARTA / 2);

            // Desenhar NOME pequeno em cima
            g.setFont(new Font("Arial", Font.PLAIN, 10));
            String txtNome = no.carta.nome;
            int nomeX = x + (LARGURA_CARTA - g.getFontMetrics().stringWidth(txtNome)) / 2;
            g.drawString(txtNome, nomeX, y + 20);

            // Desenhar Elemento embaixo
            String txtElem = no.carta.elemento;
            int elemX = x + (LARGURA_CARTA - g.getFontMetrics().stringWidth(txtElem)) / 2;
            g.drawString(txtElem, elemX, y + ALTURA_CARTA - 10);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JogoCartas().setVisible(true);
        });
    }
}