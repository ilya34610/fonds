package ru.pssbd.fonds.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pssbd.fonds.dto.input.CapitalSourceInput;
import ru.pssbd.fonds.dto.output.*;
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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CapitalSourceService {

    private final FondService fondService;
    private final CapitalSourceRepository repository;
    private final CapitalSourceMapper mapper;

    private final FondRepository fondRepository;
    private final ReceiptRepository receiptRepository;
//    private final FondMapper fondMapper;

    private final UserService userService;
    private final CurrencyTypeService currencyTypeService;

    private final DonationTypeService donationTypeService;

    @Autowired
    public CapitalSourceService(CapitalSourceRepository repository, CapitalSourceMapper mapper, FondRepository fondRepository, FondMapper fondMapper, FondService fondService, ReceiptRepository receiptRepository, UserService userService, CurrencyTypeService currencyTypeService, DonationTypeService donationTypeService) {
        this.repository = repository;
        this.mapper = mapper;
        this.fondRepository = fondRepository;
        this.fondService = fondService;
        this.receiptRepository = receiptRepository;
//        this.fondMapper = fondMapper;
        this.userService = userService;
        this.currencyTypeService = currencyTypeService;
        this.donationTypeService = donationTypeService;
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

    public List<CurrencyTypeCapitalSourcesOutput> getElemForLeftJoin() {
//        return mapper.toOutput(repository.getAllCapitalSourcesLeftJoin());

        List<CurrencyTypeCapitalSourcesOutput> result = new ArrayList<>();
//
        List<Object[]> data = repository.getAllCapitalSourcesLeftJoin();
        if (!data.isEmpty()) {
            for (Object[] row : data) {
                CurrencyTypeOutput currencyType = new CurrencyTypeOutput();
                currencyType.setId((Short) row[0]);
                currencyType.setName((String) row[1]);

                CapitalSourceOutput capitalSource = new CapitalSourceOutput();


                if (row[3] != null) {
                    capitalSource.setSum((BigDecimal) row[2]);

                    java.sql.Date sqlDate = (java.sql.Date) row[3];
                    LocalDate localDate = sqlDate.toLocalDate();
                    capitalSource.setDonationDate(localDate);

                    capitalSource.setUser(userService.getElemById((Integer) row[4]));
                }

                result.add(new CurrencyTypeCapitalSourcesOutput(currencyType, capitalSource));
            }
            return result;
        } else {
            return result;
        }
    }


    public List<DonationTypeCapitalSourcesOutput> getElemForRightJoin() {
//        return mapper.toOutput(repository.getAllCapitalSourcesLeftJoin());

        List<DonationTypeCapitalSourcesOutput> result = new ArrayList<>();
//
        List<Object[]> data = repository.getAllCapitalSourcesRightJoin();
        if (!data.isEmpty()) {
            for (Object[] row : data) {
                DonationTypeOutput donationType = new DonationTypeOutput();
                donationType.setId((Short) row[0]);
                donationType.setName((String) row[1]);

                CapitalSourceOutput capitalSource = new CapitalSourceOutput();


                if (row[3] != null) {
                    capitalSource.setSum((BigDecimal) row[2]);

                    java.sql.Date sqlDate = (java.sql.Date) row[3];
                    LocalDate localDate = sqlDate.toLocalDate();
                    capitalSource.setDonationDate(localDate);

                    capitalSource.setUser(userService.getElemById((Integer) row[4]));
                }

                result.add(new DonationTypeCapitalSourcesOutput(donationType, capitalSource));
            }
            return result;
        } else {
            return result;
        }
    }

    public List<CapitalSourceOutput> getTopThreeDonator() {

//        return repository.getTopThreeDonator().stream()
//                .map(mapper::toOutput)
//                .collect(Collectors.toList());

        Pageable topThree = PageRequest.of(0, 3);
        return repository.getTopThreeDonator(topThree)
                .stream().map(mapper::toOutput)
                .collect(Collectors.toList());
    }


    public List<CapitalSourceOutput> searchCapitalSources(String fondName) {

        Set<BigInteger> addedIds = new HashSet<>();

        List<Object[]> results = repository.findByFondName(fondName);
        List<CapitalSourceOutput> outputs = new ArrayList<>();

//        for (Object[] result : results) {
        for (int i = 0; i < results.size(); i++) {
            Object[] current = results.get(i);
            BigInteger id = (BigInteger) current[0];

            // Проверяем, есть ли уже такой id в множестве
            if (!addedIds.contains(id)) {
                CapitalSourceOutput dto = new CapitalSourceOutput();
                dto.setId(id);
                dto.setSum((BigDecimal) current[1]);
                dto.setCurrencyType(currencyTypeService.getElemById((Short) current[2]));
                dto.setDonationDate(((java.sql.Date) current[3]).toLocalDate());
                dto.setUser(userService.getElemById((Integer) current[4]));

                List<FondOutput> fondOutputs = new ArrayList<>();
                fondOutputs.add(fondService.getElemById((Integer) current[5]));
                dto.setFonds(fondOutputs);

                List<DonationTypeOutput> donationTypeOutputs = new ArrayList<>();

//                Object[] next = (i + 1 < results.size()) ? results.get(i + 1) : null;

                donationTypeOutputs.add(donationTypeService.getElemById((Short) current[6]));


                Object[] next = null;
                if (i + 1 < results.size()) {
                    next = results.get(i + 1);

                    if (next[0] == current[0]) {
                        donationTypeOutputs.add(donationTypeService.getElemById((Short) next[6]));
                        addedIds.add(id);
                    }
                    dto.setDonationTypes(donationTypeOutputs);
                    outputs.add(dto);


                }
//                else {
//                    // Добавляем dto в список outputs
//                    dto.setDonationTypes(donationTypeOutputs);
//                    outputs.add(dto);
//
//                }

//                if(next[0] != null && next[0].equals(current[0])){
//                    donationTypeOutputs.add(donationTypeService.getElemById((Short) next[6]));
//                }


                // Добавляем dto в список outputs
//                outputs.add(dto);


                // Добавляем id в множество, чтобы не добавлять повторно

            }
        }

//        List<CapitalSourceOutput> outputsNew = new ArrayList<>();
//
//        for (int i = 0; i < i++ && i < outputs.size(); i++) {
//            CapitalSourceOutput newCapitalSourceOutput = new CapitalSourceOutput();
//            if (outputs.get(i) == outputs.get(i++)) {
//                List<Short> tempList = new ArrayList<>();
//                tempList.add(outputs.get(i).getDonationTypes().get(0).getId());
//                tempList.add(outputs.get(i++).getDonationTypes().get(0).getId());
////                outputs.get(i).setDonationTypes(tempList);
//                outputs.remove(i++);
//            }
//            outputsNew.add(newCapitalSourceOutput);
//        }



        return outputs;
    }

    public BigDecimal sumOnDonationType() {
//        List<Object[]> results = repository.sumOnDonationType();
//
//        return (BigDecimal) results.get(0));

        List<Object[]> results = repository.sumOnDonationType();
        if (results != null && !results.isEmpty() && results.get(0)[0] instanceof BigDecimal) {
            return (BigDecimal) results.get(0)[0];
        }
        return BigDecimal.ZERO;
    }



}
