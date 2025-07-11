package it.loreluc.sagraservice.course;

import it.loreluc.sagraservice.error.SagraConflictException;
import it.loreluc.sagraservice.error.SagraNotFoundException;
import it.loreluc.sagraservice.jpa.Course;
import it.loreluc.sagraservice.product.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    private final ProductRepository productRepository;

    public Course findById(Long id) {
        return courseRepository.findById(Objects.requireNonNull(id)).orElseThrow(() -> new SagraNotFoundException("Nessuna portata trovato con id: " + id));
    }

    @Transactional(rollbackOn = Throwable.class)
    public Course create(String nomeportata) {

        if ( courseRepository.existsByNameIgnoreCase(nomeportata) ) {
            throw new SagraConflictException(String.format("Portata con il nome '%s' già esistente", nomeportata));
        }

        final Course course = new Course();
        course.setName(nomeportata);

        return courseRepository.save(course);
    }

    @Transactional(rollbackOn = Throwable.class)
    public void delete(Long id) {
        final Course course = findById(id);

        if ( productRepository.existsByCourse(course) ) {
            throw new SagraConflictException(String.format("Impossibile cancellare la portata '%s' in quanto è referenziata in alcuni prodotti", course.getName()));
        }

        courseRepository.delete(course);
    }

    @Transactional(rollbackOn = Throwable.class)
    public Course update(Long portataId, String courseName) {
        final Course course = findById(portataId);

        if ( courseRepository.existsByNameIgnoreCaseAndIdNot(courseName, portataId) ) {
            throw new SagraConflictException(String.format("Portata con il nome '%s' già esistente", courseName));
        }

        course.setName(courseName);

        return courseRepository.save(course);
    }



    public List<Course> search(String nome) {
        if ( nome == null || nome.isEmpty() ) {
            return courseRepository.findAll();
        }

        return courseRepository.findAllByNameContains(nome);
    }


 }
