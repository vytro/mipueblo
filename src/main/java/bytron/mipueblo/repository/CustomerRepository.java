package bytron.mipueblo.repository;

import bytron.mipueblo.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerRepository extends
        PagingAndSortingRepository<Customer, Long>,
        ListCrudRepository<Customer, Long>{

    // method to search all of the customers by name
    Page<Customer>findByNameContaining(String name, Pageable pageable);
}
