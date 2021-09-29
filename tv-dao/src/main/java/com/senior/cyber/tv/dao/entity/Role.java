package com.senior.cyber.tv.dao.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

@Entity
@Table(name = "tbl_role")
public class Role implements Serializable {

    public static final String NAME_ROOT = "root";
    public static final String NAME_DashboardPage = "DashboardPage";
    public static final String NAME_SchedulerPage = "SchedulerPage";
    public static final String NAME_MyProfilePage = "MyProfilePage";
    public static final String NAME_MyClientBrowsePage = "MyClientBrowsePage";
    public static final String NAME_MyClientCreatePage = "MyClientCreatePage";
    public static final String NAME_MyClientModifyPage = "MyClientModifyPage";
    public static final String NAME_RoleBrowsePage = "RoleBrowsePage";
    public static final String NAME_GroupBrowsePage = "GroupBrowsePage";
    public static final String NAME_ChannelBrowsePage = "ChannelBrowsePage";
    public static final String NAME_ShowBrowsePage = "ShowBrowsePage";
    public static final String NAME_ShowModifyPage = "ShowModifyPage";
    public static final String NAME_ShowCreatePage = "ShowCreatePage";
    public static final String NAME_SessionBrowsePage = "SessionBrowsePage";
    public static final String NAME_ClientBrowsePage = "ClientBrowsePage";
    public static final String NAME_ClientCreatePage = "ClientCreatePage";
    public static final String NAME_ClientModifyPage = "ClientModifyPage";
    public static final String NAME_GroupModifyPage = "GroupModifyPage";
    public static final String NAME_ChannelModifyPage = "ChannelModifyPage";
    public static final String NAME_UserBrowsePage = "UserBrowsePage";
    public static final String NAME_UserModifyPage = "UserModifyPage";
    public static final String NAME_UserSwitchPage = "UserSwitchPage";
    public static final String NAME_UserExitPage = "UserExitPage";

    public static final String DESCRIPTION_ROOT = "could access everything";
    public static final String DESCRIPTION_DashboardPage = "could access dashboard page";
    public static final String DESCRIPTION_SchedulerPage = "could access scheduler page";
    public static final String DESCRIPTION_MyProfilePage = "could access my profile page";
    public static final String DESCRIPTION_MyClientBrowsePage = "could access my client browse page";
    public static final String DESCRIPTION_MyClientCreatePage = "could access my client create page";
    public static final String DESCRIPTION_MyClientModifyPage = "could access my client modify page";
    public static final String DESCRIPTION_RoleBrowsePage = "could access role browse page";
    public static final String DESCRIPTION_GroupBrowsePage = "could access group browse page";
    public static final String DESCRIPTION_ChannelBrowsePage = "could access channel browse page";
    public static final String DESCRIPTION_ShowBrowsePage = "could access show browse page";
    public static final String DESCRIPTION_ShowCreatePage = "could access show create page";
    public static final String DESCRIPTION_SessionBrowsePage = "could access session browse page";
    public static final String DESCRIPTION_ClientBrowsePage = "could access client browse page";
    public static final String DESCRIPTION_ClientCreatePage = "could access client create page";
    public static final String DESCRIPTION_ClientModifyPage = "could access client modify page";
    public static final String DESCRIPTION_GroupModifyPage = "could access group update page";
    public static final String DESCRIPTION_ShowModifyPage = "could access show update page";
    public static final String DESCRIPTION_ChannelModifyPage = "could access channel update page";
    public static final String DESCRIPTION_UserBrowsePage = "could access user browse page";
    public static final String DESCRIPTION_UserModifyPage = "could access user update page";
    public static final String DESCRIPTION_UserSwitchPage = "could access user switch page";
    public static final String DESCRIPTION_UserExitPage = "could access user exit page";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "enabled")
    private boolean enabled;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tbl_group_role",
            joinColumns = @JoinColumn(name = "r_role_id", referencedColumnName = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "r_group_id", referencedColumnName = "group_id")

    )
    @MapKeyColumn(name = "group_role_id")
    private Map<String, Group> groups;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tbl_user_role",
            joinColumns = @JoinColumn(name = "r_role_id", referencedColumnName = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "r_user_id", referencedColumnName = "user_id")

    )
    @MapKeyColumn(name = "user_role_id")
    private Map<String, User> users;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tbl_deny_role",
            joinColumns = @JoinColumn(name = "r_role_id", referencedColumnName = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "r_user_id", referencedColumnName = "user_id")
    )
    @MapKeyColumn(name = "deny_role_id")
    private Map<String, User> denyUsers;

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Group> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, Group> groups) {
        this.groups = groups;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public void setUsers(Map<String, User> users) {
        this.users = users;
    }

    public Map<String, User> getDenyUsers() {
        return denyUsers;
    }

    public void setDenyUsers(Map<String, User> denyUsers) {
        this.denyUsers = denyUsers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
