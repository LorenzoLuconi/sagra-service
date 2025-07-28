package it.loreluc.sagraservice.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.core.annotations.ParameterObject;

@Data
@ParameterObject
public class ProductSearchRequest {

    private Long courseId;
    private Long departmentId;
    private boolean excludeLinked;

    @Schema(description = "Ricerca con operatore 'contains'")
    private String name;
}
