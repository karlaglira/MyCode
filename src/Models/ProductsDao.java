
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

public class ProductsDao {
    
    // Instanciar conexión
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    
    // Registrar producto
    public boolean registerProductQuery(Products product){
        // Sin product_quantity porque para ese se utilizará otro procedimiento
        String query = "INSERT INTO products (code, name, description,"
                + "unit_price, created, updated, category_id) VALUES "
                + "(?, ?, ?, ?, ?, ?, ?)";
        
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            
            pst.setInt(1, product.getCode());
            pst.setString(2, product.getName());
            pst.setString(3, product.getDescription());
            pst.setDouble(4, product.getUnit_price());
            pst.setTimestamp(5, datetime);
            pst.setTimestamp(6, datetime);
            pst.setInt(7, product.getCategory_id());
            
            pst.executeUpdate();
            
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al "
                    + "registrar el producto." + e);
            return false;
        }
    }
    
    // Listar productos
    public List listProductsQuery(String value){
        List<Products> list_products = new ArrayList();
        
        // Este sí me trae el id de categorías
        String query = "SELECT pro.*, ca.name AS category_name FROM products pro, "
                + "categories ca WHERE pro.category_id = ca.id;";
        
        // No traerá el id sino el nombre. Trae todos los campos de productos y
        // el campo nombre de categorías nombrado como category_name para que no
        // se llame igual que el name de productos; de la tabla productos 
        // nombrada con el alias pro, INNER JOIN categories alias ca, donde (se 
        // nombra la relación entre las dos tablas) la variable category_id de 
        // productos es igual a la variable id de categorias; donde la variable
        // name de la tabla productos sea igual a lo introducido por el usuario.       
        String query_search_products = "SELECT pro.* , ca.name AS category_name "
                + "FROM products pro INNER JOIN categories ca ON pro.category_id "
                + "= ca.id WHERE pro.name LIKE '" + value + "%'";
        
        try{
            conn = cn.getConnection();
            
            if (value.equalsIgnoreCase("")){
                pst = conn.prepareStatement(query);
                rs = pst.executeQuery();
            }else{
                pst = conn.prepareStatement(query_search_products);
                rs = pst.executeQuery();
            }
            
            while(rs.next()){
                Products product = new Products();
                product.setId(rs.getInt("id"));
                product.setCode(rs.getInt("code"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setUnit_price(rs.getDouble("unit_price"));
                product.setProduct_quantity(rs.getInt("product_quantity"));
                product.setCategory_name(rs.getString("category_name"));
                
                list_products.add(product);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.toString());
        }
        
        return list_products;
    }
    
    // Modificar producto
    public boolean updateProductQuery(Products product){
        String query = "UPDATE products SET code=?, name=?, description=?, unit_price=?, updated=?, category_id=? WHERE id = ?";
        
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            
            pst.setInt(1,product.getCode());
            pst.setString(2,product.getName());
            pst.setString(3,product.getDescription());
            pst.setDouble(4,product.getUnit_price());
            pst.setTimestamp(5,datetime);
            pst.setInt(6,product.getCategory_id());
            pst.setInt(7,product.getId());
            
            pst.executeUpdate();
            
            return true;
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al "
                    + "actualizar los datos del producto" + e);
            return false;
        }
    }
    
    // Eliminar producto
    public boolean deleteProductQuery(int id){
        String query = "DELETE FROM products WHERE id = " + id;
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            
            pst.executeUpdate();
            
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "No se pudo "
                    + "eliminar el producto");
            return false;
        }
    }
    
    // Buscar producto
    public Products searchProduct(int id){
        // Para que al seleccionar del id de un producto de la lista, se 
        // autorellenen todos los campos 
        
        String query = "SELECT pro.*, ca.name AS category_name FROM products pro "
                + "INNER JOIN categories ca ON pro.category_id = ca.id WHERE "
                + "pro.id = ?";
        
        Products product = new Products();
        
        // Es casi igual que listar pero aquí no se pone while porque sólo es 1
        // Lo que se selecciona es puntual
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            
            pst.setInt(1, id);
            
            rs = pst.executeQuery();
            
            if(rs.next()){
                product.setId(rs.getInt("id"));
                product.setCode(rs.getInt("code"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setUnit_price(rs.getDouble("unit_price"));
                product.setCategory_id(rs.getInt("category_id"));
                product.setCategory_name(rs.getString("category_name"));
            }
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return product;
    }
    
    // Buscar producto por código
    // Para que al escribir el código del producto en una nueva compra, se rellene
    // automáticamente el nombre del producto y  el id
    public Products searchCode(int code){
        
        // No se necesita lo de la conexión entre tablas porque en esta no se 
        // utilizan variables compartidas, solo nombre y id de productos
        String query = "SELECT pro.id, pro.name, pro.unit_price, pro.product_quantity FROM products pro WHERE pro.code = ?";
        // También se pudo haber escrito SELECT * FROM products
        // WHERE code = ?; para quenos traiga todos los campos
        
        Products product = new Products();
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            
            pst.setInt(1, code);
            
            rs = pst.executeQuery();
            
            if(rs.next()){
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setUnit_price(rs.getDouble("unit_price"));
                product.setProduct_quantity(rs.getInt("product_quantity"));
            }
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return product;
    } 
    
    // Devuelve cantidad actual de productos
    public Products searchId (int id){
        String query = "SELECT pro.product_quantity FROM products pro WHERE"
                + "pro.id = ?";
        
        Products product = new Products();
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            
            pst.setInt(1, id);
            
            rs = pst.executeQuery();
            
            if(rs.next()){
                product.setProduct_quantity(rs.getInt("product_quantity"));
            }
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return product;
    }
    
    // Registrar cantidad de producto (actualizar stock; cada que se compra o 
    // vende un producto)
     public boolean updateStockQuery (int amount, int product_id){
         String querySelect = "SELECT product_quantity FROM products WHERE id=?";
         String query = "UPDATE products SET product_quantity = ? WHERE id = ?";
        
        try{
            conn = cn.getConnection();
            
            pst = conn.prepareStatement(querySelect);
            pst.setInt(1, product_id);
            rs = pst.executeQuery();
            
            if(rs.next()){
                int amountStock = rs.getInt("product_quantity");
                int total = amountStock + amount;
                pst = conn.prepareStatement(query);
                pst.setInt(1, total);
                pst.setInt(2, product_id);
                pst.executeUpdate();
            }

            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        }
     }
}
