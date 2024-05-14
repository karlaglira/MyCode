
package Controllers;

import Views.SystemView;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SettingsController implements MouseListener{

    private SystemView views; // Instanciar la vista
    
    // Crear método contructor
    public SettingsController(SystemView views){
        // Escuchar todos los elementos en los que el usuario puede ingresar información
        this.views = views;
        this.views.jLabelProducts.addMouseListener(this);
        this.views.jLabelPurchases.addMouseListener(this);
        this.views.jLabelCustomers.addMouseListener(this);
        this.views.jLabelEmployees.addMouseListener(this);
        this.views.jLabelSuppliers.addMouseListener(this);
        this.views.jLabelCategories.addMouseListener(this);
        this.views.jLabelReports.addMouseListener(this);
        this.views.jLabelSettings.addMouseListener(this);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    // Para cuando pase el mouse por el boton o label
    public void mouseEntered(MouseEvent e) {
        if(e.getSource() == views.jLabelProducts){ // Si la persona se encuentra encima del jPanelProducts
            views.jPanelPoducts.setBackground(new Color(152,202,63)); // Entonces cambia de color el background
        } else if(e.getSource() == views.jLabelPurchases){
            views.jPanelPurchases.setBackground(new Color(152,202,63));
        } else if(e.getSource() == views.jLabelCustomers){
            views.jPanelCustomers.setBackground(new Color(152,202,63));
        } else if(e.getSource() == views.jLabelEmployees){
            views.jPanelEmployees.setBackground(new Color(152,202,63));
        } else if(e.getSource() == views.jLabelSuppliers){
            views.jPanelSuppliers.setBackground(new Color(152,202,63));
        } else if(e.getSource() == views.jLabelCategories){
            views.jPanelCategories.setBackground(new Color(152,202,63));
        } else if(e.getSource() == views.jLabelReports){
            views.jPanelReports.setBackground(new Color(152,202,63));
        } else if(e.getSource() == views.jLabelSettings){
            views.jPanelSettings.setBackground(new Color(152,202,63));
        }
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Cambiar color del panel del menú sobre el que se coloque el mouse encima
        if(e.getSource() == views.jLabelProducts){ // Si la persona se encuentra encima del jLabelProducts
            views.jPanelPoducts.setBackground(new Color(18,45,61)); // Entonces cambia de color el background del panel
        } else if(e.getSource() == views.jLabelPurchases){
            views.jPanelPurchases.setBackground(new Color(18,45,61));
        } else if(e.getSource() == views.jLabelCustomers){
            views.jPanelCustomers.setBackground(new Color(18,45,61));
        } else if(e.getSource() == views.jLabelEmployees){
            views.jPanelEmployees.setBackground(new Color(18,45,61));
        } else if(e.getSource() == views.jLabelSuppliers){
            views.jPanelSuppliers.setBackground(new Color(18,45,61));
        } else if(e.getSource() == views.jLabelCategories){
            views.jPanelCategories.setBackground(new Color(18,45,61));
        } else if(e.getSource() == views.jLabelReports){
            views.jPanelReports.setBackground(new Color(18,45,61));
        } else if(e.getSource() == views.jLabelSettings){
            views.jPanelSettings.setBackground(new Color(18,45,61));
        }
    }
    
}
