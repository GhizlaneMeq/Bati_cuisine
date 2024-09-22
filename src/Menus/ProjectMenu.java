package Menus;

import Entities.Client;
import Entities.Enum.ProjectStatus;
import Entities.Project;
import Services.ClientService;
import Services.ProjectService;

import java.util.Optional;
import java.util.Scanner;

public class ProjectMenu {
    private  ClientMenu clientMenu ;
     private  MaterialMenu materialMenu;
     private ProjectService projectService;

    public ProjectMenu(ProjectService projectService,ClientMenu clientMenu, MaterialMenu materialMenu) {
        this.clientMenu = clientMenu;
        this.materialMenu = materialMenu;
        this.projectService = projectService;
    }



    public void manageClient(Scanner scanner){
        System.out.println("--- Gestion des clients ---");
        System.out.println("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ?");
        System.out.println("1. Chercher un client existant");
        System.out.println("2. Ajouter un nouveau client");
        System.out.print("Choisissez une option : ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                SearchClient(scanner);
                break;
            case 2:
                addNewClient(scanner);
                break;
            default:
                System.out.println("Option invalide.");
                break;
        }
    }

    private void SearchClient(Scanner scanner) {
        Optional<Client> client = clientMenu.search();
        if(client.isPresent()){
            System.out.print("Souhaitez-vous continuer avec ce client ? (y/n): ");
            String choiceToContinue = scanner.nextLine().trim().toLowerCase();
            if (choiceToContinue.equals("y")) {
                addProject(scanner, client.get());
            } else if (choiceToContinue.equals("n")) {
                addNewClient(scanner);
            } else {
                System.out.println("Invalid choice. Please enter 'y' or 'n'.");
            }
        }
    }

    private void addProject(Scanner scanner,Client client) {
        try {
            System.out.println("\n--- Création d'un Nouveau Projet ---");
            System.out.print("Entrez le nom du projet: ");
            String name = scanner.nextLine();
            System.out.print("Entrez la surface de la cuisine (en m²): ");
            double surface = scanner.nextDouble();
            scanner.nextLine();
           Optional<Project> project = projectService.save(new Project(name,0,0, ProjectStatus.InProgress,client));
            materialMenu.create(project.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addNewClient(Scanner scanner){
        clientMenu.create();

    }

}
