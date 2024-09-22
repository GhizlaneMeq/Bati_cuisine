package Services;

import Entities.Project;
import Repositories.ProjectRepository;

import java.util.List;
import java.util.Optional;

public class ProjectService {
    private ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
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
}
