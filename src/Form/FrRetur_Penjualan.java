package Form;

import Tool.KoneksiDB;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class FrRetur_Penjualan extends javax.swing.JInternalFrame {

    KoneksiDB getCnn = new KoneksiDB();
    Connection _Cnn;
    
    String sqlselect, sqlinsert, sqldelete;
    String vid_barang, vnm_barang;
    String vid_kategori, vnm_kategori;
    String vid_member, vnm_member;
    String vno_transaksi, vno_retur, vno_faktur, vid_user, vtgl_trans, vnm_user, vstatus_brg, vtglawal, vtglakhir;
    
    double vhrg_beli, vhrg_jual, vsubtotal, vtotal, vdiskon, vtotal_akhir, vjumlah_bayar, vkembalian;
    int vjumlah_barang, vqty;
    
    static Object[]Kolom;
    DefaultTableModel tblRetur_Penjualan, tblDetail_Retur_Penjualan, tblTransRetur_Penjualan, tblPenjualan, tblBarang;
    
    SimpleDateFormat tglview = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat tglinput = new SimpleDateFormat("yyyy-MM-dd");
    
    String id = FrMenu.getU_id();
    String vakses = FrMenu.getU_hakakses();
    String vusername = FrMenu.getU_username();
    
    public FrRetur_Penjualan() {
        initComponents();
        
        akses();
        setJam();
        setTabel_Retur_Penjualan();
        showData_Retur_Penjualan();
        clearForm();
        txtNoFaktur.requestFocus();
    }
    
    private void akses(){
        lblUser.setText(vakses);
        lblUsersistem.setText(vusername);
        if(lblUser.getText().equals("KASIR")){
            btnBackUp.setEnabled(false);
            lblUser.setVisible(false);
            lblUsersistem.setVisible(false);
        }else{
            btnBackUp.setEnabled(true);
            lblUser.setVisible(false);
            lblUsersistem.setVisible(false);
        }
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

        lblJam.setText(jam + ":" + menit + ":" + detik);
        }
      };
      new Timer(1000, taskPerformer).start();
    }
    private void clearForm(){
        txtNoFaktur.setText("");
        txtIdMember.setText("");
        txtNmMember.setText("");
        buttonGroup1.clearSelection();
        dtTglRetur.setDate(new Date());
        dtTglCariAwal.setDate(new Date());
        dtTglCariAkhir.setDate(new Date());
        txtDaftarBrg.setText("");
        txtNamaBrg.setText("");
        txtHargaJual.setText("");
        txtQty.setText("");
        txtJumlah.setValue(0);
        txtTotal.setText("");
        txtNoFaktur.requestFocus();
    }
    private void disableForm(){
        rb1.setEnabled(false); rb2.setEnabled(false);
        txtHargaJual.setEnabled(false);
        txtQty.setEnabled(false);
        txtJumlah.setEnabled(false);
        txtDaftarBrg.setEnabled(false);
        txtNamaBrg.setEnabled(false);
        txtTotal.setEnabled(false);
        btnDaftarBrg.setEnabled(false);
        btnDaftarFaktur.setEnabled(false);
        btnTambahBrg.setEnabled(false);
        btnHapusBrg.setEnabled(false);
        btnSimpan.setEnabled(false);
    }
    private void enableForm(){
        rb1.setEnabled(true); rb2.setEnabled(true); 
        txtHargaJual.setEnabled(true);
        txtJumlah.setEnabled(true);
        txtQty.setEnabled(true);
        txtDaftarBrg.setEnabled(true);
        txtNamaBrg.setEnabled(true);
        txtTotal.setEnabled(true);
        btnDaftarBrg.setEnabled(true);
        btnDaftarFaktur.setEnabled(true);
        btnTambahBrg.setEnabled(true);
        btnHapusBrg.setEnabled(false);
        btnSimpan.setEnabled(true);
    }
    private void setTabel_Retur_Penjualan(){
        String[]kolom1 = {"NO. RETUR", "NO. FAKTUR", "PEGAWAI", "TGL RETUR", "MEMBER", "QTY RETUR", "TOTAL (Rp)", "STATUS"};
        tblRetur_Penjualan = new DefaultTableModel(null,kolom1){
            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.Integer.class,
                java.lang.Double.class,
                java.lang.Integer.class
                
            };
            public Class getColumnClass(int columnIndex){
                return types [columnIndex];
            }
            public boolean isCellEditable(int row, int col){
                int cola = tblRetur_Penjualan.getColumnCount();
                return (col<cola) ? false : true;
            }
        };
        tbDataRetur.setModel(tblRetur_Penjualan);
        tbDataRetur.getColumnModel().getColumn(0).setPreferredWidth(120);
        tbDataRetur.getColumnModel().getColumn(1).setPreferredWidth(120);
        tbDataRetur.getColumnModel().getColumn(2).setPreferredWidth(200);
        tbDataRetur.getColumnModel().getColumn(3).setPreferredWidth(70);
        tbDataRetur.getColumnModel().getColumn(4).setPreferredWidth(200);
        tbDataRetur.getColumnModel().getColumn(5).setPreferredWidth(100);
        tbDataRetur.getColumnModel().getColumn(6).setPreferredWidth(100);
        tbDataRetur.getColumnModel().getColumn(7).setPreferredWidth(70);
        
    }
    private void setTabel_DetRetur_Penjualan(){
        int baris = 0;
        String[]kolom1 = {"NO. RETUR", "KODE BARANG", "NAMA BARANG", "HARGA (Rp)",  "JUMLAH", "SUBTOTAL (Rp)", "STATUS"};
        tblDetail_Retur_Penjualan = new DefaultTableModel(kolom1,baris){
            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.Double.class,
                java.lang.Integer.class,
                java.lang.Double.class,
                java.lang.Integer.class
                
            };
            public Class getColumnClass(int columnIndex){
                return types [columnIndex];
            }
            public boolean isCellEditable(int row, int col){
                int cola = tblDetail_Retur_Penjualan.getColumnCount();
                return (col<cola) ? false : true;
            }
        };
        tbDetRetur_Penjualan.setModel(tblDetail_Retur_Penjualan);
        tbDetRetur_Penjualan.getColumnModel().getColumn(0).setPreferredWidth(150);
        tbDetRetur_Penjualan.getColumnModel().getColumn(1).setPreferredWidth(130);
        tbDetRetur_Penjualan.getColumnModel().getColumn(2).setPreferredWidth(200);
        tbDetRetur_Penjualan.getColumnModel().getColumn(3).setPreferredWidth(100);
        tbDetRetur_Penjualan.getColumnModel().getColumn(4).setPreferredWidth(70);
        tbDetRetur_Penjualan.getColumnModel().getColumn(5).setPreferredWidth(120);
        tbDetRetur_Penjualan.getColumnModel().getColumn(6).setPreferredWidth(70);
        
    }
    private void setTabel_TransRetur_Penjualan(){
        int baris = 0;
        String[]kolom1 = {"NO. RETUR", "KODE BARANG", "NAMA BARANG", "HARGA (Rp)", "JUMLAH", "SUBTOTAL (Rp)", "ID"};
        tblTransRetur_Penjualan = new DefaultTableModel(kolom1,baris){
            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.Double.class,
                java.lang.Integer.class,
                java.lang.Double.class,
                java.lang.Integer.class
                
            };
            public Class getColumnClass(int columnIndex){
                return types [columnIndex];
            }
            public boolean isCellEditable(int row, int col){
                int cola = tblTransRetur_Penjualan.getColumnCount();
                return (col<cola) ? false : true;
            }
        };
        tbTransRetur.setModel(tblTransRetur_Penjualan);
        tbTransRetur.getColumnModel().getColumn(0).setPreferredWidth(150);
        tbTransRetur.getColumnModel().getColumn(1).setPreferredWidth(200);
        tbTransRetur.getColumnModel().getColumn(2).setPreferredWidth(200);
        tbTransRetur.getColumnModel().getColumn(3).setPreferredWidth(200);
        tbTransRetur.getColumnModel().getColumn(4).setPreferredWidth(150);
        tbTransRetur.getColumnModel().getColumn(5).setPreferredWidth(200);
        tbTransRetur.getColumnModel().getColumn(6).setPreferredWidth(70);
        
    }
    private void setTabel_Penjualan(){
        String[]kolom1 = {"NO. FAKTUR", "MEMBER", "TANGGAL", "TOTAL (Rp)", "DISKON (Rp)", "TOTAL AKHIR (Rp)", "JUMLAH BAYAR (Rp)", "KEMBALIAN (Rp)"};
        tblPenjualan = new DefaultTableModel(null,kolom1){
            Class[] types = new Class[]{
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
        tbDataPenjualan.getColumnModel().getColumn(2).setPreferredWidth(75);
        tbDataPenjualan.getColumnModel().getColumn(3).setPreferredWidth(100);
        tbDataPenjualan.getColumnModel().getColumn(4).setPreferredWidth(100);
        tbDataPenjualan.getColumnModel().getColumn(5).setPreferredWidth(120);
        tbDataPenjualan.getColumnModel().getColumn(6).setPreferredWidth(120);
        tbDataPenjualan.getColumnModel().getColumn(7).setPreferredWidth(100);
    }
    private void setTabelBarang(){
        String[]kolom1 = {"NAMA BARANG", "KATEGORI", "KODE BARANG", "HARGA (Rp)", "QTY", "SUBTOTAL (Rp)"};
        tblBarang = new DefaultTableModel(null,kolom1){
            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.Double.class,
                java.lang.Integer.class,
                java.lang.Double.class
                
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
        tbDataBarang.getColumnModel().getColumn(0).setPreferredWidth(150);
        tbDataBarang.getColumnModel().getColumn(1).setPreferredWidth(100);
        tbDataBarang.getColumnModel().getColumn(2).setPreferredWidth(100);
        tbDataBarang.getColumnModel().getColumn(3).setPreferredWidth(100);
        tbDataBarang.getColumnModel().getColumn(4).setPreferredWidth(35);
        tbDataBarang.getColumnModel().getColumn(5).setPreferredWidth(120);
    }
    private void clearTabel_Retur(){
        int row_BM  = tblRetur_Penjualan.getRowCount();
        for(int i = 0; i <row_BM; i++){
            tblRetur_Penjualan.removeRow(0);
        }
    }
    private void tanggal_Pencarian(){
        try{
        if(dtTglCariAwal.getDate() != null && dtTglCariAkhir.getDate() != null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            vtglawal=format.format(dtTglCariAwal.getDate());
            vtglakhir=format.format(dtTglCariAkhir.getDate());
            Date Tanggal1 = format.parse(vtglawal);
            Date Tanggal2 = format.parse(vtglakhir);
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
    private void cariData_Retur_Penjualan(){
        vtgl_trans = tglinput.format(dtTglCariAwal.getDate());
        String vtgl_trans1 = tglinput.format(dtTglCariAkhir.getDate());
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            clearTabel_Retur();
            if(txtCari.getText().length() > 0 ){
                sqlselect = "select * from tb_retur_penjualan a, tb_penjualan b, tb_user c, tb_member d "
                    + " where a.no_faktur_penjualan=b.no_faktur_penjualan "
                    + " and a.id_user=c.id_user and b.id_member=d.id_member and a.no_faktur_penjualan like '%"+txtCari.getText()+"%' "
                    + " order by tgl_retur desc, a.no_retur_penjualan desc "; 
            }else{
                sqlselect = "select * from tb_retur_penjualan a, tb_penjualan b, tb_user c, tb_member d "
                    + " where a.no_faktur_penjualan=b.no_faktur_penjualan "
                    + " and a.id_user=c.id_user and b.id_member=d.id_member and "
                    + " tgl_retur between '"+vtgl_trans+"' and '"+vtgl_trans1+"' "
                    + " order by tgl_retur desc, a.no_retur_penjualan desc "; 
            }
            
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);// mengeksekusi query select
            while(res.next()){
                vno_retur = res.getString("no_retur_penjualan");
                vno_faktur = res.getString("no_faktur_penjualan");
                vid_user = res.getString("id_user")+"-"+res.getString("nama_lengkap");
                vtgl_trans = tglview.format(res.getDate("tgl_retur"));
                vnm_member = res.getString("nama_member");
                if(vnm_member.equals("")){
                    vnm_member= "NON MEMBER";
                }else{
                    vnm_member = res.getString("nama_member");
                }
                vjumlah_barang = res.getInt("jumlah_retur");
                vtotal = res.getDouble("total");
                vstatus_brg = res.getString("status_retur");
                Object[]data = {vno_retur, vno_faktur, vid_user, vtgl_trans, vnm_member, vjumlah_barang, vtotal, vstatus_brg};
                tblRetur_Penjualan.addRow(data);
            }
            lblRecordRetur.setText("RECORD : "+tblRetur_Penjualan.getRowCount());
            panelDetail.setVisible(false); panelData.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error method showData_ReturBM() : "+ex);
        }
    }    
    private void showData_Retur_Penjualan(){
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            clearTabel_Retur();
            sqlselect = "select * from tb_retur_penjualan a, tb_penjualan b, tb_user c, tb_member d "
                    + " where a.no_faktur_penjualan=b.no_faktur_penjualan "
                    + " and a.id_user=c.id_user and b.id_member=d.id_member order by tgl_retur desc, a.no_retur_penjualan desc "; 
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);// mengeksekusi query select
            while(res.next()){
                vno_retur = res.getString("no_retur_penjualan");
                vno_faktur = res.getString("no_faktur_penjualan");
                vid_user = res.getString("id_user")+"-"+res.getString("nama_lengkap");
                vtgl_trans = tglview.format(res.getDate("tgl_retur"));
                vnm_member = res.getString("nama_member");
                if(vnm_member.equals("")){
                    vnm_member= "NON MEMBER";
                }else{
                    vnm_member = res.getString("nama_member");
                }
                vjumlah_barang = res.getInt("jumlah_retur");
                vtotal = res.getDouble("total");
                vstatus_brg = res.getString("status_retur");
                Object[]data = {vno_retur, vno_faktur, vid_user, vtgl_trans, vnm_member, vjumlah_barang, vtotal, vstatus_brg};
                tblRetur_Penjualan.addRow(data);
            }
            lblRecordRetur.setText("RECORD : "+tblRetur_Penjualan.getRowCount());
            panelDetail.setVisible(false); panelData.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error method showData_ReturBM() : "+ex);
        }
    }    
     private void showDetail_Retur_Penjualan(){
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            sqlselect = "select * from tb_retur_penjualan a, tb_detail_retur_penjualan b, tb_barang c "
                    + " where a.no_retur_penjualan=b.no_retur_penjualan and b.id_barang=c.id_barang and "
                    + " b.no_retur_penjualan='"+vno_retur+"' "
                    + " order by nama_barang asc"; 
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);// mengeksekusi query select
            while(res.next()){
                vno_retur = res.getString("no_retur_penjualan");
                vid_barang = res.getString("id_barang");
                vnm_barang = res.getString("nama_barang");
                vhrg_beli = res.getDouble("harga_jual");
                vjumlah_barang = res.getInt("jumlah_barang");
                vsubtotal = res.getDouble("subtotal");
                vstatus_brg = res.getString("status_retur");
                Object[]data = { vno_retur, vid_barang, vnm_barang, vhrg_beli, vjumlah_barang, vsubtotal, vstatus_brg};
                tblDetail_Retur_Penjualan.addRow(data);
                panelUbah.setVisible(true);
            }
            lblRecordDetRetur.setText("RECORD : "+tblDetail_Retur_Penjualan.getRowCount()); btnTambah.requestFocus();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method showData_DetBM() : "+ex);
        }
    }
    private void clearTabel_Penjualan(){
        int row_BM  = tblPenjualan.getRowCount();
        for(int i = 0; i <row_BM; i++){
            tblPenjualan.removeRow(0);
        }
    }
    private void showDataPenjualan(){
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            clearTabel_Penjualan();
            sqlselect = "select * from tb_penjualan a, tb_member b "
                    +" where a.id_member=b.id_member and no_faktur_penjualan like '%"+txtCariNoFak.getText()+"%' "
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
                vtgl_trans = tglview.format(res.getDate("tgl_transaksi"));
                vtotal = res.getDouble("total");
                vdiskon = res.getDouble("diskon");
                vtotal_akhir = vtotal - vdiskon;
                vjumlah_bayar = res.getDouble("jumlah_bayar");
                vkembalian = res.getDouble("kembalian");
                Object[]data = {vno_faktur, vid_member, vtgl_trans, vtotal, vdiskon, vtotal_akhir, vjumlah_bayar, vkembalian};
                tblPenjualan.addRow(data);
            }
            lblRecordPenjualan.setText("RECORD : "+tblPenjualan.getRowCount()); btnSimpan.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method showDataPenjualan() : "+ex);
        }
    }
    private void showDataBarang(){
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            sqlselect = "select * from tb_detail_penjualan a, tb_barang b, tb_kategori c, tb_penjualan d"
                    + " where a.id_barang=b.id_barang and b.id_kategori=c.id_kategori "
                    + " and a.no_faktur_penjualan=d.no_faktur_penjualan "
                    + " and d.no_faktur_penjualan like '"+txtNoFaktur.getText()+"' "
                    + " order by nama_barang asc";
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);// mengeksekusi query select
            while(res.next()){
                vtgl_trans = tglview.format(res.getDate("tgl_transaksi"));
                vid_barang = res.getString("id_barang");
                vnm_kategori = res.getString("nama_kategori");
                vnm_barang = res.getString("nama_barang");
                vhrg_beli = res.getDouble("harga_jual");
                vjumlah_barang = res.getInt("jumlah_barang");
                vsubtotal = res.getDouble("subtotal");
                Object[]data = {vnm_barang, vnm_kategori, vid_barang, vhrg_beli, vjumlah_barang, vsubtotal};
                tblBarang.addRow(data);
            }
            lblFaktur2.setText("DAFTAR PENJUALAN BARANG NO. FAKTUR : "+txtNoFaktur.getText());
            lblTgl2.setText(""+vtgl_trans);
            lblRecordBarang.setText("RECORD : "+tbDataBarang.getRowCount());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method showDataBarang() : "+ex);
        }
    }
    public void tampilMember(){
        try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select * from tb_penjualan a, tb_member b, tb_detail_penjualan c "
                    + " where a.id_member=b.id_member and a.no_faktur_penjualan=c.no_faktur_penjualan and "
                    + " a.no_faktur_penjualan='"+vno_faktur+"' ";  
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            while(res.next()){
                txtNoFaktur.setText(res.getString("no_faktur_penjualan"));
                txtIdMember.setText(res.getString("id_member"));
                txtNmMember.setText(res.getString("nama_member"));
                Daftar_Faktur.dispose();
                txtDaftarBrg.requestFocus();
            }
            res.close(); stat.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }              
    }   
    public void tampilBarang(){
        try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select * from tb_barang a, tb_detail_penjualan b where a.id_barang=b.id_barang "
                    + " and b.id_barang='"+vid_barang+"' and no_faktur_penjualan='"+vno_faktur+"' ";  
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            while(res.next()){
                txtDaftarBrg.setText(res.getString("id_barang"));
                txtNamaBrg.setText(res.getString("nama_barang"));
                txtHargaJual.setText(res.getString("harga_jual"));
                txtQty.setText(res.getString("jumlah_barang"));
            }
            Daftar_Barang.dispose();
            txtDaftarBrg.requestFocus();
            res.close(); stat.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }              
    }
    private void aksiSimpan_Retur_Penjualan(){
        vjumlah_barang = 0;
        vno_retur = txtNoRetur.getText();
        vno_faktur = txtNoFaktur.getText();
        vtgl_trans = tglinput.format(dtTglRetur.getDate());  
        vid_member = txtIdMember.getText();
        if(rb1.isSelected()){
            vstatus_brg = "1";
        }else if(rb2.isSelected()){
            vstatus_brg = "2";
        }
        else if(rb3.isSelected()){
            vstatus_brg = "3";
        }else{
            vstatus_brg = "4";
        }
        for (int i = 0; i < tbTransRetur.getRowCount(); i++) {
            vjumlah_barang = vjumlah_barang + Integer.parseInt(tbTransRetur.getValueAt(i, 4).toString());
        } 
        vtotal = Double.valueOf(txtTotal.getText());
        
        if(btnSimpan.getText().equals("SIMPAN")){
            sqlinsert = "insert into tb_retur_penjualan(no_retur_penjualan, no_faktur_penjualan, id_user, tgl_retur, "
                    + " jumlah_retur, total, status_retur) "
                    + " values('"+vno_retur+"', '"+vno_faktur+"','"+id+"', '"+vtgl_trans+"', "
                    + " '"+vjumlah_barang+"', '"+vtotal+"', '"+vstatus_brg+"')";
        }
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            Statement state = _Cnn.createStatement();
            state.executeUpdate(sqlinsert);
            JOptionPane.showMessageDialog(null, "DATA BERHASIL DISIMPAN ! ",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            aksiSimpanTrans_Retur_Penjualan();
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method aksiSimpan_ReturBM() : "
                    +ex, "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void aksiSimpanTrans_Retur_Penjualan(){
        int jumlah_baris = tbTransRetur.getRowCount();
            try{
                _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
                _Cnn = getCnn.getConnection();// membuka koneksi
                Statement state = _Cnn.createStatement();
                int i=0;
            while(i < jumlah_baris){
                state.executeUpdate("insert into tb_detail_retur_penjualan (no_retur_penjualan, id_barang, jumlah_barang, subtotal) values("
                        + "'"+tbTransRetur.getValueAt(i, 0)+"',"
                        + "'"+tbTransRetur.getValueAt(i, 1)+"',"
                        + "'"+tbTransRetur.getValueAt(i, 4)+"',"
                        + "'"+tbTransRetur .getValueAt(i, 5)+"')");
                i++;
            } clearForm(); enableForm(); autoNoRetur();
            }catch(Exception e){
            }
    }
    private void hitungTotal(){
        int vtotal_harga = 0;
        int vbaris = tbTransRetur.getRowCount();
        tblTransRetur_Penjualan = (DefaultTableModel) tbTransRetur.getModel();
        for(int i=0;i<vbaris;i++){
            vhrg_jual = Double.parseDouble(tblTransRetur_Penjualan.getValueAt(i, 3).toString());
            vjumlah_barang = Integer.parseInt(tblTransRetur_Penjualan.getValueAt(i, 4).toString());
            vtotal_harga = (int) (vtotal_harga+(vhrg_jual*vjumlah_barang));
        }
        txtDaftarBrg.setText("");
        txtNamaBrg.setText("");
        txtHargaJual.setText("");
        txtQty.setText("");
        txtJumlah.setValue(0);
        txtTotal.setText(String.valueOf(vtotal_harga));
        txtDaftarBrg.requestFocus();
    }
    private void tambahBarang(){
        vno_retur = txtNoRetur.getText();
        vid_barang = txtDaftarBrg.getText();
        vnm_barang = txtNamaBrg.getText();
        vhrg_jual = Double.parseDouble(txtHargaJual.getText());
        vjumlah_barang = Integer.parseInt(txtJumlah.getValue().toString());
        vqty = Integer.parseInt(txtQty.getText());
        vsubtotal = vhrg_jual *vjumlah_barang;
        if(txtDaftarBrg.getText().equals("")){
            JOptionPane.showMessageDialog(null, "KODE BARANG HARUS DIISI !",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            txtDaftarBrg.requestFocus();
        }else if((int)txtJumlah.getValue()==0){
            JOptionPane.showMessageDialog(null, "JUMLAH BARANG HARUS LEBIH DARI 0 !",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            txtJumlah.requestFocus();
        }else if(vjumlah_barang > vqty){
            JOptionPane.showMessageDialog(null, "QTY TIDAK MENCUKUPI !",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            txtJumlah.requestFocus();
        }else {
            try{
                _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
                _Cnn = getCnn.getConnection();// membuka koneksi
                Statement state = _Cnn.createStatement();
                if(btnSimpan.getText().equals("SIMPAN")){
                    
                }else {
                    state.executeUpdate("insert into tb_detail_retur_penjualan (no_retur_penjualan, id_barang, jumlah_barang, subtotal) values ("
                        + "'"+vno_retur+"',"
                        + "'"+vid_barang+"',"
                        + "'"+vjumlah_barang+"',"
                        + "'"+vsubtotal+"')");
                }   
            }catch(Exception e){
                JOptionPane.showMessageDialog(rootPane, "Gagal Menyimpan! Error : "+e);
            }
        tblTransRetur_Penjualan.addRow(new Object[]{
            vno_retur,vid_barang,vnm_barang,vhrg_jual,vjumlah_barang,vsubtotal
        });
        tbTransRetur.setModel(tblTransRetur_Penjualan);
        lblBaris.setText("RECORD : "+tblTransRetur_Penjualan .getRowCount()); hitungTotal(); txtDaftarBrg.setEditable(true);
        }
    }
    private void hapusBarang(){
        int x = tbTransRetur.getSelectedRow();
        int jumlah_baris = tbTransRetur.getRowCount();
        if (btnSimpan.getText().equals("SIMPAN") || btnUbah.getText().equals("SIMPAN PERUBAHAN")){
            int jawab = JOptionPane.showConfirmDialog(null, 
                "ANDA YAKIN AKAN MEMBATALKAN TRANSAKSI BARANG INI ? KODE BARANG = '"+txtDaftarBrg.getText()+"'  !",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if(jawab==JOptionPane.YES_OPTION){
                try{
                    _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
                    _Cnn = getCnn.getConnection();// membuka koneksi
                    Statement state = _Cnn.createStatement();
                    int i=0;
                    if(i < jumlah_baris){
                        state.executeUpdate(" delete from tb_detail_retur_penjualan where id_barang='"+txtDaftarBrg.getText()+"' and "
                                + " no_retur_penjualan='"+txtNoRetur.getText()+"' and "
                                + " no_trans_retur_penjualan = '"+tbTransRetur.getValueAt(tbTransRetur.getSelectedRow(), 6)+"' ");
                        tblTransRetur_Penjualan.removeRow(x);
                        btnHapusBrg.setEnabled(false);
                        hitungTotal();
                        txtDaftarBrg.requestFocus(); txtDaftarBrg.setEditable(true);
                    } lblBaris.setText("RECORD : "+tblTransRetur_Penjualan.getRowCount());
                }catch(Exception e){
                JOptionPane.showMessageDialog(rootPane, "Gagal Menyimpan! Error : "+e);
                }
            }
        }
    }
    private void autoNoRetur(){
        Date sk = new Date();
        SimpleDateFormat format1=new SimpleDateFormat("ddMMyy");
        String time = format1.format(sk);
        try{
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select right(no_retur_penjualan,4) as no_retur_penjualan "
                    + " from tb_retur_penjualan order by no_retur_penjualan desc";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            if (res.next()) {
                String kode = res.getString("no_retur_penjualan");
                String AN = "" + (Integer.parseInt(kode) + 1);
                String Nol = "";

                if(AN.length()==1)
                {Nol = "000";}
                else if(AN.length()==2)
                {Nol = "00";}
                else if(AN.length()==3)
                {Nol = "0";}
                else if(AN.length()==4)
                {Nol = "";}
                txtNoRetur.setText("RE-" +time+ "-" + Nol + AN);
            }else{
                int kode = 1;
                txtNoRetur.setText("RE-" +time+ "-" +"000"+Integer.toString(kode));//sesuaikan dengan variable namenya
            }
            txtNoFaktur.requestFocus();
            }catch (SQLException ex){
                JOptionPane.showMessageDialog(this,"Error Method autoNoRetur : " + ex);
            }
    }
    public void getDataRetur(){
        try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select * from tb_retur_penjualan a, tb_penjualan b, tb_member d "
                    + " where a.no_faktur_penjualan=b.no_faktur_penjualan and "
                    + " b.id_member=d.id_member and "
                    + " a.no_retur_penjualan='"+vno_retur+"' "; 
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            while(res.next()){
                txtNoReturNew.setText(res.getString("no_retur_penjualan"));
                txtNoRetur.setText(res.getString("no_retur_penjualan"));
                vnm_member = res.getString("nama_member");
                if(vnm_member.equals("")){
                    vnm_member= "NON MEMBER";
                }else{
                    vnm_member = res.getString("nama_member");
                }
                txtMemberNew.setText(vnm_member);
                txtJumlahNew.setText(res.getString("jumlah_retur"));
                txtStatusBrg.setText(res.getString("status_retur"));
                txtTotalNew.setText(res.getString("total"));
                if(txtStatusBrg.getText().equals("1")){
                    lblKeterangan.setText("BARANG MASIH DI TOKO DALAM KEADAAN RUSAK  - DARI CUSTOMER !");
                }else if(txtStatusBrg.getText().equals("2")){
                    lblKeterangan.setText("BARANG INI SEDANG DI PROSES DI DISTRIBUTOR !");
                }else if(txtStatusBrg.getText().equals("3")){
                    lblKeterangan.setText("BARANG ADA DITOKO DALAM KEADAAN BAIK - DARI DISTRIBUTOR !");
                }else {
                    lblKeterangan.setText("CUSTOMER SUDAH MENDAPAT PERGANTAN BARANG !");
                }
            }
            res.close(); stat.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }              
    }
    private void aksiUbahStatus(){
        try {
            if(rb1new.isSelected()){
                vstatus_brg = "1";
            }else if(rb2new.isSelected()){
                vstatus_brg = "2";
            }
            else if(rb3new.isSelected()){
                vstatus_brg = "3";
            }else{
                vstatus_brg = "4";
            }
            sqlinsert = "update tb_retur_penjualan set status_retur='"+vstatus_brg+"' where "
                        + " no_retur_penjualan='"+txtNoReturNew.getText()+"'";
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            Statement state = _Cnn.createStatement();
            state.executeUpdate(sqlinsert);
            JOptionPane.showMessageDialog(null, "STATUS BERHASIL DI UBAH ! ",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            showData_Retur_Penjualan(); clearForm(); disableForm();
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method aksiUbahStatus() : "
                    +ex, "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
        
    }
    private void aksiUbahRetur(){
        vjumlah_barang = 0;
        vno_retur = txtNoRetur.getText();
        vno_faktur = txtNoFaktur.getText();
        vtgl_trans = tglinput.format(dtTglRetur.getDate());
        if(rb1.isSelected()){
                vstatus_brg = "1";
            }else if(rb2.isSelected()){
                vstatus_brg = "2";
            }
            else if(rb3.isSelected()){
                vstatus_brg = "3";
            }else{
                vstatus_brg = "4";
            }
        for (int i = 0; i < tbTransRetur.getRowCount(); i++) {
            vjumlah_barang = vjumlah_barang + Integer.parseInt(tbTransRetur.getValueAt(i, 4).toString());
        } 
        vtotal = Double.valueOf(txtTotal.getText());
        try{
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            Statement state = _Cnn.createStatement();
            if(btnUbah.getText().equals("SIMPAN PERUBAHAN")){
                    sqlinsert = "update tb_retur_penjualan set no_faktur_penjualan='"+vno_faktur+"', id_user='"+id+"', "
                            + " tgl_retur='"+vtgl_trans+"', jumlah_retur='"+vjumlah_barang+"', "
                            + " total='"+vtotal+"', status_retur='"+vstatus_brg+"' where no_retur_penjualan='"+vno_retur+"' ";
                    state.executeUpdate(sqlinsert);
                JOptionPane.showMessageDialog(null, "DATA BERHASIL DIUBAH ! ",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "PERIKSA KEMBALI ! "+ex);
        }
    }
    private void getData(){
        vno_retur = txtNoReturNew.getText();
        try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select * from tb_retur_penjualan a, tb_detail_retur_penjualan b, tb_member c, tb_barang d, tb_penjualan e "
                    + " where e.id_member=c.id_member and a.no_retur_penjualan=b.no_retur_penjualan and "
                    + " b.id_barang=d.id_barang and a.no_faktur_penjualan=e.no_faktur_penjualan and "
                    + " b.no_retur_penjualan='"+vno_retur+"' ";
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);
            while(res.next()){
                lblNoFak.setText(res.getString("no_retur_penjualan")+"--"+res.getString("nama_member"));
                txtNoFaktur.setText(res.getString("no_faktur_penjualan"));
                txtIdMember.setText(res.getString("id_member"));
                vstatus_brg = res.getString("status_retur");
                if(vstatus_brg.equals("1")){
                    rb1.setSelected(true);
                }else if(vstatus_brg.equals("2")){
                    rb2.setSelected(true);
                }if(vstatus_brg.equals("3")){
                    rb3.setSelected(true);
                }else {
                    rb4.setSelected(true);
                }
                txtNmMember.setText(res.getString("nama_member"));
                txtTotal.setText(res.getString("total"));
                dtTglRetur.setDate(new Date());
                
                vno_transaksi = res.getString("no_trans_retur_penjualan");
                vno_retur = res.getString("no_retur_penjualan");
                vid_barang = res.getString("id_barang");
                vnm_barang = res.getString("nama_barang");
                vhrg_jual = res.getDouble("harga_jual");
                vjumlah_barang = res.getInt("jumlah_barang");
                vsubtotal = res.getDouble("subtotal");
                Object[]data = {vno_retur, vid_barang, vnm_barang, vhrg_jual, vjumlah_barang, vsubtotal, vno_transaksi};
                tblTransRetur_Penjualan.addRow(data);
                lblBaris.setText("RECORD : "+tblTransRetur_Penjualan .getRowCount());
                btnSimpan.setVisible(false); btnSimpan.setText(""); btnUbah.setVisible(true);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "PERIKSA KEMBALI ! "+ ex);
        }              
    }
    private void hitungSubtotal(){
        int vsub = 0;
        vhrg_jual = Double.parseDouble(txtHargaJual.getText());
        vjumlah_barang = Integer.parseInt(txtJumlah.getValue().toString());
        vsub = (int) (vsub+vhrg_jual*vjumlah_barang);
        txtSubtotal.setText(Integer.toString(vsub));
        txtJumlah.requestFocus();
    }
    public void formTengah(){
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if(frameSize.height < screensize.height){
           frameSize.height =  screensize.height;
        }
        if(frameSize.width < screensize.width){
           frameSize.width =  screensize.width;
        }
        this.setLocation((screensize.width - frameSize.width)/2, (screensize.height - frameSize.height)/2);
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
                txtDir.setText(""); BackUp_Restore.dispose(); setTabel_Retur_Penjualan();showData_Retur_Penjualan();
            }
        }catch(IOException e){ System.out.println(e); }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        IfrReturPenjualan = new javax.swing.JFrame();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbTransRetur = new javax.swing.JTable();
        lblBaris = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        dtTglRetur = new com.toedter.calendar.JDateChooser();
        txtNoRetur = new Tool.Custom_TextField();
        jLabel40 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        txtNoFaktur = new Tool.Custom_TextField();
        btnDaftarFaktur = new javax.swing.JButton();
        txtIdMember = new Tool.Custom_TextField();
        txtNmMember = new Tool.Custom_TextField();
        jPanel11 = new javax.swing.JPanel();
        rb1 = new javax.swing.JRadioButton();
        rb2 = new javax.swing.JRadioButton();
        rb3 = new javax.swing.JRadioButton();
        rb4 = new javax.swing.JRadioButton();
        jLabel14 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtTotal = new Tool.NumberTextField();
        btnSimpan = new javax.swing.JButton();
        btnReturPenjualan = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtDaftarBrg = new Tool.Custom_TextField();
        btnDaftarBrg = new javax.swing.JButton();
        txtNamaBrg = new Tool.Custom_TextField();
        txtHargaJual = new Tool.NumberTextField();
        txtQty = new Tool.NumberTextField();
        txtJumlah = new javax.swing.JSpinner();
        jLabel18 = new javax.swing.JLabel();
        txtSubtotal = new Tool.NumberTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        btnTambahBrg = new javax.swing.JButton();
        btnHapusBrg = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblJam = new javax.swing.JLabel();
        Daftar_Barang = new javax.swing.JDialog();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbDataBarang = new javax.swing.JTable();
        lblRecordBarang = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        lblFaktur2 = new javax.swing.JLabel();
        lblTgl2 = new javax.swing.JLabel();
        Daftar_Faktur = new javax.swing.JDialog();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbDataPenjualan = new javax.swing.JTable();
        lblRecordPenjualan = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        txtCariNoFak = new Tool.Custom_TextField();
        jLabel35 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jFileChooser1 = new javax.swing.JFileChooser();
        BackUp_Restore = new javax.swing.JFrame();
        jPanel14 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel18 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtDir = new javax.swing.JTextField();
        btnPilihLokasi = new javax.swing.JButton();
        btnSimpanBackup = new javax.swing.JButton();
        panelData = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbDataRetur = new javax.swing.JTable();
        lblRecordRetur = new javax.swing.JLabel();
        txtCari = new Tool.Custom_TextField();
        btnTambah = new javax.swing.JButton();
        lblNoFak = new javax.swing.JLabel();
        btnBackUp = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        dtTglCariAwal = new com.toedter.calendar.JDateChooser();
        jLabel17 = new javax.swing.JLabel();
        dtTglCariAkhir = new com.toedter.calendar.JDateChooser();
        btnCariTgl = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        panelDetail = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbDetRetur_Penjualan = new javax.swing.JTable();
        lblRecordDetRetur = new javax.swing.JLabel();
        panelUbah = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtJumlahNew = new Tool.NumberTextField();
        txtTotalNew = new Tool.NumberTextField();
        jPanel9 = new javax.swing.JPanel();
        lblKeterangan = new javax.swing.JLabel();
        txtNoReturNew = new Tool.Custom_TextField();
        jLabel23 = new javax.swing.JLabel();
        txtMemberNew = new Tool.Custom_TextField();
        jLabel25 = new javax.swing.JLabel();
        txtStatusBrg = new Tool.NumberTextField();
        jPanel12 = new javax.swing.JPanel();
        rb1new = new javax.swing.JRadioButton();
        rb2new = new javax.swing.JRadioButton();
        rb3new = new javax.swing.JRadioButton();
        rb4new = new javax.swing.JRadioButton();
        btnSimpanNew = new javax.swing.JButton();
        lblUser = new javax.swing.JLabel();
        lblUsersistem = new javax.swing.JLabel();
        btnEdit = new javax.swing.JButton();
        btnKembali = new javax.swing.JButton();

        IfrReturPenjualan.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        IfrReturPenjualan.setTitle("RETUR PENJUALAN - LUBADA JAYA MARKET");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 12))); // NOI18N

        jScrollPane2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 3, true));

        tbTransRetur.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        tbTransRetur.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "NO. RETUR", "KODE BARANG", "NAMA BARANG", "HARGA BELI", "JUMLAH", "SUBTOTAL"
            }
        ));
        tbTransRetur.setRowHeight(35);
        tbTransRetur.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbTransReturMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbTransRetur);

        lblBaris.setFont(new java.awt.Font("Arial Narrow", 1, 12)); // NOI18N
        lblBaris.setText("RECORD : 0");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true), "(+) FORM"));

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true), "KETERANGAN"));

        jLabel10.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jLabel10.setForeground(java.awt.Color.red);
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("1 : *BARANG MASIH DI TOKO DALAM KEADAAN RUSAK  - DARI CUSTOMER !");
        jLabel10.setOpaque(true);

        jLabel11.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jLabel11.setForeground(java.awt.Color.red);
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText("4 : *CUSTOMER SUDAH MENDAPAT PERGANTAN BARANG !");
        jLabel11.setOpaque(true);

        jLabel13.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jLabel13.setForeground(java.awt.Color.red);
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setText("2 : *BARANG INI SEDANG DI PROSES DI DISTRIBUTOR !");
        jLabel13.setOpaque(true);

        jLabel16.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jLabel16.setForeground(java.awt.Color.red);
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel16.setText("3 : *BARANG ADA DITOKO DALAM KEADAAN BAIK - DARI DISTRIBUTOR !");
        jLabel16.setOpaque(true);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addContainerGap(124, Short.MAX_VALUE))
        );

        jPanel8.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        dtTglRetur.setEnabled(false);

        txtNoRetur.setEditable(false);
        txtNoRetur.setBackground(new java.awt.Color(0, 0, 0));
        txtNoRetur.setForeground(java.awt.Color.orange);
        txtNoRetur.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNoRetur.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        txtNoRetur.setPlaceholder("");

        jLabel40.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel40.setText("NO. RETUR    : ");

        jLabel42.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel42.setText("TANGGAL      : ");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dtTglRetur, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                    .addComponent(txtNoRetur, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoRetur, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dtTglRetur, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true), "KLIK BUTTON --> MENCARI NO. FAKTUR "));

        txtNoFaktur.setBackground(new java.awt.Color(240, 240, 240));
        txtNoFaktur.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNoFaktur.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        txtNoFaktur.setPlaceholder("NO. FAKTUR");
        txtNoFaktur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoFakturActionPerformed(evt);
            }
        });

        btnDaftarFaktur.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/search.png"))); // NOI18N
        btnDaftarFaktur.setMnemonic('f');
        btnDaftarFaktur.setText("CARI");
        btnDaftarFaktur.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDaftarFaktur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDaftarFakturActionPerformed(evt);
            }
        });

        txtIdMember.setEditable(false);
        txtIdMember.setBackground(new java.awt.Color(240, 240, 240));
        txtIdMember.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtIdMember.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        txtIdMember.setPlaceholder("KODE");

        txtNmMember.setEditable(false);
        txtNmMember.setBackground(new java.awt.Color(240, 240, 240));
        txtNmMember.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNmMember.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        txtNmMember.setPlaceholder("NAMA MEMBER");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(txtNoFaktur, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(btnDaftarFaktur))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(txtIdMember, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(txtNmMember, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNoFaktur, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDaftarFaktur, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtIdMember, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNmMember, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true), "STATUS RETUR : "));

        buttonGroup1.add(rb1);
        rb1.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        rb1.setText("1");

        buttonGroup1.add(rb2);
        rb2.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        rb2.setText("2");

        buttonGroup1.add(rb3);
        rb3.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        rb3.setText("3");

        buttonGroup1.add(rb4);
        rb4.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        rb4.setText("4");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addComponent(rb1)
                .addGap(18, 18, 18)
                .addComponent(rb2)
                .addGap(18, 18, 18)
                .addComponent(rb3)
                .addGap(18, 18, 18)
                .addComponent(rb4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rb3)
                        .addComponent(rb4))
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rb1)
                        .addComponent(rb2)))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jLabel14.setFont(new java.awt.Font("Agency FB", 3, 16)); // NOI18N
        jLabel14.setForeground(java.awt.Color.red);
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("* TELITI SEBELUM MENYIMPAN !");
        jLabel14.setOpaque(true);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel8.setFont(new java.awt.Font("Arial Narrow", 1, 30)); // NOI18N
        jLabel8.setText("TOTAL   : Rp. ");

        txtTotal.setEditable(false);
        txtTotal.setBackground(new java.awt.Color(0, 0, 0));
        txtTotal.setForeground(new java.awt.Color(255, 0, 0));
        txtTotal.setFont(new java.awt.Font("Arial Narrow", 1, 30)); // NOI18N

        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/save_add.png"))); // NOI18N
        btnSimpan.setMnemonic('s');
        btnSimpan.setText("SIMPAN");
        btnSimpan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSimpan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSimpan.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        btnReturPenjualan.setBackground(java.awt.Color.green);
        btnReturPenjualan.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReturPenjualan.setText("LIHAT DATA RETUR PENJUALAN");
        btnReturPenjualan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReturPenjualan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReturPenjualan.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReturPenjualan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReturPenjualanActionPerformed(evt);
            }
        });

        btnUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/update24.png"))); // NOI18N
        btnUbah.setText("SIMPAN PERUBAHAN");
        btnUbah.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUbah.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUbah.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });

        btnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/u.png"))); // NOI18N
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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSimpan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUbah)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBatal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnReturPenjualan)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReturPenjualan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUbah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBatal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true), "(+) DETAIL"));

        jLabel6.setText("KODE BARANG");

        jLabel9.setText("NAMA BARANG");

        jLabel5.setForeground(java.awt.Color.red);
        jLabel5.setText("* HARGA BARANG");

        jLabel12.setText("STOK");

        txtDaftarBrg.setBackground(new java.awt.Color(0, 0, 0));
        txtDaftarBrg.setForeground(java.awt.Color.orange);
        txtDaftarBrg.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDaftarBrg.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        txtDaftarBrg.setPlaceholder("KODE BARANG");
        txtDaftarBrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDaftarBrgActionPerformed(evt);
            }
        });
        txtDaftarBrg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDaftarBrgKeyTyped(evt);
            }
        });

        btnDaftarBrg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/search.png"))); // NOI18N
        btnDaftarBrg.setMnemonic('b');
        btnDaftarBrg.setText("CARI");
        btnDaftarBrg.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDaftarBrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDaftarBrgActionPerformed(evt);
            }
        });

        txtNamaBrg.setEditable(false);
        txtNamaBrg.setBackground(new java.awt.Color(0, 204, 204));
        txtNamaBrg.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNamaBrg.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        txtNamaBrg.setPlaceholder("NAMA BARANG");

        txtHargaJual.setEditable(false);
        txtHargaJual.setBackground(new java.awt.Color(0, 204, 204));
        txtHargaJual.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N

        txtQty.setEditable(false);
        txtQty.setBackground(new java.awt.Color(0, 204, 204));
        txtQty.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtQty.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N

        txtJumlah.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        txtJumlah.setModel(new javax.swing.SpinnerNumberModel(0, 0, 99, 1));
        txtJumlah.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                txtJumlahStateChanged(evt);
            }
        });
        txtJumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtJumlahKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtJumlahKeyTyped(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("->");

        txtSubtotal.setEditable(false);
        txtSubtotal.setBackground(new java.awt.Color(0, 204, 204));
        txtSubtotal.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N

        jLabel39.setText("SUBTOTAL");

        jLabel7.setForeground(java.awt.Color.red);
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("* JUMLAH");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtDaftarBrg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(btnDaftarBrg))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(txtHargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtJumlah)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSubtotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(txtJumlah))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6)
                                .addComponent(jLabel9)))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtDaftarBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNamaBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtHargaJual, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(40, 40, 40))
                            .addComponent(btnDaftarBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jPanel13.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        btnTambahBrg.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnTambahBrg.setText("TAMBAH");
        btnTambahBrg.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTambahBrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahBrgActionPerformed(evt);
            }
        });

        btnHapusBrg.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnHapusBrg.setText("HAPUS");
        btnHapusBrg.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHapusBrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusBrgActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnTambahBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHapusBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambahBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHapusBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblBaris)
                            .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING))
                        .addContainerGap())))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblBaris)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(0, 204, 204));
        jPanel7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Shopping_Cart.png"))); // NOI18N

        jLabel21.setFont(new java.awt.Font("Agency FB", 3, 30)); // NOI18N
        jLabel21.setForeground(java.awt.Color.red);
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel21.setText("SAMSUNG");

        jLabel22.setFont(new java.awt.Font("Agency FB", 3, 30)); // NOI18N
        jLabel22.setForeground(java.awt.Color.blue);
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel22.setText("ASUS");

        jLabel24.setFont(new java.awt.Font("Agency FB", 3, 30)); // NOI18N
        jLabel24.setForeground(java.awt.Color.blue);
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel24.setText("TOSHIBA");

        jLabel26.setFont(new java.awt.Font("Agency FB", 3, 30)); // NOI18N
        jLabel26.setForeground(java.awt.Color.red);
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel26.setText("ACER");

        jLabel27.setFont(new java.awt.Font("Agency FB", 3, 30)); // NOI18N
        jLabel27.setForeground(java.awt.Color.blue);
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel27.setText("SHARP");

        jLabel28.setFont(new java.awt.Font("Agency FB", 3, 30)); // NOI18N
        jLabel28.setForeground(java.awt.Color.red);
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel28.setText("PANASONIC");

        jLabel29.setFont(new java.awt.Font("Agency FB", 3, 30)); // NOI18N
        jLabel29.setForeground(java.awt.Color.red);
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel29.setText("POLYTRON");

        jLabel30.setFont(new java.awt.Font("Agency FB", 3, 30)); // NOI18N
        jLabel30.setForeground(java.awt.Color.blue);
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel30.setText("AXIOO");

        jLabel31.setFont(new java.awt.Font("Agency FB", 3, 30)); // NOI18N
        jLabel31.setForeground(java.awt.Color.red);
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel31.setText("SONY");

        jLabel32.setFont(new java.awt.Font("Agency FB", 3, 30)); // NOI18N
        jLabel32.setForeground(java.awt.Color.blue);
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel32.setText("DELL");

        jLabel33.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        jLabel33.setForeground(java.awt.Color.blue);
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("MARKET");

        jLabel34.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        jLabel34.setForeground(java.awt.Color.red);
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("LUBADA JAYA");

        jLabel36.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("FORM RETUR PENJUALAN");
        jLabel36.setOpaque(true);

        jLabel3.setFont(new java.awt.Font("Agency FB", 1, 16)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("FORM INI DIGUNAKAN UNTUK MEMASUKAN DATA RETUR PENJUALAN ATAU BARANG YANG DIKEMBALIKAN KE TOKO DARI PELANGGAN !");
        jLabel3.setOpaque(true);

        lblJam.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        lblJam.setForeground(java.awt.Color.red);
        lblJam.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJam.setOpaque(true);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblJam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 667, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblJam, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel22)
                                .addComponent(jLabel21)
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel29)
                                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel36)))
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel26)
                                    .addComponent(jLabel27)
                                    .addComponent(jLabel28)
                                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel31)))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3))))
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel33)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 8, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(112, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout IfrReturPenjualanLayout = new javax.swing.GroupLayout(IfrReturPenjualan.getContentPane());
        IfrReturPenjualan.getContentPane().setLayout(IfrReturPenjualanLayout);
        IfrReturPenjualanLayout.setHorizontalGroup(
            IfrReturPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IfrReturPenjualanLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        IfrReturPenjualanLayout.setVerticalGroup(
            IfrReturPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        Daftar_Barang.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        Daftar_Barang.setTitle("DAFTAR BARANG - LUBADA JAYA MARKET");

        jScrollPane3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white));

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

        jPanel19.setBackground(new java.awt.Color(0, 204, 204));
        jPanel19.setPreferredSize(new java.awt.Dimension(786, 86));

        jLabel43.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        jLabel43.setForeground(java.awt.Color.red);
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setText("LUBADA JAYA");

        jLabel44.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        jLabel44.setForeground(java.awt.Color.blue);
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel44.setText("MARKET");

        lblFaktur2.setFont(new java.awt.Font("Arial Narrow", 1, 24)); // NOI18N
        lblFaktur2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblTgl2.setFont(new java.awt.Font("Arial Narrow", 1, 24)); // NOI18N
        lblTgl2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFaktur2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTgl2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblFaktur2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel44)
                    .addComponent(lblTgl2)))
        );

        javax.swing.GroupLayout Daftar_BarangLayout = new javax.swing.GroupLayout(Daftar_Barang.getContentPane());
        Daftar_Barang.getContentPane().setLayout(Daftar_BarangLayout);
        Daftar_BarangLayout.setHorizontalGroup(
            Daftar_BarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 786, Short.MAX_VALUE)
            .addGroup(Daftar_BarangLayout.createSequentialGroup()
                .addComponent(lblRecordBarang)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        Daftar_BarangLayout.setVerticalGroup(
            Daftar_BarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Daftar_BarangLayout.createSequentialGroup()
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblRecordBarang)
                .addContainerGap())
        );

        Daftar_Faktur.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        Daftar_Faktur.setTitle("DAFTAR PENJUALAN - LUBADA JAYA MARKET");

        jScrollPane4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

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
        jScrollPane4.setViewportView(tbDataPenjualan);

        lblRecordPenjualan.setFont(new java.awt.Font("Arial Narrow", 1, 14)); // NOI18N
        lblRecordPenjualan.setText("jLabel1");

        jPanel15.setBackground(new java.awt.Color(0, 204, 204));

        jPanel16.setBackground(new java.awt.Color(0, 204, 204));
        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "--FILTER DATA --"));

        txtCariNoFak.setPlaceholder("PENCARIAN : NO. FAKTUR");
        txtCariNoFak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariNoFakActionPerformed(evt);
            }
        });
        txtCariNoFak.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCariNoFakKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtCariNoFak, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(txtCariNoFak, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel35.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        jLabel35.setForeground(java.awt.Color.red);
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("LUBADA JAYA");

        jLabel37.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        jLabel37.setForeground(java.awt.Color.blue);
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("MARKET");

        jLabel38.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("DATA PENJUALAN BARANG");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel37)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout Daftar_FakturLayout = new javax.swing.GroupLayout(Daftar_Faktur.getContentPane());
        Daftar_Faktur.getContentPane().setLayout(Daftar_FakturLayout);
        Daftar_FakturLayout.setHorizontalGroup(
            Daftar_FakturLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Daftar_FakturLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(Daftar_FakturLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, Daftar_FakturLayout.createSequentialGroup()
                        .addComponent(lblRecordPenjualan)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 980, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        Daftar_FakturLayout.setVerticalGroup(
            Daftar_FakturLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Daftar_FakturLayout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblRecordPenjualan))
        );

        jPanel14.setBackground(new java.awt.Color(0, 204, 204));

        jLabel41.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        jLabel41.setForeground(java.awt.Color.blue);
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setText("EXPORT TO EXCEL");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel41)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel41)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel18.setBorder(new javax.swing.border.LineBorder(java.awt.Color.lightGray, 1, true));

        jLabel1.setText("LOKASI FILE");

        txtDir.setEditable(false);

        btnPilihLokasi.setText("PILIH LOKASI FILE");
        btnPilihLokasi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPilihLokasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihLokasiActionPerformed(evt);
            }
        });

        btnSimpanBackup.setText("SIMPAN");
        btnSimpanBackup.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSimpanBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanBackupActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtDir)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                        .addComponent(btnSimpanBackup)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 202, Short.MAX_VALUE)
                        .addComponent(btnPilihLokasi)))
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPilihLokasi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSimpanBackup, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("BACKUP", jPanel18);

        javax.swing.GroupLayout BackUp_RestoreLayout = new javax.swing.GroupLayout(BackUp_Restore.getContentPane());
        BackUp_Restore.getContentPane().setLayout(BackUp_RestoreLayout);
        BackUp_RestoreLayout.setHorizontalGroup(
            BackUp_RestoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(BackUp_RestoreLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        BackUp_RestoreLayout.setVerticalGroup(
            BackUp_RestoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BackUp_RestoreLayout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1))
        );

        setClosable(true);
        setTitle("DATA RETUR PENJUALAN - LUBADA JAYA MARKET");
        setPreferredSize(new java.awt.Dimension(1205, 585));

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "KLIK 2X --> MENGUBAH STATUS RETUR PENJUALAN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        tbDataRetur.setModel(new javax.swing.table.DefaultTableModel(
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
        tbDataRetur.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDataReturMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbDataRetur);

        lblRecordRetur.setFont(new java.awt.Font("Arial Narrow", 1, 12)); // NOI18N
        lblRecordRetur.setText("RECORD : 0");

        txtCari.setPlaceholder("NO.FAKTUR PENJUALAN");
        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCariKeyTyped(evt);
            }
        });

        btnTambah.setFont(new java.awt.Font("Arial Narrow", 1, 14)); // NOI18N
        btnTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/insert.png"))); // NOI18N
        btnTambah.setText("TAMBAH");
        btnTambah.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        lblNoFak.setFont(new java.awt.Font("Agency FB", 3, 24)); // NOI18N
        lblNoFak.setForeground(java.awt.Color.red);
        lblNoFak.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        btnBackUp.setFont(new java.awt.Font("Arial Narrow", 1, 14)); // NOI18N
        btnBackUp.setText("EXPORT TO EXCEL");
        btnBackUp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBackUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackUpActionPerformed(evt);
            }
        });

        btnRefresh.setFont(new java.awt.Font("Arial Narrow", 1, 14)); // NOI18N
        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/update_1_1.png"))); // NOI18N
        btnRefresh.setText("Refresh");
        btnRefresh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        dtTglCariAwal.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        dtTglCariAwal.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dtTglCariAwalPropertyChange(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel17.setText("S.D");

        dtTglCariAkhir.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        dtTglCariAkhir.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dtTglCariAkhirPropertyChange(evt);
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

        jLabel15.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel15.setText("CARI :");

        javax.swing.GroupLayout panelDataLayout = new javax.swing.GroupLayout(panelData);
        panelData.setLayout(panelDataLayout);
        panelDataLayout.setHorizontalGroup(
            panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1184, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDataLayout.createSequentialGroup()
                .addComponent(btnBackUp, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRefresh)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNoFak, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDataLayout.createSequentialGroup()
                .addComponent(lblRecordRetur)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dtTglCariAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dtTglCariAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCariTgl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnTambah))
        );
        panelDataLayout.setVerticalGroup(
            panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDataLayout.createSequentialGroup()
                .addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDataLayout.createSequentialGroup()
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(lblNoFak, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBackUp, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnTambah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblRecordRetur, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dtTglCariAkhir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dtTglCariAwal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCariTgl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        panelDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "(+) DETAIL RETUR PENJULAN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 12))); // NOI18N
        panelDetail.setPreferredSize(new java.awt.Dimension(1184, 560));

        jScrollPane5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white), "(+)DETAIL"));

        tbDetRetur_Penjualan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "NO. RETUR", "KODE BARANG", "NAMA BARANG", "HARGA BELI", "JUMLAH", "SUBTOTAL", "STATUS"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane5.setViewportView(tbDetRetur_Penjualan);

        lblRecordDetRetur.setFont(new java.awt.Font("Arial Narrow", 1, 12)); // NOI18N
        lblRecordDetRetur.setText("RECORD : 0");

        panelUbah.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white), "(+) FORM UPDATE STATUS"));

        jLabel19.setText("TOTAL");

        jLabel20.setText("MEMBER");

        txtJumlahNew.setEditable(false);
        txtJumlahNew.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtJumlahNew.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N

        txtTotalNew.setEditable(false);
        txtTotalNew.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true), "Ket : "));

        lblKeterangan.setFont(new java.awt.Font("Arial Narrow", 1, 14)); // NOI18N
        lblKeterangan.setForeground(new java.awt.Color(255, 0, 0));
        lblKeterangan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblKeterangan.setText("KET");
        lblKeterangan.setOpaque(true);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblKeterangan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblKeterangan, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtNoReturNew.setEditable(false);
        txtNoReturNew.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNoReturNew.setText("NO. RETUR PENJUALAN");
        txtNoReturNew.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N

        jLabel23.setText("JUMLAH RETUR -->> STATUS");

        txtMemberNew.setEditable(false);
        txtMemberNew.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMemberNew.setText("MEMBER");
        txtMemberNew.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N

        jLabel25.setText("-->>");

        txtStatusBrg.setEditable(false);
        txtStatusBrg.setForeground(new java.awt.Color(255, 0, 0));
        txtStatusBrg.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtStatusBrg.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true), "PILIH STATUS BARU : "));

        buttonGroup2.add(rb1new);
        rb1new.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        rb1new.setText("1");

        buttonGroup2.add(rb2new);
        rb2new.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        rb2new.setText("2");

        buttonGroup2.add(rb3new);
        rb3new.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        rb3new.setText("3");

        buttonGroup2.add(rb4new);
        rb4new.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        rb4new.setText("4");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(rb1new)
                .addGap(18, 18, 18)
                .addComponent(rb2new)
                .addGap(18, 18, 18)
                .addComponent(rb3new)
                .addGap(18, 18, 18)
                .addComponent(rb4new)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rb3new)
                        .addComponent(rb4new))
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rb1new)
                        .addComponent(rb2new)))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        btnSimpanNew.setText("SIMPAN");
        btnSimpanNew.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSimpanNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanNewActionPerformed(evt);
            }
        });

        lblUser.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        lblUser.setForeground(java.awt.Color.red);
        lblUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblUsersistem.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        lblUsersistem.setForeground(java.awt.Color.red);
        lblUsersistem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panelUbahLayout = new javax.swing.GroupLayout(panelUbah);
        panelUbah.setLayout(panelUbahLayout);
        panelUbahLayout.setHorizontalGroup(
            panelUbahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUbahLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelUbahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNoReturNew, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelUbahLayout.createSequentialGroup()
                        .addGroup(panelUbahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                        .addGroup(panelUbahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelUbahLayout.createSequentialGroup()
                                .addComponent(txtJumlahNew, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel25)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtStatusBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtTotalNew, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(panelUbahLayout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtMemberNew, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelUbahLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblUser, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblUsersistem, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSimpanNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelUbahLayout.setVerticalGroup(
            panelUbahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUbahLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtNoReturNew, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelUbahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMemberNew, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelUbahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtJumlahNew, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel25)
                    .addComponent(txtStatusBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelUbahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotalNew, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelUbahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnSimpanNew, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(lblUsersistem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnEdit.setFont(new java.awt.Font("Arial Narrow", 1, 14)); // NOI18N
        btnEdit.setText("EDIT DATA");
        btnEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnKembali.setFont(new java.awt.Font("Arial Narrow", 1, 14)); // NOI18N
        btnKembali.setText("KEMBALI");
        btnKembali.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKembaliActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelDetailLayout = new javax.swing.GroupLayout(panelDetail);
        panelDetail.setLayout(panelDetailLayout);
        panelDetailLayout.setHorizontalGroup(
            panelDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDetailLayout.createSequentialGroup()
                .addComponent(panelUbah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDetailLayout.createSequentialGroup()
                        .addComponent(lblRecordDetRetur)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnKembali))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 801, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelDetailLayout.setVerticalGroup(
            panelDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDetailLayout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnKembali, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblRecordDetRetur, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addComponent(panelUbah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 1194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panelData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelDetail, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tbDataBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDataBarangMouseClicked
        if(evt.getClickCount()==1){
            vid_barang = tbDataBarang.getValueAt(tbDataBarang.getSelectedRow(), 2).toString();
            tampilBarang();
       }                       
    }//GEN-LAST:event_tbDataBarangMouseClicked

    private void tbTransReturMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbTransReturMouseClicked
       if(evt.getClickCount()==1){
           vno_faktur = txtNoFaktur.getText();
           vid_barang = tbTransRetur.getValueAt(tbTransRetur.getSelectedRow(), 1).toString();
           vjumlah_barang = (int) tbTransRetur.getValueAt(tbTransRetur.getSelectedRow(), 4);
           tampilBarang();
           txtJumlah.setValue(vjumlah_barang);
           btnHapusBrg.setEnabled(true);
       }   
    }//GEN-LAST:event_tbTransReturMouseClicked

    private void tbDataReturMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDataReturMouseClicked
        if(evt.getClickCount()==2){
            vno_retur = tbDataRetur.getValueAt(tbDataRetur.getSelectedRow(), 0).toString();
            panelData.setVisible(false);
            panelDetail.setVisible(true);
            setTabel_DetRetur_Penjualan();
            showDetail_Retur_Penjualan();
            getDataRetur(); 
       }else{
            vno_retur = tbDataRetur.getValueAt(tbDataRetur.getSelectedRow(), 0).toString();
            vnm_member = tbDataRetur.getValueAt(tbDataRetur.getSelectedRow(), 4).toString();
            lblNoFak.setText(vno_retur+" - "+vnm_member);
        }
    }//GEN-LAST:event_tbDataReturMouseClicked

    private void txtCariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyTyped
        cariData_Retur_Penjualan();
    }//GEN-LAST:event_txtCariKeyTyped

    private void tbDataPenjualanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDataPenjualanMouseClicked
        if(evt.getClickCount()==1){
            vno_faktur = tbDataPenjualan.getValueAt(tbDataPenjualan.getSelectedRow(), 0).toString();
            tampilMember();
       } 
    }//GEN-LAST:event_tbDataPenjualanMouseClicked

    private void txtNoFakturActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoFakturActionPerformed
        vno_faktur=txtNoFaktur.getText();
        tampilMember();
    }//GEN-LAST:event_txtNoFakturActionPerformed

    private void btnDaftarFakturActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDaftarFakturActionPerformed
        formTengah();
        Daftar_Faktur.setVisible(true);
        Daftar_Faktur.setSize(980, 580);
        Daftar_Faktur.setLocationRelativeTo(this);
        setTabel_Penjualan();
        showDataPenjualan();
    }//GEN-LAST:event_btnDaftarFakturActionPerformed

    private void btnDaftarBrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDaftarBrgActionPerformed
        Daftar_Barang.setVisible(true);
        Daftar_Barang.setSize(918, 520);
        Daftar_Barang.setLocationRelativeTo(this);
        setTabelBarang();
        showDataBarang();
    }//GEN-LAST:event_btnDaftarBrgActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if( txtNmMember.getText().length() > 0 && txtNoFaktur.getText().length() == 0 ){
            JOptionPane.showMessageDialog(null, "NO FAKTUR TIDAK DITEMUKAN !",
                "Error", JOptionPane.ERROR_MESSAGE); txtNoFaktur.requestFocus();
        }else if(txtNoFaktur.getText().equals("")){
            JOptionPane.showMessageDialog(null, "TIDAK ADA BARANG YANG DIRETUR !",
                    "Error", JOptionPane.ERROR_MESSAGE); txtNoFaktur.requestFocus();
        }else if(buttonGroup1.isSelected(null)){
                JOptionPane.showMessageDialog(null, "STATUS RETUR BELUM DI PILIH !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE); rb1.requestFocus();
        }else if(txtTotal.getText().equals("0")){
            JOptionPane.showMessageDialog(null, "TIDAK ADA BARANG YANG DIRETUR !",
                    "Error", JOptionPane.ERROR_MESSAGE);
            txtDaftarBrg.requestFocus();
        }else{
            aksiSimpan_Retur_Penjualan();
            tblTransRetur_Penjualan.getDataVector().removeAllElements();
            tblTransRetur_Penjualan .fireTableDataChanged();
        } 
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnReturPenjualanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReturPenjualanActionPerformed
        int jumlah_baris = tblTransRetur_Penjualan.getRowCount();
        if(jumlah_baris > 0 && btnSimpan.getText().equals("SIMPAN")){
            int jawab = JOptionPane.showConfirmDialog(null, 
                "ANDA YAKIN INGIN KELUAR DARI TRANSAKSI INI ?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if(jawab==JOptionPane.YES_OPTION){
                clearForm(); enableForm(); IfrReturPenjualan.dispose(); showData_Retur_Penjualan();
                btnSimpan.setVisible(true); btnSimpan.setText("SIMPAN"); btnUbah.setVisible(false); 
                btnDaftarBrg.setEnabled(true); 
            }
        }else if(jumlah_baris > 0 && btnSimpan.getText().equals("")){
            int jawab = JOptionPane.showConfirmDialog(null, 
                "ANDA YAKIN INGIN KELUAR DARI TRANSAKSI INI ?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if(jawab==JOptionPane.YES_OPTION){
                clearForm(); enableForm(); IfrReturPenjualan.dispose(); showData_Retur_Penjualan();
                btnSimpan.setVisible(true); btnSimpan.setText("SIMPAN"); btnUbah.setVisible(false); 
                btnDaftarBrg.setEnabled(true); 
            }
        }else{
            clearForm(); enableForm(); IfrReturPenjualan.dispose(); showData_Retur_Penjualan();
            btnSimpan.setVisible(true); btnSimpan.setText("SIMPAN"); btnUbah.setVisible(false); 
            btnDaftarBrg.setEnabled(true); 
        }
    }//GEN-LAST:event_btnReturPenjualanActionPerformed

    private void txtDaftarBrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDaftarBrgActionPerformed
        vid_barang=txtDaftarBrg.getText();
        tampilBarang();
    }//GEN-LAST:event_txtDaftarBrgActionPerformed

    private void txtCariNoFakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariNoFakActionPerformed
        showDataPenjualan();
    }//GEN-LAST:event_txtCariNoFakActionPerformed

    private void txtCariNoFakKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariNoFakKeyTyped
        showDataPenjualan();
    }//GEN-LAST:event_txtCariNoFakKeyTyped

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        IfrReturPenjualan.setVisible(true);
        IfrReturPenjualan.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTabel_TransRetur_Penjualan();
        autoNoRetur();
        clearForm();
        enableForm();
        btnSimpan.setVisible(true); btnSimpan.setText("SIMPAN"); btnUbah.setVisible(false);
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        if( txtNmMember.getText().length() > 0 && txtNoFaktur.getText().length() == 0 ){
            JOptionPane.showMessageDialog(null, "MEMBER TIDAK DITEMUKAN !",
                "Error", JOptionPane.ERROR_MESSAGE); txtNoFaktur.requestFocus();
        }else if(txtNoFaktur.getText().equals("")){
            JOptionPane.showMessageDialog(null, "TIDAK ADA BARANG YANG DIRETUR !",
                "Error", JOptionPane.ERROR_MESSAGE); txtNoFaktur.requestFocus();
        }else if(buttonGroup1.isSelected(null)){
            JOptionPane.showMessageDialog(null, "STATUS RETUR BELUM DI PILIH !",
                "Informasi", JOptionPane.INFORMATION_MESSAGE); rb1.requestFocus();
        }else{
            aksiUbahRetur();
            clearForm(); enableForm(); IfrReturPenjualan.dispose(); panelData.setVisible(true);panelDetail.setVisible(false);
            setTabel_Retur_Penjualan(); showData_Retur_Penjualan();
        }

    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        lblBaris.setText("");
        int jumlah_baris = tblTransRetur_Penjualan.getRowCount();
        if(jumlah_baris > 0 && btnSimpan.getText().equals("SIMPAN")){
            int jawab = JOptionPane.showConfirmDialog(null,
                "ANDA YAKIN INGIN MEMBATALKAN TRANSAKSI "+txtNoRetur.getText()+" INI ?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if(jawab==JOptionPane.YES_OPTION){
                clearForm(); enableForm(); btnSimpan.setVisible(true);
                btnSimpan.setText("SIMPAN"); btnUbah.setVisible(false);
                tblTransRetur_Penjualan.getDataVector().removeAllElements();
                tblTransRetur_Penjualan.fireTableDataChanged(); autoNoRetur();
                txtDaftarBrg.setEditable(true); txtNoFaktur.setEditable(true);
                txtJumlah.setEnabled(true); 
            }
        }else if(jumlah_baris > 0 && btnSimpan.getText().equals("")){
            int jawab = JOptionPane.showConfirmDialog(null,
                "SIMPAN TRANSAKSI ?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if(jawab==JOptionPane.YES_OPTION){
                aksiUbahRetur(); clearForm(); enableForm(); IfrReturPenjualan.dispose(); panelData.setVisible(true);panelDetail.setVisible(false);
                setTabel_Retur_Penjualan(); showData_Retur_Penjualan();
                btnSimpan.setVisible(true); btnSimpan.setText("SIMPAN");
            }else if(jawab==JOptionPane.NO_OPTION){
                clearForm(); enableForm(); btnSimpan.setVisible(true);
                btnSimpan.setText("SIMPAN"); btnUbah.setVisible(false);
                tblTransRetur_Penjualan.getDataVector().removeAllElements();
                tblTransRetur_Penjualan.fireTableDataChanged(); autoNoRetur();
                txtDaftarBrg.setEditable(true); txtNoFaktur.setEditable(true);
                txtJumlah.setEnabled(true);setTabel_Retur_Penjualan(); showData_Retur_Penjualan();
            }
        }else{
            clearForm(); enableForm(); IfrReturPenjualan.dispose(); panelData.setVisible(true);panelDetail.setVisible(false);
            setTabel_Retur_Penjualan(); showData_Retur_Penjualan();
            btnSimpan.setVisible(true); btnSimpan.setText("SIMPAN");
        }
    }//GEN-LAST:event_btnBatalActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        IfrReturPenjualan.setVisible(true);
        IfrReturPenjualan.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTabel_TransRetur_Penjualan();
        getData();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKembaliActionPerformed
        panelDetail.setVisible(false);
        panelData.setVisible(true); showData_Retur_Penjualan();
    }//GEN-LAST:event_btnKembaliActionPerformed

    private void btnSimpanNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanNewActionPerformed
        if(buttonGroup2.isSelected(null)){
            JOptionPane.showMessageDialog(null, "STATUS BARU BELUM DI PILIH !",
                "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }else {
            aksiUbahStatus();
        }
    }//GEN-LAST:event_btnSimpanNewActionPerformed

    private void btnBackUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackUpActionPerformed
        BackUp_Restore.setVisible(true);
        BackUp_Restore.setSize(500, 300);
        BackUp_Restore.setLocationRelativeTo(this);
    }//GEN-LAST:event_btnBackUpActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        setJam();
        setTabel_Retur_Penjualan();
        showData_Retur_Penjualan();
        clearForm();
        txtNoFaktur.requestFocus();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void dtTglCariAwalPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dtTglCariAwalPropertyChange
        dtTglCariAkhir.setDate(dtTglCariAwal.getDate());
        txtCari.setText("");
    }//GEN-LAST:event_dtTglCariAwalPropertyChange

    private void dtTglCariAkhirPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dtTglCariAkhirPropertyChange
        tanggal_Pencarian();
    }//GEN-LAST:event_dtTglCariAkhirPropertyChange

    private void btnCariTglActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariTglActionPerformed
        setTabel_Retur_Penjualan(); cariData_Retur_Penjualan();
    }//GEN-LAST:event_btnCariTglActionPerformed

    private void txtJumlahStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_txtJumlahStateChanged
        hitungSubtotal();
    }//GEN-LAST:event_txtJumlahStateChanged

    private void txtJumlahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyPressed
        tambahBarang();
    }//GEN-LAST:event_txtJumlahKeyPressed

    private void txtJumlahKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyTyped
        if ( txtJumlah.getValue().toString().length() == 2 ) {
            evt.consume();
        }
    }//GEN-LAST:event_txtJumlahKeyTyped

    private void btnTambahBrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahBrgActionPerformed
        tambahBarang();
    }//GEN-LAST:event_btnTambahBrgActionPerformed

    private void btnHapusBrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusBrgActionPerformed
        hapusBarang();
    }//GEN-LAST:event_btnHapusBrgActionPerformed

    private void btnPilihLokasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPilihLokasiActionPerformed
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Excel Workbook", "xls" );
        jFileChooser1.setFileFilter(filter);
        int returnVal = jFileChooser1.showSaveDialog(this);
        if(returnVal == jFileChooser1.APPROVE_OPTION) {
            txtDir.setText(jFileChooser1.getSelectedFile().getPath()+".xls");
        }
    }//GEN-LAST:event_btnPilihLokasiActionPerformed

    private void btnSimpanBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanBackupActionPerformed
        if(txtDir.getText().equals("")){
            JOptionPane.showMessageDialog(null, "LOKASI FILE BELUM DITENTUKAN !"); btnPilihLokasi.requestFocus();
        }else{
            toExcel(tbDataRetur, new File(txtDir.getText()));
        }
    }//GEN-LAST:event_btnSimpanBackupActionPerformed

    private void txtDaftarBrgKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDaftarBrgKeyTyped
        if ( txtDaftarBrg.getText().length() == 13 ) {
        evt.consume();
        }
    }//GEN-LAST:event_txtDaftarBrgKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFrame BackUp_Restore;
    private javax.swing.JDialog Daftar_Barang;
    private javax.swing.JDialog Daftar_Faktur;
    private javax.swing.JFrame IfrReturPenjualan;
    private javax.swing.JButton btnBackUp;
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnCariTgl;
    private javax.swing.JButton btnDaftarBrg;
    private javax.swing.JButton btnDaftarFaktur;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapusBrg;
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnPilihLokasi;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnReturPenjualan;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnSimpanBackup;
    private javax.swing.JButton btnSimpanNew;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnTambahBrg;
    private javax.swing.JButton btnUbah;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private com.toedter.calendar.JDateChooser dtTglCariAkhir;
    private com.toedter.calendar.JDateChooser dtTglCariAwal;
    private com.toedter.calendar.JDateChooser dtTglRetur;
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
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel5;
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
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
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
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblBaris;
    private javax.swing.JLabel lblFaktur2;
    private javax.swing.JLabel lblJam;
    private javax.swing.JLabel lblKeterangan;
    private javax.swing.JLabel lblNoFak;
    private javax.swing.JLabel lblRecordBarang;
    private javax.swing.JLabel lblRecordDetRetur;
    private javax.swing.JLabel lblRecordPenjualan;
    private javax.swing.JLabel lblRecordRetur;
    private javax.swing.JLabel lblTgl2;
    private javax.swing.JLabel lblUser;
    private javax.swing.JLabel lblUsersistem;
    private javax.swing.JPanel panelData;
    private javax.swing.JPanel panelDetail;
    private javax.swing.JPanel panelUbah;
    private javax.swing.JRadioButton rb1;
    private javax.swing.JRadioButton rb1new;
    private javax.swing.JRadioButton rb2;
    private javax.swing.JRadioButton rb2new;
    private javax.swing.JRadioButton rb3;
    private javax.swing.JRadioButton rb3new;
    private javax.swing.JRadioButton rb4;
    private javax.swing.JRadioButton rb4new;
    private javax.swing.JTable tbDataBarang;
    private javax.swing.JTable tbDataPenjualan;
    private javax.swing.JTable tbDataRetur;
    private javax.swing.JTable tbDetRetur_Penjualan;
    private javax.swing.JTable tbTransRetur;
    private Tool.Custom_TextField txtCari;
    private Tool.Custom_TextField txtCariNoFak;
    private Tool.Custom_TextField txtDaftarBrg;
    private javax.swing.JTextField txtDir;
    private Tool.NumberTextField txtHargaJual;
    private Tool.Custom_TextField txtIdMember;
    private javax.swing.JSpinner txtJumlah;
    private Tool.NumberTextField txtJumlahNew;
    private Tool.Custom_TextField txtMemberNew;
    private Tool.Custom_TextField txtNamaBrg;
    private Tool.Custom_TextField txtNmMember;
    private Tool.Custom_TextField txtNoFaktur;
    private Tool.Custom_TextField txtNoRetur;
    private Tool.Custom_TextField txtNoReturNew;
    private Tool.NumberTextField txtQty;
    private Tool.NumberTextField txtStatusBrg;
    private Tool.NumberTextField txtSubtotal;
    private Tool.NumberTextField txtTotal;
    private Tool.NumberTextField txtTotalNew;
    // End of variables declaration//GEN-END:variables
}
