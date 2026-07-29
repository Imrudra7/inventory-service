package in.maithilart.inventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import in.maithilart.inventory.security.GatewayVerificationFilter;
import in.maithilart.inventory.security.InternalSecurityFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	private final GatewayVerificationFilter gatewayVerificationFilter;
	private final InternalSecurityFilter internalSecurityFilter;

	
	public SecurityConfig(GatewayVerificationFilter gatewayVerificationFilter,
			InternalSecurityFilter internalSecurityFilter) {
		this.gatewayVerificationFilter=gatewayVerificationFilter;
		this.internalSecurityFilter=internalSecurityFilter;
	}

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // ❌ CSRF not needed for stateless REST
            .csrf(csrf -> csrf.disable())

            // ❌ No sessions
            .sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // ❌ Disable default login mechanisms
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
         // 1. Sabse pehle Secret Filter lagao
            .addFilterBefore(internalSecurityFilter, UsernamePasswordAuthenticationFilter.class)
            
            // 2. Secret Filter ke TURANT BAAD Verification Filter lagao
            .addFilterAfter(gatewayVerificationFilter, InternalSecurityFilter.class)
            // ✅ Authorization rules
            .authorizeHttpRequests(auth -> auth              
            		
            		 .requestMatchers("/inventory/api/internal/**").permitAll() 
            		 .requestMatchers("/actuator/**").permitAll()
            		 .requestMatchers("/inventory/api/admin/**").hasRole("ADMIN")
            		 .requestMatchers("/inventory/api/**").hasRole("USER")            		
            		 .requestMatchers("/inventory/api/public/**").permitAll() // gateway filter handles security
                     .anyRequest().denyAll()   // 🔐 VERY IMPORTANT
            );
        return http.build();
    }
}
