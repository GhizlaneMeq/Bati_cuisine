package Services;

import Entities.Material;
import Entities.Project;
import Repositories.MaterialRepository;

import java.util.List;
import java.util.Optional;

public class MaterialService {
    private MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public Optional<Material> save(Material material) {
        try {
            Material savedMaterial = materialRepository.save(material);
            return Optional.ofNullable(savedMaterial);
        } catch (Exception e) {
            System.out.println("Une erreur s'est produite lors de l'enregistrement du matériel : " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Material> findByProject(Project project) {
        return materialRepository.findByProjectId(project.getId());
    }
}
