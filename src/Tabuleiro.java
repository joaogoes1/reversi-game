import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Tabuleiro extends JFrame implements ActionListener {

    private static final int LINHA_CIMA = 0;
    private static final int LINHA_BAIXO = 1;
    private static final int COLUNA_FRENTE = 2;
    private static final int COLUNA_TRAS = 3;
    private static final int DIAG_PRIM_FRENTE = 4;
    private static final int DIAG_PRIM_TRAS = 5;
    private static final int DIAG_SEC_FRENTE = 6;
    private static final int DIAG_SEC_TRAS = 7;

    private Pedra[][] tabuleiro;
    private boolean jogadorPreto = true;
    private int jogadas = 0;

    public Tabuleiro() {
        tabuleiro = new Pedra[8][8];

        Container container = getContentPane();
        container.setLayout(new GridLayout(8, 8));

        for (int linha = 0; linha < 8; linha++)
            for (int coluna = 0; coluna < 8; coluna++) {
                String nome = (("btn" + linha) + coluna);
                //Se for as casas centrais, pinta a bordar de vermelho
                if (conferirCoord(linha, coluna)) tabuleiro[linha][coluna] = new Pedra(linha, coluna, nome, true);
                else tabuleiro[linha][coluna] = new Pedra(linha, coluna, nome);
                tabuleiro[linha][coluna].addActionListener(this);
                tabuleiro[linha][coluna].setText((linha + "") + coluna);
                container.add(tabuleiro[linha][coluna]);
            }

        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Pedra botao = (Pedra) e.getSource();


        //Verificar se casa está vazia
        if (botao.getEstado() != Estado.VAZIO) {
            JOptionPane.showMessageDialog(null, "Casa ocupada! Escolha outra");
            return;
        }

        //Configura o jogo nas quatro primeiras jogadas
        if (jogadas < 4)
            //Verifica se está dentro das 4 casas centrais
            if (!conferirCoord(botao.getLinha(), botao.getColuna())) {
                JOptionPane.showMessageDialog(null, "Escolha uma das 4 casas centrais para começar o jogo");
                return;
            } else {
                //Se estiver, faz a alteração
                if (jogadorPreto) botao.setEstado(Estado.PRETO);
                else botao.setEstado(Estado.BRANCO);
                return;
            }

        int[][] pecaAlvo = verificarDirecoes(botao);
        boolean jogadaValida = false;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 2; j++)
                if (pecaAlvo[i][j] == -1)
                    jogadaValida = true;
        if (!jogadaValida)
            JOptionPane.showMessageDialog(null, "JOGADA NÃO VÁLIDA! ESCOLHA OUTRA PEÇA");
        else {
            if ((pecaAlvo[LINHA_CIMA][0] != -1) && (pecaAlvo[LINHA_CIMA][1] != -1))
                virarPecas(botao.getLinha(), botao.getColuna(), pecaAlvo[LINHA_CIMA][0], pecaAlvo[LINHA_CIMA][1]);
            if ((pecaAlvo[LINHA_BAIXO][0] != -1) && (pecaAlvo[LINHA_BAIXO][1] != -1))
                virarPecas(botao.getLinha(), botao.getColuna(), pecaAlvo[LINHA_BAIXO][0], pecaAlvo[LINHA_BAIXO][1]);
            if ((pecaAlvo[COLUNA_FRENTE][0] != -1) && (pecaAlvo[COLUNA_FRENTE][1] != -1))
                virarPecas(botao.getLinha(), botao.getColuna(), pecaAlvo[COLUNA_FRENTE][0], pecaAlvo[COLUNA_FRENTE][1]);
            if ((pecaAlvo[COLUNA_TRAS][0] != -1) && (pecaAlvo[COLUNA_TRAS][1] != -1))
                virarPecas(botao.getLinha(), botao.getColuna(), pecaAlvo[COLUNA_TRAS][0], pecaAlvo[COLUNA_TRAS][1]);
            if ((pecaAlvo[DIAG_PRIM_FRENTE][0] != -1) && (pecaAlvo[DIAG_PRIM_FRENTE][1] != -1))
                virarPecas(botao.getLinha(), botao.getColuna(), pecaAlvo[DIAG_PRIM_FRENTE][0], pecaAlvo[DIAG_PRIM_FRENTE][1]);
            if ((pecaAlvo[DIAG_PRIM_TRAS][0] != -1) && (pecaAlvo[DIAG_PRIM_TRAS][1] != -1))
                virarPecas(botao.getLinha(), botao.getColuna(), pecaAlvo[DIAG_PRIM_TRAS][0], pecaAlvo[DIAG_PRIM_TRAS][1]);
            if ((pecaAlvo[DIAG_SEC_FRENTE][0] != -1) && (pecaAlvo[DIAG_SEC_FRENTE][1] != -1))
                virarPecas(botao.getLinha(), botao.getColuna(), pecaAlvo[DIAG_SEC_FRENTE][0], pecaAlvo[DIAG_SEC_FRENTE][1]);
            if ((pecaAlvo[DIAG_SEC_TRAS][0] != -1) && (pecaAlvo[DIAG_SEC_TRAS][1] != -1))
                virarPecas(botao.getLinha(), botao.getColuna(), pecaAlvo[DIAG_SEC_TRAS][0], pecaAlvo[DIAG_SEC_TRAS][1]);
            jogadorPreto = !jogadorPreto;
            jogadas++;
        }
    }

    private void virarPecas(int linhaInicial, int colunaInicial, int linhaFinal, int colunaFinal) {
        //TODO: Criar algoritmo para virar todas as pedras na trajetoria
        int linInicial = linhaInicial;
        int colInicial = colunaInicial;
        boolean paraCima = (linhaInicial < linhaFinal);
        boolean paraFrente = (colunaInicial < colunaFinal);

        while ((linInicial <= linhaFinal && colInicial <= colunaFinal) || (linInicial >= linhaFinal && colInicial >= colunaFinal)) {
            if (jogadorPreto) tabuleiro[linInicial][colInicial].setEstado(Estado.PRETO);
            else tabuleiro[linInicial][colInicial].setEstado(Estado.BRANCO);
            tabuleiro[linInicial][colInicial].repaint();
            if (paraFrente) colInicial++;
            else colInicial--;
            if (paraCima) linInicial++;
            else linInicial--;
        }
    }

    private int[][] verificarDirecoes(Pedra botao) {
        //TODO: Criar AQUI algaritmo de validação da jogada
        int[][] pecaAlvo = new int[8][2];
        //pecaAlvo[?][0] == LINHA
        //pecaAlvo[?][1] == COLUNA
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 2; j++)
                pecaAlvo[i][j] = -1;


        return pecaAlvo;
    }

    private boolean conferirCoord(int linha, int coluna) {
        return ((linha == 3 && coluna == 3) || (linha == 3 && coluna == 4) || (linha == 4 && coluna == 3) || (linha == 4 && coluna == 4));
    }
}