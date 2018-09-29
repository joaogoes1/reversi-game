import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Tabuleiro extends JFrame implements ActionListener {

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
                container.add(tabuleiro[linha][coluna]);
            }

        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Pedra botao = (Pedra) e.getSource();

        if (jogadas < 4)
            if (!conferirCoord(botao.getLinha(), botao.getColuna())) {
                JOptionPane.showMessageDialog(null, "Escolha uma das 4 casas centrais para começar o jogo");
                return;
            }

        //TODO: Arrumar o método jogadaValida()
        if (jogadaValida(botao)){
            if (jogadorPreto) botao.setEstado(Estado.PRETO);
            else botao.setEstado(Estado.BRANCO);

            //TODO: Arramar o método virarPecas()
            virarPecas();

            jogadorPreto = !jogadorPreto;
            jogadas++;
        }
    }

    private void virarPecas() {
        //TODO: Criar algoritmo para virar todas as pedras na trajetoria
    }

    private boolean jogadaValida(Pedra botao) {
        //Verificar se casa está vazia
        if (botao.getEstado() != Estado.VAZIO) {
            JOptionPane.showMessageDialog(null, "Casa ocupada! Escolha outra");
            return false;
        }

        //TODO: Criar AQUI algaritmo de validação da jogada

        return (true);
    }

    private boolean conferirCoord(int linha, int coluna) {
        return ((linha == 3 && coluna == 3) || (linha == 3 && coluna == 4) || (linha == 4 && coluna == 3) || (linha == 4 && coluna == 4));
    }
}