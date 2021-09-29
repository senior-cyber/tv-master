package com.senior.cyber.tv.web.pages.user;

import com.senior.cyber.tv.dao.entity.Role;
import com.senior.cyber.tv.web.factory.WebSession;
import com.senior.cyber.frmk.common.base.Bookmark;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;

@Bookmark("/user/exit")
@AuthorizeInstantiation({Role.NAME_UserExitPage})
public class UserExitPage extends WebPage {

    @Override
    protected void onInitialize() {
        super.onInitialize();
        WebSession session = (WebSession) getSession();
        session.exitCurrent();
        setResponsePage(getApplication().getHomePage());
    }

}
