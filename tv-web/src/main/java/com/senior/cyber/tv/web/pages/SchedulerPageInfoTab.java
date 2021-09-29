package com.senior.cyber.tv.web.pages;

import com.senior.cyber.tv.dao.entity.Show;
import com.senior.cyber.tv.web.data.SingleChoiceProvider;
import com.senior.cyber.tv.web.pages.show.ShowBrowsePage;
import com.senior.cyber.tv.web.repository.ShowRepository;
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
import com.senior.cyber.frmk.common.wicket.markup.html.form.select2.Option;
import com.senior.cyber.frmk.common.wicket.markup.html.form.select2.Select2SingleChoice;
import com.senior.cyber.frmk.common.wicket.markup.html.panel.ContainerFeedbackBehavior;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.springframework.context.ApplicationContext;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class SchedulerPageInfoTab extends ContentPanel {

    protected Form<Void> form;

    protected UIRow row1;

    protected UIColumn channel_column;
    protected UIContainer channel_container;
    protected Select2SingleChoice channel_field;
    protected SingleChoiceProvider<Long, String> channel_provider;
    protected Option channel_value;

    protected UIColumn from_column;
    protected UIContainer from_container;
    protected DateTextField from_field;
    protected Date from_value;

    protected UIColumn for_column;
    protected UIContainer for_container;
    protected DateTextField for_field;
    protected Date for_value;

    protected Button saveButton;
    protected BookmarkablePageLink<Void> cancelButton;

    public SchedulerPageInfoTab(String id, String name, TabbedPanel<Tab> containerPanel, Map<String, Object> data) {
        super(id, name, containerPanel, data);
    }

    @Override
    protected void onInitData() {
        this.channel_provider = new SingleChoiceProvider<>(Long.class, new LongConvertor(), String.class, new StringConvertor(), "tbl_channel", "channel_id", "name");
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = now.plusDays(1);
        this.for_value = tomorrow.toDate();
        if (tomorrow.getDayOfWeek() == DateTimeConstants.MONDAY) {
            this.from_value = tomorrow.minusDays(3).toDate();
        } else if (tomorrow.getDayOfWeek() == DateTimeConstants.SATURDAY) {
            this.from_value = tomorrow.minusDays(6).toDate();
        } else {
            this.from_value = now.toDate();
        }
    }

    @Override
    protected void onInitHtml(MarkupContainer body) {
        this.form = new Form<>("form");
        body.add(this.form);

        this.row1 = UIRow.newUIRow("row1", this.form);

        this.channel_column = this.row1.newUIColumn("channel_column", Size.Four_4);
        this.channel_container = this.channel_column.newUIContainer("channel_container");
        this.channel_field = new Select2SingleChoice("channel_field", new PropertyModel<>(this, "channel_value"), this.channel_provider);
        this.channel_field.setLabel(Model.of("Channel"));
        this.channel_field.add(new ContainerFeedbackBehavior());
        this.channel_field.setRequired(true);
        this.channel_container.add(this.channel_field);
        this.channel_container.newFeedback("channel_feedback", this.channel_field);

        this.from_column = this.row1.newUIColumn("from_column", Size.Four_4);
        this.from_container = this.from_column.newUIContainer("from_container");
        this.from_field = new DateTextField("from_field", new PropertyModel<>(this, "from_value"));
        this.from_field.setLabel(Model.of("From"));
        this.from_field.add(new ContainerFeedbackBehavior());
        this.from_field.setRequired(true);
        this.from_container.add(this.from_field);
        this.from_container.newFeedback("from_feedback", this.from_field);

        this.for_column = this.row1.newUIColumn("for_column", Size.Four_4);
        this.for_container = this.for_column.newUIContainer("for_container");
        this.for_field = new DateTextField("for_field", new PropertyModel<>(this, "for_value"));
        this.for_field.setLabel(Model.of("For"));
        this.for_field.add(new ContainerFeedbackBehavior());
        this.for_field.setRequired(true);
        this.for_container.add(this.for_field);
        this.for_container.newFeedback("for_feedback", this.for_field);

        this.row1.lastUIColumn("last_column");

        this.saveButton = new Button("saveButton") {
            @Override
            public void onSubmit() {
                saveButtonClick();
            }
        };
        this.form.add(this.saveButton);

        this.cancelButton = new BookmarkablePageLink<>("cancelButton", ShowBrowsePage.class);
        this.form.add(this.cancelButton);
    }

    protected void saveButtonClick() {
        ApplicationContext context = WicketFactory.getApplicationContext();
        ShowRepository showRepository = context.getBean(ShowRepository.class);
        List<Show> shows = showRepository.findBySchedule(this.from_value);

        for (Show show : shows) {
            Show n = new Show();
            n.setName(show.getName());
            n.setChannel(show.getChannel());
            n.setDuration(show.getDuration());
            n.setSchedule(this.for_value);
            n.setStartAt(show.getStartAt());
            showRepository.save(n);
        }

        setResponsePage(DashboardPage.class);
    }

}
