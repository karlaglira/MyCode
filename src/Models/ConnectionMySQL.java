
package Models;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMySQL {
    // Definir e inicializar variables referentes a base de datos
    private String database_name = "pharmacy_database"; // Variable que tendrá la base de datos
    private String user = "root";
    private String password = "Valentina0606";
    private String url = "jdbc:mysql://localhost:3306/" + database_name;
    Connection conn = null; // Conexión
    
    
    // Método para conectar Java con My SQL
    public Connection getConnection(){
        // Crear try and catch para obtener excepciones al generar conexión
        try{
            Class.forName("com.mysql.cj.jdbc.Driver"); // Para obtener valor del Driver
            conn = DriverManager.getConnection(url,user,password); // Obtener la conexión
        }catch(ClassNotFoundException e){
            System.err.println("Ha ocurrido un Class Not Found Exception: " + 
                e.getMessage());
        }catch(SQLException e){
            System.err.println("Ha ocurrido un SQL Exception" + e.getMessage());
        }
        return conn;
    }
}
