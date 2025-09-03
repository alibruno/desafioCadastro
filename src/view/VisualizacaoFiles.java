package view;

import dominio.Menu;
import dominio.Pet;
import dominio.PetAtributoInvalidoExeception;

import java.io.*;
import java.util.Scanner;

public class VisualizacaoFiles {
    private static final Menu MENU = new Menu();
    public static final File FILE_MENU_INICIAL = new File("src//arquivos", "menu_inicial.txt");
    public static final File FILE_FORMULARIO = new File("src//arquivos", "formulario.txt");
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

    public int escolherOpcao() {
        String opcao;
        while (true) {
            try {
                opcao = scanner.nextLine();
                return MENU.opcaoValida(opcao);
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
                        entrada[iteracao] = MENU.iterarSobreAtributosPet(iteracao, entrada[iteracao]);
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

    public void iniciar() {
        leituraFile(FILE_MENU_INICIAL);
        int opcao = escolherOpcao();
        switch (opcao) {
            case 1:
                cadastrar();
                break;
        }
    }
}
