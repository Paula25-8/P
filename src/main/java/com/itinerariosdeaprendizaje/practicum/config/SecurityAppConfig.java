package com.itinerariosdeaprendizaje.practicum.config;

import com.itinerariosdeaprendizaje.practicum.utils.TipoPerfil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

@Configuration
//@RequiredArgsConstructor
@EnableWebSecurity
//@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
//@EnableConfigurationProperties(LdapProperties.class)
//@EnableGlobalMethodSecurity(prePostEnabled = true)  // IMPORTANTE si queremos usar anotaciones @PreAuthorize
public class SecurityAppConfig {

    //Configuramos bean con propiedades de ldap
    /*@Autowired
    private LdapProperties ldapProperties;*/

    /* Metodo que indica que filtro es usado para cada peticion. Puede haber varios 'SecurityFilterChain' que activamos segun los parametros que indiquemos */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.headers().frameOptions().sameOrigin();
        http.authorizeRequests()
             .requestMatchers("/css/**").permitAll()
             .requestMatchers("/images/**").permitAll()
             .requestMatchers("/js/**").permitAll()
             .requestMatchers("/preguntas").permitAll()  //Apartado de preguntas sobre información general
             .requestMatchers("/lineasAprendizaje/**").permitAll() //Apartado de infor sobre líneas y estaciones de aprendizaje
             .requestMatchers("/infoEstacion/**").permitAll() //Apartado de infor sobre líneas y estaciones de aprendizaje
             //.requestMatchers("/actuator/**").hasIpAddress("10.254.0.252")
             .requestMatchers("/itinerariosPropios/**").hasAuthority(TipoPerfil.ROL_ESTUDIANTE.name())
             .requestMatchers("/itinerariosAlumnado/**").hasAnyAuthority(TipoPerfil.ROL_TUTOR_UR.name(), TipoPerfil.ROL_COORDINADOR.name())
             .requestMatchers("/dossierFinal/**").hasAnyAuthority(TipoPerfil.ROL_ESTUDIANTE.name())
             .requestMatchers("/dossierAlumnado/**").hasAnyAuthority(TipoPerfil.ROL_TUTOR_UR.name(), TipoPerfil.ROL_TUTOR_CENTRO.name())
             .requestMatchers("/**").hasAnyAuthority(TipoPerfil.ROL_ESTUDIANTE.name(), TipoPerfil.ROL_TUTOR_UR.name(), TipoPerfil.ROL_TUTOR_CENTRO.name(), TipoPerfil.ROL_COORDINADOR.name())
             .anyRequest().hasAnyAuthority(TipoPerfil.ROL_ESTUDIANTE.name(), TipoPerfil.ROL_TUTOR_UR.name(), TipoPerfil.ROL_TUTOR_CENTRO.name())
             .and()
             .formLogin()
             .loginPage("/login")
             .permitAll()
             .and()
             .logout()
             .permitAll()
             .and()
             .exceptionHandling().accessDeniedPage("/login?logout&accessdenied") //.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // convertimos el POST en GET (mala práctica)
             //.logoutSuccessUrl("/login?logout")
             //.and()
             //.csrf().disable() // CSRF protection is enabled by default with Java configuration
        ;

        return http.build();
    }

    /*
     *
     * Para autenticar contra AD usando un provider de ldap
     *
     * @param proyectobaseAuthoritiesPopulator
     * @return
     * @throws java.io.IOException
     *
     */
   // Este metodo lo necesitaremos a la hora de hacer la autenticación contra el Active Directory
    /*
    @Bean
    public LdapAuthenticationProvider LdapAuthenticationProvider(LdapAuthoritiesPopulator practicumAuthoritiesPopulator) throws IOException { // Autenticación
        File jks = File.createTempFile("cacerts", "jks");
        jks.deleteOnExit();

        try (InputStream fromJks = SecurityAppConfig.class.getResource("/cacerts.jks").openStream()) {
            FileCopyUtils.copy(FileCopyUtils.copyToByteArray(fromJks), jks);
        }

        System.setProperty("javax.net.ssl.trustStore", jks.getPath());
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        // Explicamos como es la estructura de datos de LDAP
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrls(ldapProperties.urls);
        contextSource.setUserDn(ldapProperties.username);
        contextSource.setPassword(ldapProperties.password);
        contextSource.setReferral(ldapProperties.referral);
        contextSource.afterPropertiesSet();

        LdapUserSearch ldapUserSearch = new FilterBasedLdapUserSearch(ldapProperties.searchbase, ldapProperties.searchfilter, contextSource);
        BindAuthenticator bindAuthenticator = new BindAuthenticator(contextSource);
        bindAuthenticator.setUserSearch(ldapUserSearch);

        return new LdapAuthenticationProvider(bindAuthenticator, practicumAuthoritiesPopulator);
    }

    @Bean
    LdapAuthoritiesPopulator practicumAuthoritiesPopulator(UsuarioRepository usuarioRepository) {
        return (userData, username) -> {
            List<GrantedAuthority> authorities = new ArrayList<>();

            Optional<Usuario> usuario = usuarioRepository.findByLogin(username.toLowerCase());
            if (usuario.isPresent()) {
                if(usuario.get().getRoles().size()==1) {
                    authorities.add(new SimpleGrantedAuthority(usuario.get().getRoles().get(0).getRol().name()));
                }
                if(usuario.get().getRoles().size()==2){
                    authorities.add(new SimpleGrantedAuthority(usuario.get().getRoles().get(0).getRol().name()));
                    authorities.add(new SimpleGrantedAuthority(usuario.get().getRoles().get(1).getRol().name()));
                }
            }
            return authorities;
        };
    }
    */

    /*
     * Habilitamos la auditoría
     *
     * @return
     */
    /*@Bean
    AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
    */

    // Metodo para trabajar con usuarios de prueba
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication()
                .withUser("patoledo").password("password").authorities("ROL_ESTUDIANTE")
                .and()
                .withUser("martlo").password("martlo").authorities("ROL_TUTOR_UR")
                .and()
                .withUser("rosmoreno").password("rosmoreno").authorities("ROL_ESTUDIANTE")
                .and()
                .withUser("matio").password("matio").authorities("ROL_TUTOR_CENTRO")
                .and()
                .withUser("cordi").password("cordi").authorities("ROL_COORDINADOR");
        //passwordEncoder().encode("password")
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}