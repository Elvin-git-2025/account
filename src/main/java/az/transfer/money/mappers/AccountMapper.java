package az.transfer.money.mappers;

import az.transfer.money.dtos.requests.CreateAccountRequest;
import az.transfer.money.entities.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {BigDecimal.class, LocalDateTime.class})
public interface AccountMapper {

    @Mapping(target = "balance", expression = "java(BigDecimal.ZERO)")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "customerId", source = "customerId")
    Account toEntity(CreateAccountRequest request);
}

