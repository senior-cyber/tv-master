package com.senior.cyber.tv.web.pages.role;

import com.senior.cyber.tv.dao.entity.Role;
import com.senior.cyber.tv.web.data.MySqlDataProvider;
import com.senior.cyber.tv.web.pages.MasterPage;
import com.senior.cyber.frmk.common.base.Bookmark;
import com.senior.cyber.frmk.common.base.WicketFactory;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.AbstractDataTable;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.DataTable;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.*;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.convertor.BooleanConvertor;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.convertor.LongConvertor;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.convertor.StringConvertor;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.model.Model;
import org.springframework.context.ApplicationContext;

import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Bookmark("/role")
@AuthorizeInstantiation({Role.NAME_ROOT, Role.NAME_RoleBrowsePage})
public class RoleBrowsePage extends MasterPage {

    protected FilterForm<Map<String, Expression<?>>> role_browse_form;
    protected MySqlDataProvider role_browse_provider;
    protected List<IColumn<Tuple, String>> role_browse_column;
    protected AbstractDataTable<Tuple, String> role_browse_table;

    @Override
    protected void onInitData() {
        super.onInitData();
        this.role_browse_provider = new MySqlDataProvider("tbl_role");
        this.role_browse_provider.setSort("role_id", SortOrder.ASCENDING);
        this.role_browse_provider.setCountField("role_id");

        this.role_browse_column = new ArrayList<>();
        this.role_browse_column.add(Column.normalColumn(Model.of("ID"), "id", "role_id", this.role_browse_provider, new LongConvertor()));
        this.role_browse_column.add(Column.normalColumn(Model.of("Name"), "name", "name", this.role_browse_provider, new StringConvertor()));
        this.role_browse_column.add(Column.normalColumn(Model.of("Description"), "description", "description", this.role_browse_provider, new StringConvertor()));
        this.role_browse_column.add(Column.normalColumn(Model.of("Enabled"), "enabled", "enabled", this.role_browse_provider, new BooleanConvertor()));
        this.role_browse_column.add(new ActionFilteredColumn<>(Model.of("Action"), this::role_browse_action_link, this::role_browse_action_click));
    }

    @Override
    protected void onInitHtml(MarkupContainer body) {
        this.role_browse_form = new FilterForm<>("role_browse_form", this.role_browse_provider);
        body.add(this.role_browse_form);

        this.role_browse_table = new DataTable<>("role_browse_table", this.role_browse_column,
                this.role_browse_provider, 20);
        this.role_browse_form.add(this.role_browse_table);
    }

    protected List<ActionItem> role_browse_action_link(String link, Tuple model) {
        List<ActionItem> actions = new ArrayList<>(0);
        boolean enabled = model.get("enabled", boolean.class);
        if (enabled) {
            actions.add(new ActionItem("Disable", Model.of("Disable"), ItemCss.DANGER));
        } else {
            actions.add(new ActionItem("Enable", Model.of("Enable"), ItemCss.INFO));
        }
        return actions;
    }

    protected void role_browse_action_click(String link, Tuple model, AjaxRequestTarget target) {
        ApplicationContext context = WicketFactory.getApplicationContext();
        if ("Disable".equals(link)) {
//            long uuid = ((Number) model.get("uuid")).longValue();
//            service.disableRole(uuid);
            target.add(this.role_browse_table);
        } else if ("Enable".equals(link)) {
//            long uuid = ((Number) model.get("uuid")).longValue();
//            service.enableRole(uuid);
            target.add(this.role_browse_table);
        }
    }

}
