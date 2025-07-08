package it.loreluc.sagraservice.department.resource;

import it.loreluc.sagraservice.jpa.Department;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    DepartmentResource toResource(Department department);
}
