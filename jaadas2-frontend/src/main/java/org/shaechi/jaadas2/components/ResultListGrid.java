package org.shaechi.jaadas2.components;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.shaechi.jaadas2.entity.result.ScanResult;
import org.shaechi.jaadas2.services.ScanJobService;
import org.shaechi.jaadas2.views.ResultDetailView;

//这个是漏洞列表，最下面添加了一个点击事件
public class ResultListGrid extends Grid<ScanResult> {
    /*
    * 这里是把ScanResult当成一个grid。
    * 首先加载父类
    * 然后去掉在当前页面不需要显示的字段，保留点进去需要显示的字段
    * */
    public ResultListGrid() {
        super(ScanResult.class);
        configureGrid();
    }

    private void configureGrid() {
        this.setSizeFull();
        this.removeColumnByKey("id");
        this.removeColumnByKey("sinkInfo");
        this.removeColumnByKey("sourceInfo");
        this.addColumn("component.componentName");
        //this.addColumn("component.exported");
        //grid.addColumn("component.info.pkgname");
        this.removeColumnByKey("component");
        this.removeColumnByKey("comment");
        this.addColumn("sinkInfo.category");
        this.removeColumnByKey("ignored");
        //this.removeColumnByKey("exported");
        //grid.addColumn(e-> e.getSinkInfo().getCategory());
        this.setHeightByRows(true);
        this.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        //this.addItemClickListener(e -> this.getUI().ifPresent(ui-> ui.navigate(ResultDetailView.class, e.getItem().getId())));

        this.addItemDoubleClickListener(e -> this.getUI().ifPresent(ui-> ui.navigate(ResultDetailView.class, e.getItem().getId())));

        //this.setItemDetailsRenderer(
         //       new ComponentRenderer<>(person -> {

         //       }));
    }

}
