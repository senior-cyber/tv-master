package com.senior.cyber.tv.web.factory;

import com.senior.cyber.tv.web.pages.DashboardPage;
import com.senior.cyber.tv.web.pages.LoginPage;
import com.senior.cyber.tv.web.pages.PublicPage;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;

public class WicketFactory extends com.senior.cyber.frmk.common.base.AuthenticatedWicketFactory {

    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
        return WebSession.class;
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return LoginPage.class;
    }

    @Override
    public Class<? extends Page> getHomePage() {
        WebSession session = (WebSession) WebSession.get();
        if (session.isSignedIn()) {
            return DashboardPage.class;
        } else {
            return PublicPage.class;
        }
    }

    @Override
    protected void init() {
        super.init();

    }
}
