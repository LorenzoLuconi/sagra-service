package it.loreluc.sagraservice.product.resource;

import it.loreluc.sagraservice.course.CourseService;
import it.loreluc.sagraservice.department.DepartmentService;
import it.loreluc.sagraservice.jpa.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CourseService.class, DepartmentService.class})
public interface ProductMapper {
    @Mapping(target = "quantity", source = "productQuantity.quantity")
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "departmentId", source = "department.id")
    ProductResponse toResource(Product product);

    @Mapping(target = "department", source = "departmentId")
    @Mapping(target = "course", source = "courseId")
    @Mapping(target = "sellLocked", ignore = true)
    @Mapping(target = "productQuantity", ignore = true)
    @Mapping(target = "lastUpdate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    Product toEntity(ProductRequest productRequest);

    @Mapping(target = "department", source = "departmentId")
    @Mapping(target = "course", source = "courseId")
    @Mapping(target = "sellLocked", ignore = true)
    @Mapping(target = "productQuantity", ignore = true)
    @Mapping(target = "lastUpdate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    void update(@MappingTarget Product product, ProductRequest productRequest);
}
