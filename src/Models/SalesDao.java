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

public class SalesDao {
    // Instanciar conexión
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    
    // Registrar venta
    public boolean registerSale(int customer_id, int employee_id, double total){
        String query = "INSERT INTO sales (sale_date, total, customer_id, employee_id) VALUES (?,?,?,?)";
        
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            
            pst.setTimestamp(1, datetime);
            pst.setDouble(2, total);
            pst.setInt(3, customer_id);
            pst.setInt(4, employee_id);
            
            pst.executeUpdate();
            
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al "
                    + "registrar la venta.");
            return false;
        }
    }
    
    // Registrar detalles de venta
    public boolean registerSaleDetails(int quantity, double price, double subtotal, int product_id, int sale_id){
        String query = "INSERT INTO sales_details (sale_quantity, sale_price, sale_subtotal, product_id, sale_id) "
                + "VALUES (?,?,?,?,?)";

        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            
            pst.setInt(1, quantity);
            pst.setDouble(2, price);
            pst.setDouble(3, subtotal);
            pst.setInt(4, product_id);
            pst.setInt(5, sale_id);
            
            pst.executeUpdate();
            
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al "
                    + "registrar detalles de venta.");
            return false;
        }
    }
    
    public List listSalesQuery(){
        List<Sales> salesList = new ArrayList();
        
        String query = "SELECT sa.id, sa.sale_date, sa.total, cust.fullname AS customer_name, emp.fullname AS employee_name "
                + "FROM sales sa INNER JOIN customers cust ON sa.customer_id = cust.id INNER JOIN employees emp ON "
                + "sa.employee_id = emp.id";
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            
            rs = pst.executeQuery();
            
            while(rs.next()){
                Sales sale = new Sales();
                
                sale.setSalesID(rs.getInt("id"));
                sale.setCreated(rs.getString("sale_date"));
                sale.setTotal(rs.getDouble("total"));
                sale.setCustomerName(rs.getString("customer_name"));
                sale.setEmployeeName(rs.getString("employee_name"));
                
                salesList.add(sale);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al "
                    + "listar ventas." + e);
        }
        return salesList;
    }
    
    public int lastSaleID(){
        int id = 0;
        // Para obtener el id máximo de la tabla purchases; este id se envía a
        // registerPurchaseDetailsQuery()
        String query = "SELECT MAX(id) AS id FROM sales";
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            
            rs = pst.executeQuery();
            
            if (rs.next()){
                id = rs.getInt("id");
            }
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return id;
    }
    
    public String getEmployeeName(int id){
        String query = "SELECT fullname FROM employees WHERE id = ?";
        String name = "";
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            
            pst.setInt(1, id);
            
            rs = pst.executeQuery();
            
            if (rs.next()){
                name = rs.getString("fullname");
            }
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return name;
    }
}
