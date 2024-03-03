package presentation.client;

import dao.ClientDAO;
import model.Client;

import java.util.List;

/**
 * The ClientController class handles client-related operations and interacts with the ClientGUI and ClientDAO.
 */
public class ClientController {
    private final ClientGUI view;
    private final ClientDAO dao;

    /**
     * Constructs a ClientController object.
     *
     * @param view The client view.
     * @param dao  The client data access object.
     */
    public ClientController(ClientGUI view, ClientDAO dao) {
        this.view = view;
        this.dao = dao;
    }

    /**
     * Creates a new client with the specified name and address.
     *
     * @param name    The name of the client.
     * @param address The address of the client.
     */
    public void createClient(String name, String address) {
        Client client = new Client(null, name, address);
        dao.create(client);
        view.updateClients(dao.findAll()); // update view with new client list
    }

    /**
     * Updates the client with the specified ID with the new name and address.
     *
     * @param id      The ID of the client to update.
     * @param name    The new name of the client.
     * @param address The new address of the client.
     */
    public void updateClient(Integer id, String name, String address) {
        Client client = new Client(id, name, address);
        dao.update(client);
        view.updateClients(dao.findAll()); // update view with updated client list
    }

    /**
     * Deletes the client with the specified ID.
     *
     * @param id The ID of the client to delete.
     */
    public void deleteClient(Integer id) {
        dao.delete(id);
        view.updateClients(dao.findAll()); // update view with updated client list
    }

    /**
     * Finds a client by its ID.
     *
     * @param id The ID of the client to find.
     * @return The client object if found, null otherwise.
     */
    public Client findClientById(int id) {
        return dao.findClientById(id);
    }

    /**
     * Retrieves the list of all clients.
     *
     * @return The list of all clients.
     */
    public List<Client> readClients() {
        return dao.findAll(); // return current client list
    }
}
