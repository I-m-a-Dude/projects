package dao;

import java.util.List;

/**
 * The AbstractDAO class provides generic methods for performing CRUD (Create, Read, Update, Delete) operations on a specific type of object.
 *
 * @param <T> The type of object that this DAO operates on.
 */
public abstract class AbstractDAO<T> {

    protected Class<T> clasa;

    /**
     * Constructs an AbstractDAO with the specified class type.
     *
     * @param clasaa The class type of the objects that this DAO operates on.
     */
    public AbstractDAO(Class<T> clasaa) {
        this.clasa = clasaa;
    }

    /**
     * Creates a new object in the data store.
     *
     * @param obj The object to create.
     */
    public abstract void create(T obj);

    /**
     * Updates an existing object in the data store.
     *
     * @param obj The object to update.
     */
    public abstract void update(T obj);

    /**
     * Deletes an object from the data store by its ID.
     *
     * @param id The ID of the object to delete.
     */
    public abstract void delete(int id);

    /**
     * Retrieves an object from the data store by its ID.
     *
     * @param id The ID of the object to find.
     * @return The object with the specified ID, or null if not found.
     */
    public abstract T findById(int id);

    /**
     * Retrieves all objects of the specified type from the data store.
     *
     * @return A List of all objects of the specified type.
     */
    public abstract List<T> findAll();
}
