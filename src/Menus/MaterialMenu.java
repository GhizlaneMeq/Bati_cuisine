package Menus;

import Entities.Material;
import Entities.Project;
import Services.MaterialService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MaterialMenu {

    private MaterialService materialService;
    private Scanner scanner = new Scanner(System.in);

    public MaterialMenu(MaterialService materialService) {
        this.materialService = materialService;
    }

    public void create(Project project) {
        List<Material> materials = new ArrayList<>();
        boolean addingMaterials = true;

        while (addingMaterials) {
            try {
                System.out.print("Entrez le nom du matériel: ");
                String name = scanner.nextLine();

                System.out.print("Entrez la quantité de ce matériau: ");
                double quantity = getValidDoubleInput();

                System.out.print("Entrez le coût unitaire de ce matériau (€/m²): ");
                double unitCost = getValidDoubleInput();

                System.out.print("Entrez le coût de transport de ce matériau (€): ");
                double transportCost = getValidDoubleInput();

                System.out.print("Entrez le coefficient de qualité du matériau (1.0 = standard, > 1.0 = haute qualité): ");
                double qualityCoefficient = getValidDoubleInput();

                Material material = new Material(name, "material", 0.20, project, unitCost, quantity, transportCost, qualityCoefficient);
                materialService.save(material);
                materials.add(material);

                System.out.println("Matériau ajouté avec succès.");

                System.out.print("Voulez-vous ajouter un autre matériau ? (y/n): ");
                String addMore = scanner.nextLine().trim().toLowerCase();
                addingMaterials = addMore.equals("y");

            } catch (Exception e) {
                System.out.println("Erreur lors de la création du matériau: " + e.getMessage());
            }
        }

        double[] totals = calculateTotalCost(materials);
        System.out.printf("Total des coûts sans TVA: %.2f €\n", totals[0]);
        System.out.printf("Total des coûts avec TVA: %.2f €\n", totals[1]);
    }

    public double[] calculateTotalCost(List<Material> materials) {
        double totalWithoutVAT = 0;
        double totalWithVAT = 0;

        for (Material material : materials) {
            double baseCost = material.getQuantity() * material.getUnitCost();
            double transportCost = material.getTransportCost();
            double qualityCoefficient = material.getQualityCoefficient();

            double totalCostBeforeVAT = (baseCost * qualityCoefficient) + transportCost;
            double totalCostWithVAT = totalCostBeforeVAT * (1 + material.getVatRate());

            totalWithoutVAT += totalCostBeforeVAT;
            totalWithVAT += totalCostWithVAT;
        }

        return new double[]{totalWithoutVAT, totalWithVAT};
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