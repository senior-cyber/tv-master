package com.senior.cyber.tv.web.pages.channel;

import com.senior.cyber.tv.dao.entity.Role;
import com.senior.cyber.tv.web.pages.MasterPage;
import com.senior.cyber.frmk.common.base.Bookmark;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.tabs.Tab;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

import java.util.ArrayList;
import java.util.List;

@Bookmark("/channel/modify")
@AuthorizeInstantiation({Role.NAME_ROOT, Role.NAME_ChannelModifyPage})
public class ChannelModifyPage extends MasterPage {

    protected TabbedPanel tabs;

    protected Tab info_tab;

    @Override
    protected void onInitData() {
        super.onInitData();
        this.info_tab = new Tab("info", "Channel Info", ChannelModifyPageInfoTab.class);
    }

    @Override
    protected void onInitHtml(MarkupContainer body) {
        List<Tab> tabs = new ArrayList<>();
        if (this.info_tab != null) {
            tabs.add(this.info_tab);
        }
        this.tabs = new TabbedPanel("tabs", tabs);
        body.add(this.tabs);
    }

}
