import java.awt.*;
import javax.swing.JButton;

public class Peca extends JButton {

    private static int tamanho = 64;
    private Estado estado;
    public boolean vermelho;

    public Peca() {
        super();
        estado = Estado.VAZIO;
        this.vermelho = false;
    }

    public Peca(boolean vermelho) {
        super();
        estado = Estado.VAZIO;
        this.vermelho = vermelho;
    }
    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(tamanho, tamanho);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Não preenchemos botões vazios.
        if (estado != Estado.VAZIO) {
            if (estado == Estado.BRANCO) {
                g2d.setColor(Color.WHITE);
            } else if (estado == Estado.PRETO) {
                g2d.setColor(Color.BLACK);
            }
            g2d.fillOval(6, 6, getWidth() - 12, getHeight() - 12);
        }
        // Pintamos a borda da peça independente do estado.
        if (vermelho){
            g2d.setColor(Color.RED);
            g2d.drawOval(6, 6, getWidth() - 12, getHeight() - 12);
        } else {
            g2d.setColor(Color.GRAY);
            g2d.drawOval(6, 6, getWidth() - 12, getHeight() - 12);
        }
    }

}