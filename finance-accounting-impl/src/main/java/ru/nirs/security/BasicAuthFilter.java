package ru.nirs.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 *  Фильтр, который используется для заполнения SpringSecurityContext на основе данных при Basic аутентификации.
 *  Основная задача данного фильтра заключается не в валидации пользователя, а в заполнении SpringSecurityContext
 *  нужным полномочиями на основе роли, коорая была передана через кастомное поле в заголовке.
 *  Валидация пользователя и присвоение нужной роли происходит на стороне шлюза.
 *
 */
@Slf4j
public class BasicAuthFilter extends OncePerRequestFilter {

    public static final String BASIC_PREFIX = "Basic";
    /**
     * кастомный заголовок в котором будет храниться роль пользователя
     */
    public static final String CUSTOM_HEADER_TO_TRANSFER_USER_ROLE = "ALM-USER-ROLE";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        processRequest(request);
        chain.doFilter(request, response);
    }

    private void processRequest(HttpServletRequest request) {
        if (isBasicAuthRequest(request)) {
            String userName = getUserNameFromRequest(request);
            String userRole = request.getHeader(CUSTOM_HEADER_TO_TRANSFER_USER_ROLE);
            if (userName != null) {
                setPermissionsToSecurityContext(userName, userRole);
            } else {
                log.warn("Basic Auth: Can't get username from Authorization header");
            }
        }
    }

    /**
     * Достаем из токена список ролей пользователя и те, которые относятся к системе превращаем в полномочия и кладем
     * их в SecurityContext
     *
     * @param userName    имя пользователя
     * @param userRoleStr пользовательская роль
     */
    @SuppressWarnings("unchecked")
    private static void setPermissionsToSecurityContext(String userName, String userRoleStr) {
        Set<SimpleGrantedAuthority> userPermissions = new HashSet<>();
        UserRole.getByName(userRoleStr)
                .ifPresent(userRole -> {
                    List<UserPermission> rolePermissions = RoleBasedPermissions.getPermissionsByRoles(List.of(userRole));
                    //превращаем полномочия пользователя  в SimpleGrantedAuthority
                    rolePermissions.stream().forEach(userPermission -> {
                        userPermissions.add(new SimpleGrantedAuthority(userPermission.name()));
                    });
                });
        AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userName, "",
                userPermissions);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Permissions from userInfo: {} will be set to security context", userPermissions);
    }

    public boolean isBasicAuthRequest(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        if (header == null) {
            return false;
        } else {
            header = header.trim();
            return StringUtils.startsWithIgnoreCase(header, BASIC_PREFIX);
        }
    }

    private String getUserNameFromRequest(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        if (header == null) {
            return null;
        } else {
            header = header.trim();
            if (!StringUtils.startsWithIgnoreCase(header, BASIC_PREFIX)) {
                return null;
            } else if (header.equalsIgnoreCase("Basic")) {
                throw new RuntimeException("Empty basic authentication token");
            } else {
                byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
                byte[] decoded = this.decode(base64Token);
                String token = new String(decoded, StandardCharsets.UTF_8);
                int delim = token.indexOf(":");
                if (delim == -1) {
                    throw new RuntimeException("Invalid basic authentication token");
                } else {
                    return token.substring(0, delim);
                }
            }
        }
    }

    private byte[] decode(byte[] base64Token) {
        try {
            return Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException var3) {
            throw new RuntimeException("Failed to decode basic authentication token");
        }
    }

}
