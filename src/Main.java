/*import Entities.Project;
import Menus.ClientMenu;
import Menus.MaterialMenu;
import Menus.ProjectMenu;
import Menus.LaborMenu;
import Repositories.*;
import Services.*;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String BOLD = "\033[1m";
    public static final String UNDERLINE = "\033[4m";

    private static ClientService clientService;
    private static ClientRepository clientRepository;
    private static ClientMenu clientMenu;
    private static MaterialService materialService;
    private static MaterialRepository materialRepository;
    private static MaterialMenu materialMenu;
    private static ProjectService projectService;
    private static ProjectRepository projectRepository;
    private static LaborRepository laborRepository;
    private static LaborService laborService;
    private static LaborMenu laborMenu;
    private static QuoteService quoteService;
    private static QuoteRepository quoteRepository;

    public static void main(String[] args) {
        clientRepository = new ClientRepository();
        clientService = new ClientService(clientRepository);
        materialRepository = new MaterialRepository();
        materialService = new MaterialService(materialRepository);
        materialMenu = new MaterialMenu(materialService);
        clientMenu = new ClientMenu(clientService);
        projectRepository = new ProjectRepository();
        projectService = new ProjectService(projectRepository);
        laborRepository = new LaborRepository();
        laborService = new LaborService(laborRepository);
        laborMenu = new LaborMenu(laborService);
        quoteService = new QuoteService(quoteRepository);
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        System.out.println(BOLD + BLUE + "=================================================" + RESET);
        System.out.println(BOLD + YELLOW + "=== Bienvenue dans l'application de gestion ===" + RESET);
        System.out.println(BOLD + YELLOW + "=== des projets de rénovation de cuisines ===" + RESET);
        System.out.println(BOLD + BLUE + "=================================================\n" + RESET);

        while (isRunning) {
            // Main Menu
            System.out.println(GREEN + BOLD + "========== Menu Principal ==========" + RESET);
            System.out.println(CYAN + "| 1. Créer un nouveau projet        |" + RESET);
            System.out.println(CYAN + "| 2. Afficher les projets existants |" + RESET);
            System.out.println(CYAN + "| 3. Calculer le coût d'un projet   |" + RESET);
            System.out.println(CYAN + "| 4. Quitter                        |" + RESET);
            System.out.println(GREEN + BOLD + "=====================================" + RESET);
            System.out.print(YELLOW + BOLD + "Veuillez choisir une option : " + RESET);

            int mainChoice = scanner.nextInt();
            scanner.nextLine();

            switch (mainChoice) {
                case 1:
                    createProject(scanner);
                    break;
                case 2:
                    displayExistingProjects();
                    break;
                case 3:
                    calculateExistingProjectCost(scanner);
                    break;
                case 4:
                    isRunning = false;
                    System.out.println(RED + BOLD + "\nMerci d'avoir utilisé l'application. Au revoir !" + RESET);
                    break;
                default:
                    System.out.println(RED + BOLD + "\nOption invalide. Veuillez réessayer." + RESET);
                    break;
            }

            System.out.println();
        }

        scanner.close();
    }

    private static void calculateExistingProjectCost(Scanner scanner) {
        displayExistingProjects();
        System.out.print(BOLD + "Entrez l'ID du projet pour calculer les coûts : " + RESET);
        int projectId = scanner.nextInt();

        System.out.println(BOLD + GREEN + "Le coût total estimé du projet avec ID " + projectId + " est de : 1000 €.\n" + RESET);
    }

    private static void displayExistingProjects() {
        List<Project> projects = projectService.findAll();
        System.out.println(BOLD + BLUE + "\n======= Liste des Projets =======" + RESET);
        if (projects.isEmpty()) {
            System.out.println(RED + "Aucun projet existant." + RESET);
        } else {
            for (Project project : projects) {
                System.out.println(WHITE + project.toString() + RESET);
            }
        }
        System.out.println(BOLD + BLUE + "=================================\n" + RESET);
    }

    private static void createProject(Scanner scanner) {
        ProjectMenu projectMenu = new ProjectMenu(projectService, clientMenu, materialMenu, laborMenu, materialService, laborService, quoteService);
        projectMenu.manageClient(scanner);
    }
}

 */

import Menus.MainMenu;
import Menus.ManageClient; // Import your ManageClient class
import Menus.ManageMaterial;
import Repositories.ClientRepository;
import Repositories.MaterialRepository;
import Repositories.ProjectRepository;
import Services.ClientService;
import Services.MaterialService;
import Services.ProjectService;
/*import Menus.ManageComponents; // Import your ManageComponents class
import Menus.ManageProject; // Import your ManageProject class
import Menus.ManageQuotes; // Import your ManageQuotes class


 */

public class Main {
    private static ClientService clientService;
    private static ProjectService projectService;
    private static ClientRepository clientRepository;
    private static ProjectRepository projectRepository;
    private static MaterialService materialService;
    private static MaterialRepository materialRepository;
    public static void main(String[] args) {
        clientRepository = new ClientRepository();
        projectRepository = new ProjectRepository();
        materialRepository = new MaterialRepository();
        projectService = new ProjectService(projectRepository);
        clientService = new ClientService(clientRepository,projectService);
        materialService = new MaterialService(materialRepository);
        // Create instances of the management classes
        ManageClient manageClient = new ManageClient(clientService);
        ManageMaterial manageMaterial = new ManageMaterial(materialService,projectService);
        /*ManageComponents manageComponents = new ManageComponents();
        ManageProject manageProject = new ManageProject();
        ManageQuotes manageQuotes = new ManageQuotes();

         */

        // Create the main menu with the management class instances
        MainMenu mainMenu = new MainMenu(manageClient ,manageMaterial/*,manageComponents, manageProject, manageQuotes*/);
        mainMenu.displayMainMenu();
    }
}


