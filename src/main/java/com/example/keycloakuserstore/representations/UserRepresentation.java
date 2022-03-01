package com.example.keycloakuserstore.representations;

import com.example.keycloakuserstore.dao.UserDAO;
import com.example.keycloakuserstore.model.UserDto;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserRepresentation extends AbstractUserAdapterFederatedStorage {
    private UserDto userDto;
    private UserDAO userDAO;

    public UserRepresentation(KeycloakSession session,
                              RealmModel realm,
                              ComponentModel storageProviderModel,
                              UserDto userDto,
                              UserDAO userDAO) {
        super(session, realm, storageProviderModel);
        this.userDto = userDto;
        this.userDAO = userDAO;
    }

    @Override
    public String getUsername() {
        return userDto.getUserName();
    }

    @Override
    public void setUsername(String username) {
//        userDto.setUserName(username);
//        userDto = userDAO.updateUser(userDto);
    }

    @Override
    public void setEmail(String email) {
        userDto.setEmail(email);
        userDto = userDAO.updateUser(userDto);
    }

    @Override
    public String getEmail() {
        return userDto.getEmail();
    }

    @Override
    public void setSingleAttribute(String name, String value) {
        if (name.equals("phone")) {
            userDto.setPhone(value);
        } else {
            super.setSingleAttribute(name, value);
        }
    }

    @Override
    public void removeAttribute(String name) {
        if (name.equals("phone")) {
            userDto.setPhone(null);
        } else {
            super.removeAttribute(name);
        }
        userDto = userDAO.updateUser(userDto);
    }

    @Override
    public void setAttribute(String name, List<String> values) {
        if (name.equals("phone")) {
            userDto.setPhone(values.get(0));
        } else {
            super.setAttribute(name, values);
        }
        userDto = userDAO.updateUser(userDto);
    }

    @Override
    public String getFirstAttribute(String name) {
        if (name.equals("phone")) {
            return userDto.getPhone();
        } else {
            return super.getFirstAttribute(name);
        }
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        Map<String, List<String>> attrs = super.getAttributes();
        MultivaluedHashMap<String, String> all = new MultivaluedHashMap<>();
        all.putAll(attrs);
        all.add("phone", userDto.getPhone());
        return all;
    }

    @Override
    public List<String> getAttribute(String name) {
        if (name.equals("phone")) {
            List<String> phone = new LinkedList<>();
            phone.add(userDto.getPhone());
            return phone;
        } else {
            return super.getAttribute(name);
        }
    }

    @Override
    public String getId() {
        return StorageId.keycloakId(storageProviderModel, userDto.getId());
    }

    public String getPassword() {
        return userDto.getPassword();
    }

}
