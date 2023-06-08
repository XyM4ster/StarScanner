package org.shaechi.jaadas2.client;

import com.google.gson.Gson;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import net.dongliu.apk.parser.bean.UseFeature;
import org.shaechi.jaadas2.apitool.APIScanJob;
import org.shaechi.jaadas2.apitool.JaadasEngineReporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class APIClient {
    public static void main(String args[]) {
        if (args.length <= 1) {
            System.out.println("usage: java -jar client.jar endpoint projectname projectdesc <apkpath/apkdir> uid");
            return;
        }
        String endpoint = args[0];
        String projectName = args[1];
        String projectDesc = args[2];
        String filePath = args[3];
        String uid = null;
        if (args.length >= 5)
            uid = args[4];
        List<String> targets = new ArrayList<>();
        getFileNames(targets, Paths.get(filePath), uid);
        targets.forEach(System.out::println);
        Gson gson = new Gson();

        for(String target: targets) {

            APIScanJob job = new APIScanJob();
            job.setProjectDesc(projectDesc);
            job.setProjectName(projectName);
            try {
                job.setMd5hash(calcFileHash(target));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            job.setApkPath(target);
            System.out.println("posting ..." + target);
            try {
                JaadasEngineReporter.post(endpoint, gson.toJson(job));
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean checkSingleApk(String filepath, String uid) throws IOException {
        if(!filepath.toLowerCase().endsWith(".apk"))
            return false;

        if(uid.equals("all"))
            return true;

        try (ApkFile apkFile = new ApkFile(new File(filepath))) {
            ApkMeta apkMeta = apkFile.getApkMeta();
            if(apkMeta.getSharedUserId() != null && apkMeta.getSharedUserId().contains(uid))
                return true;
            return false;
        }
    }

    private static List<String> getFileNames(List<String> fileNames, Path dir, String uidfilter) {
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path path : stream) {
                if(path.toFile().isDirectory()) {
                    getFileNames(fileNames, path, uidfilter);
                } else {
                    if (checkSingleApk(path.toAbsolutePath().toString(), uidfilter))
                        fileNames.add(path.toAbsolutePath().toString());
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return fileNames;
    }

    public static String calcFileHash(String filepath) throws IOException, NoSuchAlgorithmException {
        File file = new File(filepath);
        byte[] bytes = new byte[(int) file.length()];
        FileInputStream stream = new FileInputStream(file);
        stream.read(bytes);
        stream.close();
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(bytes,0,bytes.length);
        BigInteger i = new BigInteger(1,md.digest());
        String md5hash = String.format("%1$032X", i);
        return md5hash;
    }
}
