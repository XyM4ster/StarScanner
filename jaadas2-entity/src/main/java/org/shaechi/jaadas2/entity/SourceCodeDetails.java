package org.shaechi.jaadas2.entity;

import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jadx.api.JavaClass;

@Entity
@Table
@Data

public class SourceCodeDetails {
    @Id
    @GeneratedValue
    private Long id;


    @Lob
    private String javaMethod;

    @Lob
    private String methodFuLLName;

    @Lob
    private String methodName;


    @Lob
    private String content;


    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private SourceCode sourceCode;

    private Date createTime;

}
