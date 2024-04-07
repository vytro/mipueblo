package bytron.mipueblo.service;


import bytron.mipueblo.domain.Customer;
import bytron.mipueblo.domain.Invoice;
import bytron.mipueblo.domain.Stats;
import jakarta.validation.constraints.AssertFalse;
import org.springframework.data.domain.Page;

public interface CustomerService {
    //Customer functionaility
    Customer createCustomer(Customer customer);
    Customer updateCustomer(Customer customer);
    Page<Customer> getCustomers(int page, int size);
    Iterable<Customer> getCustomers();
    Customer getCustomer(Long id);
    Page<Customer> searchCustomers(String name, int page, int size);

    //Invoice functionality
    Invoice createInvoice(Invoice invoice);
    Page<Invoice> getInvoices(int page, int size);
//    void addInvoiceToCustomer(Long customerId, Long invoiceId);
    void addInvoiceToCustomer(Long customerId, Invoice invoice);
    Invoice getInvoice(Long id);

    Stats getStats();

}
