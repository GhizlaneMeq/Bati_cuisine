package Menus;

import Entities.Project;
import Services.ProjectService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ManageProject {
    private final ProjectService projectService;
    private final Scanner scanner;

    public ManageProject(ProjectService projectService) {
        this.projectService = projectService;
        this.scanner = new Scanner(System.in);
    }

    public void manageProjects() {
        boolean running = true;

        while (running) {
            System.out.println("\n*******************************************");
            System.out.println("           🏗️ Gestion de Projet 🏗️");
            System.out.println("*******************************************");
            System.out.println("1️⃣  Créer un nouveau projet");
            System.out.println("2️⃣  Afficher tous les projets");
            System.out.println("3️⃣  Calculer le coût d'un projet");
            System.out.println("4️⃣  Modifier un projet");
            System.out.println("5️⃣  Supprimer un projet");
            System.out.println("6️⃣  Retourner au menu principal");
            System.out.println("*******************************************");
            System.out.print("👉 Choisissez une option : ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    createProject();
                    break;
                case 2:
                    displayAllProjects();
                    break;
                case 3:
                    calculateProjectCost();
                    break;
                case 4:
                    editProject();
                    break;
                case 5:
                    deleteProject();
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

    private void createProject() {
        System.out.println("--- Créer un nouveau projet ---");
        System.out.print("Entrez le nom du projet : ");
        String name = scanner.nextLine();

        // Ask for initial profit margin (can be updated later)
        System.out.print("Entrez la marge bénéficiaire initiale (%) : ");
        double profitMargin = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline

      //  Project project = new Project(name, profitMargin, 0, null);
        //projectService.save(project);

       // System.out.println("Projet créé avec succès : " + project);
    }

    private void displayAllProjects() {
        List<Project> projects = projectService.findAll();
        System.out.println("\n--- Liste des projets ---");
        for (Project project : projects) {
            System.out.println(project);
        }
    }

    private void calculateProjectCost() {
        System.out.print("Entrez l'ID du projet : ");
        Long projectId = scanner.nextLong();
        scanner.nextLine();

        Optional<Project> projectOptional = projectService.findById(projectId);
        System.out.println(projectOptional.get().getProfitMargin());
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            project.setId(projectId);
            double[] totalCost = projectService.calculateTotalCost(project, project.getProfitMargin()/100);
            System.out.printf("Le coût total du projet '%s' est : %.2f €\n", project.getName(), totalCost[3]);
        } else {
            System.out.println("Aucun projet trouvé avec l'ID : " + projectId);
        }
    }

    private void editProject() {
        System.out.print("Entrez l'ID du projet à modifier : ");
        Long projectId = scanner.nextLong();
        scanner.nextLine();

        Optional<Project> projectOptional = projectService.findById(projectId);
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            System.out.println("Projet trouvé : " + project);

            System.out.print("Nouveau nom du projet (laisser vide pour ne pas modifier) : ");
            String newName = scanner.nextLine();
            if (!newName.isEmpty()) {
                project.setName(newName);
            }

            System.out.print("Nouveau coût total (laisser vide pour ne pas modifier) : ");
            String newCostInput = scanner.nextLine();
            if (!newCostInput.isEmpty()) {
                double newCost = Double.parseDouble(newCostInput);
                project.setTotalCost(newCost);
            }

            System.out.print("Nouvelle marge bénéficiaire (%) (laisser vide pour ne pas modifier) : ");
            String newProfitMarginInput = scanner.nextLine();
            if (!newProfitMarginInput.isEmpty()) {
                double newProfitMargin = Double.parseDouble(newProfitMarginInput);
                project.setProfitMargin(newProfitMargin);
            }

            projectService.update(project);
            System.out.println("Projet mis à jour avec succès !");
        } else {
            System.out.println("Aucun projet trouvé avec l'ID : " + projectId);
        }
    }

    private void deleteProject() {
        System.out.print("Entrez l'ID du projet à supprimer : ");
        Long projectId = scanner.nextLong();
        scanner.nextLine();

        if (projectService.delete(projectId)) {
            System.out.println("Projet supprimé avec succès !");
        } else {
            System.out.println("Aucun projet trouvé avec l'ID : " + projectId);
        }
    }
}
