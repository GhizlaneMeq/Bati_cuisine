package Menus;

import Entities.Labor;
import Entities.Project;
import Services.LaborService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LaborMenu {
    private LaborService laborService;
    private Scanner scanner = new Scanner(System.in);

    public LaborMenu(LaborService laborService) {
        this.laborService = laborService;
    }

    public void create(Project project) {
        List<Labor> laborEntries = new ArrayList<>();
        boolean addingLabor = true;

        while (addingLabor) {
            try {
                System.out.print("Entrez le type de main-d'œuvre (e.g., Ouvrier de base, Spécialiste): ");
                String laborType = scanner.nextLine();

                System.out.print("Entrez le taux horaire de cette main-d'œuvre (€/h): ");
                double hourlyRate = getValidDoubleInput();

                System.out.print("Entrez le nombre d'heures travaillées: ");
                double hoursWorked = getValidDoubleInput();

                System.out.print("Entrez le facteur de productivité (1.0 = standard, > 1.0 = haute productivité): ");
                double productivityFactor = getValidDoubleInput();

                Labor labor = new Labor(laborType, "labor", 0.0, project, hourlyRate, hoursWorked, productivityFactor);
                laborService.save(labor);
                laborEntries.add(labor);

                System.out.println("Main-d'œuvre ajoutée avec succès.");

                System.out.print("Voulez-vous ajouter un autre type de main-d'œuvre ? (y/n): ");
                String addMore = scanner.nextLine().trim().toLowerCase();
                addingLabor = addMore.equals("y");

            } catch (Exception e) {
                System.out.println("Erreur lors de la création de la main-d'œuvre: " + e.getMessage());
            }
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
