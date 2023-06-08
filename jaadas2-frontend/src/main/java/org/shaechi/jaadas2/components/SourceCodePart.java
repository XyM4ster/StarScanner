package org.shaechi.jaadas2.components;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.shaechi.jaadas2.services.ScanResultService;

public class SourceCodePart extends VerticalLayout {
    TextField sourcecode = new TextField("Souce code");


    private Long id;

    private ScanResultService service;
}
