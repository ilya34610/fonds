package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pssbd.fonds.dto.output.DonationTypeOutput;
import ru.pssbd.fonds.service.DonationTypeService;

import java.util.List;

@RestController
@RequestMapping("/donationType")
@RequiredArgsConstructor
public class DonationTypeEndpoint {

    private final DonationTypeService service;

    @GetMapping
    public List<DonationTypeOutput> getAllEntity() {
        return service.getAllElem();
    }

    @GetMapping("/{id}")
    public DonationTypeOutput getEntityById(@PathVariable int id) {
        return service.getElemById(id);
    }


}
