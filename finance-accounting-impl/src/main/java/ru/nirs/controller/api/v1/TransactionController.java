package ru.nirs.controller.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nirs.dto.TransactionDto;
import ru.nirs.dto.JoinTransactionsRequestDto;
import ru.nirs.entity.TransactionProjection;
import ru.nirs.mapper.TransactionMapper;
import ru.nirs.dto.TransactionProjectionDto;
import ru.nirs.service.ExcelParserService;
import ru.nirs.service.TransactionService;
import ru.nirs.security.UserPrincipal;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;

/**
 * Контроллер для работы с транзакциями
 */
@RequestMapping(value = TransactionController.TRANSACTIONS)
@RestController
public class TransactionController {

    static final String TRANSACTIONS = "/transactions";
    private static final String ERROR_TRANSACTION_DELETE = "Unable to delete Transaction with id = %s";

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ExcelParserService excelParserService;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return principal.getId();
    }

    @PostMapping(value = "/parse-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Обработка файла с транзакциями")
    public List<TransactionProjection> parseExcel(@Parameter(description = "xlsx файл транзакций")
                                                  @RequestPart MultipartFile file) {
        return excelParserService.parseExcelFile(file);
    }

    @GetMapping()
    @Operation(summary = "Получение данных о транзакциях согласно параметрам")
    public List<TransactionProjectionDto> getTransactions(@RequestParam(required = true)
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
                                                          @RequestParam(required = true)
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo,
                                                          @RequestParam(required = false) Long categoryId) {
        return TransactionMapper
                .INSTANCE
                .toProjectionDtoList(transactionService.getAll(getCurrentUserId(), dateFrom, dateTo, categoryId));
    }

    @PostMapping()
    @Operation(summary = "Создание транзакции")
    public Long create(@Parameter(description = "Данные для создания транзакции", required = true)
                           @RequestBody TransactionDto transactionDto) {
        return transactionService.create(TransactionMapper.INSTANCE.toEntity(transactionDto));
    }

    @PostMapping("/join")
    @Operation(summary = "Объединение транзакций")
    public void joinTransactions(@Parameter(description = "Данные для объединения транзакций", required = true)
                                @Valid @RequestBody JoinTransactionsRequestDto request) {
        transactionService.joinTransactions(request.getTransactionIds(), request.getCategoryId(), request.getDescription());
    }

    @PutMapping()
    @Operation(summary = "Обновление транзакции")
    public void update(@Parameter(description = "Данные для создания транзакции", required = true)
                       @RequestBody TransactionDto transactionDto) {
        transactionService.update(TransactionMapper.INSTANCE.toEntity(transactionDto));
    }

    @GetMapping("/discharges")
    @Operation(summary = "Получение данных о транзакциях с положительной суммой согласно параметрам")
    public List<TransactionProjectionDto> getDischarges(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo,
                                                          Long categoryId) {
        return TransactionMapper
                .INSTANCE
                .toProjectionDtoList(transactionService.getDischarges(getCurrentUserId(), dateFrom, dateTo, categoryId));
    }

    @GetMapping("/gains")
    @Operation(summary = "Получение данных о транзакциях с отрицательной суммой согласно параметрам")
    public List<TransactionProjectionDto> getGains(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo,
                                                        Long categoryId) {
        return TransactionMapper
                .INSTANCE
                .toProjectionDtoList(transactionService.getGains(getCurrentUserId(), dateFrom, dateTo, categoryId));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удаление транзакции по уникальному идентификатору")
    public Long deleteBy(@PathVariable @Parameter(description = "Идентификатор транзакции", required = true) Long id) {
        Long deletedCount = transactionService.delete(id);
        if (deletedCount > 0) {
            return id;
        }
        throw new IllegalArgumentException(format(ERROR_TRANSACTION_DELETE, id));
    }

}
