package io.nambm.buildhabit.model.user;

import io.nambm.buildhabit.entity.UserEntity;
import io.nambm.buildhabit.model.GenericModel;

public class UserModel extends GenericModel<UserEntity> {

    public static class Role {
        public static final String USER = "USER";
        public static final String ADMIN = "ADMIN";
    }

    public static final String DEFAULT_PASSWORD = "12345";

    public static final String ACC_ACTIVATED = "activated";
    public static final String ACC_PENDING = "pending";
    public static final String ACC_BLOCKED = "blocked";
    public static final String ACC_DEACTIVATED = "deactivated";

    private String username;
    private String password;
    private String name;
    private String info;
    private String email;
    private String role;
    private String accountStatus;

    public UserModel() {
    }

    public UserModel(String username, String password, String name, String info) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.info = info;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    @Override
    public String getPartitionKey() {
        return this.username;
    }

    @Override
    public String getRowKey() {
        return this.username;
    }

    public UserEntity toEntity() {
        UserEntity entity = new UserEntity();

        entity.setPartitionKey(this.username);
        entity.setRowKey(this.username);
        entity.setPassword(this.password);
        entity.setName(this.name);
        entity.setInformation(this.info);
        entity.setEmail(this.email);
        entity.setRole(this.role);
        entity.setAccountStatus(this.accountStatus);

        return entity;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", info='" + info + '\'' +
                '}';
    }
}
