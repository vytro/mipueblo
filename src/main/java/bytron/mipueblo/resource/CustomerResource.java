package bytron.mipueblo.resource;

import bytron.mipueblo.domain.Customer;
import bytron.mipueblo.domain.HttpResponse;
import bytron.mipueblo.domain.Invoice;
import bytron.mipueblo.dto.UserDTO;
import bytron.mipueblo.report.CustomerReport;
import bytron.mipueblo.service.CustomerService;
import bytron.mipueblo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.time.LocalTime.now;
import static java.util.Map.of;
import static org.springframework.http.MediaType.parseMediaType;

@RestController
@RequestMapping(path = "/customer")
@RequiredArgsConstructor
public class CustomerResource {

    private final CustomerService customerService;
    private final UserService userService;


    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createCustomer(
            @AuthenticationPrincipal UserDTO user,
            @RequestBody Customer customer) {

        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "customer", customerService.createCustomer(customer)))
                        .message("Cliente creado")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build());
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<HttpResponse> getCustomer(
            @AuthenticationPrincipal UserDTO user,
            @PathVariable("id") Long id) {

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "customer", customerService.getCustomer(id)))
                        .message("Cliente obtenido")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }


    @GetMapping("/list")
    public ResponseEntity<HttpResponse> getCustomers(
            @AuthenticationPrincipal UserDTO user,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size) {

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "page", customerService.getCustomers(
                                        page.orElse(0),
                                        size.orElse(10)),
                                "stats", customerService.getStats()))
                        .message("Clientes obtenidos")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }


//    @GetMapping("/search")
//    public ResponseEntity<HttpResponse> searchCustomer(
//            @AuthenticationPrincipal UserDTO user,
//            Optional<String> name,
//            @RequestParam Optional<Integer> page,
//            @RequestParam Optional<Integer> size) {
//
//        return ResponseEntity.ok(
//                HttpResponse.builder()
//                        .timestamp(now().toString())
//                        .data(of("user", userService.getUserByEmail(user.getEmail()),
//                                "page", customerService.searchCustomers(
//                                        name.orElse(""),
//                                        page.orElse(0),
//                                        size.orElse(10))))
//                        .message("Clientes obtenidos")
//                        .status(HttpStatus.OK)
//                        .statusCode(HttpStatus.OK.value())
//                        .build());
//    }

    @GetMapping("/search")
    public ResponseEntity<HttpResponse> searchCustomer(@AuthenticationPrincipal UserDTO user, Optional<String> name, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "page", customerService.searchCustomers(name.orElse(""), page.orElse(0), size.orElse(10))))
                        .message("Clientes obtenidas")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }


    @PutMapping("/update")
    public ResponseEntity<HttpResponse> updateCustomer(
            @AuthenticationPrincipal UserDTO user,
            @RequestBody Customer customer) {

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "customer", customerService.updateCustomer(customer)))
                        .message("Cliente actualizado")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }




    @PostMapping("/invoice/created")
    public ResponseEntity<HttpResponse> createInvoice(
            @AuthenticationPrincipal UserDTO user,
            @RequestBody Invoice invoice) {

        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "invoice", customerService.createInvoice(invoice)))
                        .message("Factura creada")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build());
    }


    @GetMapping("invoice/list")
    public ResponseEntity<HttpResponse> getInvoices(
            @AuthenticationPrincipal UserDTO user,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size) {

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "page", customerService.getInvoices(
                                        page.orElse(0),
                                        size.orElse(10))))
                        .message("Facturas obtenidas")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @GetMapping("/invoice/new") //need to fix
    public ResponseEntity<HttpResponse> newInvoice(@AuthenticationPrincipal UserDTO user) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "customers", customerService.getCustomers()))
                        .message("Clientes obtenidos")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @GetMapping("/invoice/get/{id}")
    public ResponseEntity<HttpResponse> getInvoice(
            @AuthenticationPrincipal UserDTO user,
            @PathVariable("id") Long id) {

        Invoice invoice = customerService.getInvoice(id);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "invoice", invoice,
                                "customer", invoice.getCustomer()))
                        .message("Factura obtenido")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @PostMapping("/invoice/addtocustomer/{id}") //add the invoice to the customer
    public ResponseEntity<HttpResponse> addInvoiceToCustomer(
            @AuthenticationPrincipal UserDTO user,
            @PathVariable("id") Long id,
            @RequestBody Invoice invoice) {

//        customerService.addInvoiceToCustomer(id, invoice.getId());
        customerService.addInvoiceToCustomer(id, invoice);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "customers", customerService.getCustomers()))
//                        .message("Factura añadida al cliente")
                        //might be better having name instead of id, or maybe both
                        .message(String.format(
                                "Factura añadida al cliente con ID %s. Con cliente nombre: %s",
                                id, customerService.getCustomer(id).getName()))
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }


    @GetMapping("/download/report")
    public ResponseEntity<Resource> downloadReport() throws InterruptedException {
        List<Customer> customers = new ArrayList<>();
        customerService.getCustomers().iterator().forEachRemaining(customers::add);
        CustomerReport report = new CustomerReport(customers);
        HttpHeaders headers = new HttpHeaders();
        headers.add("File-Name", "customers-report.xlsx");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;File-Name=customers-report.xlsx");

        return ResponseEntity.ok().contentType(parseMediaType("application/vnd.ms-excel"))
                .headers(headers)
                .body(report.export());
    }


}
