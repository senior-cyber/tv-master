package com.senior.cyber.tv.dao.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Channel.class)
public abstract class Channel_ {

    public static volatile SingularAttribute<Channel, Long> id;
    public static volatile SingularAttribute<Channel, String> name;

    public Channel_() {
    }

}