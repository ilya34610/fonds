package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.pssbd.fonds.dto.output.CapitalSourceOutput;
import ru.pssbd.fonds.service.CapitalSourceService;

import java.util.List;

@Controller
@RequestMapping("/capital_source")
@RequiredArgsConstructor
public class CapitalSourceEndpoint {

    private final CapitalSourceService service;

    @GetMapping
    public String getAllEntity(Model model) {
        List<CapitalSourceOutput> capitalSource = service.getAllElem();
        model.addAttribute("capitalSources", capitalSource);
        return "CapitalSource/capitalSourceGetAll";
    }

    @GetMapping("/{id}")
    public CapitalSourceOutput getEntityById(@PathVariable int id) {
        return service.getElemById(id);
    }


}
