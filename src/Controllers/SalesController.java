package Controllers;

import Models.Customers;
import Models.CustomersDao;
import static Models.EmployeesDao.fullname_user;
import static Models.EmployeesDao.id_user;
import Models.Products;
import Models.ProductsDao;
import Models.Sales;
import Models.SalesDao;
import Views.SystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class SalesController implements ActionListener, KeyListener, MouseListener{
     
    // Instanciar variables
    private Sales sale;
    private SalesDao saleDao;
    private SystemView views;
    
    // Instanciar variables de otras clases que necesitamos
    private Products infoProduct;
    private CustomersDao customerDao = new CustomersDao();
    private Customers customer;
    
    // Otras variables
    private String customerName = "";
    private int numID = 0;
    
    DefaultTableModel model = new DefaultTableModel();
    
    // Constructor con parámetros
    public SalesController(Sales sale, SalesDao saleDao, SystemView views) {
        this.sale = sale;
        this.saleDao = saleDao;
        this.views = views;
        
        // Poner en escucha a botones
        views.btn_sale_add_product.addActionListener(this);
        views.btn_sale_confirm.addActionListener(this);
        views.btn_sale_new.addActionListener(this);
        views.btn_sale_remove.addActionListener(this);
        
        // Poner en escucha de teclado a TextFields
        views.txt_sale_product_code.addKeyListener(this);
        views.txt_sale_quantity.addKeyListener(this);
        views.txt_sale_customer_id.addKeyListener(this);
        
        // Poner en escucha de clicks a la tabla
        views.sales_table.addMouseListener(this);
        views.jLabelReports.addMouseListener(this);
        
        listSalesReport();
    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == views.btn_sale_add_product){
            int customerID = Integer.parseInt(String.valueOf(views.txt_sale_customer_id.getText().trim()));
            if(numID == 0){
                numID = customerID;
                views.txt_sale_total_to_pay.setText(views.txt_sale_subtotal.getText());
                model = (DefaultTableModel) views.sales_table.getModel();
                Object[] row = new Object[6];
                row[0] = views.txt_sale_product_id.getText();
                row[1] = views.txt_sale_product_name.getText();
                row[2] = views.txt_sale_quantity.getText();
                row[3] = views.txt_sale_price.getText();
                row[4] = views.txt_sale_subtotal.getText();
                row[5] = views.txt_sale_customer_name.getText();
                model.addRow(row);
                views.sales_table.setModel(model);
                
                clearFields();
            } else if(numID != customerID){
                JOptionPane.showMessageDialog(null, "La compra se hace por un solo cliente a la vez");                
            }else if(numID == customerID){
                model = (DefaultTableModel) views.sales_table.getModel();
                Object[] row = new Object[6];
                row[0] = views.txt_sale_product_id.getText();
                row[1] = views.txt_sale_product_name.getText();
                row[2] = views.txt_sale_quantity.getText();
                row[3] = views.txt_sale_price.getText();
                row[4] = views.txt_sale_subtotal.getText();
                row[5] = views.txt_sale_customer_name.getText();
                model.addRow(row);
                views.sales_table.setModel(model);

                // Calcular total a pagar 
                double toPay = totalToPay();
                views.txt_sale_total_to_pay.setText(String.valueOf(toPay));
                clearFields();
            }
        }
        
        if(e.getSource() == views.btn_sale_confirm){
            model = (DefaultTableModel) views.sales_table.getModel();
            String custName = String.valueOf(views.sales_table.getValueAt(0, 5));
            int customer_id = customerDao.getCustomerID(custName);
            double total = Double.parseDouble(views.txt_sale_total_to_pay.getText());
            
            saleDao.registerSale(customer_id, id_user, total);
                    
            for(int i=0; i<model.getRowCount();i++){
                int saleQuantity = Integer.parseInt(String.valueOf(views.sales_table.getValueAt(i, 2)));
                double salePrice = Double.parseDouble(String.valueOf(views.sales_table.getValueAt(i, 3)));
                double saleSubtotal = Double.parseDouble(String.valueOf(views.sales_table.getValueAt(i, 4)));
                int productID = Integer.parseInt(String.valueOf(views.sales_table.getValueAt(i, 0)));
                int saleID = saleDao.lastSaleID();
                
                saleDao.registerSaleDetails(saleQuantity, salePrice, saleSubtotal, productID, saleID);
            }
            clearTable();
            clearFields();
            views.txt_sale_total_to_pay.setText("");
            numID=0;
            clearTableAllSales();
            listSalesReport();
        }
        
        if(e.getSource() == views.btn_sale_new){
            clearTable();
            clearFields();
            views.txt_sale_total_to_pay.setText("");
            numID=0;
        }
        
        if(e.getSource() == views.btn_sale_remove){
            if(views.sales_table.getSelectedRow() != -1){
                // Eliminarlo de la tabla
                DefaultTableModel ventasModel = new DefaultTableModel();
                ventasModel = (DefaultTableModel) views.sales_table.getModel();
                ventasModel.removeRow(views.sales_table.getSelectedRow());
                views.sales_table.setModel(ventasModel);
                
                // Restarlo de la suma total
                double total = Double.parseDouble(views.txt_sale_total_to_pay.getText().trim());
                double subtotal = Double.parseDouble(views.txt_sale_subtotal.getText().trim());
                double total2pay = total - subtotal;
                views.txt_sale_total_to_pay.setText(String.valueOf(total2pay));
                
                clearFields();
                
            }else{
               JOptionPane.showMessageDialog(null, "Selecciona un producto de la lista para eliminar");                
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
        if(e.getSource() == views.txt_sale_product_code){
            views.txt_sale_product_id.setText("");
            views.txt_sale_product_name.setText("");
            views.txt_sale_price.setText("");
            views.txt_sale_stock.setText("");
            
            int code = Integer.parseInt(String.valueOf(views.txt_sale_product_code.getText()));
            
            ProductsDao productDao = new ProductsDao();
            infoProduct = productDao.searchCode(code);

            if(infoProduct.getName() != null){
                views.txt_sale_product_id.setText(String.valueOf(infoProduct.getId()));
                views.txt_sale_product_name.setText(infoProduct.getName());
                views.txt_sale_price.setText(String.valueOf(infoProduct.getUnit_price()));
                views.txt_sale_stock.setText(String.valueOf(infoProduct.getProduct_quantity()));
                
                views.txt_sale_quantity.requestFocus();
            }
        }
        
        if(e.getSource() == views.txt_sale_quantity){
            if(e.getKeyCode() == KeyEvent.VK_ENTER){
                double subtotal = Double.parseDouble(views.txt_sale_price.getText()) * Double.parseDouble(views.txt_sale_quantity.getText());
                views.txt_sale_subtotal.setText(String.valueOf(subtotal));
                views.txt_sale_customer_id.requestFocus();
            }
        }
        
        if(e.getSource() == views.txt_sale_customer_id){
            views.txt_sale_customer_name.setText("");
            int id = Integer.parseInt(views.txt_sale_customer_id.getText());
            CustomersDao customerDao = new CustomersDao();
            customerName = customerDao.getCustomerName(id);
            if(!customerName.equals("")){
                views.txt_sale_customer_name.setText(customerName);
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    views.btn_sale_add_product.doClick();
                    views.txt_sale_product_code.requestFocus();
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == views.sales_table){
            model = (DefaultTableModel) views.sales_table.getModel();
            int row = views.sales_table.rowAtPoint(e.getPoint());
            views.txt_sale_product_id.setText(String.valueOf(views.sales_table.getValueAt(row, 0)));
            views.txt_sale_product_name.setText(String.valueOf(views.sales_table.getValueAt(row, 1)));
            views.txt_sale_quantity.setText(String.valueOf(views.sales_table.getValueAt(row, 2)));
            views.txt_sale_price.setText(String.valueOf(views.sales_table.getValueAt(row, 3)));
            views.txt_sale_subtotal.setText(String.valueOf(views.sales_table.getValueAt(row, 4)));
            views.txt_sale_product_name.setText(String.valueOf(views.sales_table.getValueAt(row, 5)));
        }
        
        if(e.getSource() == views.jLabelReports){
            views.jTabbedPane1.setSelectedIndex(7);
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
    
    public void clearFields(){
        views.txt_sale_customer_id.setText("");
        views.txt_sale_customer_name.setText("");
        views.txt_sale_price.setText("");
        views.txt_sale_product_code.setText("");
        views.txt_sale_product_id.setText("");
        views.txt_sale_product_name.setText("");
        views.txt_sale_quantity.setText("");
        views.txt_sale_stock.setText("");
        views.txt_sale_subtotal.setText("");
    }
    
    public void clearTable(){
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
        views.purchase_table.setModel(model);
    }
    
    public void clearTableAllSales(){
        DefaultTableModel allSalesModel = new DefaultTableModel();
        allSalesModel = (DefaultTableModel) views.table_all_sales.getModel();
        for (int i = 0; i < allSalesModel.getRowCount(); i++) {
            allSalesModel.removeRow(i);
            i = i - 1;
        }
        views.table_all_sales.setModel(allSalesModel);
    }
    
    public double totalToPay(){
        double total2pay=0;
        model = (DefaultTableModel) views.sales_table.getModel();
        for(int i=0; i<model.getRowCount(); i++){
            double total = Double.parseDouble(String.valueOf(views.txt_sale_total_to_pay.getText().trim()));
            double subtotal = Double.parseDouble(String.valueOf(views.txt_sale_subtotal.getText().trim()));
            total2pay = total + subtotal;
        }
        
        return total2pay;        
    }
    
    public void listSalesReport(){
        DefaultTableModel allSalesModel = new DefaultTableModel();
        allSalesModel = (DefaultTableModel) views.table_all_sales.getModel();
        
        List<Sales> listAllSales = new ArrayList();
        listAllSales = saleDao.listSalesQuery();
        
        for(Sales saleIt: listAllSales){
            Object[] row = new Object[5];
            row[0] = saleIt.getSalesID();
            row[1] = saleIt.getCustomerName();
            row[2] = saleIt.getEmployeeName();
            row[3] = saleIt.getTotal();
            row[4] = saleIt.getCreated();
            
            allSalesModel.addRow(row);
        }
        
        views.table_all_sales.setModel(allSalesModel);
    }
}
