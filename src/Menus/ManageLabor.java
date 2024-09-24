package Menus;

import Entities.Labor;
import Entities.Project;
import Services.LaborService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ManageLabor {
    private final LaborService laborService;
    private final Scanner scanner;

    public ManageLabor(LaborService laborService) {
        this.laborService = laborService;
        this.scanner = new Scanner(System.in);
    }

    public void manageLabor() {
        boolean running = true;

        while (running) {
            System.out.println("\n*******************************************");
            System.out.println("           🛠️ Gestion de Main-d'œuvre 🛠️");
            System.out.println("*******************************************");
            System.out.println("1️⃣  Ajouter une main-d'œuvre");
            System.out.println("2️⃣  Afficher toutes les main-d'œuvre");
            System.out.println("3️⃣  Modifier une main-d'œuvre");
            System.out.println("4️⃣  Supprimer une main-d'œuvre");
            System.out.println("5️⃣  Retourner au menu du projet");
            System.out.println("*******************************************");
            System.out.print("👉 Choisissez une option : ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addLabor(null);
                    break;
                case 2:
                    displayLabor(null);
                    break;
                case 3:
                    updateLabor();
                    break;
                case 4:
                    deleteLabor();
                    break;
                case 5:
                    running = false;
                    System.out.println("🔙 Retour au menu du projet...");
                    break;
                default:
                    System.out.println("❌ Option invalide. Veuillez réessayer.");
                    break;
            }
        }
    }

    public Optional<Labor> addLabor(Project project) {
        try {
            System.out.print("Entrez le type de main-d'œuvre (e.g., Ouvrier de base, Spécialiste) : ");
            String laborType = scanner.nextLine();

            System.out.print("Entrez le taux horaire (€/h) : ");
            double hourlyRate = getValidDoubleInput();

            System.out.print("Entrez le nombre d'heures travaillées : ");
            double hoursWorked = getValidDoubleInput();

            System.out.print("Entrez le facteur de productivité (1.0 = standard, > 1.0 = haute productivité) : ");
            double productivityFactor = getValidDoubleInput();

            System.out.print("Entrez le taux de TVA (en pourcentage) : ");
            double vatRate = getValidDoubleInput() ;

            Labor labor = new Labor(laborType, "labor", vatRate, project, hourlyRate, hoursWorked, productivityFactor);
            Optional<Labor> savedLabor = laborService.save(labor);

            if (savedLabor.isPresent()) {
                System.out.println("Main-d'œuvre ajoutée avec succès : " + savedLabor.get());
            } else {
                System.out.println("Erreur lors de l'ajout de la main-d'œuvre.");
            }

            return savedLabor;
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout de la main-d'œuvre : " + e.getMessage());
            return Optional.empty();
        }
    }


    private void displayLabor(Project project) {
        List<Labor> labors = laborService.findByProject(project);
        System.out.println("\n--- Liste des main-d'œuvre ---");
        for (Labor labor : labors) {
            System.out.println(labor);
        }
    }
    public Optional<List<Labor>> displayLaborByProject(Project project) {
        List<Labor> labors = laborService.findByProject(project);
        if (labors.isEmpty()) {
            System.out.println("Aucune main-d'œuvre trouvée pour le projet : " + project.getName());
            return Optional.empty();
        } else {
            System.out.println("\n--- Main-d'œuvre pour le projet : " + project.getName() + " ---");
            for (Labor labor : labors) {
                System.out.println(labor);
            }
            return Optional.of(labors);
        }
    }

    private void updateLabor() {
        System.out.print("Entrez l'ID de la main-d'œuvre à modifier : ");
        Long laborId = scanner.nextLong();
        scanner.nextLine();

        Optional<Labor> laborOptional = laborService.findById(laborId);
        if (laborOptional.isPresent()) {
            Labor labor = laborOptional.get();
            System.out.println("Main-d'œuvre trouvée : " + labor);

            System.out.print("Nouveau type de main-d'œuvre (laisser vide pour ne pas modifier) : ");
            String newName = scanner.nextLine();
            if (!newName.isEmpty()) {
                labor.setName(newName);
            }

            System.out.print("Nouveau taux horaire (laisser vide pour ne pas modifier) : ");
            String newRateInput = scanner.nextLine();
            if (!newRateInput.isEmpty()) {
                labor.setHourlyRate(Double.parseDouble(newRateInput));
            }

            System.out.print("Nouveau nombre d'heures travaillées (laisser vide pour ne pas modifier) : ");
            String newHoursInput = scanner.nextLine();
            if (!newHoursInput.isEmpty()) {
                labor.setHoursWorked(Double.parseDouble(newHoursInput));
            }

            System.out.print("Nouveau facteur de productivité (laisser vide pour ne pas modifier) : ");
            String newProductivityInput = scanner.nextLine();
            if (!newProductivityInput.isEmpty()) {
                labor.setWorkerProductivity(Double.parseDouble(newProductivityInput));
            }

            laborService.update(labor);
            System.out.println("Main-d'œuvre mise à jour avec succès !");
        } else {
            System.out.println("Aucune main-d'œuvre trouvée avec l'ID : " + laborId);
        }
    }

    private void deleteLabor() {
        System.out.print("Entrez l'ID de la main-d'œuvre à supprimer : ");
        Long laborId = scanner.nextLong();
        scanner.nextLine();

        if (laborService.delete(laborId)) {
            System.out.println("Main-d'œuvre supprimée avec succès !");
        } else {
            System.out.println("Aucune main-d'œuvre trouvée avec l'ID : " + laborId);
        }
    }

    private double getValidDoubleInput() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Entrée invalide. Veuillez entrer un nombre valide : ");
            }
        }
    }
}
