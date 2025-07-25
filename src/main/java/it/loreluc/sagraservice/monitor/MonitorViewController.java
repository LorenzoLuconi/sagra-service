package it.loreluc.sagraservice.monitor;

import it.loreluc.sagraservice.jpa.Monitor;
import it.loreluc.sagraservice.monitor.resource.MonitorView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
@RequestMapping("/monitors")
public class MonitorViewController {
    private final MonitorService monitorService;
    private final MonitorMapper monitorMapper;

    @GetMapping(path = "/{monitorId}", produces = MediaType.TEXT_HTML_VALUE)
    public String monitorView(Model model, @PathVariable("monitorId") Long monitorId,
                              @RequestParam(defaultValue = "30") Integer refresh) {
        final Monitor monitor = monitorService.findById(monitorId);

        final MonitorView monitorView = monitorMapper.toViewResource(monitor);
        model.addAttribute("monitor", monitorView);
        model.addAttribute("refresh", refresh);
        model.addAttribute("time",
                monitorView.getLastUpdate().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        return "monitor";
    }
}
