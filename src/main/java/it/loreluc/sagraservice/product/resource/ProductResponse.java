package it.loreluc.sagraservice.product.resource;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(name="Product")
public class ProductResponse {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    private String note;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Long departmentId;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Long courseId;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean sellLocked;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer initialQuantity;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer availableQuantity;
    private Long parentId;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastUpdate;
}
