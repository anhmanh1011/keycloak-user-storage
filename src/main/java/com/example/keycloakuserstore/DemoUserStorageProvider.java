package com.example.keycloakuserstore;

import com.example.keycloakuserstore.dao.UserDAO;
import com.example.keycloakuserstore.models.CfmastEntity;
import com.example.keycloakuserstore.models.UserloginEntity;
import com.example.keycloakuserstore.representations.UserRepresentation;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.*;
import org.keycloak.models.cache.CachedUserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;
import org.keycloak.storage.user.UserRegistrationProvider;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DemoUserStorageProvider implements UserStorageProvider,
        UserLookupProvider,
        UserRegistrationProvider,
        UserQueryProvider,
        CredentialInputUpdater,
        CredentialInputValidator {

    private final KeycloakSession session;
    private final ComponentModel model;
    private final UserDAO userDAO;
    Logger logger = Logger.getLogger(this.getClass().getName());

    public DemoUserStorageProvider(KeycloakSession session, ComponentModel model, UserDAO userDAO) {
        this.session = session;
        this.model = model;
        this.userDAO = userDAO;
    }

    @Override
    public void close() {
        userDAO.close();
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        return supportsCredentialType(credentialType);

    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput credentialInput) {
        if (!supportsCredentialType(credentialInput.getType()) || !(credentialInput instanceof UserCredentialModel)) {
            return false;
        }
        UserCredentialModel cred = (UserCredentialModel) credentialInput;
        Optional<CfmastEntity> cfmastEntity = userDAO.validateCredentials(user.getUsername(), cred.getChallengeResponse());
        if (cfmastEntity.isPresent())
            return true;
        return false;
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        logger.info("supportsCredentialType(" + credentialType + ")");
        return PasswordCredentialModel.TYPE.equals(credentialType);
    }

    @Override
    public boolean updateCredential(RealmModel realm, UserModel userModel, CredentialInput input) {
        logger.info("updateCredential(" + realm + ", " + userModel + ", " + input + ")");
        if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel)) return false;
        String id = StorageId.externalId(userModel.getId());
        UserloginEntity userloginEntity = new UserloginEntity();
        userloginEntity.setUsername(userModel.getUsername());
        userloginEntity.setLoginpwd(input.getChallengeResponse());
        userDAO.updatePassword(userloginEntity);
        return true;
    }

    @Override
    public void disableCredentialType(RealmModel realm, UserModel user, String credentialType) {
    }


    @Override
    public Set<String> getDisableableCredentialTypes(RealmModel realm, UserModel user) {
        return Collections.emptySet();
    }

    public UserRepresentation getUserRepresentation(UserModel user) {
        UserRepresentation userRepresentation = null;
        if (user instanceof CachedUserModel) {
            userRepresentation = (UserRepresentation) ((CachedUserModel) user).getDelegateForUpdate();
        } else {
            userRepresentation = (UserRepresentation) user;
        }
        return userRepresentation;
    }

    public UserRepresentation getUserRepresentation(CfmastEntity user, RealmModel realm) {
        logger.info("getUserRepresentation(user,real)");
        return new UserRepresentation(session, realm, model, user, userDAO);
    }

    @Override
    public int getUsersCount(RealmModel realm) {
        logger.info("getUsersCount(" + realm + ")");
        return userDAO.size();
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm) {
        logger.info("getUsers(" + realm + ")");
        return userDAO.findAll()
                .stream()
                .map(user -> new UserRepresentation(session, realm, model, user, userDAO))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm, int firstResult, int maxResults) {
        logger.info("getUsers(RealmModel realm, int firstResult, int maxResults)");
        return userDAO.findAll(firstResult, maxResults)
                .stream()
                .map(user -> new UserRepresentation(session, realm, model, user, userDAO))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm) {
        logger.info("searchForUser(String search, RealmModel realm)");
        return userDAO.searchForUserByUsernameOrEmail(search)
                .stream()
                .map(user -> new UserRepresentation(session, realm, model, user, userDAO))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm, int firstResult, int maxResults) {
        logger.info("searchForUser(String search, RealmModel realm, int firstResult, int maxResults)");
        return userDAO.searchForUserByUsernameOrEmail(search, firstResult, maxResults)
                .stream()
                .map(user -> new UserRepresentation(session, realm, model, user, userDAO))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm) {
        logger.info("searchForUser(params: " + params + ", realm: " + realm + ")");
        // TODO Will probably never implement; Only used by REST API
        return new ArrayList<>();
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm, int firstResult,
                                         int maxResults) {
        return userDAO.findAll(firstResult, maxResults)
                .stream()
                .map(user -> new UserRepresentation(session, realm, model, user, userDAO))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group, int firstResult, int maxResults) {
        // TODO Will probably never implement
        return new ArrayList<>();
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group) {
        // TODO Will probably never implement
        return new ArrayList<>();
    }

    @Override
    public List<UserModel> searchForUserByUserAttribute(String attrName, String attrValue, RealmModel realm) {
        // TODO Will probably never implement
        return new ArrayList<>();
    }

    @Override
    public UserModel getUserById(String keycloakId, RealmModel realm) {
        // keycloakId := keycloak internal id; needs to be mapped to external id
        logger.info("getUserById(String keycloakId, RealmModel realm) " + keycloakId);
        String id = StorageId.externalId(keycloakId);
        logger.info("getUserById id: " + id);
        CfmastEntity userById = userDAO.getUserById(id);
        logger.info("userById:  " + userById.getCusId());

        return new UserRepresentation(session, realm, model, userById, userDAO);
    }

    @Override
    public UserModel getUserByUsername(String username, RealmModel realm) {
        logger.info("getUserByUsername(String username, RealmModel realm)");
        Optional<CfmastEntity> optionalUser = userDAO.getUserByUsername(username);
        return optionalUser.map(user -> getUserRepresentation(user, realm)).orElse(null);
    }

    @Override
    public UserModel getUserByEmail(String email, RealmModel realm) {
        logger.info("getUserByEmail(String email, RealmModel realm)");
        Optional<CfmastEntity> optionalUser = userDAO.getUserByEmail(email);
        return optionalUser.map(user -> getUserRepresentation(user, realm)).orElse(null);
    }

    @Override
    public UserModel addUser(RealmModel realm, String username) {
        logger.info("addUser");
//        User user = new User();
//        user.setUsername(username);
//        user = userDAO.createUser(user);

        return null;
    }

    @Override
    public boolean removeUser(RealmModel realm, UserModel user) {
//        logger.info("removeUser(" + realm + ", " + user + ")");
//        User userEntity = userDAO.getUserById(StorageId.externalId(user.getId()));
//        if (userEntity == null) {
//            logger.info("Tried to delete invalid user with ID " + user.getId());
//            return false;
//        }
//        userDAO.deleteUser(userEntity);
        return true;
    }

}
