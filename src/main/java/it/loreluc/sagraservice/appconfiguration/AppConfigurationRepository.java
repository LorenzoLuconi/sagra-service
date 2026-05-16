package it.loreluc.sagraservice.appconfiguration;

import it.loreluc.sagraservice.jpa.AppConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppConfigurationRepository extends JpaRepository<AppConfiguration, Long> {
    List<AppConfiguration> findAllByOrderByGroupNameAscKeyAsc();

    List<AppConfiguration> findByGroupNameOrderByKeyAsc(String groupName);

    Optional<AppConfiguration> findByGroupNameAndKey(String groupName, String key);
}
