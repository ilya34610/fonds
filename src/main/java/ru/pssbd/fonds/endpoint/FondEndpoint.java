package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pssbd.fonds.dto.output.FondOutput;
import ru.pssbd.fonds.service.FondService;

import java.util.List;

@RestController
@RequestMapping("/fond")
@RequiredArgsConstructor
public class FondEndpoint {

    private final FondService service;

    @GetMapping
    public List<FondOutput> getAllEntity() {
        return service.getAllElem();
    }

    @GetMapping("/{id}")
    public FondOutput getEntityById(@PathVariable int id) {
        return service.getElemById(id);
    }


}
