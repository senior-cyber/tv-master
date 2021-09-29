package com.senior.cyber.tv.dao.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(Show.class)
public abstract class Show_ {

    public static volatile SingularAttribute<Show, Long> id;
    public static volatile SingularAttribute<Show, String> name;
    public static volatile SingularAttribute<Show, Date> startAt;
    public static volatile SingularAttribute<Show, Integer> duration;
    public static volatile SingularAttribute<Show, Date> schedule;
    public static volatile SingularAttribute<Show, Channel> channel;

    public Show_() {
    }

}
