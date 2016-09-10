package com.gmail.liliyayalovchenko.dao.hibernate;

import com.gmail.liliyayalovchenko.dao.IngredientDAO;
import com.gmail.liliyayalovchenko.dao.WarehouseDAO;
import com.gmail.liliyayalovchenko.domain.Ingredient;
import com.gmail.liliyayalovchenko.domain.Warehouse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class WarehouseDAOImpl implements WarehouseDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private IngredientDAO ingredientDAO;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void addIngredient(Ingredient ingredient, int amount) {
        Warehouse warehouse = new Warehouse();
        warehouse.setIngredId(ingredient);
        warehouse.setAmount(amount);
        sessionFactory.getCurrentSession().save(warehouse);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void removeIngredient(String ingredientName) {
        Session session = sessionFactory.getCurrentSession();
        Ingredient ingredientByName = ingredientDAO.getIngredientByName(ingredientName);
        Warehouse warehouse = (Warehouse) session.
                createQuery("select w from Warehouse w where w.ingredId =:var")
                .setParameter("var", ingredientByName)
                .list()
                .get(0);
        if (warehouse != null) {
            session.delete(warehouse);
        } else {
            throw new RuntimeException("Cant find ingredient on warehouse by this ingredient name");
        }

    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public List<Warehouse> findByName(String ingredientName) {
        return sessionFactory.getCurrentSession()
                .createQuery("select w from Warehouse w where w.ingredId.name =:var")
                .setParameter("var", ingredientName)
                .list();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public List<Warehouse> getAllIngredients() {
        return sessionFactory.getCurrentSession()
                .createQuery("select w from Warehouse w")
                .list();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public List<Warehouse> getAllOrderByName() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("select w from Warehouse w order by w.ingredId.name");
        return query.list();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public List<Warehouse> getAllOrderByAmount() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("select w from Warehouse w order by w.amount desc");
        return query.list();
    }
}
