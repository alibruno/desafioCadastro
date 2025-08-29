package dominio;

public class Menu {
    public int opcaoValida(int opcao) throws IllegalArgumentException {
        if (opcao <= 0) {
            throw new IllegalArgumentException();
        }
        return opcao;
    }

    public int opcaoValida(String opcao) throws IllegalArgumentException {
        int opcaoInt = Integer.parseInt(opcao);
        return this.opcaoValida(opcaoInt);
    }

}