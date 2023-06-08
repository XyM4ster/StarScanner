package org.shaechi.jaadas2.entity.apk;

import lombok.Data;
import lombok.ToString;
import org.shaechi.jaadas2.entity.result.ScanResult;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "apkcomponents")
@Data
public class ApkComponent {

    @Id
    @GeneratedValue
    private Long id;
    private String componentName;

    private boolean exported;

    @ElementCollection
    private List<String> permissions;

    //@ManyToOne(cascade = {CascadeType.ALL})
    //private ApkInfo info;

    //private List<String> permissions;
    //apkcomponent should have a isReachable method;

    //@OneToMany(cascade = {CascadeType.ALL})
    //@ToString.Exclude
    //private List<ScanResult> results = new ArrayList<>();
}
