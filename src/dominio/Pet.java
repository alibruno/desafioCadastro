package dominio;

import java.util.Arrays;

public class Pet {
    private String nomeCompleto;
    private Tipo tipo;
    private Sexo sexo;
    private String endereco;
    private float idade;
    private float peso;
    private static final String NAO_INFORMADO = "NÃO INFORMADO";
    private String raca;

    public Pet(String nomeCompleto, String tipo, String sexo, String endereco, float idade, float peso, String raca) {
        setNomeCompleto(nomeCompleto);
        setTipo(tipo);
        setSexo(sexo);
        setEndereco(endereco);
        setIdade(idade);
        setPeso(peso);
        setRaca(raca);
    }

    public void setNomeCompleto(String nomeCompleto) {
        String regexNomeValido = "[A-Za-z]+( [A-Za-z]+)+";
        if (nomeCompleto.matches(regexNomeValido)) {
            this.nomeCompleto = nomeCompleto;
            return;
        }
        if (nomeCompleto.isEmpty()) {
            this.nomeCompleto = NAO_INFORMADO;
            return;
        }
        throw new PetAtributoInvalidoExeception("Nome completo inválido. Digite o nome e sobrenome.");
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = Tipo.converterStringToTipo(tipo);
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = Sexo.converterStringToSexo(sexo);
    }


    public void setEndereco(String endereco) {
        String regexEnderecoCompleto = "([A-Za-z0-9]+( ?))+, [0-9]+, [A-Za-z]+";
        if (endereco.matches(regexEnderecoCompleto)) {
            this.endereco = endereco;
            return;
        }
        String regexEnderecoSemNumero = "([A-Za-z0-9]+( ?))+, [A-Za-z]+";
        if (endereco.matches(regexEnderecoSemNumero)) {
            String[] splitEndereco = endereco.split(", ");
            this.endereco = splitEndereco[0] + ", " + NAO_INFORMADO + ", " + splitEndereco[1];
            return;
        }
        throw new PetAtributoInvalidoExeception("Endereço inválido. Digite: \"Rua, NúmeroDaCasa, Cidade\" ou \"Rua, Cidade\", nessa ordem.");
    }

    public void setIdade(float idade) {
        if (idade > 0 && idade <= 20) {
            this.idade = idade;
            return;
        }
        throw new PetAtributoInvalidoExeception("Idade inválida.");
    }

    public void setPeso(float peso) {
        if (peso >= 0.5 && peso <= 60) {
            this.peso = peso;
            return;
        }
        throw new PetAtributoInvalidoExeception("Peso inválido.");
    }

    public void setRaca(String raca) {
        String regexRaca = "[A-Za-z]+( [A-Za-z]+)*";
        if (raca.matches(regexRaca)) {
            this.raca = raca;
            return;
        }
        if (raca.isEmpty()) {
            this.raca = NAO_INFORMADO;
            return;
        }
        throw new PetAtributoInvalidoExeception("Raça inválida.");
    }
}