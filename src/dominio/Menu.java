package dominio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Menu {
    private static final File DIRETORIO_PETS_CADASTRADOS = new File("petsCadastrados");

    public int opcaoValida(int opcao) throws IllegalArgumentException {
        if (opcao <= 0) {
            throw new IllegalArgumentException("Opção inválida.");
        }
        return opcao;
    }

    public int opcaoValida(String opcao) throws IllegalArgumentException {
        return this.opcaoValida(Integer.parseInt(opcao));
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

    private File criarArquivoPetCadastrado(String nomeCompleto) throws IOException {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm");
        String dataDeInsercao = LocalDateTime.now().format(pattern);
        String nomeFile = dataDeInsercao + "-" + nomeCompleto.toUpperCase().replace(" ", "") + ".txt";
        File fileNovoPetCadastrado = new File(DIRETORIO_PETS_CADASTRADOS, nomeFile);
        boolean isPetCriado = fileNovoPetCadastrado.createNewFile();
        if (isPetCriado) {
            return fileNovoPetCadastrado;
        }
        throw new RuntimeException("Erro: Pet já criado.");
    }

    private void inserirDadosDoPet(File filePetCadastrado, String[] atributosPet) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePetCadastrado))) {
            for (int i = 0; i < atributosPet.length; i++) {
                bw.write((i + 1) + " - " + atributosPet[i]);
                bw.newLine();
                bw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cria um arquivo e armazena os dados referentes ao Pet na pasta petsCadastrados a partir de
     * um array de 7 posições. <br>
     * Indices: [0] nomeCompleto, [1] tipo, [2] sexo, [3] endereco, [4] idade, [5] peso, [6] raca.
     *
     * @param artributosPet Atributos de Pet.
     * @throws IOException
     */
    public void cadastrarNovoPet(String[] artributosPet) throws IOException {
        inserirDadosDoPet(criarArquivoPetCadastrado(artributosPet[0]), artributosPet);
    }
}