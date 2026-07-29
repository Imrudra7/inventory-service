package in.maithilart.inventory.security;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import in.maithilart.common.security.MaithilPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class GatewayVerificationFilter extends OncePerRequestFilter {

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();

		// Infra / swagger ko skip
		return path.startsWith("/swagger") || path.startsWith("/v3/api-docs") || path.startsWith("/actuator")
				|| path.startsWith("/inventory/api/public/") || path.startsWith("/inventory/api/internal/");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String gatewayAuth = request.getHeader("X-Gateway-Auth");
		if (!"verified".equals(gatewayAuth)) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;
		}

		// 3. Role aur User Data Extraction (Identity Logic)
		String userId = request.getHeader("X-User-Id");
		String rolesHeader = request.getHeader("X-Roles");
		String userEmail = request.getHeader("X-User-Email");
		String fullName = request.getHeader("X-User-Full-Name");
		MaithilPrincipal principal = new MaithilPrincipal(userId, userEmail, fullName);
		if (userId != null && rolesHeader != null) {

			String[] roles = rolesHeader.split(",");

			List<SimpleGrantedAuthority> authorities = new ArrayList<>();

			for (String role : roles) {
				String formattedRole = "ROLE_" + role.trim().toUpperCase();
				authorities.add(new SimpleGrantedAuthority(formattedRole));
			}

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal,
					userEmail, authorities);

			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		try {
			filterChain.doFilter(request, response);
		} catch (java.io.IOException | ServletException e) {
			e.printStackTrace();
		}
	}
}


