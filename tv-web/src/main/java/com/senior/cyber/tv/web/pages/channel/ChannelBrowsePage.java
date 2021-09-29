package com.senior.cyber.tv.web.pages.channel;

import com.senior.cyber.tv.dao.entity.Channel;
import com.senior.cyber.tv.dao.entity.Role;
import com.senior.cyber.tv.web.data.MySqlDataProvider;
import com.senior.cyber.tv.web.pages.MasterPage;
import com.senior.cyber.tv.web.repository.ChannelRepository;
import com.senior.cyber.tv.web.validator.ChannelNameValidator;
import com.senior.cyber.frmk.common.base.Bookmark;
import com.senior.cyber.frmk.common.base.WicketFactory;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.AbstractDataTable;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.DataTable;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.*;
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
import org.springframework.context.ApplicationContext;

import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Bookmark("/channel/browse")
@AuthorizeInstantiation({Role.NAME_ROOT, Role.NAME_ChannelBrowsePage})
public class ChannelBrowsePage extends MasterPage {

    protected Form<Void> form;

    protected UIRow row1;

    protected UIColumn name_column;
    protected UIContainer name_container;
    protected TextField<String> name_field;
    protected String name_value;

    protected Button createButton;

    protected FilterForm<Map<String, Expression<?>>> channel_browse_form;
    protected MySqlDataProvider channel_browse_provider;
    protected List<IColumn<Tuple, String>> channel_browse_column;
    protected AbstractDataTable<Tuple, String> channel_browse_table;

    @Override
    protected void onInitData() {
        super.onInitData();

        this.channel_browse_provider = new MySqlDataProvider("tbl_channel");
        this.channel_browse_provider.setSort("channel_id", SortOrder.DESCENDING);
        this.channel_browse_provider.setCountField("channel_id");

        this.channel_browse_column = new ArrayList<>();
        this.channel_browse_column.add(Column.normalColumn(Model.of("ID"), "uuid", "channel_id", this.channel_browse_provider, new LongConvertor()));
        this.channel_browse_column.add(Column.normalColumn(Model.of("Name"), "name", "name", this.channel_browse_provider, new StringConvertor()));
        this.channel_browse_column.add(new ActionFilteredColumn<>(Model.of("Action"), this::channel_browse_action_link, this::channel_browse_action_click));
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
        this.name_field.add(new ChannelNameValidator());
        this.name_field.add(new ContainerFeedbackBehavior());
        this.name_container.add(this.name_field);
        this.name_container.newFeedback("name_feedback", this.name_field);

        this.row1.lastUIColumn("last_column");

        this.createButton = new Button("createButton") {
            @Override
            public void onSubmit() {
                createButtonClick();
            }
        };
        this.form.add(this.createButton);

        this.channel_browse_form = new FilterForm<>("channel_browse_form", this.channel_browse_provider);
        body.add(this.channel_browse_form);

        this.channel_browse_table = new DataTable<>("channel_browse_table", this.channel_browse_column,
                this.channel_browse_provider, 20);
        this.channel_browse_form.add(this.channel_browse_table);
    }

    protected List<ActionItem> channel_browse_action_link(String link, Tuple model) {
        List<ActionItem> actions = new ArrayList<>(0);
        actions.add(new ActionItem("Edit", Model.of("Edit"), ItemCss.INFO));
        return actions;
    }

    protected void channel_browse_action_click(String link, Tuple model, AjaxRequestTarget target) {
        long uuid = model.get("uuid", long.class);
        if ("Edit".equals(link)) {
            PageParameters parameters = new PageParameters();
            parameters.add("uuid", uuid);
            setResponsePage(ChannelModifyPage.class, parameters);
        }
    }

    protected void createButtonClick() {
        ApplicationContext context = WicketFactory.getApplicationContext();
        ChannelRepository channelRepository = context.getBean(ChannelRepository.class);

        Channel channel = new Channel();
        channel.setName(this.name_value);

        channelRepository.save(channel);
        setResponsePage(ChannelBrowsePage.class);
    }

}
