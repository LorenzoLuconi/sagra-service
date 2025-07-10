package it.loreluc.sagraservice.course.resource;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CourseRequest {
    @NotEmpty
    @Length(max = 32)
    private String name;
}
