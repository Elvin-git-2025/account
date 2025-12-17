package az.kapitalbank.mb.bff.transfermobile.controllers;

import az.kapitalbank.mb.bff.transfermobile.dto.request.CreateCustomerRequest;
import az.kapitalbank.mb.bff.transfermobile.dto.response.CustomerResponse;
import az.kapitalbank.mb.bff.transfermobile.entities.Customer;
import az.kapitalbank.mb.bff.transfermobile.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/transfer")
@RequiredArgsConstructor
public class CustomerController {

   private final CustomerService customerService;


    @PostMapping("/create/customer")
    public ResponseEntity<CustomerResponse> create(
            @Valid @RequestBody CreateCustomerRequest request) {
        return ResponseEntity.ok(customerService.createCustomer(request));
    }

    @GetMapping("/get/customer/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
       CustomerResponse customer= customerService.getCustomerById(id);
       return ResponseEntity.ok(customer);
   }

    @GetMapping("/get/allCustomers")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
       Customer updatedCustomer = customerService.update(id, customer);
       return ResponseEntity.ok(updatedCustomer);
   }

   @DeleteMapping("/delete/{id}")
    public void deleteCustomer(@PathVariable Long id) {
       customerService.delete(id);
   }

}
