package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pssbd.fonds.dto.output.CitizenOutput;
import ru.pssbd.fonds.service.CitizenService;

import java.util.List;

@RestController
@RequestMapping("/citizen")
@RequiredArgsConstructor
public class CitizenEndpoint {

    private final CitizenService service;

    @GetMapping
    public List<CitizenOutput> getAllEntity() {
        return service.getAllElem();
    }

    @GetMapping("/{id}")
    public CitizenOutput getEntityById(@PathVariable int id) {
        return service.getElemById(id);
    }

}
