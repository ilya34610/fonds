package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pssbd.fonds.dto.output.CountryOutput;
import ru.pssbd.fonds.service.CountryService;

import java.util.List;

@RestController
@RequestMapping("/country")
@RequiredArgsConstructor
public class CountryEndpoint {

    private final CountryService service;

    @GetMapping
    public List<CountryOutput> getAllEntity() {
        return service.getAllElem();
    }

    @GetMapping("/{id}")
    public CountryOutput getEntityById(@PathVariable int id) {
        return service.getElemById(id);
    }

}
