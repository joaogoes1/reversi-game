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
                tabuleiro[linha][coluna] = new Pedra(linha, coluna, nome);
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

        if (botao.getEstado() == Estado.VAZIO) {
            if (jogadorPreto) botao.setEstado(Estado.BRANCO);
            else botao.setEstado(Estado.PRETO);

            jogadorPreto = !jogadorPreto;
        } else JOptionPane.showMessageDialog(null, "Casa ocupada! Escolha outra");
    }

    private boolean conferirCoord(int linha, int coluna) {
        return ((linha == 4 && coluna == 4) || (linha == 4 && coluna == 5) || (linha == 5 && coluna == 4) || (linha == 5 && coluna == 5));
    }
}