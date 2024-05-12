package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pssbd.fonds.dto.output.DonationOutput;
import ru.pssbd.fonds.service.DonationService;

import java.util.List;

@RestController
@RequestMapping("/donations")
@RequiredArgsConstructor
public class DonationEndpoint {

    private final DonationService service;

    @GetMapping
    public List<DonationOutput> getAllEntity() {
        return service.getAllElem();
    }

    @GetMapping("/{id}")
    public DonationOutput getEntityById(@PathVariable int id) {
        return service.getElemById(id);
    }


}
