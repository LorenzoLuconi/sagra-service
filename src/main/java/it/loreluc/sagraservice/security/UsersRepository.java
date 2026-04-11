package it.loreluc.sagraservice.security;

import it.loreluc.sagraservice.jpa.Role;
import it.loreluc.sagraservice.jpa.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends CrudRepository<User, String> {

    Optional<User> findByUsername(String username);

    List<User> findAllByOrderByUsernameAsc();

    long countByRole(Role role);
}
