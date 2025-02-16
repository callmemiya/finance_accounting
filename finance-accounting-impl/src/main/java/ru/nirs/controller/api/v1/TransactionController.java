package ru.nirs.controller.api.v1;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nirs.dto.TransactionDto;
import ru.nirs.dto.JoinTransactionsRequestDto;
import ru.nirs.entity.TransactionProjection;
import ru.nirs.mapper.TransactionMapper;
import ru.nirs.dto.TransactionProjectionDto;
import ru.nirs.service.ExcelParserService;
import ru.nirs.service.TransactionService;

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

    @PostMapping(value = "/parse-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<TransactionProjection> parseExcel(@Parameter(description = "xlsx файл транзакций")
                                                  @RequestPart MultipartFile file) {
        return excelParserService.parseExcelFile(file);
    }

    @GetMapping()
    public List<TransactionProjectionDto> getTransactions(@RequestParam(required = true)
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
                                                          @RequestParam(required = true)
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo,
                                                          @RequestParam(required = false) Long categoryId) {
        return TransactionMapper
                .INSTANCE
                .toProjectionDtoList(transactionService.getAll(dateFrom, dateTo, categoryId));
    }

    @PostMapping()
    public Long create(@Parameter(description = "Данные для создания транзакции", required = true)
                           @RequestBody TransactionDto transactionDto) {
        return transactionService.create(TransactionMapper.INSTANCE.toEntity(transactionDto));
    }

    @PostMapping("/join")
    public void joinTransactions(@Parameter(description = "Данные для объединения транзакций", required = true)
                                @Valid @RequestBody JoinTransactionsRequestDto request) {
        transactionService.joinTransactions(request.getTransactionIds(), request.getCategoryId(), request.getDescription());
    }

    @PutMapping()
    public void update(@Parameter(description = "Данные для создания транзакции", required = true)
                       @RequestBody TransactionDto transactionDto) {
        transactionService.update(TransactionMapper.INSTANCE.toEntity(transactionDto));
    }

    @GetMapping("/discharges")
    public List<TransactionProjectionDto> getDischarges(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo,
                                                          Long categoryId) {
        return TransactionMapper
                .INSTANCE
                .toProjectionDtoList(transactionService.getDischarges(dateFrom, dateTo, categoryId));
    }

    @GetMapping("/gains")
    public List<TransactionProjectionDto> getGains(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo,
                                                        Long categoryId) {
        return TransactionMapper
                .INSTANCE
                .toProjectionDtoList(transactionService.getGains(dateFrom, dateTo, categoryId));
    }

    @DeleteMapping(value = "/{id}")
    public Long deleteBy(@PathVariable @Parameter(description = "Идентификатор транзакции", required = true) Long id) {
        Long deletedCount = transactionService.delete(id);
        if (deletedCount > 0) {
            return id;
        }
        throw new IllegalArgumentException(format(ERROR_TRANSACTION_DELETE, id));
    }

}
