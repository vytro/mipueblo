package bytron.mipueblo.service.implementation;

import bytron.mipueblo.domain.Customer;
import bytron.mipueblo.domain.Invoice;
import bytron.mipueblo.domain.Stats;
import bytron.mipueblo.repository.CustomerRepository;
import bytron.mipueblo.repository.InvoiceRepository;
import bytron.mipueblo.rowmapper.StatsRowMapper;
import bytron.mipueblo.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.Map;

import static bytron.mipueblo.query.CustomerQuery.STAT_QUERY;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final InvoiceRepository invoiceRepository;

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Customer createCustomer(Customer customer) {
        customer.setCreatedDate(new Date());
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    //works og
//    @Override
//    public Page<Customer> getCustomers(int page, int size) {
//        return customerRepository.findAll(PageRequest.of(page, size));
//    }

    //for descending comments
    //jpa automatically maps the createdDate to the created_date column in the database
    @Override
    public Page<Customer> getCustomers(int page, int size) {
        return customerRepository.findAll(PageRequest.of(page, size, Sort.by("createdDate").descending()));
    }

    @Override
    public Iterable<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomer(Long id) {
        return customerRepository.findById(id).get();
    }

    //works og
//    @Override
//    public Page<Customer> searchCustomers(String name, int page, int size) {
//        return customerRepository.findByNameContaining(name, PageRequest.of(page, size));
//    }

    @Override
    public Page<Customer> searchCustomers(String name, int page, int size) {
        return customerRepository.findByNameContaining(name, PageRequest.of(page, size, Sort.by("createdDate").descending()));
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        invoice.setInvoiceNumber(RandomStringUtils.randomAlphanumeric(8).toUpperCase());
        return invoiceRepository.save(invoice);
    }

    @Override
    public Page<Invoice> getInvoices(int page, int size) {
        return invoiceRepository.findAll(PageRequest.of(page, size));
    }

    @Override
//    public void addInvoiceToCustomer(Long customerId, Long invoiceId) {
    public void addInvoiceToCustomer(Long customerId, Invoice invoice) {


        invoice.setInvoiceNumber(RandomStringUtils.randomAlphanumeric(8).toUpperCase());

        Customer customer = customerRepository.findById(customerId).get();
//        Invoice invoice = invoiceRepository.findById(invoiceId).get();
        invoice.setCustomer(customer);
        invoiceRepository.save(invoice);
    }

    @Override
    public Invoice getInvoice(Long id) {
        return invoiceRepository.findById(id).get();
    }

    @Override
    public Stats getStats() {
        return jdbc.queryForObject(STAT_QUERY, Map.of(), new StatsRowMapper());
    }
}
