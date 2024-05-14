
package Controllers;

import Models.Customers;
import Models.CustomersDao;
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

public class CustomersController implements ActionListener, KeyListener, MouseListener{
    // Instanciar variables a utilizar
    private Customers customer;
    private CustomersDao customerDao;
    private SystemView views;
    
    DefaultTableModel model = new DefaultTableModel();
    
    // Constructor con parámetros
    public CustomersController(Customers customer, CustomersDao customerDao, SystemView views){
        this.customer = customer;
        this.customerDao = customerDao;
        this.views = views;
        
        // Poner en escucha variables botones
        views.btn_customer_register.addActionListener(this);
        views.btn_customer_update.addActionListener(this);
        views.btn_customer_delete.addActionListener(this);
        views.btn_customer_cancel.addActionListener(this);
        
        // Poner en escucha del teclado
        views.txt_customer_search.addKeyListener(this);
        
        // Poner en escucha del mouse 
        views.customers_table.addMouseListener(this);
        views.jLabelCustomers.addMouseListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Registrar cliente
        if(e.getSource() == views.btn_customer_register){
            if(views.txt_customer_id.getText().equals("") || views.txt_customer_fullname.getText().equals("") 
                    || views.txt_customer_address.equals("") || views.txt_customer_phone.equals("") || 
                    views.txt_customer_email.equals("")){
                JOptionPane.showMessageDialog(null,"Se deben llenar todos los campos");
            }else{
                if(ifInteger() == true){
                    customer.setId(Integer.parseInt(views.txt_customer_id.getText().trim()));
                    customer.setFullname(views.txt_customer_fullname.getText().trim());
                    customer.setAddress(views.txt_customer_address.getText().trim());
                    customer.setTelephone(views.txt_customer_phone.getText().trim());
                    customer.setEmail(views.txt_customer_email.getText().trim());
                    if(customerDao.registerCustomerQuery(customer) == true){
                        JOptionPane.showMessageDialog(null,"El cliente se registró correctamente");
                        cleanFields();
                        cleanTable();
                        listAllCustomers();
                    }else{
                      JOptionPane.showMessageDialog(null,"Error al registrar al cliente");
                    }
                }else if(ifInteger() == false){
                    JOptionPane.showMessageDialog(null,"El formato del id es incorrecto");
                }
            }
        }
        
        // Modificar cliente
        if(e.getSource() == views.btn_customer_update){
            if(views.txt_customer_id.isEditable() == true){
                JOptionPane.showMessageDialog(null,"Se debe seleccionar un usuario de la lista");
            }else{
                if(views.txt_customer_id.getText().equals("") || views.txt_customer_fullname.getText().equals("")
                        || views.txt_customer_address.getText().equals("") || views.txt_customer_phone.getText().equals("")
                        || views.txt_customer_email.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"Todos los campos son obligatorios");
                }else{
                    customer.setId(Integer.parseInt(views.txt_customer_id.getText().trim()));
                    customer.setFullname(views.txt_customer_fullname.getText().trim());
                    customer.setAddress(views.txt_customer_address.getText().trim());
                    customer.setTelephone(views.txt_customer_phone.getText().trim());
                    customer.setEmail(views.txt_customer_email.getText().trim());
                    if(customerDao.updateCustomerQuery(customer) == true){
                    JOptionPane.showMessageDialog(null,"Los datos del cliente se actualizaron correctamente");
                    cleanFields();
                    cleanTable();
                    listAllCustomers();
                    }else{
                        JOptionPane.showMessageDialog(null,"Error al actualizar datos del cliente");
                    }
                }
            }
        }
        
        // Eliminar cliente
        if(e.getSource() == views.btn_customer_delete){
            // Checar si el text de id está en editable, porque si sí, quiere decir que no se dio click en la lista
            if(views.txt_customer_id.isEditable() == true){
                JOptionPane.showMessageDialog(null,"Se debe seleccionar un usuario de la lista");
            }else{
                if(views.txt_customer_id.getText().equals("") || views.txt_customer_fullname.getText().equals("")
                        || views.txt_customer_address.getText().equals("") || views.txt_customer_phone.getText().equals("")
                        || views.txt_customer_email.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"Todos los campos son obligatorios");
                }else{
                    // Confirmar si el usuario desea eliminar el cliente
                    if(JOptionPane.showConfirmDialog(null,"¿Seguro que deseas eliminar este cliente?",
                                "WARNING", JOptionPane.YES_NO_OPTION) == 0){
                        int id = Integer.parseInt(views.txt_customer_id.getText().trim());
                        if(customerDao.deleteCustomerQuery(id)){
                            JOptionPane.showMessageDialog(null,"Cliente eliminado con éxito");
                            cleanFields();
                            cleanTable();
                            listAllCustomers();
                        }else{
                            JOptionPane.showMessageDialog(null,"Error al eliminar cliente " + e);
                        }
                    }
                }
            }
        }
        
        // Cancelar
        if(e.getSource() == views.btn_customer_cancel){
            cleanFields();
        }
    }
    
    // Validar que se intoduzca un entero
    public boolean ifInteger(){
        boolean integer;
        try{
            Integer.parseInt(views.txt_customer_id.getText().trim());
            integer = true;
        }catch(NumberFormatException exception){
            integer = false;
        }
        return integer;
    }
    
    public void cleanFields(){
        views.txt_customer_id.setText("");
        views.txt_customer_fullname.setText("");
        views.txt_customer_address.setText("");
        views.txt_customer_phone.setText("");
        views.txt_customer_email.setText("");
        
        views.txt_customer_id.setEditable(true);
        views.btn_customer_register.setEnabled(true);
    }
    
    public void cleanTable(){
        for(int i=0; i< model.getRowCount(); i++){
            model.removeRow(i);
            i = i-1;
        }
    }
    
    public void listAllCustomers(){
        List<Customers> list_customer = customerDao.listCustomerQuery(views.txt_customer_search.getText());
        model = (DefaultTableModel) views.customers_table.getModel();
        Object[] row = new Object[model.getColumnCount()];
        for(Customers customer_it : list_customer){
            row[0] = customer_it.getId();
            row[1] = customer_it.getFullname();
            row[2] = customer_it.getTelephone();
            row[3] = customer_it.getAddress();
            row[4] = customer_it.getEmail();
            
            model.addRow(row);
        }
        views.customers_table.setModel(model);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getSource() == views.txt_customer_search){
            cleanTable();
            listAllCustomers();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == views.customers_table){
            int row = views.customers_table.rowAtPoint(e.getPoint());
            views.txt_customer_id.setText(String.valueOf(views.customers_table.getValueAt(row,0)));
            views.txt_customer_fullname.setText(String.valueOf(views.customers_table.getValueAt(row,1)));
            views.txt_customer_phone.setText(String.valueOf(views.customers_table.getValueAt(row,2)));
            views.txt_customer_address.setText(String.valueOf(views.customers_table.getValueAt(row,3)));
            views.txt_customer_email.setText(String.valueOf(views.customers_table.getValueAt(row,4)));
            
            views.txt_customer_id.setEditable(false);
            views.btn_customer_register.setEnabled(false);
        }
        
        if(e.getSource() == views.jLabelCustomers){
            views.jTabbedPane1.setSelectedIndex(3);
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
}
