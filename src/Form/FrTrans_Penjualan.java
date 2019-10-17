package Form;

import Analisis.SQL;
import Analisis.data;
import Tool.KoneksiDB;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class FrTrans_Penjualan extends javax.swing.JInternalFrame {

    KoneksiDB getCnn = new KoneksiDB();
    Connection _Cnn;
    SQL sql=new SQL();
    Vector<data> dt=new Vector<data>();
    NumberFormat atur = NumberFormat.getInstance();
    String sqlselect, sqlinsert, sqldelete, sqldelete1,
           vid_barang, vnm_barang, vid_kat, vid_kategori, vnm_kategori, 
           vid_member, vnm_member, vjns_kel, vno_telp, valamat, vid_user, vnm_user, 
           vno_trans_penjualan, vno_faktur, vtgl_trans, vtglAwal, vtglAkhir, vtglAwalDet, vtglAkhirDet;
    long vhrg_beli, vhrg_jual, vsubtotal, vtotal, vjumlah_bayar, vkembalian, vdiskon, vtotal_akhir;
    long vjumlah_barang, vstok, vpersen;
    DefaultTableModel tblPenjualan, tblDetail_Penjualan, tblBarang, tblMember, tblDetail;
    SimpleDateFormat tglview = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat tglinput = new SimpleDateFormat("yyyy-MM-dd");
    String id = FrMenu.getU_id(), vakses = FrMenu.getU_hakakses(), vusername = FrMenu.getU_username();
    
    public FrTrans_Penjualan() {
        initComponents();
        Locale locale =new Locale ("id", "ID");
        locale.setDefault(locale);
        setTabel_Penjualan();
        showDataPenjualan();
        setTabel_Detail();
        showDetail();
        setJam();
        akses();
        vcout.setText(String.valueOf(sql.bnyTransaksi()));
        atur.setMaximumFractionDigits(2);
        dtTglCari.setDate(new Date());
        dtTglCariAkhir.setDate(new Date());
        dtTglCariDet.setDate(new Date());
        dtTglCariAkhirDet.setDate(new Date());
    }
    private void akses(){
        lblUser.setText(vakses);
        lblUsersistem.setText(vusername);
        if(lblUser.getText().equals("KASIR")){
            btnBackUp.setEnabled(false);
        }else{
            btnBackUp.setEnabled(true);
        }
    }
    private void clearForm(){
        lblNoFak.setText("");
        txtIdMember.setText("");
        txtNamaMember.setText("");
        dtTglPenjualan.setDate(new Date());
        dtTglCari.setDate(new Date());
        dtTglCariAkhir.setDate(new Date());
        dtTglCariDet.setDate(new Date());
        dtTglCariAkhirDet.setDate(new Date());
        txtDaftarBrg.setText("");
        txtIdMember.setText("");
        txtNamaMember.setText("");
        txtNamaBrg.setText("");
        txtHargaBeli.setText("");
        txtHargaJual.setText("");
        txtStok.setText("");
        txtSubtotal.setText("");
        txtJumlah.setText("");
        txtDes.setText("");
        txtCari.setText("");
        txtCariHarga.setText("");
        txtCariDet.setText("");
        txtCariBrg.setText("");
        txtTotal.setText("");
        txtDiskon.setText("");
        txtBayar.setText("");
        txtKembalian.setText("");
        txtIdMember.requestFocus();
    }
    private void enableForm(){
        txtIdMember.setEnabled(true);
        txtNamaMember.setEnabled(true);
        txtHargaBeli.setEnabled(true);
        txtHargaJual.setEnabled(true);
        txtJumlah.setEnabled(true);
        txtStok.setEnabled(true);
        txtSubtotal.setEnabled(true);
        txtDaftarBrg.setEnabled(true);
        txtNamaBrg.setEnabled(true);
        txtCariBrg.setEnabled(true);
        txtTotal.setEnabled(true);
        txtDiskon.setEnabled(true);
        txtPersen.setEnabled(true);
        txtTotalAkhir.setEnabled(true);
        txtBayar.setEnabled(true);
        txtKembalian.setEnabled(true);
        btnMember.setEnabled(true);
        btnTambahBrg.setEnabled(true);
        btnSimpan.setEnabled(true);
    }
    private void setTabel_Penjualan(){
        String[]kolom1 = {"NO. FAKTUR", "MEMBER", "PEGAWAI", "TANGGAL", "TOTAL", "DISKON", "TOTAL AKHIR", "JUMLAH BAYAR", "KEMBALIAN"};
        tblPenjualan = new DefaultTableModel(null,kolom1){
            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.Double.class,
                java.lang.Double.class,
                java.lang.Double.class,
                java.lang.Double.class,
                java.lang.Double.class
            };
            public Class getColumnClass(int columnIndex){
                return types [columnIndex];
            }
            public boolean isCellEditable(int row, int col){
                int cola = tblPenjualan.getColumnCount();
                return (col<cola) ? false : true;
            }
        };
        tbDataPenjualan.setModel(tblPenjualan);
        tbDataPenjualan.getColumnModel().getColumn(0).setPreferredWidth(120);
        tbDataPenjualan.getColumnModel().getColumn(1).setPreferredWidth(150);
        tbDataPenjualan.getColumnModel().getColumn(2).setPreferredWidth(155);
        tbDataPenjualan.getColumnModel().getColumn(3).setPreferredWidth(75);
        tbDataPenjualan.getColumnModel().getColumn(4).setPreferredWidth(100);
        tbDataPenjualan.getColumnModel().getColumn(5).setPreferredWidth(100);
        tbDataPenjualan.getColumnModel().getColumn(6).setPreferredWidth(100);
        tbDataPenjualan.getColumnModel().getColumn(7).setPreferredWidth(100);
        tbDataPenjualan.getColumnModel().getColumn(8).setPreferredWidth(100);
    }
    private void setTabel_TransPenjualan(){
        String[]kolom1 = {"NO. FAKTUR", "KODE BARANG", "NAMA BARANG", "HARGA BELI", "HARGA JUAL", "JUMLAH", "SUBTOTAL","NO"};
        tblDetail_Penjualan = new DefaultTableModel(null, kolom1){
            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.Double.class,
                java.lang.Double.class,
                java.lang.Integer.class,
                java.lang.Double.class,
                java.lang.Integer.class
            };
            public Class getColumnClass(int columnIndex){
                return types [columnIndex];
            }
            public boolean isCellEditable(int row, int col){
                int cola = tblDetail_Penjualan.getColumnCount();
                return (col<cola) ? false : true;
            }
        /*?    public boolean isCellEdit(int row, int col) {
            switch(col){
                case 5 : return true;
                default : return false;
            }
            }  */
        };
        tbDetailPenjualan.setModel(tblDetail_Penjualan);
        tbDetailPenjualan.getColumnModel().getColumn(0).setPreferredWidth(150);
        tbDetailPenjualan.getColumnModel().getColumn(1).setPreferredWidth(100);
        tbDetailPenjualan.getColumnModel().getColumn(2).setPreferredWidth(200);
        tbDetailPenjualan.getColumnModel().getColumn(3).setPreferredWidth(100);
        tbDetailPenjualan.getColumnModel().getColumn(4).setPreferredWidth(100);
        tbDetailPenjualan.getColumnModel().getColumn(5).setPreferredWidth(70);
        tbDetailPenjualan.getColumnModel().getColumn(6).setPreferredWidth(150);
        tbDetailPenjualan.getColumnModel().getColumn(7).setPreferredWidth(30);
    }
    private void setTabel_Detail(){
        String[]kolom1 = {"NO. FAKTUR", "KODE BARANG", "NAMA BARANG", "HARGA BELI", "HARGA JUAL", "JUMLAH", "SUBTOTAL"};
        tblDetail = new DefaultTableModel(null, kolom1){
            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.Double.class,
                java.lang.Double.class,
                java.lang.Integer.class,
                java.lang.Double.class
            };
            public Class getColumnClass(int columnIndex){
                return types [columnIndex];
            }
            public boolean isCellEditable(int row, int col){
                int cola = tblDetail.getColumnCount();
                return (col<cola) ? false : true;
            }
        };
        tbDetail.setModel(tblDetail);
        tbDetail.getColumnModel().getColumn(0).setPreferredWidth(150);
        tbDetail.getColumnModel().getColumn(1).setPreferredWidth(100);
        tbDetail.getColumnModel().getColumn(2).setPreferredWidth(200);
        tbDetail.getColumnModel().getColumn(3).setPreferredWidth(100);
        tbDetail.getColumnModel().getColumn(4).setPreferredWidth(100);
        tbDetail.getColumnModel().getColumn(5).setPreferredWidth(70);
        tbDetail.getColumnModel().getColumn(6).setPreferredWidth(150);
    }
    private void setTabelBarang(){
        String[]kolom1 = {"NAMA BARANG", "KODE BARANG", "KATEGORI", "HARGA BELI", "HARGA JUAL", "STOK"};
        tblBarang = new DefaultTableModel(null,kolom1){
            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.Double.class,
                java.lang.Double.class,
                java.lang.Integer.class
            };
            public Class getColumnClass(int columnIndex){
                return types [columnIndex];
            }
            public boolean isCellEditable(int row, int col){
                int cola = tblBarang.getColumnCount();
                return (col<cola) ? false : true;
            }
        };
        tbDataBarang.setModel(tblBarang);
        tbDataBarang.getColumnModel().getColumn(0).setPreferredWidth(200);
        tbDataBarang.getColumnModel().getColumn(1).setPreferredWidth(100);
        tbDataBarang.getColumnModel().getColumn(2).setPreferredWidth(100);
        tbDataBarang.getColumnModel().getColumn(3).setPreferredWidth(100);
        tbDataBarang.getColumnModel().getColumn(4).setPreferredWidth(100);
        tbDataBarang.getColumnModel().getColumn(5).setPreferredWidth(50);
    }
    private void setTabelMember(){
        String[] kolom1 = {"NAMA MEMBER", "ID_MEMBER", "L/P", "NO. TELEPON", "ALAMAT"};
        tblMember = new DefaultTableModel(null, kolom1){
            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            public Class getColumnClass(int columnIndex){
                return types [columnIndex];
            }
            public boolean isCellEditable(int row, int col){
                int cola = tblMember.getColumnCount();
                return (col < cola) ? false:true;
            }
        };
        tbDataMember.setModel(tblMember);
        tbDataMember.getColumnModel().getColumn(0).setPreferredWidth(200);
        tbDataMember.getColumnModel().getColumn(1).setPreferredWidth(100);
        tbDataMember.getColumnModel().getColumn(2).setPreferredWidth(50);
        tbDataMember.getColumnModel().getColumn(3).setPreferredWidth(100);
        tbDataMember.getColumnModel().getColumn(4).setPreferredWidth(300);
    }
    private void clearTabel_Penjualan(){
        int row_BM  = tblPenjualan.getRowCount();
        for(int i = 0; i <row_BM; i++){
            tblPenjualan.removeRow(0);
        }
    }
    private void clearTabel_Detail(){
        int row_BM  = tblDetail.getRowCount();
        for(int i = 0; i <row_BM; i++){
            tblDetail.removeRow(0);
        }
    }
    private void showDataPenjualan(){
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            clearTabel_Penjualan();
            sqlselect = "select * from tb_penjualan a, tb_member b, tb_user c "
                    +" where a.id_member=b.id_member and a.id_user=c.id_user "
                    +" order by tgl_transaksi desc, no_faktur_penjualan desc"; 
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);// mengeksekusi query select
            while(res.next()){
                vno_faktur = res.getString("no_faktur_penjualan");
                vid_member = res.getString("id_member");
                if(vid_member.equals("")){
                    vid_member= "NON MEMBER";
                }else{
                    vid_member = res.getString("nama_member");
                }
                vnm_user = res.getString("id_user")+"-"+res.getString("nama_lengkap");
                vtgl_trans = tglview.format(res.getDate("tgl_transaksi"));
                vtotal = res.getLong("total");
                vdiskon = res.getLong("diskon");
                vtotal_akhir = vtotal - vdiskon;
                vjumlah_bayar = res.getLong("jumlah_bayar");
                vkembalian = res.getLong("kembalian");
                Object[]data = {vno_faktur, vid_member, vnm_user, vtgl_trans, vtotal, vdiskon, vtotal_akhir, vjumlah_bayar, vkembalian};
                tblPenjualan.addRow(data); txtNoFaktur.setText(""); txtTotalAkhir.setText("");
            }
            lblRecordPenjualan.setText("RECORD : "+tblPenjualan.getRowCount()); btnSimpan.setVisible(true);
            res.close();
            stat.close();
            _Cnn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method showDataPenjualan() : "+ex);
        }
    }
    private void showDetail(){
        try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            clearTabel_Detail();
                sqlselect = "select * from tb_penjualan a, tb_detail_penjualan b, tb_barang d "
                    + " where a.no_faktur_penjualan=b.no_faktur_penjualan and "
                    + " b.id_barang=d.id_barang order by a.tgl_transaksi desc, b.no_faktur_penjualan desc ";
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);
            while(res.next()){
                vno_faktur = res.getString("no_faktur_penjualan");
                vid_barang = res.getString("id_barang");
                vnm_barang = res.getString("nama_barang");
                vhrg_beli = res.getLong("harga_beli"); 
                vhrg_jual = res.getLong("harga_jual");
                vjumlah_barang = res.getInt("jumlah_barang");
                vsubtotal = res.getLong("subtotal");
                Object[]data = {vno_faktur, vid_barang, vnm_barang, vhrg_beli, vhrg_jual, vjumlah_barang, vsubtotal};
                tblDetail.addRow(data);
                lblDetail.setText("RECORD : "+tblDetail.getRowCount());
            }
            res.close();
            stat.close();
            _Cnn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method showDetail() : "+ex);
        }              
    }
    private void cariDetail(){
        vtgl_trans = tglinput.format(dtTglCariDet.getDate());
        String vtgl_trans1 = tglinput.format(dtTglCariAkhirDet.getDate());
        try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            clearTabel_Detail();
            if(txtCariDet.getText().length() > 0 ){
                sqlselect = "select * from tb_penjualan a, tb_detail_penjualan b, tb_barang d "
                    + " where b.id_barang=d.id_barang and a.no_faktur_penjualan=b.no_faktur_penjualan and "
                    + " a.no_faktur_penjualan like '%"+txtCariDet.getText()+"%' "
                    + " order by a.tgl_transaksi desc, b.no_faktur_penjualan desc ";
            }else{
                sqlselect = "select * from tb_penjualan a, tb_detail_penjualan b, tb_barang d "
                    + " where a.no_faktur_penjualan=b.no_faktur_penjualan and "
                    + " b.id_barang=d.id_barang and tgl_transaksi between '"+vtgl_trans+"' and "
                    + " '"+vtgl_trans1+"' order by a.tgl_transaksi desc, b.no_faktur_penjualan desc ";
            }
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);
            while(res.next()){
                vno_faktur = res.getString("no_faktur_penjualan");
                vid_barang = res.getString("id_barang");
                vnm_barang = res.getString("nama_barang");
                vhrg_beli = res.getLong("harga_beli"); 
                vhrg_jual = res.getLong("harga_jual");
                vjumlah_barang = res.getInt("jumlah_barang");
                vsubtotal = res.getLong("subtotal");
                Object[]data = {vno_faktur, vid_barang, vnm_barang, vhrg_beli, vhrg_jual, vjumlah_barang, vsubtotal};
                tblDetail.addRow(data);
                lblDetail.setText("RECORD : "+tblDetail.getRowCount());
            }
            res.close();
            stat.close();
            _Cnn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method showDetail() : "+ex);
        }              
    }
    private void showDataBarang(){
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            if(cmbKat.getSelectedIndex()==0){
                sqlselect = "select * from tb_barang a, tb_kategori b"
                    + " where a.id_kategori=b.id_kategori and nama_barang like '"+txtCariBrg.getText()+"%%' "
                    + " order by nama_barang asc";
            }else if(cmbKat.getSelectedIndex()!=0 && txtCariBrg.getText().equals("") && txtCariHarga.getText().equals("0")){
                sqlselect = "select * from tb_barang a, tb_kategori b"
                    + " where a.id_kategori=b.id_kategori and nama_kategori='"+cmbKat.getSelectedItem().toString()+"' "
                    + " order by nama_barang asc";
            }else if(cmbKat.getSelectedIndex()!=0 && !txtCariBrg.getText().equals("") && txtCariHarga.getText().equals("0")){
                sqlselect = "select * from tb_barang a, tb_kategori b"
                    + " where a.id_kategori=b.id_kategori and nama_kategori='"+cmbKat.getSelectedItem().toString()+"' and "
                    + " nama_barang like '%"+txtCariBrg.getText()+"%' order by nama_barang asc";
            }else{
                sqlselect = "select * from tb_barang a, tb_kategori b"
                    + " where a.id_kategori=b.id_kategori and nama_kategori='"+cmbKat.getSelectedItem().toString()+"' "
                    + " and harga_jual like '%"+txtCariHarga.getText()+"%' order by nama_barang asc";
            }
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);// mengeksekusi query select
            while(res.next()){
                vid_barang = res.getString("id_barang");
                vnm_kategori = res.getString("nama_kategori");
                vnm_barang = res.getString("nama_barang");
                vhrg_beli = res.getLong("harga_beli");
                vhrg_jual = res.getLong("harga_jual");
                vstok = res.getInt("stok");
                Object[]data = {vnm_barang, vid_barang, vnm_kategori,  
                                vhrg_beli, vhrg_jual, vstok};
                tblBarang.addRow(data);
            }
            lblRecordBarang.setText("RECORD : "+tbDataBarang.getRowCount());
            res.close();
            stat.close();
            _Cnn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method showDataBarang() : "+ex);
        }
    }
    private void showDataMember(){
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            sqlselect = "select * from tb_member where nama_member like '"+txtCariMember.getText()+"%%' "
                    + " and id_member != '' order by nama_member asc";
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);// mengeksekusi query select
            while(res.next()){
                vnm_member = res.getString("nama_member");
                if(vnm_member.equals("")){
                    vnm_member= "NON MEMBER";
                }else{
                    vnm_member = res.getString("nama_member");
                }
                vid_member = res.getString("id_member");
                vjns_kel = res.getString("jns_kel");
                vno_telp = res.getString("no_telp");
                valamat = res.getString("alamat");
                Object[]data = {vnm_member, vid_member, vjns_kel, vno_telp, valamat};
                tblMember.addRow(data);
            }
            lblRecordMember.setText("RECORD : "+tbDataMember.getRowCount());
            res.close();
            stat.close();
            _Cnn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method showTabelMember() : "+ex);
        }
    }
    private void tampilMember(){
        try{
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select * from tb_member where id_member='"+vid_member+"' ";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            if(res.first()){
                txtIdMember.setText(res.getString("id_member"));
                txtNamaMember.setText(res.getString("nama_member"));
                Daftar_Member.dispose();
                txtIdMember.setEditable(false);
                txtDaftarBrg.requestFocus();
            }
            res.close();
            stat.close();
            _Cnn.close();
        }catch (SQLException ex){
                JOptionPane.showMessageDialog(this, "Error mthod tampilMember : " + ex);
        }
    }
    private void getDataPenjualan(){
        try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select * from tb_penjualan a, tb_detail_penjualan b, tb_member c, tb_barang d "
                    + " where a.id_member=c.id_member and a.no_faktur_penjualan=b.no_faktur_penjualan and "
                    + " b.id_barang=d.id_barang and a.no_faktur_penjualan='"+vno_faktur+"' ";
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);
            while(res.next()){
                lblNoFak.setText(res.getString("no_faktur_penjualan")+"--"+res.getString("nama_member"));
                txtNoFaktur.setText(res.getString("no_faktur_penjualan"));
                txtIdMember.setText(res.getString("id_member"));
                txtNamaMember.setText(res.getString("nama_member"));
                txtTotal.setText(res.getString("total"));
                txtDiskon.setText(res.getString("diskon"));
                txtBayar.setText(res.getString("jumlah_bayar"));
                txtKembalian.setText(res.getString("kembalian"));
                vtotal = Long.parseLong(txtTotal.getText());
                vdiskon = Long.parseLong(txtDiskon.getText());
                Long vtotal_akhir = (Long) (vtotal-vdiskon);
                txtTotalAkhir.setText(Long.toString(vtotal_akhir));
                vpersen = (Long) (vdiskon*100/vtotal);
                txtPersen.setText(Long.toString(vpersen));
                dtTglPenjualan.setDate(new Date());
                
                vno_trans_penjualan = res.getString("no_trans_penjualan");
                vno_faktur = res.getString("no_faktur_penjualan");
                vid_barang = res.getString("id_barang");
                vnm_barang = res.getString("nama_barang");
                vhrg_beli = res.getLong("harga_beli"); 
                vhrg_jual = res.getLong("harga_jual");
                vjumlah_barang = res.getInt("jumlah_barang");
                vsubtotal = res.getLong("subtotal");
                Object[]data = {vno_faktur, vid_barang, vnm_barang, vhrg_beli, vhrg_jual, vjumlah_barang, vsubtotal, vno_trans_penjualan};
                tblDetail_Penjualan.addRow(data);
                btnSimpan.setVisible(false); btnSimpan.setText(""); btnUbah.setVisible(true);
                txtDaftarBrg.setEditable(false); btnDaftarBrg.setEnabled(false);
                btnReset.setEnabled(true);
            }
            res.close();
            stat.close();
            _Cnn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method getDataPenjualan() : "+ex);
        }              
    }
    private void cariTransPenjualan(){
       vtgl_trans = tglinput.format(dtTglCari.getDate());
       String vtgl_trans1 = tglinput.format(dtTglCariAkhir.getDate());
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            clearTabel_Penjualan();
            if(txtCari.getText().length() > 0 ){
                sqlselect = "select * from tb_penjualan a, tb_member b, tb_user c "
                    + " where a.id_member=b.id_member and a.id_user=c.id_user and no_faktur_penjualan like '%%%%%%%%%%%"+txtCari.getText()+"%%%%%%' "
                    + " order by a.tgl_transaksi desc, no_faktur_penjualan desc"; 
            }else{
                sqlselect = "select * from tb_penjualan a, tb_member b, tb_user c "
                    + " where a.id_member=b.id_member and a.id_user=c.id_user and tgl_transaksi between '"+vtgl_trans+"' and "
                    + " '"+vtgl_trans1+"' order by tgl_transaksi desc, no_faktur_penjualan desc";
            }
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);// mengeksekusi query select
            while(res.next()){
                vno_faktur = res.getString("no_faktur_penjualan");
                vid_member = res.getString("id_member");
                if(vid_member.equals("")){
                    vid_member= "NON MEMBER";
                }else{
                    vid_member = res.getString("nama_member");
                }
                vnm_user = res.getString("id_user")+"-"+res.getString("nama_lengkap");
                vtgl_trans = tglview.format(res.getDate("tgl_transaksi"));
                vtotal = res.getLong("total");
                vdiskon = res.getLong("diskon");
                vtotal_akhir = vtotal - vdiskon;
                vjumlah_bayar = res.getLong("jumlah_bayar");
                vkembalian = res.getLong("kembalian");
                Object[]data = {vno_faktur, vid_member, vnm_user, vtgl_trans, vtotal, vdiskon, vtotal_akhir, vjumlah_bayar, vkembalian};
                tblPenjualan.addRow(data);
            }
            lblRecordPenjualan.setText("RECORD : "+tblPenjualan.getRowCount());
            res.close();
            stat.close();
            _Cnn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method showDataPenjualan() : "+ex);
        }
    }
    private void aksiUbahPenjualan(){
        vno_faktur = txtNoFaktur.getText();
        vid_member = txtIdMember.getText(); 
        vtgl_trans = tglinput.format(dtTglPenjualan.getDate())+" "+lbljam.getText();
        vtotal = Long.valueOf(txtTotal.getText());
        vdiskon = Long.valueOf(txtDiskon.getText());
        vjumlah_bayar = Long.valueOf(txtBayar.getText());
        vkembalian = Long.valueOf(txtKembalian.getText());    
        try{
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            Statement state = _Cnn.createStatement();
            if(btnUbah.getText().equals("SIMPAN PERUBAHAN")){
                if(txtIdMember.getText().equals("")){
                    sqlinsert = "update tb_penjualan set id_user='"+id+"', tgl_transaksi='"+vtgl_trans+"', "
                    + " total='"+vtotal+"', diskon='"+vdiskon+"', jumlah_bayar='"+vjumlah_bayar+"', kembalian='"+vkembalian+"' "
                    + " where no_faktur_penjualan='"+vno_faktur+"' ";
                    state.executeUpdate(sqlinsert);
                }else{
                    sqlinsert = "update tb_penjualan set id_member='"+vid_member+"', id_user='"+id+"', tgl_transaksi='"+vtgl_trans+"', "
                    + " total='"+vtotal+"', diskon='"+vdiskon+"', jumlah_bayar='"+vjumlah_bayar+"', kembalian='"+vkembalian+"' "
                    + " where no_faktur_penjualan='"+vno_faktur+"' ";
                    state.executeUpdate(sqlinsert);
                }
                JOptionPane.showMessageDialog(null, "DATA BERHASIL DIUBAH ! ",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE); IfrPenjualan.dispose(); showDataPenjualan();
            }
            state.close();
            _Cnn.close();
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "ERROR AKSI_UBAH_PENJUALAN " +ex);
        }
    }
    private void aksiSimpanPenjualan(){
        vno_faktur = txtNoFaktur.getText();
        vid_member = txtIdMember.getText(); 
        vtgl_trans = tglinput.format(dtTglPenjualan.getDate())+" "+lbljam.getText();
        vtotal = Long.valueOf(txtTotal.getText());
        vdiskon = Long.valueOf(txtDiskon.getText());
        vjumlah_bayar = Long.valueOf(txtBayar.getText());
        vkembalian = Long.valueOf(txtKembalian.getText());
        if(btnSimpan.getText().equals("SIMPAN")){
            if(txtIdMember.getText().equals("")){
                sqlinsert = "insert into tb_penjualan(no_faktur_penjualan,id_member, id_user, tgl_transaksi, "
                    + " total, diskon, jumlah_bayar, kembalian) "
                    + " values('"+vno_faktur+"', '"+vid_member+"', '"+id+"', '"+vtgl_trans+"', "
                    + " '"+vtotal+"',  '"+vdiskon+"', '"+vjumlah_bayar+"', '"+vkembalian+"')";
            }else{
                sqlinsert = "insert into tb_penjualan(no_faktur_penjualan,id_member,id_user, tgl_transaksi, "
                    + " total, diskon, jumlah_bayar, kembalian) "
                    + " values('"+vno_faktur+"', '"+vid_member+"', "
                    + " '"+id+"', '"+vtgl_trans+"', '"+vtotal+"', '"+vdiskon+"', '"+vjumlah_bayar+"', '"+vkembalian+"')";
            }  
        }
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            Statement state = _Cnn.createStatement();
            state.executeUpdate(sqlinsert);
            aksiSimpanDetPenjualan(); 
            state.close();
            _Cnn.close();
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method aksiSimpanPenjualan() : "
                    +ex, "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void aksiSimpanDetPenjualan(){
        int jumlah_baris = tbDetailPenjualan.getRowCount();
        if(jumlah_baris == 0){
        }else{
            try{
                _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
                _Cnn = getCnn.getConnection();// membuka koneksi
                Statement state = _Cnn.createStatement();
                int i=0;
                //String[]kolom1 = {"NO. FAKTUR", "KODE BARANG", "NAMA BARANG", "HARGA BELI", "HARGA JUAL", "JUMLAH", "SUBTOTAL","NO"};
            while(i < jumlah_baris){
                state.executeUpdate("insert into tb_detail_penjualan (no_faktur_penjualan, id_barang, harga_beli, harga_jual, jumlah_barang, subtotal) values("
                        + "'"+tbDetailPenjualan.getValueAt(i, 0)+"',"
                        + "'"+tbDetailPenjualan.getValueAt(i, 1)+"',"
                        + "'"+tbDetailPenjualan.getValueAt(i, 3)+"',"
                        + "'"+tbDetailPenjualan.getValueAt(i, 4)+"',"
                        + "'"+tbDetailPenjualan.getValueAt(i, 5)+"',"
                        + "'"+tbDetailPenjualan.getValueAt(i, 6)+"')");
                state.executeUpdate("update tb_barang set harga_jual = '"+tbDetailPenjualan.getValueAt(i, 4)+"' "
                        + " where id_barang='"+tbDetailPenjualan.getValueAt(i, 1)+"' ");
                i++;
            }
            clearForm(); enableForm(); cetakFaktur();  
            txtTotalAkhir.setText(""); txtPersen.setText(""); setTabel_TransPenjualan();
            txtIdMember.setEditable(true); btnMember.setEnabled(true); txtDaftarBrg.setEditable(true); btnDaftarBrg.setEnabled(true);
            txtJumlah.setEditable(true); autoNoFaktur();
            state.close();
            _Cnn.close();
            }catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, "Gagal Menyimpan! Error : "+e);
            }
        }
    }
    private void aksiReset(){
        int jumlah_baris = tbDetailPenjualan.getRowCount();
        if(jumlah_baris == 0){
        }else{
            try{
                _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
                _Cnn = getCnn.getConnection();// membuka koneksi
                Statement state = _Cnn.createStatement();
                int i=0;
            while(i < jumlah_baris){
                state.executeUpdate("update tb_barang set stok = stok + '"+tbDetailPenjualan.getValueAt(i, 5)+"' "
                        + " where id_barang='"+tbDetailPenjualan.getValueAt(i, 1)+"' ");
                i++;
            }
            clearForm(); enableForm();
            txtTotalAkhir.setText(""); txtPersen.setText("");  autoNoFaktur();
            state.close();
            _Cnn.close();
            }catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, "Gagal Menyimpan! Error : "+e);
            }
        }
    }
    public void listKat(){
        try {
        _Cnn = null;
        _Cnn = getCnn.getConnection();
        sqlselect = "SELECT * FROM tb_kategori";
        Statement stat = _Cnn.createStatement();
        ResultSet res = stat.executeQuery(sqlselect);
        
        while(res.next()){
            Object[] ob = new Object[1];
            ob[0] = res.getString("nama_kategori");
            cmbKat.addItem((String) ob[0]);
        }
        res.close(); stat.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private void getDataBarang(){
        try{
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select * from tb_barang a, tb_kategori b"
                    + " where a.id_kategori=b.id_kategori and id_barang='"+vid_barang+"' ";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            if(res.first()){
                txtDaftarBrg.setText(res.getString("id_barang"));
                txtNamaBrg.setText(res.getString("nama_barang"));
                vap.setText(res.getString("id_barang"));
                vnap.setText(res.getString("nama_barang"));
                txtHargaBeli.setText(res.getString("harga_beli"));
                txtHargaJual.setText(res.getString("harga_jual"));
                txtStok.setText(res.getString("stok"));
                txtDes.setText(res.getString("deskripsi"));
                Daftar_Barang.dispose();
                cmbKat.setSelectedItem(0);
                txtDaftarBrg.setEditable(false);
                txtJumlah.requestFocus();
            }
            res.close();
            stat.close();
            _Cnn.close();
        }catch (SQLException ex){
                JOptionPane.showMessageDialog(this, "Error mthod getDataBarang : " + ex);
            }
    }
    private void getDetail(){
        vno_faktur = txtNoFaktur.getText();
        try{
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select * from tb_barang a, tb_kategori b, tb_detail_penjualan c "
                    + " where a.id_kategori=b.id_kategori and a.id_barang=c.id_barang "
                    + " and c.id_barang='"+vid_barang+"' and no_faktur_penjualan='"+vno_faktur+"' ";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            if(res.first()){
                txtDaftarBrg.setText(res.getString("id_barang"));
                txtNamaBrg.setText(res.getString("nama_barang"));
                txtHargaBeli.setText(res.getString("harga_beli"));
                txtHargaJual.setText(res.getString("harga_jual"));
                txtStok.setText(res.getString("stok"));
                txtDes.setText(res.getString("deskripsi"));
                Daftar_Barang.dispose();
                txtDaftarBrg.setEditable(false);
                txtJumlah.requestFocus();
            }
            res.close();
            stat.close();
            _Cnn.close();
        }catch (SQLException ex){
                JOptionPane.showMessageDialog(this, "Error mthod getDataBarang : " + ex);
            }
    }  
    private void hitungSubtotal(){
        long vsub = 0;
        vhrg_jual = Long.parseLong(txtHargaJual.getText());
        vjumlah_barang = Long.parseLong(txtJumlah.getText());
        vsub = (vsub+vhrg_jual*vjumlah_barang);
        txtSubtotal.setText(Long.toString(vsub));
        txtJumlah.requestFocus();
    }
    private void hitungTotal(){
        long vtotal_harga = 0;
        int vbaris = tbDetailPenjualan.getRowCount();
        tblDetail_Penjualan = (DefaultTableModel) tbDetailPenjualan.getModel();
        for(int i=0;i<vbaris;i++){
            vhrg_jual = Long.parseLong(tblDetail_Penjualan.getValueAt(i, 4).toString());
            vjumlah_barang = Long.parseLong(tblDetail_Penjualan.getValueAt(i, 5).toString());
            vtotal_harga = vtotal_harga+(vhrg_jual*vjumlah_barang);
        }
        txtTotal.setText(String.valueOf(vtotal_harga));
        txtTotalAkhir.setText(String.valueOf(vtotal_harga));
    }
    private void hitungTotalAkhir(){
        long vtotal_akhir=0;
        vtotal = Long.parseLong(txtTotal.getText());
        vdiskon = Long.parseLong(txtDiskon.getText());
        vtotal_akhir = vtotal_akhir+(vtotal-vdiskon);
            txtTotalAkhir.setText(Long.toString(vtotal_akhir));
            txtBayar.requestFocus();
    }
    private void hitungDiskon(){
        long vtotal_akhir=0;
        vtotal = Long.parseLong(txtTotal.getText());
        vdiskon = Long.parseLong(txtPersen.getText());
        vtotal_akhir = vtotal_akhir+(vtotal*vdiskon/100);
            txtDiskon.setText(Long.toString(vtotal_akhir));
            txtBayar.requestFocus();
    }
    private void hitungKembalian(){
        vtotal = Long.parseLong(txtTotalAkhir.getText());
        vjumlah_bayar = Long.parseLong(txtBayar.getText());
        long kembalian = vjumlah_bayar-vtotal;
        if(vjumlah_bayar<vtotal){
            JOptionPane.showMessageDialog(null, "JUMLAH BAYAR TIDAK MENCUKUPI ! ",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            txtBayar.requestFocus();
        }else{
            lblKembali.setVisible(false);
            txtKembalian.setText(Long.toString(kembalian));
            lblKembali.setText(Long.toString(kembalian));
            txtKembalian.requestFocus(); btnSimpan.requestFocus();
        }
    }
    private void tambahBarang(){
        vno_faktur = txtNoFaktur.getText();
        vid_barang = txtDaftarBrg.getText();
        vnm_barang = txtNamaBrg.getText();
        vhrg_beli = Long.parseLong(txtHargaBeli.getText());
        vhrg_jual = Long.parseLong(txtHargaJual.getText());
        vjumlah_barang = Long.parseLong(txtJumlah.getText());
        vstok = Long.parseLong(txtStok.getText());
        vsubtotal = vhrg_jual*vjumlah_barang;
        if(txtDaftarBrg.getText().equals("")){
            JOptionPane.showMessageDialog(null, "KODE BARANG HARUS DIISI !",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            txtJumlah.requestFocus();
        }else if(txtJumlah.getText().equals("0") || txtJumlah.getText().equals("00")){
            txtJumlah.requestFocus();
        }else if(vjumlah_barang > vstok){
            JOptionPane.showMessageDialog(null, "STOK TIDAK MENCUKUPI !",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            txtJumlah.requestFocus();
        }else {
            try{
                _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
                _Cnn = getCnn.getConnection();// membuka koneksi
                    Statement state = _Cnn.createStatement();
                    if(btnSimpan.getText().equals("SIMPAN")){
                        state.executeUpdate(" update tb_barang set stok = stok - '"+vjumlah_barang+"' "
                                    + " where id_barang = '"+vid_barang+"' ");
                    }else{
                        state.executeUpdate(" update tb_barang set stok = stok - '"+vjumlah_barang+"' "
                                    + " where id_barang = '"+vid_barang+"' ");
                        state.executeUpdate("insert into tb_detail_penjualan (no_faktur_penjualan, id_barang, harga_beli, harga_jual, jumlah_barang, subtotal) values("
                            + "'"+vno_faktur+"',"
                            + "'"+vid_barang+"',"
                            + "'"+vhrg_beli+"',"
                            + "'"+vhrg_jual+"',"
                            + "'"+vjumlah_barang+"',"
                            + "'"+vsubtotal+"')");
                    }
            state.close();
            _Cnn.close();
            }catch(Exception e){
                        JOptionPane.showMessageDialog(rootPane, "Gagal Menyimpan! Error : "+e);
            }

            tblDetail_Penjualan.addRow(new Object[]{
                vno_faktur,vid_barang,vnm_barang,vhrg_beli,vhrg_jual,vjumlah_barang,vsubtotal
            });
            tbDetailPenjualan.setModel(tblDetail_Penjualan); hitungTotal(); txtDaftarBrg.setEditable(true);
            txtDaftarBrg.setText("");
            txtNamaBrg.setText("");
            txtHargaBeli.setText("");
            txtHargaJual.setText("");
            txtStok.setText("");
            txtSubtotal.setText("");
            txtJumlah.setText("");
            txtDes.setText("");
            txtCariBrg.setText("");
            txtTotal.requestFocus();
            txtDaftarBrg.requestFocus();
        }
        
    }
    private void hapusBarang(){
        int x = tbDetailPenjualan.getSelectedRow();
        int jumlah_baris = tbDetailPenjualan.getRowCount();
        try{
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            Statement state = _Cnn.createStatement();
            if (btnSimpan.getText().equals("SIMPAN")){
                int i=0;
                    if(i < jumlah_baris){
                        state.executeUpdate(" update tb_barang set stok = stok + '"+tbDetailPenjualan.getValueAt(tbDetailPenjualan.getSelectedRow(), 5)+"' "
                                + " where id_barang = '"+tbDetailPenjualan.getValueAt(tbDetailPenjualan.getSelectedRow(), 1)+"' ");
                        
                    } 
            }else{
                int i=0;
                    if(i < jumlah_baris){
                        state.executeUpdate(" update tb_barang set stok = stok + '"+tbDetailPenjualan.getValueAt(tbDetailPenjualan.getSelectedRow(), 5)+"' "
                                + " where id_barang = '"+tbDetailPenjualan.getValueAt(tbDetailPenjualan.getSelectedRow(), 1)+"' ");
                        
                        state.executeUpdate(" delete from tb_detail_penjualan where id_barang='"+txtDaftarBrg.getText()+"' and "
                                + " no_faktur_penjualan='"+txtNoFaktur.getText()+"' and "
                                + " no_trans_penjualan = '"+tbDetailPenjualan.getValueAt(tbDetailPenjualan.getSelectedRow(), 7)+"' ");               
                        
                    }
            }
            tblDetail_Penjualan.removeRow(x);
            hitungTotal();
            hitungTotalAkhir();
            txtDaftarBrg.requestFocus(); txtDaftarBrg.setEditable(true);
            state.close();
            _Cnn.close();
        }catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, "Gagal Menyimpan! Error : "+e);
        }
    }
    private void autoNoFaktur(){
        Date sk = new Date();
        SimpleDateFormat format1=new SimpleDateFormat("ddMMyy");
        String time = format1.format(sk);
        try{
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select right(no_faktur_penjualan,6) as no_faktur_penjualan "
                    + " from tb_penjualan order by no_faktur_penjualan desc";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            if (res.next()) {
                String kode = res.getString("no_faktur_penjualan");
                String AN = "" + (Integer.parseInt(kode) + 1);
                String Nol = "";
                if(AN.length()==1)
                {Nol = "00000";}
                else if(AN.length()==2)
                {Nol = "0000";}
                else if(AN.length()==3)
                {Nol = "000";}
                else if(AN.length()==4)
                {Nol = "00";}
                else if(AN.length()==5)
                {Nol = "0";}
                else if(AN.length()==6)
                {Nol = "";}
                txtNoFaktur.setText("FK-" +time+ "-" + Nol + AN);
            }else{
                int kode = 1;
                txtNoFaktur.setText("FK-" +time+ "-" +"00000"+Integer.toString(kode));//sesuaikan dengan variable namenya
            }
            res.close();
            stat.close();
            _Cnn.close();
        }catch (SQLException ex){
                JOptionPane.showMessageDialog(this,"Error Method autoNoFaktur : " + ex);
            }
    }
    private void cetakFaktur(){
        int jawab = JOptionPane.showConfirmDialog(null, 
                    "CETAK FAKTUR PENJUALAN ?",
                    "Informasi", JOptionPane.YES_NO_OPTION);
        if(jawab==JOptionPane.YES_OPTION){
            vno_faktur = txtNoFaktur.getText();
            vtotal_akhir = Long.parseLong(txtTotalAkhir.getText());
            vpersen = Long.parseLong(txtPersen.getText());
            String pth = System.getProperty("user.dir") + "/Laporan/lapFakturPenjualan.jrxml";
            try{
                Map<String, Object> parameters = new HashMap<>();
                _Cnn = null;
                _Cnn = getCnn.getConnection();
                parameters.put("parFakturPenjualan", vno_faktur);
                parameters.put("parTotalAkhir", vtotal_akhir);
                parameters.put("Diskon", vpersen);
                JasperReport jrpt = JasperCompileManager.compileReport(pth);
                JasperPrint jprint = JasperFillManager.fillReport(jrpt, parameters, _Cnn);
                JasperViewer.viewReport(jprint, false);
            }catch (SQLException | JRException ex){
                JOptionPane.showConfirmDialog(null, "Error method cetakFaktur : " + ex,
                "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    public void toExcel(JTable table, File file){
        try{
            TableModel model = table.getModel();
            try (FileWriter excel = new FileWriter(file)) {
                for(int i = 0; i < model.getColumnCount(); i++){
                    excel.write(model.getColumnName(i) + "\t");
                }
                excel.write("\n");
                for(int i=0; i< model.getRowCount(); i++) {
                    for(int j=0; j < model.getColumnCount(); j++) {
                        excel.write(model.getValueAt(i,j).toString()+"\t");
                    }
                    excel.write("\n");
                }
                JOptionPane.showMessageDialog(null, "DATA BERHASIL DI EXPORT !");
                txtDir.setText(""); BackUp_Restore.dispose(); setTabel_Penjualan();showDataPenjualan();
            }
        }catch(IOException e){ System.out.println(e); }
    }
    public void setJam() {
        ActionListener taskPerformer = new ActionListener() {
    public void actionPerformed(ActionEvent evt) {
        String nol_jam = "", nol_menit = "", nol_detik = "";

        Date dateTime = new Date();
        int nilai_jam = dateTime.getHours();
        int nilai_menit = dateTime.getMinutes();
        int nilai_detik = dateTime.getSeconds();

        if (nilai_jam <= 9) nol_jam = "0";
        if (nilai_menit <= 9) nol_menit = "0";
        if (nilai_detik <= 9) nol_detik = "0";

        String jam = nol_jam + Integer.toString(nilai_jam);
        String menit = nol_menit + Integer.toString(nilai_menit);
        String detik = nol_detik + Integer.toString(nilai_detik);

        lbljam.setText(jam + ":" + menit + ":" + detik);
        }
      };
      new Timer(1000, taskPerformer).start();
    }
    private void tanggal_Pencarian(){
        try{
        if(dtTglCari.getDate() != null && dtTglCariAkhir.getDate() != null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            vtglAwal=format.format(dtTglCari.getDate());
            vtglAkhir=format.format(dtTglCariAkhir.getDate());
            Date Tanggal1 = format.parse(vtglAwal);
            Date Tanggal2 = format.parse(vtglAkhir);
            long Hari1 = Tanggal1.getTime();
            long Hari2 = Tanggal2.getTime();
            long diff = Hari2 - Hari1;
            long Lama = diff / (24 * 60 * 60 * 1000);
            if(Lama<0){
                clearForm();
                JOptionPane.showMessageDialog(null, "TANGGAL AKHIR TIDAK BISA KURANG DARI TANGGAL AWAL ! ");
            } 
        }
        } catch(Exception e){  
            System.out.println("BERPIKIR LAGI !"+e);
        }
    }
    private void tanggal_PencarianDet(){
        try{
        if(dtTglCariDet.getDate() !=null && dtTglCariAkhirDet.getDate()!=null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            vtglAwalDet=format.format(dtTglCariDet.getDate());
            vtglAkhirDet=format.format(dtTglCariAkhirDet.getDate());
            Date Tanggal3 = format.parse(vtglAwalDet);
            Date Tanggal4 = format.parse(vtglAkhirDet);
            long Hari3 = Tanggal3.getTime();
            long Hari4 = Tanggal4.getTime();
            long diff1 = Hari4 - Hari3;
            long Lama1 = diff1 / (24 * 60 * 60 * 1000);
            if(Lama1<0){
                clearForm();
                JOptionPane.showMessageDialog(null, "TANGGAL AKHIR TIDAK BISA KURANG DARI TANGGAL AWAL ! ");
            } 
        }
        } catch(Exception e){  
            System.out.println("BERPIKIR LAGI !"+e);
        }
    }
    // ------------ LATIHAN APRIORI ANALISIS -----------------
    void isidata(){
        try{
            int temp=Integer.parseInt(vcout.getText())*Integer.parseInt(jSup.getValue().toString())/100;
            _Cnn = getCnn.getConnection();
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery("SELECT a.id_barang, count(no_faktur_penjualan) , nama_barang "
                + " FROM tb_detail_penjualan a, tb_barang b where a.id_barang=b.id_barang GROUP BY a.id_barang "
                + " HAVING count( no_faktur_penjualan ) >=1 ");
            while(res.next()){
                if(res.getInt("count(no_faktur_penjualan)")>=temp ){
                    data data=new data();
                    data.setKode_item(res.getString("id_barang"));
                    data.setNama_item(res.getString("nama_barang"));
                    data.setNilai(res.getInt("count(no_faktur_penjualan)"));
                    dt.add(data);
                }
            }
        }
        catch(Exception z){
            JOptionPane.showMessageDialog(rootPane,"SALAH "+z);
        }
    }
    void c2(){
       txtDataAP.setText("");
       int n=0;
       float bnyAB, bnyA, bnyB=0;
       float supp,conf=0;
       vid_barang = vap.getText();
       for(int a=0; a<dt.size();a++){
           n++;
           for(int b=0+n; b<dt.size();b++){
               bnyAB=sql.c2(vid_barang, dt.get(a).getKode_item(), dt.get(b).getKode_item());
               bnyA=dt.get(a).getNilai();
               bnyB=dt.get(b).getNilai();
               supp=bnyAB/Integer.parseInt(vcout.getText())*100;
               if(supp>=Float.parseFloat(jSup.getValue().toString())){
                    conf=(bnyAB/bnyA)*100; 
                    if(conf>= Float.parseFloat(jCon.getValue().toString()))
                    txtDataAP.setText(txtDataAP.getText()+" JIKA MEMBELI  '"+dt.get(a).getNama_item()+"'"
                            +" MAKA AKAN MEMBELI"+" '"+dt.get(b).getNama_item()+" '"
                            +"\n"+"(  Conf "+atur.format(conf)+"% )\n");
                    conf=(bnyAB/bnyB)*100; 
                    if(conf>= Float.parseFloat(jCon.getValue().toString()))
                    txtDataAP.setText(txtDataAP.getText()+" JIKA MEMBELI  '"+dt.get(b).getNama_item()+"'"
                            +" MAKA AKAN MEMBELI"+" '"+dt.get(a).getNama_item()+" '"
                            +"\n"+"(  Conf "+atur.format(conf)+"% )\n");
                    
                }
          }
       }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        IfrPenjualan = new javax.swing.JFrame();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        dtTglPenjualan = new com.toedter.calendar.JDateChooser();
        txtNoFaktur = new Tool.Custom_TextField();
        txtIdMember = new Tool.Custom_TextField();
        txtNamaMember = new Tool.Custom_TextField();
        btnMember = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        btnReset = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        txtTotalAkhir = new Tool.NumberTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtHargaJual = new Tool.NumberTextField();
        txtHargaBeli = new Tool.NumberTextField();
        txtJumlah = new Tool.NumberTextField();
        txtDaftarBrg = new Tool.Custom_TextField();
        txtNamaBrg = new Tool.Custom_TextField();
        txtStok = new Tool.NumberTextField();
        jLabel13 = new javax.swing.JLabel();
        btnDaftarBrg = new javax.swing.JButton();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        btnTambahBrg = new javax.swing.JButton();
        jLabel53 = new javax.swing.JLabel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbDetailPenjualan = new javax.swing.JTable();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtDataAP = new javax.swing.JTextArea();
        jPanel18 = new javax.swing.JPanel();
        jCon = new javax.swing.JSpinner();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        vap = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        vnap = new javax.swing.JTextField();
        vcout = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jSup = new javax.swing.JSpinner();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        btnApriori = new javax.swing.JButton();
        txtSubtotal = new Tool.NumberTextField();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtBayar = new Tool.NumberTextField();
        txtKembalian = new Tool.NumberTextField();
        btnSimpan = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtPersen = new Tool.NumberTextField();
        jLabel14 = new javax.swing.JLabel();
        txtTotal = new Tool.NumberTextField();
        txtDiskon = new Tool.NumberTextField();
        btnDataPenjualan = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        lblKembali = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        lbljam = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        lblUser1 = new javax.swing.JLabel();
        lblUsersistem = new javax.swing.JLabel();
        lblUser3 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtDes = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        Daftar_Barang = new javax.swing.JDialog();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbDataBarang = new javax.swing.JTable();
        lblRecordBarang = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        txtCariBrg = new Tool.Custom_TextField();
        cmbKat = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtCariHarga = new Tool.NumberTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        Daftar_Member = new javax.swing.JDialog();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbDataMember = new javax.swing.JTable();
        lblRecordMember = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        txtCariMember = new Tool.Custom_TextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        BackUp_Restore = new javax.swing.JFrame();
        jPanel13 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel14 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtDir = new javax.swing.JTextField();
        btnPilihLokasi = new javax.swing.JButton();
        btnSimpanBackup = new javax.swing.JButton();
        jFileChooser1 = new javax.swing.JFileChooser();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        panelPenjualan = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbDataPenjualan = new javax.swing.JTable();
        lblRecordPenjualan = new javax.swing.JLabel();
        txtCari = new Tool.Custom_TextField();
        btnCetakFaktur = new javax.swing.JButton();
        lblNoFak = new javax.swing.JLabel();
        btnTambah = new javax.swing.JButton();
        btnBackUp = new javax.swing.JButton();
        btnCariTgl = new javax.swing.JButton();
        dtTglCari = new com.toedter.calendar.JDateChooser();
        dtTglCariAkhir = new com.toedter.calendar.JDateChooser();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        panelDetPenjualan = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tbDetail = new javax.swing.JTable();
        lblDetail = new javax.swing.JLabel();
        txtCariDet = new Tool.Custom_TextField();
        btnCariTglDet = new javax.swing.JButton();
        dtTglCariDet = new com.toedter.calendar.JDateChooser();
        dtTglCariAkhirDet = new com.toedter.calendar.JDateChooser();
        jLabel19 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();

        IfrPenjualan.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        IfrPenjualan.setTitle("TRANSAKSI PENJUALAN-SILUB");
        IfrPenjualan.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        IfrPenjualan.setUndecorated(true);
        IfrPenjualan.setResizable(false);
        IfrPenjualan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                IfrPenjualanKeyReleased(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "(+) FORM", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 12))); // NOI18N

        dtTglPenjualan.setForeground(java.awt.Color.blue);
        dtTglPenjualan.setEnabled(false);
        dtTglPenjualan.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        txtNoFaktur.setEditable(false);
        txtNoFaktur.setBackground(new java.awt.Color(0, 204, 204));
        txtNoFaktur.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNoFaktur.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        txtNoFaktur.setPlaceholder("NO. TRANSAKSI");

        txtIdMember.setBackground(new java.awt.Color(0, 204, 204));
        txtIdMember.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtIdMember.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        txtIdMember.setPlaceholder("KODE MEMBER");
        txtIdMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdMemberActionPerformed(evt);
            }
        });
        txtIdMember.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIdMemberKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIdMemberKeyTyped(evt);
            }
        });

        txtNamaMember.setEditable(false);
        txtNamaMember.setBackground(new java.awt.Color(0, 204, 204));
        txtNamaMember.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNamaMember.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        txtNamaMember.setPlaceholder("NAMA MEMBER");

        btnMember.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/search.png"))); // NOI18N
        btnMember.setMnemonic('m');
        btnMember.setText("CARI");
        btnMember.setToolTipText("CARI MEMBER");
        btnMember.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemberActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Arial Narrow", 1, 36)); // NOI18N
        jLabel12.setForeground(java.awt.Color.red);
        jLabel12.setText("TOTAL AKHIR : Rp.");

        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/update_1_1.png"))); // NOI18N
        btnReset.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        jLabel3.setText("NO. TRANSAKSI : ");

        jLabel38.setText("TANGGAL  : ");

        jLabel42.setText("KODE MEMBER  : ");

        jLabel43.setText("NAMA MEMBER : ");

        txtTotalAkhir.setEditable(false);
        txtTotalAkhir.setBackground(new java.awt.Color(0, 0, 0));
        txtTotalAkhir.setForeground(new java.awt.Color(255, 0, 0));
        txtTotalAkhir.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTotalAkhir.setFont(new java.awt.Font("Arial Narrow", 1, 40)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel38))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNoFaktur, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dtTglPenjualan, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43)
                    .addComponent(jLabel42))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtIdMember, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                    .addComponent(txtNamaMember, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnMember, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTotalAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtIdMember, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNoFaktur, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnMember, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(dtTglPenjualan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNamaMember, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))))
            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(txtTotalAkhir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "(+) DETAIL TRANSAKSI", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 12))); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel5.setForeground(java.awt.Color.red);
        jLabel5.setText("* HARGA BELI ");

        jLabel6.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel6.setForeground(java.awt.Color.red);
        jLabel6.setText("* HARGA JUAL");

        jLabel7.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel7.setForeground(java.awt.Color.red);
        jLabel7.setText("* QTY");

        txtHargaJual.setBackground(new java.awt.Color(0, 204, 204));
        txtHargaJual.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        txtHargaJual.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtHargaJualKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHargaJualKeyTyped(evt);
            }
        });

        txtHargaBeli.setEditable(false);
        txtHargaBeli.setBackground(new java.awt.Color(0, 204, 204));
        txtHargaBeli.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N

        txtJumlah.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtJumlah.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        txtJumlah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtJumlahActionPerformed(evt);
            }
        });
        txtJumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtJumlahKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtJumlahKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtJumlahKeyTyped(evt);
            }
        });

        txtDaftarBrg.setBackground(java.awt.Color.black);
        txtDaftarBrg.setForeground(java.awt.Color.orange);
        txtDaftarBrg.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDaftarBrg.setFont(new java.awt.Font("Arial Narrow", 1, 20)); // NOI18N
        txtDaftarBrg.setPlaceholder(" -- > KODE");
        txtDaftarBrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDaftarBrgActionPerformed(evt);
            }
        });
        txtDaftarBrg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDaftarBrgKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDaftarBrgKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDaftarBrgKeyTyped(evt);
            }
        });

        txtNamaBrg.setEditable(false);
        txtNamaBrg.setBackground(new java.awt.Color(0, 204, 204));
        txtNamaBrg.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNamaBrg.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        txtNamaBrg.setPlaceholder("NAMA BARANG");

        txtStok.setEditable(false);
        txtStok.setBackground(new java.awt.Color(0, 204, 204));
        txtStok.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtStok.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N

        jLabel13.setText("STOK");

        btnDaftarBrg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/search.png"))); // NOI18N
        btnDaftarBrg.setMnemonic('b');
        btnDaftarBrg.setText("CARI");
        btnDaftarBrg.setToolTipText("CARI BARANG");
        btnDaftarBrg.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDaftarBrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDaftarBrgActionPerformed(evt);
            }
        });

        jLabel40.setText("NAMA BARANG");

        jLabel41.setText("KODE BARANG");

        jLabel44.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel44.setText("->");

        jLabel45.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel45.setForeground(java.awt.Color.red);
        jLabel45.setText("* SUBTOTAL");

        jPanel15.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        btnTambahBrg.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnTambahBrg.setText("TAMBAH");
        btnTambahBrg.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTambahBrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahBrgActionPerformed(evt);
            }
        });

        jLabel53.setText("KLIK 2X UNTUK MEMBATALKAN ITEM BARANG !");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 604, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnTambahBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnTambahBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        tbDetailPenjualan.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        tbDetailPenjualan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "NO FAKTUR", "KODE BARANG", "NAMA BARANG", "HARGA BELI", "HARGA JUAL", "JUMLAH", "SUBTOTAL"
            }
        ));
        tbDetailPenjualan.setRowHeight(35);
        tbDetailPenjualan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDetailPenjualanMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbDetailPenjualan);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1133, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("DAFTAR PENJULAN BARANG", jPanel16);

        txtDataAP.setColumns(20);
        txtDataAP.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        txtDataAP.setRows(5);
        jScrollPane5.setViewportView(txtDataAP);

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 3, true), "Minimum Confidence : "));

        jCon.setModel(new javax.swing.SpinnerNumberModel(5, 5, 100, 5));

        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel48.setText("LEVEL MINIMUM TINGKAT KEPERCAYAAN");

        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel49.setText("ANTARA SUATU ITEM BARANG ");

        vap.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel50.setText("KODE BARANG : ");

        jLabel51.setText("NAMA BARANG : ");

        vnap.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        vcout.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel52.setText("QTY TRANS : ");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(vap)
                    .addComponent(vnap)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel50)
                            .addComponent(jLabel51))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jCon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                        .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vcout, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel48)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel49)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel50)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(vap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel51)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(vnap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(vcout)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10, 10, 10))))
        );

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 3, true), "Minimum Support : "));

        jSup.setModel(new javax.swing.SpinnerNumberModel(5, 5, 100, 5));

        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText("NILAI MINIMUM DARI JUMLAH TRANSAKSI ");

        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel47.setText("YANG MEMBELI BARANG YANG SAMA");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSup, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel46)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel47)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnApriori.setText("ANALISA KERANJANG (APRIORI)");
        btnApriori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAprioriActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnApriori, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 693, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(btnApriori, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("ANALISA APRIORI", jPanel17);

        txtSubtotal.setBackground(new java.awt.Color(0, 204, 204));
        txtSubtotal.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        txtSubtotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSubtotalKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel41)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(txtDaftarBrg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(0, 0, 0)
                                .addComponent(btnDaftarBrg)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNamaBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHargaBeli, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                            .addComponent(txtStok, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtJumlah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                        .addGap(0, 0, 0)
                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2))
                            .addComponent(txtSubtotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jTabbedPane2)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel45, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnDaftarBrg, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                        .addComponent(txtJumlah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtHargaJual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtStok, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtHargaBeli, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtDaftarBrg, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtNamaBrg, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jTabbedPane2))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "(+) INPUT PEMBAYARAN"));

        jLabel10.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        jLabel10.setText("JUMLAH BAYAR : Rp. ");

        jLabel11.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        jLabel11.setText("KEMBALI              : Rp. ");

        txtBayar.setBackground(new java.awt.Color(0, 0, 0));
        txtBayar.setForeground(new java.awt.Color(255, 0, 0));
        txtBayar.setFont(new java.awt.Font("Arial Narrow", 1, 30)); // NOI18N
        txtBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBayarActionPerformed(evt);
            }
        });
        txtBayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBayarKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBayarKeyTyped(evt);
            }
        });

        txtKembalian.setEditable(false);
        txtKembalian.setBackground(new java.awt.Color(0, 0, 0));
        txtKembalian.setForeground(new java.awt.Color(255, 0, 0));
        txtKembalian.setFont(new java.awt.Font("Arial Narrow", 1, 30)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtBayar, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addComponent(txtKembalian, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtKembalian, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/save_add.png"))); // NOI18N
        btnSimpan.setMnemonic('s');
        btnSimpan.setText("SIMPAN");
        btnSimpan.setToolTipText("SIMPAN DATA");
        btnSimpan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSimpan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSimpan.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });
        btnSimpan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSimpanKeyPressed(evt);
            }
        });

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "(+) INPUT DISKON"));

        jLabel8.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        jLabel8.setText("TOTAL     :   Rp. ");

        jLabel9.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        jLabel9.setText("DISKON   : ");

        txtPersen.setBackground(new java.awt.Color(0, 0, 0));
        txtPersen.setForeground(new java.awt.Color(255, 0, 0));
        txtPersen.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPersen.setFont(new java.awt.Font("Arial Narrow", 1, 30)); // NOI18N
        txtPersen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPersenActionPerformed(evt);
            }
        });
        txtPersen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPersenKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPersenKeyTyped(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Arial Narrow", 1, 20)); // NOI18N
        jLabel14.setText("% Rp.");

        txtTotal.setEditable(false);
        txtTotal.setBackground(new java.awt.Color(0, 0, 0));
        txtTotal.setForeground(new java.awt.Color(255, 0, 0));
        txtTotal.setFont(new java.awt.Font("Arial Narrow", 1, 30)); // NOI18N

        txtDiskon.setEditable(false);
        txtDiskon.setBackground(new java.awt.Color(0, 0, 0));
        txtDiskon.setForeground(new java.awt.Color(255, 0, 0));
        txtDiskon.setFont(new java.awt.Font("Arial Narrow", 1, 30)); // NOI18N
        txtDiskon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDiskonKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDiskonKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(txtPersen, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDiskon, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE))
                    .addComponent(txtTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtPersen, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtDiskon, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0))
        );

        btnDataPenjualan.setBackground(java.awt.Color.green);
        btnDataPenjualan.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDataPenjualan.setText("LIHAT DATA PENJUALAN");
        btnDataPenjualan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDataPenjualan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDataPenjualan.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDataPenjualan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataPenjualanActionPerformed(evt);
            }
        });

        btnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/refresh_1.png"))); // NOI18N
        btnBatal.setMnemonic('c');
        btnBatal.setText("BATAL");
        btnBatal.setToolTipText("RESET");
        btnBatal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBatal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBatal.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });

        btnUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/save_add.png"))); // NOI18N
        btnUbah.setText("SIMPAN PERUBAHAN");
        btnUbah.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUbah.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUbah.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(btnSimpan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUbah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBatal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDataPenjualan))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lblKembali)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnSimpan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDataPenjualan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnBatal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnUbah, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblKembali))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 115, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(0, 204, 204));
        jPanel7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Shopping_Cart.png"))); // NOI18N

        jLabel20.setFont(new java.awt.Font("Agency FB", 3, 30)); // NOI18N
        jLabel20.setForeground(java.awt.Color.red);
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel20.setText("SAMSUNG");

        jLabel21.setFont(new java.awt.Font("Agency FB", 3, 30)); // NOI18N
        jLabel21.setForeground(java.awt.Color.blue);
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel21.setText("ASUS");

        jLabel22.setFont(new java.awt.Font("Agency FB", 3, 30)); // NOI18N
        jLabel22.setForeground(java.awt.Color.blue);
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel22.setText("TOSHIBA");

        jLabel23.setFont(new java.awt.Font("Agency FB", 3, 30)); // NOI18N
        jLabel23.setForeground(java.awt.Color.red);
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel23.setText("ACER");

        jLabel24.setFont(new java.awt.Font("Agency FB", 3, 30)); // NOI18N
        jLabel24.setForeground(java.awt.Color.blue);
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel24.setText("SHARP");

        jLabel25.setFont(new java.awt.Font("Agency FB", 3, 30)); // NOI18N
        jLabel25.setForeground(java.awt.Color.red);
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel25.setText("PANASONIC");

        jLabel26.setFont(new java.awt.Font("Agency FB", 3, 30)); // NOI18N
        jLabel26.setForeground(java.awt.Color.red);
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel26.setText("POLYTRON");

        jLabel27.setFont(new java.awt.Font("Agency FB", 3, 30)); // NOI18N
        jLabel27.setForeground(java.awt.Color.blue);
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel27.setText("AXIOO");

        jLabel28.setFont(new java.awt.Font("Agency FB", 3, 30)); // NOI18N
        jLabel28.setForeground(java.awt.Color.red);
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel28.setText("SONY");

        jLabel29.setFont(new java.awt.Font("Agency FB", 3, 30)); // NOI18N
        jLabel29.setForeground(java.awt.Color.blue);
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel29.setText("DELL");

        jLabel30.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        jLabel30.setForeground(java.awt.Color.blue);
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("MARKET");

        jLabel31.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        jLabel31.setForeground(java.awt.Color.red);
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("LUBADA JAYA");

        lbljam.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        lbljam.setForeground(java.awt.Color.red);
        lbljam.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbljam.setOpaque(true);

        lblUser.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        lblUser.setForeground(java.awt.Color.red);
        lblUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblUser1.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        lblUser1.setForeground(java.awt.Color.red);
        lblUser1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUser1.setText("- *HAK AKSES :");

        lblUsersistem.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        lblUsersistem.setForeground(java.awt.Color.red);
        lblUsersistem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblUser3.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        lblUser3.setForeground(java.awt.Color.red);
        lblUser3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUser3.setText("*USER SISTEM :");

        jLabel36.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("FORM TRANSAKSI PENJUALAN");
        jLabel36.setOpaque(true);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblUser3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblUsersistem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblUser1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblUser))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(lbljam, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(jLabel20)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbljam)
                            .addComponent(jLabel36))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(jLabel24)
                            .addComponent(jLabel25)
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28)))
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel30)
                            .addComponent(lblUser)
                            .addComponent(lblUser1)
                            .addComponent(lblUsersistem)
                            .addComponent(lblUser3))))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "-- DETAIL PRODUK --"));

        txtDes.setEditable(false);
        txtDes.setBackground(new java.awt.Color(0, 0, 0));
        txtDes.setColumns(20);
        txtDes.setForeground(new java.awt.Color(255, 255, 255));
        txtDes.setRows(5);
        jScrollPane6.setViewportView(txtDes);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane6)
                .addContainerGap())
        );

        jLabel4.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel4.setForeground(java.awt.Color.red);
        jLabel4.setText("*Tekan Enter Setelah Pengisian Field !");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jLabel4))
        );

        javax.swing.GroupLayout IfrPenjualanLayout = new javax.swing.GroupLayout(IfrPenjualan.getContentPane());
        IfrPenjualan.getContentPane().setLayout(IfrPenjualanLayout);
        IfrPenjualanLayout.setHorizontalGroup(
            IfrPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        IfrPenjualanLayout.setVerticalGroup(
            IfrPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IfrPenjualanLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(189, Short.MAX_VALUE))
        );

        Daftar_Barang.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        Daftar_Barang.setTitle("DATA BARANG - LUBADA JAYA MARKET");

        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 14))); // NOI18N

        tbDataBarang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbDataBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDataBarangMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbDataBarang);

        lblRecordBarang.setFont(new java.awt.Font("Arial Narrow", 1, 14)); // NOI18N
        lblRecordBarang.setText("jLabel1");

        jPanel8.setBackground(new java.awt.Color(0, 204, 204));

        jPanel9.setBackground(new java.awt.Color(0, 204, 204));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(java.awt.Color.lightGray, 1, true), "-- FILTER DATA --", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Agency FB", 1, 12))); // NOI18N

        txtCariBrg.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        txtCariBrg.setPlaceholder("NAMA BARANG");
        txtCariBrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariBrgActionPerformed(evt);
            }
        });

        cmbKat.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        cmbKat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- PILIH KATEGORI BARANG --" }));
        cmbKat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbKatActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("CARI :");

        jLabel18.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("HARGA : ");

        txtCariHarga.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        txtCariHarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariHargaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbKat, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCariBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCariHarga, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCariBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbKat, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtCariHarga, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel32.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        jLabel32.setForeground(java.awt.Color.red);
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("LUBADA JAYA");

        jLabel33.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        jLabel33.setForeground(java.awt.Color.blue);
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("MARKET");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel33))
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout Daftar_BarangLayout = new javax.swing.GroupLayout(Daftar_Barang.getContentPane());
        Daftar_Barang.getContentPane().setLayout(Daftar_BarangLayout);
        Daftar_BarangLayout.setHorizontalGroup(
            Daftar_BarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(Daftar_BarangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Daftar_BarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(Daftar_BarangLayout.createSequentialGroup()
                        .addComponent(lblRecordBarang)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        Daftar_BarangLayout.setVerticalGroup(
            Daftar_BarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Daftar_BarangLayout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblRecordBarang))
        );

        jScrollPane4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 14))); // NOI18N

        tbDataMember.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbDataMember.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDataMemberMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tbDataMember);

        lblRecordMember.setFont(new java.awt.Font("Arial Narrow", 1, 14)); // NOI18N
        lblRecordMember.setText("jLabel1");

        jPanel10.setBackground(new java.awt.Color(0, 204, 204));

        jPanel11.setBackground(new java.awt.Color(0, 204, 204));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "--FILTER DATA --"));

        txtCariMember.setPlaceholder("PENCARIAN :  ID / NAMA MEMBER");
        txtCariMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariMemberActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtCariMember, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(txtCariMember, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel34.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        jLabel34.setForeground(java.awt.Color.red);
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("LUBADA JAYA");

        jLabel35.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        jLabel35.setForeground(java.awt.Color.blue);
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("MARKET");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel35))
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout Daftar_MemberLayout = new javax.swing.GroupLayout(Daftar_Member.getContentPane());
        Daftar_Member.getContentPane().setLayout(Daftar_MemberLayout);
        Daftar_MemberLayout.setHorizontalGroup(
            Daftar_MemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(Daftar_MemberLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Daftar_MemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 887, Short.MAX_VALUE)
                    .addGroup(Daftar_MemberLayout.createSequentialGroup()
                        .addComponent(lblRecordMember)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        Daftar_MemberLayout.setVerticalGroup(
            Daftar_MemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Daftar_MemberLayout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblRecordMember))
        );

        jPanel13.setBackground(new java.awt.Color(0, 204, 204));

        jLabel37.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        jLabel37.setForeground(java.awt.Color.blue);
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("EXPORT TO EXCEL");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel37)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel37)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel14.setBorder(new javax.swing.border.LineBorder(java.awt.Color.lightGray, 1, true));

        jLabel1.setText("LOKASI FILE");

        txtDir.setEditable(false);

        btnPilihLokasi.setText("PILIH LOKASI FILE");
        btnPilihLokasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihLokasiActionPerformed(evt);
            }
        });

        btnSimpanBackup.setText("SIMPAN");
        btnSimpanBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanBackupActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtDir)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addComponent(btnSimpanBackup)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 202, Short.MAX_VALUE)
                        .addComponent(btnPilihLokasi)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPilihLokasi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSimpanBackup, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("BACKUP", jPanel14);

        javax.swing.GroupLayout BackUp_RestoreLayout = new javax.swing.GroupLayout(BackUp_Restore.getContentPane());
        BackUp_Restore.getContentPane().setLayout(BackUp_RestoreLayout);
        BackUp_RestoreLayout.setHorizontalGroup(
            BackUp_RestoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(BackUp_RestoreLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        BackUp_RestoreLayout.setVerticalGroup(
            BackUp_RestoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BackUp_RestoreLayout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1))
        );

        setClosable(true);
        setTitle("TRANSAKSI PENJUALAN-LUBADAJAYA MARKET");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/cetak-faktur.png"))); // NOI18N
        setPreferredSize(new java.awt.Dimension(1205, 585));

        panelPenjualan.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Agency FB", 1, 14))); // NOI18N
        panelPenjualan.setPreferredSize(new java.awt.Dimension(1184, 560));

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "KLIK 1X --> UNTUK MENCETAK FAKTUR    KLIK 2X --> UNTUK MELIHAT BARANG YANG DIBELI ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        tbDataPenjualan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbDataPenjualan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDataPenjualanMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbDataPenjualan);

        lblRecordPenjualan.setFont(new java.awt.Font("Arial Narrow", 1, 12)); // NOI18N
        lblRecordPenjualan.setText("RECORD : 0");

        txtCari.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCari.setPlaceholder("NO.FAKTUR PENJUALAN");
        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCariKeyTyped(evt);
            }
        });

        btnCetakFaktur.setFont(new java.awt.Font("Arial Narrow", 1, 14)); // NOI18N
        btnCetakFaktur.setText("CETAK FAKTUR");
        btnCetakFaktur.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCetakFaktur.setOpaque(false);
        btnCetakFaktur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakFakturActionPerformed(evt);
            }
        });

        lblNoFak.setFont(new java.awt.Font("Agency FB", 3, 24)); // NOI18N
        lblNoFak.setForeground(java.awt.Color.red);
        lblNoFak.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        btnTambah.setFont(new java.awt.Font("Arial Narrow", 1, 14)); // NOI18N
        btnTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/insert.png"))); // NOI18N
        btnTambah.setText("TAMBAH");
        btnTambah.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTambah.setOpaque(false);
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnBackUp.setFont(new java.awt.Font("Arial Narrow", 1, 14)); // NOI18N
        btnBackUp.setText("EXPORT TO EXCEL");
        btnBackUp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBackUp.setOpaque(false);
        btnBackUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackUpActionPerformed(evt);
            }
        });

        btnCariTgl.setFont(new java.awt.Font("Arial Narrow", 1, 14)); // NOI18N
        btnCariTgl.setText("CARI");
        btnCariTgl.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCariTgl.setOpaque(false);
        btnCariTgl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariTglActionPerformed(evt);
            }
        });

        dtTglCari.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        dtTglCari.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dtTglCariPropertyChange(evt);
            }
        });

        dtTglCariAkhir.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        dtTglCariAkhir.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dtTglCariAkhirPropertyChange(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel15.setText("CARI :");

        jLabel16.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel16.setText("S.D");

        btnRefresh.setFont(new java.awt.Font("Arial Narrow", 1, 14)); // NOI18N
        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/update_1_1.png"))); // NOI18N
        btnRefresh.setText("Refresh");
        btnRefresh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRefresh.setOpaque(false);
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelPenjualanLayout = new javax.swing.GroupLayout(panelPenjualan);
        panelPenjualan.setLayout(panelPenjualanLayout);
        panelPenjualanLayout.setHorizontalGroup(
            panelPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPenjualanLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(panelPenjualanLayout.createSequentialGroup()
                        .addComponent(lblRecordPenjualan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dtTglCari, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16)
                        .addGap(2, 2, 2)
                        .addComponent(dtTglCariAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCariTgl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPenjualanLayout.createSequentialGroup()
                        .addComponent(btnBackUp, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnCetakFaktur)
                        .addGap(0, 0, 0)
                        .addComponent(btnRefresh)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblNoFak, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        panelPenjualanLayout.setVerticalGroup(
            panelPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPenjualanLayout.createSequentialGroup()
                .addGroup(panelPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNoFak, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnCetakFaktur, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                        .addComponent(btnBackUp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addGroup(panelPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCariTgl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(dtTglCariAkhir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblRecordPenjualan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnTambah, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(dtTglCari, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)))
        );

        jTabbedPane3.addTab("DATA PENJUALAN", panelPenjualan);

        panelDetPenjualan.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Agency FB", 1, 14))); // NOI18N
        panelDetPenjualan.setPreferredSize(new java.awt.Dimension(1184, 560));

        jScrollPane7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "DATA DETAIL PENJUALAN BARANG", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        tbDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane7.setViewportView(tbDetail);

        lblDetail.setFont(new java.awt.Font("Arial Narrow", 1, 12)); // NOI18N
        lblDetail.setText("RECORD : 0");

        txtCariDet.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCariDet.setPlaceholder("NO.FAKTUR PENJUALAN");
        txtCariDet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCariDetKeyTyped(evt);
            }
        });

        btnCariTglDet.setFont(new java.awt.Font("Arial Narrow", 1, 14)); // NOI18N
        btnCariTglDet.setText("CARI");
        btnCariTglDet.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCariTglDet.setOpaque(false);
        btnCariTglDet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariTglDetActionPerformed(evt);
            }
        });

        dtTglCariDet.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        dtTglCariDet.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dtTglCariDetPropertyChange(evt);
            }
        });

        dtTglCariAkhirDet.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        dtTglCariAkhirDet.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dtTglCariAkhirDetPropertyChange(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel19.setText("CARI :");

        jLabel39.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel39.setText("S.D");

        javax.swing.GroupLayout panelDetPenjualanLayout = new javax.swing.GroupLayout(panelDetPenjualan);
        panelDetPenjualan.setLayout(panelDetPenjualanLayout);
        panelDetPenjualanLayout.setHorizontalGroup(
            panelDetPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDetPenjualanLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDetPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7)
                    .addGroup(panelDetPenjualanLayout.createSequentialGroup()
                        .addComponent(lblDetail)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dtTglCariDet, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel39)
                        .addGap(2, 2, 2)
                        .addComponent(dtTglCariAkhirDet, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCariTglDet)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 410, Short.MAX_VALUE)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCariDet, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        panelDetPenjualanLayout.setVerticalGroup(
            panelDetPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDetPenjualanLayout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDetPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCariDet, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addGroup(panelDetPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnCariTglDet, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(dtTglCariAkhirDet, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblDetail, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dtTglCariDet, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)))
        );

        jTabbedPane3.addTab("DATA DETAIL PENJUALAN", panelDetPenjualan);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1485, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCariBrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariBrgActionPerformed
        setTabelBarang();
        showDataBarang();
        Daftar_Barang.setVisible(true);
        Daftar_Barang.setSize(1200, 600);
    }//GEN-LAST:event_txtCariBrgActionPerformed

    private void tbDataBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDataBarangMouseClicked
        if(evt.getClickCount()==1){
            vid_barang = tbDataBarang.getValueAt(tbDataBarang.getSelectedRow(), 1).toString();
             getDataBarang();
       }                       
    }//GEN-LAST:event_tbDataBarangMouseClicked

    private void txtJumlahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtJumlahActionPerformed
        if(txtJumlah.getText().equals("0") || txtJumlah.getText().equals("00")){
            JOptionPane.showMessageDialog(null, "JUMLAH BARANG HARUS LEBIH DARI 0 !",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            txtJumlah.requestFocus();
        }else{
            tambahBarang();
        }
    }//GEN-LAST:event_txtJumlahActionPerformed

    private void txtBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBayarActionPerformed
        hitungKembalian(); txtDaftarBrg.requestFocus();
    }//GEN-LAST:event_txtBayarActionPerformed

    private void tbDataPenjualanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDataPenjualanMouseClicked
        if(evt.getClickCount()==2){
            IfrPenjualan.setExtendedState(JFrame.MAXIMIZED_BOTH); 
            IfrPenjualan.setUndecorated(true);
            IfrPenjualan.setVisible(true);
            setTabel_TransPenjualan();
            vno_faktur = tbDataPenjualan.getValueAt(tbDataPenjualan.getSelectedRow(), 0).toString();
            enableForm(); getDataPenjualan();
        }else {
            vno_faktur = tbDataPenjualan.getValueAt(tbDataPenjualan.getSelectedRow(), 0).toString();
            setTabel_TransPenjualan(); getDataPenjualan(); 
        }
    }//GEN-LAST:event_tbDataPenjualanMouseClicked

    private void tbDataMemberMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDataMemberMouseClicked
        if(evt.getClickCount()==1){
            vid_member = tbDataMember.getValueAt(tbDataMember.getSelectedRow(), 1).toString();
             tampilMember();
       } 
    }//GEN-LAST:event_tbDataMemberMouseClicked

    private void txtCariMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariMemberActionPerformed
        setTabelMember();
        showDataMember();
        Daftar_Member.setVisible(true);
        Daftar_Member.setSize(920, 532);
        Daftar_Member.setLocationRelativeTo(this);
    }//GEN-LAST:event_txtCariMemberActionPerformed

    private void txtDaftarBrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDaftarBrgActionPerformed
        vid_barang=txtDaftarBrg.getText();
        getDataBarang();
    }//GEN-LAST:event_txtDaftarBrgActionPerformed

    private void txtIdMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdMemberActionPerformed
        vid_member=txtIdMember.getText();
        tampilMember();
    }//GEN-LAST:event_txtIdMemberActionPerformed

    private void btnMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMemberActionPerformed
        Daftar_Member.setVisible(true);
        Daftar_Member.setSize(920, 532);
        Daftar_Member.setLocationRelativeTo(this);
        setTabelMember();
        showDataMember();
    }//GEN-LAST:event_btnMemberActionPerformed

    private void btnDaftarBrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDaftarBrgActionPerformed
        Daftar_Barang.setVisible(true);
        Daftar_Barang.setSize(1200, 600);
        listKat();
        setTabelBarang();
        showDataBarang();
    }//GEN-LAST:event_btnDaftarBrgActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if( txtIdMember.getText().length() > 0 && txtNamaMember.getText().length() == 0 ){
            JOptionPane.showMessageDialog(null, "MEMBER TIDAK DITEMUKAN !",
                    "Error", JOptionPane.ERROR_MESSAGE); txtIdMember.requestFocus();
        }else if( txtNamaMember.getText().length() > 0 && txtIdMember.getText().length() == 0 ){
            JOptionPane.showMessageDialog(null, "MEMBER TIDAK DITEMUKAN !",
                    "Error", JOptionPane.ERROR_MESSAGE); txtIdMember.requestFocus();
        }else if(txtTotal.getText().equals("0")){
            JOptionPane.showMessageDialog(null, "TIDAK ADA BARANG YANG DIBELI !",
                    "Error", JOptionPane.ERROR_MESSAGE); txtDaftarBrg.requestFocus();
        }else if(Long.parseLong(txtTotal.getText()) * Long.parseLong(txtPersen.getText()) / 100 != Long.parseLong(txtDiskon.getText()) ){
            JOptionPane.showMessageDialog(null, "TEKAN ENTER SETELAH PENGISIAN FIELD DISKON !",
                    "Error", JOptionPane.ERROR_MESSAGE); txtPersen.requestFocus();
        }else if(txtBayar.getText().equals("0")){
            JOptionPane.showMessageDialog(null, "TRANSAKSI "+txtNoFaktur.getText()+ " BELUM DIBAYAR !",
                    "Error", JOptionPane.ERROR_MESSAGE); txtBayar.requestFocus();
        }else if(Long.parseLong(txtTotalAkhir.getText()) + Long.parseLong(txtKembalian.getText()) != Long.parseLong(txtBayar.getText()) ){
            JOptionPane.showMessageDialog(null, "TEKAN ENTER SETELAH PENGISIAN FIELD PEMBAYARAN !",
                    "Error", JOptionPane.ERROR_MESSAGE); txtBayar.requestFocus();
        }else{
            aksiSimpanPenjualan();
            tblDetail_Penjualan.getDataVector().removeAllElements();
            tblDetail_Penjualan.fireTableDataChanged();
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnDataPenjualanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataPenjualanActionPerformed
        int jumlah_baris = tblDetail_Penjualan.getRowCount();
        if(jumlah_baris > 0 && btnSimpan.getText().equals("SIMPAN")){
            int jawab = JOptionPane.showConfirmDialog(null, 
                "ANDA YAKIN INGIN KELUAR DARI TRANSAKSI INI ?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if(jawab==JOptionPane.YES_OPTION){
                aksiReset(); clearForm(); enableForm(); IfrPenjualan.dispose(); showDataPenjualan();
                btnSimpan.setVisible(true); btnSimpan.setText("SIMPAN"); btnUbah.setVisible(false); 
                btnDaftarBrg.setEnabled(true); 
            }
        }else if(jumlah_baris > 0 && btnSimpan.getText().equals("")){
            int jawab = JOptionPane.showConfirmDialog(null, 
                "ANDA YAKIN INGIN KELUAR DARI TRANSAKSI INI ?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if(jawab==JOptionPane.YES_OPTION){
                clearForm(); enableForm(); IfrPenjualan.dispose(); showDataPenjualan();
                btnSimpan.setVisible(true); btnSimpan.setText("SIMPAN"); btnUbah.setVisible(false); 
                btnDaftarBrg.setEnabled(true); 
            }
        }else{
            clearForm(); enableForm(); IfrPenjualan.dispose(); showDataPenjualan();
            btnSimpan.setVisible(true); btnSimpan.setText("SIMPAN"); btnUbah.setVisible(false); 
            btnDaftarBrg.setEnabled(true); 
        }
    }//GEN-LAST:event_btnDataPenjualanActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        int jumlah_baris = tblDetail_Penjualan.getRowCount();
        if(jumlah_baris > 0 && btnSimpan.getText().equals("SIMPAN")){
            int jawab = JOptionPane.showConfirmDialog(null, 
                "ANDA YAKIN INGIN MEMBATALKAN TRANSAKSI "+txtNoFaktur.getText()+" INI ?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if(jawab==JOptionPane.YES_OPTION){
                aksiReset(); clearForm(); enableForm(); btnSimpan.setVisible(true); 
                btnSimpan.setText("SIMPAN"); btnUbah.setVisible(false);txtTotalAkhir.setText(""); txtPersen.setText("");
                tblDetail_Penjualan.getDataVector().removeAllElements();
                tblDetail_Penjualan.fireTableDataChanged(); autoNoFaktur();
                txtDaftarBrg.setEditable(true); btnDaftarBrg.setEnabled(true);
                txtJumlah.setEditable(true); btnReset.setEnabled(true); 
            }
        }else if(jumlah_baris > 0 && btnSimpan.getText().equals("")){
            int jawab = JOptionPane.showConfirmDialog(null, 
                "APAKAH ANDA AKAN AKAN MENYIMPAN PERUBAHAN INI ?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if(jawab==JOptionPane.YES_OPTION){
                aksiUbahPenjualan(); clearForm(); enableForm(); IfrPenjualan.dispose(); showDataPenjualan();
                btnSimpan.setVisible(true); btnSimpan.setText("SIMPAN"); btnUbah.setVisible(false); 
                btnDaftarBrg.setEnabled(true); 
            }else if(jawab==JOptionPane.NO_OPTION){
                clearForm(); enableForm(); btnSimpan.setVisible(true); 
                btnSimpan.setText("SIMPAN"); btnUbah.setVisible(false);txtTotalAkhir.setText(""); txtPersen.setText("");
                tblDetail_Penjualan.getDataVector().removeAllElements();
                tblDetail_Penjualan.fireTableDataChanged(); autoNoFaktur();
                txtDaftarBrg.setEditable(true); btnDaftarBrg.setEnabled(true);
                txtJumlah.setEditable(true); btnReset.setEnabled(true); 
            }
        }else{
            clearForm(); enableForm(); IfrPenjualan.dispose(); showDataPenjualan();
            btnSimpan.setVisible(true); btnSimpan.setText("SIMPAN"); btnUbah.setVisible(false); 
            btnDaftarBrg.setEnabled(true); 
        }
    }//GEN-LAST:event_btnBatalActionPerformed

    private void tbDetailPenjualanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDetailPenjualanMouseClicked
        if(btnSimpan.getText().equals("")){
            if(evt.getClickCount()==2){
                vid_barang = tbDetailPenjualan.getValueAt(tbDetailPenjualan.getSelectedRow(), 1).toString();
                vjumlah_barang = (long) tbDetailPenjualan.getValueAt(tbDetailPenjualan.getSelectedRow(), 5);
                txtDaftarBrg.setText(vid_barang);
                txtJumlah.setText(String.valueOf(vjumlah_barang));
                getDetail();
                hitungSubtotal();
                hapusBarang();
                getDetail();
            }
        }else{
            if(evt.getClickCount()==2){
                vid_barang = tbDetailPenjualan.getValueAt(tbDetailPenjualan.getSelectedRow(), 1).toString();
                getDataBarang();
                vjumlah_barang = (long) tbDetailPenjualan.getValueAt(tbDetailPenjualan.getSelectedRow(), 5);
                txtJumlah.setText(String.valueOf(vjumlah_barang));
                hitungSubtotal();
                hapusBarang();
                getDataBarang();
            }
        }
    }//GEN-LAST:event_tbDetailPenjualanMouseClicked

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        if( txtIdMember.getText().length() > 0 && txtNamaMember.getText().length() == 0 ){
            JOptionPane.showMessageDialog(null, "MEMBER TIDAK DITEMUKAN !",
                    "Error", JOptionPane.ERROR_MESSAGE); txtIdMember.requestFocus();
        }else if( txtNamaMember.getText().length() > 0 && txtIdMember.getText().length() == 0 ){
            JOptionPane.showMessageDialog(null, "MEMBER TIDAK DITEMUKAN !",
                    "Error", JOptionPane.ERROR_MESSAGE); txtIdMember.requestFocus();
        }else if(Long.parseLong(txtTotal.getText()) * Long.parseLong(txtPersen.getText()) / 100 != Long.parseLong(txtDiskon.getText()) ){
            JOptionPane.showMessageDialog(null, "TEKAN ENTER SETELAH PENGISIAN FIELD DISKON !",
                    "Error", JOptionPane.ERROR_MESSAGE); txtPersen.requestFocus();
        }else if(Long.parseLong(txtTotalAkhir.getText()) + Long.parseLong(txtKembalian.getText()) != Long.parseLong(txtBayar.getText()) ){
            JOptionPane.showMessageDialog(null, "TEKAN ENTER SETELAH PENGISIAN FIELD PEMBAYARAN !",
                    "Error", JOptionPane.ERROR_MESSAGE); txtBayar.requestFocus();
        }else{
            aksiUbahPenjualan();
        }
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnCetakFakturActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakFakturActionPerformed
        if(txtTotal.getText().equals("0")){
            JOptionPane.showMessageDialog(null, "TIDAK ADA DATA YANG DIPILIH !");
        }else{
            cetakFaktur();
        }
    }//GEN-LAST:event_btnCetakFakturActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        btnSimpan.setVisible(true); btnSimpan.setText("SIMPAN"); btnUbah.setVisible(false);
        IfrPenjualan.setExtendedState(JFrame.MAXIMIZED_BOTH);
        IfrPenjualan.setVisible(true);
        autoNoFaktur(); clearForm(); enableForm(); txtTotalAkhir.setText(""); txtPersen.setText(""); setTabel_TransPenjualan();
        txtDaftarBrg.setEditable(true); btnDaftarBrg.setEnabled(true);
        txtJumlah.setEditable(true); btnReset.setEnabled(true);
        isidata();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnBackUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackUpActionPerformed
        BackUp_Restore.setVisible(true);
        BackUp_Restore.setSize(500, 300);
        BackUp_Restore.setLocationRelativeTo(this);
    }//GEN-LAST:event_btnBackUpActionPerformed

    private void txtPersenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPersenActionPerformed
        hitungDiskon(); hitungTotalAkhir();
    }//GEN-LAST:event_txtPersenActionPerformed

    private void btnSimpanBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanBackupActionPerformed
        if(txtDir.getText().equals("")){
            JOptionPane.showMessageDialog(null, "LOKASI FILE BELUM DITENTUKAN !"); btnPilihLokasi.requestFocus();
        }else{
            toExcel(tbDataPenjualan, new File(txtDir.getText()));
        }
    }//GEN-LAST:event_btnSimpanBackupActionPerformed

    private void btnPilihLokasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPilihLokasiActionPerformed
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Microsoft Excel Worksheet", "xls" );
        jFileChooser1.setFileFilter(filter);
        int returnVal = jFileChooser1.showSaveDialog(this);
        if(returnVal == jFileChooser1.APPROVE_OPTION) {
           txtDir.setText(jFileChooser1.getSelectedFile().getPath()+".xls");
        }
        //Microsoft Excel Worksheet (.xlsx)
    }//GEN-LAST:event_btnPilihLokasiActionPerformed

    private void txtJumlahKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyTyped
        if ( txtJumlah.getText().length() == 2 ) {
        evt.consume();
    }
    }//GEN-LAST:event_txtJumlahKeyTyped

    private void txtPersenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPersenKeyTyped
        if ( txtPersen.getText().length() == 2 ) {
        evt.consume();
    }
    }//GEN-LAST:event_txtPersenKeyTyped

    private void txtIdMemberKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdMemberKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN || evt.getKeyCode() == evt.VK_ENTER){
            txtDaftarBrg.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtBayar.requestFocus();
        }
    }//GEN-LAST:event_txtIdMemberKeyPressed

    private void txtIdMemberKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdMemberKeyTyped
        char karakter = evt.getKeyChar();
        if(!(((karakter >= '0') && (karakter <= '9') || (karakter == KeyEvent.VK_BACK_SPACE) || (karakter == KeyEvent.VK_DELETE)))){
            getToolkit().beep();
            evt.consume();
        }
        if ( txtIdMember.getText().length() == 10 ) {
        evt.consume();
        }
    }//GEN-LAST:event_txtIdMemberKeyTyped

    private void txtDaftarBrgKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDaftarBrgKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN || evt.getKeyCode() == evt.VK_ENTER){
            txtJumlah.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtPersen.requestFocus();
        }
    }//GEN-LAST:event_txtDaftarBrgKeyPressed

    private void txtDaftarBrgKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDaftarBrgKeyTyped
        if ( txtDaftarBrg.getText().length() == 100 ) {
        evt.consume();
        }
    }//GEN-LAST:event_txtDaftarBrgKeyTyped

    private void txtJumlahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            txtPersen.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtDaftarBrg.requestFocus();
        }
    }//GEN-LAST:event_txtJumlahKeyPressed

    private void txtPersenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPersenKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            txtBayar.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtJumlah.requestFocus();
        }
    }//GEN-LAST:event_txtPersenKeyPressed

    private void txtBayarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBayarKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            btnSimpan.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtPersen.requestFocus();
        }
    }//GEN-LAST:event_txtBayarKeyPressed

    private void btnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSimpanKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            txtIdMember.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtIdMember.requestFocus();
        }
        if(evt.getKeyCode() == evt.VK_ENTER)
        btnBatal.doClick();
    }//GEN-LAST:event_btnSimpanKeyPressed

    private void txtBayarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBayarKeyTyped
        if ( txtBayar.getText().length() == 13 ) {
        evt.consume();
        }
    }//GEN-LAST:event_txtBayarKeyTyped

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        txtIdMember.setEditable(true);
        txtIdMember.setText("");
        txtNamaMember.setText("");
        txtIdMember.requestFocus();
        
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCariTglActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariTglActionPerformed
         cariTransPenjualan();
    }//GEN-LAST:event_btnCariTglActionPerformed

    private void txtCariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyTyped
        cariTransPenjualan();
    }//GEN-LAST:event_txtCariKeyTyped

    private void dtTglCariPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dtTglCariPropertyChange
        dtTglCariAkhir.setDate(dtTglCari.getDate());
        txtCari.setText(""); 
    }//GEN-LAST:event_dtTglCariPropertyChange

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        clearForm(); showDataPenjualan(); showDetail();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void cmbKatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbKatActionPerformed
        txtCariBrg.setText(""); txtCariHarga.setText("");
        setTabelBarang(); showDataBarang();
    }//GEN-LAST:event_cmbKatActionPerformed

    private void dtTglCariAkhirPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dtTglCariAkhirPropertyChange
        tanggal_Pencarian();
    }//GEN-LAST:event_dtTglCariAkhirPropertyChange

    private void txtCariHargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariHargaActionPerformed
        setTabelBarang();
        showDataBarang();
    }//GEN-LAST:event_txtCariHargaActionPerformed

    private void txtCariDetKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariDetKeyTyped
        setTabel_Detail(); cariDetail();
    }//GEN-LAST:event_txtCariDetKeyTyped

    private void btnCariTglDetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariTglDetActionPerformed
        setTabel_Detail(); cariDetail();
    }//GEN-LAST:event_btnCariTglDetActionPerformed

    private void dtTglCariDetPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dtTglCariDetPropertyChange
        dtTglCariAkhirDet.setDate(dtTglCariDet.getDate());
        txtCariDet.setText(""); 
    }//GEN-LAST:event_dtTglCariDetPropertyChange

    private void dtTglCariAkhirDetPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dtTglCariAkhirDetPropertyChange
        tanggal_PencarianDet();
    }//GEN-LAST:event_dtTglCariAkhirDetPropertyChange

    private void txtHargaJualKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHargaJualKeyTyped
        if ( txtHargaJual.getText().length() == 9 ) {
        evt.consume();
    }
    }//GEN-LAST:event_txtHargaJualKeyTyped

    private void txtHargaJualKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHargaJualKeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER){
            txtJumlah.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_DOWN){
            txtJumlah.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtDaftarBrg.requestFocus();
        }
    }//GEN-LAST:event_txtHargaJualKeyPressed

    private void btnTambahBrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahBrgActionPerformed
        tambahBarang();
    }//GEN-LAST:event_btnTambahBrgActionPerformed

    private void txtJumlahKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyReleased
        if(txtJumlah.getText().equals("")){
           txtJumlah.setText("0"); txtSubtotal.setText("0"); 
           txtSubtotal.requestFocus(); txtJumlah.requestFocus();
        }else if(Long.parseLong(txtJumlah.getText()) > Long.parseLong(txtStok.getText()) ){
           txtJumlah.setText("0"); txtSubtotal.setText("0"); 
           txtSubtotal.requestFocus(); txtJumlah.requestFocus();
        }else{
            hitungSubtotal();
        }
    }//GEN-LAST:event_txtJumlahKeyReleased

    private void btnAprioriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAprioriActionPerformed
        c2(); //c3();
    }//GEN-LAST:event_btnAprioriActionPerformed

    private void txtDaftarBrgKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDaftarBrgKeyReleased
        vid_barang= txtDaftarBrg.getText();
        getDataBarang();
    }//GEN-LAST:event_txtDaftarBrgKeyReleased

    private void txtSubtotalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSubtotalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSubtotalKeyPressed

    private void txtDiskonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiskonKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiskonKeyPressed

    private void txtDiskonKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiskonKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiskonKeyTyped

    private void IfrPenjualanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IfrPenjualanKeyReleased
        vid_barang= txtDaftarBrg.getText();
        getDataBarang();
    }//GEN-LAST:event_IfrPenjualanKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFrame BackUp_Restore;
    private javax.swing.JDialog Daftar_Barang;
    private javax.swing.JDialog Daftar_Member;
    private javax.swing.JFrame IfrPenjualan;
    private javax.swing.JButton btnApriori;
    private javax.swing.JButton btnBackUp;
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnCariTgl;
    private javax.swing.JButton btnCariTglDet;
    private javax.swing.JButton btnCetakFaktur;
    private javax.swing.JButton btnDaftarBrg;
    private javax.swing.JButton btnDataPenjualan;
    private javax.swing.JButton btnMember;
    private javax.swing.JButton btnPilihLokasi;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnSimpanBackup;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnTambahBrg;
    private javax.swing.JButton btnUbah;
    private javax.swing.JComboBox<String> cmbKat;
    private com.toedter.calendar.JDateChooser dtTglCari;
    private com.toedter.calendar.JDateChooser dtTglCariAkhir;
    private com.toedter.calendar.JDateChooser dtTglCariAkhirDet;
    private com.toedter.calendar.JDateChooser dtTglCariDet;
    private com.toedter.calendar.JDateChooser dtTglPenjualan;
    private javax.swing.JSpinner jCon;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSpinner jSup;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JLabel lblDetail;
    private javax.swing.JLabel lblKembali;
    private javax.swing.JLabel lblNoFak;
    private javax.swing.JLabel lblRecordBarang;
    private javax.swing.JLabel lblRecordMember;
    private javax.swing.JLabel lblRecordPenjualan;
    private javax.swing.JLabel lblUser;
    private javax.swing.JLabel lblUser1;
    private javax.swing.JLabel lblUser3;
    private javax.swing.JLabel lblUsersistem;
    private javax.swing.JLabel lbljam;
    private javax.swing.JPanel panelDetPenjualan;
    private javax.swing.JPanel panelPenjualan;
    private javax.swing.JTable tbDataBarang;
    private javax.swing.JTable tbDataMember;
    private javax.swing.JTable tbDataPenjualan;
    private javax.swing.JTable tbDetail;
    private javax.swing.JTable tbDetailPenjualan;
    private Tool.NumberTextField txtBayar;
    private Tool.Custom_TextField txtCari;
    private Tool.Custom_TextField txtCariBrg;
    private Tool.Custom_TextField txtCariDet;
    private Tool.NumberTextField txtCariHarga;
    private Tool.Custom_TextField txtCariMember;
    private Tool.Custom_TextField txtDaftarBrg;
    private javax.swing.JTextArea txtDataAP;
    private javax.swing.JTextArea txtDes;
    private javax.swing.JTextField txtDir;
    private Tool.NumberTextField txtDiskon;
    private Tool.NumberTextField txtHargaBeli;
    private Tool.NumberTextField txtHargaJual;
    private Tool.Custom_TextField txtIdMember;
    private Tool.NumberTextField txtJumlah;
    private Tool.NumberTextField txtKembalian;
    private Tool.Custom_TextField txtNamaBrg;
    private Tool.Custom_TextField txtNamaMember;
    private Tool.Custom_TextField txtNoFaktur;
    private Tool.NumberTextField txtPersen;
    private Tool.NumberTextField txtStok;
    private Tool.NumberTextField txtSubtotal;
    private Tool.NumberTextField txtTotal;
    private Tool.NumberTextField txtTotalAkhir;
    private javax.swing.JTextField vap;
    private javax.swing.JTextField vcout;
    private javax.swing.JTextField vnap;
    // End of variables declaration//GEN-END:variables
}
