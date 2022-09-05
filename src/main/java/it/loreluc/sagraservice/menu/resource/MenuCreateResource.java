package it.loreluc.sagraservice.menu.resource;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
public class MenuCreateResource {
    @NotEmpty
    @Length(max = 32)
    private String nome;
}
