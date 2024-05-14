package Controllers;

import Models.Employees;
import Models.EmployeesDao;
import static Models.EmployeesDao.address_user;
import static Models.EmployeesDao.email_user;
import static Models.EmployeesDao.fullname_user;
import static Models.EmployeesDao.id_user;
import static Models.EmployeesDao.rol_user;
import static Models.EmployeesDao.telephone_user;
import Views.SystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class EmployeesController implements ActionListener, MouseListener, KeyListener{
    
    // Instanciar variables
    private Employees employee;
    private EmployeesDao employees_dao;
    private SystemView s_view;
    // Rol
    String rol = rol_user;
    // Modelo para tabla
    DefaultTableModel model = new DefaultTableModel();

    // Constructor con parámetros
    public EmployeesController(Employees employee, EmployeesDao employees_dao, SystemView s_view){
        this.employee = employee;
        this.employees_dao = employees_dao;
        this.s_view = s_view;
        
        // Poner en escucha botones registrar, modificar, borrar, cancelar y buscar
        this.s_view.btn_employee_register.addActionListener(this);
        this.s_view.btn_employee_update.addActionListener(this);
        this.s_view.btn_employee_delete.addActionListener(this);
        this.s_view.btn_employee_cancel.addActionListener(this);
        this.s_view.btn_profile_update.addActionListener(this);
        
        // Poner en escucha la tabla
        this.s_view.employees_table.addMouseListener(this);
        this.s_view.jLabelEmployees.addMouseListener(this);
        
        // Poner en escucha el text field de buscar
        this.s_view.txt_employee_search.addKeyListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        // Registrar empleado
        // Comprobar que se presionó el botón registrar
        if(e.getSource() == s_view.btn_employee_register){
            // Se verifica que todos los campos estén llenos
            if(s_view.txt_employee_id.getText().equals("") || 
               s_view.txt_employee_fullname.getText().equals("") || 
               s_view.txt_employee_username.getText().equals("") || 
               s_view.cmb_roll.getSelectedItem().equals("Seleccionar") || 
               s_view.txt_employee_address.getText().equals("") || 
               s_view.txt_employee_phone.getText().equals("") || 
               s_view.txt_employee_email.getText().equals("") || 
               String.valueOf(s_view.txt_employee_password.getPassword()).equals("")) {
                JOptionPane.showMessageDialog(null, "Se deben llenar todos los campos");
            }else{
                if(ifInteger() == true){
                    employee.setId(Integer.parseInt(s_view.txt_employee_id.getText().trim()));
                    employee.setFullname(s_view.txt_employee_fullname.getText().trim());
                    employee.setUsername(s_view.txt_employee_username.getText().trim());
                    employee.setRol((String)s_view.cmb_roll.getSelectedItem());
                    employee.setAddress(s_view.txt_employee_address.getText().trim());
                    employee.setTelephone(s_view.txt_employee_phone.getText().trim());
                    employee.setEmail(s_view.txt_employee_email.getText().trim());
                    employee.setPassword(String.valueOf(s_view.txt_employee_password.getPassword()));               
                    // Condicional para ver si todo salió bien
                    if(employees_dao.registerEmployeeQuery(employee)){
                        JOptionPane.showMessageDialog(null, "Empleado registrado con éxito");
                        // Una vez que se registre, se borra el texto de todos los campos.
                        cleanFields();
                        cleanTable();
                        listAllEmployees(); 
                    }else{
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar usuario");
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "El formato del id es incorrecto");
                }
            }
        }
        
        
        // Modificar empleados
        // Comprobar que se presionó el botón Modify
        if(e.getSource() == s_view.btn_employee_update){
            // Verificar que se ingrese id
            if (s_view.txt_employee_id.isEditable() == true) {
                JOptionPane.showMessageDialog(null, "Se debe seleccionar un empleado de la lista");
            } else if(s_view.txt_employee_id.getText().equals("") || 
               s_view.txt_employee_fullname.getText().equals("") || 
               s_view.txt_employee_username.getText().equals("") || 
               s_view.cmb_roll.getSelectedItem().equals("Seleccionar") || 
               s_view.txt_employee_address.getText().equals("") || 
               s_view.txt_employee_phone.getText().equals("") || 
               s_view.txt_employee_email.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Todos los campos deben estar llenos");
            }else{
                employee.setId(Integer.parseInt(s_view.txt_employee_id.getText().trim()));
                employee.setFullname(s_view.txt_employee_fullname.getText().trim());
                employee.setUsername(s_view.txt_employee_username.getText().trim());
                employee.setAddress(s_view.txt_employee_address.getText().trim());
                employee.setTelephone(s_view.txt_employee_phone.getText().trim());
                employee.setEmail(s_view.txt_employee_email.getText().trim());
                employee.setRol(String.valueOf(s_view.cmb_roll.getSelectedItem()));

                // Si todo sale bien, realizar método employees_dao
                if (employees_dao.updateEmployeeQuery(employee) == true) {
                    JOptionPane.showMessageDialog(null, "Datos de empleado modificados con éxito");
                    // Una vez que se registre, se borra el texto de todos los campos.
                    cleanFields();
                    cleanTable();
                    listAllEmployees();
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar datos de usuario");
                }
            }
        }
    
        
        // Eliminar empleado
        if(e.getSource() == s_view.btn_employee_delete){
            int row = s_view.employees_table.getSelectedRow();
            if(row == -1){
                JOptionPane.showMessageDialog(null, "Se debe seleccionar un empleado de la lista para eliminar");
            // Validar que el usuario no se eliga a él mismo
            }else if(s_view.employees_table.getValueAt(row,0).equals(id_user)){
                JOptionPane.showMessageDialog(null, "No te puedes eliminar a ti mismo");
            }else{
                if(JOptionPane.showConfirmDialog(null, 
                        "¿Estás seguro que deseas eliminar este usuario?", "WARNING", 
                        JOptionPane.YES_NO_OPTION) == 0){
                    if(employees_dao.deleteEmployeeQuery(Integer.parseInt(s_view.txt_employee_id.getText()))){
                        JOptionPane.showMessageDialog(null, "Empleado eliminado con éxito");
                        cleanFields();
                        s_view.txt_employee_id.setEditable(true);
                        s_view.txt_employee_password.setEditable(true);
                        s_view.btn_employee_register.setEnabled(true);
                        cleanTable();
                        listAllEmployees(); 
                    }else{
                        JOptionPane.showMessageDialog(null, "Error al eliminar el empleado");
                    }
                }
            }
        }
        
        // Función de cancelar
        if(e.getSource() == s_view.btn_employee_cancel){
            s_view.txt_employee_id.setEditable(true);
            s_view.txt_employee_password.setEditable(true);
            s_view.btn_employee_register.setEnabled(true);
            cleanFields();
            cleanTable();
            listAllEmployees(); 
        }
        
        // Modificar contraseña de usuario
        if(e.getSource() == s_view.btn_profile_update){
            String new_password = String.valueOf(s_view.txt_profile_new_psw.getPassword()); 
            String confirm_password = String.valueOf(s_view.txt_profile_confirm_psw.getPassword()); 
            if(new_password.equals("")){
                JOptionPane.showMessageDialog(null, "Se debe ingresar la contraseña nueva");
            }else if(confirm_password.equals("")){
                JOptionPane.showMessageDialog(null, "Se debe confirmar la nueva contraseña");
            }else{
                if(new_password.equals(confirm_password)){
                    if(employees_dao.updateEmployeePassword(new_password) == true){
                        JOptionPane.showMessageDialog(null, "Se actualizó correctamente la contraseña");
                        s_view.txt_profile_new_psw.setText("");
                        s_view.txt_profile_confirm_psw.setText("");
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden");
                }
            }
        }
    }  
    
    // Listar todos los empleados (Sin búsqueda)
    public void listAllEmployees(){
        // Empleados sólo se listan si el usuario tiene el rol de admin
        if(rol.equals("Administrador")){
            // Guardar la lista de employees que se devuelve de listEmployeesQuery en variable employees_list
            List<Employees> employees_list = employees_dao.listEmployeesQuery(s_view.txt_employee_search.getText());
            // Crear modelo acorde al modelo de nuestra tabla
            model = (DefaultTableModel) s_view.employees_table.getModel();
            // Crear objeto (fila con el número de columnas de la tabla)
            Object[] row = new Object[model.getColumnCount()];
            // Recorrer el arreglo employees_list, cada fila se va guardando en employee_it y este lo vamos agregando a la tabla
            for(Employees employee_it : employees_list){
                row[0] = employee_it.getId();
                row[1] = employee_it.getFullname();
                row[2] = employee_it.getUsername();
                row[3] = employee_it.getAddress();
                row[4] = employee_it.getTelephone();
                row[5] = employee_it.getEmail();
                row[6] = employee_it.getRol();
                
                model.addRow(row);
            } 
            s_view.employees_table.setModel(model);
        } 
    }
    
    // Checar si es enter
    public boolean ifInteger(){
        try{
            Integer.parseInt(s_view.txt_employee_id.getText().trim());
            return true;
        }catch(NumberFormatException exception){
            return false;
        }
    }
    
    // Mostrar perfil
    public void showProfile(){
        s_view.txt_profile_id.setText(String.valueOf(id_user));
        s_view.txt_profile_fullname.setText(fullname_user);
        s_view.txt_profile_address.setText(address_user);
        s_view.txt_profile_phone.setText(telephone_user);
        s_view.txt_profile_email.setText(email_user);
        
        s_view.txt_profile_id.setEditable(false);
        s_view.txt_profile_fullname.setEditable(false);
        s_view.txt_profile_address.setEditable(false);
        s_view.txt_profile_phone.setEditable(false);
        s_view.txt_profile_email.setEditable(false);
        
        // Limitar pestaña Empleados si no eres Admin
        if(rol_user.equals("Auxiliar")){
            s_view.jTabbedPane1.setEnabledAt(4,false);
            s_view.jLabelEmployees.setEnabled(false);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == s_view.employees_table){
            // Para obtener en qué fila se dio click
            int row = s_view.employees_table.rowAtPoint(e.getPoint());
            s_view.txt_employee_id.setText(String.valueOf( s_view.employees_table.getValueAt(row,0)));
            s_view.txt_employee_fullname.setText(String.valueOf(s_view.employees_table.getValueAt(row,1)));
            s_view.txt_employee_username.setText(String.valueOf(s_view.employees_table.getValueAt(row,2)));
            s_view.txt_employee_address.setText(String.valueOf(s_view.employees_table.getValueAt(row,3)));
            s_view.txt_employee_phone.setText(String.valueOf(s_view.employees_table.getValueAt(row,4)));  
            s_view.txt_employee_email.setText(String.valueOf(s_view.employees_table.getValueAt(row,5)));
            s_view.cmb_roll.setSelectedItem(String.valueOf(s_view.employees_table.getValueAt(row,6)));
            
            s_view.txt_employee_id.setEditable(false);
            s_view.txt_employee_password.setEditable(false);
            s_view.btn_employee_register.setEnabled(false);
        }
        if(e.getSource() == s_view.jLabelEmployees){
            if(rol_user.equals("Auxiliar")){
                JOptionPane.showMessageDialog(null, "No tienes acceso a esta pestaña");
            }else{
                s_view.jTabbedPane1.setSelectedIndex(4);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
      
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getSource() == s_view.txt_employee_search){
            cleanTable();
            listAllEmployees();
        }
        
    }
    
    public void cleanTable(){
        for(int i=0; i<model.getRowCount(); i++){
            model.removeRow(i);
            i=i-1;
        }
    }
    
    public void cleanFields(){
        s_view.txt_employee_id.setText("");
        s_view.txt_employee_fullname.setText("");
        s_view.txt_employee_username.setText("");
        s_view.cmb_roll.setSelectedItem("Seleccionar");
        s_view.txt_employee_address.setText("");
        s_view.txt_employee_phone.setText("");
        s_view.txt_employee_email.setText("");
        s_view.txt_employee_password.setText("");
        
        s_view.txt_employee_id.setEditable(true);
        s_view.txt_employee_password.setEditable(true);
        s_view.btn_employee_register.setEnabled(true);
    }
}