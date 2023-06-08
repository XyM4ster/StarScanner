package org.shaechi.jaadas2.entity.vuln;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "vulninfos")
@Data
public class VulnInfo {
    private VulnLevel vulnLevel;
    private String desc;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
