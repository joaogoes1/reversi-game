public class Pedra extends Peca{

    private int linha;
    private int coluna;
    private String nome;

    public Pedra(){
        super();
    }

    public Pedra(int linha, int coluna, String nome){
        this();
        setColuna(coluna);
        setLinha(linha);
        setNome(nome);
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}