package it.loreluc.sagraservice.security;

import it.loreluc.sagraservice.jpa.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Transactional
    public User createUser(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        final Optional<User> optionalUtente = usersRepository.findByUsername(username);

        if ( optionalUtente.isPresent() ) {
            // TODO secgliere altra eccezione
            throw new RuntimeException("Utente già esistente: " +username);
        }

        final User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        return usersRepository.save(user);
    }

    @Transactional
    public void updatePassword(String username, String password) {
        final User user = usersRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));

        if ( passwordEncoder.matches(password, user.getPassword()) ) {
            // TODO rivedere eccezione
            throw new RuntimeException("Password identica alla precedente per " + username);
        }

        user.setPassword(passwordEncoder.encode(password));
    }
}
