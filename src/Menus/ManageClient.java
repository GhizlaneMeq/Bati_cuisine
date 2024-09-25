package Menus;

import Entities.Client;
import Entities.Project;
import Services.ClientService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ManageClient {
    private final ClientService clientService;
    private final Scanner scanner;

    public ManageClient(ClientService clientService) {
        this.clientService = clientService;
        this.scanner = new Scanner(System.in);
    }

    public void manageClients() {
        boolean running = true;

        while (running) {
            System.out.println("\n*******************************************");
            System.out.println("            Gestion des Clients ");
            System.out.println("*******************************************");
            System.out.println("1  Ajouter un nouveau client");
            System.out.println("2  Chercher un client existant");
            System.out.println("3  Modifier un client");
            System.out.println("4  Supprimer un client");
            System.out.println("5  Afficher tous les clients");
            System.out.println("6  Retourner au menu principal");
            System.out.println("*******************************************");
            System.out.print(" Choisissez une option : ");

            int choice = getValidMenuOption(6);

            switch (choice) {
                case 1:
                    addNewClient();
                    break;
                case 2:
                    searchClient();
                    break;
                case 3:
                    modifyClient();
                    break;
                case 4:
                    deleteClient();
                    break;
                case 5:
                    displayClients();
                    break;
                case 6:
                    running = false;
                    System.out.println(" Retour au menu principal...");
                    break;
                default:
                    System.out.println(" Option invalide. Veuillez réessayer.");
                    break;
            }
        }
    }

    public Optional<Client> addNewClient() {
        System.out.println("--- Ajouter un nouveau client ---");

        System.out.print("Entrez le nom du client : ");
        String name = scanner.nextLine();

        System.out.print("Entrez l'adresse du client : ");
        String address = scanner.nextLine();

        System.out.print("Entrez le numéro de téléphone du client : ");
        String phoneNumber = scanner.nextLine();

        boolean status = getClientStatus();

        Client client = new Client(name, address, phoneNumber, status);

        try {
            Optional<Client> existingClient = clientService.findByName(name);
            if (existingClient.isPresent()) {
                System.out.println("Un client avec le même nom existe déjà.");
                return Optional.empty();
            } else {
                Client savedClient = clientService.save(client);
                System.out.println("Client ajouté avec succès.");
                return Optional.of(savedClient);
            }
        } catch (Exception e) {
            System.out.println("Une erreur s'est produite lors de l'ajout du client : " + e.getMessage());
            return Optional.empty();
        }
    }

    private boolean getClientStatus() {
        while (true) {
            System.out.print("Le client est-il professionnel ? (oui/non) : ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("oui")) {
                return true;
            } else if (input.equals("non")) {
                return false;
            } else {
                System.out.println(" Entrée invalide. Veuillez entrer 'oui' ou 'non'.");
            }
        }
    }

    public Optional<Client> searchClient() {
        System.out.print("Entrez le nom du client à rechercher : ");
        String name = scanner.nextLine();

        try {
            Optional<Client> client = clientService.findByName(name);
            if (client.isPresent()) {
                System.out.println("Client trouvé : " + client.get());
                return client;
            } else {
                System.out.println("Aucun client trouvé avec le nom : " + name);
                return Optional.empty();
            }
        } catch (Exception e) {
            System.out.println("Une erreur s'est produite lors de la recherche du client : " + e.getMessage());
            return Optional.empty();
        }
    }

    private void modifyClient() {
        System.out.print("Entrez le nom du client à modifier : ");
        String name = scanner.nextLine();

        Optional<Client> existingClient = clientService.findByName(name);
        if (existingClient.isPresent()) {
            Client client = existingClient.get();
            System.out.println("Client trouvé : " + client);

            System.out.print("Êtes-vous sûr de vouloir modifier ce client ? (y/n) : ");
            String confirm = scanner.nextLine().trim().toLowerCase();

            if (confirm.equals("y")) {
                updateClientDetails(client);
                clientService.update(client);
                System.out.println("Client mis à jour avec succès !");
            } else {
                System.out.println("Modification annulée.");
            }
        } else {
            System.out.println("Aucun client trouvé avec le nom : " + name);
        }
    }

    private void updateClientDetails(Client client) {
        System.out.print("Modifier le nom (actuel : " + client.getName() + ") : ");
        String newName = scanner.nextLine();
        if (!newName.trim().isEmpty()) {
            client.setName(newName);
        }

        System.out.print("Modifier l'adresse (actuelle : " + client.getAddress() + ") : ");
        String newAddress = scanner.nextLine();
        if (!newAddress.trim().isEmpty()) {
            client.setAddress(newAddress);
        }

        System.out.print("Modifier le téléphone (actuel : " + client.getPhone() + ") : ");
        String newPhone = scanner.nextLine();
        if (!newPhone.trim().isEmpty()) {
            client.setPhone(newPhone);
        }

        System.out.print("Le client est-il professionnel (actuel : " + (client.isProfessional() ? "Oui" : "Non") + ") ? (oui/non) : ");
        String statusInput = scanner.nextLine().trim().toLowerCase();
        if (statusInput.equals("oui")) {
            client.setProfessional(true);
        } else if (statusInput.equals("non")) {
            client.setProfessional(false);
        }
    }

    private void deleteClient() {
        System.out.print("Entrez le nom du client à supprimer : ");
        String name = scanner.nextLine();

        Optional<Client> existingClient = clientService.findByName(name);
        if (existingClient.isPresent()) {
            System.out.print("Êtes-vous sûr de vouloir supprimer ce client ? (y/n) : ");
            String confirm = scanner.nextLine().trim().toLowerCase();

            if (confirm.equals("y")) {
                clientService.delete(existingClient.get().getId());
                System.out.println("Client supprimé avec succès !");
            } else {
                System.out.println("Suppression annulée.");
            }
        } else {
            System.out.println("Aucun client trouvé avec le nom : " + name);
        }
    }

    private void displayClients() {
        List<Client> clients = clientService.findAll();
        System.out.println("\n--- Liste des clients ---");

        for (Client client : clients) {
            System.out.printf("Nom: %s, Adresse: %s, Téléphone: %s, Professionnel: %s\n",
                    client.getName(), client.getAddress(), client.getPhone(),
                    client.isProfessional() ? "Oui" : "Non");

            List<Project> projects = clientService.findProjectsByClient(client);
            if (projects.isEmpty()) {
                System.out.println("  Aucun projet assigné.");
            } else {
                System.out.println("  Projets assignés :");
                projects.forEach(project -> System.out.println("  - " + project.getName()));
            }
        }
    }

    private int getValidMenuOption(int maxOption) {
        while (true) {
            int option = getValidIntegerInput();
            if (option >= 1 && option <= maxOption) {
                return option;
            } else {
                System.out.println(" Option invalide. Veuillez choisir une option entre 1 et " + maxOption + ".");
            }
        }
    }

    private int getValidIntegerInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Entrée invalide. Veuillez entrer un nombre entier valide : ");
            }
        }
    }
}
