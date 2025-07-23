package it.loreluc.sagraservice.order.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "Count")
public class CountResponse {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private final Long count;
}
