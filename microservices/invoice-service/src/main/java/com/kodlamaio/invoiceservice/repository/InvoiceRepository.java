package com.kodlamaio.invoiceservice.repository;

import com.kodlamaio.invoiceservice.entity.Invoice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface InvoiceRepository extends ElasticsearchRepository<Invoice, String> {
    @Override
    List<Invoice> findAll();
}
