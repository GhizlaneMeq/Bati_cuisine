/*package Menus;

import Entities.Client;
import Entities.Enum.ProjectStatus;
import Entities.Material;
import Entities.Project;
import Entities.Labor;
import Services.ClientService;
import Services.MaterialService;
import Services.LaborService;
import Services.ProjectService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProjectMenu {
    private ClientMenu clientMenu;
    private MaterialMenu materialMenu;
    private LaborMenu laborMenu;
    private ProjectService projectService;
    private MaterialService materialService;
    private LaborService laborService;

    public ProjectMenu(ProjectService projectService, ClientMenu clientMenu, MaterialMenu materialMenu, LaborMenu laborMenu,
                       MaterialService materialService, LaborService laborService) {
        this.clientMenu = clientMenu;
        this.materialMenu = materialMenu;
        this.laborMenu = laborMenu;
        this.projectService = projectService;
        this.materialService = materialService;
        this.laborService = laborService;
    }

    public void manageClient(Scanner scanner) {
        System.out.println("--- Gestion des clients ---");
        System.out.println("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ?");
        System.out.println("1. Chercher un client existant");
        System.out.println("2. Ajouter un nouveau client");
        System.out.print("Choisissez une option : ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                searchClient(scanner);
                break;
            case 2:
                addNewClient(scanner);
                break;
            default:
                System.out.println("Option invalide.");
                break;
        }
    }

    private void searchClient(Scanner scanner) {
        Optional<Client> client = clientMenu.search();
        if (client.isPresent()) {
            System.out.print("Souhaitez-vous continuer avec ce client ? (y/n): ");
            String choiceToContinue = scanner.nextLine().trim().toLowerCase();
            if (choiceToContinue.equals("y")) {
                addProject(scanner, client.get());
            } else if (choiceToContinue.equals("n")) {
                addNewClient(scanner);
            } else {
                System.out.println("Choix invalide. Veuillez entrer 'y' ou 'n'.");
            }
        }
    }

    private void addProject(Scanner scanner, Client client) {
        try {
            System.out.println("\n--- Création d'un Nouveau Projet ---");
            System.out.print("Entrez le nom du projet: ");
            String name = scanner.nextLine();
            System.out.print("Entrez la surface de la cuisine (en m²): ");
            double surface = scanner.nextDouble();
            scanner.nextLine();

            Project project = new Project(name, surface, 0, ProjectStatus.InProgress, client);
            Optional<Project> savedProject = projectService.save(project);

            materialMenu.create(savedProject.get());
            laborMenu.create(savedProject.get());



        } catch (Exception e) {
            System.out.println("Erreur lors de la création du projet: " + e.getMessage());
        }
    }

    public void addNewClient(Scanner scanner) {
        clientMenu.create();
    }

}


 */


package Menus;

import Entities.Client;
import Entities.Enum.ProjectStatus;
import Entities.Material;
import Entities.Project;
import Entities.Labor;
import Services.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProjectMenu {
    private ClientMenu clientMenu;
    private MaterialMenu materialMenu;
    private LaborMenu laborMenu;
    private ProjectService projectService;
    private MaterialService materialService;
    private LaborService laborService;
    private QuoteService quoteService;
    public ProjectMenu(ProjectService projectService, ClientMenu clientMenu, MaterialMenu materialMenu, LaborMenu laborMenu,
                       MaterialService materialService, LaborService laborService,QuoteService quoteService) {
        this.clientMenu = clientMenu;
        this.materialMenu = materialMenu;
        this.laborMenu = laborMenu;
        this.projectService = projectService;
        this.materialService = materialService;
        this.laborService = laborService;
        this.quoteService = quoteService;
    }

    public void manageClient(Scanner scanner) {
        System.out.println("--- Gestion des clients ---");
        System.out.println("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ?");
        System.out.println("1. Chercher un client existant");
        System.out.println("2. Ajouter un nouveau client");
        System.out.print("Choisissez une option : ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                searchClient(scanner);
                break;
            case 2:
                addNewClient(scanner);
                break;
            default:
                System.out.println("Option invalide.");
                break;
        }
    }

    private void searchClient(Scanner scanner) {
        Optional<Client> client = clientMenu.search();
        if (client.isPresent()) {
            System.out.print("Souhaitez-vous continuer avec ce client ? (y/n): ");
            String choiceToContinue = scanner.nextLine().trim().toLowerCase();
            if (choiceToContinue.equals("y")) {
                addProject(scanner, client.get());
            } else if (choiceToContinue.equals("n")) {
                addNewClient(scanner);
            } else {
                System.out.println("Choix invalide. Veuillez entrer 'y' ou 'n'.");
            }
        }
    }

    private void addProject(Scanner scanner, Client client) {
        try {
            System.out.println("\n--- Création d'un Nouveau Projet ---");
            System.out.print("Entrez le nom du projet: ");
            String name = scanner.nextLine();
            System.out.print("Entrez la surface de la cuisine (en m²): ");
            double surface = scanner.nextDouble();
            scanner.nextLine();

            Project project = new Project(name, surface, 0, ProjectStatus.InProgress, client);
            Optional<Project> savedProject = projectService.save(project);

            materialMenu.create(savedProject.get());
            laborMenu.create(savedProject.get());

            calculateTotalCost(scanner, savedProject.get());

        } catch (Exception e) {
            System.out.println("Erreur lors de la création du projet: " + e.getMessage());
        }
    }

    public void addNewClient(Scanner scanner) {
        clientMenu.create();
    }
    private void calculateTotalCost(Scanner scanner, Project project) {
        System.out.print("Souhaitez-vous appliquer une TVA au projet ? (y/n) : ");
        String applyVAT = scanner.nextLine().trim().toLowerCase();
        double vatRate = 0;
        if (applyVAT.equals("y")) {
            System.out.print("Entrez le pourcentage de TVA (%) : ");
            vatRate = scanner.nextDouble() / 100;
            scanner.nextLine();
        }

        System.out.print("Souhaitez-vous appliquer une marge bénéficiaire au projet ? (y/n) : ");
        String applyMargin = scanner.nextLine().trim().toLowerCase();
        double marginRate = 0;
        if (applyMargin.equals("y")) {
            System.out.print("Entrez le pourcentage de marge bénéficiaire (%) : ");
            marginRate = scanner.nextDouble() / 100;
            scanner.nextLine();
        }

        // Calculate and display costs

        double[] totals = laborMenu.calculateTotalCost(laborService.findByProject(project));
        double totalLabor = totals[0];
        double totalLaborWithVAT = totals[1];

        totals = materialMenu.calculateTotalCost(materialService.findByProject(project));
        double totalMaterials = totals[0];
        double totalMaterialsWithVAT = totals[1];


        double totalCostBeforTva = totalLaborWithVAT + totalMaterialsWithVAT;
        double totalCostAfterTva = totalCostBeforTva *(1+vatRate);

        double totalWithMargin = totalCostBeforTva * marginRate;
        projectService.update(new Project(project.getName(),totalWithMargin, totalCostBeforTva+totalWithMargin,project.getProjectStatus(),project.getClient()));

        System.out.printf("Coût total avant marge : %.2f €\n",totalCostBeforTva);
        System.out.printf("Marge bénéficiaire (%.2f%%) : %.2f €\n", marginRate * 100, totalWithMargin);
        System.out.printf("Coût total final du projet : %.2f €\n", totalCostBeforTva+totalWithMargin);

        saveQuote(scanner, project, totalCostBeforTva+totalWithMargin);
    }

    private void saveQuote(Scanner scanner, Project project, double total) {
        System.out.println("--- Enregistrement du Devis ---");
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.print("Entrez la date d'émission du devis (format : jj/mm/aaaa) : ");
        String issueDateInput = scanner.nextLine();
        LocalDate issueDate = LocalDate.parse(issueDateInput, formatter);

        System.out.print("Entrez la date de validité du devis (format : jj/mm/aaaa) : ");
        String validityDateInput = scanner.nextLine();
        LocalDate validityDate = LocalDate.parse(validityDateInput, formatter);

        // Here you can create a Quote entity and save it using the QuoteService
        // For now, we'll just print it out for demonstration
        System.out.printf("Devis enregistré :\nMontant estimé : %.2f €\nDate d'émission : %s\nDate de validité : %s\n",
                total, issueDate.format(formatter), validityDate.format(formatter));


    }


}
