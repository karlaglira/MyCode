
package Models;

public class Customers {
    
    // Definir variables
    private int id;
    private String fullname;
    private String address;
    private String telephone;
    private String email;
    private String updated;
    private String created;
    
    // Crear método constructor sin parámetros
    public Customers() {
    }
    
    // Método constructor con parámetros
    public Customers(int id, String fullname, String address, String telephone, String email, String updated, String created) {
        this.id = id;
        this.fullname = fullname;
        this.address = address;
        this.telephone = telephone;
        this.email = email;
        this.updated = updated;
        this.created = created;
    }
    
    // Generar Getter and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
    
}
