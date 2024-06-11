package ru.pssbd.fonds.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.output.FondFondExpensesOutput;
import ru.pssbd.fonds.dto.output.ReceiptCapitalSourceOutput;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportService {

    public void exportToExcel(HttpServletResponse response, List<ReceiptCapitalSourceOutput> receipts, List<FondFondExpensesOutput> expenses) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        // Создание листа для поступлений
        Sheet receiptSheet = workbook.createSheet("Receipts");
        createReceiptHeader(receiptSheet);
        fillReceiptData(receiptSheet, receipts);

        // Создание листа для трат
        Sheet expenseSheet = workbook.createSheet("Expenses");
        createExpenseHeader(expenseSheet);
        fillExpenseData(expenseSheet, expenses);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=Report.xlsx");

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
        } finally {
            workbook.close();
        }
    }

    private void createReceiptHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        Cell cell = headerRow.createCell(0);
        cell.setCellValue("ID");
        cell = headerRow.createCell(1);
        cell.setCellValue("Источник формирования капитала");
        cell = headerRow.createCell(2);
        cell.setCellValue("Фонд");
        cell = headerRow.createCell(3);
        cell.setCellValue("Поступления");
    }

    private void fillReceiptData(Sheet sheet, List<ReceiptCapitalSourceOutput> receipts) {
        int rowCount = 1;
        for (ReceiptCapitalSourceOutput receipt : receipts) {
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue((RichTextString) receipt.getCapitalSource().getId());
            row.createCell(1).setCellValue(receipt.getCapitalSource().getUser().getFio());
            row.createCell(2).setCellValue(receipt.getCapitalSource().getFonds().get(0).getName());
            row.createCell(3).setCellValue(receipt.getCapitalSource().getSum().toString());
        }
    }

    private void createExpenseHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        Cell cell = headerRow.createCell(0);
        cell.setCellValue("ID");
        cell = headerRow.createCell(1);
        cell.setCellValue("Фонд");
        cell = headerRow.createCell(2);
        cell.setCellValue("Гражданин");
        cell = headerRow.createCell(3);
        cell.setCellValue("Траты");
    }

    private void fillExpenseData(Sheet sheet, List<FondFondExpensesOutput> expenses) {
        int rowCount = 1;
        for (FondFondExpensesOutput expense : expenses) {
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue((RichTextString) expense.getFondExpense().getId());
            row.createCell(1).setCellValue(expense.getFond().getName());
            row.createCell(2).setCellValue(expense.getFondExpense().getCitizen().getUser().getFio());
            row.createCell(3).setCellValue(expense.getFondExpense().getSum().toString());
        }
    }

}
