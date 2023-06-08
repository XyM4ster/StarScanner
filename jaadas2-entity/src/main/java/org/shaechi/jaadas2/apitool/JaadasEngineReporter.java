package org.shaechi.jaadas2.apitool;

import com.google.gson.Gson;
import org.shaechi.jaadas2.entity.apk.ApkInfo;
import org.shaechi.jaadas2.entity.engine.EngineComponentReport;
import org.shaechi.jaadas2.entity.engine.EngineReport;
import org.shaechi.jaadas2.entity.engine.EngineReportType;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/*
The API is
POST /api/enginereport/{uuid}
post content is json
 */
public class JaadasEngineReporter {
    private static String URL;

    public static void setURL(String url) {
        URL = url;
    }
    private static Gson gson = new Gson();

    public static String postApkMeta(ApkInfo info) throws Exception {
        EngineReport report = new EngineReport();
        report.setType(EngineReportType.ENGINE_APK_META_REPORT);
        report.setContent(gson.toJson(info));
        return post(URL, gson.toJson(report));
    }

    public static String postCompReportPath(EngineComponentReport ecr) throws Exception {
        EngineReport report = new EngineReport();
        report.setType(EngineReportType.ENGINE_APK_COMP_REPORT);
        report.setContent(gson.toJson(ecr));
        return post(URL, gson.toJson(report));
    }

    public static String postScanFinished() throws Exception {
        EngineReport report = new EngineReport();
        report.setType(EngineReportType.ENGINE_SCAN_FINISH);
        return post(URL, gson.toJson(report));
    }

    public static String post(String url, String json) throws Exception{
        String charset = "UTF-8";
        URLConnection connection = new URL(url).openConnection();
        connection.setDoOutput(true); // Triggers POST.
        connection.setRequestProperty("Accept-Charset", charset);
        connection.setRequestProperty("Content-Type", "application/json;charset=" + charset);

        try (OutputStream output = connection.getOutputStream()) {
            output.write(json.getBytes(charset));
        }

        InputStream response = connection.getInputStream();
        byte[] buf = new byte[4096];
        StringBuilder sb = new StringBuilder();
        int size = response.read(buf);
        while (size != -1) {
            sb.append(new String(buf, size));
            size = response.read(buf);
        }
        return sb.toString();
    }
}
