package Menus;

import Entities.Client;
import Entities.Enum.ProjectStatus;
import Entities.Labor;
import Entities.Material;
import Entities.Project;
import Services.ProjectService;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ManageProject {
    private final ProjectService projectService;
    private final ManageClient manageClient;
    private final ManageMaterial manageMaterial;
    private final ManageLabor manageLabor;
    private final Scanner scanner;
    private final ManageQuote manageQuote;

    public ManageProject(ProjectService projectService, ManageClient manageClient, ManageMaterial manageMaterial, ManageLabor manageLabor, ManageQuote manageQuote) {
        this.projectService = projectService;
        this.manageClient = manageClient;
        this.manageMaterial = manageMaterial;
        this.manageLabor = manageLabor;
        this.manageQuote = manageQuote;
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
            scanner.nextLine();

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

        Optional<Client> clientOptional = selectOrAddClient();
        if (clientOptional.isEmpty()) {
            System.out.println("Création de projet annulée, aucun client sélectionné.");
            return;
        }

        System.out.print("Entrez le nom du projet : ");
        String projectName = scanner.nextLine();

        Project project = new Project(projectName, 0, 0, ProjectStatus.InProgress, clientOptional.get());
        projectService.save(project);
        addMaterialsToProject(project);
        addLaborToProject(project);

        System.out.print("Souhaitez-vous appliquer une marge bénéficiaire au projet ? (y/n) : ");
        String applyMargin = scanner.nextLine().trim().toLowerCase();
        if (applyMargin.equals("y")) {
            System.out.print("Entrez le pourcentage de marge bénéficiaire (%) : ");
            double profitMargin = scanner.nextDouble();
            scanner.nextLine();
            project.setProfitMargin(profitMargin);
            projectService.update(project);
            double[] totalCostDetails = projectService.calculateTotalCost(project, profitMargin / 100);
            project.setTotalCost(totalCostDetails[1]);
            projectService.update(project);
        } else {
            System.out.println("Aucune marge bénéficiaire appliquée.");
        }
        manageQuote.createQuote(project);
    }


    private void addMaterialsToProject(Project project) {
        boolean addingMaterials = true;
        while (addingMaterials) {
            manageMaterial.addNewMaterial(project);
            System.out.print("Voulez-vous ajouter un autre matériau ? (y/n) : ");
            String anotherMaterial = scanner.nextLine().trim().toLowerCase();
            addingMaterials = anotherMaterial.equals("y");
        }
    }

    private void addLaborToProject(Project project) {
        boolean addingLabor = true;
        while (addingLabor) {
            manageLabor.addLabor(project);
            System.out.print("Voulez-vous ajouter un autre main-d'œuvre ? (y/n) : ");
            String anotherLabor = scanner.nextLine().trim().toLowerCase();
            addingLabor = anotherLabor.equals("y");
        }
    }


    private Optional<Client> selectOrAddClient() {
        boolean validClient = false;
        Optional<Client> clientOptional = Optional.empty();

        while (!validClient) {
            System.out.println("1️⃣  Chercher un client existant");
            System.out.println("2️⃣  Ajouter un nouveau client");
            System.out.print("👉 Choisissez une option : ");
            int clientChoice = scanner.nextInt();
            scanner.nextLine();

            switch (clientChoice) {
                case 1:
                    clientOptional = manageClient.searchClient();
                    break;
                case 2:
                    clientOptional = manageClient.addNewClient();
                    break;
                default:
                    System.out.println("❌ Option invalide. Veuillez réessayer.");
            }

            if (clientOptional.isPresent()) {
                System.out.print("Souhaitez-vous continuer avec ce client ? (y/n) : ");
                String continueWithClient = scanner.nextLine().trim().toLowerCase();
                if (continueWithClient.equals("y")) {
                    validClient = true;
                } else {
                    System.out.println("Recommençons le processus de sélection du client.");
                    clientOptional = Optional.empty();
                }
            }
        }

        return clientOptional;
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
        if (projectOptional.isPresent()) {
            System.out.println(projectOptional.get().getId());
            Project project = projectOptional.get();
            double[] totalCost = projectService.calculateTotalCost(project, project.getProfitMargin() / 100);
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

            System.out.print("Nouvelle marge bénéficiaire (%) (laisser vide pour ne pas modifier) : ");
            String newProfitMarginInput = scanner.nextLine();
            if (!newProfitMarginInput.isEmpty()) {
                double newProfitMargin = Double.parseDouble(newProfitMarginInput);
                project.setProfitMargin(newProfitMargin);

                double[] totalCostDetails = projectService.calculateTotalCost(project, newProfitMargin / 100);
                project.setTotalCost(totalCostDetails[1]);
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