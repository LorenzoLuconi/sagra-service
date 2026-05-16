package it.loreluc.sagraservice.appconfiguration;

import it.loreluc.sagraservice.appconfiguration.resource.AppConfigurationGroupResource;
import it.loreluc.sagraservice.appconfiguration.resource.AppConfigurationValueResource;
import it.loreluc.sagraservice.jpa.AppConfiguration;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AppConfigurationMapper {
    public AppConfigurationValueResource toValueResource(AppConfiguration appConfiguration) {
        final AppConfigurationDefinition definition = AppConfigurationDefinition.findByGroupAndKey(appConfiguration.getGroupName(), appConfiguration.getKey())
                .orElseThrow(() -> new IllegalStateException("Configurazione non registrata: " + appConfiguration.getGroupName() + "." + appConfiguration.getKey()));
        final AppConfigurationValueResource resource = new AppConfigurationValueResource();
        resource.setKey(appConfiguration.getKey());
        resource.setValue(appConfiguration.getValue());
        resource.setType(definition.getType());
        resource.setAllowedValues(definition.getAllowedValues());
        return resource;
    }

    public List<AppConfigurationGroupResource> toGroupResources(Collection<AppConfiguration> appConfigurations) {
        return appConfigurations.stream()
                .collect(Collectors.groupingBy(AppConfiguration::getGroupName, Collectors.toList()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> toGroupResource(entry.getKey(), entry.getValue()))
                .toList();
    }

    public AppConfigurationGroupResource toGroupResource(String groupName, Collection<AppConfiguration> appConfigurations) {
        final AppConfigurationGroupResource resource = new AppConfigurationGroupResource();
        resource.setGroup(groupName);
        resource.setKeys(appConfigurations.stream()
                .map(this::toValueResource)
                .toList());
        return resource;
    }
}
