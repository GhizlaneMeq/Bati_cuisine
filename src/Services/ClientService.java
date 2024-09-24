package Services;

import Entities.Client;
import Entities.Project;
import Repositories.ClientRepository;

import java.util.List;
import java.util.Optional;

public class ClientService {
    private ClientRepository clientRepository;
    private ProjectService projectService;

    public ClientService(ClientRepository clientRepository, ProjectService projectService) {
        this.clientRepository = clientRepository;
        this.projectService = projectService;
    }

    public Client save(Client client){
        Optional<Client> existClient = clientRepository.findByName(client.getName());
        if(existClient.isPresent()){
            return null;
        }
        return clientRepository.save(client);
    }


    public Optional<Client> findById(Long id){
        return clientRepository.findById(id);
    }
    public List<Client> findAll(){
        return clientRepository.findAll();
    }
    public Client update(Client client){
        return clientRepository.update(client);
    }
    public Boolean delete(Long id) {
        return clientRepository.delete(id);
    }
    public Optional<Client> findByName(String name) {
        Optional<Client> client = clientRepository.findByName(name);

        if (client.isEmpty()) {
            return Optional.empty();
        } else {
            return client;
        }
    }
    public List<Project> findProjectsByClient(Client client) {
        return projectService.findByClient(client.getId());
    }


}
