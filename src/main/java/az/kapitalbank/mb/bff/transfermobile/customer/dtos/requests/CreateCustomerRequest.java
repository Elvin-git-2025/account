package az.kapitalbank.mb.bff.transfermobile.customer.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCustomerRequest {

    @NotBlank
    String firstName;

    @NotBlank
    String lastName;

    @NotBlank
    String pin;

    @NotNull
    LocalDate dateOfBirth;
}

