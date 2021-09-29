package com.senior.cyber.tv.web.pages.user;

import com.senior.cyber.tv.dao.entity.Role;
import com.senior.cyber.tv.dao.entity.User;
import com.senior.cyber.tv.dao.entity.User_;
import com.senior.cyber.tv.web.data.MySqlDataProvider;
import com.senior.cyber.tv.web.data.SingleChoiceProvider;
import com.senior.cyber.tv.web.pages.group.GroupBrowsePage;
import com.senior.cyber.tv.web.repository.RoleRepository;
import com.senior.cyber.tv.web.repository.UserRepository;
import com.senior.cyber.frmk.common.base.WicketFactory;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.AbstractDataTable;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.DataTable;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.*;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.convertor.BooleanConvertor;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.convertor.LongConvertor;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.repeater.data.table.filter.convertor.StringConvertor;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.tabs.ContentPanel;
import com.senior.cyber.frmk.common.wicket.extensions.markup.html.tabs.Tab;
import com.senior.cyber.frmk.common.wicket.layout.Size;
import com.senior.cyber.frmk.common.wicket.layout.UIColumn;
import com.senior.cyber.frmk.common.wicket.layout.UIContainer;
import com.senior.cyber.frmk.common.wicket.layout.UIRow;
import com.senior.cyber.frmk.common.wicket.markup.html.form.select2.Option;
import com.senior.cyber.frmk.common.wicket.markup.html.form.select2.Select2SingleChoice;
import com.senior.cyber.frmk.common.wicket.markup.html.panel.ContainerFeedbackBehavior;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.hibernate.jpa.QueryHints;
import org.springframework.context.ApplicationContext;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

public class UserModifyPageDeniedRoleTab extends ContentPanel {

    protected Long uuid;

    protected Form<Void> form;

    protected UIRow row1;

    protected UIColumn role_column;
    protected UIContainer role_container;
    protected Select2SingleChoice role_field;
    protected SingleChoiceProvider<Long, String> role_provider;
    protected Option role_value;

    protected Button denyButton;
    protected BookmarkablePageLink<Void> cancelButton;

    protected FilterForm<Map<String, Expression<?>>> role_browse_form;
    protected MySqlDataProvider role_browse_provider;
    protected List<IColumn<Tuple, String>> role_browse_column;
    protected AbstractDataTable<Tuple, String> role_browse_table;

    public UserModifyPageDeniedRoleTab(String id, String name, TabbedPanel<Tab> containerPanel,
                                       Map<String, Object> data) {
        super(id, name, containerPanel, data);
    }

    @Override
    protected void onInitData() {
        this.uuid = getPage().getPageParameters().get("id").toLong(-1);

        ApplicationContext context = WicketFactory.getApplicationContext();
//		DataContext dataContext = context.getBean(DataContext.class);
//		Role roleTable = Role.staticInitialize(dataContext);
//		UserDeny userDenyTable = UserDeny.staticInitialize(dataContext);

        String not_in = "SELECT ud.r_role_id FROM tbl_deny_role ud WHERE ud.r_user_id = " + this.uuid;
        this.role_provider = new SingleChoiceProvider<>(Long.class, new LongConvertor(),
                String.class, new StringConvertor(),
                "tbl_role r", "r.role_id", "r.name");
        this.role_provider.applyWhere("UserDeny", "r.role_id NOT IN (" + not_in + ")");

        this.role_browse_provider = new MySqlDataProvider("tbl_role r");
        this.role_browse_provider.applyJoin("UserDeny", "INNER JOIN tbl_deny_role ud ON ud.r_role_id = r.role_id");
        this.role_browse_provider.applyWhere("Group", "ud.r_user_id = " + this.uuid);
        this.role_browse_provider.setSort("role", SortOrder.ASCENDING);
        this.role_browse_provider.setCountField("ud.deny_role_id");
        this.role_browse_provider.selectNormalColumn("uuid", "ud.deny_role_id", new StringConvertor());

        this.role_browse_column = new ArrayList<>();
        this.role_browse_column.add(Column.normalColumn(Model.of("Role"), "role", "r.name", this.role_browse_provider, new StringConvertor()));
        this.role_browse_column.add(Column.normalColumn(Model.of("Description"), "description", "r.description", this.role_browse_provider, new StringConvertor()));
        this.role_browse_column.add(Column.normalColumn(Model.of("Enabled"), "enabled", "r.enabled", this.role_browse_provider, new BooleanConvertor()));
        this.role_browse_column.add(new ActionFilteredColumn<>(Model.of("Action"), this::role_browse_action_link, this::role_browse_action_click));
    }

    @Override
    protected void onInitHtml(MarkupContainer body) {
        this.form = new Form<>("form");
        body.add(this.form);

        this.row1 = UIRow.newUIRow("row1", this.form);

        this.role_column = this.row1.newUIColumn("role_column", Size.Six_6);
        this.role_container = this.role_column.newUIContainer("role_container");
        this.role_field = new Select2SingleChoice("role_field", new PropertyModel<>(this, "role_value"),
                this.role_provider);
        this.role_field.setLabel(Model.of("Role"));
        this.role_field.setRequired(true);
        this.role_field.add(new ContainerFeedbackBehavior());
        this.role_container.add(this.role_field);
        this.role_container.newFeedback("role_feedback", this.role_field);

        this.row1.lastUIColumn("last_column");

        this.denyButton = new Button("denyButton") {
            @Override
            public void onSubmit() {
                denyButtonClick();
            }
        };
        this.form.add(this.denyButton);

        this.cancelButton = new BookmarkablePageLink<>("cancelButton", GroupBrowsePage.class);
        this.form.add(this.cancelButton);

        this.role_browse_form = new FilterForm<>("role_browse_form", this.role_browse_provider);
        body.add(this.role_browse_form);

        this.role_browse_table = new DataTable<>("role_browse_table", this.role_browse_column,
                this.role_browse_provider, 20);
        this.role_browse_form.add(this.role_browse_table);
    }

    protected List<ActionItem> role_browse_action_link(String link, Tuple model) {
        List<ActionItem> actions = new ArrayList<>();
        actions.add(new ActionItem("Remove", Model.of("Remove"), ItemCss.DANGER));
        return actions;
    }

    protected void role_browse_action_click(String link, Tuple model, AjaxRequestTarget target) {
        if ("Remove".equals(link)) {
            String uuid = model.get("uuid", String.class);
            ApplicationContext context = WicketFactory.getApplicationContext();
            UserRepository userRepository = context.getBean(UserRepository.class);
            EntityManager entityManager = context.getBean(EntityManager.class);
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);

            EntityGraph<User> graph = entityManager.createEntityGraph(User.class);
            graph.addAttributeNodes("denyRoles");

            Root<User> root = criteriaQuery.from(User.class);
            criteriaQuery.select(root);
            criteriaQuery.where(criteriaBuilder.equal(root.get(User_.id), this.uuid));
            TypedQuery<User> query = entityManager.createQuery(criteriaQuery);
            query.setHint(QueryHints.HINT_LOADGRAPH, graph);
            User user = query.getSingleResult();

            user.getDenyRoles().remove(uuid);
            userRepository.save(user);
            target.add(this.role_browse_table);
        }
    }

    protected void denyButtonClick() {
        ApplicationContext context = WicketFactory.getApplicationContext();
        RoleRepository roleRepository = context.getBean(RoleRepository.class);
        UserRepository userRepository = context.getBean(UserRepository.class);
        EntityManager entityManager = context.getBean(EntityManager.class);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);

        EntityGraph<User> graph = entityManager.createEntityGraph(User.class);
        graph.addAttributeNodes("denyRoles");

        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(root.get(User_.id), this.uuid));
        TypedQuery<User> query = entityManager.createQuery(criteriaQuery);
        query.setHint(QueryHints.HINT_LOADGRAPH, graph);
        User user = query.getSingleResult();

        Role role = roleRepository.findById(Long.valueOf(this.role_value.getId())).orElseThrow();
        if (user.getDenyRoles() == null || user.getDenyRoles().isEmpty()) {
            Map<String, Role> roles = new HashMap<>();
            roles.put(UUID.randomUUID().toString(), role);
            user.setDenyRoles(roles);
        } else {
            user.getDenyRoles().put(UUID.randomUUID().toString(), role);
        }
        userRepository.save(user);
    }

}
