
package Main;

import Views.LoginView;

public class main {
    public static void main(String[] args) {
        // Instancia del login para que sea lo primero que sale
        LoginView login = new LoginView();
        login.setVisible(true); // Para mostrar el login
        
    }
}
