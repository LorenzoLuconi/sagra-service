package it.loreluc.sagraservice.department;

import it.loreluc.sagraservice.department.resource.DepartmentMapper;
import it.loreluc.sagraservice.department.resource.DepartmentRequest;
import it.loreluc.sagraservice.department.resource.DepartmentResource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentMapper departmentMapper;
    private final DepartmentService departmentService;

    @GetMapping("/{departmentId}")
    public DepartmentResource findOne(@PathVariable("departmentId") Long id) {
        return departmentMapper.toResource(departmentService.findById(id));
    }

    @PutMapping("/{departmentId}")
    public DepartmentResource update(@PathVariable("departmentId") Long id, @RequestBody @Valid DepartmentRequest departmentRequest) {
        return departmentMapper.toResource(departmentService.update(id, departmentRequest.getName()));
    }

    @GetMapping
    public List<DepartmentResource> search(@RequestParam(required = false) String name) {
        return departmentService.search(name).stream().map(departmentMapper::toResource).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentResource create(@RequestBody @Valid DepartmentRequest departmentRequest) {
        return departmentMapper.toResource(departmentService.create(departmentRequest.getName()));
    }

    @DeleteMapping("/{departmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long departmentId) {
        departmentService.delete(departmentId);
    }
}
