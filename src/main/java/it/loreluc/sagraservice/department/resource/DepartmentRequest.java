package it.loreluc.sagraservice.department.resource;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class DepartmentRequest {

    @NotEmpty
    @Length(max = 32)
    private final String name;
}
