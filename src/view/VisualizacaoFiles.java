package view;

import dominio.Menu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class VisualizacaoFiles {
    private static final Menu MENU = new Menu();
    private static final File FILE_MENU_INICIAL = new File("src//arquivos", "menu_inicial.txt");
    private static final File FILE_FORMULARIO = new File("src//arquivos", "formulario.txt");

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
        Scanner scanner = new Scanner(System.in);
        String opcao;
        int i = 0;
        while (true) {
            try {
                opcao = scanner.nextLine();
                return MENU.opcaoValida(opcao);
            } catch (IllegalArgumentException e) {
                leituraFile(FILE_MENU_INICIAL);
            }
        }
    }
}
