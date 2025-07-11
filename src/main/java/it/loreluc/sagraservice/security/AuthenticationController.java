package it.loreluc.sagraservice.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody AuthRequest loginRequest, HttpServletRequest request){

        final Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getUsername(), loginRequest.getPassword());
        final Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

        SecurityContextHolder.getContext().setAuthentication(authenticationResponse);

        final HttpSession session = request.getSession();
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        return Map.of("message", "Utente autenticato con successo");
    }

    @PostMapping("/logout")
    public Map<String, String> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        logoutHandler.logout(request, response, authentication);

        request.getSession().removeAttribute("SPRING_SECURITY_CONTEXT");
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();

        return Map.of("message", "Logout effettuato con successo");
    }
}