package Menus;

import Entities.Material;
import Entities.Project;
import Services.MaterialService;

import java.util.Optional;
import java.util.Scanner;

public class MaterialMenu {

    private MaterialService materialService;
    private Scanner scanner = new Scanner(System.in);

    public MaterialMenu(MaterialService materialService) {
        this.materialService = materialService;
    }

    public Optional<Material> create(Project project) {
        try {
            System.out.print("Entrez le nom du matériel: ");
            String name = scanner.nextLine();

            System.out.print("Entrez la quantité de ce matériau: ");
            double quantity = getValidDoubleInput();

            System.out.print("Entrez le coût unitaire de ce matériau (€/m²): ");
            double unitCost = getValidDoubleInput();

            System.out.print("Entrez le coût de transport de ce matériau (€): ");
            double transportCost = getValidDoubleInput();

            System.out.print("Entrez le taux de TVA : ");
            double vatRate = getValidDoubleInput();

            System.out.print("Entrez le coefficient de qualité du matériau (1.0 = standard, > 1.0 = haute qualité): ");
            double qualityCoefficient = getValidDoubleInput();

            Material material = new Material(name, "material", vatRate, project, unitCost, quantity, transportCost, qualityCoefficient);
            materialService.save(material);

            System.out.println("Matériau ajouté avec succès.");
            return Optional.of(material);

        } catch (Exception e) {
            System.out.println("Erreur lors de la création du matériau: " + e.getMessage());
            return Optional.empty();
        }
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
