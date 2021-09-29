package com.senior.cyber.tv.web.pages.group;

import com.senior.cyber.tv.dao.entity.Role;
import com.senior.cyber.tv.web.pages.MasterPage;
import com.senior.cyber.frmk.common.base.Bookmark;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.tabs.Tab;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

import java.util.ArrayList;
import java.util.List;

@Bookmark("/group/modify")
@AuthorizeInstantiation({Role.NAME_ROOT, Role.NAME_GroupModifyPage})
public class GroupModifyPage extends MasterPage {

    protected TabbedPanel tabs;

    protected Tab info_tab;

    protected Tab role_tab;

    protected Tab member_tab;

    @Override
    protected void onInitData() {
        super.onInitData();
        this.info_tab = new Tab("info", "Group Info", GroupModifyPageInfoTab.class);
        this.role_tab = new Tab("role", "Granted Permission", GroupModifyPageRoleTab.class);
        this.member_tab = new Tab("member", "Member", GroupModifyPageMemberTab.class);
    }

    @Override
    protected void onInitHtml(MarkupContainer body) {
        List<Tab> tabs = new ArrayList<>();
        if (this.info_tab != null) {
            tabs.add(this.info_tab);
        }
        if (this.role_tab != null) {
            tabs.add(this.role_tab);
        }
        if (this.member_tab != null) {
            tabs.add(this.member_tab);
        }
        this.tabs = new TabbedPanel("tabs", tabs);
        body.add(this.tabs);
    }

}
