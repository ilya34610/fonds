package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pssbd.fonds.dto.output.CapitalSourceOutput;
import ru.pssbd.fonds.service.CapitalSourceService;

import java.util.List;

@RestController
@RequestMapping("/capitalSource")
@RequiredArgsConstructor
public class CapitalSourceEndpoint {

    private final CapitalSourceService service;

    @GetMapping
    public List<CapitalSourceOutput> getAllEntity() {
        return service.getAllElem();
    }

    @GetMapping("/{id}")
    public CapitalSourceOutput getEntityById(@PathVariable int id) {
        return service.getElemById(id);
    }

}
