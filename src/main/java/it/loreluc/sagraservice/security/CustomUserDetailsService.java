package it.loreluc.sagraservice.security;

import it.loreluc.sagraservice.jpa.Utente;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UtentiRepository utentiRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return utentiRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Transactional
    public Utente createUser(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        final Optional<Utente> optionalUtente = utentiRepository.findById(username);

        if ( optionalUtente.isPresent() ) {
            // TODO secgliere altra eccezione
            throw new RuntimeException("Utente già esistente: " +username);
        }

        final Utente utente = new Utente();
        utente.setUsername(username);
        utente.setPassword(passwordEncoder.encode(password));

        return utentiRepository.save(utente);
    }

    @Transactional
    public void updatePassword(String username, String password) {
        final Utente utente = utentiRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));

        if ( passwordEncoder.matches(password, utente.getPassword()) ) {
            // TODO rivedere eccezione
            throw new RuntimeException("Password identica alla precedente per " + username);
        }

        utente.setPassword(passwordEncoder.encode(password));
    }
}
