package ru.nirs.security;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Optional;

/**
 * Роль в системе
 *
 * @author Nikita Gushcha
 */
public enum UserRole {

    INTEGRATION_ADMIN("Администратор интеграций"),
    AUDITOR_IS("Аудитор безопасности"),
    DATA_ADMIN("Администратор данных"),
    LIQUIDITY_POSITIONER("Позиционер(Ликвидность)"),
    OVP_POSITIONER("Позиционер(ОВП)"),
    SUPERVISOR("Супервизор"),
    INITIATOR_OF_RESERVATION("Инициатор брони"),
    FTP_ANALYST("Аналитик FTP"),
    API_STORE("Доступность API-методов модуля STORE"),
    API_LMS("Доступность API-методов модуля LMS"),
    API_FTP("Доступность API-методов модуля FTP"),
    API_OCP("Доступность API-методов модуля OCP"),
    API_CFT("Доступность API-методов модуля CFT"),
    API_FC("Доступность API-методов модуля FC"),
    API_QORT("Доступность API-методов модуля QORT"),
    FC_CLIENT_MANAGER("Клиентский менеджер"),
    FC_CLIENT_MANAGER_DEPOSIT("КМ Депозиты"),
    FC_CLIENT_MANAGER_CREDIT("КМ Кредиты");

    @Schema(description = "Наименование роли")
    private final String name;

    UserRole(String name) {
        this.name = name;
    }

    public static Optional<UserRole> getByName(String searchRole) {
        for (UserRole userRole : values()) {
            if (userRole.name().equalsIgnoreCase(searchRole)) {
                return Optional.of(userRole);
            }
        }
        return Optional.empty();
    }

}

