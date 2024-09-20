package Repositories.Interfaces;

        import java.util.List;
        import java.util.Optional;

public interface GenericRepositoryInterface<T> {
    T save(T entity);
    Optional<T> findById(Long id);
    List<T> findAll();
    T update(T entity);
    boolean delete(Long id);
}
