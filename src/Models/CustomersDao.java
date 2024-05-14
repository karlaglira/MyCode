
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

public class CustomersDao {
    
    // Instanciar conexión
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    
    // Método para registrar clientes
    public boolean registerCustomerQuery(Customers customer){
        String query = "INSERT INTO customers (id, fullname, address, telephone,"
                + " email, created, updated) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            
            pst.setInt(1, customer.getId());
            pst.setString(2, customer.getFullname());
            pst.setString(3,customer.getAddress());
            pst.setString(4,customer.getTelephone());
            pst.setString(5,customer.getEmail());
            pst.setTimestamp(6, datetime);
            pst.setTimestamp(7, datetime);
            
            pst.executeUpdate();
            
            return true;
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al "
                    + "registrar cliente");
            
            return false;
        }       
    }
    
    // Método para listar clientes
    public List listCustomerQuery(String value){
        
        String query = "SELECT * FROM customers ORDER BY id ASC";
        String query_search_customer = "SELECT * FROM customers WHERE id LIKE '%" + value + "%'";
        
        List<Customers> list_customers = new ArrayList();
        
        try{
            conn = cn.getConnection();
            
            if (value.equalsIgnoreCase("")){
                pst = conn.prepareStatement(query);
                rs = pst.executeQuery();
     
            }else{
                pst = conn.prepareStatement(query_search_customer); 
                rs = pst.executeQuery();
            } 
            
            while(rs.next()){
                Customers customer = new Customers();
                customer.setId(rs.getInt("id"));
                customer.setFullname(rs.getString("fullname"));
                customer.setAddress(rs.getString("address"));
                customer.setTelephone(rs.getString("telephone"));
                customer.setEmail(rs.getString("email"));
                
                list_customers.add(customer);
            }
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.toString());
        }
        
        return list_customers;
    }
    
    // Método modificar cliente
    public boolean updateCustomerQuery(Customers customer){
        
        String query = "UPDATE customers SET fullname = ?, address = ?, telephone = ?,"
                + " email = ?, updated = ? WHERE id = ?";
        
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            
            pst.setString(1, customer.getFullname());
            pst.setString(2, customer.getAddress());
            pst.setString(3, customer.getTelephone());
            pst.setString(4, customer.getEmail());
            pst.setTimestamp(5, datetime);
            pst.setInt(6, customer.getId());
            
            pst.executeUpdate();
            
            return true;
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al "
                    + "modificar los datos del cliente");
            return false;
        }
    }
    
    // Método eliminar cliente
    public boolean deleteCustomerQuery(int id){
        String query = "DELETE FROM customers WHERE id = ?";
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            
            pst.setInt(1,id);
            
            pst.executeUpdate();
            
            return true; 
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al "
                    + "eliminar cliente " + e);
            return false;
        }
    }
    
    public String getCustomerName(int id){
        String query = "SELECT cust.fullname FROM customers cust WHERE id = ?";
        String name = "";
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            
            pst.setInt(1,id);
            
            rs = pst.executeQuery();
            
            if(rs.next()){
                name = rs.getString("fullname");
            }
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al "
                    + "obtener nombre de cliene " + e);
        }
        
        return name;
    }
    
    public int getCustomerID(String name){
        String query = "SELECT cust.id FROM customers cust WHERE fullname = ?";
        int id=0;
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            
            pst.setString(1,name);
            
            rs = pst.executeQuery();
            
            if(rs.next()){
                id = rs.getInt("id");
            }
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al "
                    + "obtener id de cliene " + e);
        }
        
        return id;
    }
}
