package ru.nirs.controller.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nirs.mapper.TransactionMapper;
import ru.nirs.dto.TransactionProjectionDto;
import ru.nirs.security.UserPrincipal;
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

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return principal.getId();
    }

    @GetMapping("/salary")
    @Operation(summary = "Получение данных о зарплатах по параметрам")
    public List<TransactionProjectionDto> getSalaries(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo) {
        return TransactionMapper
                .INSTANCE
                .toProjectionDtoList(transactionService.getSalaries(getCurrentUserId(), dateFrom, dateTo));
    }

}
