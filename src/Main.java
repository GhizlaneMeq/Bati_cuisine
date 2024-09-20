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
                    creerNouveauProjet(scanner);
                    break;
                case 2:
                    afficherProjets();
                    break;
                case 3:
                    calculerCoutProjet();
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

    private static void creerNouveauProjet(Scanner scanner) {
        System.out.println("--- Recherche de client ---");
        System.out.println("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ?");
        System.out.println("1. Chercher un client existant");
        System.out.println("2. Ajouter un nouveau client");
        System.out.print("Choisissez une option : ");

        int clientChoice = scanner.nextInt();
        scanner.nextLine();

        switch (clientChoice) {
            case 1:
                rechercherClient(scanner);
                break;
            case 2:
                ajouterNouveauClient(scanner);
                break;
            default:
                System.out.println("Option invalide.");
                break;
        }
    }

    private static void rechercherClient(Scanner scanner) {
        System.out.println("--- Recherche de client existant ---");
        System.out.print("Entrez le nom du client : ");
        String nomClient = scanner.nextLine();

        // Simulation de recherche du client dans la base de données
        if ("Mme Dupont".equalsIgnoreCase(nomClient)) {
            System.out.println("Client trouvé !");
            System.out.println("Nom : Mme Dupont");
            System.out.println("Adresse : 12 Rue des Fleurs, Paris");
            System.out.println("Numéro de téléphone : 06 12345678");

            System.out.print("Souhaitez-vous continuer avec ce client ? (y/n) : ");
            String choix = scanner.nextLine();
            if ("y".equalsIgnoreCase(choix)) {
                System.out.println("Vous pouvez maintenant créer le projet pour Mme Dupont.");
                // Logique pour continuer à créer le projet...
            } else {
                System.out.println("Retour au menu principal.");
            }
        } else {
            System.out.println("Client non trouvé.");
        }
    }

    private static void ajouterNouveauClient(Scanner scanner) {
        System.out.println("--- Ajout d'un nouveau client ---");
        System.out.print("Entrez le nom du client : ");
        String nom = scanner.nextLine();
        System.out.print("Entrez l'adresse du client : ");
        String adresse = scanner.nextLine();
        System.out.print("Entrez le numéro de téléphone du client : ");
        String telephone = scanner.nextLine();
        System.out.println("Nouveau client ajouté !");
        System.out.println("Nom : " + nom);
        System.out.println("Adresse : " + adresse);
        System.out.println("Téléphone : " + telephone);

        System.out.println("Vous pouvez maintenant créer le projet pour " + nom + ".");
    }

    private static void afficherProjets() {
        System.out.println("--- Affichage des projets existants ---");
        System.out.println("Aucun projet disponible pour le moment.");
    }

    private static void calculerCoutProjet() {
        System.out.println("--- Calcul du coût du projet ---");
        System.out.println("Fonctionnalité à implémenter.");
    }
}
