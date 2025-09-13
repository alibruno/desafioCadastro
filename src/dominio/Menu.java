package dominio;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Menu {
    private static final File DIRETORIO_PETS_CADASTRADOS = new File("petsCadastrados");

    public int opcaoValidaMenuInicial(int opcao) throws IllegalArgumentException {
        if (opcao <= 0) {
            throw new IllegalArgumentException("Opção inválida.");
        }
        return opcao;
    }

    public int opcaoValidaMenuInicial(String opcao) throws IllegalArgumentException {
        return this.opcaoValidaMenuInicial(Integer.parseInt(opcao));
    }

    public String iterarValidacaoDeAtributosPet(int ordemAtributo, String atributoString) {
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

    public int linhaReferenteAoCriterioDeBusca(String criterio) {
        if (criterio.equals("Nome") || criterio.equals("Sobrenome") || criterio.equals("Nome e sobrenome")) {
            return 1;
        }
        if (criterio.equals("Tipo")) {
            return 2;
        }
        if (criterio.equals("Sexo")) {
            return 3;
        }
        if (criterio.equals("Endereço")) {
            return 4;
        }
        if (criterio.equals("Idade")) {
            return 5;
        }
        if (criterio.equals("Peso")) {
            return 6;
        }
        if (criterio.equals("Raça")) {
            return 7;
        }
        throw new RuntimeException("Ocorreu um erro durante a busca");
    }

    public String lerArquivoPetFormatado(int i, File file) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String leitura;
            sb.append(i).append(". ");
            while ((leitura = br.readLine()) != null) {
                sb.append(leitura.replaceAll("\\d - ", "")).append(" - ");
            }
            sb.delete(sb.length() - 3, sb.length());
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public boolean alterarFilePetExistente(int numCampo, String novoCampo, File fileASerReescrito) throws IOException {
        File fileFinal = new File(fileASerReescrito.getPath().concat("temp"));
        fileFinal.createNewFile();
        try (BufferedReader br = new BufferedReader(new FileReader(fileASerReescrito));
             BufferedWriter bw = new BufferedWriter(new FileWriter(fileFinal))) {
            String leitura;
            while ((leitura = br.readLine()) != null) {
                if (leitura.matches(numCampo + " - (.+)")) {
                    bw.write(numCampo + " - " + novoCampo);
                    bw.newLine();
                    bw.flush();
                    continue;
                }
                bw.write(leitura);
                bw.newLine();
                bw.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fileASerReescrito.delete();
        StringBuilder sb = new StringBuilder();
        if (numCampo == 1){
            sb.append(fileFinal.getPath(), 0, 30).append(novoCampo.toUpperCase().replace(" ", "")).append(".txt");
            return fileFinal.renameTo(new File(sb.toString()));
        }
        sb.append(fileFinal.getPath(), 0, fileFinal.getPath().length() - 4);
        return fileFinal.renameTo(new File(sb.toString()));
    }
}