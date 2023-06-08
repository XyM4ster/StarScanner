package org.shaechi.jaadas2;

import org.shaechi.jaadas2.entity.*;
import org.shaechi.jaadas2.repo.*;
//import org.shaechi.jaadas2.services.ApkDecompileServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Jaadas2FrontendApplication implements ApplicationRunner {
	@Autowired
	SourceCodeRepository sourceCodeRepository;
	@Autowired
	ScanResultRepository scanResultRepository;
	@Autowired
	ScanJobRepository scanJobRepository;
	@Autowired
	ScanProjectRepository scanProjectRepository;

	@Autowired
	SourceCodeDetailsRepository sourceCodeDetailsRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	MenuRepository menuRepository;
	@Autowired
	RoleRepository roleRepository;



	public static void main(String[] args) {

		SpringApplication.run(Jaadas2FrontendApplication.class, args);

	}



	@Override
	public void run(ApplicationArguments args) throws Exception {

//		ScanProject project = new ScanProject();
//		project.setProjectName("default");
//		project.setProjectDesc("A default project");
//		scanProjectRepository.save(project);
//		ScanJob job = new ScanJob();
//		//scanDirAndReportAPI("/tmp/jaadas2-output/24dea377-d155-4ac9-85ed-7dac3865906f", "9316454BC0312CF4987388BFDC43128C-FactoryAirCommandManager.apk", "24dea377-d155-4ac9-85ed-7dac3865906f");
//		//
//		job.setUuid(UUID.fromString("24dea377-d155-4ac9-85ed-7dac3865906f"));
//		job.setStatus(JobStatus.Running);
//
//		ApkInfo info = new ApkInfo();
//		info.setApkPath("/tmp/9316454BC0312CF4987388BFDC43128C-FactoryAirCommandManager.apk");
//		info.setMd5hash("9316454BC0312CF4987388BFDC43128C");
//
//		job.setSubmitTime(LocalDateTime.now());
//		job.setScanStartTime(LocalDateTime.now());
//		job.setApkInfo(info);
//		scanJobRepository.save(job);
//		project.addScanJob(job);

/**
 * ce shi dai ma
 * */
//
//		Menu menu = new Menu();
//		menu.setName("后台管理");
//		menu.setUrl("/admin");
//
//
//		List<Menu> menuList = new ArrayList<>();
//		menuList.add(menu);
//
//		List<Menu> menuList1 = new ArrayList<>();
//		menuList.add(menu);
//
////
////        Role superAdmin = new Role();
////        superAdmin.setName("超级管理员");
////        superAdmin.setCode("superadmin");
//
//
//		Role admin = new Role();
//		admin.setName("管理员1");
//		admin.setCode("admin");
//		admin.setMenu(menuList1);
//		roleRepository.save(admin);
////        roleRepository.save(superAdmin);
//
////        List<Role> roleList = new ArrayList<>();
////        roleList.add(admin);
////        List<Role> roleList1 = new ArrayList<>();
////        roleList.add(superAdmin);
//
//		ScanProject scanProject = scanProjectRepository.getById((long) 1);
//		ScanJob scanJob = scanJobRepository.getById((long) 2);
//
////		User px = new User();
////        px.setUsername("pxiang1");
////        px.setPassword("lbg321313");
////        px.setRole(superAdmin);
////		px.addScanProject(scanProject);
////        px.addScanJob(scanJob);
////        userRepository.save(px);
//        User bg = new User();
//        bg.setUsername("bge2");
//        bg.setPassword("lpxasddsa");
//		bg.addScanProject(scanProject);
//		bg.addScanJob(scanJob);
//		bg.setRole(admin);
////        userRepository.save(px);
//        userRepository.save(bg);

//		ScanJob byId = scanJobRepository.getById((long) 2);
//		UUID uuid = byId.getUuid();
//		ProcessEngineReportService processEngineReportService = new ProcessEngineReportService(uuid, );


//		String path = "D:\\success\\StarScanner-Android-Backend-ceac0fb\\apk";
//		File file = new File(path);
//		File[] filelist = file.listFiles();
//		ScanJob byId = scanJobRepository.getById((long) 2);
//
//		for (int i = 0; i < 6; i++) {
//			ScanFileDirectoryServiceImpl scanFileDirectoryService = new ScanFileDirectoryServiceImpl(sourceCodeRepository, scanResultRepository, sourceCodeDetailsRepository, scanJobRepository);
//			scanFileDirectoryService.apkDeCompile(filelist[i].getAbsolutePath(), byId);
//
//		}

//
//	ScanFileDirectoryServiceImpl scanFileDirectoryService = new ScanFileDirectoryServiceImpl(sourceCodeRepository, scanResultRepository, sourceCodeDetailsRepository,scanJobRepository);
//		ScanJob byId = scanJobRepository.getById((long) 2);
//		scanFileDirectoryService.apkDeCompile("PowerKeeper.apk", byId);




//		SourceCodeDetails sourceCodeDetails = new SourceCodeDetails();
//		sourceCodeDetails.setMethodName("saasd");
//		sourceCodeDetails.setSourceCode(sourceCode);
//		sourceCodeDetails.setCreateTime(new Date());
//		sourceCodeDetails.setContent("sadd");
//		sourceCodeDetailsRepository.save(sourceCodeDetails);
//		scanFileDirectoryService.scanFilesWithRecursion(path1, byId);
//		scanFileDirectoryService.scanFilesWithRecursion(path2, byId);
	//	ScanFileDirectoryServiceImpl scanFileDirectoryService = new ScanFileDirectoryServiceImpl(sourceCodeRepository,scanResultRepository);
//		String path = scanFileDirectoryService.getPath((long) 53);
//		System.out.println(path);
//		String filePath = "D:\\SpringBoot\\output\\sources\\com\\miui\\powerkeeper\\utils\\FileUtil.java";
//		String methodName = "<com.miui.powerkeeper.utils.FileUtil: void unZipFile(java.lang.String,java.lang.String)>";
//		String s = scanFileDirectoryService.readFile(filePath, methodName);
//		System.out.println(s);




	}
}
