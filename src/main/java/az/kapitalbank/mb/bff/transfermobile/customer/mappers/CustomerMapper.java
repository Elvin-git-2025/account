package az.kapitalbank.mb.bff.transfermobile.customer.mappers;

import az.kapitalbank.mb.bff.transfermobile.customer.dtos.requests.CreateCustomerRequest;
import az.kapitalbank.mb.bff.transfermobile.customer.dtos.requests.UpdateCustomerRequest;
import az.kapitalbank.mb.bff.transfermobile.customer.dtos.responses.CustomerResponse;
import az.kapitalbank.mb.bff.transfermobile.customer.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toEntity(CreateCustomerRequest request);

    CustomerResponse toResponse(Customer customer);

    void updateCustomerFromRequest(UpdateCustomerRequest request,@MappingTarget Customer customer);

    List<CustomerResponse> toResponseList(List<Customer> customers);
}