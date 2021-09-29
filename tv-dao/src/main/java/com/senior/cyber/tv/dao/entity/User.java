package com.senior.cyber.tv.dao.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Entity
@Table(name = "tbl_user")
public class User implements Serializable {

    public static final String PASSWORD_STATUS_VALID = "Valid";
    public static final String PASSWORD_STATUS_EXPIRED = "Expired";
    public static final String STATUS_ENABLED = "Enabled";
    public static final String STATUS_DISABLED = "Disabled";
    public static final String STATUS_LOCKED = "Locked";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "login")
    private String login;

    @Column(name = "pwd")
    private String password;

    @Column(name = "pwd_status")
    private String passwordStatus;

    @Column(name = "status")
    private String status;

    @Column(name = "mobile_phone_number")
    private String mobilePhoneNumber;

    @Column(name = "retry_count")
    private Long retryCount;

    @Column(name = "email_address")
    private String emailAddress;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_seen")
    private Date lastSeen;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tbl_user_group",
            joinColumns = @JoinColumn(name = "r_user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "r_group_id", referencedColumnName = "group_id")

    )
    @MapKeyColumn(name = "user_group_id")
    private Map<String, Group> groups;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tbl_user_role",
            joinColumns = @JoinColumn(name = "r_user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "r_role_id", referencedColumnName = "role_id")

    )
    @MapKeyColumn(name = "user_role_id")
    private Map<String, Role> roles;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tbl_deny_role",
            joinColumns = @JoinColumn(name = "r_user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "r_role_id", referencedColumnName = "role_id")
    )
    @MapKeyColumn(name = "deny_role_id")
    private Map<String, Role> denyRoles;

    public Long getId() {
        return id;
    }

    public Map<String, Group> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, Group> groups) {
        this.groups = groups;
    }

    public Map<String, Role> getRoles() {
        return roles;
    }

    public void setRoles(Map<String, Role> roles) {
        this.roles = roles;
    }

    public Map<String, Role> getDenyRoles() {
        return denyRoles;
    }

    public void setDenyRoles(Map<String, Role> denyRoles) {
        this.denyRoles = denyRoles;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordStatus() {
        return passwordStatus;
    }

    public void setPasswordStatus(String passwordStatus) {
        this.passwordStatus = passwordStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Long retryCount) {
        this.retryCount = retryCount;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }
}
