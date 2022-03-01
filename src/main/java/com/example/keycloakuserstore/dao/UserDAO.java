package com.example.keycloakuserstore.dao;


import com.example.keycloakuserstore.entity.CfmastEntity;
import com.example.keycloakuserstore.entity.UserLoginEntity;
import com.example.keycloakuserstore.model.UserDto;
import com.example.keycloakuserstore.utils.ConvertUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class UserDAO {

//    public static final int MAX_RESULT = 50;

    private EntityManager entityManager;
    Logger logger = Logger.getLogger(UserDAO.class.getName());

    public UserDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<UserDto> findAll() {
        return findAll(null, null);
    }

    public List<UserDto> findAll(int start, int max) {
        return findAll((Integer) start, (Integer) max);
    }

    private List<UserDto> findAll(Integer start, Integer max) {
        TypedQuery<CfmastEntity> query = entityManager.createQuery("SELECT t from CfmastEntity t where t.status = 'A'", CfmastEntity.class);
//        query.setParameter("search", "%" );

        if (start != null) {
            query.setFirstResult(start);
        }
        if (max != null) {
            query.setMaxResults(max);
        }
        else
            query.setMaxResults(50);

        List<CfmastEntity> users = query.getResultList();
        return users.stream().map(ConvertUtils::convertCfmastToUserDto).collect(Collectors.toList());
    }

    public Optional<UserDto> getUserByUsername(String username) {
        logger.info("getUserByUsername(username: " + username + ")");
        TypedQuery<CfmastEntity> query = entityManager.createNamedQuery("getUserByUsername", CfmastEntity.class);
        query.setParameter("username", username);

        return query.getResultList().stream().map(ConvertUtils::convertCfmastToUserDto).findFirst();
    }

    public Optional<UserDto> getUserByEmail(String email) {
        logger.info("getUserByEmail(email: " + email + ")");
        TypedQuery<CfmastEntity> query = entityManager.createNamedQuery("getUserByEmail", CfmastEntity.class);
        query.setParameter("email", email);
        return query.getResultList().stream().map(ConvertUtils::convertCfmastToUserDto).findFirst();
    }

    public List<UserDto> searchForUserByUsernameOrEmail(String searchString) {
        logger.info("searchForUserByUsernameOrEmail(searchString: " + searchString + ")");
        return searchForUserByUsernameOrEmail(searchString, null, null);
    }

    public List<UserDto> searchForUserByUsernameOrEmail(String searchString, int start, int max) {
        logger.info("searchForUserByUsernameOrEmail(searchString: " + searchString + ", start: " + start + ", max: " + max + ")");
        return searchForUserByUsernameOrEmail(searchString, (Integer) start, (Integer) max);
    }

    private List<UserDto> searchForUserByUsernameOrEmail(String searchString, Integer start, Integer max) {
        logger.info("searchForUserByUsernameOrEmail(searchString: " + searchString + ", start: " + start + ", max: " + max + ")");
        TypedQuery<CfmastEntity> query = entityManager.createNamedQuery("searchForUser", CfmastEntity.class);
        query.setParameter("search", "%" + searchString + "%");
        if (start != null) {
            query.setFirstResult(start);
        }
        if (max != null) {
            query.setMaxResults(max);

        }
//        else
//            query.setMaxResults(MAX_RESULT);
        return query.getResultList().stream().map(ConvertUtils::convertCfmastToUserDto).collect(Collectors.toList());
    }

    public UserDto getUserById(String id) {
        logger.info("getUserById(id: " + id + ")");
        CfmastEntity cfmastEntity = entityManager.find(CfmastEntity.class, id);
        return ConvertUtils.convertCfmastToUserDto(cfmastEntity);
    }


    public void close() {
        this.entityManager.close();
    }

    public UserDto updateUser(UserDto  userDto) {
        CfmastEntity cfmastEntity = ConvertUtils.convertUserDtoToEntity(userDto);
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(cfmastEntity);
        transaction.commit();
        return userDto;
    }

    public boolean updatePassword(String userName, String password) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        String qryString = "update UserLoginEntity s set s.username=:username where s.password=:password";
        Query query = entityManager.createQuery(qryString)
                .setParameter("username", userName)
                .setParameter("password", password);
        int i = query.executeUpdate();
        transaction.commit();
        logger.info("update " + i + " record to DB , username: " + userName);
        return i > 0;
    }

    public int size() {
        return entityManager.createNamedQuery("getUserCount", Integer.class).getSingleResult();
    }

    public boolean validateCredentials(String username, String challengeResponse) {
        logger.info("validateCredentials(String username, String challengeResponse) ");
        String queryDB = "SELECT 1 from UserLoginEntity u where u.username = (SELECT c.userName FROM CfmastEntity  c where c.userName = :username or c.email = :email or c.phone = :phone and c.status = 'A' ) and u.password = :password";
        TypedQuery<CfmastEntity> query = entityManager.createQuery(queryDB, CfmastEntity.class);
        return query.getFirstResult() == 1;
    }
}
