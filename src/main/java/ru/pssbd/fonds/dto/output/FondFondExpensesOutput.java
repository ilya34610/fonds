package ru.pssbd.fonds.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FondFondExpensesOutput {

    private FondOutput fond;

    private FondExpenseOutput fondExpense;

}
