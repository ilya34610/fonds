package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import ru.pssbd.fonds.repository.FondExpenseRepository;
import ru.pssbd.fonds.repository.ReceiptRepository;
import ru.pssbd.fonds.service.FondExpenseService;
import ru.pssbd.fonds.service.FondService;
import ru.pssbd.fonds.service.ReceiptService;

@RestController
@RequestMapping("/receiptAndFond_expenses")
@RequiredArgsConstructor
public class ReceiptAndFondExpensEndpoint {

    private final ReceiptRepository receiptRepository;
    private final ReceiptService receiptService;

    private final FondExpenseRepository fondExpenseRepository;
    private final FondExpenseService fondExpenseService;

    private final FondService fondService;

    //Открытие справочника
    @GetMapping
    @ResponseBody
    public ModelAndView capitalSources(Authentication authentication) {
        ModelAndView mav = new ModelAndView("receiptAndFond_expens/receiptAndFond_expenses");
//передаем данные из двух таблиц СОЕДИНЯЯ ИХ по таблицам пересечения
        mav.addObject("receiptsCapitalSources", receiptService.getAllElem());

        mav.addObject("fondExpensesFonds", fondExpenseService.getAllElem());

        mav.addObject("fondExpensesFondsQuery", fondService.getElemQueryNotIn());


        return mav;
    }

}
