package org.shaechi.jaadas2.components;

import com.github.appreciated.prism.element.Language;
import com.github.appreciated.prism.element.PrismHighlighter;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.shaechi.jaadas2.entity.SourceCode;
import org.shaechi.jaadas2.entity.SourceCodeDetails;
import org.shaechi.jaadas2.entity.result.ScanResult;
import org.shaechi.jaadas2.entity.sourcesink.SerializedPathElement;
import org.shaechi.jaadas2.entity.sourcesink.SerializedSourceInfo;
import org.shaechi.jaadas2.services.ScanFileDirectoryService;
import org.shaechi.jaadas2.services.ScanResultService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**这里对应的是http://localhost:8090/job/2点开一个具体显示的Category Source Sink····
 *
 * 这里面传入的是SerializedPathElemnt.class
 *
 * */
public class ResultDetailPart extends VerticalLayout {
    private TextArea sourceField = new TextArea("Source");
    private TextArea sinkField = new TextArea("Sink");
    private TextField categoryField = new TextField("Category");
    private TextArea commentField = new TextArea("Comment");
    private TextArea apField = new TextArea("Sink AccessPath");
    private TextField associatedCompField = new TextField("Associated Component");
    private Checkbox validBox = new Checkbox("Ignored?");
    private Button saveCommentButton = new Button("Save comment");
    private TextField compPermissionField = new TextField("Component Access Permission");
    private TextArea sourceCodeField = new TextArea("SouceCode");

    private Accordion sourceCodeAccordion = new Accordion();
    private  Details sourceCodeDetails = new Details();
    VerticalLayout sourceCodeVerticalLayout = new VerticalLayout();

    public void setId(Long id) {
        this.id = id;
    }
    public void setScanJobId(Long scanJobId) {
        this.scanJobId = scanJobId;
    }

    private Long id;
    private Long scanJobId;

    private ScanResultService service;
    private ScanFileDirectoryService scanFileDirectoryService;

    private Grid<SerializedPathElement> apGrid = new Grid<>(SerializedPathElement.class);
    private Grid<SourceCodeDetails> grid = new Grid<>(SourceCodeDetails.class);

    public ResultDetailPart(ScanResultService service, ScanFileDirectoryService scanFileDirectoryService) {

        this.service = service;
        this.scanFileDirectoryService = scanFileDirectoryService;


        sourceField.setReadOnly(true);
        sinkField.setReadOnly(true);
        categoryField.setReadOnly(true);
        sourceCodeField.setReadOnly(true);

        //setWidthFull()就是设置这个格满，不是根据字符串的长度设置格的长度
        sourceField.setWidthFull();
        sinkField.setWidthFull();
        categoryField.setWidthFull();
        apField.setWidthFull();
        sourceCodeField.setWidthFull();
        associatedCompField.setWidthFull();
        associatedCompField.setReadOnly(false);
        apField.setReadOnly(true);
        apGrid.setWidthFull();
        apGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES);
        apGrid.recalculateColumnWidths();
        apGrid.setHeightFull();

        apGrid.removeColumnByKey("id");
        apGrid.removeColumnByKey("category");
        apGrid.removeColumnByKey("accessPath");


        this.add(categoryField);
        this.add(sourceField);
        this.add(sinkField);
        this.add(apField);
        this.add(associatedCompField);
        apGrid.setHeightByRows(true);
        //mainContent.setSizeFull();
        //mainContent.add(apGrid);

        commentField.addValueChangeListener(event -> service.updateResultComment(this.id, event.getValue()));
        commentField.setWidthFull();


        this.add(validBox);
        this.add(commentField);
        this.add(apGrid);
        this.add(sourceCodeDetails);

        validBox.addValueChangeListener(event -> {
            System.out.println("updating result status id :" + this.id);
            service.updateResultStatus(this.id, event.getValue());
        });


//        grid.setWidthFull();
//        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES);
//        grid.recalculateColumnWidths();
//        grid.setHeightFull();
//
//        grid.removeColumnByKey("id");
//        grid.removeColumnByKey("methodName");
//        grid.removeColumnByKey("parameters");
//        grid.removeColumnByKey("createTime");
//        grid.removeColumnByKey("sourceCode");
//        grid.setHeightByRows(true);
//
//
//        this.add(grid);

//        PrismHighlighter javacodeHighlighter = new PrismHighlighter(content,Language.java);
//        //this.add(sourceCodeField);



    }

    public void refreshUI() {
        ScanResult current = service.getById(id);
        SerializedSourceInfo info = current.getSourceInfo();
        sourceField.setValue(info.getStatement() + "\n" + info.getMethod());
        sinkField.setValue(current.getSinkInfo().getStatement() + "\n" + current.getSinkInfo().getMethod());
        categoryField.setValue(current.getVulnLevel().toString());
        apGrid.setItems(current.getSourceInfo().getPropagationPath());
        validBox.setValue(current.isIgnored());
        commentField.setValue(current.getComment());
        apField.setValue(current.getSinkInfo().getAccessPath().toString());

        List<SerializedPathElement> propagationPath = info.getPropagationPath();
        List<String> methods = new ArrayList<>();

        PrismHighlighter javaCode = null;

        for (SerializedPathElement serializedPathElement : propagationPath) {
            String method = serializedPathElement.getMethod();

            if (!methods.contains(method)) {
                String res = "";
                String content = "Decompilation is in progress, please wait······";
                try {
                    javaCode = new PrismHighlighter(content, Language.java);
                    res = scanFileDirectoryService.getContent(method, scanJobId);
                    if(res != null)
                        javaCode.getElement().setProperty("code",res);
                    //用tab实现代码区滑动功能
                    Tab methodTab = new Tab(method);
                    Tab javaCodeTab = new Tab(javaCode);
                    Tabs tabs = new Tabs(
                            methodTab, javaCodeTab
                    );
                    tabs.setWidth("900px");
                    tabs.setHeight("300px");
                    tabs.setOrientation(Tabs.Orientation.VERTICAL);
                    sourceCodeVerticalLayout.add(tabs);
                    methods.add(method);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //这里是Component中的details,不是entity中的SourceCodeDetails
        sourceCodeDetails.setSummaryText("Source Code");
        sourceCodeDetails.setContent(sourceCodeVerticalLayout);

//        sourceCodeAccordion.add("Source Code", tabs);
//        sourceCodeAccordion.close();
//        add(sourceCodeAccordion);

       // sourceCodeField.setValue(s);
       // grid.setItems(contents);

        String compval = service.getApkByCompId(current.getComponent().getId()).getPkgname() + " , " +
                current.getComponent().getComponentName() + ": exported ? " + current.getComponent().isExported();
        if (current.getComponent().getPermissions() != null && current.getComponent().getPermissions().size() > 0)
            compval += (" Permission: " + current.getComponent().getPermissions().get(0));
        associatedCompField.setValue(compval);
    }

}
