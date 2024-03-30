package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pssbd.fonds.dto.output.CityOutput;
import ru.pssbd.fonds.service.CityService;

import java.util.List;

@RestController
@RequestMapping("/city")
@RequiredArgsConstructor
public class CityEndpoint {

    private final CityService service;

    @GetMapping
    public List<CityOutput> getAllEntity() {
        return service.getAllElem();
    }

    @GetMapping("/{id}")
    public CityOutput getEntityById(@PathVariable int id) {
        return service.getElemById(id);
    }

}
