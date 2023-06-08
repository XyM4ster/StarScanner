package org.shaechi.jaadas2.services;

import jadx.api.JadxArgs;
import jadx.api.JadxDecompiler;
import jadx.api.JavaClass;
import jadx.api.JavaMethod;
import jadx.core.dex.nodes.MethodNode;
import org.eclipse.jdt.core.dom.*;
import org.shaechi.jaadas2.entity.ScanJob;
import org.shaechi.jaadas2.entity.SourceCode;
import org.shaechi.jaadas2.entity.SourceCodeDetails;
import org.shaechi.jaadas2.entity.result.ScanResult;
import org.shaechi.jaadas2.entity.sourcesink.SerializedPathElement;
import org.shaechi.jaadas2.repo.ScanJobRepository;
import org.shaechi.jaadas2.repo.ScanResultRepository;
import org.shaechi.jaadas2.repo.SourceCodeDetailsRepository;
import org.shaechi.jaadas2.repo.SourceCodeRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.boot.context.annotation.Configurations.getClasses;

@Service
//@Async("parallelDecompileExecutor")
public class ScanFileDirectoryServiceImpl implements ScanFileDirectoryService {
    final
    SourceCodeRepository sourceCodeRepository;

    final ScanResultRepository scanResultRepository;

    final SourceCodeDetailsRepository sourceCodeDetailsRepository;

    final ScanJobRepository scanJobRepository;

    public JadxDecompiler jadx;

    public ScanFileDirectoryServiceImpl(SourceCodeRepository sourceCodeRepository, ScanResultRepository scanResultRepository, SourceCodeDetailsRepository sourceCodeDetailsRepository, ScanJobRepository scanJobRepository) {
        this.sourceCodeRepository = sourceCodeRepository;
        this.scanResultRepository = scanResultRepository;
        this.sourceCodeDetailsRepository = sourceCodeDetailsRepository;
        this.scanJobRepository = scanJobRepository;
    }

    public SourceCode getById(Long id) {
        return sourceCodeRepository.getById(id);
    }
    //获取代码

    public void apkDeCompile(String apkName, ScanJob scanJob) throws IOException {

        JadxArgs jadxArgs = new JadxArgs();
        jadxArgs.setInputFile(new File(apkName));
        jadxArgs.setOutDir(new File("apkDecompile/" + apkName));
        JadxDecompiler jadx = new JadxDecompiler(jadxArgs);
        jadx.load();

        for (JavaClass cls : jadx.getClasses()) {
            SourceCode sourceCode = new SourceCode();
            sourceCode.setLocation(cls.getFullName());
            sourceCode.setCreateTime(new Date());
            sourceCode.setName(cls.getName() + ".java");
            sourceCode.setScanJob(scanJob);
            sourceCodeRepository.save(sourceCode);


            List<JavaMethod> mth = cls.getMethods();
            for (int i = 0; i < mth.size(); i++) {

                String code = "";
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(cls.getCode().getBytes().length);
                byteArrayOutputStream.write(cls.getCode().getBytes(), 0, cls.getCode().getBytes().length);
                String[] split = byteArrayOutputStream.toString().split("\\n");

                int startLine = mth.get(i).getDecompiledLine();
                int endLine = Integer.MAX_VALUE;
                if (i + 1 != mth.size())
                    endLine = mth.get(i + 1).getDecompiledLine();

                for (int line = startLine - 1; line < split.length && line < endLine - 1; line++) {
                    code += split[line] + "\n";
                }

                SourceCodeDetails sourceCodeDetails = new SourceCodeDetails();
                sourceCodeDetails.setSourceCode(sourceCode);
                if (mth.get(i).getReturnType().toString() != null && mth.get(i).getName() != null)
                    sourceCodeDetails.setMethodName(mth.get(i).getReturnType().toString() + " " + mth.get(i).getName());
                if (mth.get(i).getFullName() != null)
                    sourceCodeDetails.setMethodFuLLName(mth.get(i).getFullName());
                if (mth.get(i).toString() != null)
                    sourceCodeDetails.setJavaMethod(mth.get(i).toString());
                sourceCodeDetails.setContent(code);
                sourceCodeDetails.setCreateTime(new Date());
                sourceCodeDetailsRepository.save(sourceCodeDetails);

                byteArrayOutputStream.close();

            }
        }

    }

    public String getContent(String method, Long scanJobId) throws IOException {
        int colonIndex = method.lastIndexOf(":");
        String subMethod = method.substring(1, colonIndex);
        //String location = subMethod + ".java";
        // source sink 中的方法，method1Name即void unZipFile
        int startIndex = method.indexOf("(");
        int endIndex = method.indexOf(")");
        String subMethod1 = method.substring(startIndex + 1, endIndex);
        String[] split = subMethod1.split(",");
        String method1Name = method.substring(colonIndex + 1, startIndex).trim();

        String code = "";

        List<SourceCode> sourceCodeList = sourceCodeRepository.findByScanJobAndLocation(scanJobRepository.getById(scanJobId), subMethod);
        if(sourceCodeList.size() == 0)
            return null;
        List<SourceCodeDetails> sourceCodeDetailsList = sourceCodeDetailsRepository.findBySourceCodeAndMethodName(sourceCodeList.get(0), method1Name);
        if(sourceCodeDetailsList.size() == 0)
            return null;

        /**
         * 1. 这里没找到，说明没有
         * 2. 直接返回null
         * */

        for(SourceCodeDetails sourceCodeDetails : sourceCodeDetailsList){
            String javaMethod = sourceCodeDetails.getJavaMethod();
            int j = 0;
            String[] split1 = javaMethod.substring(javaMethod.indexOf("(") + 1, javaMethod.indexOf(")")).split(",");
            if (split.length == split1.length) {
                for (j = 0; j < split1.length; j++) {
                    if (!split[j].equals(split1[j].trim()))
                        break;
                }
                if (j == split.length) {
                    code = sourceCodeDetails.getContent();
                }
            }
        }
        return code;
    }

}
