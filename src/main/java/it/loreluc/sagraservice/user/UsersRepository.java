package it.loreluc.sagraservice.user;

import it.loreluc.sagraservice.jpa.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsersRepository extends CrudRepository<User, String> {

    Optional<User> findByUsername(String username);
}
