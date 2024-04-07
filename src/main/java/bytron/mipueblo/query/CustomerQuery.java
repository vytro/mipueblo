package bytron.mipueblo.query;

public class CustomerQuery {
//    public static final String STAT_QUERY = "SELECT count(*) as totalCustomers, " +
//            "(SELECT count(*) FROM invoice) as totalInvoices, " +
//            "(SELECT sum(total) FROM invoice) as totalBilled " +
//            "FROM customer";

    public static final String STAT_QUERY = "SELECT c.total_customers, i.total_invoices, inv.total_billed FROM (SELECT count(*) total_customers FROM customer) c, " +
            "(SELECT count(*) total_invoices FROM invoice) i, " +
            "(SELECT round(sum(total)) total_billed FROM invoice) inv";
}
