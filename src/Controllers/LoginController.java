package Controllers;

import Models.Employees;
import Models.EmployeesDao;
import Views.LoginView;
import Views.SystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;

public class LoginController implements ActionListener, KeyListener{

    // Encapsular variables
    private Employees employee;
    private EmployeesDao employees_dao;
    private LoginView login_view;

    // Crear constructor con parámetros
    public LoginController(Employees employee, EmployeesDao employees_dao, LoginView login_view) {
        this.employee = employee;
        this.employees_dao = employees_dao;
        this.login_view = login_view;
        // Para estar a la escucha del botón ingresar
        this.login_view.btn_enter.addActionListener(this);
        this.login_view.btn_enter.addKeyListener(this);
    }

    // Implementar lógica del método de ActionListener
    @Override
    public void actionPerformed(ActionEvent e) {
        // Para obtener los datos introducidos por el usuario en campos user y 
        // password en la vista de Login
        String user = login_view.txt_username.getText().trim();
        // El trim es para descartar los espacios
        String psw = String.valueOf(login_view.txt_password.getPassword());

        // Para verificar si el usuario presionó el botón ingresar
        if (e.getSource() == login_view.btn_enter) {
            // Para validar que los campos no estén vacíos
            if (!user.equals("") || !psw.equals("")) {
                // Se pasan los parámetros al método login y se guarda variable 
                // tipo Employee devuelta del método LoginQuery en employee
                employee = employees_dao.loginQuery(user, psw);
                // Verificar existencia del usuario
                if (employee.getUsername() != null) {
                    // Se verifica el tipo de usuario que ingresa: admin o aux
                    if (employee.getRol().equalsIgnoreCase("Administrador")) {
                        SystemView admin = new SystemView();
                        admin.setVisible(true);
                    } else {
                        SystemView aux = new SystemView();
                        aux.setVisible(true);
                    }
                    // En este caso se abre la misma ventana de SystemView porque
                    // No creamos dos ventanas distintas para cada tipo de usuario
                    // pero es para mostrar cómo se haría

                    this.login_view.dispose(); // para cerrar ventana login
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario "
                            + "o contraseña incorrectos");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Los campos "
                        + "están vacíos");
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

}
