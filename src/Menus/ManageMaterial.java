package Menus;

import Entities.Labor;
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
            System.out.println("1️⃣  Chercher un matériau existant");
            System.out.println("2️⃣  Modifier un matériau");
            System.out.println("3️⃣  Supprimer un matériau");
            System.out.println("4️⃣  Afficher tous les matériaux");
            System.out.println("5️⃣  Retourner au menu principal");
            System.out.println("*******************************************");
            System.out.print("👉 Choisissez une option : ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    searchMaterial();
                    break;
                case 2:
                    updateMaterial();
                    break;
                case 3:
                    deleteMaterial();
                    break;
                case 4:
                    displayMaterials();
                    break;
                case 5:
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
        System.out.println("Cherchez un matériau par :");
        System.out.println("1️⃣  ID");
        System.out.println("2️⃣  Nom");
        System.out.println("3️⃣  ID du projet");
        System.out.println("🔙  Retourner au menu principal");

        System.out.print("👉 Choisissez une option : ");
        int choice = scanner.nextInt();
        scanner.nextLine(); 

        switch (choice) {
            case 1:
                System.out.print("Entrez l'ID du matériau : ");
                Long id = scanner.nextLong();
                scanner.nextLine();
                Optional<Material> materialById = materialService.findById(id);
                if (materialById.isPresent()) {
                    System.out.println("Matériau trouvé : " + materialById.get());
                } else {
                    System.out.println("Aucun matériau trouvé avec l'ID : " + id);
                }
                break;

            case 2:
                System.out.print("Entrez le nom du matériau : ");
                String name = scanner.nextLine();
                List<Material> materialsByName = materialService.findByName(name);
                if (materialsByName.isEmpty()) {
                    System.out.println("Aucun matériau trouvé avec le nom : " + name);
                } else {
                    System.out.println("Matériaux trouvés :");
                    for (Material material : materialsByName) {
                        System.out.println(material);
                    }
                }
                break;

            case 3:
                System.out.print("Entrez l'ID du projet : ");
                Long projectId = scanner.nextLong();
                scanner.nextLine();
                Optional<Project> project = projectService.findById(projectId);
                if (project.isPresent()) {
                    List<Material> materialsByProjectId = materialService.findByProject(project.get());
                    if (materialsByProjectId.isEmpty()) {
                        System.out.println("Aucun matériau trouvé pour le projet avec l'ID : " + projectId);
                    } else {
                        System.out.println("Matériaux trouvés pour le projet :");
                        for (Material material : materialsByProjectId) {
                            System.out.println(material);
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




    public Optional<List<Material>> displayMaterialsByProject(Project project) {
        List<Material> materials = materialService.findByProject(project);
        if (materials.isEmpty()) {
            System.out.println("Aucune main-d'œuvre trouvée pour le projet : " + project.getName());
            return Optional.empty();
        } else {
            System.out.println("\n--- Main-d'œuvre pour le projet : " + project.getName() + " ---");
            for (Material material : materials) {
                System.out.println(material);
            }
            return Optional.of(materials);
        }
    }

    public Optional<Material> addNewMaterial(Project project) {
        System.out.println("--- Ajouter un nouveau matériau ---");
        System.out.print("Entrez le nom du matériau : ");
        String name = scanner.nextLine();
        System.out.print("Entrez la quantité de ce matériau (en m²) : ");
        double quantity = scanner.nextDouble();
        System.out.print("Entrez le coût unitaire de ce matériau (€/m²) : ");
        double unitCost = scanner.nextDouble();
        System.out.print("Entrez le coût de transport de ce matériau (€) : ");
        double transportCost = scanner.nextDouble();
        System.out.print("Entrez le taux de TVA : ");
        double vatRate = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Entrez le coefficient de qualité du matériau (1.0 = standard, > 1.0 = haute qualité) : 1.1\n : ");
        double qualityCoefficient = getValidDoubleInput();
        scanner.nextLine();

        Material material = new Material(name, "material", vatRate, project, unitCost, quantity, transportCost, qualityCoefficient);

        Optional<Material> savedMaterial = materialService.save(material);

        if (savedMaterial.isPresent()) {
            System.out.println("Matériau ajouté avec succès : " + savedMaterial.get());
        } else {
            System.out.println("Erreur lors de l'ajout du matériau.");
        }

        return savedMaterial;
    }



    private void updateMaterial() {
        System.out.print("Entrez l'ID du matériau à mettre à jour : ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        Optional<Material> existingMaterial = materialService.findById(id);
        if (existingMaterial.isPresent()) {
            Material material = existingMaterial.get();
            System.out.println("Matériau actuel -------------------------: " + material.getId());

            System.out.println("Entrez les nouvelles informations pour ce matériau (laisser vide pour garder les valeurs actuelles) :");

            System.out.print("Nouveau nom (actuel : " + material.getName() + ") : ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) {
                material.setName(name);
            }

            System.out.print("Nouvelle quantité (actuel : " + material.getQuantity() + ") : ");
            String quantityInput = scanner.nextLine();
            if (!quantityInput.isEmpty()) {
                try {
                    material.setQuantity(Double.parseDouble(quantityInput));
                } catch (NumberFormatException e) {
                    System.out.println("Quantité invalide, gardant la valeur actuelle.");
                }
            }

            System.out.print("Nouveau coût unitaire (actuel : " + material.getUnitCost() + ") : ");
            String unitCostInput = scanner.nextLine();
            if (!unitCostInput.isEmpty()) {
                try {
                    material.setUnitCost(Double.parseDouble(unitCostInput));
                } catch (NumberFormatException e) {
                    System.out.println("Coût unitaire invalide, gardant la valeur actuelle.");
                }
            }

            System.out.print("Nouveau coût de transport (actuel : " + material.getTransportCost() + ") : ");
            String transportCostInput = scanner.nextLine();
            if (!transportCostInput.isEmpty()) {
                try {
                    material.setTransportCost(Double.parseDouble(transportCostInput));
                } catch (NumberFormatException e) {
                    System.out.println("Coût de transport invalide, gardant la valeur actuelle.");
                }
            }

            System.out.print("Nouveau taux de TVA (actuel : " + material.getVatRate() + ") : ");
            String vatRateInput = scanner.nextLine();
            if (!vatRateInput.isEmpty()) {
                try {
                    material.setVatRate(Double.parseDouble(vatRateInput));
                } catch (NumberFormatException e) {
                    System.out.println("Taux de TVA invalide, gardant la valeur actuelle.");
                }
            }

            System.out.print("Nouveau coefficient de qualité (actuel : " + material.getQualityCoefficient() + ") : ");
            String qualityCoefficientInput = scanner.nextLine();
            if (!qualityCoefficientInput.isEmpty()) {
                try {
                    material.setQualityCoefficient(Double.parseDouble(qualityCoefficientInput));
                } catch (NumberFormatException e) {
                    System.out.println("Coefficient de qualité invalide, gardant la valeur actuelle.");
                }
            }

            System.out.print("Êtes-vous sûr de vouloir enregistrer ces modifications ? (oui/non) : ");
            String confirmation = scanner.nextLine();
            if (confirmation.equalsIgnoreCase("oui")) {
                Optional<Material> updatedMaterial = materialService.update(material);
                if (updatedMaterial.isPresent()) {
                    System.out.println("Matériau mis à jour avec succès !");
                    updateProjectCosts(material.getProject());
                } else {
                    System.out.println("Erreur lors de la mise à jour du matériau.");
                }
            } else {
                System.out.println("Modification annulée.");
            }
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
        double[] newTotalCost = projectService.calculateTotalCost(project,project.getProfitMargin());
        project.setTotalCost(newTotalCost[1]);
        projectService.update(project);

        System.out.printf("Coût total du projet mis à jour : %.2f €\n", newTotalCost[1]);
    }




    private double getValidDoubleInput() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Entrée invalide. Veuillez entrer un nombre valide: ");
            }
        }
    }


}
