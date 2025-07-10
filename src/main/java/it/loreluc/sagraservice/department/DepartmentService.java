package it.loreluc.sagraservice.department;

import it.loreluc.sagraservice.error.SagraConflictException;
import it.loreluc.sagraservice.error.SagraNotFoundException;
import it.loreluc.sagraservice.jpa.Department;
import it.loreluc.sagraservice.product.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final ProductRepository productRepository;

    public Department findById(Long departmentId ) {
        return departmentRepository.findById(Objects.requireNonNull(departmentId)).orElseThrow(() -> new SagraNotFoundException("Nessun reparto trovato con id: " + departmentId));
    }

    @Transactional(rollbackOn = Throwable.class)
    public Department create(String departmentName) {

        if ( departmentRepository.existsByNameContainingIgnoreCase(departmentName)) {
            throw new SagraConflictException(String.format("Reparto con il nome '%s' già esistente", departmentName));
        }

        final Department department = new Department();
        department.setName(departmentName);

        return departmentRepository.save(department);
    }

    @Transactional(rollbackOn = Throwable.class)
    public void delete(Long departmentId) {
        final Department department = findById(departmentId);

        if (productRepository.existsByDepartment(department)) {
            throw new SagraConflictException(String.format("Impossibile cancellare il reparto '%s' in quanto è referenziato in alcuni prodotti", department.getName()));
        }

        departmentRepository.delete(department);
    }

    @Transactional(rollbackOn = Throwable.class)
    public Department update(Long departmentId, String departmentName) {
        final Department department = findById(departmentId);

        if ( departmentRepository.existsByNameContainingIgnoreCaseAndIdNot(departmentName, departmentId) ) {
            throw new SagraConflictException(String.format("Reparto con il nome '%s' già esistente", departmentName));
        }

        department.setName(departmentName);

        return departmentRepository.save(department);
    }

    public List<Department> search(String nome) {

        if ( nome == null || nome.isEmpty() ) {
            return departmentRepository.findAll();
        }

        return departmentRepository.findByNameContaining(nome);
    }
}
