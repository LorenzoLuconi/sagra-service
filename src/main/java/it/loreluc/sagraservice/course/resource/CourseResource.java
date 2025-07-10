package it.loreluc.sagraservice.course.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Portata", name = "Course")
public class CourseResource {
    private Long id;
    private String name;
}
