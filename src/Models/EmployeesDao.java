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

// En esta clase van los métodos que permiten a Java interactuar con MySQL

public class EmployeesDao {
    
    // Instanciar conexión
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    
    // Variables para conectarnos con la base de datos
    PreparedStatement pst; // Para consultas
    ResultSet rs; // Para obtener resultado de consulta
    
    // Crear variables que nos permitan enviar datos entre interfaces (con el public static)
    public static int id_user = 0;
    public static String fullname_user = "";
    public static String username_user = "";
    public static String address_user = "";
    public static String rol_user = "";
    public static String email_user = "";
    public static String telephone_user = "";
    
    
    // Crear método de login de tipo Employees, recibe dos parámetros
    public Employees loginQuery(String user, String password){       
        // Crear variable para pasar a la base de datos la consulta que necesitamos
        String query = "SELECT * FROM employees WHERE username = ? AND password = ?";       
        // Nos traerá TODOS los campos de la tabla employees (en la base de datos)
        // relacionados con el usuario y password que se ingresen
       
        // Instanciar clase Employees (que ocuparé en este método)
        Employees employee = new Employees();
    
        // Try and catch para capturar posibles errores que se puedan
        // generar al ingresar a la base de datos
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
        
            // Asignar valores de query a través de variables ingresadas por el usuario
            pst.setString(1,user);
            pst.setString(2, password);
            
            // Ejecutar consulta
            rs = pst.executeQuery();
            
            // Condicional para ver si los datos de user y password coinciden
            // con alguno en la base de datos
            if (rs.next()){
                // En la variable id del método employee se guarda lo que se
                // obtiene del result set (resultado de la consulta)
                employee.setId(rs.getInt("id"));
                // Guardar id en variable local
                id_user = employee.getId(); 
                employee.setFullname(rs.getString("fullname"));
                fullname_user = employee.getFullname();
                employee.setUsername(rs.getString("username"));
                username_user = employee.getUsername();
                employee.setAddress(rs.getString("address"));
                address_user = employee.getAddress();
                employee.setTelephone(rs.getString("telephone"));
                telephone_user = employee.getTelephone();
                employee.setEmail(rs.getString("email"));
                email_user = employee.getEmail();
                employee.setRol(rs.getString("rol"));
                rol_user = employee.getRol();
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Error al obtener el empleado " + e);
        }
        return employee;
    }
   
    // Crear método para registrar empleados de tipo boolean (regresa true o false)
    public boolean registerEmployeeQuery(Employees employee){
        String query = "INSERT INTO employees (id, fullname, username, address,"
                + "telephone, email, password, rol, created, updated) VALUES (?,"
                + "?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        // Crear variable tipo TimeStamp para created y updated; nos da el tiempo real
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        // Try and catch
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            // Asignar variables ingresadas por usuario a los ? del query
            pst.setInt(1,employee.getId());
            pst.setString(2, employee.getFullname());
            pst.setString(3, employee.getUsername());
            pst.setString(4, employee.getAddress());
            pst.setString(5, employee.getTelephone());
            pst.setString(6, employee.getEmail());
            pst.setString(7, employee.getPassword());
            pst.setString(8, employee.getRol());
            pst.setTimestamp(9, datetime);
            pst.setTimestamp(10, datetime);
            
            pst.executeUpdate();
            
            return true;
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al "
                    + "registrar al empleado" + e);
            
            return false;
        }
    }
    
    // Crear método para listar empleados
    public List listEmployeesQuery(String value){       
        // Query para mostrar todos los empleados
        String query = "SELECT * FROM employees ORDER BY id ASC";
        
        // Query para mostrar el empleado que busca el usuario
        String query_search_employee = "SELECT * FROM employees WHERE id LIKE '%" + value + "%'";
        
        // Crear variable de tipo List del objeto Employees
        List<Employees> list_employees = new ArrayList();
        
        try{
            conn = cn.getConnection();
            
            if(value.equalsIgnoreCase("")){
                pst = conn.prepareStatement(query);
                rs = pst.executeQuery();
            }else{
                pst = conn.prepareStatement(query_search_employee);
                rs = pst.executeQuery();
            }
            
            // Se recorre la respuesta de la consulta mientras haya registro
            // para guardar la info en variable employee de tipo Employees
            while(rs.next()){
                Employees employee = new Employees();
                employee.setId(rs.getInt("id"));
                employee.setFullname(rs.getString("fullname"));
                employee.setUsername(rs.getString("username"));
                employee.setAddress(rs.getString("address"));
                employee.setTelephone(rs.getString("telephone"));
                employee.setEmail(rs.getString("email"));
                employee.setRol(rs.getString("rol"));
                
                // Agregar este employee a la lista de employees
                list_employees.add(employee);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.toString());
        }
        return list_employees;
    }
    
    // Crear método para modificar datos del empleado del id que se ingrese
    public boolean updateEmployeeQuery(Employees employee){
        boolean bandera_id;
        // String para guardar los datos si se encuentra el id
        String query_all = "SELECT * from employees WHERE id = ?";
        // Strings para modificar por variable
        String query_fullname = "UPDATE employees SET fullname = ?, updated = ? WHERE id = ?";
        String query_username = "UPDATE employees SET username = ?, updated = ? WHERE id = ?";
        String query_address = "UPDATE employees SET address = ?, updated = ? WHERE id = ?";
        String query_telephone = "UPDATE employees SET telephone = ?, updated = ? WHERE id = ?";
        String query_email = "UPDATE employees SET email = ?, updated = ? WHERE id = ?";
        String query_rol = "UPDATE employees SET rol = ?, updated = ? WHERE id = ?";
        
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try{
            conn = cn.getConnection();
            
            // Ejecutar query para obtener los campos del id ingresado (si se encuentra)
            pst = conn.prepareStatement(query_all);
            pst.setInt(1, employee.getId());
            rs = pst.executeQuery();
            
            // Si se encuentra el id:
            if(rs.next()){
                // Validar las variables que se modificaron (son diferenes de vacío) y modificar 
                // únicamente estas
                // Dar valor true a la bandera; que sí se encontró el id
                bandera_id = true;
                if(!employee.getFullname().equals("")){
                    pst = conn.prepareStatement(query_fullname);
                    pst.setString(1, employee.getFullname());
                    pst.setTimestamp(2, datetime);
                    pst.setInt(3, employee.getId());
                    pst.executeUpdate();
                }if(!employee.getUsername().equals("")){
                    pst = conn.prepareStatement(query_username);
                    pst.setString(1, employee.getUsername());
                    pst.setTimestamp(2, datetime);
                    pst.setInt(3, employee.getId());
                    pst.executeUpdate();
                }if(!employee.getAddress().equals("")){
                    pst = conn.prepareStatement(query_address);
                    pst.setString(1, employee.getAddress());
                    pst.setTimestamp(2, datetime);
                    pst.setInt(3, employee.getId());
                    pst.executeUpdate();
                }if(!employee.getTelephone().equals("")){
                    pst = conn.prepareStatement(query_telephone);
                    pst.setString(1, employee.getTelephone());
                    pst.setTimestamp(2, datetime);
                    pst.setInt(3, employee.getId());
                    pst.executeUpdate();
                }if(!employee.getEmail().equals("")){
                    pst = conn.prepareStatement(query_email);
                    pst.setString(1, employee.getEmail());
                    pst.setTimestamp(2, datetime);
                    pst.setInt(3, employee.getId());
                    pst.executeUpdate();
                }if(!employee.getRol().equals("")){
                    pst = conn.prepareStatement(query_rol);
                    pst.setString(1, employee.getRol());
                    pst.setTimestamp(2, datetime);
                    pst.setInt(3, employee.getId());
                    pst.executeUpdate();
                }
            }else{
                bandera_id = false;
                JOptionPane.showMessageDialog(null, "No se encontró "
                        + "usuario con ese id asignado. \n Intenta ingresando otro.");
            }
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al "
                    + "modificar los datos del empleado" + e);
            bandera_id = false;
        }
        
        return bandera_id;
    }
    
    // Eliminar empleado
    public boolean deleteEmployeeQuery(int id){
        String query = "DELETE FROM employees WHERE id = " + id;
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            
            pst.executeUpdate();
            
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "No se puede eliminar"
                    + "un empleado que tenga relación con otra tabla" + e);
            
            return false;
        }
    }
    
    // Cambiar contraseña
    public boolean updateEmployeePassword(String new_password){
        String query = "UPDATE employees SET password = ? WHERE username = '" + 
                username_user + "'";
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            
            pst.setString(1, new_password);
            
            pst.executeUpdate();
            
            return true;
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al "
                    + "modificar la contraseña " + e);
            
            return false;
        }
    }
}
