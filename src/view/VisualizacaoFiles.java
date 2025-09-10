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


    private int linhaReferenteAoCriterioDeBusca(String criterio) {
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

    public void lerArquivosDePetsEncontrados(int i, File file) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String leitura;
            sb.append(i).append(". ");
            while ((leitura = br.readLine()) != null) {
                sb.append(leitura.replaceAll("\\d - ", "")).append(" - ");
            }
            sb.delete(sb.length() - 3, sb.length());
            System.out.println(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buscarDadosPet() {
        String[][] criterios = selecionarCriteriosECampos();
        File[] petsCadastradosNoSistema = DIRETORIO_PETS_CADASTRADOS.listFiles();
        if (petsCadastradosNoSistema == null) {
            System.out.println("Não há pets cadastrados no sistema.");
            return;
        }
        int[][] filesDePetsEncontrados = new int[criterios.length][petsCadastradosNoSistema.length];
        // CRITERIO -> ARQUIVOS EXISTENTES. CASO 1: ENCONTRO. CASO 0: NÃO ENCONTRO.
        int i = 0;
        for (String[] criterio : criterios) {
            int j = 0;
            for (File file : petsCadastradosNoSistema) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String leitura;
                    Pattern pattern = Pattern.compile(linhaReferenteAoCriterioDeBusca(criterio[0]) + " - (.*)" + criterio[1] + "(.*)", Pattern.CASE_INSENSITIVE);
                    Matcher matcher;
                    while ((leitura = br.readLine()) != null) {
                        matcher = pattern.matcher(leitura);
                        if (matcher.find()) {
                            filesDePetsEncontrados[i][j] = 1;
                        }
                    }
                    j++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            i++;
        }
        int ordemDeEncontroDadosPet = 1;
        boolean animalEncontrado = false;
        if (filesDePetsEncontrados.length == 3) {
            for (int l = 0; l < petsCadastradosNoSistema.length; l++) {
                if (filesDePetsEncontrados[0][l] == 0 || filesDePetsEncontrados[1][l] == 0 || filesDePetsEncontrados[2][l] == 0) {
                    continue;
                }
                animalEncontrado = true;
                lerArquivosDePetsEncontrados(ordemDeEncontroDadosPet, petsCadastradosNoSistema[l]);
                ordemDeEncontroDadosPet++;
            }
        } else {
            for (int l = 0; l < petsCadastradosNoSistema.length; l++) {
                if (filesDePetsEncontrados[0][l] == 0 || filesDePetsEncontrados[1][l] == 0) {
                    continue;
                }
                animalEncontrado = true;
                lerArquivosDePetsEncontrados(ordemDeEncontroDadosPet, petsCadastradosNoSistema[l]);
                ordemDeEncontroDadosPet++;
            }
        }
        if (!animalEncontrado) {
            System.out.println("Animal com esses críterios não encontrado.");
        }
    }

    public void iniciar() {
        leituraFile(FILE_MENU_INICIAL);
        int opcao = escolherOpcaoMenuInicial();
        switch (opcao) {
            case 1:
                cadastrar();
                System.out.println("\nPet criado com sucesso!");
                break;
            case 5:
                buscarDadosPet();
                break;
        }
    }
}
