package it.loreluc.sagraservice.appconfiguration;

import it.loreluc.sagraservice.appconfiguration.resource.AppConfigurationGroupValuesUpdateRequest;
import it.loreluc.sagraservice.appconfiguration.resource.AppConfigurationUpdateRequest;
import it.loreluc.sagraservice.appconfiguration.resource.AppConfigurationValueUpdateRequest;
import it.loreluc.sagraservice.error.SagraBadRequestException;
import it.loreluc.sagraservice.error.SagraNotFoundException;
import it.loreluc.sagraservice.jpa.AppConfiguration;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppConfigurationService {
    private final AppConfigurationRepository appConfigurationRepository;

    @Transactional(rollbackOn = Throwable.class)
    public List<AppConfiguration> findAll() {
        return appConfigurationRepository.findAllByOrderByGroupNameAscKeyAsc()
                .stream()
                .filter(this::isRegistered)
                .toList();
    }

    @Transactional(rollbackOn = Throwable.class)
    public List<AppConfiguration> findByGroup(String groupName) {
        requireGroup(groupName);
        return appConfigurationRepository.findByGroupNameOrderByKeyAsc(groupName)
                .stream()
                .filter(this::isRegistered)
                .toList();
    }

    @Transactional(rollbackOn = Throwable.class)
    public AppConfiguration findByGroupAndKey(String groupName, String key) {
        requireDefinition(groupName, key);
        return appConfigurationRepository.findByGroupNameAndKey(groupName, key)
                .orElseThrow(() -> new SagraNotFoundException("Nessuna configurazione trovata per gruppo '" + groupName + "' e chiave '" + key + "'"));
    }

    @Transactional(rollbackOn = Throwable.class)
    public List<AppConfiguration> updateAll(AppConfigurationUpdateRequest request) {
        request.getGroups().forEach(group -> updateGroupValues(group.getGroup(), group.getKeys()));
        return findAll();
    }

    @Transactional(rollbackOn = Throwable.class)
    public List<AppConfiguration> updateGroup(String groupName, AppConfigurationGroupValuesUpdateRequest request) {
        updateGroupValues(groupName, request.getKeys());
        return findByGroup(groupName);
    }

    @Transactional(rollbackOn = Throwable.class)
    public AppConfiguration updateValue(String groupName, String key, String value) {
        final AppConfigurationDefinition definition = requireDefinition(groupName, key);
        validateValue(definition, value);
        final AppConfiguration appConfiguration = findByGroupAndKey(groupName, key);
        appConfiguration.setValue(value);
        return appConfigurationRepository.save(appConfiguration);
    }

    private void updateGroupValues(String groupName, List<AppConfigurationValueUpdateRequest> values) {
        requireGroup(groupName);
        values.forEach(value -> updateValue(groupName, value.getKey(), value.getValue()));
    }

    private void requireGroup(String groupName) {
        if (AppConfigurationDefinition.findByGroup(groupName).isEmpty()) {
            throw new SagraNotFoundException("Nessun gruppo di configurazione trovato con nome: " + groupName);
        }
    }

    private AppConfigurationDefinition requireDefinition(String groupName, String key) {
        return AppConfigurationDefinition.findByGroupAndKey(groupName, key)
                .orElseThrow(() -> new SagraNotFoundException("Nessuna configurazione registrata per gruppo '" + groupName + "' e chiave '" + key + "'"));
    }

    private void validateValue(AppConfigurationDefinition definition, String value) {
        if (!definition.isValidValue(value)) {
            throw new SagraBadRequestException("Valore non valido per la configurazione '" + definition.getGroup() + "." + definition.getKey() + "'");
        }
    }

    private boolean isRegistered(AppConfiguration appConfiguration) {
        return AppConfigurationDefinition.findByGroupAndKey(appConfiguration.getGroupName(), appConfiguration.getKey()).isPresent();
    }
}
