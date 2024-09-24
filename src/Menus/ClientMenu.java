package Menus;

import Entities.Client;
import Services.ClientService;

import java.util.Optional;
import java.util.Scanner;

public class ClientMenu {
    private ClientService clientService;
    Scanner scanner = new Scanner(System.in);

    public ClientMenu(ClientService clientService) {
        this.clientService = clientService;
    }

    public Client create() {
        System.out.println("\n--- Ajouter un nouveau client ---");
        System.out.print("Entrez le nom du client : ");
        String name = scanner.nextLine();
        System.out.print("Entrez l'adresse du client : ");
        String address = scanner.nextLine();
        System.out.print("Entrez le numéro de téléphone du client : ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Le client est-il professionnel (true pour oui, false pour non) : ");
        boolean status = scanner.nextBoolean();
        scanner.nextLine();

        Client client = new Client(name, address, phoneNumber, status);

        try {
            Client savedClient = clientService.save(client);
            if (savedClient == null) {
                System.out.println("Un client avec le même nom existe déjà.");
                return null;
            } else {
                System.out.println("Client ajouté avec succès.");
                return savedClient;
            }
        } catch (Exception e) {
            System.out.println("Une erreur s'est produite lors de l'ajout du client : " + e.getMessage());
            return null;
        }
    }

    public Optional<Client> search() {
        System.out.println("\n--- Recherche d'un client existant ---");
        System.out.print("Entrez le nom du client : ");
        String name = scanner.nextLine();

        try {
            Optional<Client> client = clientService.findByName(name);

            if (client.isPresent()) {
                Client foundClient = client.get();
                System.out.println("\nClient trouvé :");
                System.out.println("Nom : " + foundClient.getName());
                System.out.println("Adresse : " + foundClient.getAddress());
                System.out.println("Numéro de téléphone : " + foundClient.getPhone());
                System.out.println("Statut professionnel : " + (foundClient.isProfessional() ? "Oui" : "Non"));
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
}

/*

    public void update(){
        System.out.println("--- Modifier un client ---");
        System.out.print("Entrez l'identifiant du client à modifier : ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Optional<Client> existingClient = clientService.findById((long) id);
        if (existingClient.isPresent()) {
            Client client = existingClient.get();

            System.out.println("Modifier le nom (actuel : " + client.getName() + ") : ");
            String name = scanner.nextLine();
            if (!name.trim().isEmpty()) {
                client.setName(name);
            }

            System.out.println("Modifier l'adresse (actuelle : " + client.getAddress() + ") : ");
            String address = scanner.nextLine();
            if (!address.trim().isEmpty()) {
                client.setAddress(address);
            }

            System.out.println("Modifier le téléphone (actuel : " + client.getPhone() + ") : ");
            String phone = scanner.nextLine();
            if (!phone.trim().isEmpty()) {
                client.setPhone(phone);
            }
            System.out.println("Le client est-il professionnel (actuel : " + (client.isProfessional() ? "Oui" : "Non") + ") ?");
            System.out.println("1. Oui");
            System.out.println("2. Non");
            String choice = scanner.nextLine();
            scanner.nextLine();

            if (choice.equalsIgnoreCase("oui")) {
                client.setProfessional(true);
            } else if (choice.equalsIgnoreCase("non")) {
                client.setProfessional(false);
            } else {
                System.out.println("Choix invalide. Statut professionnel inchangé.");
            }
            clientService.update(client);
            System.out.println("Client mis à jour avec succès !");
        } else {
            System.out.println("Aucun client trouvé avec l'identifiant : " + id);
        }
    }

    public void delete() throws SQLException {
        System.out.println("--- Supprimer un client ---");
        System.out.print("Entrez l'identifiant du client à supprimer : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Optional<Client> existingClient = clientService.findById((long) id);
        if (existingClient.isPresent()) {
            clientService.delete((long) id);
            System.out.println("Client supprimé avec succès !");
        } else {
            System.out.println("Aucun client trouvé avec l'identifiant : " + id);
        }
    }

    public void displayAllClient() throws SQLException {
        System.out.println("--- Liste de tous les clients ---");
        List<Client> clients = clientService.findAll();
        if (clients.isEmpty()) {
            System.out.println("Aucun client trouvé.");
        } else {
            for (Client client : clients) {
                System.out.println(client.toString());
            }
        }
    }
}

 */
