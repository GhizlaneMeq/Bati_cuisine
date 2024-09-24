package Menus;

import java.util.Scanner;

public class MainMenu {
    private ManageClient manageClient;
    private ManageMaterial manageMaterial;
   private ManageProject manageProject;
   private ManageQuote manageQuote;
   private  ManageLabor manageLabor;

    public MainMenu(ManageClient manageClient, ManageMaterial manageMaterial, ManageLabor manageLabor,
            ManageProject manageProject, ManageQuote manageQuote) {
        this.manageClient = manageClient;
        this.manageMaterial = manageMaterial;
        this.manageLabor = manageLabor;
        this.manageProject = manageProject;
        this.manageQuote = manageQuote;


    }

    public void displayMainMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n*******************************************");
            System.out.println("           🏠 Menu Principal 🏠");
            System.out.println("*******************************************");
            System.out.println("1️⃣  Gestion de Projet");
            System.out.println("2️⃣  Gestion de Composant");
            System.out.println("3️⃣  Gestion de Client");
            System.out.println("4️⃣  Gestion de Devis");
            System.out.println("5️⃣  Quitter");
            System.out.println("*******************************************");
            System.out.print("👉 Choisissez une option : ");

            int choice = scanner.nextInt();
            scanner.nextLine();

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
                    System.out.println("\n👋 Au revoir !");
                    break;
                default:
                    System.out.println("❌ Option invalide. Veuillez réessayer.");
            }
        }
    }

    private void manageQuotes() {
        manageQuote.manageQuote();
    }

    private void manageClient() {
        manageClient.manageClients();
    }

    private void manageComponents() {
        boolean running = true;
        Scanner scanner = new Scanner(System.in);

        while (running) {
            System.out.println("\n*******************************************");
            System.out.println("           🛠️ Gestion des Composants 🛠️");
            System.out.println("*******************************************");
            System.out.println("1️⃣  Gestion des Matériaux");
            System.out.println("2️⃣  Gestion de la Main d'Oeuvre");
            System.out.println("3️⃣  Retourner au Menu Principal");
            System.out.println("*******************************************");
            System.out.print("👉 Choisissez une option : ");

            int choice = scanner.nextInt();
            scanner.nextLine();

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
                    System.out.println("❌ Option invalide. Veuillez réessayer.");
            }
        }
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
}
