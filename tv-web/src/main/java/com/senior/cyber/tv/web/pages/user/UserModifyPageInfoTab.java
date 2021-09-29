package com.senior.cyber.tv.web.pages.user;


import com.senior.cyber.tv.dao.entity.User;
import com.senior.cyber.tv.web.repository.UserRepository;
import com.senior.cyber.frmk.common.base.WicketFactory;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.tabs.ContentPanel;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.tabs.Tab;
import com.senior.cyber.frmk.common.wicket.layout.Size;
import com.senior.cyber.frmk.common.wicket.layout.UIColumn;
import com.senior.cyber.frmk.common.wicket.layout.UIContainer;
import com.senior.cyber.frmk.common.wicket.layout.UIRow;
import com.senior.cyber.frmk.common.wicket.markup.html.panel.ContainerFeedbackBehavior;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Optional;

public class UserModifyPageInfoTab extends ContentPanel {

    protected long uuid;

    protected Form<Void> form;

    protected UIRow row1;

    protected UIColumn email_address_column;
    protected UIContainer email_address_container;
    protected TextField<String> email_address_field;
    protected String email_address_value;

    protected UIRow row3;

    protected UIColumn mobile_phone_column;
    protected UIContainer mobile_phone_container;
    protected TextField<String> mobile_phone_field;
    protected String mobile_phone_value;

    protected Button saveButton;
    protected BookmarkablePageLink<Void> cancelButton;

    public UserModifyPageInfoTab(String id, String name, TabbedPanel<Tab> containerPanel, Map<String, Object> data) {
        super(id, name, containerPanel, data);
    }

    @Override
    protected void onInitData() {
        this.uuid = getPage().getPageParameters().get("id").toLong(-1L);
        loadData();
    }

    protected void loadData() {
        ApplicationContext context = WicketFactory.getApplicationContext();
        UserRepository userRepository = context.getBean(UserRepository.class);

        Optional<User> userOptional = userRepository.findById(this.uuid);

        User user = userOptional.orElseThrow();

        this.email_address_value = user.getEmailAddress();
        this.mobile_phone_value = user.getMobilePhoneNumber();
    }

    @Override
    protected void onInitHtml(MarkupContainer body) {
        this.form = new Form<>("form");
        body.add(this.form);

        this.row1 = UIRow.newUIRow("row1", this.form);

        this.email_address_column = this.row1.newUIColumn("email_address_column", Size.Four_4);
        this.email_address_container = this.email_address_column.newUIContainer("email_address_container");
        this.email_address_field = new TextField<>("email_address_field", new PropertyModel<>(this, "email_address_value"));
        this.email_address_field.setLabel(Model.of("Login"));
        this.email_address_field.setRequired(true);
//        this.email_address_field.add(new UserLoginValidator(this.uuid));
        this.email_address_field.add(new ContainerFeedbackBehavior());
        this.email_address_container.add(this.email_address_field);
        this.email_address_container.newFeedback("email_address_feedback", this.email_address_field);

        this.row1.lastUIColumn("last_column");

        this.row3 = UIRow.newUIRow("row3", this.form);

        this.mobile_phone_column = this.row3.newUIColumn("mobile_phone_column", Size.Four_4);
        this.mobile_phone_container = this.mobile_phone_column.newUIContainer("mobile_phone_container");
        this.mobile_phone_field = new TextField<>("mobile_phone_field", new PropertyModel<>(this, "mobile_phone_value"));
        this.mobile_phone_field.setLabel(Model.of("Mobile Phone"));
        this.mobile_phone_field.setRequired(true);
//        this.mobile_phone_field.add(new MobilePhoneValidator(this.uuid));
        this.mobile_phone_field.add(new ContainerFeedbackBehavior());
        this.mobile_phone_container.add(this.mobile_phone_field);
        this.mobile_phone_container.newFeedback("mobile_phone_feedback", this.mobile_phone_field);

        this.row3.lastUIColumn("last_column");

        this.saveButton = new Button("saveButton") {
            @Override
            public void onSubmit() {
                saveButtonClick();
            }
        };
        this.form.add(this.saveButton);

        this.cancelButton = new BookmarkablePageLink<>("cancelButton", UserBrowsePage.class);
        this.form.add(this.cancelButton);
    }

    protected void saveButtonClick() {
        ApplicationContext context = WicketFactory.getApplicationContext();
        UserRepository userRepository = context.getBean(UserRepository.class);

        Optional<User> userOptional = userRepository.findById(this.uuid);

        User user = userOptional.orElseThrow();
        user.setMobilePhoneNumber(this.mobile_phone_value);
        user.setEmailAddress(this.email_address_value);

        userRepository.save(user);

        setResponsePage(UserBrowsePage.class);
    }

}
