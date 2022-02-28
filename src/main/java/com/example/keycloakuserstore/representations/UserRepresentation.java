package com.example.keycloakuserstore.representations;

import com.example.keycloakuserstore.dao.UserDAO;
import com.example.keycloakuserstore.models.CfmastEntity;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class UserRepresentation extends AbstractUserAdapterFederatedStorage {
    private CfmastEntity userEntity;
    private UserDAO userDAO;
    Logger logger = Logger.getLogger(this.getClass().getName());

    public UserRepresentation(KeycloakSession session,
                              RealmModel realm,
                              ComponentModel storageProviderModel,
                              CfmastEntity userEntity,
                              UserDAO userDAO) {
        super(session, realm, storageProviderModel);
        this.userEntity = userEntity;
        this.userDAO = userDAO;
    }

    @Override
    public String getUsername() {
        return userEntity.getCusToCD();
    }

    @Override
    public void setUsername(String username) {
        userEntity.setCusToCD(username);
        userEntity = userDAO.updateUser(userEntity);
    }

    @Override
    public void setEmail(String email) {
        userEntity.setEmail(email);
        userEntity = userDAO.updateUser(userEntity);
    }

    @Override
    public String getEmail() {
        return userEntity.getEmail();
    }

    @Override
    public void setSingleAttribute(String name, String value) {
        if (name.equals("phone")) {
            userEntity.setMobile(value);
        } else {
            super.setSingleAttribute(name, value);
        }
    }

    @Override
    public void removeAttribute(String name) {
        if (name.equals("phone")) {
            userEntity.setMobile(null);
        } else {
            super.removeAttribute(name);
        }
        userEntity = userDAO.updateUser(userEntity);
    }

    @Override
    public void setAttribute(String name, List<String> values) {
        if (name.equals("phone")) {
            userEntity.setMobile(values.get(0));
        } else {
            super.setAttribute(name, values);
        }
        userEntity = userDAO.updateUser(userEntity);
    }

    @Override
    public String getFirstAttribute(String name) {
        if (name.equals("phone")) {
            return userEntity.getMobile();
        } else {
            return super.getFirstAttribute(name);
        }
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        Map<String, List<String>> attrs = super.getAttributes();
        MultivaluedHashMap<String, String> all = new MultivaluedHashMap<>();
        all.putAll(attrs);
        all.add("phone", userEntity.getMobile());
        return all;
    }

    @Override
    public List<String> getAttribute(String name) {
        if (name.equals("phone")) {
            List<String> phone = new LinkedList<>();
            phone.add(userEntity.getMobile());
            return phone;
        } else {
            return super.getAttribute(name);
        }
    }

    @Override
    public String getId() {
        logger.info("getID: " + userEntity.getCusId());
        return StorageId.keycloakId(storageProviderModel, userEntity.getCusId());
    }

}
