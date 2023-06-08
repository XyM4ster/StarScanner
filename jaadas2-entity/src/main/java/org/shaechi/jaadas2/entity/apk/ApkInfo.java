package org.shaechi.jaadas2.entity.apk;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class ApkInfo {

    @Id
    @GeneratedValue
    private Long id;

    private String pkgname;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<ApkComponent> components;

    private String md5hash;

    private String version;

    private String uid;

    private String apkPath;
}
