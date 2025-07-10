package it.loreluc.sagraservice.department.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "Department", description = "Reparto")
public class DepartmentResource {
    private Long id;
    private String name;
}
