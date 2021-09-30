package com.senior.cyber.tv.web.pages;

import com.senior.cyber.frmk.common.base.WicketFactory;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.convertor.LongConvertor;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.convertor.StringConvertor;
import com.senior.cyber.frmk.common.wicket.layout.Size;
import com.senior.cyber.frmk.common.wicket.layout.UIColumn;
import com.senior.cyber.frmk.common.wicket.layout.UIContainer;
import com.senior.cyber.frmk.common.wicket.layout.UIRow;
import com.senior.cyber.frmk.common.wicket.markup.html.FullCalendar;
import com.senior.cyber.frmk.common.wicket.markup.html.FullCalendarProvider;
import com.senior.cyber.frmk.common.wicket.markup.html.form.select2.Option;
import com.senior.cyber.frmk.common.wicket.markup.html.form.select2.Select2SingleChoice;
import com.senior.cyber.frmk.common.wicket.markup.html.panel.ContainerFeedbackBehavior;
import com.senior.cyber.tv.dao.entity.Channel;
import com.senior.cyber.tv.dao.entity.Role;
import com.senior.cyber.tv.web.data.ShowProvider;
import com.senior.cyber.tv.web.data.SingleChoiceProvider;
import com.senior.cyber.tv.web.repository.ChannelRepository;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

@AuthorizeInstantiation({Role.NAME_ROOT, Role.NAME_DashboardPage})
public class DashboardPage extends MasterPage {

    protected Form<Void> form;

    protected UIRow row1;

    protected UIColumn channel_column;
    protected UIContainer channel_container;
    protected Select2SingleChoice channel_field;
    protected SingleChoiceProvider<Long, String> channel_provider;
    protected Option channel_value;

    protected Button filterButton;
    protected BookmarkablePageLink<Void> cancelButton;

    protected FullCalendarProvider calendar_provider;
    protected FullCalendar calendar_field;

    public DashboardPage() {
    }


    @Override
    protected void onInitData() {
        super.onInitData();
        ApplicationContext context = WicketFactory.getApplicationContext();
        ChannelRepository channelRepository = context.getBean(ChannelRepository.class);

        PageParameters parameters = getPageParameters();
        String uuid = parameters.get("uuid").toString();
        this.channel_provider = new SingleChoiceProvider<>(Long.class, new LongConvertor(), String.class, new StringConvertor(), "tbl_channel", "channel_id", "name");
        this.calendar_provider = new ShowProvider(uuid);

        Channel channel = null;
        if (uuid != null && !"".equals(uuid)) {
            Optional<Channel> optionalChannel = channelRepository.findById(Long.parseLong(uuid));
            channel = optionalChannel.orElseThrow();
            this.channel_value = new Option(String.valueOf(channel.getId()), channel.getName());
        }
    }

    @Override
    protected void onInitHtml(MarkupContainer body) {
        this.form = new Form<>("form");
        body.add(this.form);

        this.row1 = UIRow.newUIRow("row1", this.form);

        this.channel_column = this.row1.newUIColumn("channel_column", Size.Six_6);
        this.channel_container = this.channel_column.newUIContainer("channel_container");
        this.channel_field = new Select2SingleChoice("channel_field", new PropertyModel<>(this, "channel_value"), this.channel_provider);
        this.channel_field.setLabel(Model.of("Channel"));
        this.channel_field.add(new ContainerFeedbackBehavior());
        this.channel_field.setRequired(true);
        this.channel_container.add(this.channel_field);
        this.channel_container.newFeedback("channel_feedback", this.channel_field);

        this.row1.lastUIColumn("last_column");

        this.filterButton = new Button("filterButton") {
            @Override
            public void onSubmit() {
                filterButtonClick();
            }
        };
        this.form.add(this.filterButton);

        this.cancelButton = new BookmarkablePageLink<>("cancelButton", DashboardPage.class);
        this.form.add(this.cancelButton);

        this.calendar_field = new FullCalendar("calendar", this.calendar_provider);
        body.add(this.calendar_field);
    }

    protected void filterButtonClick() {
        PageParameters parameters = new PageParameters();
        parameters.add("uuid", this.channel_value.getId());
        setResponsePage(DashboardPage.class, parameters);
    }

}
