package com.senior.cyber.tv.web.pages.group;

import com.senior.cyber.tv.dao.entity.Group;
import com.senior.cyber.tv.dao.entity.Role;
import com.senior.cyber.tv.web.data.MultipleChoiceProvider;
import com.senior.cyber.tv.web.data.MySqlDataProvider;
import com.senior.cyber.tv.web.pages.MasterPage;
import com.senior.cyber.tv.web.repository.GroupRepository;
import com.senior.cyber.tv.web.repository.RoleRepository;
import com.senior.cyber.frmk.common.base.Bookmark;
import com.senior.cyber.frmk.common.base.WicketFactory;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.AbstractDataTable;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.DataTable;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.*;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.convertor.BooleanConvertor;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.convertor.LongConvertor;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.convertor.StringConvertor;
import com.senior.cyber.frmk.common.wicket.layout.Size;
import com.senior.cyber.frmk.common.wicket.layout.UIColumn;
import com.senior.cyber.frmk.common.wicket.layout.UIContainer;
import com.senior.cyber.frmk.common.wicket.layout.UIRow;
import com.senior.cyber.frmk.common.wicket.markup.html.form.select2.Option;
import com.senior.cyber.frmk.common.wicket.markup.html.form.select2.Select2MultipleChoice;
import com.senior.cyber.frmk.common.wicket.markup.html.panel.ContainerFeedbackBehavior;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.context.ApplicationContext;

import javax.persistence.Tuple;
import java.util.*;

@Bookmark("/group/browse")
@AuthorizeInstantiation({Role.NAME_ROOT, Role.NAME_GroupBrowsePage})
public class GroupBrowsePage extends MasterPage {

    protected Form<Void> form;

    protected UIRow row1;

    protected UIColumn name_column;
    protected UIContainer name_container;
    protected TextField<String> name_field;
    protected String name_value;

    protected UIColumn role_column;
    protected UIContainer role_container;
    protected Select2MultipleChoice role_field;
    protected MultipleChoiceProvider<Long, String> role_provider;
    protected List<Option> role_value;

    protected Button createButton;

    protected FilterForm<Map<String, Expression<?>>> group_browse_form;
    protected MySqlDataProvider group_browse_provider;
    protected List<IColumn<Tuple, String>> group_browse_column;
    protected AbstractDataTable<Tuple, String> group_browse_table;

    @Override
    protected void onInitData() {
        super.onInitData();
        this.role_provider = new MultipleChoiceProvider<>(Long.class, new LongConvertor(), String.class, new StringConvertor(), "tbl_role", "role_id", "name");

        this.group_browse_provider = new MySqlDataProvider("tbl_group");
        this.group_browse_provider.setSort("id", SortOrder.DESCENDING);
        this.group_browse_provider.setCountField("group_id");

        this.group_browse_column = new ArrayList<>();
        this.group_browse_column.add(Column.normalColumn(Model.of("ID"), "id", "group_id", this.group_browse_provider, new LongConvertor()));
        this.group_browse_column.add(Column.normalColumn(Model.of("Name"), "name", "name", this.group_browse_provider, new StringConvertor()));
        this.group_browse_column.add(Column.normalColumn(Model.of("Enabled"), "enabled", "enabled", this.group_browse_provider, new BooleanConvertor()));
        this.group_browse_column.add(new ActionFilteredColumn<>(Model.of("Action"), this::group_browse_action_link, this::group_browse_action_click));
    }

    @Override
    protected void onInitHtml(MarkupContainer body) {
        this.form = new Form<>("form");
        body.add(this.form);

        this.row1 = UIRow.newUIRow("row1", this.form);

        this.name_column = this.row1.newUIColumn("name_column", Size.Six_6);
        this.name_container = this.name_column.newUIContainer("name_container");
        this.name_field = new TextField<>("name_field", new PropertyModel<>(this, "name_value"));
        this.name_field.setLabel(Model.of("Name"));
        this.name_field.setRequired(true);
//        this.name_field.add(new GroupNameValidator());
        this.name_field.add(new ContainerFeedbackBehavior());
        this.name_container.add(this.name_field);
        this.name_container.newFeedback("name_feedback", this.name_field);

        this.role_column = this.row1.newUIColumn("role_column", Size.Six_6);
        this.role_container = this.role_column.newUIContainer("role_container");
        this.role_field = new Select2MultipleChoice("role_field", new PropertyModel<>(this, "role_value"),
                this.role_provider);
        this.role_field.setLabel(Model.of("Role"));
        this.role_field.add(new ContainerFeedbackBehavior());
        this.role_container.add(this.role_field);
        this.role_container.newFeedback("role_feedback", this.role_field);

        this.row1.lastUIColumn("last_column");

        this.createButton = new Button("createButton") {
            @Override
            public void onSubmit() {
                createButtonClick();
            }
        };
        this.form.add(this.createButton);

        this.group_browse_form = new FilterForm<>("group_browse_form", this.group_browse_provider);
        body.add(this.group_browse_form);

        this.group_browse_table = new DataTable<>("group_browse_table", this.group_browse_column,
                this.group_browse_provider, 20);
        this.group_browse_form.add(this.group_browse_table);
    }

    protected List<ActionItem> group_browse_action_link(String link, Tuple model) {
        List<ActionItem> actions = new ArrayList<>(0);
        actions.add(new ActionItem("Edit", Model.of("Edit"), ItemCss.INFO));
        boolean enabled = (boolean) model.get("enabled");
        if (enabled) {
            actions.add(new ActionItem("Disable", Model.of("Disable"), ItemCss.DANGER));
        } else {
            actions.add(new ActionItem("Enable", Model.of("Enable"), ItemCss.INFO));
        }
        return actions;
    }

    protected void group_browse_action_click(String link, Tuple model, AjaxRequestTarget target) {
        ApplicationContext context = WicketFactory.getApplicationContext();
        GroupRepository groupRepository = context.getBean(GroupRepository.class);
        long id = model.get("id", long.class);
        if ("Edit".equals(link)) {
            PageParameters parameters = new PageParameters();
            parameters.add("id", id);
            setResponsePage(GroupModifyPage.class, parameters);
        } else if ("Disable".equals(link)) {
            Optional<Group> groupOptional = groupRepository.findById(id);
            Group group = groupOptional.orElseThrow();
            group.setEnabled(false);
            groupRepository.save(group);
            target.add(this.group_browse_table);
        } else if ("Enable".equals(link)) {
            Optional<Group> groupOptional = groupRepository.findById(id);
            Group group = groupOptional.orElseThrow();
            group.setEnabled(true);
            groupRepository.save(group);
            target.add(this.group_browse_table);
        }
    }

    protected void createButtonClick() {
        ApplicationContext context = WicketFactory.getApplicationContext();
        GroupRepository groupRepository = context.getBean(GroupRepository.class);
        RoleRepository roleRepository = context.getBean(RoleRepository.class);

        Group group = new Group();
        group.setEnabled(true);
        group.setName(this.name_value);

        Map<String, Role> roles = new HashMap<>();
        group.setRoles(roles);

        if (this.role_value != null) {
            for (Option option : this.role_value) {
                Optional<Role> roleOptional = roleRepository.findById(Long.valueOf(option.getId()));
                roles.put(UUID.randomUUID().toString(), roleOptional.orElseThrow());
            }
        }
        groupRepository.save(group);
        setResponsePage(GroupBrowsePage.class);
    }

}
