package ru.nirs.security;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Маппинг между ролями и полномочиями
 *
 * @author Nikita Gushcha
 */
@UtilityClass
public final class RoleBasedPermissions {

    /** Маппинг полномочий на роли пользователей */
    private static final Map<UserRole, Set<UserPermission>> ROLE_BASED_PERMISSIONS_MAP = new HashMap<>();

    static {
        ROLE_BASED_PERMISSIONS_MAP.put(UserRole.INTEGRATION_ADMIN,
                Set.of(UserPermission.PLA_VIEW, UserPermission.INT_VIEW, UserPermission.INT_EDIT));

        ROLE_BASED_PERMISSIONS_MAP.put(UserRole.AUDITOR_IS,
                Set.of(UserPermission.PLA_VIEW, UserPermission.AUDIT_VIEW));

        ROLE_BASED_PERMISSIONS_MAP.put(UserRole.DATA_ADMIN,
                Set.of(UserPermission.PLA_VIEW, UserPermission.DICTS_VIEW, UserPermission.DICTS_EDIT));

        ROLE_BASED_PERMISSIONS_MAP.put(UserRole.LIQUIDITY_POSITIONER,
                Set.of(UserPermission.PLA_VIEW, UserPermission.LIQ_VIEW, UserPermission.LIQ_EDIT, UserPermission.RES_VIEW, UserPermission.RES_TRE_EDIT));

        ROLE_BASED_PERMISSIONS_MAP.put(UserRole.OVP_POSITIONER, Set.of(UserPermission.PLA_VIEW, UserPermission.DICTS_VIEW, UserPermission.LIQ_VIEW,
                UserPermission.OCP_VIEW, UserPermission.OCP_EDIT));

        ROLE_BASED_PERMISSIONS_MAP.put(UserRole.SUPERVISOR, Set.of(UserPermission.PLA_VIEW, UserPermission.DICTS_VIEW, UserPermission.INT_VIEW,
                UserPermission.AUDIT_VIEW, UserPermission.LIQ_VIEW, UserPermission.OCP_VIEW, UserPermission.RES_VIEW, UserPermission.FTP_VIEW));

        ROLE_BASED_PERMISSIONS_MAP.put(UserRole.INITIATOR_OF_RESERVATION, Set.of(UserPermission.PLA_VIEW, UserPermission.RES_VIEW, UserPermission.RES_BIS_EDIT));

        ROLE_BASED_PERMISSIONS_MAP.put(UserRole.FTP_ANALYST, Set.of(UserPermission.FTP_VIEW, UserPermission.FTP_EDIT));
        ROLE_BASED_PERMISSIONS_MAP.put(UserRole.FC_CLIENT_MANAGER, Set.of(UserPermission.FC_VIEW, UserPermission.DICTS_VIEW));
        ROLE_BASED_PERMISSIONS_MAP.put(UserRole.FC_CLIENT_MANAGER_DEPOSIT, Set.of(UserPermission.FC_VIEW, UserPermission.FC_DEPOSIT_VIEW, UserPermission.DICTS_VIEW));
        ROLE_BASED_PERMISSIONS_MAP.put(UserRole.FC_CLIENT_MANAGER_CREDIT, Set.of(UserPermission.FC_VIEW, UserPermission.FC_CREDIT_VIEW, UserPermission.DICTS_VIEW));
        ROLE_BASED_PERMISSIONS_MAP.put(UserRole.API_LMS, Set.of(UserPermission.API_LMS_VIEW));
        ROLE_BASED_PERMISSIONS_MAP.put(UserRole.API_STORE, Set.of(UserPermission.API_STORE_VIEW));
        ROLE_BASED_PERMISSIONS_MAP.put(UserRole.API_FC, Set.of(UserPermission.API_FC_VIEW));
        ROLE_BASED_PERMISSIONS_MAP.put(UserRole.API_CFT, Set.of(UserPermission.API_CFT_VIEW));
        ROLE_BASED_PERMISSIONS_MAP.put(UserRole.API_FTP, Set.of(UserPermission.API_FTP_VIEW));
        ROLE_BASED_PERMISSIONS_MAP.put(UserRole.API_OCP, Set.of(UserPermission.API_OCP_VIEW));
    }

    public static List<UserPermission> getPermissionsByRoles(List<UserRole> roles) {
        Set<UserPermission> permissionsSet = new HashSet<>();
        for (UserRole role : roles) {
            permissionsSet.addAll(ROLE_BASED_PERMISSIONS_MAP.get(role));
        }
        return new ArrayList<>(permissionsSet);
    }

    public static Map<UserRole, Set<UserPermission>> getRoleBasedPermissionsMap() {
        return ROLE_BASED_PERMISSIONS_MAP;
    }

}
