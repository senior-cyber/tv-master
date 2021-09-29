package com.senior.cyber.tv.web.pages.show;

import com.senior.cyber.tv.dao.entity.Role;
import com.senior.cyber.tv.web.data.MySqlDataProvider;
import com.senior.cyber.tv.web.pages.MasterPage;
import com.senior.cyber.frmk.common.base.Bookmark;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.AbstractDataTable;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.DataTable;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.*;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.convertor.DateConvertor;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.convertor.LongConvertor;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.convertor.StringConvertor;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.convertor.TimeConvertor;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Bookmark("/show/browse")
@AuthorizeInstantiation({Role.NAME_ROOT, Role.NAME_ShowBrowsePage})
public class ShowBrowsePage extends MasterPage {

    protected BookmarkablePageLink<Void> createButton;

    protected FilterForm<Map<String, Expression<?>>> show_browse_form;
    protected MySqlDataProvider show_browse_provider;
    protected List<IColumn<Tuple, String>> show_browse_column;
    protected AbstractDataTable<Tuple, String> show_browse_table;

    @Override
    protected void onInitData() {
        super.onInitData();

        this.show_browse_provider = new MySqlDataProvider("tbl_show s");
        this.show_browse_provider.applyJoin("channel", "INNER JOIN tbl_channel c ON s.channel_id = c.channel_id");
        this.show_browse_provider.setSort("s.show_id", SortOrder.DESCENDING);
        this.show_browse_provider.setCountField("s.show_id");

        this.show_browse_column = new ArrayList<>();
        this.show_browse_column.add(Column.normalColumn(Model.of("ID"), "uuid", "s.show_id", this.show_browse_provider, new LongConvertor()));
        this.show_browse_column.add(Column.normalColumn(Model.of("Channel"), "channel", "c.name", this.show_browse_provider, new StringConvertor()));
        this.show_browse_column.add(Column.normalColumn(Model.of("Name"), "name", "s.name", this.show_browse_provider, new StringConvertor()));
        this.show_browse_column.add(Column.normalColumn(Model.of("Schedule"), "schedule", "s.schedule", this.show_browse_provider, new DateConvertor()));
        this.show_browse_column.add(Column.normalColumn(Model.of("Start At"), "start_at", "s.start_at", this.show_browse_provider, new TimeConvertor()));
        this.show_browse_column.add(Column.normalColumn(Model.of("Duration"), "duration", "s.duration", this.show_browse_provider, new LongConvertor()));
        this.show_browse_column.add(new ActionFilteredColumn<>(Model.of("Action"), this::show_browse_action_link, this::show_browse_action_click));
    }

    @Override
    protected void onInitHtml(MarkupContainer body) {
        this.createButton = new BookmarkablePageLink<>("createButton", ShowCreatePage.class);
        body.add(this.createButton);

        this.show_browse_form = new FilterForm<>("show_browse_form", this.show_browse_provider);
        body.add(this.show_browse_form);

        this.show_browse_table = new DataTable<>("show_browse_table", this.show_browse_column,
                this.show_browse_provider, 20);
        this.show_browse_form.add(this.show_browse_table);
    }

    protected List<ActionItem> show_browse_action_link(String link, Tuple model) {
        List<ActionItem> actions = new ArrayList<>(0);
        actions.add(new ActionItem("Edit", Model.of("Edit"), ItemCss.INFO));
        return actions;
    }

    protected void show_browse_action_click(String link, Tuple model, AjaxRequestTarget target) {
        long uuid = model.get("uuid", long.class);
        if ("Edit".equals(link)) {
            PageParameters parameters = new PageParameters();
            parameters.add("uuid", uuid);
            setResponsePage(ShowModifyPage.class, parameters);
        }
    }

}
