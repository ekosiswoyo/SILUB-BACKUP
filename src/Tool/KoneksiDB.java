package Tool;

import java.sql.*;
import javax.swing.JOptionPane;

public class KoneksiDB {
    
    public KoneksiDB(){
    
    }
    
    public Connection getConnection() throws SQLException {
        Connection cn;
        try{
            String server = "jdbc:mysql://localhost/db_lubadajaya";
            String drever = "com.mysql.jdbc.Driver";
            Class.forName(drever);
            cn = DriverManager.getConnection (server, "root","");
            return cn;
        } catch (SQLException | ClassNotFoundException se) {
            JOptionPane.showMessageDialog(null,"SERVER LEMAH, MOHON RESTART SISTEM ! ");
            return null;
        }
    }
    
}
