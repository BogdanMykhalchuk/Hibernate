package com.chapter12;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@FilterDefs({
        @FilterDef(name = "byStatus", parameters = @ParamDef(name = "status", type =
                "boolean")),
        @FilterDef(name = "byGroup", parameters = @ParamDef(name = "group", type =
                "string")),
        @FilterDef(name = "userEndsWith1")
})
@Filters({
        @Filter(name = "byStatus", condition = "active = :status"),
        @Filter(name = "byGroup", condition = ":group in (select ug.groups from User_groups ug where ug.user_id = id)"),
        @Filter(name = "userEndsWith1", condition = "name like '%1'")
})

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    @Audited
    @Column(unique = true)
    String name;
    @Audited
    boolean active;
    @Audited
    @ElementCollection
    Set<String> groups;
    @Audited
    String description;
    public User(String name, boolean active) {
        this.name = name;
        this.active = active;
    }
    public void addGroups(String... groupSet) {
        if (getGroups() == null) {
            setGroups(new HashSet<>());
        }
        getGroups().addAll(Arrays.asList(groupSet));
    }
}
