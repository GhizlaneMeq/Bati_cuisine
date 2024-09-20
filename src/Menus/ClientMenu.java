package Menus;


import Entities.Client;
import Repositories.ClientRepository;
import Services.ClientService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ClientMenu {
    private ClientRepository clientRepository = new ClientRepository();
   private ClientService clientService = new ClientService(clientRepository);
    Scanner scanner = new Scanner(System.in);

    public void create() {
        System.out.println("--- Ajout d'un nouveau client ---");
        System.out.print("Entrez le nom du client : ");
        String name = scanner.nextLine();
        System.out.print("Entrez l'adresse du client : ");
        String address = scanner.nextLine();
        System.out.print("Entrez le numéro de téléphone du client : ");
        String phone = scanner.nextLine();
        System.out.println("Choisissez si le client est professionnel (oui/non) :");
        String choice = scanner.nextLine();
        boolean isProfessional;

        if (choice.equalsIgnoreCase("oui")) {
            isProfessional = true;
        } else if (choice.equalsIgnoreCase("non")) {
            isProfessional = false;
        } else {
            System.out.println("Choix invalide. Le client sera enregistré comme non professionnel par défaut.");
            isProfessional = false;
        }

        Client client = new Client(name, address, phone, isProfessional);
        clientService.save(client);

        System.out.println("Nouveau client ajouté !");
        System.out.println("Nom : " + name);
        System.out.println("Adresse : " + address);
        System.out.println("Téléphone : " + phone);
        System.out.println("Vous pouvez maintenant créer le projet pour " + name + ".");
    }

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
