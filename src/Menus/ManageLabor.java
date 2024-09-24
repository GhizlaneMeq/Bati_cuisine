package Menus;

import Entities.Labor;
import Entities.Material;
import Entities.Project;
import Services.LaborService;
import Services.ProjectService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ManageLabor {
    private final LaborService laborService;
    private final ProjectService projectService;
    private final Scanner scanner;

    public ManageLabor(LaborService laborService, ProjectService projectService) {
        this.laborService = laborService;
        this.projectService = projectService;
        this.scanner = new Scanner(System.in);
    }

    public void manageLabor() {
        boolean running = true;

        while (running) {
            System.out.println("\n*******************************************");
            System.out.println("           🛠️ Gestion de Main-d'œuvre 🛠️");
            System.out.println("*******************************************");
            System.out.println("1️⃣  Afficher toutes les main-d'œuvre");
            System.out.println("2️⃣  Modifier une main-d'œuvre");
            System.out.println("3️⃣  Supprimer une main-d'œuvre");
            System.out.println("4️⃣  Chercher une main-d'œuvre");
            System.out.println("5️⃣  Retourner au menu du projet");
            System.out.println("*******************************************");
            System.out.print("👉 Choisissez une option : ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    displayLabor();
                    break;
                case 2:
                    updateLabor();
                    break;
                case 3:
                    deleteLabor();
                    break;
                case 4:
                    searchLabor();
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

    private void searchLabor() {
        System.out.println("Cherchez une main-d'œuvre par :");
        System.out.println("1️⃣  ID");
        System.out.println("2️⃣  Nom");
        System.out.println("3️⃣  ID du projet");
        System.out.println("🔙  Retourner au menu principal");

        System.out.print("👉 Choisissez une option : ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Entrez l'ID de la main-d'œuvre : ");
                Long laborId = scanner.nextLong();
                scanner.nextLine();
                Optional<Labor> laborById = laborService.findById(laborId);
                if (laborById.isPresent()) {
                    System.out.println("Main-d'œuvre trouvée : " + laborById.get());
                } else {
                    System.out.println("Aucune main-d'œuvre trouvée avec l'ID : " + laborId);
                }
                break;

            case 2:
                System.out.print("Entrez le nom de la main-d'œuvre : ");
                String laborName = scanner.nextLine();
                List<Labor> laborsByName = laborService.findByName(laborName);
                if (laborsByName.isEmpty()) {
                    System.out.println("Aucune main-d'œuvre trouvée avec le nom : " + laborName);
                } else {
                    System.out.println("Main-d'œuvre trouvées :");
                    for (Labor labor : laborsByName) {
                        System.out.println(labor);
                    }
                }
                break;

            case 3:
                System.out.print("Entrez l'ID du projet : ");
                Long projectId = scanner.nextLong();
                scanner.nextLine();
                Optional<Project> project = projectService.findById(projectId);
                if (project.isPresent()) {
                    List<Labor> laborsByProjectId = laborService.findByProject(project.get());
                    if (laborsByProjectId.isEmpty()) {
                        System.out.println("Aucune main-d'œuvre trouvée pour le projet avec l'ID : " + projectId);
                    } else {
                        System.out.println("Main-d'œuvre trouvées pour le projet :");
                        for (Labor labor : laborsByProjectId) {
                            System.out.println(labor);
                        }
                    }
                } else {
                    System.out.println("Aucun projet trouvé avec l'ID : " + projectId);
                }
                break;

            case 4:
                System.out.println("Retour au menu principal.");
                break;

            default:
                System.out.println("❌ Option invalide. Veuillez réessayer.");
                break;
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
            double vatRate = getValidDoubleInput();

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

    private void displayLabor() {
        List<Labor> labors = laborService.findAll();
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

            System.out.println("Entrez les nouvelles informations pour cette main-d'œuvre (laisser vide pour garder les valeurs actuelles) :");
            updateLaborDetails(labor);

            Optional<Labor> updatedLabor = laborService.update(labor);
            if (updatedLabor.isPresent()) {
                System.out.println("Main-d'œuvre mise à jour avec succès !");
                updateProjectCosts(labor.getProject());
            } else {
                System.out.println("Erreur lors de la mise à jour de la main-d'œuvre.");
            }
        } else {
            System.out.println("Aucune main-d'œuvre trouvée avec l'ID : " + laborId);
        }
    }

    private void updateLaborDetails(Labor labor) {
        System.out.print("Nouveau type de main-d'œuvre (actuel : " + labor.getName() + ", laisser vide pour ne pas modifier) : ");
        String newName = scanner.nextLine();
        if (!newName.isEmpty()) {
            labor.setName(newName);
        }

        System.out.print("Nouveau taux horaire (actuel : " + labor.getHourlyRate() + ", laisser vide pour ne pas modifier) : ");
        String newRateInput = scanner.nextLine();
        if (!newRateInput.isEmpty()) {
            try {
                labor.setHourlyRate(Double.parseDouble(newRateInput));
            } catch (NumberFormatException e) {
                System.out.println("Taux horaire invalide, gardant la valeur actuelle.");
            }
        }

        System.out.print("Nouveau nombre d'heures travaillées (actuel : " + labor.getHoursWorked() + ", laisser vide pour ne pas modifier) : ");
        String newHoursInput = scanner.nextLine();
        if (!newHoursInput.isEmpty()) {
            try {
                labor.setHoursWorked(Double.parseDouble(newHoursInput));
            } catch (NumberFormatException e) {
                System.out.println("Nombre d'heures invalide, gardant la valeur actuelle.");
            }
        }

        System.out.print("Nouveau facteur de productivité (actuel : " + labor.getWorkerProductivity() + ", laisser vide pour ne pas modifier) : ");
        String newProductivityInput = scanner.nextLine();
        if (!newProductivityInput.isEmpty()) {
            try {
                labor.setWorkerProductivity(Double.parseDouble(newProductivityInput));
            } catch (NumberFormatException e) {
                System.out.println("Facteur de productivité invalide, gardant la valeur actuelle.");
            }
        }

        System.out.print("Nouveau taux de TVA (actuel : " + labor.getVatRate() + ", laisser vide pour ne pas modifier) : ");
        String newVatRateInput = scanner.nextLine();
        if (!newVatRateInput.isEmpty()) {
            try {
                labor.setVatRate(Double.parseDouble(newVatRateInput));
            } catch (NumberFormatException e) {
                System.out.println("Taux de TVA invalide, gardant la valeur actuelle.");
            }
        }
    }

    private void deleteLabor() {
        System.out.print("Entrez l'ID de la main-d'œuvre à supprimer : ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        Optional<Labor> existingLabor = laborService.findById(id);
        if (existingLabor.isPresent()) {
            System.out.print("Êtes-vous sûr de vouloir supprimer cette main-d'œuvre ? (oui/non) : ");
            String confirmation = scanner.nextLine();
            if (!confirmation.equalsIgnoreCase("oui")) {
                System.out.println("Suppression annulée.");
                return;
            }

            Project project = existingLabor.get().getProject();

            if (laborService.delete(id)) {
                System.out.println("Main-d'œuvre supprimée avec succès !");
                updateProjectCosts(project);
            } else {
                System.out.println("Erreur lors de la suppression de la main-d'œuvre.");
            }
        } else {
            System.out.println("Aucune main-d'œuvre trouvée avec l'ID : " + id);
        }
    }

    private void updateProjectCosts(Project project) {
        double[] newTotalCost = projectService.calculateTotalCost(project, project.getProfitMargin());
        project.setTotalCost(newTotalCost[1]);
        projectService.update(project);

        System.out.printf("Coût total du projet mis à jour : %.2f €\n", newTotalCost[1]);
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
