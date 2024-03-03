package business_logic;

import dao.ClientDAO;
import model.Client;

import java.util.List;

public class ClientService {
    private final ClientDAO clientDAO;

    public ClientService() {
        this.clientDAO = new ClientDAO();
    }

    public void createClient(Client client) {
        clientDAO.create(client);
    }

    public void updateClient(Client client) {
        clientDAO.update(client);
    }

    public void deleteClient(int id) {
        clientDAO.delete(id);
    }

    public Client findClientById(int id) {
        return clientDAO.findById(id);
    }

    public List<Client> findAllClients() {
        return clientDAO.findAll();
    }
}
