package ru.pssbd.fonds.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import ru.pssbd.fonds.service.FondExpenseService;
import ru.pssbd.fonds.service.ReceiptService;

@RestController
@RequestMapping("/receiptAndFond_expenses")
public class ReceiptAndFondExpensEndpoint {


    private final ReceiptService receiptService;

    private final FondExpenseService fondExpenseService;

    @Autowired
    public ReceiptAndFondExpensEndpoint(ReceiptService receiptService, FondExpenseService fondExpenseService) {
        this.receiptService = receiptService;
        this.fondExpenseService = fondExpenseService;
    }

    //Открытие справочника
    @GetMapping
    @ResponseBody
    public ModelAndView capitalSources(Authentication authentication) {
        ModelAndView mav = new ModelAndView("receiptAndFond_expens/receiptAndFond_expenses");
//передаем данные из двух таблиц СОЕДИНЯЯ ИХ по таблицам пересечения
        mav.addObject("receiptsCapitalSources", receiptService.getAllElem());

        mav.addObject("fondExpensesFonds", fondExpenseService.getAllElem());



        return mav;
    }

}
