package ru.pssbd.fonds.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.input.ReceiptInput;
import ru.pssbd.fonds.dto.output.ReceiptOutput;
import ru.pssbd.fonds.entity.ReceiptEntity;
import ru.pssbd.fonds.service.FondService;

@Service
@RequiredArgsConstructor
public class ReceiptMapper {

    private final FondService fondService;
    private final FondMapper fondMapper;

    public ReceiptEntity fromInput(ReceiptInput input, ReceiptEntity entity) {
        entity.setSum(input.getSum());
        entity.setFond(fondService.get(input.getFond()));
        return entity;
    }

    public ReceiptOutput toOutput(ReceiptEntity entity) {
        return new ReceiptOutput(entity.getId(),
                entity.getSum(),
                fondMapper.toOutput(entity.getFond()));
    }

    public ReceiptEntity fromInput(ReceiptInput input) {
        return fromInput(input, new ReceiptEntity());
    }

}
