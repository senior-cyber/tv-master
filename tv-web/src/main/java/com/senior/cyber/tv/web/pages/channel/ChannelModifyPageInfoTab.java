package com.senior.cyber.tv.web.pages.channel;

import com.senior.cyber.tv.dao.entity.Channel;
import com.senior.cyber.tv.web.repository.ChannelRepository;
import com.senior.cyber.tv.web.validator.ChannelNameValidator;
import com.senior.cyber.frmk.common.base.WicketFactory;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.tabs.ContentPanel;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.tabs.Tab;
import com.senior.cyber.frmk.common.wicket.layout.Size;
import com.senior.cyber.frmk.common.wicket.layout.UIColumn;
import com.senior.cyber.frmk.common.wicket.layout.UIContainer;
import com.senior.cyber.frmk.common.wicket.layout.UIRow;
import com.senior.cyber.frmk.common.wicket.markup.html.panel.ContainerFeedbackBehavior;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.WicketRuntimeException;
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

public class ChannelModifyPageInfoTab extends ContentPanel {

    protected long uuid;

    protected Form<Void> form;

    protected UIRow row1;

    protected UIColumn name_column;
    protected UIContainer name_container;
    protected TextField<String> name_field;
    protected String name_value;

    protected Button saveButton;
    protected BookmarkablePageLink<Void> cancelButton;

    public ChannelModifyPageInfoTab(String id, String name, TabbedPanel<Tab> containerPanel, Map<String, Object> data) {
        super(id, name, containerPanel, data);
    }

    @Override
    protected void onInitData() {
        this.uuid = getPage().getPageParameters().get("uuid").toLong(-1L);
        loadData();
    }

    protected void loadData() {
        ApplicationContext context = WicketFactory.getApplicationContext();
        ChannelRepository channelRepository = context.getBean(ChannelRepository.class);
        Optional<Channel> optionalChannel = channelRepository.findById(this.uuid);
        Channel channel = optionalChannel.orElseThrow(() -> new WicketRuntimeException("Not Found"));
        this.name_value = channel.getName();
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
        this.name_field.add(new ChannelNameValidator(this.uuid));
        this.name_field.add(new ContainerFeedbackBehavior());
        this.name_field.setRequired(true);
        this.name_container.add(this.name_field);
        this.name_container.newFeedback("name_feedback", this.name_field);

        this.row1.lastUIColumn("last_column");

        this.saveButton = new Button("saveButton") {
            @Override
            public void onSubmit() {
                saveButtonClick();
            }
        };
        this.form.add(this.saveButton);

        this.cancelButton = new BookmarkablePageLink<>("cancelButton", ChannelBrowsePage.class);
        this.form.add(this.cancelButton);
    }

    protected void saveButtonClick() {
        ApplicationContext context = WicketFactory.getApplicationContext();
        ChannelRepository channelRepository = context.getBean(ChannelRepository.class);
        Optional<Channel> optionalChannel = channelRepository.findById(this.uuid);
        Channel channel = optionalChannel.orElseThrow(() -> new WicketRuntimeException("Not Found"));
        channel.setName(this.name_value);
        channelRepository.save(channel);
        setResponsePage(ChannelBrowsePage.class);
    }

}
