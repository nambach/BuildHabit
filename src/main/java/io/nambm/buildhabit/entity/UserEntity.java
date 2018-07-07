package io.nambm.buildhabit.entity;

import com.microsoft.azure.storage.table.TableServiceEntity;
import io.nambm.buildhabit.model.user.UserModel;
import io.nambm.buildhabit.table.annotation.AzureTableName;

@AzureTableName("user")
public class UserEntity extends TableServiceEntity {
    private String name;
    private String password;
    private String fullName;
    private String information;
    private String email;
    private String role;
    private String accountStatus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
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

    public UserModel toModel() {
        UserModel model = new UserModel();

        model.setUsername(this.rowKey);
        model.setName(this.name);
        model.setPassword(this.password);
        model.setInfo(this.information);
        model.setEmail(this.email);
        model.setRole(this.role);
        model.setAccountStatus(this.accountStatus);

        return model;
    }
}
