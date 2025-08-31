package dominio;

import java.util.Arrays;

public class Pet {
    private String nomeCompleto;
    private Tipo tipo;
    private Sexo sexo;
    private String endereco;
    private String idade;
    private String peso;
    private static final String NAO_INFORMADO = "NÃO INFORMADO";
    private String raca;

    public Pet(String nomeCompleto, String tipo, String sexo, String endereco, String idade, String peso, String raca) {
        this.nomeCompleto = validarNomeCompleto(nomeCompleto);
        this.tipo = Tipo.converterStringToTipo(tipo);
        this.sexo = Sexo.converterStringToSexo(sexo);
        this.endereco = validarEndereco(endereco);
        this.idade = validarIdade(idade);
        this.peso = validarPeso(peso);
        this.raca = validarRaca(raca);
    }

    /**
     * Constrói um novo Pet a partir de um array de 7 posições. <br>
     * Indices: [0] nomeCompleto, [1] tipo, [2] sexo, [3] endereco, [4] idade, [5] peso, [6] raca.
     *
     * @param atributos Atributos de Pet.
     *
     */
    public Pet(String[] atributos) {
        this(atributos[0], atributos[1], atributos[2], atributos[3], atributos[4], atributos[5], atributos[6]);
    }

    public static String validarNomeCompleto(String nomeCompleto) {
        String regexNomeValido = "[A-Za-z]+( [A-Za-z]+)+";
        if (nomeCompleto.matches(regexNomeValido)) {
            return nomeCompleto;
        }
        if (nomeCompleto.isEmpty() || nomeCompleto.equals(NAO_INFORMADO)) {
            return NAO_INFORMADO;
        }
        throw new PetAtributoInvalidoExeception("Nome completo inválido. Digite o nome e sobrenome.");
    }

    public static String validarTipo(String tipo) {
        Tipo.converterStringToTipo(tipo);
        return tipo;
    }

    public static String validarSexo(String sexo) {
        Sexo.converterStringToSexo(sexo);
        return sexo;
    }

    public static String validarEndereco(String endereco) {
        String regexEnderecoCompleto = "([A-Za-z0-9]+( ?))+, [0-9]+, [A-Za-z]+";
        if (endereco.matches(regexEnderecoCompleto)) {
            return endereco;
        }
        String regexEnderecoSemNumero = "([A-Za-z0-9]+( ?))+, [A-Za-z]+";
        if (endereco.matches(regexEnderecoSemNumero)) {
            String[] splitEndereco = endereco.split(", ");
            return splitEndereco[0] + ", " + NAO_INFORMADO + ", " + splitEndereco[1];
        }
        if (endereco.matches("([A-Za-z0-9]+( ?))+, NÃO INFORMADO, [A-Za-z]+")) {
            return endereco;
        }
        throw new PetAtributoInvalidoExeception("Endereço inválido. Digite: \"Rua, NúmeroDaCasa, Cidade\" ou \"Rua, Cidade\", nessa ordem.");
    }

    private static void validarIdade(float idade) {
        if (idade > 0 && idade <= 20) {
            return;
        }
        throw new PetAtributoInvalidoExeception("Idade inválida.");
    }

    public static String validarIdade(String idade) {
        if (idade.isEmpty() || idade.equals(NAO_INFORMADO)) {
            return NAO_INFORMADO;
        }
        try {
            float idadeFloat = Float.parseFloat(idade);
            validarIdade(idadeFloat);
            if (idadeFloat < 1) {
                return idade;
            }
            return String.valueOf((int) idadeFloat);
        } catch (NumberFormatException e) {
            throw new PetAtributoInvalidoExeception("Idade inválida.");
        }
    }

    private static void validarPeso(float peso) {
        if (peso >= 0.5 && peso <= 60) {
            return;
        }
        throw new PetAtributoInvalidoExeception("Peso inválido.");
    }

    public static String validarPeso(String peso) {
        if (peso.isEmpty() || peso.equals(NAO_INFORMADO)) {
            return NAO_INFORMADO;
        }
        try {
            validarPeso(Float.parseFloat(peso));
            return peso;
        } catch (NumberFormatException e) {
            throw new PetAtributoInvalidoExeception("Peso inválida.");
        }
    }

    public static String validarRaca(String raca) {
        String regexRaca = "[A-Za-z]+( [A-Za-z]+)*";
        if (raca.matches(regexRaca)) {
            return raca;
        }
        if (raca.isEmpty() || raca.equals(NAO_INFORMADO)) {
            return NAO_INFORMADO;
        }
        throw new PetAtributoInvalidoExeception("Raça inválida.");
    }
}