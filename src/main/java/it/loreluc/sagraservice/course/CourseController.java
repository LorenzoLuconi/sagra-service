package it.loreluc.sagraservice.course;

import it.loreluc.sagraservice.course.resource.CourseMapper;
import it.loreluc.sagraservice.course.resource.CourseRequest;
import it.loreluc.sagraservice.course.resource.CourseResource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseMapper courseMapper;
    private final CourseService courseService;

    @GetMapping("/{id}")
    public CourseResource findOne(@PathVariable("id") Long id) {
        return courseMapper.toResource(courseService.findById(id));
    }

    @GetMapping
    public List<CourseResource> search(@RequestParam(required = false) String name) {
        return courseService.search(name).stream().map(courseMapper::toResource).collect(Collectors.toList());
    }

    @PostMapping
    public CourseResource create(@RequestBody @Valid CourseRequest courseRequest) {
        return courseMapper.toResource(courseService.create(courseRequest.getName()));
    }

    @PutMapping("/{id}")
    public CourseResource update(@PathVariable("id") Long id, @RequestBody @Valid CourseRequest courseRequest) {
        return courseMapper.toResource(courseService.update(id, courseRequest.getName()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        courseService.delete(id);
    }
}
