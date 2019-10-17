package Form;

import Tool.KoneksiDB;
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

public class FrRetur_Pembelian extends javax.swing.JInternalFrame {

    KoneksiDB getCnn = new KoneksiDB();
    Connection _Cnn;
    
    String sqlselect, sqlinsert, sqldelete;
    String vid_barang, vnm_barang;
    String vid_kategori, vnm_kategori;
    String vid_distributor, vnm_distributor;
    String vno_transaksi, vno_retur, vno_faktur, vno_invoice, vid_user, vtgl_trans, vnm_user, vstatus_brg, vtglawal, vtglakhir;
    
    double vhrg_beli, vhrg_jual, vsubtotal, vtotal;
    int vjumlah_barang, vqty;
    
    static Object[]Kolom;
    DefaultTableModel tblRetur_BM, tblDetail_returBM, tblTransRetur_BM, tblPembelian, tblBarang;
    
    SimpleDateFormat tglview = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat tglinput = new SimpleDateFormat("yyyy-MM-dd");
    
    String id = FrMenu.getU_id();
    String vakses = FrMenu.getU_hakakses();
    String vusername = FrMenu.getU_username();
    
    public FrRetur_Pembelian() {
        initComponents();
        
        akses();
        setTabel_Retur_BM();
        showData_ReturBM();
        listDis();
        setJam(); 
        clearForm();
        txtCari.requestFocus();
    }
    private void akses(){
        lblUser.setText(vakses);
        lblUserSistem.setText(vusername);
        if(lblUser.getText().equals("KASIR")){
            btnBackUp.setEnabled(false);
            lblUser.setVisible(false);
            lblUserSistem.setVisible(false);
        }else{
            btnBackUp.setEnabled(true);
            lblUser.setVisible(false);
            lblUserSistem.setVisible(false);
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
        txtIdDis.setText("");
        txtNmDis.setText("");
        buttonGroup1.clearSelection();
        dtTglRetur.setDate(new Date());
        dtTglCariAwal.setDate(new Date());
        dtTglCariAkhir.setDate(new Date());
        cmbDis.setSelectedIndex(0);
        txtDaftarBrg.setText("");
        txtNamaBrg.setText("");
        txtHargaBeli.setText("");
        txtQty.setText("");
        txtJumlah.setValue(0);
        txtSubtotal.setText("");
        txtTotal.setText("");
        txtNoFaktur.requestFocus();
    }
    private void disableForm(){
        rb1.setEnabled(false); rb2.setEnabled(false); rb3.setEnabled(false);
        txtHargaBeli.setEnabled(false);
        txtQty.setEnabled(false);
        txtJumlah.setEnabled(false);
        txtDaftarBrg.setEnabled(false);
        txtNamaBrg.setEnabled(false);
        txtTotal.setEnabled(false);
        txtSubtotal.setEnabled(false);
        btnDaftarBrg.setEnabled(false);
        btnDaftarFaktur.setEnabled(false);
        btnTambahBrg.setEnabled(false);
        btnHapusBrg.setEnabled(false);
        btnSimpan.setEnabled(false);
    }
    private void enableForm(){
        rb1.setEnabled(true); rb2.setEnabled(true); rb3.setEnabled(true);
        txtHargaBeli.setEnabled(true);
        txtJumlah.setEnabled(true);
        txtQty.setEnabled(true);
        txtSubtotal.setEnabled(true);
        txtDaftarBrg.setEnabled(true);
        txtNamaBrg.setEnabled(true);
        txtTotal.setEnabled(true);
        btnDaftarBrg.setEnabled(true);
        btnDaftarFaktur.setEnabled(true);
        btnTambahBrg.setEnabled(true);
        btnHapusBrg.setEnabled(false);
        btnSimpan.setEnabled(true);
    }
    private void setTabel_Retur_BM(){
        String[]kolom1 = {"NO. RETUR", "NO. FAKTUR-NO. INVOICE", "PEGAWAI", "TGL RETUR", "DISTRIBUTOR", "QTY RETUR", "TOTAL (Rp)", "STATUS"};
        tblRetur_BM = new DefaultTableModel(null,kolom1){
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
                int cola = tblRetur_BM.getColumnCount();
                return (col<cola) ? false : true;
            }
        };
        tbDataRetur.setModel(tblRetur_BM);
        tbDataRetur.getColumnModel().getColumn(0).setPreferredWidth(120);
        tbDataRetur.getColumnModel().getColumn(1).setPreferredWidth(200);
        tbDataRetur.getColumnModel().getColumn(2).setPreferredWidth(200);
        tbDataRetur.getColumnModel().getColumn(3).setPreferredWidth(120);
        tbDataRetur.getColumnModel().getColumn(4).setPreferredWidth(200);
        tbDataRetur.getColumnModel().getColumn(5).setPreferredWidth(80);
        tbDataRetur.getColumnModel().getColumn(6).setPreferredWidth(120);
        tbDataRetur.getColumnModel().getColumn(7).setPreferredWidth(75);
        
    }
    private void setTabel_DetRetur_BM(){
        int baris = 0;
        String[]kolom1 = {"NO. RETUR", "KODE BARANG", "NAMA BARANG", "HARGA BELI (Rp)",  "JUMLAH", "SUBTOTAL (Rp)", "STATUS"};
        tblDetail_returBM = new DefaultTableModel(kolom1,baris){
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
                int cola = tblDetail_returBM.getColumnCount();
                return (col<cola) ? false : true;
            }
        };
        tbDetRetur_BM.setModel(tblDetail_returBM);
        tbDetRetur_BM.getColumnModel().getColumn(0).setPreferredWidth(150);
        tbDetRetur_BM.getColumnModel().getColumn(1).setPreferredWidth(130);
        tbDetRetur_BM.getColumnModel().getColumn(2).setPreferredWidth(200);
        tbDetRetur_BM.getColumnModel().getColumn(3).setPreferredWidth(120);
        tbDetRetur_BM.getColumnModel().getColumn(4).setPreferredWidth(70);
        tbDetRetur_BM.getColumnModel().getColumn(5).setPreferredWidth(120);
        tbDetRetur_BM.getColumnModel().getColumn(6).setPreferredWidth(70);
        
    }
    private void setTabel_TransRetur_BM(){
        int baris = 0;
        String[]kolom1 = {"NO. RETUR", "KODE BARANG", "NAMA BARANG", "HARGA BELI (Rp)", "JUMLAH", "SUBTOTAL (Rp)", "ID"};
        tblTransRetur_BM = new DefaultTableModel(kolom1,baris){
            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.Double.class,
                java.lang.Integer.class,
                java.lang.Double.class,
                java.lang.String.class
                
            };
            public Class getColumnClass(int columnIndex){
                return types [columnIndex];
            }
            public boolean isCellEditable(int row, int col){
                int cola = tblTransRetur_BM.getColumnCount();
                return (col<cola) ? false : true;
            }
        };
        tbTransRetur.setModel(tblTransRetur_BM);
        tbTransRetur.getColumnModel().getColumn(0).setPreferredWidth(150);
        tbTransRetur.getColumnModel().getColumn(1).setPreferredWidth(100);
        tbTransRetur.getColumnModel().getColumn(2).setPreferredWidth(200);
        tbTransRetur.getColumnModel().getColumn(3).setPreferredWidth(150);
        tbTransRetur.getColumnModel().getColumn(4).setPreferredWidth(75);
        tbTransRetur.getColumnModel().getColumn(5).setPreferredWidth(150);
        tbTransRetur.getColumnModel().getColumn(6).setPreferredWidth(25);
        
    }
    private void setTabel_Pembelian(){
        String[]kolom1 = {"NO. FAKTUR", "NO. INVOICE", "TANGGAL", "DISTRIBUTOR", "PEGAWAI", "TOTAL (Rp)"};
        tblPembelian = new DefaultTableModel(null,kolom1){
            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.Double.class
                
            };
            public Class getColumnClass(int columnIndex){
                return types [columnIndex];
            }
            public boolean isCellEditable(int row, int col){
                int cola = tblPembelian.getColumnCount();
                return (col<cola) ? false : true;
            }
        };
        tbDataPembelian.setModel(tblPembelian);
        tbDataPembelian.getColumnModel().getColumn(0).setPreferredWidth(120);
        tbDataPembelian.getColumnModel().getColumn(1).setPreferredWidth(120);
        tbDataPembelian.getColumnModel().getColumn(2).setPreferredWidth(75);
        tbDataPembelian.getColumnModel().getColumn(3).setPreferredWidth(200);
        tbDataPembelian.getColumnModel().getColumn(4).setPreferredWidth(200);
        tbDataPembelian.getColumnModel().getColumn(5).setPreferredWidth(100);
        
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
        int row_BM  = tblRetur_BM.getRowCount();
        for(int i = 0; i <row_BM; i++){
            tblRetur_BM.removeRow(0);
        }
    }
    private void cariData_ReturBM(){
        vtgl_trans = tglinput.format(dtTglCariAwal.getDate());
        String vtgl_trans1 = tglinput.format(dtTglCariAkhir.getDate());
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            clearTabel_Retur();
            if(txtCari.getText().length() > 0 ){
                sqlselect = "select * from tb_retur_pembelian a, tb_pembelian b, tb_user c, tb_distributor d "
                    + " where a.no_faktur_pembelian=b.no_faktur_pembelian "
                    + " and a.id_user=c.id_user and b.id_distributor=d.id_distributor and a.no_faktur_pembelian like '%"+txtCari.getText()+"%' "
                    + " order by tgl_retur desc, a.no_retur_pembelian desc"; 
            }else{
                sqlselect = "select * from tb_retur_pembelian a, tb_pembelian b, tb_user c, tb_distributor d "
                    + " where a.no_faktur_pembelian=b.no_faktur_pembelian "
                    + " and a.id_user=c.id_user and b.id_distributor=d.id_distributor "
                    + " and tgl_retur between '"+vtgl_trans+"' and '"+vtgl_trans1+"' "
                    + " order by tgl_retur desc, a.no_retur_pembelian desc"; 
            }
            
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);// mengeksekusi query select
            while(res.next()){
                vno_retur = res.getString("no_retur_pembelian");
                vno_faktur = res.getString("no_faktur_pembelian")+"-"+res.getString("no_invoice");;
                vid_user = res.getString("id_user")+"-"+res.getString("nama_lengkap");
                vtgl_trans = tglview.format(res.getDate("tgl_retur"));
                vnm_distributor = res.getString("nama_distributor");
                vjumlah_barang = res.getInt("jumlah_retur");
                vtotal = res.getDouble("total");
                vstatus_brg = res.getString("status_retur");
                Object[]data = {vno_retur, vno_faktur, vid_user, vtgl_trans, vnm_distributor, vjumlah_barang, vtotal, vstatus_brg};
                tblRetur_BM.addRow(data);
            }
            lblRecordRetur.setText("RECORD : "+tblRetur_BM.getRowCount());
            panelDetail.setVisible(false); panelData.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method showData_ReturBM() : "+ex);
        }
    }    
    private void showData_ReturBM(){
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            clearTabel_Retur();
            sqlselect = "select * from tb_retur_pembelian a, tb_pembelian b, tb_user c, tb_distributor d "
                    + " where a.no_faktur_pembelian=b.no_faktur_pembelian "
                    + " and a.id_user=c.id_user and b.id_distributor=d.id_distributor "
                    + " order by tgl_retur desc, a.no_retur_pembelian desc"; 
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);// mengeksekusi query select
            while(res.next()){
                vno_retur = res.getString("no_retur_pembelian");
                vno_faktur = res.getString("no_faktur_pembelian")+"-"+res.getString("no_invoice");;
                vid_user = res.getString("id_user")+"-"+res.getString("nama_lengkap");
                vtgl_trans = tglview.format(res.getDate("tgl_retur"));
                vnm_distributor = res.getString("nama_distributor");
                vjumlah_barang = res.getInt("jumlah_retur");
                vtotal = res.getDouble("total");
                vstatus_brg = res.getString("status_retur");
                Object[]data = {vno_retur, vno_faktur, vid_user, vtgl_trans, vnm_distributor, vjumlah_barang, vtotal, vstatus_brg};
                tblRetur_BM.addRow(data);
            }
            lblRecordRetur.setText("RECORD : "+tblRetur_BM.getRowCount());
            panelDetail.setVisible(false);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method showData_ReturBM() : "+ex);
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
    private void showPerDis(){
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            if(cmbDis.getSelectedIndex()==0){
                sqlselect = "select * from tb_retur_pembelian a, tb_pembelian b, tb_user c, tb_distributor d "
                    + " where a.no_faktur_pembelian=b.no_faktur_pembelian "
                    + " and a.id_user=c.id_user and b.id_distributor=d.id_distributor "
                    + " order by tgl_retur desc, a.no_retur_pembelian desc"; 
            }else{
                sqlselect = "select * from tb_retur_pembelian a, tb_pembelian b, tb_user c, tb_distributor d "
                    + " where a.no_faktur_pembelian=b.no_faktur_pembelian "
                    + " and a.id_user=c.id_user and b.id_distributor=d.id_distributor "
                    + " and nama_distributor='"+cmbDis.getSelectedItem().toString()+"' "
                    + " order by tgl_retur desc, a.no_retur_pembelian desc"; 
            }
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);// mengeksekusi query select
            while(res.next()){
                vno_retur = res.getString("no_retur_pembelian");
                vno_faktur = res.getString("no_faktur_pembelian")+"-"+res.getString("no_invoice");;
                vid_user = res.getString("id_user")+"-"+res.getString("nama_lengkap");
                vtgl_trans = tglview.format(res.getDate("tgl_retur"));
                vnm_distributor = res.getString("nama_distributor");
                vjumlah_barang = res.getInt("jumlah_retur");
                vtotal = res.getDouble("total");
                vstatus_brg = res.getString("status_retur");
                Object[]data = {vno_retur, vno_faktur, vid_user, vtgl_trans, vnm_distributor, vjumlah_barang, vtotal, vstatus_brg};
                tblRetur_BM.addRow(data);
            }
            lblRecordRetur.setText("RECORD : "+tblRetur_BM.getRowCount());
            panelDetail.setVisible(false); panelData.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method showData_ReturBM() : "+ex);
        }
    }
    private void listDis(){
        try {
        _Cnn = null;
        _Cnn = getCnn.getConnection();
        sqlselect = "SELECT * FROM tb_distributor";
        Statement stat = _Cnn.createStatement();
        ResultSet res = stat.executeQuery(sqlselect);
        
        while(res.next()){
            Object[] ob = new Object[1];
            ob[0] = res.getString("nama_distributor");
            cmbDis.addItem((String) ob[0]);
        }
        res.close(); stat.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
     private void showDetail_ReturBM(){
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            sqlselect = "select * from tb_retur_pembelian a, tb_detail_retur_pembelian b, tb_barang c "
                    + " where a.no_retur_pembelian=b.no_retur_pembelian and b.id_barang=c.id_barang and "
                    + " b.no_retur_pembelian='"+vno_retur+"' "
                    + " order by nama_barang asc"; 
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);// mengeksekusi query select
            while(res.next()){
                vno_retur = res.getString("no_retur_pembelian");
                vid_barang = res.getString("id_barang");
                vnm_barang = res.getString("nama_barang");
                vhrg_beli = res.getDouble("harga_beli");
                vjumlah_barang = res.getInt("jumlah_barang");
                vsubtotal = res.getDouble("subtotal");
                vstatus_brg = res.getString("status_retur");
                Object[]data = { vno_retur, vid_barang, vnm_barang, vhrg_beli, vjumlah_barang, vsubtotal, vstatus_brg};
                tblDetail_returBM.addRow(data);
                panelUbah.setVisible(true);
            }
            lblRecordDetRetur.setText("RECORD : "+tblDetail_returBM.getRowCount()); btnTambah.requestFocus();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method showData_DetBM() : "+ex);
        }
    }
    private void clearTabel_Pembelian(){
        int row_BM  = tblPembelian.getRowCount();
        for(int i = 0; i <row_BM; i++){
            tblPembelian.removeRow(0);
        }
    }
    private void showDataPembelian(){
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            clearTabel_Pembelian();
            sqlselect = "select * from tb_pembelian a, tb_distributor b, tb_user c "
                    +"where a.id_distributor=b.id_distributor and a.id_user=c.id_user and a.no_faktur_pembelian like '%"+txtCariNoFak.getText()+"%'"
                    +"order by tgl_transaksi desc, no_faktur_pembelian desc"; 
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);// mengeksekusi query select
            while(res.next()){
                vno_faktur = res.getString("no_faktur_pembelian");
                vno_invoice = res.getString("no_invoice");
                vtgl_trans = tglview.format(res.getDate("tgl_transaksi"));
                vnm_distributor = res.getString("nama_distributor");
                vnm_user = res.getString("id_user")+"-"+res.getString("nama_lengkap");
                vtotal = res.getDouble("total");
                Object[]data = {vno_faktur, vno_invoice, vtgl_trans, vnm_distributor, vnm_user, vtotal};
                tblPembelian.addRow(data);
            }
            lblRecordPembelian.setText("RECORD : "+tblPembelian.getRowCount());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method showDataBM() : "+ex);
        }
    }
    private void showDataBarang(){
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            sqlselect = "select * from tb_detail_pembelian a, tb_barang b, tb_kategori c, tb_pembelian d"
                    + " where a.id_barang=b.id_barang and b.id_kategori=c.id_kategori "
                    + " and a.no_faktur_pembelian=d.no_faktur_pembelian "
                    + " and d.no_faktur_pembelian like '"+txtNoFaktur.getText()+"' "
                    + " order by nama_barang asc";
            
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);// mengeksekusi query select
            while(res.next()){
                vtgl_trans = tglview.format(res.getDate("tgl_transaksi"));
                vid_barang = res.getString("id_barang");
                vnm_kategori = res.getString("nama_kategori");
                vnm_barang = res.getString("nama_barang");
                vhrg_beli = res.getDouble("a.harga_beli");
                vjumlah_barang = res.getInt("jumlah_barang");
                vsubtotal = res.getDouble("subtotal");
                Object[]data = {vnm_barang, vnm_kategori, vid_barang, vhrg_beli, vjumlah_barang, vsubtotal};
                tblBarang.addRow(data);
            }
            lblFaktur.setText("DAFTAR PEMBELIAN BARANG NO. FAKTUR : "+txtNoFaktur.getText());
            lblTgl.setText(""+vtgl_trans);
            lblRecordBarang.setText("RECORD : "+tbDataBarang.getRowCount());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method showDataBarang() : "+ex);
        }
    }
    public void tampilDistributor(){
        try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select * from tb_pembelian a, tb_distributor b, tb_detail_pembelian c "
                    + " where a.id_distributor=b.id_distributor and a.no_faktur_pembelian=c.no_faktur_pembelian and "
                    + " a.no_faktur_pembelian='"+vno_faktur+"' "
                    + " order by a.id_distributor asc ";  
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            while(res.next()){
                txtNoFaktur.setText(res.getString("no_faktur_pembelian"));
                txtIdDis.setText(res.getString("id_distributor"));
                txtNmDis.setText(res.getString("nama_distributor"));
                Daftar_Faktur.dispose();
                txtNoFaktur.setEditable(false);
                rb1.requestFocus();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }              
    }   
    public void tampilBarang(){
        try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();

            sqlselect = "select * from tb_barang a, tb_detail_pembelian b where a.id_barang=b.id_barang "
                    + " and a.id_barang='"+vid_barang+"' and no_faktur_pembelian='"+vno_faktur+"' ";  
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);

            while(res.next()){
                txtDaftarBrg.setText(res.getString("id_barang"));
                txtNamaBrg.setText(res.getString("nama_barang"));
                txtHargaBeli.setText(res.getString("b.harga_beli"));
                txtQty.setText(res.getString("jumlah_barang"));
            }
            Daftar_Barang.dispose();
            txtDaftarBrg.requestFocus();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }              
    }
    private void aksiSimpan_ReturBM(){
        vjumlah_barang = 0;
        vno_retur = txtNoRetur.getText();
        vno_faktur = txtNoFaktur.getText();
        vtgl_trans = tglinput.format(dtTglRetur.getDate())+" "+lblJam.getText();
        vid_distributor = txtIdDis.getText();
        if(rb1.isSelected()){
            vstatus_brg = "1";
        }else if(rb2.isSelected()){
            vstatus_brg = "2";
        }else {
            vstatus_brg = "3";
        }
        for (int i = 0; i < tbTransRetur.getRowCount(); i++) {
            vjumlah_barang = vjumlah_barang + Integer.parseInt(tbTransRetur.getValueAt(i, 4).toString());
        } 
        vtotal = Double.valueOf(txtTotal.getText());
        
        if(btnSimpan.getText().equals("SIMPAN")){
            sqlinsert = "insert into tb_retur_pembelian(no_retur_pembelian, no_faktur_pembelian, id_user, tgl_retur, "
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
            aksiSimpanTrans_ReturBM();
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "PERIKSA KEMBALI ! "+ex, "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void aksiSimpanTrans_ReturBM(){
        int jumlah_baris = tbTransRetur.getRowCount();
            try{
                _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
                _Cnn = getCnn.getConnection();// membuka koneksi
                Statement state = _Cnn.createStatement();
                int i=0;
            while(i < jumlah_baris){
                state.executeUpdate("insert into tb_detail_retur_pembelian (no_retur_pembelian, id_barang, jumlah_barang, subtotal) values("
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
        tblTransRetur_BM = (DefaultTableModel) tbTransRetur.getModel();
        for(int i=0;i<vbaris;i++){
            vhrg_beli = Double.parseDouble(tblTransRetur_BM.getValueAt(i, 3).toString());
            vjumlah_barang = Integer.parseInt(tblTransRetur_BM.getValueAt(i, 4).toString());
            vtotal_harga = (int) (vtotal_harga+(vhrg_beli*vjumlah_barang));
        }
        txtDaftarBrg.setText("");
        txtNamaBrg.setText("");
        txtHargaBeli.setText("");
        txtQty.setText("");
        txtJumlah.setValue(0);
        txtSubtotal.setText("");
        txtTotal.setText(String.valueOf(vtotal_harga));
        txtDaftarBrg.requestFocus();
    }
    private void tambahBarang(){
        vno_retur = txtNoRetur.getText();
        vid_barang = txtDaftarBrg.getText();
        vnm_barang = txtNamaBrg.getText();
        vhrg_beli = Double.parseDouble(txtHargaBeli.getText());
        vjumlah_barang = Integer.parseInt(txtJumlah.getValue().toString());
        vqty = Integer.parseInt(txtQty.getText());
        vsubtotal = vhrg_beli*vjumlah_barang;
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
                    state.executeUpdate(" update tb_barang set stok = stok - '"+vjumlah_barang+"' where id_barang = '"+vid_barang+"' ");
                }else {
                    state.executeUpdate(" update tb_barang set stok = stok - '"+vjumlah_barang+"' "
                                + " where id_barang = '"+vid_barang+"' ");
                    state.executeUpdate("insert into tb_detail_retur_pembelian (no_retur_pembelian, id_barang, jumlah_barang, subtotal) values ("
                        + "'"+vno_retur+"',"
                        + "'"+vid_barang+"',"
                        + "'"+vjumlah_barang+"',"
                        + "'"+vsubtotal+"')");
                }   
            }catch(Exception e){
                JOptionPane.showMessageDialog(rootPane, "Gagal Menyimpan! Error : "+e);
            }
        tblTransRetur_BM.addRow(new Object[]{
            vno_retur,vid_barang,vnm_barang,vhrg_beli,vjumlah_barang,vsubtotal
        });
        tbTransRetur.setModel(tblTransRetur_BM);
        lblBaris.setText("RECORD : "+tblTransRetur_BM .getRowCount()); hitungTotal(); txtDaftarBrg.setEditable(true);
        }
    }
    private void hapusBarang(){
        int x = tbTransRetur.getSelectedRow();
        int jumlah_baris = tbTransRetur.getRowCount();
        if (btnSimpan.getText().equals("SIMPAN") || btnUbah.getText().equals("SIMPAN PERUBAHAN")){
            int jawab = JOptionPane.showConfirmDialog(null, 
                "ANDA YAKIN AKAN MEMBATALKAN TRANSAKSI BARANG INI ? \n OTOMATIS AKAN MENAMBAH STOK KODE BARANG = '"+txtDaftarBrg.getText()+"'  !",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if(jawab==JOptionPane.YES_OPTION){
                try{
                    _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
                    _Cnn = getCnn.getConnection();// membuka koneksi
                    Statement state = _Cnn.createStatement();
                    int i=0;
                    if(i < jumlah_baris){
                        state.executeUpdate(" update tb_barang set stok = stok + '"+tbTransRetur.getValueAt(tbTransRetur.getSelectedRow(), 4)+"' "
                                + " where id_barang = '"+tbTransRetur.getValueAt(tbTransRetur.getSelectedRow(), 1)+"' ");
                        state.executeUpdate(" delete from tb_detail_retur_pembelian where id_barang='"+txtDaftarBrg.getText()+"' and "
                                + " no_retur_pembelian='"+txtNoRetur.getText()+"' and "
                                + " no_trans_retur_pembelian = '"+tbTransRetur.getValueAt(tbTransRetur.getSelectedRow(), 6)+"' ");
                        tblTransRetur_BM.removeRow(x);
                        btnHapusBrg.setEnabled(false);
                        hitungTotal();
                        txtDaftarBrg.requestFocus(); txtDaftarBrg.setEditable(true);
                    } lblBaris.setText("RECORD : "+tblTransRetur_BM.getRowCount());
                }catch(Exception e){
                JOptionPane.showMessageDialog(rootPane, "Gagal Menyimpan! Error : "+e);
                }
            }
        }
    }
    private void hitungSubtotal(){
        int vsub = 0;
        vhrg_beli = Double.parseDouble(txtHargaBeli.getText());
        vjumlah_barang = Integer.parseInt(txtJumlah.getValue().toString());
        vsub = (int) (vsub+vhrg_beli*vjumlah_barang);
        txtSubtotal.setText(Integer.toString(vsub));
        txtJumlah.requestFocus();
    }
    private void autoNoRetur(){
        Date sk = new Date();
        SimpleDateFormat format1=new SimpleDateFormat("ddMMyy");
        String time = format1.format(sk);
        try{
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select right(no_retur_pembelian,4) as no_retur_pembelian "
                    + " from tb_retur_pembelian order by no_retur_pembelian desc";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            if (res.next()) {
                String kode = res.getString("no_retur_pembelian");
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
            sqlselect = "select * from tb_retur_pembelian a, tb_pembelian b, tb_distributor d "
                    + "where a.no_faktur_pembelian=b.no_faktur_pembelian and "
                    + " b.id_distributor=d.id_distributor and a.no_retur_pembelian='"+vno_retur+"' "; 
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            while(res.next()){
                txtNoReturNew.setText(res.getString("no_retur_pembelian"));
                txtDistributorNew.setText(res.getString("nama_distributor"));
                txtJumlahNew.setText(res.getString("jumlah_retur"));
                txtStatusBrg.setText(res.getString("status_retur"));
                txtTotalNew.setText(res.getString("total"));
                txtNoRetur.setText(res.getString("no_retur_pembelian"));
                txtNmDis.setText(res.getString("nama_distributor"));
                txtIdDis.setText(res.getString("id_distributor"));
                txtNoFaktur.setText(res.getString("no_faktur_pembelian"));
                txtTotal.setText(res.getString("total"));
                if(txtStatusBrg.getText().equals("1")){
                    lblKeterangan.setText("BARANG INI ADA DI TOKO DALAM KEADAAN RUSAK !");
                    rb1new.setEnabled(false); rb2new.setEnabled(true); rb3new.setEnabled(true); 
                    btnSimpanNew.setVisible(true); rb2new.requestFocus();
                }else if(txtStatusBrg.getText().equals("2")){
                    lblKeterangan.setText("BARANG INI ADA DI DISTRIBUTOR !");
                    rb1new.setEnabled(false); rb2new.setEnabled(false); rb3new.setEnabled(true); 
                    btnSimpanNew.setVisible(true); rb3new.requestFocus();
                }else if(txtStatusBrg.getText().equals("3")){
                    lblKeterangan.setText("BARANG INI ADA DI TOKO DALAM KEADAAN BAIK !");
                    rb1new.setEnabled(false); rb2new.setEnabled(false); rb3new.setEnabled(false); btnSimpanNew.setVisible(false);
                }
            }
            txtJumlah.requestFocus();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "PERIKSA KEMBALI ! ");
        }              
    }
    private void getData(){
        vno_retur = txtNoReturNew.getText();
        try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select * from tb_retur_pembelian a, tb_detail_retur_pembelian b, tb_distributor c, tb_barang d, tb_pembelian e "
                    + " where e.id_distributor=c.id_distributor and a.no_retur_pembelian=b.no_retur_pembelian and "
                    + " b.id_barang=d.id_barang and a.no_faktur_pembelian=e.no_faktur_pembelian and "
                    + " b.no_retur_pembelian='"+vno_retur+"' ";
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);
            while(res.next()){
                lblNoFak.setText(res.getString("no_retur_pembelian")+"--"+res.getString("nama_distributor"));
                txtNoFaktur.setText(res.getString("no_faktur_pembelian"));
                txtIdDis.setText(res.getString("id_distributor"));
                txtNmDis.setText(res.getString("nama_distributor"));
                txtTotal.setText(res.getString("total"));
                vstatus_brg = res.getString("status_retur");
                if(vstatus_brg.equals("1")){
                    rb1.setSelected(true);
                }else if(vstatus_brg.equals("2")){
                    rb2.setSelected(true);
                }else{
                    rb3.setSelected(true);
                }
                dtTglRetur.setDate(new Date());
                
                vno_transaksi = res.getString("no_trans_retur_pembelian");
                vno_retur = res.getString("no_retur_pembelian");
                vid_barang = res.getString("id_barang");
                vnm_barang = res.getString("nama_barang");
                vhrg_beli = res.getDouble("harga_beli");
                vjumlah_barang = res.getInt("jumlah_barang");
                vsubtotal = res.getDouble("subtotal");
                Object[]data = {vno_retur, vid_barang, vnm_barang, vhrg_beli, vjumlah_barang, vsubtotal, vno_transaksi};
                tblTransRetur_BM.addRow(data);
                lblBaris.setText("RECORD : "+tblTransRetur_BM.getRowCount());
                btnSimpan.setVisible(false); btnSimpan.setText(""); btnUbah.setVisible(true);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "PERIKSA KEMBALI ! ");
        }              
    }
    private void aksiUpdateStok(){
        int jumlah_baris = tblDetail_returBM.getRowCount();
            try{
                _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
                _Cnn = getCnn.getConnection();// membuka koneksi
                Statement state = _Cnn.createStatement();
                int i=0;
            while(i < jumlah_baris){
                state.executeUpdate("update tb_barang set stok = stok + '"+(int)tblDetail_returBM.getValueAt(i, 4)+"' "
                        + " where id_barang = '"+tblDetail_returBM.getValueAt(i, 1)+"' ");
                i++;
            }
            }catch(Exception e){
            JOptionPane.showMessageDialog(null, "PERIKSA KEMBALI ! ");
            }
    }
    private void aksiUbahStatus(){
        try {
            if(rb1new.isSelected()){
                vstatus_brg = "1";
                sqlinsert = "update tb_retur_pembelian set status_retur='"+vstatus_brg+"' where "
                        + " no_retur_pembelian='"+txtNoReturNew.getText()+"'";
                panelData.setVisible(true); setTabel_Retur_BM(); showData_ReturBM();
            }else if(rb2new.isSelected()){
                vstatus_brg = "2";
                sqlinsert = "update tb_retur_pembelian set status_retur='"+vstatus_brg+"' where "
                        + " no_retur_pembelian='"+txtNoReturNew.getText()+"'";
                panelData.setVisible(true); setTabel_Retur_BM(); showData_ReturBM();
            }else {
                vstatus_brg = "3";
                sqlinsert = "update tb_retur_pembelian set status_retur='"+vstatus_brg+"' where "
                        + " no_retur_pembelian='"+txtNoReturNew.getText()+"'";
                aksiUpdateStok(); panelData.setVisible(true); setTabel_Retur_BM(); showData_ReturBM();
            }
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            Statement state = _Cnn.createStatement();
            state.executeUpdate(sqlinsert);
            JOptionPane.showMessageDialog(null, "STATUS BERHASIL DIUBAH ! ",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            showData_ReturBM(); clearForm(); disableForm();
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "PERIKSA KEMBALI ! ", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void aksiUbahRetur(){
        vjumlah_barang = 0;
        vno_retur = txtNoRetur.getText();
        vno_faktur = txtNoFaktur.getText();
        vtgl_trans = tglinput.format(dtTglRetur.getDate())+" "+lblJam.getText();  
        vid_distributor = txtIdDis.getText();
        if(rb1.isSelected()){
            vstatus_brg = "1";
        }else if(rb2.isSelected()){
            vstatus_brg = "2";
        }else {
            vstatus_brg = "3";
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
                    sqlinsert = "update tb_retur_pembelian set no_faktur_pembelian='"+vno_faktur+"', id_user='"+id+"', "
                            + " tgl_retur='"+vtgl_trans+"', jumlah_retur='"+vjumlah_barang+"', "
                            + " total='"+vtotal+"', status_retur='"+vstatus_brg+"' where no_retur_pembelian='"+vno_retur+"' ";
                    state.executeUpdate(sqlinsert);
                JOptionPane.showMessageDialog(null, "DATA BERHASIL DIUBAH ! ",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "PERIKSA KEMBALI ! ");
        }
    }
    private void aksiReset(){
        int jumlah_baris = tbTransRetur.getRowCount();
        if(jumlah_baris == 0){
        }else{
            try{
                _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
                _Cnn = getCnn.getConnection();// membuka koneksi
                Statement state = _Cnn.createStatement();
                int i=0;
            while(i < jumlah_baris){
                state.executeUpdate("update tb_barang set stok = stok - '"+tbTransRetur.getValueAt(i, 4)+"' "
                        + " where id_barang='"+tbTransRetur.getValueAt(i, 1)+"' ");
                i++;
            }
            clearForm(); enableForm();
            }catch(Exception e){
            JOptionPane.showMessageDialog(null, "PERIKSA KEMBALI ! ");
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
                txtDir.setText(""); BackUp_Restore.dispose(); setTabel_Retur_BM();showData_ReturBM();
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

        IfrReturPembelian = new javax.swing.JFrame();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbTransRetur = new javax.swing.JTable();
        lblBaris = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        dtTglRetur = new com.toedter.calendar.JDateChooser();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        txtNoRetur = new Tool.Custom_TextField();
        jPanel10 = new javax.swing.JPanel();
        txtNoFaktur = new Tool.Custom_TextField();
        btnDaftarFaktur = new javax.swing.JButton();
        txtIdDis = new Tool.Custom_TextField();
        txtNmDis = new Tool.Custom_TextField();
        jPanel11 = new javax.swing.JPanel();
        rb1 = new javax.swing.JRadioButton();
        rb2 = new javax.swing.JRadioButton();
        rb3 = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtTotal = new Tool.NumberTextField();
        btnSimpan = new javax.swing.JButton();
        btnReturPembelian = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnTambahBrg = new javax.swing.JButton();
        btnHapusBrg = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtDaftarBrg = new Tool.Custom_TextField();
        btnDaftarBrg = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtNamaBrg = new Tool.Custom_TextField();
        jLabel5 = new javax.swing.JLabel();
        txtHargaBeli = new Tool.NumberTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtSubtotal = new Tool.NumberTextField();
        jLabel13 = new javax.swing.JLabel();
        txtQty = new Tool.NumberTextField();
        txtJumlah = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
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
        jPanel17 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        lblFaktur = new javax.swing.JLabel();
        lblTgl = new javax.swing.JLabel();
        Daftar_Faktur = new javax.swing.JDialog();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbDataPembelian = new javax.swing.JTable();
        lblRecordPembelian = new javax.swing.JLabel();
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
        jLabel15 = new javax.swing.JLabel();
        btnBackUp = new javax.swing.JButton();
        cmbDis = new javax.swing.JComboBox<>();
        dtTglCariAwal = new com.toedter.calendar.JDateChooser();
        jLabel17 = new javax.swing.JLabel();
        dtTglCariAkhir = new com.toedter.calendar.JDateChooser();
        jLabel18 = new javax.swing.JLabel();
        btnCariTgl = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        panelDetail = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbDetRetur_BM = new javax.swing.JTable();
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
        txtDistributorNew = new Tool.Custom_TextField();
        jLabel25 = new javax.swing.JLabel();
        txtStatusBrg = new Tool.NumberTextField();
        jPanel12 = new javax.swing.JPanel();
        rb1new = new javax.swing.JRadioButton();
        rb2new = new javax.swing.JRadioButton();
        rb3new = new javax.swing.JRadioButton();
        btnSimpanNew = new javax.swing.JButton();
        lblUserSistem = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        btnEdit = new javax.swing.JButton();
        btnKembali = new javax.swing.JButton();

        IfrReturPembelian.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        IfrReturPembelian.setTitle("RETUR PEMBELIAN - SILUB");

        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));

        jScrollPane2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 3, true));

        tbTransRetur.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));
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

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true), "KETERANGAN : "));

        jLabel10.setFont(new java.awt.Font("Agency FB", 1, 16)); // NOI18N
        jLabel10.setForeground(java.awt.Color.red);
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("1 : *BARANG MASIH DI TOKO DALAM KEADAAN RUSAK !");
        jLabel10.setOpaque(true);

        jLabel11.setFont(new java.awt.Font("Agency FB", 1, 16)); // NOI18N
        jLabel11.setForeground(java.awt.Color.red);
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("2 : *BARANG SUDAH DIKIRIM KEPADA DISTRIBUTOR  ! ");
        jLabel11.setOpaque(true);

        jLabel14.setFont(new java.awt.Font("Agency FB", 1, 16)); // NOI18N
        jLabel14.setForeground(java.awt.Color.red);
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("3 : *BARANG SUDAH DI TOKO DALAM KEADAAN BAIK  !");
        jLabel14.setOpaque(true);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addContainerGap(46, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true)));

        dtTglRetur.setEnabled(false);

        jLabel42.setText("NO RETUR : ");

        jLabel43.setText("TANGGAL  : ");

        txtNoRetur.setEditable(false);
        txtNoRetur.setBackground(new java.awt.Color(0, 0, 0));
        txtNoRetur.setForeground(java.awt.Color.orange);
        txtNoRetur.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNoRetur.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        txtNoRetur.setPlaceholder("");
        txtNoRetur.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoReturKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel42)
                    .addComponent(jLabel43))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dtTglRetur, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNoRetur, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNoRetur, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dtTglRetur, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel43)))
                .addContainerGap())
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true), "KLIK BUTTON --> MENCARI NO. FAKTUR"));

        txtNoFaktur.setBackground(new java.awt.Color(240, 240, 240));
        txtNoFaktur.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNoFaktur.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        txtNoFaktur.setPlaceholder("MASUKAN NO. FAKTUR");
        txtNoFaktur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoFakturActionPerformed(evt);
            }
        });
        txtNoFaktur.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoFakturKeyTyped(evt);
            }
        });

        btnDaftarFaktur.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/search.png"))); // NOI18N
        btnDaftarFaktur.setText("CARI");
        btnDaftarFaktur.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDaftarFaktur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDaftarFakturActionPerformed(evt);
            }
        });

        txtIdDis.setBackground(new java.awt.Color(240, 240, 240));
        txtIdDis.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtIdDis.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        txtIdDis.setPlaceholder("KODE");
        txtIdDis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIdDisKeyTyped(evt);
            }
        });

        txtNmDis.setBackground(new java.awt.Color(240, 240, 240));
        txtNmDis.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNmDis.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        txtNmDis.setPlaceholder("NAMA DISTRBUTOR");
        txtNmDis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNmDisKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(txtNoFaktur, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(btnDaftarFaktur))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(txtIdDis, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(txtNmDis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(10, 10, 10))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNoFaktur, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDaftarFaktur, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIdDis, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNmDis, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(rb1)
                .addGap(18, 18, 18)
                .addComponent(rb2)
                .addGap(18, 18, 18)
                .addComponent(rb3)
                .addGap(100, 100, 100))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rb1)
                    .addComponent(rb2)
                    .addComponent(rb3))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jLabel4.setFont(new java.awt.Font("Agency FB", 3, 16)); // NOI18N
        jLabel4.setForeground(java.awt.Color.red);
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("* TELITI SEBELUM MENYIMPAN  !!");
        jLabel4.setOpaque(true);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, Short.MAX_VALUE)
                .addComponent(jLabel4))
        );

        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jLabel8.setFont(new java.awt.Font("Arial Narrow", 1, 30)); // NOI18N
        jLabel8.setText("TOTAL   : Rp. ");

        txtTotal.setEditable(false);
        txtTotal.setBackground(new java.awt.Color(0, 0, 0));
        txtTotal.setForeground(new java.awt.Color(255, 0, 0));
        txtTotal.setFont(new java.awt.Font("Arial Narrow", 1, 30)); // NOI18N

        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/save_add.png"))); // NOI18N
        btnSimpan.setText("SIMPAN");
        btnSimpan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSimpan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSimpan.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        btnReturPembelian.setBackground(java.awt.Color.green);
        btnReturPembelian.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReturPembelian.setText("LIHAT DATA RETUR PEMBELIAN");
        btnReturPembelian.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReturPembelian.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReturPembelian.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReturPembelian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReturPembelianActionPerformed(evt);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                .addComponent(btnReturPembelian)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReturPembelian, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUbah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBatal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnTambahBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHapusBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambahBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHapusBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true), "(+) DETAIL"));

        jLabel9.setText(" KODE BARANG");

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
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDaftarBrgKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDaftarBrgKeyTyped(evt);
            }
        });

        btnDaftarBrg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/search.png"))); // NOI18N
        btnDaftarBrg.setText("CARI");
        btnDaftarBrg.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDaftarBrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDaftarBrgActionPerformed(evt);
            }
        });

        jLabel6.setText("NAMA BARANG");

        txtNamaBrg.setEditable(false);
        txtNamaBrg.setBackground(new java.awt.Color(0, 204, 204));
        txtNamaBrg.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNamaBrg.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        txtNamaBrg.setPlaceholder("NAMA BARANG");

        jLabel5.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel5.setForeground(java.awt.Color.red);
        jLabel5.setText("* HARGA BELI ");

        txtHargaBeli.setEditable(false);
        txtHargaBeli.setBackground(new java.awt.Color(0, 204, 204));
        txtHargaBeli.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("STOK");

        jLabel16.setText("SUBTOTAL");

        txtSubtotal.setEditable(false);
        txtSubtotal.setBackground(new java.awt.Color(0, 204, 204));
        txtSubtotal.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("->");

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

        jLabel7.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel7.setForeground(java.awt.Color.red);
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("* JUMLAH");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(txtDaftarBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnDaftarBrg))
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 165, Short.MAX_VALUE))
                    .addComponent(txtNamaBrg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(73, 73, 73))
                    .addComponent(txtHargaBeli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtQty, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtJumlah)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(txtJumlah))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(40, 40, 40))
                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel13Layout.createSequentialGroup()
                                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel9))
                                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txtDaftarBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtNamaBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(txtHargaBeli, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnDaftarBrg, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel13Layout.createSequentialGroup()
                                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGap(0, 0, 0)
                                    .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2)))
                        .addGap(10, 10, 10))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblBaris))
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblBaris)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        jLabel36.setText("FORM RETUR PEMBELIAN");
        jLabel36.setOpaque(true);

        jLabel3.setFont(new java.awt.Font("Agency FB", 1, 16)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("FORM INI DIGUNAKAN UNTUK MEMASUKAN DATA RETUR PEMBELIAN DAN OTOMATIS MENGURANGI JUMLAH STOK BARANG !");
        jLabel3.setOpaque(true);

        lblJam.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        lblJam.setForeground(java.awt.Color.red);
        lblJam.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJam.setText("--");
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 535, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addGap(541, 541, 541)
                            .addComponent(lblJam, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel22)
                                        .addComponent(jLabel21)
                                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel29)
                                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel36))
                                    .addComponent(lblJam, javax.swing.GroupLayout.Alignment.TRAILING))))
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel26)
                                    .addComponent(jLabel27)
                                    .addComponent(jLabel28)
                                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel31)))
                            .addComponent(jLabel33)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)))))
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout IfrReturPembelianLayout = new javax.swing.GroupLayout(IfrReturPembelian.getContentPane());
        IfrReturPembelian.getContentPane().setLayout(IfrReturPembelianLayout);
        IfrReturPembelianLayout.setHorizontalGroup(
            IfrReturPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IfrReturPembelianLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1))
        );
        IfrReturPembelianLayout.setVerticalGroup(
            IfrReturPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IfrReturPembelianLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 93, Short.MAX_VALUE))
        );

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

        jPanel17.setBackground(new java.awt.Color(0, 204, 204));
        jPanel17.setPreferredSize(new java.awt.Dimension(786, 86));

        jLabel39.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        jLabel39.setForeground(java.awt.Color.red);
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("LUBADA JAYA");

        jLabel40.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        jLabel40.setForeground(java.awt.Color.blue);
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setText("MARKET");

        lblFaktur.setFont(new java.awt.Font("Arial Narrow", 1, 24)); // NOI18N
        lblFaktur.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblTgl.setFont(new java.awt.Font("Arial Narrow", 1, 24)); // NOI18N
        lblTgl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFaktur, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTgl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel39)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblFaktur)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel40)
                    .addComponent(lblTgl))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout Daftar_BarangLayout = new javax.swing.GroupLayout(Daftar_Barang.getContentPane());
        Daftar_Barang.getContentPane().setLayout(Daftar_BarangLayout);
        Daftar_BarangLayout.setHorizontalGroup(
            Daftar_BarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 786, Short.MAX_VALUE)
            .addComponent(lblRecordBarang)
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        Daftar_BarangLayout.setVerticalGroup(
            Daftar_BarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Daftar_BarangLayout.createSequentialGroup()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblRecordBarang)
                .addContainerGap())
        );

        jScrollPane4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white));

        tbDataPembelian.setModel(new javax.swing.table.DefaultTableModel(
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
        tbDataPembelian.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDataPembelianMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tbDataPembelian);

        lblRecordPembelian.setFont(new java.awt.Font("Arial Narrow", 1, 14)); // NOI18N
        lblRecordPembelian.setText("jLabel1");

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

        jLabel38.setBackground(new java.awt.Color(0, 204, 204));
        jLabel38.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("DATA PEMBELIAN BARANG");
        jLabel38.setOpaque(true);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel37)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout Daftar_FakturLayout = new javax.swing.GroupLayout(Daftar_Faktur.getContentPane());
        Daftar_Faktur.getContentPane().setLayout(Daftar_FakturLayout);
        Daftar_FakturLayout.setHorizontalGroup(
            Daftar_FakturLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Daftar_FakturLayout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(Daftar_FakturLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Daftar_FakturLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Daftar_FakturLayout.createSequentialGroup()
                        .addComponent(lblRecordPembelian)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane4))
                .addContainerGap())
        );
        Daftar_FakturLayout.setVerticalGroup(
            Daftar_FakturLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Daftar_FakturLayout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblRecordPembelian))
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
        setTitle("RETUR PEMBELIAN - SILUB");
        setPreferredSize(new java.awt.Dimension(1205, 585));

        panelData.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "KLIK 2X --> MENGUPDATE STATUS RETUR PEMBELIAN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

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

        lblRecordRetur.setFont(new java.awt.Font("Arial Narrow", 1, 14)); // NOI18N
        lblRecordRetur.setText("RECORD : 0");

        txtCari.setPlaceholder("NO.FAKTUR PEMBELIAN");
        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCariKeyTyped(evt);
            }
        });

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

        lblNoFak.setFont(new java.awt.Font("Agency FB", 3, 24)); // NOI18N
        lblNoFak.setForeground(java.awt.Color.red);
        lblNoFak.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel15.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel15.setText("CARI :");

        btnBackUp.setFont(new java.awt.Font("Arial Narrow", 1, 14)); // NOI18N
        btnBackUp.setText("EXPORT TO EXCEL");
        btnBackUp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBackUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackUpActionPerformed(evt);
            }
        });

        cmbDis.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        cmbDis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- PILIH DISTRIBUTOR --" }));
        cmbDis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDisActionPerformed(evt);
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

        jLabel18.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel18.setText("DISTRIBUTOR : ");

        btnCariTgl.setFont(new java.awt.Font("Arial Narrow", 1, 14)); // NOI18N
        btnCariTgl.setText("CARI");
        btnCariTgl.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCariTgl.setOpaque(false);
        btnCariTgl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariTglActionPerformed(evt);
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

        javax.swing.GroupLayout panelDataLayout = new javax.swing.GroupLayout(panelData);
        panelData.setLayout(panelDataLayout);
        panelDataLayout.setHorizontalGroup(
            panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1180, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDataLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblRecordRetur)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dtTglCariAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dtTglCariAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCariTgl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNoFak, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTambah))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDataLayout.createSequentialGroup()
                .addComponent(btnBackUp, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRefresh)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbDis, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelDataLayout.setVerticalGroup(
            panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDataLayout.createSequentialGroup()
                .addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCari, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbDis, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBackUp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                .addGap(1, 1, 1)
                .addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(dtTglCariAkhir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblRecordRetur, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dtTglCariAwal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCariTgl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblNoFak, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        panelDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "(+) DETAIL RETUR PEMBELIAN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 12))); // NOI18N
        panelDetail.setPreferredSize(new java.awt.Dimension(1184, 560));

        jScrollPane5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white), "(+) DETAIL"));

        tbDetRetur_BM.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane5.setViewportView(tbDetRetur_BM);

        lblRecordDetRetur.setFont(new java.awt.Font("Arial Narrow", 1, 12)); // NOI18N
        lblRecordDetRetur.setText("RECORD : 0");

        panelUbah.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(255, 255, 255), new java.awt.Color(255, 255, 255), new java.awt.Color(255, 255, 255), new java.awt.Color(255, 255, 255)), "(+) FORM UPDATE STATUS"));

        jLabel19.setText("TOTAL Rp. ");

        jLabel20.setText("DISTRIBUTOR");

        txtJumlahNew.setEditable(false);
        txtJumlahNew.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtJumlahNew.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        txtJumlahNew.setOpaque(false);

        txtTotalNew.setEditable(false);
        txtTotalNew.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        txtTotalNew.setOpaque(false);

        jPanel9.setBackground(new java.awt.Color(0, 0, 0));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true), "Ket : "));
        jPanel9.setOpaque(false);

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
        txtNoReturNew.setText("NO. RETUR PEMBELIAN");
        txtNoReturNew.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        txtNoReturNew.setOpaque(false);

        jLabel23.setText("JUMLAH RETUR -->> STATUS");

        txtDistributorNew.setEditable(false);
        txtDistributorNew.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDistributorNew.setText("DISTRIBUTOR");
        txtDistributorNew.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        txtDistributorNew.setOpaque(false);

        jLabel25.setText("-->>");

        txtStatusBrg.setEditable(false);
        txtStatusBrg.setForeground(new java.awt.Color(255, 0, 0));
        txtStatusBrg.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtStatusBrg.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        txtStatusBrg.setOpaque(false);

        jPanel12.setBackground(new java.awt.Color(0, 0, 0));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true), "PILIH STATUS BARU : ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel12.setOpaque(false);

        rb1new.setBackground(new java.awt.Color(0, 204, 204));
        buttonGroup2.add(rb1new);
        rb1new.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rb1new.setText("1");
        rb1new.setOpaque(false);

        rb2new.setBackground(new java.awt.Color(0, 204, 204));
        buttonGroup2.add(rb2new);
        rb2new.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rb2new.setText("2");
        rb2new.setOpaque(false);

        rb3new.setBackground(new java.awt.Color(0, 204, 204));
        buttonGroup2.add(rb3new);
        rb3new.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rb3new.setText("3");
        rb3new.setOpaque(false);

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
                .addGap(92, 92, 92))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rb1new)
                    .addComponent(rb2new)
                    .addComponent(rb3new))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnSimpanNew.setText("SIMPAN");
        btnSimpanNew.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSimpanNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanNewActionPerformed(evt);
            }
        });

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
                            .addComponent(jLabel20)
                            .addComponent(jLabel23)
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelUbahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelUbahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtDistributorNew, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                                .addComponent(txtTotalNew, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelUbahLayout.createSequentialGroup()
                                .addComponent(txtJumlahNew, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel25)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtStatusBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelUbahLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblUser)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblUserSistem)
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
                    .addComponent(txtDistributorNew, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addGroup(panelUbahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSimpanNew, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelUbahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblUserSistem)
                        .addComponent(lblUser)))
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
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 811, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        panelDetailLayout.setVerticalGroup(
            panelDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDetailLayout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblRecordDetRetur, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnKembali, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
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
                .addGap(0, 0, Short.MAX_VALUE))
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
            vno_faktur = txtNoFaktur.getText();
            vid_barang = tbDataBarang.getValueAt(tbDataBarang.getSelectedRow(), 2).toString();
            tampilBarang();
            vjumlah_barang = (int) tbDataBarang.getValueAt(tbDataBarang.getSelectedRow(), 4);
            txtQty.setText(String.valueOf(vjumlah_barang));
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
            setTabel_DetRetur_BM();
            showDetail_ReturBM();
            getDataRetur();
        }else{
            vno_retur = tbDataRetur.getValueAt(tbDataRetur.getSelectedRow(), 0).toString();
            vnm_distributor = tbDataRetur.getValueAt(tbDataRetur.getSelectedRow(), 4).toString();
            lblNoFak.setText(vno_retur+" - "+vnm_distributor);
        }
    }//GEN-LAST:event_tbDataReturMouseClicked

    private void txtCariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyTyped
        cariData_ReturBM();
    }//GEN-LAST:event_txtCariKeyTyped

    private void tbDataPembelianMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDataPembelianMouseClicked
        if(evt.getClickCount()==1){
            vno_faktur = tbDataPembelian.getValueAt(tbDataPembelian.getSelectedRow(), 0).toString();
            tampilDistributor();
       } 
    }//GEN-LAST:event_tbDataPembelianMouseClicked

    private void txtNoFakturActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoFakturActionPerformed
        vid_distributor=txtNoFaktur.getText();
        tampilDistributor();
    }//GEN-LAST:event_txtNoFakturActionPerformed

    private void btnDaftarFakturActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDaftarFakturActionPerformed
        FrRetur_Penjualan ret = new FrRetur_Penjualan();
        ret.formTengah();
        Daftar_Faktur.setVisible(true);
        Daftar_Faktur.setSize(918, 580);
        Daftar_Faktur.setLocationRelativeTo(this);
        setTabel_Pembelian();
        showDataPembelian();
    }//GEN-LAST:event_btnDaftarFakturActionPerformed

    private void btnDaftarBrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDaftarBrgActionPerformed
        Daftar_Barang.setVisible(true);
        Daftar_Barang.setSize(918, 520);
        Daftar_Barang.setLocationRelativeTo(this);
        setTabelBarang();
        showDataBarang();
    }//GEN-LAST:event_btnDaftarBrgActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if( txtNoFaktur.getText().length() > 0 && txtNmDis.getText().length() == 0 ){
            JOptionPane.showMessageDialog(null, "DISTRIBUTOR TIDAK DITEMUKAN !",
                "Error", JOptionPane.ERROR_MESSAGE); txtNoFaktur.requestFocus();
        }else if( txtNmDis.getText().length() > 0 && txtNoFaktur.getText().length() == 0 ){
            JOptionPane.showMessageDialog(null, "DISTRIBUTOR TIDAK DITEMUKAN !",
                "Error", JOptionPane.ERROR_MESSAGE); txtNoFaktur.requestFocus();
        }else if(txtNmDis.getText().equals("")){
            JOptionPane.showMessageDialog(null, "TIDAK ADA BARANG YANG DIRETUR !",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }else if(buttonGroup1.isSelected(null)){
                JOptionPane.showMessageDialog(null, "STATUS RETUR BELUM DI PILIH !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtTotal.getText().equals("0")){
            JOptionPane.showMessageDialog(null, "TIDAK ADA BARANG YANG DIRETUR !",
                    "Error", JOptionPane.ERROR_MESSAGE);
            txtDaftarBrg.requestFocus();
        }else{
            aksiSimpan_ReturBM();
            tblTransRetur_BM.getDataVector().removeAllElements();
            tblTransRetur_BM.fireTableDataChanged();
        }  
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnReturPembelianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReturPembelianActionPerformed
        lblBaris.setText("");
        int jumlah_baris = tblTransRetur_BM.getRowCount();
        if(jumlah_baris > 0 && btnSimpan.getText().equals("SIMPAN")){
            int jawab = JOptionPane.showConfirmDialog(null,
                "ANDA YAKIN INGIN MEMBATALKAN TRANSAKSI "+txtNoRetur.getText()+" INI ?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if(jawab==JOptionPane.YES_OPTION){
                tblTransRetur_BM.getDataVector().removeAllElements();
                tblTransRetur_BM.fireTableDataChanged();
                clearForm(); enableForm(); aksiReset(); btnSimpan.setVisible(true);
                btnSimpan.setText("SIMPAN"); btnUbah.setVisible(false);
                autoNoRetur();
                txtDaftarBrg.setEditable(true); btnDaftarBrg.setEnabled(true);
                txtJumlah.setEnabled(true); 
            }
        }else if(jumlah_baris > 0 && btnSimpan.getText().equals("")){
            int jawab = JOptionPane.showConfirmDialog(null,
                "SIMPAN TRANSAKSI "+txtNoRetur.getText()+" ?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if(jawab==JOptionPane.YES_OPTION){
                aksiUbahRetur(); clearForm(); enableForm(); IfrReturPembelian.dispose(); 
                panelData.setVisible(true);panelDetail.setVisible(false);
                setTabel_Retur_BM(); showData_ReturBM();
                btnSimpan.setVisible(true); btnSimpan.setText("SIMPAN"); btnUbah.setVisible(false);
                btnDaftarBrg.setEnabled(true);
            }else {
                tblTransRetur_BM.getDataVector().removeAllElements();
                tblTransRetur_BM.fireTableDataChanged();
                clearForm(); enableForm(); btnSimpan.setVisible(true);
                btnSimpan.setText("SIMPAN"); btnUbah.setVisible(false);
                autoNoRetur();
                txtDaftarBrg.setEditable(true); btnDaftarBrg.setEnabled(true);
                txtJumlah.setEnabled(true);
            }
        }else{
            clearForm(); enableForm(); IfrReturPembelian.dispose(); panelData.setVisible(true);panelDetail.setVisible(false);
             setTabel_Retur_BM(); showData_ReturBM();
            btnSimpan.setVisible(true); btnSimpan.setText("SIMPAN");
            
        }
    }//GEN-LAST:event_btnReturPembelianActionPerformed

    private void txtDaftarBrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDaftarBrgActionPerformed
        vid_barang=txtDaftarBrg.getText();
        tampilBarang();
    }//GEN-LAST:event_txtDaftarBrgActionPerformed

    private void txtCariNoFakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariNoFakActionPerformed
        showDataPembelian();
    }//GEN-LAST:event_txtCariNoFakActionPerformed

    private void txtCariNoFakKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariNoFakKeyTyped
        showDataPembelian();
    }//GEN-LAST:event_txtCariNoFakKeyTyped

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        IfrReturPembelian.setVisible(true);
        IfrReturPembelian.setExtendedState(JFrame.MAXIMIZED_BOTH);
        autoNoRetur();
        clearForm();
        enableForm();
        setTabel_TransRetur_BM();
        btnSimpan.setVisible(true); btnSimpan.setText("SIMPAN"); btnUbah.setVisible(false);
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        if( txtNoFaktur.getText().length() > 0 && txtNmDis.getText().length() == 0 ){
            JOptionPane.showMessageDialog(null, "DISTRIBUTOR TIDAK DITEMUKAN !",
                "Error", JOptionPane.ERROR_MESSAGE); txtNoFaktur.requestFocus();
        }else if( txtNmDis.getText().length() > 0 && txtNoFaktur.getText().length() == 0 ){
            JOptionPane.showMessageDialog(null, "DISTRIBUTOR TIDAK DITEMUKAN !",
                "Error", JOptionPane.ERROR_MESSAGE); txtNoFaktur.requestFocus();
        }else if(txtNmDis.getText().equals("")){
            JOptionPane.showMessageDialog(null, "TIDAK ADA BARANG YANG DIRETUR !",
                    "Error", JOptionPane.ERROR_MESSAGE); txtNoFaktur.requestFocus();
        }else if(buttonGroup1.isSelected(null)){
                JOptionPane.showMessageDialog(null, "STATUS RETUR BELUM DI PILIH !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE); rb1.requestFocus();
        }else{
            aksiUbahRetur();
            clearForm(); enableForm(); IfrReturPembelian.dispose(); panelData.setVisible(true);panelDetail.setVisible(false);
            setTabel_Retur_BM(); showData_ReturBM();
        } 
        
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        lblBaris.setText("");
        int jumlah_baris = tblTransRetur_BM.getRowCount();
        if(jumlah_baris > 0 && btnSimpan.getText().equals("SIMPAN")){
            int jawab = JOptionPane.showConfirmDialog(null,
                "ANDA YAKIN INGIN MEMBATALKAN TRANSAKSI "+txtNoRetur.getText()+" INI ?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if(jawab==JOptionPane.YES_OPTION){
                clearForm(); enableForm(); aksiReset(); btnSimpan.setVisible(true);
                btnSimpan.setText("SIMPAN"); btnUbah.setVisible(false);
                tblTransRetur_BM.getDataVector().removeAllElements();
                tblTransRetur_BM.fireTableDataChanged(); autoNoRetur();
                txtDaftarBrg.setEditable(true); txtNoFaktur.setEditable(true);
                txtJumlah.setEnabled(true); 
            }
        }else if(jumlah_baris > 0 && btnSimpan.getText().equals("")){
            int jawab = JOptionPane.showConfirmDialog(null,
                "SIMPAN TRANSAKSI ?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if(jawab==JOptionPane.YES_OPTION){
                aksiUbahRetur(); clearForm(); enableForm(); IfrReturPembelian.dispose(); panelData.setVisible(true);panelDetail.setVisible(false);
                setTabel_Retur_BM(); showData_ReturBM();
                btnSimpan.setVisible(true); btnSimpan.setText("SIMPAN");
            }else if(jawab==JOptionPane.NO_OPTION){
                clearForm(); enableForm(); btnSimpan.setVisible(true);
                btnSimpan.setText("SIMPAN"); btnUbah.setVisible(false);
                tblTransRetur_BM.getDataVector().removeAllElements();
                tblTransRetur_BM.fireTableDataChanged(); autoNoRetur();
                txtDaftarBrg.setEditable(true); txtNoFaktur.setEditable(true);
                txtJumlah.setEnabled(true);setTabel_Retur_BM(); showData_ReturBM();
            }
        }else{
            clearForm(); enableForm(); IfrReturPembelian.dispose(); panelData.setVisible(true);panelDetail.setVisible(false);
            setTabel_Retur_BM(); showData_ReturBM();
            btnSimpan.setVisible(true); btnSimpan.setText("SIMPAN");
        }
    }//GEN-LAST:event_btnBatalActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        setTabel_TransRetur_BM();
        getData();
        IfrReturPembelian.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        IfrReturPembelian.setVisible(true);
        
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKembaliActionPerformed
        panelDetail.setVisible(false);
        panelData.setVisible(true); showData_ReturBM();
    }//GEN-LAST:event_btnKembaliActionPerformed

    private void txtNoFakturKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoFakturKeyTyped
        if ( txtNoFaktur.getText().length() == 15 ) {
        evt.consume();
        }
    }//GEN-LAST:event_txtNoFakturKeyTyped

    private void txtDaftarBrgKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDaftarBrgKeyTyped
        if ( txtDaftarBrg.getText().length() == 13 ) {
        evt.consume();
        }
    }//GEN-LAST:event_txtDaftarBrgKeyTyped

    private void btnSimpanNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanNewActionPerformed
        if(buttonGroup2.isSelected(null)){
                JOptionPane.showMessageDialog(null, "STATUS BARU BELUM DI PILIH !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }else {
            aksiUbahStatus();
        }
    }//GEN-LAST:event_btnSimpanNewActionPerformed

    private void btnTambahBrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahBrgActionPerformed
        tambahBarang();
    }//GEN-LAST:event_btnTambahBrgActionPerformed

    private void btnHapusBrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusBrgActionPerformed
        hapusBarang();
    }//GEN-LAST:event_btnHapusBrgActionPerformed

    private void cmbDisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDisActionPerformed
        setTabel_Retur_BM(); showPerDis();
    }//GEN-LAST:event_cmbDisActionPerformed

    private void dtTglCariAwalPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dtTglCariAwalPropertyChange
        dtTglCariAkhir.setDate(dtTglCariAwal.getDate());
        txtCari.setText("");
    }//GEN-LAST:event_dtTglCariAwalPropertyChange

    private void dtTglCariAkhirPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dtTglCariAkhirPropertyChange
        tanggal_Pencarian();
    }//GEN-LAST:event_dtTglCariAkhirPropertyChange

    private void btnCariTglActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariTglActionPerformed
        setTabel_Retur_BM(); cariData_ReturBM();
    }//GEN-LAST:event_btnCariTglActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        setTabel_Retur_BM();
        showData_ReturBM();
        listDis();
        setJam(); 
        clearForm();
        txtCari.requestFocus();
    }//GEN-LAST:event_btnRefreshActionPerformed

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

    private void btnBackUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackUpActionPerformed
        BackUp_Restore.setVisible(true);
        BackUp_Restore.setSize(500, 300);
        BackUp_Restore.setLocationRelativeTo(this);
    }//GEN-LAST:event_btnBackUpActionPerformed

    private void txtJumlahStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_txtJumlahStateChanged
        hitungSubtotal();
    }//GEN-LAST:event_txtJumlahStateChanged

    private void txtJumlahKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyTyped
        if ( txtJumlah.getValue().toString().length() == 2 ) {
        evt.consume();
        }
    }//GEN-LAST:event_txtJumlahKeyTyped

    private void txtJumlahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyPressed
        tambahBarang();
    }//GEN-LAST:event_txtJumlahKeyPressed

    private void txtDaftarBrgKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDaftarBrgKeyPressed
        vid_barang=txtDaftarBrg.getText();
        tampilBarang();
    }//GEN-LAST:event_txtDaftarBrgKeyPressed

    private void txtNoReturKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoReturKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoReturKeyTyped

    private void txtIdDisKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdDisKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdDisKeyTyped

    private void txtNmDisKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNmDisKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNmDisKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFrame BackUp_Restore;
    private javax.swing.JDialog Daftar_Barang;
    private javax.swing.JDialog Daftar_Faktur;
    private javax.swing.JFrame IfrReturPembelian;
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
    private javax.swing.JButton btnReturPembelian;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnSimpanBackup;
    private javax.swing.JButton btnSimpanNew;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnTambahBrg;
    private javax.swing.JButton btnUbah;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox<String> cmbDis;
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
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
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
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
    private javax.swing.JLabel lblFaktur;
    private javax.swing.JLabel lblJam;
    private javax.swing.JLabel lblKeterangan;
    private javax.swing.JLabel lblNoFak;
    private javax.swing.JLabel lblRecordBarang;
    private javax.swing.JLabel lblRecordDetRetur;
    private javax.swing.JLabel lblRecordPembelian;
    private javax.swing.JLabel lblRecordRetur;
    private javax.swing.JLabel lblTgl;
    private javax.swing.JLabel lblUser;
    private javax.swing.JLabel lblUserSistem;
    private javax.swing.JPanel panelData;
    private javax.swing.JPanel panelDetail;
    private javax.swing.JPanel panelUbah;
    private javax.swing.JRadioButton rb1;
    private javax.swing.JRadioButton rb1new;
    private javax.swing.JRadioButton rb2;
    private javax.swing.JRadioButton rb2new;
    private javax.swing.JRadioButton rb3;
    private javax.swing.JRadioButton rb3new;
    private javax.swing.JTable tbDataBarang;
    private javax.swing.JTable tbDataPembelian;
    private javax.swing.JTable tbDataRetur;
    private javax.swing.JTable tbDetRetur_BM;
    private javax.swing.JTable tbTransRetur;
    private Tool.Custom_TextField txtCari;
    private Tool.Custom_TextField txtCariNoFak;
    private Tool.Custom_TextField txtDaftarBrg;
    private javax.swing.JTextField txtDir;
    private Tool.Custom_TextField txtDistributorNew;
    private Tool.NumberTextField txtHargaBeli;
    private Tool.Custom_TextField txtIdDis;
    private javax.swing.JSpinner txtJumlah;
    private Tool.NumberTextField txtJumlahNew;
    private Tool.Custom_TextField txtNamaBrg;
    private Tool.Custom_TextField txtNmDis;
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
