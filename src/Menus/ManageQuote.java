package Menus;

import Entities.Enum.ProjectStatus;
import Entities.Project;
import Entities.Quote;
import Services.QuoteService;
import Services.ProjectService;

import java.time.LocalDate;
import java.util.InputMismatchException;
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
            System.out.println("           Gestion des Devis");
            System.out.println("*******************************************");
            System.out.println("1  Afficher tous les devis");
            System.out.println("2  Modifier un devis");
            System.out.println("3  Supprimer un devis");
            System.out.println("4  Accepter ou refuser un devis");
            System.out.println("5  Retourner au menu principal");
            System.out.println("*******************************************");
            System.out.print("Choisissez une option : ");

            int choice = -1;
            while (true) {
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Option invalide. Veuillez entrer un numéro valide.");
                    scanner.nextLine();
                }
            }

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
                    acceptOrRefuseQuote();
                    break;
                case 5:
                    running = false;
                    System.out.println(" Retour au menu principal...");
                    break;
                default:
                    System.out.println(" Option invalide. Veuillez réessayer.");
                    break;
            }
        }
    }

    private void acceptOrRefuseQuote() {
        Long quoteId = promptForLong("Entrez l'ID du devis à accepter ou refuser : ");
        Optional<Quote> quoteOptional = quoteService.findById(quoteId);
        if (quoteOptional.isPresent()) {
            Quote quote = quoteOptional.get();
            Project project = quote.getProject();

            System.out.println("Devis trouvé : " + quote);
            System.out.print("Voulez-vous accepter ou refuser ce devis ? (accepter/refuser) : ");
            String decision = scanner.nextLine();

            if (decision.equalsIgnoreCase("accepter")) {
                quote.setAccepted(true);
                project.setProjectStatus(ProjectStatus.Completed);
                System.out.println(" Le devis a été accepté et le projet est maintenant 'Completed'.");
            } else if (decision.equalsIgnoreCase("refuser")) {
                quote.setAccepted(false);
                project.setProjectStatus(ProjectStatus.Cancelled);
                System.out.println(" Le devis a été refusé et le projet est maintenant 'Cancelled'.");
            } else {
                System.out.println(" Option invalide. Veuillez entrer 'accepter' ou 'refuser'.");
                return;
            }

            quoteService.update(quote);
            projectService.update(project);
            System.out.println("Mise à jour du devis et du projet réussie !");
        } else {
            System.out.println(" Aucun devis trouvé avec l'ID : " + quoteId);
        }
    }

    public void createQuote(Project project) {
        System.out.println("--- Créer un nouveau devis ---");

        LocalDate issueDate = promptForDate("Entrez la date de début du devis (yyyy-mm-dd) : ");
        LocalDate validityDate = promptForDate("Entrez la date de validité du devis (yyyy-mm-dd) : ");

        Quote quote = new Quote(project.getTotalCost(), issueDate, validityDate, false, project);
        quoteService.save(quote);

        System.out.println("Devis créé avec succès : " + quote);
    }

    private void displayAllQuotes() {
        List<Quote> quotes = quoteService.findAll();
        System.out.println("\n--- Liste des devis ---");
        for (Quote quote : quotes) {
            System.out.println(quote);
        }
    }

    private void editQuote() {
        Long quoteId = promptForLong("Entrez l'ID du devis à modifier : ");
        Optional<Quote> quoteOptional = quoteService.findById(quoteId);
        if (quoteOptional.isPresent()) {
            Quote quote = quoteOptional.get();

            System.out.println("Devis trouvé : " + quote);

            String newEstimatedAmount = promptForString("Nouveau montant estimé (laisser vide pour ne pas modifier) : ");
            if (!newEstimatedAmount.isEmpty()) {
                quote.setEstimatedAmount(Double.parseDouble(newEstimatedAmount));
            }

            String newIssueDate = promptForString("Nouvelle date d'émission (yyyy-mm-dd, laisser vide pour ne pas modifier) : ");
            if (!newIssueDate.isEmpty()) {
                quote.setIssueDate(LocalDate.parse(newIssueDate));
            }

            String newValidityDate = promptForString("Nouvelle date de validité (yyyy-mm-dd, laisser vide pour ne pas modifier) : ");
            if (!newValidityDate.isEmpty()) {
                quote.setValidityDate(LocalDate.parse(newValidityDate));
            }

            String newIsAccepted = promptForString("Le devis est-il accepté ? (laisser vide pour ne pas modifier) : ");
            if (!newIsAccepted.isEmpty()) {
                quote.setAccepted(Boolean.parseBoolean(newIsAccepted));
            }

            quoteService.update(quote);
            System.out.println(" Devis mis à jour avec succès !");
        } else {
            System.out.println(" Aucun devis trouvé avec l'ID : " + quoteId);
        }
    }

    private void deleteQuote() {
        Long quoteId = promptForLong("Entrez l'ID du devis à supprimer : ");
        if (quoteService.delete(quoteId)) {
            System.out.println(" Devis supprimé avec succès !");
        } else {
            System.out.println(" Aucun devis trouvé avec l'ID : " + quoteId);
        }
    }

    private Long promptForLong(String message) {
        Long result = null;
        while (result == null) {
            System.out.print(message);
            try {
                result = scanner.nextLong();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println(" Veuillez entrer un ID valide.");
                scanner.nextLine();
            }
        }
        return result;
    }

    private LocalDate promptForDate(String message) {
        LocalDate date = null;
        while (date == null) {
            System.out.print(message);
            try {
                date = LocalDate.parse(scanner.nextLine());
            } catch (Exception e) {
                System.out.println(" Veuillez entrer une date valide au format yyyy-mm-dd.");
            }
        }
        return date;
    }

    private String promptForString(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }
}
