package Menus;

import java.util.Scanner;

public class MainMenu {
    private final ManageClient manageClient;
    private final ManageMaterial manageMaterial;
    private final ManageProject manageProject;
    private final ManageQuote manageQuote;
    private final ManageLabor manageLabor;
    private final Scanner scanner;

    public MainMenu(ManageClient manageClient, ManageMaterial manageMaterial, ManageLabor manageLabor,
                    ManageProject manageProject, ManageQuote manageQuote) {
        this.manageClient = manageClient;
        this.manageMaterial = manageMaterial;
        this.manageLabor = manageLabor;
        this.manageProject = manageProject;
        this.manageQuote = manageQuote;
        this.scanner = new Scanner(System.in);
    }

    public void displayMainMenu() {
        boolean running = true;

        while (running) {
            printMainMenu();

            int choice = getValidMenuOption(5);

            switch (choice) {
                case 1:
                    manageProjects();
                    break;
                case 2:
                    manageComponents();
                    break;
                case 3:
                    manageClient();
                    break;
                case 4:
                    manageQuotes();
                    break;
                case 5:
                    running = false;
                    System.out.println("\n Au revoir !");
                    break;
                default:
                    System.out.println(" Option invalide. Veuillez réessayer.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n*******************************************");
        System.out.println("            Menu Principal ");
        System.out.println("*******************************************");
        System.out.println("1 Gestion de Projet");
        System.out.println("2 Gestion de Composant");
        System.out.println("3  Gestion de Client");
        System.out.println("4  Gestion de Devis");
        System.out.println("5  Quitter");
        System.out.println("*******************************************");
        System.out.print(" Choisissez une option : ");
    }

    private void manageQuotes() {
        manageQuote.manageQuote();
    }

    private void manageClient() {
        manageClient.manageClients();
    }

    private void manageComponents() {
        boolean running = true;

        while (running) {
            printComponentsMenu();

            int choice = getValidMenuOption(3);

            switch (choice) {
                case 1:
                    manageMaterials();
                    break;
                case 2:
                    manageLabor();
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println(" Option invalide. Veuillez réessayer.");
            }
        }
    }

    private void printComponentsMenu() {
        System.out.println("\n*******************************************");
        System.out.println("            Gestion des Composants ");
        System.out.println("*******************************************");
        System.out.println("1  Gestion des Matériaux");
        System.out.println("2  Gestion de la Main d'Oeuvre");
        System.out.println("3  Retourner au Menu Principal");
        System.out.println("*******************************************");
        System.out.print(" Choisissez une option : ");
    }

    private void manageMaterials() {
        manageMaterial.manageMaterials();
    }

    private void manageLabor() {
        manageLabor.manageLabor();
    }

    private void manageProjects() {
        manageProject.manageProjects();
    }

    private int getValidMenuOption(int maxOption) {
        while (true) {
            try {
                int option = Integer.parseInt(scanner.nextLine());
                if (option >= 1 && option <= maxOption) {
                    return option;
                } else {
                    System.out.println(" Option invalide. Veuillez choisir une option entre 1 et " + maxOption + ".");
                }
            } catch (NumberFormatException e) {
                System.out.print("Entrée invalide. Veuillez entrer un nombre entier valide : ");
            }
        }
    }
}
