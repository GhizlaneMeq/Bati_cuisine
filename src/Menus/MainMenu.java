package Menus;

import java.util.Scanner;

public class MainMenu {
    private ManageClient manageClient;
    /*private ManageComponents manageComponents; // Assuming you have a ManageComponents class
    private ManageProject manageProject; // Assuming you have a ManageProject class
    private ManageQuotes manageQuotes; // Assuming you have a ManageQuotes class

     */

    public MainMenu(ManageClient manageClient/*, ManageComponents manageComponents,
                    ManageProject manageProject, ManageQuotes manageQuotes*/) {
        this.manageClient = manageClient;
       /* this.manageComponents = manageComponents;
        this.manageProject = manageProject;
        this.manageQuotes = manageQuotes;

        */
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
            scanner.nextLine(); // Consume the newline

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
       // manageQuotes.manageQuotes(); // Implement this method in ManageQuotes
    }

    private void manageClient() {
        manageClient.manageClients(); // Implement this method in ManageClient
    }

    private void manageComponents() {
       // manageComponents.manageComponents(); // Implement this method in ManageComponents
    }

    private void manageProjects() {
      //  manageProject.manageProjects(); // Implement this method in ManageProject
    }
}
