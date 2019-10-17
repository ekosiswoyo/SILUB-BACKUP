package Analisis;

import Tool.KoneksiDB;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class SQL {
    KoneksiDB getCnn = new KoneksiDB();
    Connection _Cnn;

    public int bnyTransaksi(){
        int jml=0;
        
        try{
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery("select count(no_faktur_penjualan) from tb_penjualan");
            while(res.next()){
                jml=res.getInt("count(no_faktur_penjualan)");
            }
            res.close();
            stat.close();
        }
        catch(Exception z) { JOptionPane.showMessageDialog(null, z.getMessage());}
        return jml;
    }
    public int c2(String a,String b, String c){
        int jml=0;
        try{
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery("select COUNT(DISTINCT no_faktur_penjualan)as ddd from tb_detail_penjualan "+
                                                    "where "+
                                                    "no_faktur_penjualan in ( select no_faktur_penjualan from tb_detail_penjualan where id_barang='"+a+"') "+
                                                    "and "+
                                                    "no_faktur_penjualan in ( select no_faktur_penjualan from tb_detail_penjualan where id_barang='"+b+"') "+
                                                    "and "+
                                                    "no_faktur_penjualan in ( select no_faktur_penjualan from tb_detail_penjualan where id_barang='"+c+"')");
            while(res.next()){
                jml=res.getInt("ddd");
            }
            res.close();
            stat.close();
        }
        catch(Exception x){
            JOptionPane.showMessageDialog(null, "TIDAK TERDAPAT DATA TERKAIT !");
        }
        return jml;
    }
}
