package it.loreluc.sagraservice.security;

import it.loreluc.sagraservice.config.SagraSettings;
import it.loreluc.sagraservice.error.InvalidValue;
import it.loreluc.sagraservice.error.SagraBadRequestException;
import it.loreluc.sagraservice.error.SagraConflictException;
import it.loreluc.sagraservice.error.SagraNotFoundException;
import it.loreluc.sagraservice.jpa.Role;
import it.loreluc.sagraservice.jpa.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final SagraSettings sagraSettings;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Transactional
    public User createUser(String username, String name, Role role, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(name);
        Objects.requireNonNull(role);
        Objects.requireNonNull(password);

        if (usersRepository.findByUsername(username).isPresent()) {
            throw new SagraConflictException("Utente già esistente: " + username);
        }

        final User user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(password));

        return usersRepository.save(user);
    }

    @Transactional
    public User updateUser(String username, String name, Role role, String password) {
        final User user = usersRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        Objects.requireNonNull(name);
        Objects.requireNonNull(role);

        ensureAdminNotRemoved(user, role);

        user.setName(name);
        user.setRole(role);

        if (password != null && !password.isBlank()) {
            updatePassword(user, password);
        }

        return user;
    }

    @Transactional
    public void deleteUser(String username, String currentUsername) {
        final User user = findUser(username);

        if (user.getUsername().equals(currentUsername)) {
            throw new SagraConflictException("Non puoi cancellare il tuo utente");
        }

        ensureAdminNotDeleted(user);
        usersRepository.delete(user);
    }

    @Transactional
    public void updatePassword(String username, String password) {
        updatePassword(findUser(username), password);
    }

    @Transactional
    public void changePassword(String username, String currentPassword, String newPassword) {
        final User user = findUser(username);
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new SagraBadRequestException(
                    InvalidValue.builder().field("currentPassword").message("Password corrente non valida").build()
            );
        }

        updatePassword(user, newPassword);
    }

    @Transactional
    public void ensureDefaultAdmin() {
        final String username = sagraSettings.getDefaultAdmin().getUsername();
        if (usersRepository.findByUsername(username).isPresent()) {
            return;
        }

        createUser(
                username,
                sagraSettings.getDefaultAdmin().getName(),
                Role.admin,
                sagraSettings.getDefaultAdmin().getPassword()
        );
    }

    public List<User> findAllUsers() {
        return usersRepository.findAllByOrderByUsernameAsc();
    }

    public User findUser(String username) {
        return usersRepository.findByUsername(username)
                .orElseThrow(() -> new SagraNotFoundException("Utente non trovato: " + username));
    }

    private void updatePassword(User user, String password) {
        Objects.requireNonNull(password);
        if (passwordEncoder.matches(password, user.getPassword())) {
            throw new SagraConflictException("Password identica alla precedente per " + user.getUsername());
        }

        user.setPassword(passwordEncoder.encode(password));
    }

    private void ensureAdminNotRemoved(User user, Role newRole) {
        if (user.getRole() == Role.admin && newRole != Role.admin && usersRepository.countByRole(Role.admin) <= 1) {
            throw new SagraConflictException("Deve essere presente almeno un utente admin");
        }
    }

    private void ensureAdminNotDeleted(User user) {
        if (user.getRole() == Role.admin && usersRepository.countByRole(Role.admin) <= 1) {
            throw new SagraConflictException("Non puoi cancellare l'ultimo admin");
        }
    }
}
