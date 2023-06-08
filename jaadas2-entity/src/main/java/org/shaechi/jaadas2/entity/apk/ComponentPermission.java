package org.shaechi.jaadas2.entity.apk;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class ComponentPermission {
    String permissionName;
    PermissionType permissionType;
    @Id
    @GeneratedValue
    private Long id;
}
