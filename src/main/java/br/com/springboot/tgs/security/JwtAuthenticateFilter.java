package br.com.springboot.tgs.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.springboot.tgs.data.UserDetailData;
import br.com.springboot.tgs.entities.Dentist;

public class JwtAuthenticateFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private static final int TOKEN_EXPIRATION = 600_000;
    public static final String TOKEN_PASSWORD = "191782f8-9c38-459f-b8a8-262f1e800bff";

    public JwtAuthenticateFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            Dentist dentist = new ObjectMapper().readValue(request.getInputStream(), Dentist.class);

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dentist.getCro(),
                    dentist.getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException("Falha ao autenticar o dentista", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        UserDetailData userDetailData = (UserDetailData) authResult.getPrincipal();

        String token = JWT.create().withSubject(userDetailData.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                .sign(Algorithm.HMAC512(TOKEN_PASSWORD));

        response.getWriter().write(token);
        response.getWriter().flush();
    }
}
