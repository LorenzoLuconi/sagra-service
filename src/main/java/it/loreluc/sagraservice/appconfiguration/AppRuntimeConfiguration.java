package it.loreluc.sagraservice.appconfiguration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AppRuntimeConfiguration {
    private final AppConfigurationService appConfigurationService;

    public boolean isOrderNameMandatory() {
        return booleanValue(AppConfigurationDefinition.ORDER_NAME_MANDATORY);
    }

    public boolean isOrderTakeAwayEnabled() {
        return booleanValue(AppConfigurationDefinition.ORDER_TAKE_AWAY_ENABLED);
    }

    public boolean isOrderServiceEnabled() {
        return booleanValue(AppConfigurationDefinition.ORDER_SERVICE_ENABLED);
    }

    public BigDecimal orderServiceCost() {
        return new BigDecimal(value(AppConfigurationDefinition.ORDER_SERVICE_COST));
    }

    private boolean booleanValue(AppConfigurationDefinition definition) {
        return Boolean.parseBoolean(value(definition));
    }

    private String value(AppConfigurationDefinition definition) {
        return appConfigurationService.findByGroupAndKey(definition.getGroup(), definition.getKey()).getValue();
    }
}
