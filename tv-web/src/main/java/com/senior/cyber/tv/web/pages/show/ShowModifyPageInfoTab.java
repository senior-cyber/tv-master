package com.senior.cyber.tv.web.pages.show;

import com.senior.cyber.frmk.common.base.WicketFactory;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.convertor.LongConvertor;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.convertor.StringConvertor;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.tabs.ContentPanel;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.tabs.Tab;
import com.senior.cyber.frmk.common.wicket.layout.Size;
import com.senior.cyber.frmk.common.wicket.layout.UIColumn;
import com.senior.cyber.frmk.common.wicket.layout.UIContainer;
import com.senior.cyber.frmk.common.wicket.layout.UIRow;
import com.senior.cyber.frmk.common.wicket.markup.html.form.DateTextField;
import com.senior.cyber.frmk.common.wicket.markup.html.form.TimeTextField;
import com.senior.cyber.frmk.common.wicket.markup.html.form.select2.Option;
import com.senior.cyber.frmk.common.wicket.markup.html.form.select2.Select2SingleChoice;
import com.senior.cyber.frmk.common.wicket.markup.html.panel.ContainerFeedbackBehavior;
import com.senior.cyber.tv.dao.entity.Channel;
import com.senior.cyber.tv.dao.entity.Show;
import com.senior.cyber.tv.web.data.SingleChoiceProvider;
import com.senior.cyber.tv.web.pages.DashboardPage;
import com.senior.cyber.tv.web.repository.ChannelRepository;
import com.senior.cyber.tv.web.repository.ShowRepository;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.RangeValidator;
import org.springframework.context.ApplicationContext;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

public class ShowModifyPageInfoTab extends ContentPanel {

    protected long uuid;

    protected Form<Void> form;

    protected UIRow row1;

    protected UIColumn name_column;
    protected UIContainer name_container;
    protected TextField<String> name_field;
    protected String name_value;

    protected UIRow row2;

    protected UIColumn channel_column;
    protected UIContainer channel_container;
    protected Select2SingleChoice channel_field;
    protected SingleChoiceProvider<Long, String> channel_provider;
    protected Option channel_value;

    protected UIColumn schedule_column;
    protected UIContainer schedule_container;
    protected DateTextField schedule_field;
    protected Date schedule_value;

    protected UIRow row3;

    protected UIColumn start_at_column;
    protected UIContainer start_at_container;
    protected TimeTextField start_at_field;
    protected Date start_at_value;

    protected UIColumn duration_column;
    protected UIContainer duration_container;
    protected TextField<Integer> duration_field;
    protected Integer duration_value;

    protected Button saveButton;
    protected Button saveNewButton;
    protected Button deleteButton;
    protected BookmarkablePageLink<Void> cancelButton;

    public ShowModifyPageInfoTab(String id, String name, TabbedPanel<Tab> containerPanel, Map<String, Object> data) {
        super(id, name, containerPanel, data);
    }

    @Override
    protected void onInitData() {
        this.channel_provider = new SingleChoiceProvider<>(Long.class, new LongConvertor(), String.class, new StringConvertor(), "tbl_channel", "channel_id", "name");

        PageParameters parameters = getPage().getPageParameters();
        this.uuid = parameters.get("uuid").toLong();

        ApplicationContext context = WicketFactory.getApplicationContext();
        ShowRepository showRepository = context.getBean(ShowRepository.class);

        Optional<Show> optionalShow = showRepository.findById(this.uuid);
        Show show = optionalShow.orElseThrow(() -> new WicketRuntimeException("show is not found"));
        this.schedule_value = show.getSchedule();
        this.start_at_value = show.getStartAt();
        this.duration_value = show.getDuration();
        this.name_value = show.getName();
        this.channel_value = new Option(String.valueOf(show.getChannel().getId()), show.getChannel().getName());
    }

    @Override
    protected void onInitHtml(MarkupContainer body) {
        this.form = new Form<>("form");
        body.add(this.form);

        this.row1 = UIRow.newUIRow("row1", this.form);

        this.name_column = this.row1.newUIColumn("name_column", Size.Twelve_12);
        this.name_container = this.name_column.newUIContainer("name_container");
        this.name_field = new TextField<>("name_field", new PropertyModel<>(this, "name_value"));
        this.name_field.setLabel(Model.of("Name"));
        this.name_field.add(new ContainerFeedbackBehavior());
        this.name_field.setRequired(true);
        this.name_container.add(this.name_field);
        this.name_container.newFeedback("name_feedback", this.name_field);

        this.row1.lastUIColumn("last_column");

        this.row2 = UIRow.newUIRow("row2", this.form);

        this.channel_column = this.row2.newUIColumn("channel_column", Size.Six_6);
        this.channel_container = this.channel_column.newUIContainer("channel_container");
        this.channel_field = new Select2SingleChoice("channel_field", new PropertyModel<>(this, "channel_value"), this.channel_provider);
        this.channel_field.setLabel(Model.of("Channel"));
        this.channel_field.add(new ContainerFeedbackBehavior());
        this.channel_field.setRequired(true);
        this.channel_container.add(this.channel_field);
        this.channel_container.newFeedback("channel_feedback", this.channel_field);

        this.schedule_column = this.row2.newUIColumn("schedule_column", Size.Six_6);
        this.schedule_container = this.schedule_column.newUIContainer("schedule_container");
        this.schedule_field = new DateTextField("schedule_field", new PropertyModel<>(this, "schedule_value"));
        this.schedule_field.setLabel(Model.of("Schedule"));
        this.schedule_field.add(new ContainerFeedbackBehavior());
        this.schedule_field.setRequired(true);
        this.schedule_container.add(this.schedule_field);
        this.schedule_container.newFeedback("schedule_feedback", this.schedule_field);

        this.row2.lastUIColumn("last_column");

        this.row3 = UIRow.newUIRow("row3", this.form);

        this.start_at_column = this.row3.newUIColumn("start_at_column", Size.Six_6);
        this.start_at_container = this.start_at_column.newUIContainer("start_at_container");
        this.start_at_field = new TimeTextField("start_at_field", new PropertyModel<>(this, "start_at_value"));
        this.start_at_field.setLabel(Model.of("Start At"));
        this.start_at_field.add(new ContainerFeedbackBehavior());
        this.start_at_field.setRequired(true);
        this.start_at_container.add(this.start_at_field);
        this.start_at_container.newFeedback("start_at_feedback", this.start_at_field);

        this.duration_column = this.row3.newUIColumn("duration_column", Size.Six_6);
        this.duration_container = this.duration_column.newUIContainer("duration_container");
        this.duration_field = new TextField<>("duration_field", new PropertyModel<>(this, "duration_value"));
        this.duration_field.setLabel(Model.of("Duration"));
        this.duration_field.add(RangeValidator.range(30, 240));
        this.duration_field.add(new ContainerFeedbackBehavior());
        this.duration_field.setRequired(true);
        this.duration_container.add(this.duration_field);
        this.duration_container.newFeedback("duration_feedback", this.duration_field);

        this.row3.lastUIColumn("last_column");

        this.saveButton = new Button("saveButton") {
            @Override
            public void onSubmit() {
                saveButtonClick();
            }
        };
        this.form.add(this.saveButton);

        this.saveNewButton = new Button("saveNewButton") {
            @Override
            public void onSubmit() {
                saveNewButtonClick();
            }
        };
        this.form.add(this.saveNewButton);

        this.deleteButton = new Button("deleteButton") {
            @Override
            public void onSubmit() {
                deleteButtonClick();
            }
        };
        this.form.add(this.deleteButton);

        this.cancelButton = new BookmarkablePageLink<>("cancelButton", DashboardPage.class);
        this.form.add(this.cancelButton);
    }

    protected void saveNewButtonClick() {
        ApplicationContext context = WicketFactory.getApplicationContext();
        ChannelRepository channelRepository = context.getBean(ChannelRepository.class);

        Optional<Channel> optionalChannel = channelRepository.findById(Long.valueOf(this.channel_value.getId()));
        Channel channel = optionalChannel.orElseThrow(() -> new WicketRuntimeException("channel is not found"));

        ShowRepository showRepository = context.getBean(ShowRepository.class);

        Optional<Show> optionalShow = showRepository.findById(this.uuid);
        Show show = optionalShow.orElseThrow(() -> new WicketRuntimeException("show is not found"));
        show.setChannel(channel);
        show.setName(this.name_value);
        show.setSchedule(this.schedule_value);
        show.setStartAt(this.start_at_value);
        show.setDuration(this.duration_value);

        showRepository.save(show);

        setResponsePage(DashboardPage.class);
    }

    protected void saveButtonClick() {
        ApplicationContext context = WicketFactory.getApplicationContext();
        ChannelRepository channelRepository = context.getBean(ChannelRepository.class);

        Optional<Channel> optionalChannel = channelRepository.findById(Long.valueOf(this.channel_value.getId()));
        Channel channel = optionalChannel.orElseThrow(() -> new WicketRuntimeException("channel is not found"));

        ShowRepository showRepository = context.getBean(ShowRepository.class);

        Show show = new Show();
        show.setChannel(channel);
        show.setName(this.name_value);
        show.setSchedule(this.schedule_value);
        show.setStartAt(this.start_at_value);
        show.setDuration(this.duration_value);


        showRepository.save(show);

        setResponsePage(DashboardPage.class);
    }

    protected void deleteButtonClick() {
        ApplicationContext context = WicketFactory.getApplicationContext();
        ShowRepository showRepository = context.getBean(ShowRepository.class);

        showRepository.deleteById(this.uuid);

        setResponsePage(DashboardPage.class);
    }

}
