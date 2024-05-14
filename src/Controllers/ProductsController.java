package Controllers;

import Models.Categories;
import Models.DynamicComboBox;
import Models.Products;
import Models.ProductsDao;
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

public class ProductsController implements ActionListener, KeyListener, MouseListener {

    // Instanciar variables privadas
    private Products product;
    private ProductsDao productDao;
    private SystemView views;

    DefaultTableModel model = new DefaultTableModel();

    // Constructor con parámetros
    public ProductsController(Products product, ProductsDao productDao, SystemView views) {
        this.product = product;
        this.productDao = productDao;
        this.views = views;

        views.btn_register_product.addActionListener(this);
        views.btn_update_product.addActionListener(this);
        views.btn_delete_product.addActionListener(this);
        views.btn_cancel_product.addActionListener(this);

        views.txt_search_product.addKeyListener(this);

        views.products_table.addMouseListener(this);
        views.jLabelProducts.addMouseListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Registrar producto
        if (e.getSource() == views.btn_register_product) {
            if (views.txt_product_code.getText().equals("") || views.txt_product_name.getText().equals("")
                    || views.txt_product_unit_price.getText().equals("") || views.txt_product_description.getText().equals("")
                    || views.cmb_product_category.getSelectedItem().toString().equals("")) {
                JOptionPane.showMessageDialog(null, "Se deben llenar todos los campos");
            } else {
                product.setName(views.txt_product_name.getText().trim());
                product.setCode(Integer.parseInt(views.txt_product_code.getText()));
                product.setUnit_price(Double.parseDouble(views.txt_product_unit_price.getText().trim()));
                product.setDescription(views.txt_product_description.getText());
                DynamicComboBox category_id = (DynamicComboBox) views.cmb_product_category.getSelectedItem();
                product.setCategory_id(category_id.getId());
                if (productDao.registerProductQuery(product) == true) {
                    JOptionPane.showMessageDialog(null, "El producto se registró satisfactoriamente");
                    cleanFields();
                    cleanTable();
                    listAllProducts();
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo registrar el producto");
                }
            }
        }

        // Modificar producto
        if (e.getSource() == views.btn_update_product) {
            if (views.btn_register_product.isEnabled() == true) {
                JOptionPane.showMessageDialog(null, "Selecciona un producto de la lista");
            } else {
                if (views.txt_product_code.getText().equals("") || views.txt_product_name.getText().equals("")
                        || views.txt_product_unit_price.getText().equals("") || views.txt_product_description.getText().equals("")
                        || views.cmb_product_category.getSelectedItem().equals("")) {
                    JOptionPane.showMessageDialog(null, "Todos los campos deben estar llenos");
                } else {
                    product.setCode(Integer.parseInt(views.txt_product_code.getText()));
                    product.setName(views.txt_product_name.getText());
                    product.setDescription(views.txt_product_description.getText());
                    product.setUnit_price(Double.parseDouble(views.txt_product_unit_price.getText()));
                    product.setId(Integer.parseInt(views.txt_product_id.getText()));
                    DynamicComboBox category_prod = (DynamicComboBox) views.cmb_product_category.getSelectedItem();
                    product.setCategory_id(category_prod.getId());
                    if (productDao.updateProductQuery(product)) {
                        JOptionPane.showMessageDialog(null, "Se actualizó el producto con éxito");
                        cleanFields();
                        cleanTable();
                        listAllProducts();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al actualizar datos de producto");
                    }
                }
            }
        }

        // Eliminar producto
        if (e.getSource() == views.btn_delete_product) {
            if (views.btn_register_product.isEnabled() == true) {
                JOptionPane.showMessageDialog(null, "Selecciona un producto de la lista");
            } else {
                if (views.txt_product_code.getText().equals("") || views.txt_product_name.getText().equals("")
                        || views.txt_product_unit_price.getText().equals("") || views.txt_product_description.getText().equals("")
                        || views.cmb_product_category.getSelectedItem().equals("")) {
                    JOptionPane.showMessageDialog(null, "Todos los campos deben estar llenos");
                } else {
                    if (JOptionPane.showConfirmDialog(null, "¿Seguro que deseas eliminar el producto?", "WARNING!",
                            JOptionPane.YES_NO_OPTION) == 0) {
                        int id = Integer.parseInt(views.txt_product_id.getText());
                        if (productDao.deleteProductQuery(id)) {
                            JOptionPane.showMessageDialog(null, "Se eliminó el producto con éxito");
                            cleanFields();
                            cleanTable();
                            listAllProducts();
                        } else {
                            JOptionPane.showMessageDialog(null, "Error al eliminar producto");
                        }
                    }
                }
            }
        }

            // Cancelar
            if (e.getSource() == views.btn_cancel_product) {
                cleanFields();
            }
        }
    
    

    public void cleanFields() {
        views.txt_product_name.setText("");
        views.txt_product_code.setText("");
        views.txt_product_unit_price.setText("");
        views.txt_product_description.setText("");
        views.cmb_product_category.setSelectedItem("Seleccionar");

        views.txt_product_code.setEditable(true);
        views.btn_register_product.setEnabled(true);
    }

    public void cleanTable() {
        model = (DefaultTableModel) views.products_table.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    public void listAllProducts() {
        List<Products> list_products = productDao.listProductsQuery(views.txt_search_product.getText());
        model = (DefaultTableModel) views.products_table.getModel();
        Object[] row = new Object[model.getColumnCount()];
        for (Products product : list_products) {
            row[0] = product.getId();
            row[1] = product.getCode();
            row[2] = product.getName();
            row[3] = product.getDescription();
            row[4] = product.getUnit_price();
            row[5] = product.getProduct_quantity();
            row[6] = product.getCategory_name();

            model.addRow(row);
        }
        views.suppliers_table.setModel(model);
    }

    // Método para mostrar el nombre de las categorías
    public void getCategoryName() {
        List<Categories> list = productDao.listProductsQuery(views.txt_search_product.getText());
        for (Categories category_it : list) {
            int id = category_it.getId();
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
        if (e.getSource() == views.txt_search_product) {
            cleanTable();
            listAllProducts();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.products_table) {
            int row = views.products_table.getSelectedRow();
            views.txt_product_id.setText(String.valueOf(model.getValueAt(row, 0)));
            views.txt_product_code.setText(String.valueOf(model.getValueAt(row, 1)));
            views.txt_product_name.setText(String.valueOf(model.getValueAt(row, 2)));
            views.txt_product_description.setText(String.valueOf(model.getValueAt(row, 3)));
            views.txt_product_unit_price.setText(String.valueOf(model.getValueAt(row, 4)));
            views.cmb_product_category.setSelectedItem(model.getValueAt(row, 6));

            views.txt_product_code.setEditable(false);
            views.btn_register_product.setEnabled(false);
        }

        if (e.getSource() == views.jLabelProducts) {
            views.jTabbedPane1.setSelectedIndex(0);
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
