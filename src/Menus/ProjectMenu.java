package Menus;

import Entities.*;
import Entities.Enum.ProjectStatus;
import Services.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Scanner;

public class ProjectMenu {
    private ClientMenu clientMenu;
    private MaterialMenu materialMenu;
    private LaborMenu laborMenu;
    private ProjectService projectService;
    private QuoteService quoteService;

    public ProjectMenu(ProjectService projectService, ClientMenu clientMenu, MaterialMenu materialMenu, LaborMenu laborMenu,
                       QuoteService quoteService) {
        this.clientMenu = clientMenu;
        this.materialMenu = materialMenu;
        this.laborMenu = laborMenu;
        this.projectService = projectService;
        this.quoteService = quoteService;
    }

    public void manageClient(Scanner scanner) {
        boolean inClientManagement = true;
        while (inClientManagement) {
            System.out.println("--- Gestion des clients ---");
            System.out.println("1. Chercher un client existant");
            System.out.println("2. Ajouter un nouveau client");
            System.out.println("3. Retourner au menu précédent");
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
                case 3:
                    inClientManagement = false;
                    break;
                default:
                    System.out.println("Option invalide.");
                    break;
            }
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

            double marginProfit =0.0;
            scanner.nextLine();

            Project project = new Project(name, marginProfit , 0, ProjectStatus.InProgress, client);
            Optional<Project> savedProject = projectService.save(project);

            materialMenu.create(savedProject.get());
            laborMenu.create(savedProject.get());

          calculateTotalCost(scanner, savedProject.get());

        } catch (Exception e) {
            System.out.println("Erreur lors de la création du projet: " + e.getMessage());
        }
    }

    private void calculateTotalCost(Scanner scanner, Project project) {
        System.out.print("Souhaitez-vous appliquer une marge bénéficiaire au projet ? (y/n) : ");
        String applyMargin = scanner.nextLine().trim().toLowerCase();
        double marginRate = 0;
        if (applyMargin.equals("y")) {
            System.out.print("Entrez le pourcentage de marge bénéficiaire (%) : ");
            marginRate = scanner.nextDouble() / 100;
            scanner.nextLine();
        }

        double[] totals = projectService.calculateTotalCost(project, marginRate);

        System.out.printf("Coût total avant marge : %.2f €\n", totals[1]);
        System.out.printf("Marge bénéficiaire (%.2f%%) : %.2f €\n", marginRate * 100, totals[2]);
        System.out.printf("Coût total final du projet : %.2f €\n", totals[3]);

        project.setProfitMargin(marginRate * 100);
        project.setTotalCost(totals[1]);

        projectService.update(project);

        saveQuote(scanner, project, totals[3]);
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

        quoteService.save(new Quote(total, issueDate, validityDate, false, project));
        System.out.printf("Devis enregistré :\nMontant estimé : %.2f €\nDate d'émission : %s\nDate de validité : %s\n",
                total, issueDate.format(formatter), validityDate.format(formatter));
    }


    public void addNewClient(Scanner scanner) {
        clientMenu.create();
    }
}
