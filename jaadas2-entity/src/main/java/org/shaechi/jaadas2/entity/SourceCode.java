package org.shaechi.jaadas2.entity;

import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.shaechi.jaadas2.entity.result.ScanResult;
import org.shaechi.jaadas2.entity.sourcesink.SerializedSinkInfo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table
@Data

public class SourceCode {
    @Id
    @GeneratedValue
    private Long id;

    //存在本地的地址
    private String location;

    //保留文件原来的名字
    private String name;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private ScanJob scanJob;

    private Date createTime;


//    @Lob
//    @Column(columnDefinition="text")
//    @Basic(fetch = FetchType.LAZY)
//    private byte[] content;


}
