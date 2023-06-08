package org.shaechi.jaadas2.apitool;

import lombok.Data;

@Data
public class APIScanJob {
    private String apkPath;
    private String projectName;
    private String projectDesc;
    private String md5hash; //we trust the hash if it exist
}
