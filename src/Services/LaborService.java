package Services;

import Entities.Labor;
import Repositories.LaborRepository;

import java.util.List;
import java.util.Optional;

public class LaborService {
    private final LaborRepository laborRepository;

    public LaborService(LaborRepository laborRepository) {
        this.laborRepository = laborRepository;
    }

    public Optional<Labor> save(Labor labor) {
        Labor savedLabor = laborRepository.save(labor);
        return Optional.ofNullable(savedLabor);
    }

    public Optional<Labor> findById(Long id) {
        return laborRepository.findById(id);
    }

    public List<Labor> findAll() {
        return laborRepository.findAll();
    }

    public Optional<Labor> update(Labor labor) {
        if (laborRepository.findById(labor.getId()).isPresent()) {
            return Optional.of(laborRepository.update(labor));
        }
        return Optional.empty();
    }

    public boolean delete(Long id) {
        return laborRepository.delete(id);
    }
}
