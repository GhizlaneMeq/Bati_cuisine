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
    public List<Material> findByName(String name) {
        return materialRepository.findByName(name);
    }

    public Optional<Material> update(Material material) {

        try {
            Optional<Material> existingMaterialOptional = materialRepository.findById(material.getId());

            if (existingMaterialOptional.isPresent()) {
                Material existingMaterial = existingMaterialOptional.get();

                if (!material.equals(existingMaterial)) {
                    Material updatedMaterial = materialRepository.update(material);
                    System.out.println("Material updated successfully: " + updatedMaterial);
                    return Optional.ofNullable(updatedMaterial);
                } else {
                    System.out.println("No changes detected. Material was not updated.");
                    return Optional.of(existingMaterial);
                }
            } else {
                System.out.println("No material found with ID: " + material.getId());
                return Optional.empty();
            }
        } catch (Exception e) {
            System.out.println("An error occurred while updating the material: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }


    public boolean delete(Long id) {
        return materialRepository.delete(id);
    }

    public double[] calculateTotalCost(Project project) {
        List<Material> materials = findByProject(project);
        double totalWithoutVAT = 0;
        double totalWithVAT = 0;

        System.out.println("1. Matériaux :");

        for (Material material : materials) {
            double baseCost = material.getQuantity() * material.getUnitCost();
            double transportCost = material.getTransportCost();
            double qualityCoefficient = material.getQualityCoefficient();

            double totalCostBeforeVAT = (baseCost * qualityCoefficient) + transportCost;
            double totalCostWithVAT = totalCostBeforeVAT * (1 + material.getVatRate() / 100);

            totalWithoutVAT += totalCostBeforeVAT;
            totalWithVAT += totalCostWithVAT;

            System.out.printf("- %s : %.2f € (quantité : %.2f, coût unitaire : %.2f €/m², qualité : %.2f, transport : %.2f €)%n",
                    material.getName(), totalCostBeforeVAT, material.getQuantity(),
                    material.getUnitCost(), material.getQualityCoefficient(), transportCost);
        }

        System.out.printf("**Coût total des matériaux avant TVA : %.2f €**%n", totalWithoutVAT);
        System.out.printf("**Coût total des matériaux avec TVA (%.0f%%) : %.2f €**%n",
                materials.size() > 0 ? materials.get(0).getVatRate() : 0, totalWithVAT);

        return new double[]{totalWithoutVAT, totalWithVAT};
    }



}
