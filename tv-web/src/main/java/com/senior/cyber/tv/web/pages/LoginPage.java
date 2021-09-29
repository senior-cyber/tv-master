package com.senior.cyber.tv.web.pages;

import com.senior.cyber.tv.web.factory.WebSession;
import com.senior.cyber.frmk.common.base.AdminLTEResourceReference;
import com.senior.cyber.frmk.common.base.Bookmark;
import com.senior.cyber.frmk.common.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

@Bookmark("/login")
public class LoginPage extends WebPage {

    protected Form<Void> form = null;

    protected TextField<String> usernameField;
    protected ComponentFeedbackPanel usernameFeedback;
    protected String usernameValue;

    protected PasswordTextField passwordField;
    protected ComponentFeedbackPanel passwordFeedback;
    protected String passwordValue;

    protected Button loginButton;

    @Override
    protected void onInitialize() {
        super.onInitialize();
        this.form = new Form<>("form");
        add(this.form);

        this.usernameField = new TextField<>("usernameField", new PropertyModel<>(this, "usernameValue"));
        this.usernameField.setLabel(Model.of("Username"));
        this.usernameField.setRequired(true);
        this.form.add(this.usernameField);
        this.usernameFeedback = new ComponentFeedbackPanel("usernameFeedback", this.usernameField);
        this.form.add(this.usernameFeedback);

        this.passwordField = new PasswordTextField("passwordField", new PropertyModel<>(this, "passwordValue"));
        this.passwordField.setLabel(Model.of("Password"));
        this.passwordField.setRequired(true);
        this.form.add(this.passwordField);
        this.passwordFeedback = new ComponentFeedbackPanel("passwordFeedback", this.passwordField);
        this.form.add(this.passwordFeedback);

        this.loginButton = new Button("loginButton") {
            @Override
            public void onSubmit() {
                loginButtonClick();
            }
        };
        this.form.add(this.loginButton);
    }

    protected void loginButtonClick() {
        WebSession webSession = (WebSession) getSession();
        boolean valid = webSession.signIn(this.usernameValue, this.passwordValue);
        if (!valid) {
            this.usernameField.error("invalid");
            this.passwordField.error("invalid");
        } else {
            setResponsePage(getApplication().getHomePage());
        }
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        // <!-- Font Awesome -->
        response.render(CssHeaderItem.forReference(new AdminLTEResourceReference(AdminLTEResourceReference.CSS_FONT_AWESOME)));
        // <!-- Ionicons -->
        response.render(CssHeaderItem.forUrl(AdminLTEResourceReference.CSS_ION_ICONS));
        // <!-- icheck bootstrap -->
        response.render(CssHeaderItem.forReference(new AdminLTEResourceReference(AdminLTEResourceReference.CSS_ICHECK_BOOTSTRAP)));
        // <!-- Theme style -->
        response.render(CssHeaderItem.forReference(new AdminLTEResourceReference(AdminLTEResourceReference.CSS_THEME_STYLE)));
        // <!-- Google Font: Source Sans Pro -->
        response.render(CssHeaderItem.forUrl(AdminLTEResourceReference.CSS_GOOGLE_FONT));

        response.render(JavaScriptHeaderItem.forReference(getApplication().getJavaScriptLibrarySettings().getJQueryReference()));

        // <!-- Bootstrap 4 -->
        response.render(JavaScriptHeaderItem.forReference(new AdminLTEResourceReference(AdminLTEResourceReference.JS_BOOTSTRAP_4)));
        // <!-- AdminLTE App -->
        response.render(JavaScriptHeaderItem.forReference(new AdminLTEResourceReference(AdminLTEResourceReference.JS_ADMINLTE_APP)));
    }

}
