package org.shaechi.jaadas2.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.shaechi.jaadas2.components.ResultDetailPart;
import org.shaechi.jaadas2.entity.apk.ApkInfo;
import org.shaechi.jaadas2.entity.result.ScanResult;
import org.shaechi.jaadas2.entity.sourcesink.SerializedAccessPath;
import org.shaechi.jaadas2.entity.sourcesink.SerializedPathElement;
import org.shaechi.jaadas2.entity.sourcesink.SerializedSourceInfo;
import org.shaechi.jaadas2.repo.ScanResultRepository;
import org.shaechi.jaadas2.services.ScanFileDirectoryService;
import org.shaechi.jaadas2.services.ScanResultService;
import org.springframework.beans.factory.annotation.Autowired;

@Route("result")
public class ResultDetailView extends VerticalLayout implements HasUrlParameter<Long> {

    private ResultDetailPart part;

    public ResultDetailView(ScanResultService resultService, ScanFileDirectoryService scanFileDirectoryService) {
        part = new ResultDetailPart(resultService, scanFileDirectoryService);
        add(part);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Long aLong) {
        part.setId(aLong);
        part.refreshUI();
    }

}
