package Services;

import Entities.Material;
import Entities.Project;
import Repositories.MaterialRepository;

import java.util.List;
import java.util.Optional;

public class MaterialService {
    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public Optional<Material> save(Material material) {
        try {
            Material savedMaterial = materialRepository.save(material);
            return Optional.ofNullable(savedMaterial);
        } catch (Exception e) {
            System.out.println("Une erreur s'est produite lors de l'enregistrement du matériau : " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Material> findById(Long id) {
        return materialRepository.findById(id);
    }

    public List<Material> findByProject(Project project) {
        return materialRepository.findByProjectId(project.getId());
    }

    public List<Material> findAll() {
        return materialRepository.findAll();
    }

    public Optional<Material> update(Material material) {
        try {
            Optional<Material> existingMaterialOptional = materialRepository.findById(material.getId());
            if (existingMaterialOptional.isPresent()) {
                Material updatedMaterial = materialRepository.update(material);
                return Optional.ofNullable(updatedMaterial);
            }
        } catch (Exception e) {
            System.out.println("Une erreur s'est produite lors de la mise à jour du matériau : " + e.getMessage());
            return Optional.empty();
        }
        return Optional.empty();
    }

    public boolean delete(Long id) {
        return materialRepository.delete(id);
    }

    public double[] calculateTotalCost(Project project) {
        List<Material> materials = findByProject(project);
        double totalWithoutVAT = 0;
        double totalWithVAT = 0;

        for (Material material : materials) {
            double baseCost = material.getQuantity() * material.getUnitCost();
            double transportCost = material.getTransportCost();
            double qualityCoefficient = material.getQualityCoefficient();

            double totalCostBeforeVAT = (baseCost * qualityCoefficient) + transportCost;
            double totalCostWithVAT = totalCostBeforeVAT * (1 + material.getVatRate()/100);

            totalWithoutVAT += totalCostBeforeVAT;
            totalWithVAT += totalCostWithVAT;
        }

        return new double[]{totalWithoutVAT, totalWithVAT};
    }
}
