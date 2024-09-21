package Menus;

import Entities.Client;
import Services.ClientService;

import java.util.Optional;
import java.util.Scanner;

public class ProjectMenu {
    private ClientService clientService;
    private  ClientMenu clientMenu ;
    public ProjectMenu(ClientService clientService) {
        this.clientService = clientService;
        this.clientMenu = new ClientMenu(clientService);
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
        System.out.println("\n--- Recherche d'un client existant ---");
        System.out.print("Entrez le nom du client : ");
        String name = scanner.nextLine();

        try {
            Optional<Client> client = clientService.findByName(name);

            if (client.isPresent()) {
                System.out.println("\nClient trouvé :");
                Client foundClient = client.get();
                System.out.println("Nom : " + foundClient.getName());
                System.out.println("Adresse : " + foundClient.getAddress());
                System.out.println("Numéro de téléphone : " + foundClient.getPhone());
                System.out.println("Statut professionnel : " + (foundClient.isProfessional() ? "Oui" : "Non"));
            } else {
                System.out.println("Aucun client trouvé avec le nom : " + name);
            }
        } catch (Exception e) {
            System.out.println("Une erreur s'est produite lors de la recherche du client : " + e.getMessage());
        }
    }

    public void addNewClient(Scanner scanner){
        clientMenu.create();

    }

}
