package com.example.keycloakuserstore.dao;

import com.example.keycloakuserstore.models.CfmastEntity;
import com.example.keycloakuserstore.models.UserloginEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class UserDAO {

    private EntityManager entityManager;
    Logger logger = Logger.getLogger(UserDAO.class.getName());

    public UserDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<CfmastEntity> findAll() {
        return findAll(null, null);
    }

    public List<CfmastEntity> findAll(int start, int max) {
        return findAll((Integer)start, (Integer)max);
    }

    private List<CfmastEntity> findAll(Integer start, Integer max) {
        TypedQuery<CfmastEntity> query = entityManager.createQuery("SELECT U FROM CfmastEntity U  ", CfmastEntity.class);
        if(start != null) {
            query.setFirstResult(start);
        }
        if(max != null) {
            query.setMaxResults(max);
        }
//        query.setParameter("search", "%");
        List<CfmastEntity> users =  query.getResultList();
        return users;
    }

    public Optional<CfmastEntity> validateCredentials(String username, String password) {
        logger.info("validateCredentials(username: " + username + ")" + " password: " + password);
        String queryDB = "SELECT u from UserloginEntity u where u.username=(SELECT cf.cusToCD from CfmastEntity  cf WHERE cf.cusToCD = :username or cf.email = :email or cf.mobile = :mobile) and u.loginpwd = :password and u.status='A'";
        TypedQuery<CfmastEntity> query = entityManager.createQuery(queryDB, CfmastEntity.class);
        query.setParameter("username", username);
        query.setParameter("email", username);
        query.setParameter("mobile", username);
        query.setParameter("password", password);
        return query.getResultList().stream().findFirst();
    }

    public Optional<CfmastEntity> getUserByUsername(String username) {
        logger.info("getUserByUsername(username: " + username + ")");
        String queryDB = "SELECT u from CfmastEntity u where u.cusToCD = :username ";
        TypedQuery<CfmastEntity> query = entityManager.createQuery(queryDB, CfmastEntity.class);
        query.setParameter("username", username);
        return query.getResultList().stream().findFirst();
    }

    public Optional<CfmastEntity> getUserByEmail(String email) {
        logger.info("getUserByEmail(email: " + email + ")");
        String queryDB = "SELECT u from CfmastEntity u where u.email = :email ";
        TypedQuery<CfmastEntity> query = entityManager.createQuery(queryDB, CfmastEntity.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }

    public List<CfmastEntity> searchForUserByUsernameOrEmail(String searchString) {
        logger.info("searchForUserByUsernameOrEmail(searchString: " + searchString + ")");
        return searchForUserByUsernameOrEmail(searchString, null, null);
    }

    public List<CfmastEntity> searchForUserByUsernameOrEmail(String searchString, int start, int max) {
        logger.info("searchForUserByUsernameOrEmail(searchString: " + searchString + ", start: "+start+", max: "+max+")");
        return searchForUserByUsernameOrEmail(searchString, (Integer)start, (Integer)max);
    }

    private List<CfmastEntity> searchForUserByUsernameOrEmail(String searchString, Integer start, Integer max) {
        logger.info("searchForUserByUsernameOrEmail(searchString: " + searchString + ", start: "+start+", max: "+max+")");
        String queryDB = "SELECT u from CfmastEntity u where u.email like :email or u.cusToCD like :username ";
        TypedQuery<CfmastEntity> query = entityManager.createQuery(queryDB, CfmastEntity.class);
        query.setParameter("email", "%" + searchString + "%");
        query.setParameter("username", "%" + searchString + "%");
        if(start != null) {
            query.setFirstResult(start);
        }
        if(max != null) {
            query.setMaxResults(max);
        }
        return query.getResultList();
    }

    public CfmastEntity getUserById(String id) {
//        logger.info("getUserById(id: " + UUID.fromString(id) + ")");
        return entityManager.find(CfmastEntity.class, id);
    }

    public CfmastEntity createUser(CfmastEntity user) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(user);
        transaction.commit();
        return user;
    }

    public void deleteUser(CfmastEntity user) {

    }

    public void close() {
        this.entityManager.close();
    }

    public UserloginEntity updatePassword(UserloginEntity userloginEntity) {
        String queryupdate = "UPDATE UserloginEntity u SET u.loginpwd = :password where u.username = : username ";

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.createQuery(queryupdate)
                .setParameter("password",userloginEntity.getLoginpwd())
                        .setParameter("username",userloginEntity.getUsername());
        transaction.commit();
        return userloginEntity;
    }

    public CfmastEntity updateUser(CfmastEntity cfmastEntity) {
        String queryupdate = "UPDATE CfmastEntity u SET u.cusToCD = :username , u.email = :email , u.mobile = :mobile , u.fullName = :fullName  where u.cusId = :id ";

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.createQuery(queryupdate)
                .setParameter("username",cfmastEntity.getCusToCD())
                .setParameter("mobile",cfmastEntity.getMobile())
                .setParameter("fullName",cfmastEntity.getFullName())
                .setParameter("email",cfmastEntity.getEmail());
        transaction.commit();
        return cfmastEntity;
    }

    public int size() {
        return entityManager.createQuery("SELECT COUNT(U) FROM CfmastEntity U", Integer.class).getSingleResult();
    }
}
