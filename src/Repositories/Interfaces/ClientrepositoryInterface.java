package Repositories.Interfaces;

import Entities.Client;

import java.util.Optional;

public interface ClientrepositoryInterface extends GenericRepositoryInterface<Client> {
    Optional<Client> findByName(String name) ;
}
