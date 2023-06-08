package org.shaechi.jaadas2.entity.result;

import lombok.Data;
import org.shaechi.jaadas2.entity.SourceCode;
import org.shaechi.jaadas2.entity.apk.ApkComponent;
import org.shaechi.jaadas2.entity.sourcesink.SerializedSinkInfo;
import org.shaechi.jaadas2.entity.sourcesink.SerializedSourceInfo;
import org.shaechi.jaadas2.entity.vuln.VulnLevel;

import javax.persistence.*;

@Entity
@Data
@Table
public class ScanResult {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private VulnLevel vulnLevel;

    @OneToOne(cascade = {CascadeType.ALL})
    private SerializedSinkInfo sinkInfo;

    @ManyToOne(cascade = {CascadeType.ALL})
    private SerializedSourceInfo sourceInfo;

    @ManyToOne(cascade = {CascadeType.ALL})
    private ApkComponent component;


    @Column(length = 100000)
    private String comment = "";

    private boolean ignored = false;
}
