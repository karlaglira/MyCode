package Models;

public class Sales {
    // Definir variables
    private int code;
    private int salesID;
    private String productName;
    private int amount;
    private int customerID;
    private double total;
    private double price;
    private double subtotal;
    private int stock;
    private String customerName;
    private String created;
    private String employeeName;
    
    // Método constructor sin parámetros
    public Sales() {
    }
    
    // Método constructor con parámetros
    public Sales(int code, int salesID, String productName, int amount, int customerID, double total, double price, double subtotal, int stock, String customerName, String created, String employeeName) {
        this.code = code;
        this.salesID = salesID;
        this.productName = productName;
        this.amount = amount;
        this.customerID = customerID;
        this.total = total;
        this.price = price;
        this.subtotal = subtotal;
        this.stock = stock;
        this.customerName = customerName;
        this.created = created;
        this.employeeName = employeeName;
    }
    
    
    
    // Getter & Setter
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getSalesID() {
        return salesID;
    }

    public void setSalesID(int salesID) {
        this.salesID = salesID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    
}
