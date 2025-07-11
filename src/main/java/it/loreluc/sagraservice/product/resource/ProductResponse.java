package it.loreluc.sagraservice.product.resource;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(name="Product")
public class ProductResponse {

    private Long id;
    private String name;
    private String note;
    private Long departmentId;
    private Long courseId;
    private BigDecimal price;
    private boolean sellLocked;
    private Integer quantity;
    private Long parentId;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastUpdate;
}
