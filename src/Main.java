/*
import Entities.Project;
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
        materialRepository = new MaterialRepository();
        projectRepository = new ProjectRepository();
        laborRepository = new LaborRepository();
        quoteRepository = new QuoteRepository();

        laborService = new LaborService(laborRepository);
        materialService = new MaterialService(materialRepository);
        projectService = new ProjectService(materialService, laborService, projectRepository);
        clientService = new ClientService(clientRepository, projectService);
        quoteService = new QuoteService(quoteRepository);

        clientMenu = new ClientMenu(clientService);
        materialMenu = new MaterialMenu(materialService);
        laborMenu = new LaborMenu(laborService);

        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        System.out.println(BOLD + BLUE + "=================================================" + RESET);
        System.out.println(BOLD + YELLOW + "=== Bienvenue dans l'application de gestion ===" + RESET);
        System.out.println(BOLD + YELLOW + "=== des projets de rénovation de cuisines ===" + RESET);
        System.out.println(BOLD + BLUE + "=================================================\n" + RESET);

        while (isRunning) {
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
        ProjectMenu projectMenu = new ProjectMenu(projectService, clientMenu, materialMenu, laborMenu, quoteService);
        projectMenu.manageClient(scanner);
    }
}




 */




import Menus.*;
import Repositories.*;
import Services.*;


public class Main {
    private static ClientService clientService;
    private static ProjectService projectService;
    private static ClientRepository clientRepository;
    private static ProjectRepository projectRepository;
    private static MaterialService materialService;
    private static MaterialRepository materialRepository;
    private static LaborRepository laborRepository;
    private static LaborService laborService;
    private static QuoteRepository quoteRepository;
    private static QuoteService quoteService;
    public static void main(String[] args) {
        clientRepository = new ClientRepository();
        projectRepository = new ProjectRepository();
        materialRepository = new MaterialRepository();
        laborRepository = new LaborRepository();
        quoteRepository = new QuoteRepository();

        materialService = new MaterialService(materialRepository);
        projectService = new ProjectService(materialService,new LaborService(new LaborRepository()),projectRepository);
        clientService = new ClientService(clientRepository,projectService);
        laborService = new LaborService(laborRepository);
        quoteService = new QuoteService(quoteRepository);


        ManageClient manageClient = new ManageClient(clientService);
        ManageMaterial manageMaterial = new ManageMaterial(materialService,projectService);
        ManageLabor manageLabor = new ManageLabor(laborService);
        ManageQuote manageQuote = new ManageQuote(quoteService,projectService);
        ManageProject manageProject = new ManageProject(projectService,manageClient,manageMaterial,manageLabor,manageQuote);


        MainMenu mainMenu = new MainMenu(manageClient ,manageMaterial,manageLabor,manageProject,manageQuote);
        mainMenu.displayMainMenu();
    }
}





