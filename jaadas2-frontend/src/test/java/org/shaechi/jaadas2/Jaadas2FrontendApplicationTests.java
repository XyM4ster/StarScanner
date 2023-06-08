package org.shaechi.jaadas2;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("dev")
@SpringBootTest

class Jaadas2FrontendApplicationTests {


//	@Autowired
//	ScanResultRepository scanResultRepository;
//	@Autowired
//	ApkComponentRepository apkComponentRepository;
//
//	@Autowired
//	ScanProjectRepository scanProjectRepository;
//
//	@Autowired
//	ProcessEngineReportService service;
//	@Test
//	void contextLoads() {
//		ScanProject project = new ScanProject();
//		project.setProjectName("default");
//		project.setProjectDesc("A default project");
//
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
//
//		project.addScanJob(job);
//
//		scanProjectRepository.save(project);
//		try {
//			XMLToResult.scanDirAndReportAPI("/tmp/jaadas2-output/24dea377-d155-4ac9-85ed-7dac3865906f", "9316454BC0312CF4987388BFDC43128C-FactoryAirCommandManager.apk", "24dea377-d155-4ac9-85ed-7dac3865906f");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	//
		//ScanProject project = XMLToResult.parseXMLWithReaderInDir("/tmp/vivoout");
		//scanProjectRepository.save(project);

		//ScanJob job = new ScanJob();
		//project.addScanJob(job);
		//scanProjectRepository.save(project);
	//}

}
