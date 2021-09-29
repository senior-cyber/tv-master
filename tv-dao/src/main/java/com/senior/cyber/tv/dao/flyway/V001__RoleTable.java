package com.senior.cyber.tv.dao.flyway;

import com.senior.cyber.frmk.jdbc.query.InsertQuery;
import com.senior.cyber.tv.dao.LiquibaseMigration;
import com.senior.cyber.tv.dao.entity.Role;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class V001__RoleTable extends LiquibaseMigration {

    @Override
    protected List<String> getXmlChecksum() {
        return List.of("V001__RoleTable.xml");
    }

    @Override
    protected void doMigrate(NamedParameterJdbcTemplate named) throws Exception {
        updateLiquibase("V001__RoleTable.xml");

        Map<String, String> roles = new LinkedHashMap<>();
        roles.put(Role.NAME_ROOT, Role.DESCRIPTION_ROOT);
        roles.put(Role.NAME_MyProfilePage, Role.DESCRIPTION_MyProfilePage);
        roles.put(Role.NAME_MyClientBrowsePage, Role.DESCRIPTION_MyClientBrowsePage);
        roles.put(Role.NAME_MyClientCreatePage, Role.DESCRIPTION_MyClientCreatePage);
        roles.put(Role.NAME_MyClientModifyPage, Role.DESCRIPTION_MyClientModifyPage);
        roles.put(Role.NAME_RoleBrowsePage, Role.DESCRIPTION_RoleBrowsePage);
        roles.put(Role.NAME_ClientBrowsePage, Role.DESCRIPTION_ClientBrowsePage);
        roles.put(Role.NAME_ClientCreatePage, Role.DESCRIPTION_ClientCreatePage);
        roles.put(Role.NAME_ClientModifyPage, Role.DESCRIPTION_ClientModifyPage);
        roles.put(Role.NAME_SessionBrowsePage, Role.DESCRIPTION_SessionBrowsePage);
        roles.put(Role.NAME_GroupBrowsePage, Role.DESCRIPTION_GroupBrowsePage);
        roles.put(Role.NAME_GroupModifyPage, Role.DESCRIPTION_GroupModifyPage);
        roles.put(Role.NAME_UserBrowsePage, Role.DESCRIPTION_UserBrowsePage);
        roles.put(Role.NAME_UserModifyPage, Role.DESCRIPTION_UserModifyPage);
        roles.put(Role.NAME_UserSwitchPage, Role.DESCRIPTION_UserSwitchPage);
        roles.put(Role.NAME_UserExitPage, Role.DESCRIPTION_UserExitPage);

        String insert = "INSERT INTO tbl_role(name, description, enabled) VALUES(:name, :description, true)";
        for (Map.Entry<String, String> role : roles.entrySet()) {
            Map<String, Object> params = new HashMap<>();
            params.put("name", role.getKey());
            params.put("description", role.getValue());
            named.update(insert, params);
        }
    }

}