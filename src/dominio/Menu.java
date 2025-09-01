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

    public String iterarSobreAtributosPet(int ordemAtributo, String atributoString) {
        switch (ordemAtributo) {
            case 0:
                return Pet.validarNomeCompleto(atributoString);
            case 1:
                return Pet.validarTipo(atributoString);
            case 2:
                return Pet.validarSexo(atributoString);
            case 3:
                return Pet.validarEndereco(atributoString);
            case 4:
                return Pet.validarIdade(atributoString);
            case 5:
                return Pet.validarPeso(atributoString);
            case 6:
                return Pet.validarRaca(atributoString);
            default:
                throw new PetAtributoInvalidoExeception("Ocorreu um erro durante a iteração.");
        }
    }
}