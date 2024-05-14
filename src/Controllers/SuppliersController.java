
package Controllers;

import Models.DynamicComboBox;
import static Models.EmployeesDao.rol_user;
import Models.Suppliers;
import Models.SuppliersDao;
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
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class SuppliersController implements ActionListener, KeyListener, MouseListener{
    // Instanciar variables
    private Suppliers supplier;
    private SuppliersDao supplierDao;
    private SystemView views;
    String rol = rol_user;
    
    DefaultTableModel model = new DefaultTableModel();
    
    // Constructor
    public SuppliersController(Suppliers supplier, SuppliersDao supplierDao, SystemView views) {
        this.supplier = supplier;
        this.supplierDao = supplierDao;
        this.views = views;
        
        views.btn_suppliers_register.addActionListener(this);
        views.btn_suppliers_update.addActionListener(this);
        views.btn_suppliers_delete.addActionListener(this);
        views.btn_suppliers_cancel.addActionListener(this);
        
        views.txt_suppliers_search.addKeyListener(this);
        
        views.suppliers_table.addMouseListener(this);
        views.jLabelSuppliers.addMouseListener(this);
        
        getSupplierName();
        // Para este se necesita la variable swingx-all-1.6.4.jar
        // Autocompleta el combobox con las opciones al escribir en el alguna letra
        AutoCompleteDecorator.decorate(views.cmb_purchase_supplier);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Registrar proveedor
        if(e.getSource() == views.btn_suppliers_register){
            if(views.txt_suppliers_name.getText().equals("") || 
                    views.txt_suppliers_address.getText().equals("") || views.txt_suppliers_phone.getText().equals("")
                    || views.txt_suppliers_email.getText().equals("") || views.txt_suppliers_description.getText().equals("")
                    || views.cmb_suppliers_city.getSelectedItem().equals("Seleccionar")){
                JOptionPane.showMessageDialog(null,"Se deben llenar todos los campos");
            }else{
                supplier.setName(views.txt_suppliers_name.getText());
                supplier.setTelephone(views.txt_suppliers_phone.getText());
                supplier.setAddress(views.txt_suppliers_address.getText());
                supplier.setDescription(views.txt_suppliers_description.getText());
                supplier.setEmail(views.txt_suppliers_email.getText());
                supplier.setCity(String.valueOf(views.cmb_suppliers_city.getSelectedItem()));
                if (supplierDao.registerSupplierQuery(supplier) == true) {
                    JOptionPane.showMessageDialog(null, "Proveedor registrado correctamente");
                    clearFields();
                    clearTable();
                    listAllSuppliers();
                }else{
                    JOptionPane.showMessageDialog(null, "Error al registrar proveedor");
                }
            }
        }
        
        // Modificar proveedor
        if(e.getSource() == views.btn_suppliers_update){
            if(views.btn_suppliers_register.isEnabled() == true){
                JOptionPane.showMessageDialog(null, "Se debe seleccionar un proveedor de la lista");
            } else if (views.txt_suppliers_name.getText().equals("")
                    || views.txt_suppliers_address.getText().equals("") || views.txt_suppliers_phone.getText().equals("")
                    || views.txt_suppliers_email.getText().equals("") || views.txt_suppliers_description.getText().equals("")
                    || views.cmb_suppliers_city.getSelectedItem().equals("Seleccionar")) {
                JOptionPane.showMessageDialog(null, "Se deben llenar todos los campos");
            } else {
                supplier.setName(views.txt_suppliers_name.getText());
                supplier.setTelephone(views.txt_suppliers_phone.getText());
                supplier.setAddress(views.txt_suppliers_address.getText());
                supplier.setDescription(views.txt_suppliers_description.getText());
                supplier.setEmail(views.txt_suppliers_email.getText());
                supplier.setCity(String.valueOf(views.cmb_suppliers_city.getSelectedItem()));
                supplier.setId(Integer.parseInt(views.txt_suppliers_id.getText()));
                if (supplierDao.updateSupplierQuery(supplier) == true) {
                    JOptionPane.showMessageDialog(null, "Proveedor modificado correctamente");
                    clearFields();
                    clearTable();
                    listAllSuppliers();
                }else{
                    JOptionPane.showMessageDialog(null, "Error al modificar datos del proveedor" + e);
                }
            }
        }
        
        // Eliminar proveedor
        if(e.getSource() == views.btn_suppliers_delete){
            if(views.btn_suppliers_register.isEnabled() == true){
                JOptionPane.showMessageDialog(null, "Se debe seleccionar un proveedor de la lista");
            } else if (views.txt_suppliers_name.getText().equals("")
                    || views.txt_suppliers_address.getText().equals("") || views.txt_suppliers_phone.getText().equals("")
                    || views.txt_suppliers_email.getText().equals("") || views.txt_suppliers_description.getText().equals("")
                    || views.cmb_suppliers_city.getSelectedItem().equals("Seleccionar")) {
                JOptionPane.showMessageDialog(null, "Todos los campos deben estar llenos");
            } else {
                if(JOptionPane.showConfirmDialog(null,"¿Seguro que deseas eliminar este cliente?",
                                "WARNING", JOptionPane.YES_NO_OPTION) == 0){
                    String name = views.txt_suppliers_name.getText();
                    if (supplierDao.deleteSupplierQuery(name) == true) {
                        JOptionPane.showMessageDialog(null, "Proveedor eliminado correctamente");
                        clearFields();
                        clearTable();
                        listAllSuppliers();
                    }else{
                        JOptionPane.showMessageDialog(null, "Error al eliminar proveedor" + e);
                    }
                }
            }
        }
        
        // Cancelar
        if(e.getSource() == views.btn_suppliers_cancel){
            clearFields();
        }
    }
    
    // Listar todos los proveedores
    public void listAllSuppliers(){
        List<Suppliers> listSuppliers = supplierDao.listSuppliersQuery(views.txt_suppliers_search.getText());
        model = (DefaultTableModel) views.suppliers_table.getModel();
        Object[] row = new Object[model.getColumnCount()];
        for(Suppliers supplier : listSuppliers){
            row[0] = supplier.getId();
            row[1] = supplier.getName();
            row[2] = supplier.getDescription();
            row[3] = supplier.getAddress();
            row[4] = supplier.getTelephone();
            row[5] = supplier.getEmail();
            row[6] = supplier.getCity();
            
            model.addRow(row);
        }
        views.suppliers_table.setModel(model);
    }
    
    // Limpiar text fields
    public void clearFields(){
        views.txt_suppliers_id.setText("");
        views.txt_suppliers_name.setText("");
        views.txt_suppliers_phone.setText("");
        views.txt_suppliers_address.setText("");
        views.txt_suppliers_description.setText("");
        views.txt_suppliers_email.setText("");
        views.cmb_suppliers_city.setSelectedItem("Seleccionar");
        
        views.btn_suppliers_register.setEnabled(true);
    }
    
    // Limpiar tabla
    public void clearTable(){
        for (int i=0; i<model.getRowCount(); i++){
            model.removeRow(i);
            i=i-1;
        }
    }
    
    // Guardar en el combobox de Purchase, los proveedores de Suppliers
    public void getSupplierName(){
        List<Suppliers> listSup = supplierDao.listSuppliersQuery(views.txt_suppliers_search.getText());
        for(Suppliers sup : listSup){
            int id= sup.getId();
            String name = sup.getName();
            views.cmb_purchase_supplier.addItem(new DynamicComboBox(id,name));
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
        if(e.getSource() == views.txt_suppliers_search){
            clearTable();
            listAllSuppliers();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == views.suppliers_table){
            int row = views.suppliers_table.rowAtPoint(e.getPoint());
            views.txt_suppliers_id.setText(String.valueOf(model.getValueAt(row, 0))); 
            views.txt_suppliers_name.setText(String.valueOf(model.getValueAt(row, 1)));   
            views.txt_suppliers_description.setText(String.valueOf(model.getValueAt(row, 2))); 
            views.txt_suppliers_address.setText(String.valueOf(model.getValueAt(row, 3))); 
            views.txt_suppliers_phone.setText(String.valueOf(model.getValueAt(row, 4))); 
            views.txt_suppliers_email.setText(String.valueOf(model.getValueAt(row, 5))); 
            views.cmb_suppliers_city.setSelectedItem(String.valueOf(model.getValueAt(row, 6))); 
            
            views.btn_suppliers_register.setEnabled(false);
        }
        
        if(e.getSource() == views.jLabelSuppliers){
            views.jTabbedPane1.setSelectedIndex(5);
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
