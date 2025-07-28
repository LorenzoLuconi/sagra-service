package it.loreluc.sagraservice.product;

import it.loreluc.sagraservice.course.CourseRepository;
import it.loreluc.sagraservice.department.DepartmentRepository;
import it.loreluc.sagraservice.error.InvalidValue;
import it.loreluc.sagraservice.error.SagraBadRequestException;
import it.loreluc.sagraservice.jpa.Course;
import it.loreluc.sagraservice.jpa.Department;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductEntityResolver {

    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;

    public Department getDepartment (Long departmentId) {
        return departmentRepository.findById(departmentId).orElseThrow(() -> new SagraBadRequestException(InvalidValue.builder()
                .field("departmentId")
                .value(departmentId)
                .message("Reparto non trovato")
                .build()
        ));
    }

    public Course getCourse (Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(() -> new SagraBadRequestException(InvalidValue.builder()
                .field("courseId")
                .value(courseId)
                .message("Tipo di portata non trovata")
                .build()
        ));
    }
}
