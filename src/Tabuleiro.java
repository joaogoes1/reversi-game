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

    private JLabel placarPRETO;
    private JLabel placarBRANCO;
    private JLabel vez;

    private int preto = 0;
    private int branco = 0;

    private Pedra[][] tabuleiro;
    private boolean jogadorPreto = true;

    public Tabuleiro() {
        tabuleiro = new Pedra[8][8];

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        JPanel placar = new JPanel(new GridLayout(1, 3));
        JPanel jogo = new JPanel(new GridLayout(8, 8));
        container.add(placar, BorderLayout.NORTH);
        container.add(jogo, BorderLayout.CENTER);

        criarPlacar(placar);

        for (int linha = 0; linha < 8; linha++)
            for (int coluna = 0; coluna < 8; coluna++) {
                String nome = (("btn" + linha) + coluna);
                tabuleiro[linha][coluna] = new Pedra(linha, coluna, nome);
                if (conferirCoord(linha, coluna) == 0) tabuleiro[linha][coluna].setEstado(Estado.PRETO);
                if (conferirCoord(linha, coluna) == 1) tabuleiro[linha][coluna].setEstado(Estado.BRANCO);
                tabuleiro[linha][coluna].addActionListener(this);
                tabuleiro[linha][coluna].setText((linha + "") + coluna);
                jogo.add(tabuleiro[linha][coluna]);
            }

        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void criarPlacar(JPanel placar) {
        placarPRETO = new JLabel("PRETO: 0");
        placar.add(placarPRETO);
        placarBRANCO = new JLabel("BRANCO: 0");
        placar.add(placarBRANCO);
        vez = new JLabel("Vez: PRETO");
        placar.add(vez);
        JButton zerar = new JButton("Zerar");
        zerar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placarPRETO.setText("PRETO: 0");
                preto = 0;
                placarBRANCO.setText("BRANCO: 0");
                branco = 0;
                vez.setText("Vez: PRETO");
                jogadorPreto = true;
                reiniciaTabuleiro();
            }
        });
        placar.add(zerar);
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
        int[][] pecaAlvo = verificarDirecoes(botao.getLinha(), botao.getColuna());
        /*
            Cria uma variável para verificar a validade da jogada.
            Percorre o vetor em todos os sentidos.
            Se em nenhum sentido retornar um valor diferente de -1, é por que essa jogada não é válida.
            Se for válida, ele vai virar as peças nos sentidos válidos.
         */
        boolean jogadaValida = validarJogadas(pecaAlvo);

        if (!jogadaValida) {
            JOptionPane.showMessageDialog(null, "JOGADA NÃO VÁLIDA! ESCOLHA OUTRA PEÇA");
            if (!verificarPossibilidade())
                acabouJogo();
        } else {
            if ((pecaAlvo[LINHA_CIMA][0] != -1) && (pecaAlvo[LINHA_CIMA][1] != -1))
                virarPecas(botao.getLinha(), botao.getColuna(), pecaAlvo[LINHA_CIMA][0], pecaAlvo[LINHA_CIMA][1], LINHA_CIMA);
            if ((pecaAlvo[LINHA_BAIXO][0] != -1) && (pecaAlvo[LINHA_BAIXO][1] != -1))
                virarPecas(botao.getLinha(), botao.getColuna(), pecaAlvo[LINHA_BAIXO][0], pecaAlvo[LINHA_BAIXO][1], LINHA_BAIXO);
            if ((pecaAlvo[COLUNA_FRENTE][0] != -1) && (pecaAlvo[COLUNA_FRENTE][1] != -1))
                virarPecas(botao.getLinha(), botao.getColuna(), pecaAlvo[COLUNA_FRENTE][0], pecaAlvo[COLUNA_FRENTE][1], COLUNA_FRENTE);
            if ((pecaAlvo[COLUNA_TRAS][0] != -1) && (pecaAlvo[COLUNA_TRAS][1] != -1))
                virarPecas(botao.getLinha(), botao.getColuna(), pecaAlvo[COLUNA_TRAS][0], pecaAlvo[COLUNA_TRAS][1], COLUNA_TRAS);
            if ((pecaAlvo[DIAG_PRIM_FRENTE][0] != -1) && (pecaAlvo[DIAG_PRIM_FRENTE][1] != -1))
                virarPecas(botao.getLinha(), botao.getColuna(), pecaAlvo[DIAG_PRIM_FRENTE][0], pecaAlvo[DIAG_PRIM_FRENTE][1], DIAG_PRIM_FRENTE);
            if ((pecaAlvo[DIAG_PRIM_TRAS][0] != -1) && (pecaAlvo[DIAG_PRIM_TRAS][1] != -1))
                virarPecas(botao.getLinha(), botao.getColuna(), pecaAlvo[DIAG_PRIM_TRAS][0], pecaAlvo[DIAG_PRIM_TRAS][1], DIAG_PRIM_TRAS);
            if ((pecaAlvo[DIAG_SEC_FRENTE][0] != -1) && (pecaAlvo[DIAG_SEC_FRENTE][1] != -1))
                virarPecas(botao.getLinha(), botao.getColuna(), pecaAlvo[DIAG_SEC_FRENTE][0], pecaAlvo[DIAG_SEC_FRENTE][1], DIAG_SEC_FRENTE);
            if ((pecaAlvo[DIAG_SEC_TRAS][0] != -1) && (pecaAlvo[DIAG_SEC_TRAS][1] != -1))
                virarPecas(botao.getLinha(), botao.getColuna(), pecaAlvo[DIAG_SEC_TRAS][0], pecaAlvo[DIAG_SEC_TRAS][1], DIAG_SEC_TRAS);

            jogadorPreto = !jogadorPreto;
            if (jogadorPreto)
                vez.setText("PRETO");
            else
                vez.setText("BRANCO");
        }


        // Verificar se todas as peças do tabuleiro estão preenchidas
        // Se estiver completo, chama o método acabou o jogo
        for (int l = 0; l < 8; l++)
            for (int c = 0; c < 8; c++)
                if (tabuleiro[l][c].getEstado() == Estado.VAZIO)
                    break;
                else
                    acabouJogo();
    }

    private boolean validarJogadas(int[][] pecaAlvo) {
        for (int i = 0; i < 8; i++) {
            if ((pecaAlvo[i][1] != -1) && (pecaAlvo[i][0] != -1)) {
                return true;
            }
        }
        return false;
    }

    private void virarPecas(int linhaInicial, int colunaInicial, int linhaFinal, int colunaFinal, int sentido) {
        switch (sentido) {
            case LINHA_CIMA:
                for (int l = linhaInicial; l >= linhaFinal; l--) {
                    if (jogadorPreto) {
                        tabuleiro[l][colunaInicial].setEstado(Estado.PRETO);
                    } else {
                        tabuleiro[l][colunaInicial].setEstado(Estado.BRANCO);
                    }
                    tabuleiro[l][colunaInicial].repaint();
                }
                break;
            case LINHA_BAIXO:
                for (int l = linhaInicial; l <= linhaFinal; l++) {
                    if (jogadorPreto) {
                        tabuleiro[l][colunaInicial].setEstado(Estado.PRETO);
                    } else {
                        tabuleiro[l][colunaInicial].setEstado(Estado.BRANCO);
                    }
                    tabuleiro[l][colunaInicial].repaint();
                }
                break;
            case COLUNA_FRENTE:
                for (int c = colunaInicial; c <= colunaFinal; c++) {
                    if (jogadorPreto) {
                        tabuleiro[linhaInicial][c].setEstado(Estado.PRETO);
                    } else {
                        tabuleiro[linhaInicial][c].setEstado(Estado.BRANCO);
                    }
                    tabuleiro[linhaInicial][c].repaint();
                }
                break;
            case COLUNA_TRAS:
                for (int c = colunaInicial; c >= colunaFinal; c--) {
                    if (jogadorPreto) {
                        tabuleiro[linhaInicial][c].setEstado(Estado.PRETO);
                    } else {
                        tabuleiro[linhaInicial][c].setEstado(Estado.BRANCO);
                    }
                    tabuleiro[linhaInicial][c].repaint();
                }
                break;
            case DIAG_PRIM_FRENTE: {
                int l = linhaInicial;
                int c = colunaInicial;
                while (l <= linhaFinal && c <= colunaFinal) {
                    if (jogadorPreto) {
                        tabuleiro[l][c].setEstado(Estado.PRETO);
                    } else {
                        tabuleiro[l][c].setEstado(Estado.BRANCO);
                    }
                    tabuleiro[l][c].repaint();
                    l++;
                    c++;
                }
                break;
            }
            case DIAG_PRIM_TRAS: {
                int l = linhaInicial;
                int c = colunaInicial;
                while (l >= linhaFinal && c >= colunaFinal) {
                    if (jogadorPreto) {
                        tabuleiro[l][c].setEstado(Estado.PRETO);
                    } else {
                        tabuleiro[l][c].setEstado(Estado.BRANCO);
                    }
                    tabuleiro[l][c].repaint();
                    l--;
                    c--;
                }
                break;
            }
            case DIAG_SEC_FRENTE: {
                int l = linhaInicial;
                int c = colunaInicial;
                while (l >= linhaFinal && c <= colunaFinal) {
                    if (jogadorPreto) {
                        tabuleiro[l][c].setEstado(Estado.PRETO);
                    } else {
                        tabuleiro[l][c].setEstado(Estado.BRANCO);
                    }
                    tabuleiro[l][c].repaint();
                    l--;
                    c++;
                }
                break;
            }
            case DIAG_SEC_TRAS: {
                int l = linhaInicial;
                int c = colunaInicial;
                while (l <= linhaFinal && c >= colunaFinal) {
                    if (jogadorPreto) {
                        tabuleiro[l][c].setEstado(Estado.PRETO);
                    } else {
                        tabuleiro[l][c].setEstado(Estado.BRANCO);
                    }
                    tabuleiro[l][c].repaint();
                    l++;
                    c--;
                }
                break;
            }
        }
    }

    private int[][] verificarDirecoes(int linha, int coluna) {
        int linhaFinal, colunaFinal;
        int[][] pecaAlvo = new int[8][2];
        Estado jogador;
        Estado outroJogador;
        if (jogadorPreto) {
            jogador = Estado.PRETO;
            outroJogador = Estado.BRANCO;
        } else {
            jogador = Estado.BRANCO;
            outroJogador = Estado.PRETO;
        }

        //Inicializa todas as posições em -1, para caso não for um movimento válido
        for (int i = 0; i < 8; i++) {
            pecaAlvo[i][0] = -1;
            pecaAlvo[i][1] = -1;
        }

        //LINHA_CIMA
        //Esse if compara se o estado da peça ao lado é igual ao estado do botão pressionado ou vazio, se for, segue adiante com o código
        //pois a jogada é invalida
        if (linha > 1) {
            colunaFinal = coluna;
            for (linhaFinal = (linha - 1); linhaFinal >= 0; linhaFinal--) {
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == outroJogador) {
                    pecaAlvo[LINHA_CIMA][0] = linhaFinal;
                    pecaAlvo[LINHA_CIMA][1] = colunaFinal;
                } else if (tabuleiro[linhaFinal][colunaFinal].getEstado() == jogador) {
                    break;
                } else if (tabuleiro[linhaFinal][colunaFinal].getEstado() == Estado.VAZIO) {
                    pecaAlvo[LINHA_CIMA][0] = -1;
                    pecaAlvo[LINHA_CIMA][1] = -1;
                    break;
                }
            }
        }

        //LINHA_BAIXO
        if (linha < 6) {
            linhaFinal = linha;
            colunaFinal = coluna;
            for (linhaFinal = (linha + 1); linhaFinal < 8; linhaFinal++) {
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == outroJogador) {
                    pecaAlvo[LINHA_BAIXO][0] = linhaFinal;
                    pecaAlvo[LINHA_BAIXO][1] = colunaFinal;
                } else if (tabuleiro[linhaFinal][colunaFinal].getEstado() == jogador) {
                    break;
                } else if (tabuleiro[linhaFinal][colunaFinal].getEstado() == Estado.VAZIO) {
                    pecaAlvo[LINHA_BAIXO][0] = -1;
                    pecaAlvo[LINHA_BAIXO][1] = -1;
                    break;
                }
            }
        }

        //COLUNA_FRENTE
        if (coluna < 6) {
            linhaFinal = linha;
            colunaFinal = coluna;
            for (colunaFinal = (coluna + 1); colunaFinal < 8; colunaFinal++) {
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == outroJogador) {
                    pecaAlvo[COLUNA_FRENTE][0] = linhaFinal;
                    pecaAlvo[COLUNA_FRENTE][1] = colunaFinal;
                } else if (tabuleiro[linhaFinal][colunaFinal].getEstado() == jogador) {
                    break;
                } else if (tabuleiro[linhaFinal][colunaFinal].getEstado() == Estado.VAZIO) {
                    pecaAlvo[COLUNA_FRENTE][0] = -1;
                    pecaAlvo[COLUNA_FRENTE][1] = -1;
                    break;
                }
            }
        }

        //COLUNA_TRÁS
        if (coluna > 1) {
            linhaFinal = linha;
            colunaFinal = coluna;
            for (colunaFinal = (coluna - 1); colunaFinal >= 0; colunaFinal--) {
                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == outroJogador) {
                    pecaAlvo[COLUNA_TRAS][0] = linhaFinal;
                    pecaAlvo[COLUNA_TRAS][1] = colunaFinal;
                } else if (tabuleiro[linhaFinal][colunaFinal].getEstado() == jogador) {
                    break;
                } else if (tabuleiro[linhaFinal][colunaFinal].getEstado() == Estado.VAZIO) {
                    pecaAlvo[COLUNA_TRAS][0] = -1;
                    pecaAlvo[COLUNA_TRAS][1] = -1;
                    break;
                }
            }
        }

        //DIAG_PRIM_FRENTE
        if ((coluna < 6) && (linha < 6)) {
            linhaFinal = linha + 1;
            colunaFinal = coluna + 1;
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
        if ((coluna > 1) && (linha > 1)) {
            linhaFinal = linha - 1;
            colunaFinal = coluna - 1;
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
        if ((coluna < 6) && (linha > 1)) {
            linhaFinal = linha - 1;
            colunaFinal = coluna + 1;
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
        if ((coluna > 1) && (linha < 6)) {
            linhaFinal = linha + 1;
            colunaFinal = coluna - 1;
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

    private boolean verificarPossibilidade() {
        int linhaFinal;
        int colunaFinal;
        Estado jogadorAtual = null;
        Estado jogadorAdversario = null;

        if (jogadorPreto) {
            jogadorAtual = Estado.PRETO;
            jogadorAdversario = Estado.BRANCO;
        }
        else {
            jogadorAtual = Estado.BRANCO;
            jogadorAdversario = Estado.PRETO;
        }

        for (int linha = 0; linha < 8; linha++)
            for (int coluna = 0; coluna < 8; coluna++)
                if (tabuleiro[linha][coluna].getEstado() == jogadorAtual) {

                    //LINHA_CIMA
                    if (linha > 1)
                        if (tabuleiro[linha - 1][coluna].getEstado() == jogadorAdversario)
                            for (linhaFinal = (linha - 2); linhaFinal >= 0; linhaFinal--)
                                if (tabuleiro[linhaFinal][coluna].getEstado() == Estado.VAZIO)
                                    return true;

                    //LINHA_BAIXO
                    if (linha < 6)
                        if (tabuleiro[linha + 1][coluna].getEstado() == jogadorAdversario)
                            for (linhaFinal = (linha + 2); linhaFinal < 8; linhaFinal++)
                                if (tabuleiro[linhaFinal][coluna].getEstado() == Estado.VAZIO)
                                    return true;

                    //COLUNA_FRENTE
                    if (coluna < 6)
                        if (tabuleiro[linha][coluna + 1].getEstado() == jogadorAdversario)
                            for (colunaFinal = (coluna + 2); colunaFinal < 8; colunaFinal++)
                                if (tabuleiro[linha][colunaFinal].getEstado() == Estado.VAZIO)
                                    return true;

                    //COLUNA_TRÁS
                    if (coluna > 1)
                        if (tabuleiro[linha][coluna - 1].getEstado() == jogadorAdversario)
                            for (colunaFinal = (coluna - 2); colunaFinal >= 0; colunaFinal--)
                                if (tabuleiro[linha][colunaFinal].getEstado() == Estado.VAZIO)
                                    return true;

                    //DIAG_PRIM_FRENTE
                    if ((coluna < 6) && (linha < 6))
                        if (tabuleiro[linha + 1][coluna + 1].getEstado() == jogadorAdversario)
                            for (colunaFinal = (coluna + 2), linhaFinal = (linha + 2); colunaFinal < 8 && linhaFinal < 8; colunaFinal++, linhaFinal++)
                                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == Estado.VAZIO)
                                    return true;


                    //DIAG_PRIM_TRÁS
                    if ((coluna > 1) && (linha > 1))
                        if (tabuleiro[linha - 1][coluna - 1].getEstado() == jogadorAdversario)
                            for (colunaFinal = (coluna - 2), linhaFinal = (linha - 2); colunaFinal >= 0 && linhaFinal >= 0; colunaFinal--, linhaFinal--)
                                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == Estado.VAZIO)
                                    return true;

                    //DIAG_SEC_FRENTE
                    if ((coluna < 6) && (linha > 1))
                        if (tabuleiro[linha - 1][coluna + 1].getEstado() == jogadorAdversario)
                            for (colunaFinal = (coluna + 2), linhaFinal = (linha - 2); colunaFinal < 8 && linhaFinal >= 0; colunaFinal++, linhaFinal--)
                                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == Estado.VAZIO)
                                    return true;


                    //DIAG_SEC_TRÁS
                    if ((coluna > 1) && (linha < 6))
                        if (tabuleiro[linha + 1][coluna - 1].getEstado() == jogadorAdversario)
                            for (colunaFinal = (coluna - 2), linhaFinal = (linha + 2); colunaFinal >= 0 && linhaFinal < 8; colunaFinal--, linhaFinal++)
                                if (tabuleiro[linhaFinal][colunaFinal].getEstado() == Estado.VAZIO)
                                    return true;


                }
        return false;
    }

    private int conferirCoord(int linha, int coluna) {
        if ((linha == 3 && coluna == 3) || (linha == 3 && coluna == 4)) {
            return 0;
        }
        if ((linha == 4 && coluna == 3) || (linha == 4 && coluna == 4)) {
            return 1;
        }
        return -1;
    }

    private void acabouJogo() {
        int preto = 0;
        int branco = 0;

        for (int l = 0; l < 8; l++)
            for (int c = 0; c < 8; c++)
                if (tabuleiro[l][c].getEstado() == Estado.PRETO)
                    preto++;
                else if (tabuleiro[l][c].getEstado() == Estado.BRANCO)
                    branco++;


        if (preto > branco) {
            JOptionPane.showMessageDialog(null, "O jogador PRETO venceu");
            this.preto++;
            placarPRETO.setText("PRETO: " + this.preto);
        } else {
            JOptionPane.showMessageDialog(null, "O jogador BRANCO venceu");
            this.branco++;
            placarBRANCO.setText("PRETO: " + this.branco);
        }


        reiniciaTabuleiro();
    }

    private void reiniciaTabuleiro() {
        // Reinicia o tabuleiro
        for (int l = 0; l < 8; l++) {
            for (int c = 0; c < 8; c++) {
                tabuleiro[l][c].setEstado(Estado.VAZIO);
                if (conferirCoord(l, c) == 0) tabuleiro[l][c].setEstado(Estado.PRETO);
                if (conferirCoord(l, c) == 1) tabuleiro[l][c].setEstado(Estado.BRANCO);
                tabuleiro[l][c].repaint();
            }
        }
        jogadorPreto = true;
    }
}
