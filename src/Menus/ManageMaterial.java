package Menus;

import Entities.Material;
import Entities.Project;
import Services.MaterialService;
import Services.ProjectService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ManageMaterial {
    private MaterialService materialService;
    private ProjectService projectService;
    private Scanner scanner;

    public ManageMaterial(MaterialService materialService, ProjectService projectService) {
        this.materialService = materialService;
        this.projectService = projectService;
        this.scanner = new Scanner(System.in);
    }

    public void manageMaterials() {
        boolean running = true;

        while (running) {
            System.out.println("\n*******************************************");
            System.out.println("           🏗️ Gestion des Matériaux 🏗️");
            System.out.println("*******************************************");
            System.out.println("1️⃣  Ajouter un nouveau matériau");
            System.out.println("2️⃣  Chercher un matériau existant");
            System.out.println("3️⃣  Modifier un matériau");
            System.out.println("4️⃣  Supprimer un matériau");
            System.out.println("5️⃣  Afficher tous les matériaux");
            System.out.println("6️⃣  Retourner au menu principal");
            System.out.println("*******************************************");
            System.out.print("👉 Choisissez une option : ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addNewMaterial();
                    break;
                case 2:
                    searchMaterial();
                    break;
                case 3:
                    modifyMaterial();
                    break;
                case 4:
                    deleteMaterial();
                    break;
                case 5:
                    displayMaterials();
                    break;
                case 6:
                    running = false;
                    System.out.println("🔙 Retour au menu principal...");
                    break;
                default:
                    System.out.println("❌ Option invalide. Veuillez réessayer.");
                    break;
            }
        }
    }

    private void searchMaterial() {
    }

    private void addNewMaterial() {
        System.out.println("--- Ajouter un nouveau matériau ---");
        System.out.print("Entrez le nom du matériau : ");
        String name = scanner.nextLine();
        System.out.print("Entrez le taux de TVA : ");
        double vatRate = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Entrez l'ID du projet (ou laissez vide pour en créer un nouveau) : ");
        String projectIdInput = scanner.nextLine();

        Project project = null;

        if (projectIdInput.isEmpty()) {
            System.out.println("Vous pouvez créer un nouveau projet avec ces matériaux.");
            // Call project management logic here to create a new project.
            // projectManagementMenu();
            return; // Exiting for now, implement project creation logic
        } else {
            Long projectId = Long.parseLong(projectIdInput);
            Optional<Project> projectOptional = projectService.findById(projectId);
            if (!projectOptional.isPresent()) {
                System.out.println("Aucun projet trouvé avec l'ID : " + projectId);
                return;
            }
            project = projectOptional.get();
        }

        System.out.print("Entrez le coût unitaire : ");
        double unitCost = scanner.nextDouble();
        System.out.print("Entrez la quantité : ");
        double quantity = scanner.nextDouble();
        System.out.print("Entrez le coût de transport : ");
        double transportCost = scanner.nextDouble();
        System.out.print("Entrez le coefficient de qualité : ");
        double qualityCoefficient = scanner.nextDouble();
        scanner.nextLine();

        Material material = new Material(name, "material", vatRate, project, unitCost, quantity, transportCost, qualityCoefficient);

        Optional<Material> savedMaterial = materialService.save(material);
        if (savedMaterial.isPresent()) {
            System.out.println("Matériau ajouté avec succès : " + savedMaterial.get());
            if (project != null) {
                updateProjectCosts(project);
            } else {
                System.out.println("Aucun projet associé pour mettre à jour les coûts.");
            }
        } else {
            System.out.println("Erreur lors de l'ajout du matériau.");
        }
    }


    private void modifyMaterial() {
        System.out.print("Entrez l'ID du matériau à modifier : ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        Optional<Material> existingMaterial = materialService.findById(id);
        if (existingMaterial.isPresent()) {
            Material material = existingMaterial.get();
            System.out.println("Matériau trouvé : " + material);

            System.out.print("Êtes-vous sûr de vouloir modifier ce matériau ? (oui/non) : ");
            String confirmation = scanner.nextLine();
            if (!confirmation.equalsIgnoreCase("oui")) {
                System.out.println("Modification annulée.");
                return;
            }

            // Update material details here...
            // Similar to how you would do in the previous example
            // Then save the material
            materialService.update(material);
            System.out.println("Matériau mis à jour avec succès !");
            updateProjectCosts(material.getProject());
        } else {
            System.out.println("Aucun matériau trouvé avec l'ID : " + id);
        }
    }

    private void deleteMaterial() {
        System.out.print("Entrez l'ID du matériau à supprimer : ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        Optional<Material> existingMaterial = materialService.findById(id);
        if (existingMaterial.isPresent()) {
            System.out.print("Êtes-vous sûr de vouloir supprimer ce matériau ? (oui/non) : ");
            String confirmation = scanner.nextLine();
            if (!confirmation.equalsIgnoreCase("oui")) {
                System.out.println("Suppression annulée.");
                return;
            }

            if (materialService.delete(id)) {
                System.out.println("Matériau supprimé avec succès !");
                updateProjectCosts(existingMaterial.get().getProject());
            } else {
                System.out.println("Erreur lors de la suppression du matériau.");
            }
        } else {
            System.out.println("Aucun matériau trouvé avec l'ID : " + id);
        }
    }

    private void displayMaterials() {
        List<Material> materials = materialService.findAll();
        System.out.println("\n--- Liste des matériaux ---");
        for (Material material : materials) {
            System.out.println(material);
        }
    }

    private void updateProjectCosts(Project project) {
        // Logic to calculate and update project costs
        double[] totals = materialService.calculateTotalCost(materialService.findByProject(project));
        double totalMaterials = totals[0];
        double totalMaterialsWithVAT = totals[1];

        // Assuming the Project class has methods to update costs
        double newTotalCost = project.getTotalCost() + totalMaterialsWithVAT; // Add your logic here
        projectService.update(new Project(project.getId(), project.getName(), newTotalCost, project.getProjectStatus(), project.getClient()));

        System.out.printf("Coût total du projet mis à jour : %.2f €\n", newTotalCost);
    }
}
