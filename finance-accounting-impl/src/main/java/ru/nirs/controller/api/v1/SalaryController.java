package ru.nirs.controller.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nirs.mapper.TransactionMapper;
import ru.nirs.dto.TransactionProjectionDto;
import ru.nirs.service.TransactionService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Контроллер для работы с зарплатой
 */
@RestController
public class SalaryController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/salary")
    public List<TransactionProjectionDto> getSalaries(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo) {
        return TransactionMapper
                .INSTANCE
                .toProjectionDtoList(transactionService.getSalaries(dateFrom, dateTo));
    }

}
