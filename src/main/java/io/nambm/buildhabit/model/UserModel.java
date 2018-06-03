package io.nambm.buildhabit.model;

import io.nambm.buildhabit.entity.UserEntity;

public class UserModel {
    private String username;
    private String password;
    private String name;
    private String info;

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

    public UserEntity toEntity() {
        UserEntity entity = new UserEntity();

        entity.setPartitionKey(this.username);
        entity.setRowKey(this.username);
        entity.setPassword(this.password);
        entity.setName(this.name);
        entity.setInformation(this.info);

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
