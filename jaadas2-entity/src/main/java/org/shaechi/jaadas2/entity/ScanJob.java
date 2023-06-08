package org.shaechi.jaadas2.entity;

import lombok.Data;
import org.shaechi.jaadas2.entity.apk.ApkInfo;
import org.shaechi.jaadas2.entity.result.ScanResult;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table
public class ScanJob implements Serializable, Cloneable {
    public ScanJob() {
    }

    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime submitTime;

    private LocalDateTime scanStartTime;

    private LocalDateTime scanFinishTime;

    private Long jobProcessId;

    private int counter;

    private int total;

    private UUID uuid = UUID.randomUUID();

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<ScanResult> results = new ArrayList<>();


    private String jimplePath;

    @OneToOne(cascade = {CascadeType.ALL})
    private ApkInfo apkInfo;



    //@ManyToOne(cascade = {CascadeType.ALL})
    //private ScanProject project;
}