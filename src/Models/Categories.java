
package Models;

public class Categories {
    
    // Definir variables
    private int id;
    private String name;
    private String created;
    private String updated;
    
    // Método constructor sin parámetros
    public Categories() {
    }
    
    // Método constructor con parámetros
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }
    
}
