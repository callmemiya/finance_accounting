package ru.nirs.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.nirs.dao.AccountDao;
import ru.nirs.dao.CategoryDao;
import ru.nirs.dao.TransactionDao;
import ru.nirs.entity.Account;
import ru.nirs.entity.Category;
import ru.nirs.entity.Transaction;
import ru.nirs.entity.TransactionProjection;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Сервис для обработки файла excel
 */
@RequiredArgsConstructor
@Service
public class ExcelParserService {

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private AccountDao accountDao;

    public List<TransactionProjection> parseExcelFile(MultipartFile excelFile) {
        List<TransactionProjection> transactionProjectionList = new ArrayList<>();
        try (Workbook sheets = WorkbookFactory.create(excelFile.getInputStream())) {
            Sheet sheet = sheets.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                } // Пропускаем заголовок таблицы

                TransactionProjection transactionProjection = new TransactionProjection(
                        null,
                        getCellDateValue(row.getCell(0)),  // Дата операции
                        getCellStringValue(row.getCell(2)), // Номер карты
                        getCellStringValue(row.getCell(3)), // Статус
                        getCellNumericValue(row.getCell(4)), // Сумма операции
                        getCellStringValue(row.getCell(5)), // Валюта операции
                        getCellNumericValue(row.getCell(8)), // Кэшбэк
                        Objects.equals(getCellStringValue(row.getCell(9)), "") ?
                                "Без категории" : getCellStringValue(row.getCell(9)), // Категория
                        getCellStringValue(row.getCell(11)), // Описание
                        LocalDateTime.now(),
                        LocalDateTime.now()
                );
                transactionProjectionList.add(transactionProjection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        save(transactionProjectionList);
        return transactionProjectionList;
    }

    private Integer save(List<TransactionProjection> projectionList) {
        var count = 0;
        for (TransactionProjection proj : projectionList) {
            if (!transactionDao.existsTransactionByTransactionDateAndAmount(proj.getTransactionDate(), proj.getOperationAmount())) {
                var categoryName = proj.getCategory();
                Optional<Category> categoryOp = categoryDao.getCategoryByDescription(proj.getDescription());
                if (categoryOp.isEmpty()) {
                    categoryOp = categoryDao.getByCategoryName(categoryName);
                }
                Category category = categoryOp.orElseGet(() -> categoryDao.save(Category.builder()
                        .categoryName(categoryName)
                        .createdDatetime(LocalDateTime.now())
                        .modifiedDatetime(LocalDateTime.now())
                        .build()));
                var accountName = proj.getCardNumber();
                Optional<Account> accountOp;
                if (accountName.isEmpty()) {
                    accountOp = accountDao.getByAccountName("WITHOUT_CARD");
                } else {
                    accountOp = accountDao.getByAccountName(accountName);
                }
                Account account = accountOp.orElseGet(() -> {
                    String currency = "RUB";
                    Optional<TransactionProjection> projOp = projectionList
                            .stream()
                            .filter(transactionProjection -> Objects.equals(transactionProjection.getCardNumber(), accountName))
                            .findFirst();
                    if (projOp.isPresent()) {
                        currency = projOp.get().getCurrency();
                    }
                    return accountDao.save(Account.builder()
                            .userId(1L)
                            .accountName(accountName)
                            .currency(currency)
                            .createdDatetime(LocalDateTime.now())
                            .modifiedDatetime(LocalDateTime.now())
                            .build());
                });
                var transaction = Transaction.builder()
                        .accountId(account.getId())
                        .categoryId(category.getId())
                        .status(proj.getStatus())
                        .amount(proj.getOperationAmount())
                        .cashback(proj.getCashback())
                        .transactionDate(proj.getTransactionDate())
                        .description(proj.getDescription())
                        .createdDatetime(LocalDateTime.now())
                        .modifiedDatetime(LocalDateTime.now())
                        .build();
                transactionDao.save(transaction);
                count++;
            }
        }
        return count;
    }

    private String getCellStringValue(Cell cell) {
        return cell == null ? "" : cell.toString();
    }

    private double getCellNumericValue(Cell cell) {
        return cell == null ? 0.0 : cell.getNumericCellValue();
    }

    private Date getCellDateValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            return formatter.parse(cell.getStringCellValue());
        } catch (Exception e) {
            return null;
        }
    }

}
