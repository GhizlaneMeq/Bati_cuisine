package Services;

import Entities.Labor;
import Entities.Material;
import Entities.Project;
import Repositories.ProjectRepository;

import java.util.List;
import java.util.Optional;

public class ProjectService {
    private ProjectRepository projectRepository;
    private final MaterialService materialService;
    private final LaborService laborService;

    public ProjectService(MaterialService materialService, LaborService laborService,ProjectRepository projectRepository) {
        this.materialService = materialService;
        this.laborService = laborService;
        this.projectRepository = projectRepository;
    }

    public Optional<Project> save(Project project) {
        try {
            Project savedProject = projectRepository.save(project);
            return Optional.ofNullable(savedProject);
        } catch (Exception e) {
            System.out.println("Une erreur s'est produite lors de l'enregistrement du projet : " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Optional<Project> update(Project project) {
        try {
            Project updatedProject = projectRepository.update(project);
            return Optional.ofNullable(updatedProject);

        } catch (Exception e) {
            System.out.println("Une erreur s'est produite lors de la mise à jour du projet : " + e.getMessage());
            return Optional.empty();
        }
    }

    public boolean delete(Long id) {
        return projectRepository.delete(id);
    }
    public List<Project> findByClient(Long clientId) {
        return projectRepository.findProjectsByClient(clientId);
    }

    public double[] calculateTotalCost(Project project, double marginRate) {

        double[] materialTotals = materialService.calculateTotalCost(project);
        double totalMaterialsWithoutVAT = materialTotals[0];
        double totalMaterialsWithVAT = materialTotals[1];

        double[] laborTotals = laborService.calculateTotalCost(project);
        double totalLaborWithoutVAT = laborTotals[0];
        double totalLaborWithVAT = laborTotals[1];


        double totalCostBeforeVAT = totalMaterialsWithoutVAT + totalLaborWithoutVAT;

        double totalCostWithVAT = totalMaterialsWithVAT + totalLaborWithVAT;


        double totalMargin = totalCostWithVAT * marginRate;

        double finalTotalCost = totalCostWithVAT + totalMargin;
        System.out.println("withTva"+totalCostWithVAT);
        System.out.println("withmargin"+totalMargin);
        System.out.println("cout total"+finalTotalCost);
        return new double[]{totalCostBeforeVAT, totalCostWithVAT, totalMargin, finalTotalCost};
    }

}
