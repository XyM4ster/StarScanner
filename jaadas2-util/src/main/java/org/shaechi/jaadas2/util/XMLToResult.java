package org.shaechi.jaadas2.util;

import com.google.gson.Gson;
import org.shaechi.jaadas2.apitool.JaadasEngineReporter;
import org.shaechi.jaadas2.entity.JobStatus;
import org.shaechi.jaadas2.entity.ScanJob;
import org.shaechi.jaadas2.entity.ScanProject;
import org.shaechi.jaadas2.entity.apk.ApkComponent;
import org.shaechi.jaadas2.entity.apk.ApkInfo;
import org.shaechi.jaadas2.entity.engine.EngineComponentReport;
import org.shaechi.jaadas2.entity.result.ScanResult;
import org.shaechi.jaadas2.entity.sourcesink.SerializedAccessPath;
import org.shaechi.jaadas2.entity.sourcesink.SerializedPathElement;
import org.shaechi.jaadas2.entity.vuln.VulnLevel;
import org.springframework.data.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import soot.jimple.infoflow.results.xml.InfoflowResultsReader;
import soot.jimple.infoflow.results.xml.SerializedInfoflowResults;
import org.shaechi.jaadas2.entity.sourcesink.SerializedSinkInfo;
import org.shaechi.jaadas2.entity.sourcesink.SerializedSourceInfo;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class XMLToResult {

    public static void main(String[] args)
    {
        try {
            //Process p = Runtime.getRuntime().exec("java -Xmx104g -jar /home/test/soot-infoflow-cmd-jar-with-dependencies.jar -ot -dt 1200 -a /tmp//471BAA41FD08CD623C4FCEAEFCCDA7E6-app-debug.apk  -p /usr/lib/android-sdk/platforms -s /tmp/SourceSinkFull.xml -o /tmp/jaadas2-output/2fc7e253-33bd-40bb-8f15-01b6bc232700");
           //p.getOutputStream().close();
           //p.getInputStream().close();
           //p.getErrorStream().close();
            //scanDirAndReportAPI("/tmp/jaadas2-output/24dea377-d155-4ac9-85ed-7dac3865906f", "9316454BC0312CF4987388BFDC43128C-FactoryAirCommandManager.apk", "24dea377-d155-4ac9-85ed-7dac3865906f");
            // parseXMLWithReaderInDir("/tmp/vivoout");
            System.out.println(parseXMLwithReader("/mnt/data2/jaadas2-output/27ee5c9c-cf14-4790-9209-b5440993bdf0/com.samsung.android.scloud.oem.lib.bnr.BNRClientProivder_TouchWizHome_2017.xml", 1));
        }

        catch (XMLStreamException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void parseXML(String filepath, int retrycount)
    {
        //创建一个DocumentBuilderFactory的对象
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        //创建一个DocumentBuilder的对象
        while (retrycount > 0) {
            try {
                //创建DocumentBuilder对象
                DocumentBuilder db = dbf.newDocumentBuilder();
                //通过DocumentBuilder对象的parser方法加载books.xml文件到当前项目下
                Document document = db.parse(filepath);
                //获取所有book节点的集合
                NodeList resultList = document.getElementsByTagName("Result");
                //通过nodelist的getLength()方法可以获取bookList的长度
                System.out.println("一共有" + resultList.getLength() + "result");
                for (int i = 0; i < resultList.getLength(); i++) {
                    System.out.println("=================下面开始遍历第" + (i + 1) + "result的内容=================");
                    //通过 item(i)方法 获取一个book节点，nodelist的索引值从0开始
                    Node result = resultList.item(i);
                    //获取book节点的所有属性集合
                    NamedNodeMap attrs = result.getAttributes();
                    System.out.println("第 " + (i + 1) + "本书共有" + attrs.getLength() + "个属性");
                    //遍历book的属性
                    for (int j = 0; j < attrs.getLength(); j++) {
                        //通过item(index)方法获取book节点的某一个属性
                        Node attr = attrs.item(j);
                        //获取属性名
                        System.out.print("属性名：" + attr.getNodeName());
                        //获取属性值
                        System.out.println("--属性值" + attr.getNodeValue());
                    }
                    //解析book节点的子节点
                    NodeList childNodes = result.getChildNodes();

                    Node sinkNode = childNodes.item(0);
                    Node sourcesNode = childNodes.item(1);

                    // parse sinkNode
                    String sinkStmt = sinkNode.getAttributes().getNamedItem("Statement").getNodeValue();
                    String sinkCategory = sinkNode.getAttributes().getNamedItem("SystemCategory").getNodeValue();
                    String sinkMethod = sinkNode.getAttributes().getNamedItem("Method").getNodeValue();

                    // sinkNode has one AccessPath subNode
                    System.out.println(sinkNode.getChildNodes().getLength());

                    System.out.println("sink stmt: " + sinkStmt + ", category " + sinkCategory + ", sinkMethod " + sinkMethod);

                    for (int k = 0; k < sourcesNode.getChildNodes().getLength(); k++) {
                        Node sourceNode = sourcesNode.getChildNodes().item(k);
                        String sourceStmt = sinkNode.getAttributes().getNamedItem("Statement").getNodeValue();
                        String sourceCategory = sinkNode.getAttributes().getNamedItem("SystemCategory").getNodeValue();
                        String sourceMethod = sinkNode.getAttributes().getNamedItem("Method").getNodeValue();

                        // sourceNode has an AccessPath and
                    }
                    System.out.println("======================结束遍历第" + (i + 1) + "本result的内容=================");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            --retrycount;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static List<ScanResult> parseXMLwithReader(String xmlPath, int retrycount) throws XMLStreamException, IOException {

        InfoflowResultsReader reader = new InfoflowResultsReader();
        SerializedInfoflowResults results = reader.readResults(xmlPath);
        List<ScanResult> scanResults = new ArrayList<>();

        for (SerializedSinkInfo sinkInfo: results.getResults().keySet()) {
            Set<SerializedSourceInfo> sourceInfos = results.getResults().get(sinkInfo);
            for(SerializedSourceInfo sourceInfo: sourceInfos)
            {
                ScanResult scanResult = new ScanResult();
                scanResult.setSinkInfo(sinkInfo);
                scanResult.setSourceInfo(sourceInfo);
                fixResultLevel(sourceInfo, sinkInfo, scanResult);
                scanResults.add(scanResult);
            }
        }
        return scanResults;
    }

    private static boolean fixResultLevel(SerializedSourceInfo sourceInfo, SerializedSinkInfo sinkInfo, ScanResult result) {
        if(sinkInfo.getCategory().contains("FILE_IO")) {
            //add filter for external-storage -> file,  see com.samsung.android.pushservice cn.jiguang.g.a getExternalStorage -> FileOutputStream.write
            if(sinkInfo.getStatement().contains("File") || propagationPathContains(sourceInfo, Arrays.asList("file"))) {
                if(sourceInfo.getStatement().contains("External")) {
                    sinkInfo.setCategory("FILE_EXTERNAL_WRITE");
                    result.setVulnLevel(VulnLevel.LOW);
                } else {
                    sinkInfo.setCategory("FILE_IO");
                    result.setVulnLevel(VulnLevel.HIGH);
                }

            } else if(propagationPathContains(sourceInfo, Arrays.asList("http", "volley", "url"))) {
                sinkInfo.setCategory("HTTP_IO");
                result.setVulnLevel(VulnLevel.HIGH);
            } else if(sourceInfo.getMethod().contains("Zip")) {
                sinkInfo.setCategory("ZIP_IO");
                result.setVulnLevel(VulnLevel.HIGH);
            } else {
                sinkInfo.setCategory("IO");
                result.setVulnLevel(VulnLevel.MEDIUM);
            }
        } else if(sinkInfo.getCategory().contains("INTER_APP_COMM")) {
            if (sinkInfo.getCategory().contains("TARGET_NOT_CONTROLLABLE")) {
                result.setVulnLevel(VulnLevel.LOW);
                sinkInfo.setCategory("INTER_APP_COMM_TARGET_NOT_CONTROLLABLE");
            }
            else if (sinkInfo.getAccessPath().getFields() == null || sinkInfo.getAccessPath().getFields().size() == 0)
            {
                //this a fully tainted intent without fields
                result.setVulnLevel(VulnLevel.HIGH);
                sinkInfo.setCategory("INTER_APP_COMM_FULL_INTENT_CONTROL");
            }
            else if(sinkInfo.getAccessPath().getFields() != null) {
                SerializedAccessPath ap = sinkInfo.getAccessPath();
                String content = ap.toString().toLowerCase();
                //tainted intent with importaint fields
                if(content.contains("comp") || content.contains("action") || content.contains("uri") || content.contains("flags")) {
                    sinkInfo.setCategory("INTER_APP_COMM_FULL_INTENT_CONTROL");
                    result.setVulnLevel(VulnLevel.MEDIUM);
                } else
                    result.setVulnLevel(VulnLevel.LOW);
            }
        } else if(sinkInfo.getCategory().contains("CODE_EXEC")) {
            result.setVulnLevel(VulnLevel.HIGH);
        } else if (sinkInfo.getCategory().contains("PENDING_INTENT")) {
            result.setVulnLevel(VulnLevel.HIGH);
        }
        else {
            result.setVulnLevel(VulnLevel.LOW);
        }
        return true;
    }

    private static boolean propagationPathContains(SerializedSourceInfo sourceInfo, List<String> keyword) {
        for(SerializedPathElement pathElement: sourceInfo.getPropagationPath()) {
            String content = pathElement.toString();
            for(String key: keyword)
                if(content.toLowerCase().contains(key.toLowerCase()))
                    return true;
        }
        return false;
    }
    public static ScanProject parseXMLWithReaderInDir(String dirpath) throws XMLStreamException, IOException {
        File file = new File(dirpath);
        Set<Pair<SerializedSourceInfo, SerializedSinkInfo>> fullresults = new HashSet<>();
        int totcnt = 0;
        Set<String> names = new HashSet<>();
        for (String path: file.list()) {
            if(path.endsWith(".json"))
                names.add(path.substring(0, path.length() - 5));
            else
                names.add(path.substring(0, path.length() - 4));
        }
        Gson gson = new Gson();

        ApkInfo info = new ApkInfo();
        info.setApkPath("/home/test/vivoassistent.apk");
        info.setPkgname("com.vivo.assistant");
        info.setMd5hash("xxx");
        info.setVersion("1.0.0");


        ScanProject project = new ScanProject();
        project.setProjectName("default");
        project.setProjectDesc("This is a default project");

        ScanJob job = new ScanJob();
        job.setApkInfo(info);
        project.addScanJob(job);
        job.setStatus(JobStatus.SuccessFinish);

        List<ScanResult> results = new ArrayList<>();

        List<ApkComponent> components = new ArrayList<>();
        for(String name: names){
            System.out.println("processing " + name);
            String prefix = dirpath + File.separator + name;
            FileReader reader = new FileReader(prefix + ".json");
            Map<String, String> meta = gson.fromJson(reader, Map.class);
            System.out.println(meta);

            ApkComponent component = new ApkComponent();
            component.setComponentName(meta.get("ComponentName"));
            List<ScanResult> pairs = parseXMLwithReader(prefix + ".xml", 1);
            totcnt += pairs.size();
            System.out.println("Totcnt: " + totcnt);
            for(ScanResult result: pairs)
                result.setComponent(component);

            results.addAll(pairs);
            components.add(component);
        }
        //report.setJob(job);
        job.setSubmitTime(LocalDateTime.now());
        job.setScanStartTime(LocalDateTime.now());
        job.setScanFinishTime(LocalDateTime.now());
        job.setResults(results);
        return project;
    }

    public static void scanDirAndReportAPI(String dirpath, String apkname, String uuid) throws Exception {
        // report APK meta
        File file = new File(dirpath, apkname.replace(".apk", ".json"));
        Gson gson = new Gson();
        ApkInfo info = gson.fromJson(new FileReader(file), ApkInfo.class);
        JaadasEngineReporter.setURL("http://127.0.0.1:8080/api/enginereport/" + uuid);

        JaadasEngineReporter.postApkMeta(info);
        int counter = 1;
        int total = info.getComponents().size();
        for(String name: new File(dirpath).list()){
            if (name.endsWith("xml")) {
                EngineComponentReport ecr = new EngineComponentReport();
                ecr.setReportPath(new File(dirpath, name).getAbsolutePath());
                ecr.setReportStatus("Success");
                ecr.setCounter(counter++);
                ecr.setTotal(total);
                ecr.setComponentName(name.split("_")[0]);
                JaadasEngineReporter.postCompReportPath(ecr);
            }
        }
    }
}
