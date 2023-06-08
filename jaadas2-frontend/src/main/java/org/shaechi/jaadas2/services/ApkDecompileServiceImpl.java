//package org.shaechi.jaadas2.services;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//@Slf4j
//@Service
//public class ApkDecompileServiceImpl implements ApkDecompileService{
//    int state = 0;
//
//    @Async("parallelDecompileExecutor")
//    public void parallelDecompile(String jar_name) throws IOException {
//        String message = jar_name + "反编译未完成";
//        String batPath = "./jadx/bat" + jar_name + ".bat";
//        File file = new File(batPath);
//        file.createNewFile();
//        //String jar_name = "PowerKeeper.apk";
//        String context = "jadx -d ./decompile/" + jar_name + " jadx/lib/" + jar_name;
//
//        //创建输入流
//        FileOutputStream fileOutputStream = null;
//        fileOutputStream = new FileOutputStream(file);
//        fileOutputStream.write(context.getBytes());
//        fileOutputStream.close();
//
//        String cmd = "cmd.exe /c start " + file.getAbsolutePath();
//        Process exec = Runtime.getRuntime().exec(cmd);
//        message = jar_name + "反编译成功";
//        log.info("do something, message={}", message);
//        state = 1;
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            log.error("do something error: ", e);
//        }
//        //file.delete();
//    }
//
//    public int getState(){
//        return state;
//    }
//}
