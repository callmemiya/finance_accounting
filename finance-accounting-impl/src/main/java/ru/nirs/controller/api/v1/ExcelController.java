package ru.nirs.controller.api.v1;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.nirs.entity.TransactionProjection;
import ru.nirs.service.ExcelParserService;

import java.util.List;

/**
 * Контроллер для загрузки данных из excel файла
 */
@RestController
public class ExcelController {

    @Autowired
    private ExcelParserService excelParserService;

    @PostMapping(value = "/parse-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<TransactionProjection> parseExcel(@Parameter(description = "xlsx файл транзакций")
                                                      @RequestPart MultipartFile file) {
        return excelParserService.parseExcelFile(file);
    }

}
