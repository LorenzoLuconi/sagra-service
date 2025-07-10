package it.loreluc.sagraservice.course.resource;

import it.loreluc.sagraservice.jpa.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseResource toResource(Course course);
}
