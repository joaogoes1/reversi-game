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
                tabuleiro[linha][coluna] = new Pedra(linha, coluna, nome);
                if (conferirCoord(linha, coluna) == 0) tabuleiro[linha][coluna].setEstado(Estado.PRETO);
                if (conferirCoord(linha, coluna) == 1) tabuleiro[linha][coluna].setEstado(Estado.BRANCO);
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

        /*
            Cria o vetor bidimensional com a verificação das peças nos 8 sentidos possíveis(Cada sentido possuí sua constante)
            vetor[sentido][?]
            vetor[?][0] = linha
            vetor[?][1] = coluna
            Se retornar -1 é pq o movimento é inválido naquela direção
        */
        int[][] pecaAlvo = verificarDirecoes(botao);
        /*
            Cria uma variável para verificar a validade da jogada.
            Percorre o vetor em todos os sentidos.
            Se em nenhum sentido retornar um valor diferente de -1, é por que essa jogada não é válida.
            Se for válida, ele vai virar as peças nos sentidos válidos.
        */
        boolean jogadaValida = false;
        for (int i = 0; i < 8; i++)
            if ((pecaAlvo[i][1] == -1) && (pecaAlvo[i][0] == -1))
                jogadaValida = true;
        if (!jogadaValida)
            JOptionPane.showMessageDialog(null, "JOGADA NÃO VÁLIDA! ESCOLHA OUTRA PEÇA");
        else {
            if ((pecaAlvo[LINHA_CIMA][0] != -1) && (pecaAlvo[LINHA_CIMA][1] != -1))
                virarPecas(botao.getLinha(), botao.getColuna(), pecaAlvo[LINHA_CIMA][0], pecaAlvo[LINHA_CIMA][1], LINHA_CIMA);
            if ((pecaAlvo[LINHA_BAIXO][0] != -1) && (pecaAlvo[LINHA_BAIXO][1] != -1))
                virarPecas(botao.getLinha(), botao.getColuna(), pecaAlvo[LINHA_BAIXO][0], pecaAlvo[LINHA_BAIXO][1], LINHA_BAIXO);
            if ((pecaAlvo[COLUNA_FRENTE][0] != -1) && (pecaAlvo[COLUNA_FRENTE][1] != -1))
                virarPecas(botao.getLinha(), botao.getColuna(), pecaAlvo[COLUNA_FRENTE][0], pecaAlvo[COLUNA_FRENTE][1], COLUNA_FRENTE);
            if ((pecaAlvo[COLUNA_TRAS][0] != -1) && (pecaAlvo[COLUNA_TRAS][1] != -1))
                virarPecas(botao.getLinha(), botao.getColuna(), pecaAlvo[COLUNA_TRAS][0], pecaAlvo[COLUNA_TRAS][1], COLUNA_TRAS);
            if ((pecaAlvo[DIAG_PRIM_FRENTE][0] != -1) && (pecaAlvo[DIAG_PRIM_FRENTE][1] != -1))
                virarPecas(botao.getLinha(), botao.getColuna(), pecaAlvo[DIAG_PRIM_FRENTE][0], pecaAlvo[DIAG_PRIM_FRENTE][1], DIAG_SEC_FRENTE);
            if ((pecaAlvo[DIAG_PRIM_TRAS][0] != -1) && (pecaAlvo[DIAG_PRIM_TRAS][1] != -1))
                virarPecas(botao.getLinha(), botao.getColuna(), pecaAlvo[DIAG_PRIM_TRAS][0], pecaAlvo[DIAG_PRIM_TRAS][1], DIAG_PRIM_TRAS);
            if ((pecaAlvo[DIAG_SEC_FRENTE][0] != -1) && (pecaAlvo[DIAG_SEC_FRENTE][1] != -1))
                virarPecas(botao.getLinha(), botao.getColuna(), pecaAlvo[DIAG_SEC_FRENTE][0], pecaAlvo[DIAG_SEC_FRENTE][1], DIAG_SEC_FRENTE);
            if ((pecaAlvo[DIAG_SEC_TRAS][0] != -1) && (pecaAlvo[DIAG_SEC_TRAS][1] != -1))
                virarPecas(botao.getLinha(), botao.getColuna(), pecaAlvo[DIAG_SEC_TRAS][0], pecaAlvo[DIAG_SEC_TRAS][1], DIAG_SEC_TRAS);
            jogadorPreto = !jogadorPreto;
            jogadas++;
        }
    }

    private void virarPecas(int linhaInicial, int colunaInicial, int linhaFinal, int colunaFinal, int sentido) {
        if (sentido == LINHA_CIMA)
            for (int l = linhaInicial; l >= linhaFinal; l--) {
                if (jogadorPreto) tabuleiro[l][colunaInicial].setEstado(Estado.PRETO);
                else tabuleiro[l][colunaInicial].setEstado(Estado.BRANCO);
                tabuleiro[l][colunaInicial].repaint();
            }
        else if (sentido == LINHA_BAIXO)
            for (int l = linhaInicial; l <= linhaFinal; l++) {
                if (jogadorPreto) tabuleiro[l][colunaInicial].setEstado(Estado.PRETO);
                else tabuleiro[l][colunaInicial].setEstado(Estado.BRANCO);
                tabuleiro[l][colunaInicial].repaint();
            }
        else if (sentido == COLUNA_FRENTE)
            for (int c = colunaInicial; c <= colunaFinal; c++) {
                if (jogadorPreto) tabuleiro[linhaInicial][c].setEstado(Estado.PRETO);
                else tabuleiro[linhaInicial][c].setEstado(Estado.BRANCO);
                tabuleiro[linhaInicial][c].repaint();
            }
        else if (sentido == COLUNA_TRAS)
            for (int c = colunaInicial; c >= colunaFinal; c--) {
                if (jogadorPreto) tabuleiro[c][colunaInicial].setEstado(Estado.PRETO);
                else tabuleiro[c][colunaInicial].setEstado(Estado.BRANCO);
                tabuleiro[linhaInicial][c].repaint();
            }
        else if (sentido == DIAG_PRIM_FRENTE) {
            int l = linhaInicial;
            int c = colunaInicial;
            while (l <= linhaFinal && c <= colunaFinal) {
                if (jogadorPreto) tabuleiro[l][c].setEstado(Estado.PRETO);
                else tabuleiro[l][c].setEstado(Estado.BRANCO);
                tabuleiro[l][c].repaint();
                l++;
                c++;
            }
        } else if (sentido == DIAG_PRIM_TRAS) {
            int l = linhaInicial;
            int c = colunaInicial;
            while (l >= linhaFinal && c >= colunaFinal) {
                if (jogadorPreto) tabuleiro[l][c].setEstado(Estado.PRETO);
                else tabuleiro[l][c].setEstado(Estado.BRANCO);
                tabuleiro[l][c].repaint();
                l--;
                c--;
            }
        } else if (sentido == DIAG_SEC_FRENTE) {
            int l = linhaInicial;
            int c = colunaInicial;
            while (l >= linhaFinal && c <= colunaFinal) {
                if (jogadorPreto) tabuleiro[l][c].setEstado(Estado.PRETO);
                else tabuleiro[l][c].setEstado(Estado.BRANCO);
                tabuleiro[l][c].repaint();
                l--;
                c++;
            }
        } else if (sentido == DIAG_SEC_TRAS) {
            int l = linhaInicial;
            int c = colunaInicial;
            while (l <= linhaFinal && c >= colunaFinal) {
                if (jogadorPreto) tabuleiro[l][c].setEstado(Estado.PRETO);
                else tabuleiro[l][c].setEstado(Estado.BRANCO);
                tabuleiro[l][c].repaint();
                l++;
                c--;
            }
        }
    }

    private int[][] verificarDirecoes(Pedra botao) {
        int linhaFinal, colunaFinal;
        int linhaInicial = linhaFinal = botao.getLinha();
        int colunaInicial = colunaFinal = botao.getColuna();
        int[][] pecaAlvo = new int[8][2];
        Estado jogador;
        Estado outroJogador;
        if (jogadorPreto) {
            jogador = Estado.PRETO;
            outroJogador = Estado.BRANCO;
        } else {
            jogador = Estado.BRANCO;
            outroJogador = Estado.BRANCO;
        }

        //Inicializa todas as posições em -1, para caso não for um movimento válido
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 2; j++)
                pecaAlvo[i][j] = -1;

        //LINHA_CIMA
        //Esse if compara se o estado da peça ao lado é igual ao estado do botão pressionado ou vazio, se for, segue adiante com o código
        //pois a jogada é invalida
        if (linhaInicial > 1)
            for (linhaFinal = (linhaInicial - 1); linhaFinal >= 0; linhaFinal--) {
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == outroJogador) {
                    pecaAlvo[LINHA_CIMA][0] = linhaFinal;
                    pecaAlvo[LINHA_CIMA][1] = colunaFinal;
                }
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == jogador) break;
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == Estado.VAZIO) {
                    pecaAlvo[LINHA_CIMA][0] = -1;
                    pecaAlvo[LINHA_CIMA][1] = -1;
                    break;
                }
            }


        //LINHA_BAIXO
        if (linhaInicial < 6) {
            linhaFinal = linhaInicial;
            colunaFinal = colunaInicial;
            for (linhaFinal = (linhaInicial + 1); linhaFinal < 8; linhaFinal++) {
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == outroJogador) {
                    pecaAlvo[LINHA_BAIXO][0] = linhaFinal;
                    pecaAlvo[LINHA_BAIXO][1] = colunaFinal;
                }
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == jogador) break;
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == Estado.VAZIO) {
                    pecaAlvo[LINHA_BAIXO][0] = -1;
                    pecaAlvo[LINHA_BAIXO][1] = -1;
                    break;
                }
            }
        }

        //COLUNA_FRENTE
        if (colunaInicial < 6) {
            linhaFinal = linhaInicial;
            colunaFinal = colunaInicial;
            for (colunaFinal = (colunaInicial + 1); colunaFinal < 8; colunaFinal++) {
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == outroJogador) {
                    pecaAlvo[COLUNA_FRENTE][0] = linhaFinal;
                    pecaAlvo[COLUNA_FRENTE][1] = colunaFinal;
                }
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == jogador) break;
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == Estado.VAZIO) {
                    pecaAlvo[COLUNA_FRENTE][0] = -1;
                    pecaAlvo[COLUNA_FRENTE][1] = -1;
                    break;
                }
            }
        }

        //COLUNA_TRÁS
        if (colunaInicial > 1) {
            linhaFinal = linhaInicial;
            colunaFinal = colunaInicial;
            for (colunaFinal = (colunaInicial - 1); colunaFinal >= 0; colunaFinal--) {
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == outroJogador) {
                    pecaAlvo[COLUNA_TRAS][0] = linhaFinal;
                    pecaAlvo[COLUNA_TRAS][1] = colunaFinal;
                }
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == jogador) break;
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == Estado.VAZIO) {
                    pecaAlvo[COLUNA_TRAS][0] = -1;
                    pecaAlvo[COLUNA_TRAS][1] = -1;
                    break;
                }
            }
        }


        //DIAG_PRIM_FRENTE
        if ((colunaInicial < 6) && (linhaInicial < 6)) {
            linhaFinal = linhaInicial + 1;
            colunaFinal = colunaInicial + 1;
            while ((linhaFinal < 8) && (colunaFinal < 8)) {
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == outroJogador) {
                    pecaAlvo[DIAG_PRIM_FRENTE][0] = linhaFinal;
                    pecaAlvo[DIAG_PRIM_FRENTE][1] = colunaFinal;
                }
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == jogador) break;
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == Estado.VAZIO) {
                    pecaAlvo[DIAG_PRIM_FRENTE][0] = -1;
                    pecaAlvo[DIAG_PRIM_FRENTE][1] = -1;
                    break;
                }
                linhaFinal++;
                colunaFinal++;
            }
        }

        //DIAG_PRIM_TRÁS
        if ((colunaInicial > 1) && (linhaInicial < 1)) {
            linhaFinal = linhaInicial - 1;
            colunaFinal = colunaInicial - 1;
            while ((linhaFinal > -1) && (colunaFinal > -1)) {
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == outroJogador) {
                    pecaAlvo[DIAG_PRIM_TRAS][0] = linhaFinal;
                    pecaAlvo[DIAG_PRIM_TRAS][1] = colunaFinal;
                }
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == jogador) break;
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == Estado.VAZIO) {
                    pecaAlvo[DIAG_PRIM_TRAS][0] = -1;
                    pecaAlvo[DIAG_PRIM_TRAS][1] = -1;
                    break;
                }
                linhaFinal--;
                colunaFinal--;
            }
        }

        //DIAG_SEC_FRENTE
        if ((colunaInicial < 6) && (linhaInicial > 1)) {
            linhaFinal = linhaInicial - 1;
            colunaFinal = colunaInicial + 1;
            while ((linhaFinal > -1) && (colunaFinal < 8)) {
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == outroJogador) {
                    pecaAlvo[DIAG_SEC_FRENTE][0] = linhaFinal;
                    pecaAlvo[DIAG_SEC_FRENTE][1] = colunaFinal;
                }
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == jogador) break;
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == Estado.VAZIO) {
                    pecaAlvo[DIAG_SEC_FRENTE][0] = -1;
                    pecaAlvo[DIAG_SEC_FRENTE][1] = -1;
                    break;
                }
                linhaFinal--;
                colunaFinal++;
            }
        }


        //DIAG_SEC_TRÁS
        if ((colunaInicial > 1) && (linhaInicial < 6)) {
            linhaFinal = linhaInicial + 1;
            colunaFinal = colunaInicial - 1;
            while ((linhaFinal < 8) && (colunaFinal > -1)) {
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == outroJogador) {
                    pecaAlvo[DIAG_SEC_TRAS][0] = linhaFinal;
                    pecaAlvo[DIAG_SEC_TRAS][1] = colunaFinal;
                }
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == jogador) break;
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == Estado.VAZIO) {
                    pecaAlvo[DIAG_SEC_TRAS][0] = -1;
                    pecaAlvo[DIAG_SEC_TRAS][1] = -1;
                    break;
                }
                linhaFinal++;
                colunaFinal--;
            }
        }


        return pecaAlvo;
    }

    private int conferirCoord(int linha, int coluna) {
        if ((linha == 3 && coluna == 3) || (linha == 3 && coluna == 4))
            return 0;
        if ((linha == 4 && coluna == 3) || (linha == 4 && coluna == 4))
            return 1;
        return -1;
    }
}