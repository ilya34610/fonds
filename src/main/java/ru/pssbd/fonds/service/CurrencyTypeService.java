package ru.pssbd.fonds.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.input.CurrencyTypeInput;
import ru.pssbd.fonds.dto.output.CurrencyTypeOutput;
import ru.pssbd.fonds.entity.CurrencyTypeEntity;
import ru.pssbd.fonds.mappers.CurrencyTypeMapper;
import ru.pssbd.fonds.repository.CurrencyTypeRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CurrencyTypeService {

    private final CurrencyTypeRepository repository;
    private final CurrencyTypeMapper mapper;

    public CurrencyTypeService(CurrencyTypeRepository repository, CurrencyTypeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CurrencyTypeOutput> getAllElem() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .map(mapper::toOutput)
                .collect(Collectors.toList());
    }

    public CurrencyTypeOutput getElemById(Short id) {
        return repository.findById(id)
                .map(mapper::toOutput)
                .orElseThrow(() -> new NoSuchElementException("Элемент с id " + id + " не найден"));
    }

    public CurrencyTypeEntity get(short id) {
        return repository.getById(id);
    }

    public CurrencyTypeEntity save(CurrencyTypeEntity entity) {
        return repository.save(entity);
    }


    public void deleteById(Short id) {
        repository.deleteById(id);
    }

    public void putById(Short id, CurrencyTypeInput input) {
        CurrencyTypeEntity entity = repository.findById(id)
                .map(existngEntity -> mapper.fromInput(input, existngEntity))
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
        repository.save(entity);
    }

//    public List<GroupByRequestOutput> getElemForGroupBy() {
//        List<GroupByRequestOutput> result = new ArrayList<>();
//
//        LocalDate date = LocalDate.of(2000, 1, 5);
//        List<Object[]> data = repository.getElemForGroupBy(date);
//        if (!data.isEmpty()) {
//            for (Object[] row : data) {
//
//                result.add(new GroupByRequestOutput((BigDecimal) row[0], (String) row[1]));
//            }
//            return result;
//        } else {
//            return result;
//        }
//
//    }


}
