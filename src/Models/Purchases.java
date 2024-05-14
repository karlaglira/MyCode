
package Models;

public class Purchases {
    
    // Definir variables
    private int id;
    private int code;
    private String name; // nombre del producto comprado
    private int purchase_amount; // purchases_details
    private double purchase_price; // purchases_details
    private double purchase_subtotal; // purchases_details
    private double total;
    private String created;
    private String supplier_name; 
    private String purchaser; // employee_fullname de employees

    
    // Método constructor sin parámetros
    public Purchases() {
    }
    
    // Método constructor con parámetros
    public Purchases(int id, int code, String name, int purchase_amount, double purchase_price, double purchase_subtotal, double total, String created, String supplier_name_product, String purchaser) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.purchase_amount = purchase_amount;
        this.purchase_price = purchase_price;
        this.purchase_subtotal = purchase_subtotal;
        this.total = total;
        this.created = created;
        this.supplier_name = supplier_name;
        this.purchaser = purchaser;
    }
    
    
    // Getter & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPurchase_amount() {
        return purchase_amount;
    }

    public void setPurchase_amount(int purchase_amount) {
        this.purchase_amount = purchase_amount;
    }

    public double getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(double purchase_price) {
        this.purchase_price = purchase_price;
    }

    public double getPurchase_subtotal() {
        return purchase_subtotal;
    }

    public void setPurchase_subtotal(double purchase_subtotal) {
        this.purchase_subtotal = purchase_subtotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getPurchaser() {
        return purchaser;
    }

    public void setPurchaser(String purchaser) {
        this.purchaser = purchaser;
    }

 
}
