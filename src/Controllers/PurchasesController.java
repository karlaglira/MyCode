package Controllers;

import Models.DynamicComboBox;
import static Models.EmployeesDao.id_user;
import Models.Products;
import Models.ProductsDao;
import Models.Purchases;
import Models.PurchasesDao;
import Views.Print;
import Views.SystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class PurchasesController implements KeyListener, ActionListener, MouseListener {

    // Instanciar variables
    private Purchases purchase;
    private PurchasesDao purchaseDao;
    private SystemView views;
    DefaultTableModel model = new DefaultTableModel();
    private int numID = 0;
    private int supplierID=0;
    private int purchaseID = 0;
    private double total=0;
    DefaultTableModel listaCompras = new DefaultTableModel();
    DecimalFormat formatea = new DecimalFormat("###,###.##");

    // Instanciar modelo productos
    Products product = new Products();
    ProductsDao productDao = new ProductsDao();

    // Constructor con parámetros
    public PurchasesController(Purchases purchase, PurchasesDao purchaseDao, SystemView views) {
        this.purchase = purchase;
        this.purchaseDao = purchaseDao;
        this.views = views;

        views.btn_purchase_add_product.addActionListener(this);
        views.btn_purchase_confirm.addActionListener(this);
        views.btn_purchase_remove.addActionListener(this);
        views.btn_purchase_new.addActionListener(this);

        views.txt_purchase_product_code.addKeyListener(this);
        views.txt_purchase_amount.addKeyListener(this);
        views.txt_purchase_price.addKeyListener(this);

        views.purchase_table.addMouseListener(this);
        views.jLabelPurchases.addMouseListener(this);
        
        listPurchases();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Botón agregar para agregar productos (que se van a comprar) a la lista
        // Sólo se pueden agregar productos de UN proveedor
        if (e.getSource() == views.btn_purchase_add_product) {
            if (views.txt_purchase_product_code.getText().equals("")
                    || views.txt_purchase_amount.getText().equals("")
                    || views.txt_purchase_subtotal.getText().equals("")
                    || views.txt_purchase_price.getText().equals("")
                    || views.cmb_purchase_supplier.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "Todos los campos deben estar llenos");
            } else {
                // Para validar que se agreguen elementos del mismo proveedor
                DynamicComboBox supplier_purchases = (DynamicComboBox) views.cmb_purchase_supplier.getSelectedItem();
                supplierID = supplier_purchases.getId();
                
                // Se guarda el id del proveedor si la variable numID está en cero (reseteada o no se ha
                // guardado nada en ella)
                if (numID == 0) {
                    numID = supplierID;

                    listaCompras = (DefaultTableModel) views.purchase_table.getModel();

                    Object[] row = new Object[listaCompras.getColumnCount()];
                    row[0] = views.txt_purchase_id.getText().trim();
                    row[1] = views.txt_purchase_product_name.getText().trim();
                    row[2] = views.txt_purchase_amount.getText().trim();
                    row[3] = views.txt_purchase_price.getText().trim();
                    row[4] = views.txt_purchase_subtotal.getText().trim();
                    row[5] = supplier_purchases.toString();
                    listaCompras.addRow(row);
                    views.purchase_table.setModel(listaCompras);
                    
                    double subtotal = Double.parseDouble(views.txt_purchase_subtotal.getText().trim());
                    views.txt_purchase_total_to_pay.setText("");
                    total = total + subtotal;
                    views.txt_purchase_total_to_pay.setText(String.valueOf(total));

                    clearFields();
                    //views.cmb_purchase_supplier.setEditable(false);
                    views.txt_purchase_product_code.requestFocus();
                } else {
                    // Si el usuario quiere agregar un producto de otro proveedor, manda un error
                    if (numID != supplierID) {
                        JOptionPane.showMessageDialog(null, "Sólo se pueden comprar productos de un "
                                + "proveedor a la vez");
                    } else {
                        // Validar si al agregar producto, la cantidad es mayor a cero
                        if (Integer.parseInt(views.txt_purchase_amount.getText().trim()) > 0) {
                            // Si la cantidad de producto es mayor a cero, se valida si este producto ya está en la lista
                            // (recorriendo la lista y viendo si el nombre del producto agregado por el usuario, ya está en la lista)
                            // Y se le pregunta si desea agregar de nuevo este producto
                            int exit = 0; // Bandera para comprobar si el programa entró a la condición de "producto ya presente en la lista"
                            int noOption = 0; // Bandera para comprobar si el usuario presionó la opción no en el "deseas agregar de nuevo este producto?"
                            String productName = String.valueOf(views.txt_purchase_product_name.getText().trim());
                            for (int i = 0; i < views.purchase_table.getRowCount() && exit == 0; i++) {
                                if (views.purchase_table.getValueAt(i, 1).equals(productName)) {
                                    if (JOptionPane.showConfirmDialog(null, "Este producto ya está en la tabla de compras.\n"
                                            + "¿Deseas agregarlo nuevamente?", "MESSAGE", JOptionPane.YES_NO_OPTION) == 0) {
                                        
                                        listaCompras = (DefaultTableModel) views.purchase_table.getModel();

                                        Object[] row = new Object[listaCompras.getColumnCount()];
                                        row[0] = views.txt_purchase_id.getText().trim();
                                        row[1] = views.txt_purchase_product_name.getText().trim();
                                        row[2] = views.txt_purchase_amount.getText().trim();
                                        row[3] = views.txt_purchase_price.getText().trim();
                                        row[4] = views.txt_purchase_subtotal.getText().trim();
                                        row[5] = supplier_purchases.toString();
                                        listaCompras.addRow(row);
                                        views.purchase_table.setModel(listaCompras);
                                        
                                        // suma del total a pagar
                                        double subtotal = Double.parseDouble(views.txt_purchase_subtotal.getText().trim());
                                        views.txt_purchase_total_to_pay.setText("");
                                        total = total + subtotal;
                                        views.txt_purchase_total_to_pay.setText(String.valueOf(total));

                                        clearFields();
                                        //views.cmb_purchase_supplier.setEditable(false);
                                        views.txt_purchase_product_code.requestFocus();

                                        // Para salir del ciclo for la primera vez que encuentra el mismo producto en la lista
                                        exit = 1;
                                    } else {
                                        // Si el usuario presiona no, se cambia esta variable a 1 para que ya no entre en la siguiente condición
                                        noOption = 1;
                                    }
                                }
                            }
                            // Comprobar si el programa no encontró en la tabla ya el mismo producto ni se presionó la opción de NO
                            // al preguntar si seguro que quería agregar el mismo producto
                            if (exit == 0 && noOption == 0) {
                                listaCompras = (DefaultTableModel) views.purchase_table.getModel();

                                Object[] row = new Object[listaCompras.getColumnCount()];
                                row[0] = views.txt_purchase_id.getText().trim();
                                row[1] = views.txt_purchase_product_name.getText().trim();
                                row[2] = views.txt_purchase_amount.getText().trim();
                                row[3] = views.txt_purchase_price.getText().trim();
                                row[4] = views.txt_purchase_subtotal.getText().trim();
                                row[5] = supplier_purchases.toString();
                                listaCompras.addRow(row);
                                views.purchase_table.setModel(listaCompras);
                                
                                // Suma del total a pagar
                                double subtotal = Double.parseDouble(views.txt_purchase_subtotal.getText().trim());
                                views.txt_purchase_total_to_pay.setText("");
                                total = total + subtotal;
                                views.txt_purchase_total_to_pay.setText(String.valueOf(total));

                                clearFields();
                                //views.cmb_purchase_supplier.setEditable(false);
                                views.txt_purchase_product_code.requestFocus();
                            }
                        }
                    }
                }
            }
        }

        // Botón comprar
        if (e.getSource() == views.btn_purchase_confirm) {
            insertPurchase();
            views.txt_purchase_total_to_pay.setText("");
            numID = 0;
         
            // Limpiar lista de reporte de compras para actualizarla de nuevo al hacer una compra
            model = (DefaultTableModel) views.table_all_purchases.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                model.removeRow(i);
                i = i - 1;
            }
            views.purchase_table.setModel(model);
            //listPurchases();
            Print print = new Print(purchaseID);
            print.setVisible(true);
        }

        // Botón eliminar (para eliminar productos de la lista o del carrito)
        if (e.getSource() == views.btn_purchase_remove) {
            if(views.purchase_table.getSelectedRow() == -1){
                JOptionPane.showMessageDialog(null, "Selecciona un producto de la lista para eliminar");
            }else{
                listaCompras = (DefaultTableModel) views.purchase_table.getModel();
                listaCompras.removeRow(views.purchase_table.getSelectedRow());
                views.purchase_table.setModel(listaCompras);
                
                // Restarlo al total a pagar
                double subtotal = Double.parseDouble(views.txt_purchase_subtotal.getText().trim());
                views.txt_purchase_total_to_pay.setText("");
                total = total - subtotal;
                views.txt_purchase_total_to_pay.setText(String.valueOf(total));
                JOptionPane.showMessageDialog(null, "Se eliminó el producto de la lista");
                clearFields();
            }
        }

        // Si se presiona botón Nuevo
        if (e.getSource() == views.btn_purchase_new) {
            clearFields();
            clearTable();
            views.txt_purchase_total_to_pay.setText("");
            total = 0;
        }
    }
    
    public void listPurchases(){
        List<Purchases> listPurchasesReport = purchaseDao.listPurchasesQuery();
        model = (DefaultTableModel) views.table_all_purchases.getModel();
        for(Purchases purchaseTemp : listPurchasesReport){
            Object[] row = new Object[4];
            row[0] = purchaseTemp.getId();
            row[1] = purchaseTemp.getSupplier_name();
            row[2] = formatea.format(purchaseTemp.getTotal());
            row[3] = purchaseTemp.getCreated();
            model.addRow(row);
        }
        views.table_all_purchases.setModel(model);
    }
    
    public void insertPurchase(){
        int employeeId = id_user;
        
        // Se registra en la base de datos los datos generales de la compra
        if(purchaseDao.registerPurchaseQuery(supplierID, employeeId, total)){
            purchaseID = purchaseDao.purchaseId();
            // Se registra en la base de datos los detalles de la compra general (los campos de todos productos de la tabla)
            for(int i=0; i<views.purchase_table.getRowCount();i++){
                int productID = Integer.parseInt( views.purchase_table.getValueAt(i, 0).toString());
                int amount = Integer.parseInt( views.purchase_table.getValueAt(i, 2).toString());
                double price = Double.parseDouble(views.purchase_table.getValueAt(i, 3).toString());
                double subtotal = Double.parseDouble(views.purchase_table.getValueAt(i, 4).toString());
                purchaseDao.registerPurchaseDetailsQuery(amount, price, subtotal, purchaseID, productID);
                productDao.updateStockQuery(amount, productID);
            }
            clearTable();
            JOptionPane.showMessageDialog(null, "Compra generada con éxito");
            clearFields();
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
        // Rellenar automáticamente id y name al ingresar un código del producto
        if (e.getSource() == views.txt_purchase_product_code) {
            // Para limpiar los campos de id y name cada que se teclea algo
            views.txt_purchase_id.setText("");
            views.txt_purchase_product_name.setText("");
            // Guardar el código ingresado en la variable code
            int code = Integer.parseInt(views.txt_purchase_product_code.getText());
            // Llamar la función searchCode del modelo productDao enviándole el code
            product = productDao.searchCode(code);
            // Que sólo se llenen los campos id y name si coincide el code con uno de la base de datos
            if (product.getName() != null) {
                if (!product.getName().equals("")) {
                    views.txt_purchase_id.setText(String.valueOf(product.getId()));
                    views.txt_purchase_product_name.setText(product.getName());
                    // Para que el cursor se ponga en amount
                    views.txt_purchase_amount.requestFocus();
                }
            }
        }

        if (e.getSource() == views.txt_purchase_amount) {
            // Cuando se de ENTER en la casilla de amount, se redireccione automáticamente a la casilla de precio
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (!views.txt_purchase_amount.getText().equals("")) {
                    views.txt_purchase_price.requestFocus();
                } else {
                    JOptionPane.showMessageDialog(null, "Ingresa la cantidad del producto");
                }
            }
        }

        // Cuando se teclee en la casilla precio, se rellena automáticamente la casilla de subtotal y total
        // calculándolos multiplicando amount*price y subtotal*1.16
        if (e.getSource() == views.txt_purchase_price) {
            views.txt_purchase_subtotal.setText("");

            Double amount = Double.parseDouble(views.txt_purchase_amount.getText());
            Double price = Double.parseDouble(views.txt_purchase_price.getText());
            Double subtotal = amount * price;
            views.txt_purchase_subtotal.setText(String.valueOf(subtotal));
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                views.btn_purchase_add_product.doClick();
            }
        }
    }

    public void clearFields() {
        views.txt_purchase_id.setText("");
        views.txt_purchase_amount.setText("");
        views.txt_purchase_product_code.setText("");
        views.txt_purchase_product_name.setText("");
        views.txt_purchase_price.setText("");
        views.txt_purchase_subtotal.setText("");
        //views.txt_purchase_total_to_pay.setText("");
        views.cmb_purchase_supplier.setSelectedItem("");
    }

    public void clearTable() {
        for (int i = 0; i < listaCompras.getRowCount(); i++) {
            listaCompras.removeRow(i);
            i = i - 1;
        }
        views.purchase_table.setModel(listaCompras);
    }

    @Override
    // Para que cuando se seleccione una fila de la tabla, se rellenen los campos
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.purchase_table) {
            int row = views.purchase_table.rowAtPoint(e.getPoint());
            views.txt_purchase_id.setText(String.valueOf(views.purchase_table.getValueAt(row, 0)));
            views.txt_purchase_product_name.setText(String.valueOf(views.purchase_table.getValueAt(row, 1)));
            views.txt_purchase_amount.setText(String.valueOf(views.purchase_table.getValueAt(row, 2)));
            views.txt_purchase_price.setText(String.valueOf(views.purchase_table.getValueAt(row, 3)));
            views.txt_purchase_subtotal.setText(String.valueOf(views.purchase_table.getValueAt(row, 4)));
            views.cmb_purchase_supplier.setSelectedItem(String.valueOf(views.purchase_table.getValueAt(row, 5)));
        }
        
        if(e.getSource() == views.jLabelPurchases){
            views.jTabbedPane1.setSelectedIndex(1);
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
