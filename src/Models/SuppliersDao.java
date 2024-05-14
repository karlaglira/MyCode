
package Models;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

public class SuppliersDao {
    // Instanciar conexión
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    
    // Registrar proveedor
    public boolean registerSupplierQuery(Suppliers supplier){
        String query = "INSERT INTO suppliers (name, description, telephone, "
                + "address, email, city, created, updated) VALUES (?, ?, ?, ?, "
                + "?, ?, ?, ?)"; // El id no se pone porque es autoincremental
        
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            
            pst.setString(1, supplier.getName());
            pst.setString(2, supplier.getDescription());
            pst.setString(3, supplier.getTelephone());
            pst.setString(4, supplier.getAddress());
            pst.setString(5, supplier.getEmail());
            pst.setString(6, supplier.getCity());
            pst.setTimestamp(7, datetime);
            pst.setTimestamp(8, datetime);
            
            pst.executeUpdate();
            
            return true;
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al "
                    + "registrar al proveedor." + e);
            return false;
        }
    }
    
    // Modelo para listar proveedores
    public List listSuppliersQuery(String value){
        String query = "SELECT * FROM suppliers";
        String query_search_supplier = "SELECT * FROM suppliers WHERE name LIKE "
                + "'%" + value + "%'";
        
        List<Suppliers> list_suppliers = new ArrayList();
        
        try{
            conn = cn.getConnection();
  
            if(value.equalsIgnoreCase("")){
                pst = conn.prepareStatement(query);  
                rs = pst.executeQuery();
            }else{
                pst = conn.prepareStatement(query_search_supplier); 
                rs = pst.executeQuery();
            }
            
            while(rs.next()){
                Suppliers supplier = new Suppliers();
                supplier.setId(rs.getInt("id"));
                supplier.setName(rs.getString("name"));
                supplier.setDescription(rs.getString("description"));
                supplier.setTelephone(rs.getString("telephone"));
                supplier.setAddress(rs.getString("address"));
                supplier.setEmail(rs.getString("email"));
                supplier.setCity(rs.getString("city"));
                
                list_suppliers.add(supplier);
            }        
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return list_suppliers;
    }
    
    // Modificar proveedores
    public boolean updateSupplierQuery(Suppliers supplier){
        String query = "UPDATE suppliers SET name=?, description=?, telephone=?, "
                + "address=?, email=?, city=?, updated=? WHERE id = ?";
        
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            
            pst.setString(1,supplier.getName());
            pst.setString(2,supplier.getDescription());
            pst.setString(3,supplier.getTelephone());
            pst.setString(4,supplier.getAddress());
            pst.setString(5,supplier.getEmail());
            pst.setString(6,supplier.getCity());
            pst.setTimestamp(7, datetime);
            pst.setInt(8,supplier.getId());
            
            pst.executeUpdate();
            
            return true;
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "No se pudo "
                    + "actualizar los datos del proveedor" + e);
            return false;
        }
    }
    
    // Eliminar proveedor
    public boolean deleteSupplierQuery(String supplierName){
        String query = "DELETE FROM suppliers WHERE name = ?";
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            
            pst.setString(1, supplierName);
            
            pst.executeUpdate();
            
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "No se pudo "
                    + "eliminar al proveedor" + e);
            return false;
        }
    }
}
