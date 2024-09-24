package Menus;

import Entities.Project;
import Entities.Quote;
import Services.QuoteService;
import Services.ProjectService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ManageQuote {
    private final QuoteService quoteService;
    private final ProjectService projectService;
    private final Scanner scanner;

    public ManageQuote(QuoteService quoteService, ProjectService projectService) {
        this.quoteService = quoteService;
        this.projectService = projectService;
        this.scanner = new Scanner(System.in);
    }

    public void manageQuote() {
        boolean running = true;

        while (running) {
            System.out.println("\n*******************************************");
            System.out.println("           💼 Gestion des Devis 💼");
            System.out.println("*******************************************");
            System.out.println("1️⃣  Afficher tous les devis");
            System.out.println("2️⃣  Modifier un devis");
            System.out.println("3️⃣  Supprimer un devis");
            System.out.println("4️⃣  Retourner au menu principal");
            System.out.println("*******************************************");
            System.out.print("👉 Choisissez une option : ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    displayAllQuotes();
                    break;
                case 2:
                    editQuote();
                    break;
                case 3:
                    deleteQuote();
                    break;
                case 4:
                    running = false;
                    System.out.println("🔙 Retour au menu principal...");
                    break;
                default:
                    System.out.println("❌ Option invalide. Veuillez réessayer.");
                    break;
            }
        }
    }

    public void createQuote(Project project) {
        System.out.println("--- Créer un nouveau devis ---");

        System.out.print("Entrez la date de début du devis (yyyy-mm-dd) : ");
        LocalDate issueDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Entrez la date de validité du devis (yyyy-mm-dd) : ");
        LocalDate validityDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Le devis est-il accepté ? (true/false) : ");
        boolean isAccepted = scanner.nextBoolean();
        scanner.nextLine();


        Quote quote = new Quote(project.getTotalCost(), issueDate, validityDate, isAccepted, project);
        quoteService.save(quote);

        System.out.println("✅ Devis créé avec succès : " + quote);
    }

    private void displayAllQuotes() {
        List<Quote> quotes = quoteService.findAll();
        System.out.println("\n--- Liste des devis ---");
        for (Quote quote : quotes) {
            System.out.println(quote);
        }
    }

    private void editQuote() {
        System.out.print("Entrez l'ID du devis à modifier : ");
        Long quoteId = scanner.nextLong();
        scanner.nextLine();

        Optional<Quote> quoteOptional = quoteService.findById(quoteId);
        if (quoteOptional.isPresent()) {
            Quote quote = quoteOptional.get();

            System.out.println("Devis trouvé : " + quote);

            System.out.print("Nouveau montant estimé (laisser vide pour ne pas modifier) : ");
            String newEstimatedAmount = scanner.nextLine();
            if (!newEstimatedAmount.isEmpty()) {
                quote.setEstimatedAmount(Double.parseDouble(newEstimatedAmount));
            }

            System.out.print("Nouvelle date d'émission (yyyy-mm-dd, laisser vide pour ne pas modifier) : ");
            String newIssueDate = scanner.nextLine();
            if (!newIssueDate.isEmpty()) {
                quote.setIssueDate(LocalDate.parse(newIssueDate));
            }

            System.out.print("Nouvelle date de validité (yyyy-mm-dd, laisser vide pour ne pas modifier) : ");
            String newValidityDate = scanner.nextLine();
            if (!newValidityDate.isEmpty()) {
                quote.setValidityDate(LocalDate.parse(newValidityDate));
            }

            System.out.print("Le devis est-il accepté ? (laisser vide pour ne pas modifier) : ");
            String newIsAccepted = scanner.nextLine();
            if (!newIsAccepted.isEmpty()) {
                quote.setAccepted(Boolean.parseBoolean(newIsAccepted));
            }

            quoteService.update(quote);
            System.out.println("✅ Devis mis à jour avec succès !");
        } else {
            System.out.println("❌ Aucun devis trouvé avec l'ID : " + quoteId);
        }
    }

    private void deleteQuote() {
        System.out.print("Entrez l'ID du devis à supprimer : ");
        Long quoteId = scanner.nextLong();
        scanner.nextLine();

        if (quoteService.delete(quoteId)) {
            System.out.println("✅ Devis supprimé avec succès !");
        } else {
            System.out.println("❌ Aucun devis trouvé avec l'ID : " + quoteId);
        }
    }

    private Optional<Project> selectProject() {
        System.out.println("1️⃣  Chercher un projet existant");
        System.out.println("2️⃣  Ajouter un nouveau projet");
        System.out.print("👉 Choisissez une option : ");
        int projectChoice = scanner.nextInt();
        scanner.nextLine();

        switch (projectChoice) {
            case 1:
                return searchProject();
            case 2:
                return createProject();
            default:
                System.out.println("❌ Option invalide. Veuillez réessayer.");
                return Optional.empty();
        }
    }

    private Optional<Project> searchProject() {
        System.out.print("Entrez l'ID du projet : ");
        Long projectId = scanner.nextLong();
        scanner.nextLine();
        return projectService.findById(projectId);
    }

    private Optional<Project> createProject() {
        // Create a new project (you can call an existing method if you have a ManageProject class)
        // This is a placeholder implementation
        System.out.print("Entrez le nom du nouveau projet : ");
        String projectName = scanner.nextLine();
        Project newProject = new Project(projectName, 0, 0, null, null); // Adjust with required fields
        return Optional.of(newProject);
    }
}
