package org.shaechi.jaadas2.services;

import jadx.api.JadxDecompiler;
import org.shaechi.jaadas2.entity.ScanJob;
import org.shaechi.jaadas2.entity.SourceCode;
import org.shaechi.jaadas2.entity.SourceCodeDetails;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ScanFileDirectoryService {

    public String getContent(String method, Long scanJobId) throws IOException;
    public void apkDeCompile(String apkName, ScanJob scanJob) throws IOException;
    SourceCode getById(Long id);

}
