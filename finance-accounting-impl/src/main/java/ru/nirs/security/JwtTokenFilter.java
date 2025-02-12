package ru.nirs.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Фильтр, который используется для заполнения SpringSecurityContext на основе данных из токена
 */
@Slf4j
@RequiredArgsConstructor
@WebFilter(filterName = "JwtTokenFilter", urlPatterns = {"/**"})
public class JwtTokenFilter extends OncePerRequestFilter {

    /**
     * claim в котором указывается информация о пользовательских ролях
     */
    public static final String REALM_ACCESS_CLAIM = "realm_access";
    /**
     * claim в котором указывается имя пользователя
     */
    public static final String PREFERRED_USERNAME_CLAIM = "preferred_username";

    public static final String ROLES = "roles";

    @Value("#{'${service.security.unprotected:/swagger-ui/**,/v3/api-docs/**,/actuator/**}'.split(',')}")
    private List<String> unprotectedUrlPatterns;
    private final PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        processRequest(request);
        chain.doFilter(request, response);
    }

    private void processRequest(HttpServletRequest request) {
        if (!isUnprotectedUri(request.getRequestURI())) {
            List<String> bearerTokens = getBearerAuthorizationHeaderValues(request);
            if (!bearerTokens.isEmpty()) {
                String jwtToken = bearerTokens.get(0).replace("Bearer ", "");
                setPermissionsToSecurityContext(jwtToken);
            }
        }
    }

    /**
     * Достаем из токена список ролей пользователя и те, которые относятся к системе превращаем в полномочия и кладем
     * их в SecurityContext
     *
     * @param jwtToken - токен
     */
    @SuppressWarnings("unchecked")
    private static void setPermissionsToSecurityContext(String jwtToken) {
        if (jwtToken != null) {

            DecodedJWT jwt = JWT.decode(jwtToken);
            Map<String, Claim> claimMap = jwt.getClaims();

            //вычисление информации по ролям и полномочиям пользователя
            List<String> roles = (List<String>) claimMap.get(REALM_ACCESS_CLAIM).asMap()
                    .getOrDefault(ROLES, Collections.emptyList());
            List<UserRole> userRoles = new ArrayList<>();
            for (String roleInToken : roles) {
                UserRole.getByName(roleInToken).ifPresent(userRoles::add);
            }
            List<UserPermission> userPermissions = RoleBasedPermissions.getPermissionsByRoles(userRoles);

            //превращаем полномочия пользователя  в SimpleGrantedAuthority
            Set<SimpleGrantedAuthority> permissions =
                    userPermissions.stream()
                    .map(userPermission -> new SimpleGrantedAuthority(userPermission.name()))
                    .collect(Collectors.toSet());

            log.info("Permissions from userInfo: {} will be set to security context", permissions);
            JwtTokenAuthentication jwtTokenAuthentication = new JwtTokenAuthentication(
                    jwtToken,
                    claimMap.get(PREFERRED_USERNAME_CLAIM).asString(),
                    permissions,
                    true);
            SecurityContextHolder.getContext().setAuthentication(jwtTokenAuthentication);
        }
    }

    private boolean isUnprotectedUri(String currentUrl) {
        boolean result = false;
        for (String unprotectedUri : unprotectedUrlPatterns) {
            if (StringUtils.isNotBlank(currentUrl) &&
                    pathMatcher.match(unprotectedUri, currentUrl)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private List<String> getBearerAuthorizationHeaderValues(HttpServletRequest httpServletRequest) {
        Enumeration<String> authorizationHeaders = httpServletRequest.getHeaders("Authorization");
        List<String> bearerAuthorizationHeaders = new ArrayList<>();
        if (authorizationHeaders != null) {
            while (authorizationHeaders.hasMoreElements()) {
                String authorizationHeader = authorizationHeaders.nextElement();
                if (this.isValidAuthorizationHeader(authorizationHeader)) {
                    bearerAuthorizationHeaders.add(authorizationHeader);
                }
            }
        }

        return bearerAuthorizationHeaders;
    }

    private boolean isValidAuthorizationHeader(String authorizationHeaderValue) {
        return !StringUtils.isBlank(authorizationHeaderValue) && authorizationHeaderValue.startsWith("Bearer ");
    }

}
