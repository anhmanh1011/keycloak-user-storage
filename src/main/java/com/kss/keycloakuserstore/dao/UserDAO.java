package com.kss.keycloakuserstore.dao;


import com.kss.keycloakuserstore.entity.CfmastEntity;
import com.kss.keycloakuserstore.model.UserDto;
import com.kss.keycloakuserstore.utils.ConvertUtils;
import org.keycloak.utils.StringUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
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
        TypedQuery<CfmastEntity> query = entityManager.createNamedQuery("searchForUser", CfmastEntity.class);
        query.setParameter("search", "%");

        if (start != null) {
            query.setFirstResult(start);
        }
        if (max != null) {
            query.setMaxResults(max);
        }


        List<CfmastEntity> users = query.getResultList();
        return users.stream().map(ConvertUtils::convertCfmastToUserDto).collect(Collectors.toList());
    }

    public Optional<UserDto> getUserByUsername(String username) {
        logger.info("getUserByUsername(username: " + username + ")");

        TypedQuery<CfmastEntity> query = entityManager.createQuery("SELECT u FROM CfmastEntity  u where (lower(u.userName) = lower(:username)  or u.email = :email or u.phone = :phone) and u.status = 'A' order by u.openTime desc ", CfmastEntity.class)

                .setParameter("username", username)
                .setParameter("email", username)
                .setParameter("phone", username);
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
        TypedQuery<CfmastEntity> query = entityManager.createNamedQuery("getUserByUsernameOrEmail", CfmastEntity.class);
        query.setParameter("username", "%" + searchString + "%");
        query.setParameter("email", "%" + searchString + "%");
        query.setParameter("phone", "%" + searchString + "%");
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

    public List<UserDto> searchForUserByParam(Map<String, String> param, Integer start, Integer max) {
        logger.info("searchForUserByParam(param: " + param + ", start: " + start + ", max: " + max + ")");
        String queryDB = "SELECT c FROM CfmastEntity  c where lower(c.userName) like lower(:username) or c.email like :email or c.phone like :phone and c.status = 'A' ";
        TypedQuery<CfmastEntity> query = entityManager.createQuery(queryDB, CfmastEntity.class);
        if (StringUtil.isNotBlank(param.get("username")))
            query.setParameter("username", "%" + param.get("username") + "%");
        else
            query.setParameter("username", "%");

        if (StringUtil.isNotBlank(param.get("email")))
            query.setParameter("email", "%" + param.get("email"));
        else
            query.setParameter("email", "%");

        if (StringUtil.isNotBlank(param.get("phone")))
            query.setParameter("phone", "%" + param.get("phone") + "%");
        else
            query.setParameter("phone", "%");


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

    public UserDto updateUser(UserDto userDto) {
        CfmastEntity cfmastEntity = ConvertUtils.convertUserDtoToEntity(userDto);
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(cfmastEntity);
        transaction.commit();
        return userDto;
    }

    public boolean updatePassword(String userName, String password) throws Exception {
        logger.info("updatePassword(id: " + userName + ")");

        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            String qryString = "update UserLoginEntity s set s.password=:password where lower(s.username) =lower(:username)  ";
            Query query = entityManager.createQuery(qryString)
                    .setParameter("username", userName)
                    .setParameter("password", password);
            int i = query.executeUpdate();
            logger.info("update " + i + " record to DB , username: " + userName);
            if (i == 1) {
                transaction.commit();
                return true;
            } else {
                logger.info("update password false , record: " + i);
                transaction.rollback();
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            transaction.rollback();
            throw new Exception("update password failed");
        }
    }

    public int size() {
        return entityManager.createNamedQuery("getUserCount", Integer.class).getSingleResult();
    }

    public boolean validateCredentials(String username, String challengeResponse) {
        logger.info("validateCredentials(String username, String challengeResponse) ");
        String queryDB = "SELECT 1 from UserLoginEntity u where u.username = (SELECT c.userName FROM CfmastEntity  c where lower(c.userName) = lower(:username) or c.email = :email or c.phone = :phone and c.status = 'A' ) and u.password = :password";
        TypedQuery<Integer> query = entityManager.createQuery(queryDB, Integer.class);
        query.setParameter("username", username);
        query.setParameter("email", username);
        query.setParameter("phone", username);
        query.setParameter("password", challengeResponse);
        return query.getFirstResult() == 1;
    }
}
