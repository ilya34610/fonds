package ru.pssbd.fonds.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pssbd.fonds.dto.input.CapitalSourceInput;
import ru.pssbd.fonds.dto.output.CapitalSourceOutput;
import ru.pssbd.fonds.dto.output.FondCapitalSourceOutput;
import ru.pssbd.fonds.dto.output.UserOutput;
import ru.pssbd.fonds.entity.CapitalSourceEntity;
import ru.pssbd.fonds.entity.FondEntity;
import ru.pssbd.fonds.entity.ReceiptEntity;
import ru.pssbd.fonds.entity.UserEntity;
import ru.pssbd.fonds.mappers.CapitalSourceMapper;
import ru.pssbd.fonds.mappers.FondMapper;
import ru.pssbd.fonds.repository.CapitalSourceRepository;
import ru.pssbd.fonds.repository.FondRepository;
import ru.pssbd.fonds.repository.ReceiptRepository;

import javax.persistence.EntityNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CapitalSourceService {

    private final CapitalSourceRepository repository;
    private final CapitalSourceMapper mapper;

    private final FondRepository fondRepository;
    private final ReceiptRepository receiptRepository;
//    private final FondMapper fondMapper;

    private final UserService userService;

    @Autowired
    public CapitalSourceService(CapitalSourceRepository repository, CapitalSourceMapper mapper, FondRepository fondRepository, FondMapper fondMapper, ReceiptRepository receiptRepository, UserService userService) {
        this.repository = repository;
        this.mapper = mapper;
        this.fondRepository = fondRepository;
        this.receiptRepository = receiptRepository;
//        this.fondMapper = fondMapper;
        this.userService = userService;
    }

    public List<CapitalSourceOutput> getAllElem() {
        return repository.findAll().stream()
                .map(mapper::toOutput)
                .collect(Collectors.toList());
    }

    public CapitalSourceOutput getElemById(int id) {
        return repository.findById(BigInteger.valueOf(id))
                .map(mapper::toOutput)
                .orElseThrow(() -> new NoSuchElementException("Элемент с id " + id + " не найден"));
    }

    public List<CapitalSourceOutput> getAllElemForCurrentDonater(UserOutput donater) {
        UserEntity userEntity = userService.get(donater.getId());
        return repository.findAllElemByCurrentDonater(userEntity).stream()
                .map(mapper::toOutput)
                .collect(Collectors.toList());
    }

    @Transactional
    public CapitalSourceEntity save(CapitalSourceEntity entity) {

        fondRepository.plusBalance(entity.getSum(), entity.getFonds().get(0).getId());

//        receiptRepository.plusNewReceipt(entity.getSum(), entity.getFonds().get(0).getId());

        List<ReceiptEntity> recieptList = new ArrayList<>();
        ReceiptEntity receiptEntity = new ReceiptEntity(entity.getSum(), entity.getFonds().get(0));
        receiptRepository.save(receiptEntity);
        recieptList.add(receiptEntity);
        entity.setReceipts(recieptList);

        return repository.save(entity);
    }


    public void deleteById(BigInteger id) {
        repository.deleteById(id);
    }

    public void putById(BigInteger id, CapitalSourceInput input) {
        CapitalSourceEntity entity = repository.findById(id)
                .map(existngEntity -> mapper.fromInput(input, existngEntity))
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
        repository.save(entity);
    }

    public List<FondCapitalSourceOutput> getAllCapitalSource() {

        List<FondCapitalSourceOutput> result = new ArrayList<>();

        List<Object[]> data = repository.findAllElem();
        if (!data.isEmpty()) {
            for (Object[] row : data) {
                FondEntity fond = (FondEntity) row[0];
                CapitalSourceEntity capitalSource = (CapitalSourceEntity) row[1];
                result.add(new FondCapitalSourceOutput(fond, capitalSource));
            }
            return result;
        } else {
            return result;
        }
    }


}
