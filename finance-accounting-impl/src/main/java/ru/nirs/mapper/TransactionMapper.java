package ru.nirs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.nirs.entity.Transaction;
import ru.nirs.entity.TransactionProjection;
import ru.nirs.dto.TransactionDto;
import ru.nirs.dto.TransactionProjectionDto;

import java.util.List;

/**
 * Маппер для {@link TransactionProjection}
 */
@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    TransactionDto toDto(TransactionProjection transactionProjection);

    Transaction toEntity(TransactionDto transactionDto);

    TransactionProjectionDto toProjectionDto(TransactionProjection proj);

    List<TransactionProjectionDto> toProjectionDtoList(List<TransactionProjection> projList);

}
