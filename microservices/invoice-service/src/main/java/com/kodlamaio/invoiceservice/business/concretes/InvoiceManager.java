package com.kodlamaio.invoiceservice.business.concretes;

import com.kodlamaio.invoiceservice.business.abstracts.InvoiceService;
import com.kodlamaio.invoiceservice.business.dto.*;
import com.kodlamaio.invoiceservice.business.rules.InvoiceBusinessRules;
import com.kodlamaio.invoiceservice.entity.Invoice;
import com.kodlamaio.invoiceservice.repository.InvoiceRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InvoiceManager implements InvoiceService {
    private final InvoiceRepository repository;
    private final ModelMapper mapper;
    private final InvoiceBusinessRules rules;

    @Override
    public List<GetAllInvoicesResponse> getAll() {
        List<Invoice> invoices = repository.findAll();
        List<GetAllInvoicesResponse> response = invoices
                .stream()
                .map(invoice -> mapper.map(invoice, GetAllInvoicesResponse.class))
                .toList();

        return response;
    }

    @Override
    public GetInvoiceResponse getById(String id) {
        rules.checkIfInvoiceExists(id);
        Invoice invoice = repository.findById(id).orElseThrow();
        GetInvoiceResponse response = mapper.map(invoice, GetInvoiceResponse.class);

        return response;
    }

    @Override
    public CreateInvoiceResponse add(CreateInvoiceRequest request) {
        Invoice invoice = mapper.map(request, Invoice.class);
        invoice.setId(null);
        invoice.setTotalPrice(getTotalPrice(invoice));
        repository.save(invoice);
        CreateInvoiceResponse response = mapper.map(invoice, CreateInvoiceResponse.class);

        return response;
    }

    @Override
    public UpdateInvoiceResponse update(String id, UpdateInvoiceRequest request) {
        rules.checkIfInvoiceExists(id);
        Invoice invoice = mapper.map(request, Invoice.class);
        invoice.setId(id);
        invoice.setTotalPrice(getTotalPrice(invoice));
        repository.save(invoice);
        UpdateInvoiceResponse response = mapper.map(invoice, UpdateInvoiceResponse.class);

        return response;
    }

    @Override
    public void delete(String id) {
        rules.checkIfInvoiceExists(id);
        repository.deleteById(id);
    }

    private double getTotalPrice(Invoice invoice) {
        return invoice.getDailyPrice() * invoice.getRentedForDays();
    }
}
