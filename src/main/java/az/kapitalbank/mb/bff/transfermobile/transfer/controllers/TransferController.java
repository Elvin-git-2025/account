package az.kapitalbank.mb.bff.transfermobile.transfer.controllers;

import az.kapitalbank.mb.bff.transfermobile.transfer.dtos.requests.CreateTransferRequest;
import az.kapitalbank.mb.bff.transfermobile.transfer.dtos.responses.TransferResponse;
import az.kapitalbank.mb.bff.transfermobile.transfer.entities.Transfer;
import az.kapitalbank.mb.bff.transfermobile.transfer.services.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transfer")
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @PostMapping("/create")
    public Transfer createTransfer(CreateTransferRequest request) {
        return transferService.createTransfer(request);
    }

    @GetMapping("/getTransfer/{id}")
    public TransferResponse getTransfer(@PathVariable Long id) {
        return transferService.getTransferById(id);
    }

}
