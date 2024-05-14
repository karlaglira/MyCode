
package Controllers;

import Models.Categories;
import Models.CategoriesDao;
import Models.DynamicComboBox;
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

public class CategoriesController implements ActionListener, KeyListener, MouseListener{
    // Instanciar variables
    private Categories category;
    private CategoriesDao categoryDao;
    private SystemView views;
    
    DefaultTableModel model = new DefaultTableModel();

    // Constructor
    public CategoriesController(Categories category, CategoriesDao categoryDao, SystemView views) {
        this.category = category;
        this.categoryDao = categoryDao;
        this.views = views;
        
        views.btn_categories_register.addActionListener(this);
        views.btn_categories_update.addActionListener(this);
        views.btn_categories_delete.addActionListener(this);
        views.btn_categories_cancel.addActionListener(this);
        
        views.txt_categories_search.addKeyListener(this);
        
        views.categories_table.addMouseListener(this);
        views.jLabelCategories.addMouseListener(this);
        
        getCategoryName();
        
        // Para este se necesita la variable swingx-all-1.6.4.jar
        // Autocompleta el combobox con las opciones al escribir en el alguna letra
        AutoCompleteDecorator.decorate(views.cmb_product_category);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Registrar categoría
        if(e.getSource() == views.btn_categories_register){
            if(views.txt_categories_name.getText().equals("")){
                JOptionPane.showMessageDialog(null,"Se debe ingresar el nombre de la categoría");
            }else{
                category.setName(views.txt_categories_name.getText().trim());
                if(categoryDao.registerCategoryQuery(category)){
                    JOptionPane.showMessageDialog(null,"Se registró la categoría con éxito");
                    clearTable();
                    clearFields();
                    listAllCategories();
                }else{
                    JOptionPane.showMessageDialog(null,"Ocurrió un error al registrar categoría");
                }
            }
        }
        
        if(e.getSource() == views.btn_categories_update){
            if(views.btn_categories_register.isEnabled() == true){
                JOptionPane.showMessageDialog(null,"Selecciona una categoría de la lista para modificarla");
            }else{
                if(views.txt_categories_name.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"Todos los campos deben estar llenos");
                }else{
                    category.setId(Integer.parseInt(views.txt_categories_id.getText().trim()));
                    category.setName(views.txt_categories_name.getText().trim());
                    if (categoryDao.updateCategoryQuery(category)) {
                        JOptionPane.showMessageDialog(null, "Se modificó el nombre de la categoría con éxito");
                        clearTable();
                        clearFields();
                        listAllCategories();
                    } else {
                        JOptionPane.showMessageDialog(null,"Ocurrió un error al actualizar categoría");
                    }
                }
            }
        }
        
        if (e.getSource() == views.btn_categories_delete) {
            if (views.btn_categories_register.isEnabled() == true) {
                JOptionPane.showMessageDialog(null, "Selecciona una categoría de la lista para eliminarla");
            } else {
                if (views.txt_categories_name.equals("")) {
                    JOptionPane.showMessageDialog(null, "Todos los campos deben estar llenos");
                } else {
                    if (JOptionPane.showConfirmDialog(null, "¿Seguro que deseas eliminar esta categoría?",
                            "WARNING", JOptionPane.YES_NO_OPTION) == 0) {
                        int id_category = Integer.parseInt(views.txt_categories_id.getText().trim());
                        if (categoryDao.deleteCategoryQuery(id_category)) {
                            JOptionPane.showMessageDialog(null, "Se eliminó la categoría con éxito");
                            clearTable();
                            clearFields();
                            listAllCategories();
                        } else {
                            JOptionPane.showMessageDialog(null, "Ocurrió un error al eliminar categoría");
                        }
                    }
                }
            }
        }

        if(e.getSource() == views.btn_categories_cancel){
            clearFields();
        }
        
    }
    
    public void listAllCategories(){
        List<Categories> categories_list = categoryDao.listCategoriesQuery(views.txt_categories_search.getText());
        model = (DefaultTableModel) views.categories_table.getModel();
        Object[] row = new Object[model.getColumnCount()];
        for(Categories category_it : categories_list){
            row[0] = category_it.getId();
            row[1] = category_it.getName();
            
            model.addRow(row);
        }
        views.categories_table.setModel(model);
    }
    
    public void clearFields(){
        views.txt_categories_id.setText("");
        views.txt_categories_name.setText("");
        
        views.btn_categories_register.setEnabled(true);
    }
    
    public void clearTable(){
        for(int i=0; i<model.getRowCount(); i++){
            model.removeRow(i);
            i = i-1;
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
        if(e.getSource() == views.txt_categories_search){
            clearTable();
            listAllCategories();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == views.categories_table){
            int row = views.categories_table.rowAtPoint(e.getPoint());
            views.txt_categories_name.setText(String.valueOf(views.categories_table.getValueAt(row, 1)));
            views.txt_categories_id.setText(String.valueOf(views.categories_table.getValueAt(row, 0)));
            
            views.btn_categories_register.setEnabled(false);
        }
        
        if(e.getSource() == views.jLabelCategories){
            views.jTabbedPane1.setSelectedIndex(6);
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
    
    // Método para mostrar el nombre de las categorías en el ComboBox de Products
    public void getCategoryName(){
        List<Categories> list = categoryDao.listCategoriesQuery("");
        for(Categories cat : list){
            int id = cat.getId();
            String name = cat.getName();
            views.cmb_product_category.addItem(new DynamicComboBox(id,name));
        }
    }
}
