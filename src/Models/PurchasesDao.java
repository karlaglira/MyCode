
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

public class PurchasesDao {
    // Instanciar conexión
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    
    // Registrar compra 
    public boolean registerPurchaseQuery(int supplier_id, int employee_id, double total){
        String query = "INSERT INTO purchases (total, created, supplier_id,"
                + "employee_id) VALUES (?, ?, ?, ?)";
        
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            
            pst.setDouble(1, total);
            pst.setTimestamp(2, datetime);
            pst.setInt(3, supplier_id);
            pst.setInt(4, employee_id);
            
            pst.executeUpdate();
            
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al "
                    + "registrar la compra.");
            return false;
        }
    }
    
    // Registrar detalles de compra
    public boolean registerPurchaseDetailsQuery(int purchase_amount,
            double purchase_price, double purchase_subtotal, int purchase_id,
            int product_id){
        String query = "INSERT INTO purchases_details (purchase_price, purchase_amount, "
                + "purchase_subtotal, purchase_id, product_id) VALUES (?, ?, ?, ?, ?)";
        
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            
            pst.setDouble(1, purchase_price);
            pst.setInt(2, purchase_amount);
            pst.setDouble(3, purchase_subtotal);
            pst.setInt(4, purchase_id);
            pst.setInt(5, product_id);
            //pst.setTimestamp(6, datetime);
            
            pst.executeUpdate();
            
            return true;
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al "
                    + "registrar detalles de compra." + e);
            return false;
        }
    }
    
    // Obtener id de la compra
    public int purchaseId(){
        int id = 0;
        // Para obtener el id máximo de la tabla purchases; este id se envía a
        // registerPurchaseDetailsQuery()
        String query = "SELECT MAX(id) AS id FROM purchases";
        
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
    
    // Listar todas las compras realizadas
    public List listPurchasesQuery(){
        // No tiene parámetros porque todo el tiempo está esta lista, no necesita
        // variables introducidas por el usuario
        
        List<Purchases> list_purchases = new ArrayList();
        
        String query = "SELECT pur.*, sup.name AS supplier_name FROM purchases pur, suppliers sup "
                + "WHERE pur.supplier_id = sup.id ORDER BY pur.created ASC"; // que lo ordene por fecha de compra en orden asc
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            
            rs = pst.executeQuery();
            
            while(rs.next()){
                Purchases purchase = new Purchases();
                purchase.setId(rs.getInt("id"));
                purchase.setTotal(rs.getDouble("total"));
                purchase.setCreated(rs.getString("created"));
                purchase.setSupplier_name(rs.getString("supplier_name"));
                
                list_purchases.add(purchase);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        
        return list_purchases;
    }
    
    // Listar compras para imprimir factura
    public List listPurchasesDetailsQuery(int id){
        List<Purchases> list_purchases_details = new ArrayList();
        
            String query = "SELECT pur.created, pur_det.purchase_price, "
                    + "pur_det.purchase_amount, pur_det.purchase_subtotal, " +
                    "sup.name AS supplier_name, pro.name AS product_name, "
                    + "em.fullname AS employee_fullname FROM purchases pur "
                    + "INNER JOIN purchases_details pur_det ON pur.id = pur_det.purchase_id "
                    + "INNER JOIN suppliers sup ON pur.supplier_id = sup.id INNER JOIN employees em " +
                    "ON pur.employee_id = em.id INNER JOIN products pro ON "
                    + "pur_det.product_id = pro.id WHERE pur.id = ?"; // que lo ordene por id de compra en orden asc
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            
            pst.setInt(1,id);
            
            rs = pst.executeQuery();
            
            while(rs.next()){               
                Purchases purchase = new Purchases();
                
                purchase.setCreated(rs.getString("created"));
                purchase.setPurchase_price(rs.getDouble("purchase_price"));
                purchase.setPurchase_amount(rs.getInt("purchase_amount"));
                purchase.setPurchase_subtotal(rs.getDouble("purchase_subtotal"));
                purchase.setSupplier_name(rs.getString("supplier_name"));
                purchase.setName(rs.getString("product_name"));
                purchase.setPurchaser(rs.getString("employee_fullname"));                              
                
                list_purchases_details.add(purchase);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
        
        return list_purchases_details;
    }
}
