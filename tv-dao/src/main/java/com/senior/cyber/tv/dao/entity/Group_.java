package com.senior.cyber.tv.dao.entity;

import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Group.class)
public abstract class Group_ {

    public static volatile SingularAttribute<Group, Long> id;
    public static volatile SingularAttribute<Group, String> name;
    public static volatile SingularAttribute<Group, Boolean> enabled;
    public static volatile MapAttribute<Group, String, Role> roles;
    public static volatile MapAttribute<Group, String, User> users;

    public Group_() {
    }

}