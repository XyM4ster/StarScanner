package org.shaechi.jaadas2.entity;

import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table
public class ScanProject {

    private String projectName;
    private String projectDesc;

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<ScanJob> projectJobs = new ArrayList<>();

//
//    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
//    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
//    private User user;


    public void addScanJob(ScanJob job) {
        projectJobs.add(job);
    }
}
