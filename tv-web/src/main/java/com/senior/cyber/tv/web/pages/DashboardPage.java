package com.senior.cyber.tv.web.pages;

import com.senior.cyber.tv.dao.entity.Role;
import com.senior.cyber.tv.web.data.ShowProvider;
import com.senior.cyber.frmk.common.wicket.markup.html.FullCalendar;
import com.senior.cyber.frmk.common.wicket.markup.html.FullCalendarProvider;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

@AuthorizeInstantiation({Role.NAME_ROOT, Role.NAME_DashboardPage})
public class DashboardPage extends MasterPage {

    protected FullCalendarProvider calendar_provider;
    protected FullCalendar calendar_field;

    public DashboardPage() {
    }


    @Override
    protected void onInitData() {
        super.onInitData();
        this.calendar_provider = new ShowProvider();
    }

    @Override
    protected void onInitHtml(MarkupContainer body) {
        this.calendar_field = new FullCalendar("calendar", this.calendar_provider);
        body.add(this.calendar_field);
    }

}
