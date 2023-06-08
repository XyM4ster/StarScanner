package org.shaechi.jaadas2.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.shaechi.jaadas2.components.ResultListGrid;
import org.shaechi.jaadas2.components.UpdatebleResultPart;
import org.shaechi.jaadas2.entity.result.ScanResult;
import org.shaechi.jaadas2.repo.ScanResultRepository;
import org.shaechi.jaadas2.services.ScanFileDirectoryService;
import org.shaechi.jaadas2.services.ScanResultService;

@Route("/results")
public class ResultListView extends VerticalLayout {

    private ScanResultRepository resultRepository;
    private ScanResultService scanResultService;
    private ScanFileDirectoryService scanFileDirectoryService;

    public ResultListView(ScanResultService scanResultService, ScanResultRepository resultRepository, ScanFileDirectoryService scanFileDirectoryService, Long id) {
        this.resultRepository = resultRepository;
        addClassName("list-view");
        setSizeFull();

        add(new UpdatebleResultPart(scanResultService, resultRepository.findAll(), scanFileDirectoryService, id));
    }

}
