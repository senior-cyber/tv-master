package com.senior.cyber.tv.web.pages.user;

import com.senior.cyber.tv.dao.entity.Role;
import com.senior.cyber.tv.dao.entity.User;
import com.senior.cyber.tv.web.data.MySqlDataProvider;
import com.senior.cyber.tv.web.pages.MasterPage;
import com.senior.cyber.tv.web.repository.UserRepository;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Bookmark("/user/browse")
@AuthorizeInstantiation({Role.NAME_ROOT, Role.NAME_UserBrowsePage})
public class UserBrowsePage extends MasterPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserBrowsePage.class);

    protected Form<Void> form;

    protected UIRow row10;

    protected UIColumn mobile_phone_column;
    protected UIContainer mobile_phone_container;
    protected TextField<String> mobile_phone_field;
    protected String mobile_phone_value;

    protected UIColumn email_address_column;
    protected UIContainer email_address_container;
    protected TextField<String> email_address_field;
    protected String email_address_value;

    protected Button createButton;

    protected FilterForm<Map<String, Expression<?>>> user_browse_form;
    protected MySqlDataProvider user_browse_provider;
    protected List<IColumn<Tuple, String>> user_browse_column;
    protected AbstractDataTable<Tuple, String> user_browse_table;

    @Override
    protected void onInitData() {
        super.onInitData();
        this.user_browse_provider = new MySqlDataProvider("tbl_user u");
        this.user_browse_provider.setCountField("u.user_id");

        this.user_browse_provider.selectNormalColumn("uuid", "u.user_id", new LongConvertor());

        this.user_browse_provider.setSort("uuid", SortOrder.DESCENDING);

        this.user_browse_column = new ArrayList<>();
        this.user_browse_column.add(FilteredColumn.normalColumn(Model.of("Email Address"), "email_address", "u.email_address", this.user_browse_provider, new StringConvertor()));
        this.user_browse_column.add(FilteredColumn.normalColumn(Model.of("Mobile Phone"), "mobile_phone", "u.mobile_phone_number", this.user_browse_provider, new StringConvertor()));
        this.user_browse_column.add(FilteredColumn.normalColumn(Model.of("Enabled"), "enabled", "u.enabled", this.user_browse_provider, new BooleanConvertor()));
        this.user_browse_column.add(FilteredColumn.normalColumn(Model.of("Status"), "status", "u.status", this.user_browse_provider, new StringConvertor()));
        this.user_browse_column.add(new ActionFilteredColumn<>(Model.of("Action"), this::user_browse_action_link, this::user_browse_action_click));
    }

    @Override
    protected void onInitHtml(MarkupContainer body) {
        this.form = new Form<>("form");
        body.add(this.form);

        this.row10 = UIRow.newUIRow("row10", this.form);

        this.mobile_phone_column = this.row10.newUIColumn("mobile_phone_column", Size.Six_6);
        this.mobile_phone_container = this.mobile_phone_column.newUIContainer("mobile_phone_container");
        this.mobile_phone_field = new TextField<>("mobile_phone_field", new PropertyModel<>(this, "mobile_phone_value"));
        this.mobile_phone_field.setLabel(Model.of("Mobile Phone"));
        this.mobile_phone_field.setRequired(true);
//        this.mobile_phone_field.add(new MobilePhoneValidator());
        this.mobile_phone_field.add(new ContainerFeedbackBehavior());
        this.mobile_phone_container.add(this.mobile_phone_field);
        this.mobile_phone_container.newFeedback("mobile_phone_feedback", this.mobile_phone_field);

        this.email_address_column = this.row10.newUIColumn("email_address_column", Size.Six_6);
        this.email_address_container = this.email_address_column.newUIContainer("email_address_container");
        this.email_address_field = new TextField<>("email_address_field", new PropertyModel<>(this, "email_address_value"));
        this.email_address_field.setLabel(Model.of("Login"));
        this.email_address_field.setRequired(true);
//        this.email_address_field.add(new UserLoginValidator());
        this.email_address_field.add(new ContainerFeedbackBehavior());
        this.email_address_container.add(this.email_address_field);
        this.email_address_container.newFeedback("email_address_feedback", this.email_address_field);

        this.row10.lastUIColumn("last_column");

        this.createButton = new Button("createButton") {
            @Override
            public void onSubmit() {
                createButtonClick();
            }
        };
        this.form.add(this.createButton);

        this.user_browse_form = new FilterForm<>("user_browse_form", this.user_browse_provider);
        body.add(this.user_browse_form);

        this.user_browse_table = new DataTable<>("user_browse_table", this.user_browse_column,
                this.user_browse_provider, 20);
        this.user_browse_table.addTopToolbar(new FilterToolbar(this.user_browse_table, this.user_browse_form));
        this.user_browse_form.add(this.user_browse_table);
    }

    protected List<ActionItem> user_browse_action_link(String link, Tuple model) {
        List<ActionItem> actions = new ArrayList<>(0);
        String status = (String) model.get("status");
        if ("Pending".equals(status)) {
            actions.add(new ActionItem("Approve", Model.of("Approve"), ItemCss.INFO));
            actions.add(new ActionItem("Reject", Model.of("Reject"), ItemCss.INFO));
        } else if ("Approved".equals(status) || "Rejected".equals(status)) {
            actions.add(new ActionItem("Edit", Model.of("Edit"), ItemCss.INFO));
            boolean enabled = model.get("enabled", boolean.class);
            if (enabled) {
                actions.add(new ActionItem("Disable", Model.of("Disable"), ItemCss.DANGER));
            } else {
                actions.add(new ActionItem("Enable", Model.of("Enable"), ItemCss.INFO));
            }
        }
        return actions;
    }

    protected void user_browse_action_click(String link, Tuple model, AjaxRequestTarget target) {
        ApplicationContext context = WicketFactory.getApplicationContext();
        UserRepository userRepository = context.getBean(UserRepository.class);

        long uuid = model.get("uuid", Long.class);

        if ("Edit".equals(link)) {
            PageParameters parameters = new PageParameters();
            parameters.add("id", uuid);
            setResponsePage(UserModifyPage.class, parameters);
        } else if ("Disable".equals(link)) {
            Optional<User> userOptional = userRepository.findById(uuid);
            User user = userOptional.orElseThrow();
            user.setEnabled(false);
            userRepository.save(user);
            target.add(this.user_browse_table);
        } else if ("Enable".equals(link)) {
            Optional<User> userOptional = userRepository.findById(uuid);
            User user = userOptional.orElseThrow();
            user.setEnabled(true);
            userRepository.save(user);
            target.add(this.user_browse_table);
        }
    }

    protected void createButtonClick() {
        ApplicationContext context = WicketFactory.getApplicationContext();
        UserRepository userRepository = context.getBean(UserRepository.class);

        User user = new User();
        user.setMobilePhoneNumber(this.mobile_phone_value);
        user.setEmailAddress(this.email_address_value);
        user.setEnabled(true);

        userRepository.save(user);

        setResponsePage(UserBrowsePage.class);
    }

}
