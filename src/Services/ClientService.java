package Services;

import Entities.Client;
import Repositories.ClientRepository;

import java.util.List;
import java.util.Optional;

public class ClientService {
    private ClientRepository clientRepository;
    public ClientService(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
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
    public Optional<Client> findByName(String name){
        return clientRepository.findByName(name);
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
}
