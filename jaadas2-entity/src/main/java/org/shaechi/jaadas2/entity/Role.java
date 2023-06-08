package org.shaechi.jaadas2.entity;

import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;


@Data
@Entity
@Table
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue
    private int id;

    private String name;
    //权限编码
    private String code;



    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<Menu> menu;

    @Override
    public String getAuthority() {
        return "ROLE_"+ code;
    }

}
