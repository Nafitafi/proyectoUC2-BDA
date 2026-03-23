/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantepersistencia;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Andrea Lara, Nahomi Figueroa, Zaira Barajas
 */
public class ManejadorConexiones {
    
    /**
     * 
     * @return 
     */
    public static EntityManager crearEntityManager(){
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("RestaurantePU");
        EntityManager entityManager = emFactory.createEntityManager();
        return entityManager;
    }
}
