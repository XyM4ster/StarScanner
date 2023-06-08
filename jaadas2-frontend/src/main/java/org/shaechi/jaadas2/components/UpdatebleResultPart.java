package org.shaechi.jaadas2.components;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.shaechi.jaadas2.entity.result.ScanResult;
import org.shaechi.jaadas2.entity.vuln.VulnLevel;
import org.shaechi.jaadas2.services.ScanFileDirectoryService;
import org.shaechi.jaadas2.services.ScanResultService;

import java.util.List;

public class UpdatebleResultPart extends VerticalLayout {
    private ResultListGrid grid = new ResultListGrid();

    private Select<String> select = new Select<>();
    private Checkbox reachableBox = new Checkbox("Reachable Only?");
    private Checkbox ignoredBox = new Checkbox("Hide ignored?");
    private Checkbox permissionBox = new Checkbox("Non-permission only?");


    public UpdatebleResultPart(ScanResultService service, List<ScanResult> results, ScanFileDirectoryService scanFileDirectoryService, Long id) {

        //select.setLabel("Level");
        select.setItems("HIGH", "MEDIUM", "LOW", "INFO", "ALL");
        select.setValue("ALL");

        HorizontalLayout l = new HorizontalLayout();
        l.add(reachableBox, ignoredBox, permissionBox, select);
        add(l);
        add(grid);

        ListDataProvider<ScanResult> dataProvider = new ListDataProvider<>(results);
        reachableBox.addValueChangeListener(e -> applyFilter(dataProvider));
        ignoredBox.addValueChangeListener(e -> applyFilter(dataProvider));
        permissionBox.addValueChangeListener(e -> applyFilter(dataProvider));
        select.addValueChangeListener(e -> applyFilter(dataProvider));
        grid.setDataProvider(dataProvider);

        ignoredBox.setValue(true);
        reachableBox.setValue(true);
        permissionBox.setValue(true);
        select.setValue("HIGH");

        grid.setItemDetailsRenderer(new ComponentRenderer<>(result -> {
            ResultDetailPart part =  new ResultDetailPart(service, scanFileDirectoryService);
            part.setId(result.getId());
            part.setScanJobId(id);
            part.refreshUI();
            return part;
        }));

        //grid.setDetailsVisibleOnClick(true);
    }

    private void applyFilter(ListDataProvider<ScanResult> dataProvider) {
        dataProvider.clearFilters();
        if (reachableBox.getValue())
            dataProvider.addFilter(result -> result.getComponent().isExported());
        if (ignoredBox.getValue())
            dataProvider.addFilter(result -> !result.isIgnored());
        if (permissionBox.getValue())
            dataProvider.addFilter(result -> !(result.getComponent().getPermissions() != null && result.getComponent().getPermissions().size() > 0));

        String level = select.getValue();
        switch (level) {
            case "ALL":
                break;
            case "HIGH":
                dataProvider.addFilter(result -> result.getVulnLevel().equals(VulnLevel.HIGH));
                break;
            case "MEDIUM":
                dataProvider.addFilter(result -> result.getVulnLevel().equals(VulnLevel.MEDIUM));
                break;
            case "LOW":
                dataProvider.addFilter(result -> result.getVulnLevel().equals(VulnLevel.LOW));
                break;
            case "INFO":
                dataProvider.addFilter(result -> result.getVulnLevel().equals(VulnLevel.INFO));
                break;
        }
    }
}
