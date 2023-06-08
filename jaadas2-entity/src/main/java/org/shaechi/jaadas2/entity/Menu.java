package org.shaechi.jaadas2.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Data
public class Menu {


    @Id
    @GeneratedValue
    private Long id;

    private String name;
    //菜单URL
    private String url;
    //上级菜单id
    private int parentId;

}
