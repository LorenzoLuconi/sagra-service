package it.loreluc.sagraservice.menu.resource;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class MenuRequest {
    @NotEmpty
    @Length(max = 32)
    private String name;
}
