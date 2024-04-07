package bytron.mipueblo.repository;

import bytron.mipueblo.domain.Invoice;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface InvoiceRepository extends
        PagingAndSortingRepository<Invoice, Long>,
        ListCrudRepository<Invoice, Long> {

}
