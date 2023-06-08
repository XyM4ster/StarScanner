package org.shaechi.jaadas2.entity;

import lombok.Data;
import org.shaechi.jaadas2.entity.apk.ApkComponent;
import org.shaechi.jaadas2.entity.result.ScanResult;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table
public class ComponentResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private ApkComponent component;

    @OneToMany
    private List<ScanResult> compResults;
}
