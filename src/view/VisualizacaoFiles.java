package view;

import dominio.Menu;
import dominio.Pet;
import dominio.PetAtributoInvalidoExeception;
import dominio.Tipo;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VisualizacaoFiles {
    private static final Menu MENU = new Menu();
    public static final File FILE_MENU_INICIAL = new File("src//arquivos", "menu_inicial.txt");
    public static final File FILE_FORMULARIO = new File("src//arquivos", "formulario.txt");
    private static final File DIRETORIO_PETS_CADASTRADOS = new File("petsCadastrados");
    private Scanner scanner = new Scanner(System.in);

    public void leituraFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String leitura;
            while ((leitura = br.readLine()) != null) {
                System.out.println(leitura);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int escolherOpcaoMenuInicial() {
        String opcao;
        while (true) {
            try {
                opcao = scanner.nextLine();
                return MENU.opcaoValidaMenuInicial(opcao);
            } catch (IllegalArgumentException e) {
                leituraFile(FILE_MENU_INICIAL);
            }
        }
    }

    public void cadastrar() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_FORMULARIO))) {
            String leitura;
            String[] entrada = new String[7];
            int iteracao = 0;
            while ((leitura = br.readLine()) != null) {
                while (true) {
                    System.out.println(leitura);
                    entrada[iteracao] = scanner.nextLine();
                    try {
                        entrada[iteracao] = MENU.iterarValidacaoDeAtributosPet(iteracao, entrada[iteracao]);
                        iteracao++;
                        break;
                    } catch (PetAtributoInvalidoExeception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            Pet pet = new Pet(entrada);
            MENU.cadastrarNovoPet(entrada);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String selecionarTipo() {
        System.out.println("Primeiramente, selecione o TIPO de animal que deseja buscar.");
        System.out.print("Tipos disponíveis: " + Tipo.tiposDisponiveis() + "\n");
        String tipoEscolhido;
        while (true) {
            try {
                tipoEscolhido = scanner.nextLine();
                return Tipo.converterStringToTipo(tipoEscolhido).toString();
            } catch (PetAtributoInvalidoExeception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private int definirQuantidadeDeCriterios() {
        System.out.println("Você deseja utilizar 1 ou 2 critérios de busca?");
        int quantCriteriosInt;
        while (true) {
            try {
                quantCriteriosInt = Integer.parseInt(scanner.nextLine());
                if (quantCriteriosInt != 1 && quantCriteriosInt != 2) {
                    throw new IllegalArgumentException("Quantidade inválida. São permitidas apenas a escolha de 1 ou 2 critérios.");
                }
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return quantCriteriosInt;
    }

    private String[][] selecionarCriteriosECampos() {
        String tipoSelecionado = selecionarTipo();
        int quantCriterios = definirQuantidadeDeCriterios();
        String[][] criteriosSelecionados = new String[quantCriterios + 1][2];
        criteriosSelecionados[0][0] = "Tipo";
        criteriosSelecionados[0][1] = tipoSelecionado;

        System.out.println("Menu de busca: \n" +
                "    Nome ou sobrenome\n" +
                "    Sexo (Digite: Macho ou Femea)\n" +
                "    Idade\n" +
                "    Peso\n" +
                "    Raça\n" +
                "    Endereço\n");

        String[] palavrasChavesCriterio = {"Nome", "Sobrenome", "Nome e sobrenome", "Sexo", "Idade", "Peso", "Raça", "Endereço"};
        for (int i = 1; i < criteriosSelecionados.length; i++) {
            System.out.println("Digite o critério " + (i) + ": ");
            while (true) {
                try {
                    String criterioInserido = scanner.nextLine();
                    for (String criterioValido : palavrasChavesCriterio) {
                        if (criterioInserido.equalsIgnoreCase(criterioValido)) {
                            criteriosSelecionados[i][0] = criterioValido;
                            break;
                        }
                    }
                    if (criteriosSelecionados[i][0] == null) {
                        throw new IllegalArgumentException("Critério inválido. Insira uma das seguintes palavras: " +
                                Arrays.toString(palavrasChavesCriterio));
                    }
                    if (i == 2 && criteriosSelecionados[i][0].equals(criteriosSelecionados[i - 1][0])) {
                        criteriosSelecionados[i][0] = null;
                        throw new IllegalArgumentException("Critério já selecionado. Por favor, selecione outro critério.");
                    }
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
            System.out.println("Insira o que deve ser buscado: ");
            String campoASerBuscado;
            while (true) {
                try {
                    campoASerBuscado = scanner.nextLine();
                    if (criteriosSelecionados[i][0].equals("Sexo") && !(campoASerBuscado.equalsIgnoreCase("Macho") || campoASerBuscado.equalsIgnoreCase("Femea"))) {
                        throw new IllegalArgumentException("Campo inválido. Digite: Macho ou Femea.");
                    }
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
            criteriosSelecionados[i][1] = campoASerBuscado;
        }
        return criteriosSelecionados;
    }

    public String[] buscarPosicoesFilesPetsEncontrados() {
        String[][] criterios = selecionarCriteriosECampos();
        File[] petsCadastradosNoSistema = DIRETORIO_PETS_CADASTRADOS.listFiles();
        if (petsCadastradosNoSistema == null) {
            throw new RuntimeException("Não há pets cadastrados no sistema.");
        }
        int[][] validarEncontrosPetsECriterios = new int[criterios.length][petsCadastradosNoSistema.length];
        // CRITERIO -> ARQUIVOS EXISTENTES. CASO 1: ENCONTRO. CASO 0: NÃO ENCONTRO.
        int i = 0;
        for (String[] criterio : criterios) {
            int j = 0;
            for (File file : petsCadastradosNoSistema) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String leitura;
                    Pattern pattern = Pattern.compile(MENU.linhaReferenteAoCriterioDeBusca(criterio[0]) + " - (.*)" + criterio[1] + "(.*)", Pattern.CASE_INSENSITIVE);
                    Matcher matcher;
                    while ((leitura = br.readLine()) != null) {
                        matcher = pattern.matcher(leitura);
                        if (matcher.find()) {
                            validarEncontrosPetsECriterios[i][j] = 1;
                        }
                    }
                    j++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            i++;
        }
        StringBuilder posicoesDoArrayPetsCadast = new StringBuilder();
        boolean animalEncontrado = false;
        if (validarEncontrosPetsECriterios.length == 3) {
            for (int l = 0; l < petsCadastradosNoSistema.length; l++) {
                if (validarEncontrosPetsECriterios[0][l] == 0 || validarEncontrosPetsECriterios[1][l] == 0 || validarEncontrosPetsECriterios[2][l] == 0) {
                    continue;
                }
                animalEncontrado = true;
                posicoesDoArrayPetsCadast.append(l).append(",");
            }
        } else {
            for (int l = 0; l < petsCadastradosNoSistema.length; l++) {
                if (validarEncontrosPetsECriterios[0][l] == 0 || validarEncontrosPetsECriterios[1][l] == 0) {
                    continue;
                }
                animalEncontrado = true;
                posicoesDoArrayPetsCadast.append(l).append(",");
            }
        }
        if (!animalEncontrado) {
            throw new IllegalArgumentException("\nCritérios inválidos. Por favor, digite os critérios novamente.\n");
        }
        return posicoesDoArrayPetsCadast.toString().split(",");
    }

    public void buscarDadosPet() {
        while (true) {
            try {
                String[] posicaoPetsEncontrados = buscarPosicoesFilesPetsEncontrados();
                File[] petsCadastradosNoSistema = DIRETORIO_PETS_CADASTRADOS.listFiles();
                if (petsCadastradosNoSistema == null) {
                    throw new RuntimeException("Não há pets cadastrados no sistema.");
                }
                System.out.println("Pets encontrados:");
                for (int i = 0; i < posicaoPetsEncontrados.length; i++) {
                    System.out.println(MENU.lerArquivoPetFormatado((i + 1), petsCadastradosNoSistema[Integer.parseInt(posicaoPetsEncontrados[i])]));
                }
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public File fileDoPetQueSeraAlterado() {
        String[] posicaoPetsEncontrados;
        while (true) {
            try {
                posicaoPetsEncontrados = buscarPosicoesFilesPetsEncontrados();
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        File[] petsCadastradosNoSistema = DIRETORIO_PETS_CADASTRADOS.listFiles();
        if (petsCadastradosNoSistema == null) {
            throw new RuntimeException("Não há pets cadastrados no sistema.");
        }
        System.out.println("Pets encontrados:");
        for (int i = 0; i < posicaoPetsEncontrados.length; i++) {
            System.out.println(MENU.lerArquivoPetFormatado((i + 1), petsCadastradosNoSistema[Integer.parseInt(posicaoPetsEncontrados[i])]));
        }
        // posicaoPetsEncontrados = [0, 5]
        // petsCadastradosNoSistema [0,1,2,3,4,5]
        // petsCadastradosNoSistema [posicaoPetsEncontrados == 1 => 5] -> File 5
        // indiceEscolhido = 2 -> File 5
        // petsCadastradosNoSistema [posicaoPetsEncontrados(indiceEscolhido = 2 - 1) => 5] -> File 5

        System.out.println("Selecione a posição dos pets em que você deseja alterar.");
        while (true) {
            try {
                int indicePetEscolhido = Integer.parseInt(scanner.nextLine());
                indicePetEscolhido -= 1;
                if (indicePetEscolhido < 0 || indicePetEscolhido > (posicaoPetsEncontrados.length - 1)) {
                    throw new IllegalArgumentException();
                }
                return petsCadastradosNoSistema[Integer.parseInt(posicaoPetsEncontrados[indicePetEscolhido])];
            } catch (IllegalArgumentException e) {
                System.out.println("Posição do pet inválida. Insira novamente.");
            }
        }
    }

    public void alterarPetCadastrado() {
        File file = fileDoPetQueSeraAlterado();
        System.out.println("O que você deseja alterar no pet? Selecione o campo pelo número: 1(Nome), 4(Endereço), 5(Idade), 6(Peso), 7(Raça)");
        System.out.println("Nota: não se pode alterar \"(2)Tipo\" e \"(3)Sexo\"");
        int criterioQueSofreraAlteracoes;
        while (true) {
            try {
                criterioQueSofreraAlteracoes = Integer.parseInt(scanner.nextLine());
                if (criterioQueSofreraAlteracoes <= 0 || criterioQueSofreraAlteracoes == 2 || criterioQueSofreraAlteracoes == 3 ||
                        criterioQueSofreraAlteracoes > 7) {
                    throw new IllegalArgumentException();
                }
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Número inválido. Digite de um número entre 1(Nome), 4(Endereço), 5(Idade), 6(Peso), 7(Raça)");
                System.out.println("Nota: não se pode alterar \"2(Tipo)\" e \"3(Sexo)\"");
            }
        }
        System.out.println("Digite a alteração:");
        String campoQueSofreraAlteracoes;
        while (true) {
            try {
                campoQueSofreraAlteracoes = scanner.nextLine();
                campoQueSofreraAlteracoes = MENU.iterarValidacaoDeAtributosPet((criterioQueSofreraAlteracoes - 1), campoQueSofreraAlteracoes);
                break;
            } catch (PetAtributoInvalidoExeception e) {
                System.out.println(e.getMessage());
            }
        }
        try {
            boolean foiCriado = MENU.alterarFilePetExistente(criterioQueSofreraAlteracoes, campoQueSofreraAlteracoes, file);
            if (!foiCriado) {
                throw new RuntimeException("Erro durante a alteração de um PetExistente. Possíveis causas: " +
                        "\n1. Criação do arq. temporário, \n2. Delete do arq. original, \n3. Não foi possível renomear o arq. temporário");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Alteração realizada com sucesso!");
    }

    public void iniciar() {
        leituraFile(FILE_MENU_INICIAL);
        int opcao = escolherOpcaoMenuInicial();
        switch (opcao) {
            case 1:
                cadastrar();
                System.out.println("\nPet criado com sucesso!");
                break;
            case 2:
                alterarPetCadastrado();
                break;
            case 5:
                buscarDadosPet();
                break;
        }
    }
}
