package az.kapitalbank.mb.bff.transfermobile.mappers;

import az.kapitalbank.mb.bff.transfermobile.dtos.requests.CreateCustomerRequest;
import az.kapitalbank.mb.bff.transfermobile.dtos.responses.CustomerResponse;
import az.kapitalbank.mb.bff.transfermobile.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toEntity(CreateCustomerRequest request);

    @Mapping(target = "age", expression = "java(customer.getAge())")
    CustomerResponse toResponse(Customer customer);

    List<CustomerResponse> toResponseList(List<Customer> customers);
}
