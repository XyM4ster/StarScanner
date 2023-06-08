package org.shaechi.jaadas2.views;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.shaechi.jaadas2.components.ResultListGrid;
import org.shaechi.jaadas2.components.UpdatebleResultPart;
import org.shaechi.jaadas2.entity.ScanJob;
import org.shaechi.jaadas2.entity.apk.ApkInfo;
import org.shaechi.jaadas2.entity.result.ScanResult;
import org.shaechi.jaadas2.repo.ScanJobRepository;
import org.shaechi.jaadas2.services.ScanFileDirectoryService;
import org.shaechi.jaadas2.services.ScanJobService;
import org.shaechi.jaadas2.services.ScanResultService;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Objects;
//http://localhost:8090/job/2
@Route("job")
public class ScanJobDetailView extends VerticalLayout implements HasUrlParameter<Long> {
    private ScanJob current;
    private ScanJobService service;
    private ScanResultService resultService;
    private ScanFileDirectoryService scanFileDirectoryService;
    private Long id;

    private TextField apkNameField = new TextField("Apk Name");
    private TextField apkHashField = new TextField("Apk Hash");
    private TextField apkPathField = new TextField("Apk Path");
    private TextField apkVersionField = new TextField("Apk Version");
    private TextField submitTimeField = new TextField("Submit Time");
    private TextField scanStartTimeField = new TextField("Scan Start Time");
    private TextField scanFinishTimeField = new TextField("Scan Finish Time");

    private ProgressBar progressBar = new ProgressBar();
    private ResultListGrid grid = new ResultListGrid();

    private TextField sourcecodeField = new TextField("Source Code");
//
//    public void setId(Long id) {
//        this.id = id;
//    }

    //参数都是Service
    public ScanJobDetailView(ScanResultService resultService, ScanJobService service, ScanFileDirectoryService scanFileDirectoryService) {
        this.resultService = resultService;
        this.service = service;
        this.scanFileDirectoryService = scanFileDirectoryService;

        HorizontalLayout layout = new HorizontalLayout();
        layout.add(apkNameField, apkHashField, apkPathField, apkVersionField);
        add(layout);

        layout = new HorizontalLayout();
        layout.add(submitTimeField, scanStartTimeField, scanFinishTimeField, progressBar);
        add(layout);

        layout = new HorizontalLayout();
        progressBar.setWidthFull();
        layout.setWidthFull();
        layout.add(progressBar);
        add(layout);

        progressBar.setWidthFull();
        progressBar.setValue(0);


    }

    @PreAuthorize("@securityService.checkScanJob(#aLong)")
    @Override
    public void setParameter(BeforeEvent beforeEvent, Long aLong) {
        current = this.service.getByJobId(aLong);
        this.id = aLong;
        refreshUI();
    }

    private String wrapper(Object in) {
        return in == null ? "undefined" : in.toString();
    }
    private void refreshUI()
    {
        this.apkNameField.setValue(wrapper(current.getApkInfo().getPkgname()));
        this.apkHashField.setValue(current.getApkInfo().getMd5hash());
        this.apkPathField.setValue(current.getApkInfo().getApkPath());
        this.apkVersionField.setValue(wrapper(current.getApkInfo().getVersion()));

        this.submitTimeField.setValue(wrapper(current.getSubmitTime()));
        this.scanStartTimeField.setValue(wrapper(current.getScanStartTime()));
        this.scanFinishTimeField.setValue(wrapper(current.getScanFinishTime()));
        if (current.getResults() != null) {

            add(new UpdatebleResultPart(this.resultService, current.getResults(), scanFileDirectoryService, id));


        }

        this.progressBar.setMax(current.getTotal());
        //there might be a situation that counter = total + 1, set to max in this time
        // e.g application is counted in as a component
        this.progressBar.setValue(Math.min(current.getCounter(), current.getTotal()));

    }
}
