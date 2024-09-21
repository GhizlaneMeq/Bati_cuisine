import Menus.ProjectMenu;
import Repositories.ClientRepository;
import Services.ClientService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        System.out.println("=== Bienvenue dans l'application de gestion des projets de rénovation de cuisines ===");
        while (isRunning) {
            System.out.println("=== Menu Principal ===");
            System.out.println("1. Créer un nouveau projet");
            System.out.println("2. Afficher les projets existants");
            System.out.println("3. Calculer le coût d'un projet");
            System.out.println("4. Quitter");
            System.out.print("Choisissez une option : ");

            int mainChoice = scanner.nextInt();
            scanner.nextLine();

            switch (mainChoice) {
                case 1:
                    createProject(scanner);
                    break;
                case 2:
                   // displayExistingProjects();
                    break;
                case 3:
                   // calculate();
                    break;
                case 4:
                    isRunning = false;
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
                    break;
            }
        }

        scanner.close();
    }

    private static void createProject(Scanner scanner) {
        ProjectMenu projectMenu = new ProjectMenu(new ClientService(new ClientRepository()));
        projectMenu.manageClient(scanner);
    }
}