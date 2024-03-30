package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pssbd.fonds.dto.output.CurrencyTypeOutput;
import ru.pssbd.fonds.service.CurrencyTypeService;

import java.util.List;

@RestController
@RequestMapping("/currencyType")
@RequiredArgsConstructor
public class CurrencyTypeEndpoint {

    private final CurrencyTypeService service;

    @GetMapping
    public List<CurrencyTypeOutput> getAllEntity() {
        return service.getAllElem();
    }

    @GetMapping("/{id}")
    public CurrencyTypeOutput getEntityById(@PathVariable int id) {
        return service.getElemById(id);
    }


}
