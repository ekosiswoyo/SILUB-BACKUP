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

public class FrTrans_Pembelian extends javax.swing.JInternalFrame {

    KoneksiDB getCnn = new KoneksiDB();
    Connection _Cnn;
    
    String sqlselect, sqlinsert, sqldelete, sqldelete1;
    String vid_barang, vnm_barang;
    String vid_kategori, vnm_kategori;
    String vid_dis, vnm_dis, vnm_manager, valamat, vno_telp, vemail;
    String vid_user, vnm_user;
    String vno_faktur, vtgl_trans, vno_invoice, vno_trans, vtglAwalDet, vtglAkhirDet;
    
    long vhrg_beli, vhrg_jual, vsubtotal, vtotal, vdiskon, vtotal_akhir;
    long vjumlah_barang, vstok;
    
    DefaultTableModel tblPembelian, tblDetail_Pembelian, tblBarang, tblDistributor, tblDetail;
    
    SimpleDateFormat tglview = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat tglinput = new SimpleDateFormat("yyyy-MM-dd");
    
    String id = FrMenu.getU_id();
    String vakses = FrMenu.getU_hakakses();
    String vusername = FrMenu.getU_username();
    
    public FrTrans_Pembelian() {
        initComponents();
        setTabel_Pembelian();
        showDataPembelian();
        setTabel_Detail();
        showDetail();
        akses();
        setJam();
        dtTglCari.setDate(new Date());
        dtTglCariAkhir.setDate(new Date());
        dtTglCariDet.setDate(new Date());
        dtTglCariAkhirDet.setDate(new Date());
        dtTglPembelian.setVisible(false);
        lblRecordTrans.setVisible(true);
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
        txtIdDis.setText("");
        txtnoinvoice.setText("");
        txtNamaDis.setText("");
        dtTglPembelian.setDate(new Date());
        dtTglCari.setDate(new Date());
        dtTglCariAkhir.setDate(new Date());
        dtTglCariDet.setDate(new Date());
        dtTglCariAkhirDet.setDate(new Date());
        txtDaftarBrg.setText("");
        txtIdDis.setText("");
        txtNamaDis.setText("");
        txtNamaBrg.setText("");
        txtHargaBeli.setText("");
        txtHargaJual.setText("");
        txtStok.setText("");
        txtSubtotal.setText("");
        txtJumlah.setText("");
        txtDes.setText("");
        txtCariBrg.setText("");
        txtTotal.setText("");
        txtDiskon.setText("");
        txtnoinvoice.requestFocus();
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

        lblJam.setText(jam + ":" + menit + ":" + detik + " ");
        }
      };
      new Timer(1000, taskPerformer).start();
    }
    private void enableForm(){
        txtIdDis.setEnabled(true);
        txtNamaDis.setEnabled(true);
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
        txtTotalAkhir.setEnabled(true);
        btnDis.setEnabled(true);
        btnTambahBrg.setEnabled(true);
        btnSimpan.setEnabled(true);
        btnHapusBrg.setEnabled(false);
    }
    private void setTabel_Pembelian(){
        String[]kolom1 = {"NO. FAKTUR", "NO. INVOICE", "TANGGAL", "DISTRIBUTOR", "PEGAWAI", "TOTAL", "DISKON", "TOTAL AKHIR"};
        tblPembelian = new DefaultTableModel(null,kolom1){
            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.Double.class,
                java.lang.Double.class,
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
        tbDataPembelian.getColumnModel().getColumn(0).setPreferredWidth(100);
        tbDataPembelian.getColumnModel().getColumn(1).setPreferredWidth(150);
        tbDataPembelian.getColumnModel().getColumn(2).setPreferredWidth(80);
        tbDataPembelian.getColumnModel().getColumn(3).setPreferredWidth(200);
        tbDataPembelian.getColumnModel().getColumn(4).setPreferredWidth(200);
        tbDataPembelian.getColumnModel().getColumn(5).setPreferredWidth(100);
        tbDataPembelian.getColumnModel().getColumn(6).setPreferredWidth(100);
        tbDataPembelian.getColumnModel().getColumn(7).setPreferredWidth(110);
    }
    private void setTabel_TransPembelian(){
        String[]kolom1 = {"NO. FAKTUR", "KODE BARANG", "NAMA BARANG", "HARGA BELI", "HARGA JUAL", "JUMLAH", "SUBTOTAL", "ID"};
        tblDetail_Pembelian = new DefaultTableModel(null, kolom1){
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
                int cola = tblDetail_Pembelian.getColumnCount();
                return (col<cola) ? false : true;
            }
        };
        tbDetailPembelian.setModel(tblDetail_Pembelian);
        tbDetailPembelian.getColumnModel().getColumn(0).setPreferredWidth(150);
        tbDetailPembelian.getColumnModel().getColumn(1).setPreferredWidth(100);
        tbDetailPembelian.getColumnModel().getColumn(2).setPreferredWidth(200);
        tbDetailPembelian.getColumnModel().getColumn(3).setPreferredWidth(100);
        tbDetailPembelian.getColumnModel().getColumn(4).setPreferredWidth(100);
        tbDetailPembelian.getColumnModel().getColumn(5).setPreferredWidth(70);
        tbDetailPembelian.getColumnModel().getColumn(6).setPreferredWidth(150);
        tbDetailPembelian.getColumnModel().getColumn(7).setPreferredWidth(25);
    }
    private void setTabel_Detail(){
        String[]kolom1 = {"TANGGAL", "NO. FAKTUR", "NO. INVOICE", "KODE BARANG", "NAMA BARANG", "HARGA BELI", "HARGA JUAL", "JUMLAH", "SUBTOTAL"};
        tblDetail = new DefaultTableModel(null, kolom1){
            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
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
        tbDetail.getColumnModel().getColumn(0).setPreferredWidth(80);
        tbDetail.getColumnModel().getColumn(1).setPreferredWidth(100);
        tbDetail.getColumnModel().getColumn(2).setPreferredWidth(150);
        tbDetail.getColumnModel().getColumn(3).setPreferredWidth(100);
        tbDetail.getColumnModel().getColumn(4).setPreferredWidth(200);
        tbDetail.getColumnModel().getColumn(5).setPreferredWidth(100);
        tbDetail.getColumnModel().getColumn(6).setPreferredWidth(100);
        tbDetail.getColumnModel().getColumn(7).setPreferredWidth(70);
        tbDetail.getColumnModel().getColumn(8).setPreferredWidth(110);
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
    private void setTabelDistributor(){
        String[] kolom1 = {"NAMA DISTRIBUTOR", "NAMA MANAGER", "ID. DISTRIBUTOR", "NO. TELEPON"};
        tblDistributor = new DefaultTableModel(null, kolom1){
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
            
            //agar tabel tidak bisa diedit
            public boolean isCellEditable(int row, int col){
                int cola = tblDistributor.getColumnCount();
                return (col < cola) ? false:true;
            }
        };
        tbDataDistributor.setModel(tblDistributor);
        tbDataDistributor.getColumnModel().getColumn(0).setPreferredWidth(150);
        tbDataDistributor.getColumnModel().getColumn(1).setPreferredWidth(150);
        tbDataDistributor.getColumnModel().getColumn(2).setPreferredWidth(100);
        tbDataDistributor.getColumnModel().getColumn(3).setPreferredWidth(100);
    }
    private void clearTabel_Pembelian(){
        int row_BM  = tblPembelian.getRowCount();
        for(int i = 0; i <row_BM; i++){
            tblPembelian.removeRow(0);
        }
    }
    private void clearTabel_Detail(){
        int row_BM  = tblDetail.getRowCount();
        for(int i = 0; i <row_BM; i++){
            tblDetail.removeRow(0);
        }
    }
    private void showDataPembelian(){
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            clearTabel_Pembelian();
            sqlselect = "select * from tb_pembelian a, tb_distributor b, tb_user c "
                    +"where a.id_distributor=b.id_distributor and a.id_user=c.id_user "
                    +"order by tgl_transaksi desc, no_faktur_pembelian desc"; 
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);// mengeksekusi query select
            while(res.next()){
                vno_faktur = res.getString("no_faktur_pembelian");
                vno_invoice = res.getString("no_invoice");
                vtgl_trans = tglview.format(res.getDate("tgl_transaksi"));
                vnm_dis = res.getString("nama_distributor");
                vnm_user = res.getString("id_user")+"-"+res.getString("nama_lengkap");
                vtotal = res.getLong("total");
                vdiskon = res.getLong("diskon");
                vtotal_akhir = vtotal - vdiskon;
                Object[]data = {vno_faktur, vno_invoice, vtgl_trans, vnm_dis, vnm_user, vtotal, vdiskon, vtotal_akhir};
                tblPembelian.addRow(data);
            }
            lblRecordPembelian.setText("RECORD : "+tblPembelian.getRowCount());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method showDataPembelian() : "+ex);
        }
    }
    private void showDetail(){
        try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            clearTabel_Detail();
                sqlselect = "select * from tb_pembelian a, tb_detail_pembelian b, tb_barang d "
                    + " where a.no_faktur_pembelian=b.no_faktur_pembelian and "
                    + " b.id_barang=d.id_barang order by a.tgl_transaksi desc, b.no_faktur_pembelian desc ";
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);
            while(res.next()){
                vno_invoice = res.getString("no_invoice");
                vtgl_trans = tglview.format(res.getDate("tgl_transaksi"));
                vno_faktur = res.getString("no_faktur_pembelian");
                vid_barang = res.getString("id_barang");
                vnm_barang = res.getString("nama_barang");
                vhrg_beli = res.getLong("b.harga_beli"); 
                vhrg_jual = res.getLong("harga_jual");
                vjumlah_barang = res.getInt("jumlah_barang");
                vsubtotal = res.getLong("subtotal");
                Object[]data = {vtgl_trans, vno_faktur, vno_invoice, vid_barang, vnm_barang, vhrg_beli, vhrg_jual, vjumlah_barang, vsubtotal};
                tblDetail.addRow(data);
                lblDetail.setText("RECORD : "+tblDetail.getRowCount());
            }
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
                sqlselect = "select * from tb_pembelian a, tb_detail_pembelian b, tb_barang d "
                    + " where b.id_barang=d.id_barang and a.no_faktur_pembelian=b.no_faktur_pembelian and "
                    + " a.no_faktur_pembelian like '%"+txtCariDet.getText()+"%' "
                    + " order by a.tgl_transaksi desc, b.no_faktur_pembelian desc ";
            }else{
                sqlselect = "select * from tb_pembelian a, tb_detail_pembelian b, tb_barang d "
                    + " where a.no_faktur_pembelian=b.no_faktur_pembelian and "
                    + " b.id_barang=d.id_barang and tgl_transaksi between '"+vtgl_trans+"' and "
                    + " '"+vtgl_trans1+"' order by a.tgl_transaksi desc, b.no_faktur_pembelian desc ";
            }
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);
            while(res.next()){
                vno_invoice = res.getString("no_invoice");
                vtgl_trans = tglview.format(res.getDate("tgl_transaksi"));
                vno_faktur = res.getString("no_faktur_pembelian");
                vid_barang = res.getString("id_barang");
                vnm_barang = res.getString("nama_barang");
                vhrg_beli = res.getLong("b.harga_beli"); 
                vhrg_jual = res.getLong("harga_jual");
                vjumlah_barang = res.getInt("jumlah_barang");
                vsubtotal = res.getLong("subtotal");
                Object[]data = {vtgl_trans, vno_faktur, vno_invoice, vid_barang, vnm_barang, vhrg_beli, vhrg_jual, vjumlah_barang, vsubtotal};
                tblDetail.addRow(data);
                lblDetail.setText("RECORD : "+tblDetail.getRowCount());
            }
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
            }else if(cmbKat.getSelectedIndex()!=0 && txtCariBrg.getText().equals("")){
                sqlselect = "select * from tb_barang a, tb_kategori b"
                    + " where a.id_kategori=b.id_kategori and nama_kategori='"+cmbKat.getSelectedItem().toString()+"' "
                    + " order by nama_barang asc";
            }else {
                sqlselect = "select * from tb_barang a, tb_kategori b"
                    + " where a.id_kategori=b.id_kategori and nama_kategori='"+cmbKat.getSelectedItem().toString()+"' and "
                    + " nama_barang like '%"+txtCariBrg.getText()+"%' order by nama_barang asc";
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
                Object[]data = {vnm_barang, vid_barang, vnm_kategori, vhrg_beli, vhrg_jual, vstok};
                tblBarang.addRow(data);
            }
            lblRecordBarang.setText("RECORD : "+tbDataBarang.getRowCount());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method showDataBarang() : "+ex);
        }
    }
    private void showDataDistributor(){
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            sqlselect = "select * from tb_distributor where nama_distributor like '"+txtCariDistributor.getText()+"%%' "
                    + " or id_distributor like '"+txtCariDistributor.getText()+"%%' order by nama_distributor asc";
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);// mengeksekusi query select
            while(res.next()){
                vid_dis = res.getString("id_distributor");
                vnm_dis = res.getString("nama_distributor");
                vnm_manager = res.getString("nama_manager");
                vno_telp = res.getString("no_telp");
                Object[]data = {vnm_dis, vnm_manager, vid_dis, vno_telp};
                tblDistributor.addRow(data);
            }
            lblRecordMember.setText("RECORD : "+tbDataDistributor.getRowCount());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method showTabelDistributor() : "+ex);
        }
    }
    private void tampilDistributor(){
        try{
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select * from tb_distributor where id_distributor='"+vid_dis+"' ";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            if(res.first()){
                txtIdDis.setText(res.getString("id_distributor"));
                txtNamaDis.setText(res.getString("nama_distributor"));
                Daftar_Distributor.dispose();
                txtDaftarBrg.requestFocus();
            }
            }catch (SQLException ex){
                JOptionPane.showMessageDialog(this, "Error mthod tampilDistributor : " + ex);
            }
    }
    private void getDataPembelian(){
        try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select * from tb_pembelian a, tb_detail_pembelian b, tb_distributor c, tb_barang d "
                    + " where a.id_distributor=c.id_distributor and a.no_faktur_pembelian=b.no_faktur_pembelian and "
                    + " b.id_barang=d.id_barang and b.no_faktur_pembelian='"+vno_faktur+"' ";
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);
            while(res.next()){
                lblNoFak.setText(res.getString("no_faktur_pembelian")+"--"+res.getString("nama_distributor"));
                txtNoFaktur.setText(res.getString("no_faktur_pembelian"));
                txtnoinvoice.setText(res.getString("no_invoice"));
                txtIdDis.setText(res.getString("id_distributor"));
                txtNamaDis.setText(res.getString("nama_distributor"));
                txtTotal.setText(res.getString("total"));
                txtDiskon.setText(res.getString("diskon"));
                vtotal = Long.parseLong(txtTotal.getText());
                vdiskon = Long.parseLong(txtDiskon.getText());
                int vtotal_akhir = (int) (vtotal-vdiskon);
                txtTotalAkhir.setText(Integer.toString(vtotal_akhir));
                dtTglPembelian.setDate(new Date());
                
                vno_trans = res.getString("no_trans_pembelian");
                vno_faktur = res.getString("no_faktur_pembelian");
                vid_barang = res.getString("id_barang");
                vnm_barang = res.getString("nama_barang");
                vhrg_beli = res.getLong("b.harga_beli"); 
                vhrg_jual = res.getLong("harga_jual");
                vjumlah_barang = res.getInt("jumlah_barang");
                vsubtotal = res.getLong("subtotal");
                Object[]data = {vno_faktur, vid_barang, vnm_barang, vhrg_beli, vhrg_jual, vjumlah_barang, vsubtotal, vno_trans};
                tblDetail_Pembelian.addRow(data);
                lblRecordTrans.setText("RECORD : "+tblDetail_Pembelian.getRowCount());
                btnSimpan.setVisible(false); btnSimpan.setText(""); btnUbah.setVisible(true); btnDis.setEnabled(false);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method getDataPembelian() : "+ex);
        }              
    }
    private void cariTransPembelian(){
        vtgl_trans = tglinput.format(dtTglCari.getDate());
        String vtgl_trans1 = tglinput.format(dtTglCariAkhir.getDate());
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            clearTabel_Pembelian();
            if(txtCari.getText().length() > 0 ){
                sqlselect = "select * from tb_pembelian a, tb_distributor b, tb_user c "
                        + " where a.id_distributor=b.id_distributor and a.id_user=c.id_user and a.no_faktur_pembelian like '%"+txtCari.getText()+"%' "
                        + " order by tgl_transaksi desc, no_faktur_pembelian desc";
            }else{
                sqlselect = "select * from tb_pembelian a, tb_distributor b, tb_user c "
                        + " where a.id_distributor=b.id_distributor and a.id_user=c.id_user and tgl_transaksi "
                        + " between '"+vtgl_trans+"' and '"+vtgl_trans1+"' "
                        + " order by tgl_transaksi desc, no_faktur_pembelian desc";
            }
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);// mengeksekusi query select
            while(res.next()){
                vno_faktur = res.getString("no_faktur_pembelian");
                vno_invoice = res.getString("no_invoice");
                vtgl_trans = tglview.format(res.getDate("tgl_transaksi"));
                vnm_dis = res.getString("nama_distributor");
                vnm_user = res.getString("id_user")+"-"+res.getString("nama_lengkap");
                vtotal = res.getLong("total");
                vdiskon = res.getLong("diskon");
                vtotal_akhir = vtotal - vdiskon;
                Object[]data = {vno_faktur, vno_invoice, vtgl_trans, vnm_dis, vnm_user, vtotal, vdiskon, vtotal_akhir};
                tblPembelian.addRow(data);
            }
            lblRecordPembelian.setText("RECORD : "+tblPembelian.getRowCount());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method showDataPembelian() : "+ex);
        }
    }
    private void aksiUbahPembelian(){
        vno_faktur = txtNoFaktur.getText();
        vno_invoice = txtnoinvoice.getText();
        vid_dis = txtIdDis.getText(); 
        vtgl_trans = tglinput.format(dtTglPembelian.getDate())+" "+lblJam.getText();
        vtotal = Long.valueOf(txtTotal.getText());
        vdiskon = Long.valueOf(txtDiskon.getText());  
        try{
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            Statement state = _Cnn.createStatement();
            if(btnUbah.getText().equals("SIMPAN PERUBAHAN")){
                    sqlinsert = "update tb_pembelian set no_invoice='"+vno_invoice+"', id_user='"+id+"', id_distributor='"+vid_dis+"', "
                            + " total='"+vtotal+"', diskon='"+vdiskon+"' where no_faktur_pembelian='"+vno_faktur+"' ";
                    state.executeUpdate(sqlinsert);
                JOptionPane.showMessageDialog(null, "DATA BERHASIL DIUBAH ! ",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE); IfrPembelian.dispose(); showDataPembelian();
            }
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "ERROR AKSI_UBAH_PENJUALAN " +ex);
        }
        int jumlah_baris = tbDetailPembelian.getRowCount();
        if(jumlah_baris == 0){
        }else{
            try{
                _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
                _Cnn = getCnn.getConnection();// membuka koneksi
                Statement state = _Cnn.createStatement();
                int i=0;
            while(i < jumlah_baris){
                state.executeUpdate(" update tb_barang set "
                        + " harga_beli='"+tbDetailPembelian.getValueAt(i, 3)+"',"
                        + " harga_jual='"+tbDetailPembelian.getValueAt(i, 4)+"' "
                        + " where id_barang='"+tbDetailPembelian.getValueAt(i, 1)+ "' ");
                i++;
            } 
            }catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, "Gagal Menyimpan! Error : "+e);
            }
        }
    }
    
    private void aksiSimpanPembelian(){
        vno_faktur = txtNoFaktur.getText();
        vno_invoice = txtnoinvoice.getText();
        vtgl_trans = tglinput.format(dtTglPembelian.getDate())+" "+lblJam.getText();
        vid_dis = txtIdDis.getText();
        vtotal = Long.valueOf(txtTotal.getText());
        vdiskon = Long.valueOf(txtDiskon.getText());
        if(btnSimpan.getText().equals("SIMPAN")){
            sqlinsert = "insert into tb_pembelian(no_faktur_pembelian, no_invoice, tgl_transaksi, id_distributor ,id_user, total, diskon) "
                    + " values('"+vno_faktur+"', '"+vno_invoice+"', '"+vtgl_trans+"', '"+vid_dis+"', '"+id+"', '"+vtotal+"', '"+vdiskon+"' )";
        }
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            Statement state = _Cnn.createStatement();
            state.executeUpdate(sqlinsert);
            JOptionPane.showMessageDialog(null, "DATA BERHASIL DISIMPAN ! ",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            aksiSimpanDetPembelian();
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error method aksiSimpanPembelian() : "
                    +ex, "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void aksiSimpanDetPembelian(){
        int jumlah_baris = tbDetailPembelian.getRowCount();
        if(jumlah_baris == 0){
        }else{
            try{
                _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
                _Cnn = getCnn.getConnection();// membuka koneksi
                Statement state = _Cnn.createStatement();
                int i=0;
            while(i < jumlah_baris){
                state.executeUpdate("insert into tb_detail_pembelian (no_faktur_pembelian, id_barang, harga_beli, jumlah_barang, subtotal) values("
                        + "'"+tbDetailPembelian.getValueAt(i, 0)+"',"
                        + "'"+tbDetailPembelian.getValueAt(i, 1)+"',"
                        + "'"+tbDetailPembelian.getValueAt(i, 3)+"',"
                        + "'"+tbDetailPembelian.getValueAt(i, 5)+"',"
                        + "'"+tbDetailPembelian.getValueAt(i, 6)+"')");
                state.executeUpdate(" update tb_barang set "
                        + " harga_beli='"+tbDetailPembelian.getValueAt(i, 3)+"',"
                        + " harga_jual='"+tbDetailPembelian.getValueAt(i, 4)+"' "
                        + " where id_barang='"+tbDetailPembelian.getValueAt(i, 1)+ "'");
                i++;
            }
            clearForm(); enableForm(); txtTotalAkhir.setText(""); autoNoFaktur();
            }catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, "Gagal Menyimpan! Error : "+e);
            }
        }
    }
    private void aksiReset(){
        int jumlah_baris = tbDetailPembelian.getRowCount();
        if(jumlah_baris == 0){
        }else{
            try{
                _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
                _Cnn = getCnn.getConnection();// membuka koneksi
                Statement state = _Cnn.createStatement();
                int i=0;
            while(i < jumlah_baris){
                state.executeUpdate("update tb_barang set stok = stok - '"+tbDetailPembelian.getValueAt(i, 5)+"' "
                        + " where id_barang='"+tbDetailPembelian.getValueAt(i, 1)+"' ");
                i++;
            }
            clearForm(); enableForm();
            txtTotalAkhir.setText(""); txtDiskon.setText("");  autoNoFaktur();
            }catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, "Gagal Menyimpan! Error : "+e);
            }
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
                txtHargaBeli.setText(res.getString("harga_beli"));
                txtHargaJual.setText(res.getString("harga_jual"));
                txtStok.setText(res.getString("stok"));
                txtDes.setText(res.getString("deskripsi"));
                Daftar_Barang.dispose();
                txtDaftarBrg.setEditable(false);
                txtJumlah.requestFocus();
            }
            }catch (SQLException ex){
                JOptionPane.showMessageDialog(this, "Error mthod getDataBarang : " + ex);
            }
    }
    private void getDetail(){
        vno_faktur = txtNoFaktur.getText();
        try{
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select * from tb_barang a, tb_kategori b, tb_detail_pembelian c "
                    + " where a.id_kategori=b.id_kategori and a.id_barang=c.id_barang "
                    + " and c.id_barang='"+vid_barang+"' and no_faktur_pembelian='"+vno_faktur+"' ";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            if(res.first()){
                txtDaftarBrg.setText(res.getString("id_barang"));
                txtNamaBrg.setText(res.getString("nama_barang"));
                txtHargaBeli.setText(res.getString("c.harga_beli"));
                txtHargaJual.setText(res.getString("harga_jual"));
                txtStok.setText(res.getString("stok"));
                txtDes.setText(res.getString("deskripsi"));
                txtSubtotal.setText(res.getString("subtotal"));
                Daftar_Barang.dispose();
                txtDaftarBrg.setEditable(false);
                txtJumlah.requestFocus();
            }
            }catch (SQLException ex){
                JOptionPane.showMessageDialog(this, "Error mthod getDataBarang : " + ex);
            }
    }  
    private void hitungTotal(){
        long vtotal_harga = 0;
        int vbaris = tbDetailPembelian.getRowCount();
        tblDetail_Pembelian = (DefaultTableModel) tbDetailPembelian.getModel();
        for(int i=0;i<vbaris;i++){
            vhrg_beli = Long.parseLong(tblDetail_Pembelian.getValueAt(i, 3).toString());
            vjumlah_barang = Long.parseLong(tblDetail_Pembelian.getValueAt(i, 5).toString());
            vtotal_harga = (Long) (vtotal_harga+(vhrg_beli*vjumlah_barang));
        }
        txtDaftarBrg.setText("");
        txtNamaBrg.setText("");
        txtHargaBeli.setText("");
        txtHargaJual.setText("");
        txtStok.setText("");
        txtSubtotal.setText("");
        txtJumlah.setText("");
        txtDes.setText("");
        txtCariBrg.setText("");
        txtTotal.setText(String.valueOf(vtotal_harga));
        txtTotalAkhir.setText(String.valueOf(vtotal_harga));
        txtTotal.requestFocus();
        txtDaftarBrg.requestFocus();
    }
    private void hitungTotalAkhir(){
        vtotal = Long.parseLong(txtTotal.getText());
        vdiskon = Long.parseLong(txtDiskon.getText());
        long vtotal_akhir = (long) (vtotal-vdiskon);
            txtTotalAkhir.setText(String.valueOf(vtotal_akhir));
            btnSimpan.requestFocus();
    }
    private void tambahBarang(){
        vno_faktur = txtNoFaktur.getText();
        vid_barang = txtDaftarBrg.getText();
        vnm_barang = txtNamaBrg.getText();
        vhrg_beli = Long.parseLong(txtHargaBeli.getText());
        vhrg_jual = Long.parseLong(txtHargaJual.getText());
        vjumlah_barang = Integer.parseInt(txtJumlah.getText());
        vstok = Integer.parseInt(txtStok.getText());
        vsubtotal = vhrg_beli*vjumlah_barang;
        if(txtDaftarBrg.getText().equals("")){
            JOptionPane.showMessageDialog(null, "KODE BARANG HARUS DIISI !",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            txtJumlah.requestFocus();
        }else if(txtJumlah.getText().equals("0") || txtJumlah.getText().equals("00")){
            JOptionPane.showMessageDialog(null, "JUMLAH BARANG HARUS LEBIH DARI 0 !",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            txtJumlah.requestFocus();
        }else {
            try{
                _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
                _Cnn = getCnn.getConnection();// membuka koneksi
                Statement state = _Cnn.createStatement();
                if(btnSimpan.getText().equals("SIMPAN")){
                    state.executeUpdate(" update tb_barang set stok = stok + '"+vjumlah_barang+"' where id_barang = '"+vid_barang+"' ");
                }else {
                    state.executeUpdate(" update tb_barang set stok = stok + '"+vjumlah_barang+"' "
                                + " where id_barang = '"+vid_barang+"' ");
                    state.executeUpdate("insert into tb_detail_pembelian (no_faktur_pembelian, id_barang, harga_beli, jumlah_barang, subtotal) values ("
                        + "'"+vno_faktur+"',"
                        + "'"+vid_barang+"',"
                        + "'"+vhrg_beli+"',"
                        + "'"+vjumlah_barang+"',"
                        + "'"+vsubtotal+"')");
                } 
            }catch(Exception e){
                JOptionPane.showMessageDialog(rootPane, "Gagal Menyimpan! Error : "+e);
            }
        tblDetail_Pembelian.addRow(new Object[]{
            vno_faktur,vid_barang,vnm_barang,vhrg_beli,vhrg_jual,vjumlah_barang,vsubtotal
        });
        tbDetailPembelian.setModel(tblDetail_Pembelian);
        lblRecordTrans.setText("RECORD : "+tblDetail_Pembelian.getRowCount()); hitungTotal(); hitungTotalAkhir(); txtDaftarBrg.setEditable(true);
        }
    }
    private void hapusBarang(){
        int x = tbDetailPembelian.getSelectedRow();
        int jumlah_baris = tbDetailPembelian.getRowCount();
        if (btnSimpan.getText().equals("SIMPAN") || btnUbah.getText().equals("SIMPAN PERUBAHAN")){
            int jawab = JOptionPane.showConfirmDialog(null, 
                "ANDA YAKIN AKAN MEMBATALKAN TRANSAKSI BARANG INI ? \n OTOMATIS AKAN MENGURANGI STOK KODE BARANG = '"+txtDaftarBrg.getText()+"'  !",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if(jawab==JOptionPane.YES_OPTION){
                try{
                    _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
                    _Cnn = getCnn.getConnection();// membuka koneksi
                    Statement state = _Cnn.createStatement();
                    int i=0;
                    if(i < jumlah_baris){
                        state.executeUpdate(" update tb_barang set stok = stok - '"+tbDetailPembelian.getValueAt(tbDetailPembelian.getSelectedRow(), 5)+"' "
                                + " where id_barang = '"+tbDetailPembelian.getValueAt(tbDetailPembelian.getSelectedRow(), 1)+"' ");
                        state.executeUpdate(" delete from tb_detail_pembelian where id_barang='"+txtDaftarBrg.getText()+"' and "
                                + " no_faktur_pembelian='"+txtNoFaktur.getText()+"' and "
                                + " no_trans_pembelian = '"+tbDetailPembelian.getValueAt(tbDetailPembelian.getSelectedRow(), 7)+"' ");
                        tblDetail_Pembelian.removeRow(x);
                        btnHapusBrg.setEnabled(false);
                        hitungTotal();
                        hitungTotalAkhir();
                        txtDaftarBrg.requestFocus(); txtDaftarBrg.setEditable(true);
                    } lblRecordTrans.setText("RECORD : "+tblDetail_Pembelian.getRowCount());
                }catch(Exception e){
                JOptionPane.showMessageDialog(rootPane, "Gagal Menyimpan! Error : "+e);
                }
            }
        }
    }
    private void autoNoFaktur(){
        Date sk = new Date();
        SimpleDateFormat format1=new SimpleDateFormat("ddMMyy");
        String time = format1.format(sk);
        try{
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select right(no_faktur_pembelian,4) as no_faktur_pembelian "
                    + " from tb_detail_pembelian order by no_faktur_pembelian desc";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            if (res.next()) {
                String kode = res.getString("no_faktur_pembelian");
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
                txtNoFaktur.setText("FK-" +time+ "-" + Nol + AN);
            }else{
                int kode = 1;
                txtNoFaktur.setText("FK-" +time+ "-" +"000"+Integer.toString(kode));//sesuaikan dengan variable namenya
            }
            }catch (SQLException ex){
                JOptionPane.showMessageDialog(this,"Error Method autoNoFaktur : " + ex);
            }
    }
    private void tanggal_Pencarian(){
        try{
        if(dtTglCari.getDate() !=null && dtTglCariAkhir.getDate()!=null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            vtglAwalDet=format.format(dtTglCari.getDate());
            vtglAkhirDet=format.format(dtTglCariAkhir.getDate());
            Date Tanggal1 = format.parse(vtglAwalDet);
            Date Tanggal2 = format.parse(vtglAkhirDet);
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
    private void formTengah(){
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
    private void hitungSubtotal(){
        long vsub = 0;
        vhrg_beli = Long.parseLong(txtHargaBeli.getText());
        vjumlah_barang = Long.parseLong (txtJumlah.getText());
        vsub = (long) (vsub+vhrg_beli*vjumlah_barang);
        txtSubtotal.setText(Long.toString(vsub));
        txtJumlah.requestFocus();
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
                txtDir.setText(""); BackUp_Restore.dispose(); setTabel_Pembelian(); showDataPembelian();
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

        IfrPembelian = new javax.swing.JFrame();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        txtNoFaktur = new Tool.Custom_TextField();
        jLabel12 = new javax.swing.JLabel();
        txtTotalAkhir = new Tool.NumberTextField();
        jLabel14 = new javax.swing.JLabel();
        txtnoinvoice = new Tool.Custom_TextField();
        jLabel37 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        txtIdDis = new Tool.Custom_TextField();
        txtNamaDis = new Tool.Custom_TextField();
        btnDis = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbDetailPembelian = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        btnTambahBrg = new javax.swing.JButton();
        btnHapusBrg = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtDaftarBrg = new Tool.Custom_TextField();
        btnDaftarBrg = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtNamaBrg = new Tool.Custom_TextField();
        jLabel10 = new javax.swing.JLabel();
        txtHargaBeli = new Tool.NumberTextField();
        jLabel6 = new javax.swing.JLabel();
        txtHargaJual = new Tool.NumberTextField();
        jLabel13 = new javax.swing.JLabel();
        txtStok = new Tool.NumberTextField();
        jLabel7 = new javax.swing.JLabel();
        txtJumlah = new Tool.NumberTextField();
        jLabel18 = new javax.swing.JLabel();
        txtSubtotal = new Tool.NumberTextField();
        jLabel42 = new javax.swing.JLabel();
        lblRecordTrans = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtTotal = new Tool.NumberTextField();
        txtDiskon = new Tool.NumberTextField();
        btnDataPenjualan = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
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
        jLabel36 = new javax.swing.JLabel();
        lblJam = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        lblUsersistem = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        dtTglPembelian = new com.toedter.calendar.JDateChooser();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtDes = new javax.swing.JTextArea();
        jLabel41 = new javax.swing.JLabel();
        Daftar_Barang = new javax.swing.JDialog();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbDataBarang = new javax.swing.JTable();
        lblRecordBarang = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        cmbKat = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        txtCariBrg = new Tool.Custom_TextField();
        Daftar_Distributor = new javax.swing.JDialog();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbDataDistributor = new javax.swing.JTable();
        lblRecordMember = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        txtCariDistributor = new Tool.Custom_TextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        BackUp_Restore = new javax.swing.JFrame();
        jPanel15 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel16 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        txtDir = new javax.swing.JTextField();
        btnPilihLokasi = new javax.swing.JButton();
        btnSimpanBackup = new javax.swing.JButton();
        jFileChooser1 = new javax.swing.JFileChooser();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        panelPembelian = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbDataPembelian = new javax.swing.JTable();
        lblRecordPembelian = new javax.swing.JLabel();
        txtCari = new Tool.Custom_TextField();
        lblNoFak = new javax.swing.JLabel();
        btnTambah = new javax.swing.JButton();
        btnBackUp = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        dtTglCari = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        dtTglCariAkhir = new com.toedter.calendar.JDateChooser();
        btnCariTgl = new javax.swing.JButton();
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

        IfrPembelian.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        IfrPembelian.setTitle("TRANSAKSI PEMBELIAN-LUBADAJAYA MARKET");
        IfrPembelian.setUndecorated(true);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "(+) FORM", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 12))); // NOI18N

        txtNoFaktur.setEditable(false);
        txtNoFaktur.setBackground(new java.awt.Color(0, 204, 204));
        txtNoFaktur.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNoFaktur.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        txtNoFaktur.setPlaceholder("");

        jLabel12.setFont(new java.awt.Font("Arial Narrow", 1, 36)); // NOI18N
        jLabel12.setForeground(java.awt.Color.red);
        jLabel12.setText("TOTAL AKHIR : Rp.");

        txtTotalAkhir.setEditable(false);
        txtTotalAkhir.setBackground(new java.awt.Color(0, 0, 0));
        txtTotalAkhir.setForeground(new java.awt.Color(255, 0, 0));
        txtTotalAkhir.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTotalAkhir.setFont(new java.awt.Font("Arial Narrow", 1, 40)); // NOI18N

        jLabel14.setText("NO. TRANSAKSI : ");

        txtnoinvoice.setBackground(new java.awt.Color(0, 204, 204));
        txtnoinvoice.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtnoinvoice.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        txtnoinvoice.setPlaceholder("");
        txtnoinvoice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtnoinvoiceKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtnoinvoiceKeyTyped(evt);
            }
        });

        jLabel37.setText("NO. INVOICE     : ");

        jLabel43.setText("KODE DISTRIBUTOR : ");

        jLabel44.setText("NAMA DISTRIBUTOR : ");

        txtIdDis.setEditable(false);
        txtIdDis.setBackground(new java.awt.Color(0, 204, 204));
        txtIdDis.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtIdDis.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        txtIdDis.setPlaceholder("");
        txtIdDis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdDisActionPerformed(evt);
            }
        });

        txtNamaDis.setEditable(false);
        txtNamaDis.setBackground(new java.awt.Color(0, 204, 204));
        txtNamaDis.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNamaDis.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        txtNamaDis.setPlaceholder("");

        btnDis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/search.png"))); // NOI18N
        btnDis.setMnemonic('d');
        btnDis.setText("CARI");
        btnDis.setToolTipText("PENCARIAN DISTRIBUTOR");
        btnDis.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtNoFaktur, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtnoinvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel44)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtIdDis, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                    .addComponent(txtNamaDis, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addComponent(btnDis)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTotalAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNoFaktur, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtIdDis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDis, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtnoinvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaDis, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37))
                .addGap(0, 10, Short.MAX_VALUE))
            .addComponent(txtTotalAkhir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "(+) DETAIL TRANSAKSI", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 12))); // NOI18N

        tbDetailPembelian.setFont(new java.awt.Font("Arial Narrow", 1, 16)); // NOI18N
        tbDetailPembelian.setModel(new javax.swing.table.DefaultTableModel(
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
        tbDetailPembelian.setRowHeight(25);
        tbDetailPembelian.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDetailPembelianMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbDetailPembelian);

        jPanel14.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

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

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(btnTambahBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHapusBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnTambahBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnHapusBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel9.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));

        jLabel11.setText("KODE BARANG");

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
        btnDaftarBrg.setMnemonic('b');
        btnDaftarBrg.setText("CARI");
        btnDaftarBrg.setToolTipText("PENCARIAN BARANG");
        btnDaftarBrg.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDaftarBrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDaftarBrgActionPerformed(evt);
            }
        });

        jLabel5.setText("NAMA BARANG");

        txtNamaBrg.setEditable(false);
        txtNamaBrg.setBackground(new java.awt.Color(0, 204, 204));
        txtNamaBrg.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNamaBrg.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        txtNamaBrg.setPlaceholder("NAMA BARANG");

        jLabel10.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        jLabel10.setForeground(java.awt.Color.red);
        jLabel10.setText("* HARGA BELI");

        txtHargaBeli.setBackground(new java.awt.Color(0, 204, 204));
        txtHargaBeli.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        txtHargaBeli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtHargaBeliKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHargaBeliKeyTyped(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        jLabel6.setForeground(java.awt.Color.red);
        jLabel6.setText("* HARGA JUAL");

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

        jLabel13.setText("STOK");

        txtStok.setEditable(false);
        txtStok.setBackground(new java.awt.Color(0, 204, 204));
        txtStok.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtStok.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        jLabel7.setForeground(java.awt.Color.red);
        jLabel7.setText("* QTY");

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

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("->");

        txtSubtotal.setEditable(false);
        txtSubtotal.setBackground(new java.awt.Color(0, 204, 204));
        txtSubtotal.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N

        jLabel42.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel42.setForeground(java.awt.Color.red);
        jLabel42.setText("* SUBTOTAL");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtDaftarBrg, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addComponent(btnDaftarBrg)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(txtNamaBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHargaBeli, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(txtStok, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(txtJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7))
                        .addGap(42, 42, 42))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel11)
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDaftarBrg, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtHargaBeli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtNamaBrg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDaftarBrg, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel42)
                                .addGap(0, 0, 0)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtStok, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(0, 0, 0)
                                .addComponent(txtHargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );

        lblRecordTrans.setFont(new java.awt.Font("Arial Narrow", 1, 12)); // NOI18N
        lblRecordTrans.setText("RECORD : 0");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lblRecordTrans)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblRecordTrans))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/save_add.png"))); // NOI18N
        btnSimpan.setMnemonic('s');
        btnSimpan.setText("SIMPAN");
        btnSimpan.setToolTipText("KLIK UNTUK MENYIMPAN");
        btnSimpan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSimpan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSimpan.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "(+) INPUT DISKON"));

        jLabel8.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        jLabel8.setText("TOTAL               : Rp. ");

        jLabel9.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        jLabel9.setText("DISKON            : Rp.");

        txtTotal.setEditable(false);
        txtTotal.setBackground(new java.awt.Color(0, 0, 0));
        txtTotal.setForeground(new java.awt.Color(255, 0, 0));
        txtTotal.setFont(new java.awt.Font("Arial Narrow", 1, 24)); // NOI18N

        txtDiskon.setBackground(new java.awt.Color(0, 0, 0));
        txtDiskon.setForeground(new java.awt.Color(255, 0, 0));
        txtDiskon.setFont(new java.awt.Font("Arial Narrow", 1, 24)); // NOI18N
        txtDiskon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDiskonActionPerformed(evt);
            }
        });
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
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDiskon, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDiskon, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        btnDataPenjualan.setBackground(java.awt.Color.green);
        btnDataPenjualan.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDataPenjualan.setText("LIHAT DATA PEMBELIAN");
        btnDataPenjualan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDataPenjualan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDataPenjualan.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDataPenjualan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataPenjualanActionPerformed(evt);
            }
        });

        btnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/refresh_1.png"))); // NOI18N
        btnBatal.setMnemonic('n');
        btnBatal.setText("BATAL");
        btnBatal.setToolTipText("KLIK UNTUK BATAL TRANSAKSI");
        btnBatal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBatal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBatal.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });

        btnUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/save_add.png"))); // NOI18N
        btnUbah.setMnemonic('u');
        btnUbah.setText("SIMPAN PERUBAHAN");
        btnUbah.setToolTipText("KLIK UNTUK SIMPAN PERUBAHAN");
        btnUbah.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUbah.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUbah.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(java.awt.Color.lightGray, 1, true), "PANEL INFORMASI"));

        jLabel1.setText("* FORM INI DIGUNAKAN UNTUK MEMBELI BARANG DARI DISTRIBUTOR !");

        jLabel3.setText("* INPUT DATA PADA FORM INI AKAN OTOMATIS MENGUPDATE JUMLAH STOK BARANG !");

        jLabel4.setText("* TELITI SEBELUM MENYIMPAN !");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSimpan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUbah)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBatal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnDataPenjualan)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDataPenjualan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBatal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUbah, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        jLabel36.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("FORM PEMBELIAN BARANG");
        jLabel36.setOpaque(true);

        lblJam.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        lblJam.setForeground(java.awt.Color.red);
        lblJam.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJam.setOpaque(true);

        lblUser.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        lblUser.setForeground(java.awt.Color.red);
        lblUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel38.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jLabel38.setForeground(java.awt.Color.red);
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("- *HAK AKSES :");

        lblUsersistem.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        lblUsersistem.setForeground(java.awt.Color.red);
        lblUsersistem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel40.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jLabel40.setForeground(java.awt.Color.red);
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setText("*USER SISTEM :");

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
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(dtTglPembelian, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblUsersistem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblUser))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 538, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblJam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel21)
                                .addComponent(jLabel20)
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel26)
                                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel36)
                                .addComponent(lblJam))
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addGap(2, 2, 2)
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel23)
                                        .addComponent(jLabel24)
                                        .addComponent(jLabel25)
                                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel28)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(dtTglPembelian, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel30)
                            .addComponent(lblUser)
                            .addComponent(jLabel38)
                            .addComponent(lblUsersistem)
                            .addComponent(jLabel40))))
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
            .addComponent(jScrollPane6)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane6)
                .addContainerGap())
        );

        jLabel41.setFont(new java.awt.Font("Agency FB", 3, 14)); // NOI18N
        jLabel41.setForeground(java.awt.Color.red);
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel41.setText("*TEKAN ENTER SETELAH PENGISIAN FIELD !");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel41)
                .addContainerGap(111, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout IfrPembelianLayout = new javax.swing.GroupLayout(IfrPembelian.getContentPane());
        IfrPembelian.getContentPane().setLayout(IfrPembelianLayout);
        IfrPembelianLayout.setHorizontalGroup(
            IfrPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        IfrPembelianLayout.setVerticalGroup(
            IfrPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IfrPembelianLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 9, Short.MAX_VALUE))
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

        jPanel8.setBackground(new java.awt.Color(0, 204, 204));

        jLabel32.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        jLabel32.setForeground(java.awt.Color.red);
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("LUBADA JAYA");

        jLabel33.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        jLabel33.setForeground(java.awt.Color.blue);
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("MARKET");

        jPanel13.setBackground(new java.awt.Color(0, 204, 204));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(java.awt.Color.lightGray, 1, true), "-- FILTER DATA --", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Agency FB", 1, 12))); // NOI18N

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

        txtCariBrg.setPlaceholder("NAMA BARANG");
        txtCariBrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariBrgActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbKat, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCariBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbKat, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCariBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblRecordBarang)
                .addContainerGap())
        );

        jScrollPane4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 14))); // NOI18N

        tbDataDistributor.setModel(new javax.swing.table.DefaultTableModel(
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
        tbDataDistributor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDataDistributorMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tbDataDistributor);

        lblRecordMember.setFont(new java.awt.Font("Arial Narrow", 1, 14)); // NOI18N
        lblRecordMember.setText("jLabel1");

        jPanel10.setBackground(new java.awt.Color(0, 204, 204));

        jPanel11.setBackground(new java.awt.Color(0, 204, 204));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "--FILTER DATA --"));

        txtCariDistributor.setPlaceholder("PENCARIAN :  ID / NAMA MEMBER");
        txtCariDistributor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariDistributorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtCariDistributor, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(txtCariDistributor, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        javax.swing.GroupLayout Daftar_DistributorLayout = new javax.swing.GroupLayout(Daftar_Distributor.getContentPane());
        Daftar_Distributor.getContentPane().setLayout(Daftar_DistributorLayout);
        Daftar_DistributorLayout.setHorizontalGroup(
            Daftar_DistributorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(Daftar_DistributorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Daftar_DistributorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 887, Short.MAX_VALUE)
                    .addGroup(Daftar_DistributorLayout.createSequentialGroup()
                        .addComponent(lblRecordMember)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        Daftar_DistributorLayout.setVerticalGroup(
            Daftar_DistributorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Daftar_DistributorLayout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblRecordMember))
        );

        jPanel15.setBackground(new java.awt.Color(0, 204, 204));

        jLabel45.setFont(new java.awt.Font("Agency FB", 1, 30)); // NOI18N
        jLabel45.setForeground(java.awt.Color.blue);
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setText("EXPORT TO EXCEL");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel45)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel45)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel16.setBorder(new javax.swing.border.LineBorder(java.awt.Color.lightGray, 1, true));

        jLabel46.setText("LOKASI FILE");

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

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel46)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtDir)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addComponent(btnSimpanBackup)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 202, Short.MAX_VALUE)
                        .addComponent(btnPilihLokasi)))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel46)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPilihLokasi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSimpanBackup, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("BACKUP", jPanel16);

        javax.swing.GroupLayout BackUp_RestoreLayout = new javax.swing.GroupLayout(BackUp_Restore.getContentPane());
        BackUp_Restore.getContentPane().setLayout(BackUp_RestoreLayout);
        BackUp_RestoreLayout.setHorizontalGroup(
            BackUp_RestoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(BackUp_RestoreLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );
        BackUp_RestoreLayout.setVerticalGroup(
            BackUp_RestoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BackUp_RestoreLayout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2))
        );

        setClosable(true);
        setTitle("TRANSAKSI PEMBELIAN-LUBADAJAYA MARKET");
        setPreferredSize(new java.awt.Dimension(1205, 585));

        panelPembelian.setPreferredSize(new java.awt.Dimension(1184, 560));

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "KLIK 2X --> UNTUK MELIHAT BARANG YANG DIBELI ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

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
        jScrollPane1.setViewportView(tbDataPembelian);

        lblRecordPembelian.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        lblRecordPembelian.setText("RECORD : 0");

        txtCari.setPlaceholder("NO.FAKTUR PEMBELIAN");
        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCariKeyTyped(evt);
            }
        });

        lblNoFak.setFont(new java.awt.Font("Agency FB", 3, 24)); // NOI18N
        lblNoFak.setForeground(java.awt.Color.red);
        lblNoFak.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        btnTambah.setFont(new java.awt.Font("Arial Narrow", 1, 14)); // NOI18N
        btnTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/insert.png"))); // NOI18N
        btnTambah.setText("TAMBAH");
        btnTambah.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnBackUp.setFont(new java.awt.Font("Arial Narrow", 1, 14)); // NOI18N
        btnBackUp.setText("EXPORT TO EXCEL");
        btnBackUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackUpActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel15.setText("CARI :");

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

        dtTglCari.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        dtTglCari.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dtTglCariPropertyChange(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel16.setText("S.D");

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

        javax.swing.GroupLayout panelPembelianLayout = new javax.swing.GroupLayout(panelPembelian);
        panelPembelian.setLayout(panelPembelianLayout);
        panelPembelianLayout.setHorizontalGroup(
            panelPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPembelianLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPembelianLayout.createSequentialGroup()
                        .addComponent(lblRecordPembelian)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dtTglCari, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16)
                        .addGap(2, 2, 2)
                        .addComponent(dtTglCariAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCariTgl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 602, Short.MAX_VALUE)
                        .addComponent(btnTambah))
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPembelianLayout.createSequentialGroup()
                        .addComponent(btnBackUp)
                        .addGap(0, 0, 0)
                        .addComponent(btnRefresh)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblNoFak, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        panelPembelianLayout.setVerticalGroup(
            panelPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPembelianLayout.createSequentialGroup()
                .addGroup(panelPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCari, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblNoFak, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnBackUp, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addGroup(panelPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblRecordPembelian, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCariTgl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dtTglCariAkhir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dtTglCari, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(btnTambah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("DATA PEMBELIAN", panelPembelian);

        panelDetPenjualan.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Agency FB", 1, 14))); // NOI18N
        panelDetPenjualan.setPreferredSize(new java.awt.Dimension(1184, 560));

        jScrollPane7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "DATA DETAIL PEMBELIAN BARANG", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

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
        txtCariDet.setPlaceholder("NO.FAKTUR PEMBELIAN");
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
                    .addComponent(txtCariDet, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addGroup(panelDetPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnCariTglDet, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(dtTglCariAkhirDet, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblDetail, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dtTglCariDet, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("DATA DETAIL PEMBELIAN", panelDetPenjualan);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCariBrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariBrgActionPerformed
        formTengah();
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

    private void tbDataPembelianMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDataPembelianMouseClicked
        if(evt.getClickCount()==2){
            IfrPembelian.setExtendedState(JFrame.MAXIMIZED_BOTH); 
            IfrPembelian.setVisible(true);
            setTabel_TransPembelian();
            vno_faktur = tbDataPembelian.getValueAt(tbDataPembelian.getSelectedRow(), 0).toString();
            getDataPembelian();  enableForm();
        }else if (evt.getClickCount()==1){
            vno_faktur = tbDataPembelian.getValueAt(tbDataPembelian.getSelectedRow(), 0).toString();
            setTabel_TransPembelian(); getDataPembelian(); 
        }
    }//GEN-LAST:event_tbDataPembelianMouseClicked

    private void txtCariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyTyped
        cariTransPembelian();
    }//GEN-LAST:event_txtCariKeyTyped

    private void tbDataDistributorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDataDistributorMouseClicked
        if(evt.getClickCount()==1){
            vid_dis = tbDataDistributor.getValueAt(tbDataDistributor.getSelectedRow(), 2).toString();
             tampilDistributor();
       } 
    }//GEN-LAST:event_tbDataDistributorMouseClicked

    private void txtDiskonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiskonActionPerformed
        hitungTotalAkhir();
    }//GEN-LAST:event_txtDiskonActionPerformed

    private void txtCariDistributorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariDistributorActionPerformed
        setTabelDistributor();
        showDataDistributor();
        Daftar_Distributor.setVisible(true);
        Daftar_Distributor.setSize(920, 532);
        Daftar_Distributor.setLocationRelativeTo(this);
    }//GEN-LAST:event_txtCariDistributorActionPerformed

    private void txtDaftarBrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDaftarBrgActionPerformed
        vid_barang=txtDaftarBrg.getText();
        getDataBarang();
    }//GEN-LAST:event_txtDaftarBrgActionPerformed

    private void txtIdDisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdDisActionPerformed
        vid_dis=txtIdDis.getText();
        tampilDistributor();
    }//GEN-LAST:event_txtIdDisActionPerformed

    private void btnDisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisActionPerformed
        Daftar_Distributor.setVisible(true);
        Daftar_Distributor.setSize(920, 532);
        Daftar_Distributor.setLocationRelativeTo(this);
        setTabelDistributor();
        showDataDistributor();
    }//GEN-LAST:event_btnDisActionPerformed

    private void btnDaftarBrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDaftarBrgActionPerformed
        formTengah();
        Daftar_Barang.setVisible(true);
        Daftar_Barang.setSize(1200, 600);
        listKat();
        setTabelBarang();
        showDataBarang();
    }//GEN-LAST:event_btnDaftarBrgActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if(txtTotal.getText().equals("0")){
            JOptionPane.showMessageDialog(null, "TIDAK ADA BARANG YANG DIBELI !",
                    "Error", JOptionPane.ERROR_MESSAGE); txtDaftarBrg.requestFocus();
        }else if(txtIdDis.getText().equals("")){
            JOptionPane.showMessageDialog(null, "KODE DISTRIBUTOR HARUS DIISI !",
                    "Error", JOptionPane.ERROR_MESSAGE); btnDis.requestFocus();
        }else if(Double.parseDouble(txtTotalAkhir.getText()) + Double.parseDouble(txtDiskon.getText()) != Double.parseDouble(txtTotal.getText()) ){
            JOptionPane.showMessageDialog(null, "TEKAN ENTER SETELAH PENGISIAN FIELD DISKON !",
                    "Error", JOptionPane.ERROR_MESSAGE); txtDiskon.requestFocus();
        }else{
            aksiSimpanPembelian();
            tblDetail_Pembelian.getDataVector().removeAllElements();
            tblDetail_Pembelian.fireTableDataChanged();  
            lblRecordTrans.setText("RECORD : "+tblDetail_Pembelian.getRowCount());  
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnDataPenjualanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataPenjualanActionPerformed
        int jumlah_baris = tblDetail_Pembelian.getRowCount();
        if(jumlah_baris > 0 && btnSimpan.getText().equals("SIMPAN")){
            int jawab = JOptionPane.showConfirmDialog(null, 
                "ANDA YAKIN INGIN KELUAR DARI TRANSAKSI INI ?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if(jawab==JOptionPane.YES_OPTION){
                aksiReset(); clearForm(); enableForm(); IfrPembelian.dispose(); showDataPembelian();
                btnSimpan.setVisible(true); btnSimpan.setText("SIMPAN"); btnUbah.setVisible(false); 
                btnDaftarBrg.setEnabled(true); 
            }
        }else if(jumlah_baris > 0 && btnSimpan.getText().equals("")){
            int jawab = JOptionPane.showConfirmDialog(null, 
                "ANDA YAKIN INGIN KELUAR DARI TRANSAKSI INI ?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if(jawab==JOptionPane.YES_OPTION){
                clearForm(); enableForm(); IfrPembelian.dispose(); showDataPembelian();
                btnSimpan.setVisible(true); btnSimpan.setText("SIMPAN"); btnUbah.setVisible(false); 
                btnDaftarBrg.setEnabled(true); 
            }
        }else{
            clearForm(); enableForm(); IfrPembelian.dispose(); showDataPembelian();
            btnSimpan.setVisible(true); btnSimpan.setText("SIMPAN"); btnUbah.setVisible(false); 
            btnDaftarBrg.setEnabled(true); 
        }
    }//GEN-LAST:event_btnDataPenjualanActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        if(btnSimpan.getText().equals("SIMPAN")){
            aksiReset(); clearForm(); enableForm(); btnSimpan.setVisible(true); 
            btnSimpan.setText("SIMPAN");  btnUbah.setVisible(false);txtTotalAkhir.setText(""); 
            tblDetail_Pembelian.getDataVector().removeAllElements();
            tblDetail_Pembelian.fireTableDataChanged(); autoNoFaktur();
            txtDaftarBrg.setEditable(true); btnDaftarBrg.setEnabled(true);
        }else{
            int jawab = JOptionPane.showConfirmDialog(null, 
                "TRANSAKSI BELUM DISIMPAN, APAKAH INGIN MENYIMPAN ? NO. FAKTUR : "+txtNoFaktur.getText(),
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if(jawab==JOptionPane.YES_OPTION){
            aksiUbahPembelian();
        }
        }lblRecordTrans.setText("RECORD : "+tblDetail_Pembelian.getRowCount());
}//GEN-LAST:event_btnBatalActionPerformed

    private void tbDetailPembelianMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDetailPembelianMouseClicked
        if(btnSimpan.getText().equals("")){
            if(evt.getClickCount()==1){
                vid_barang = tbDetailPembelian.getValueAt(tbDetailPembelian.getSelectedRow(), 1).toString();
                vjumlah_barang = (long) tbDetailPembelian.getValueAt(tbDetailPembelian.getSelectedRow(), 5);
                txtDaftarBrg.setText(vid_barang);
                txtJumlah.setText(String.valueOf(vjumlah_barang));
                getDetail();
                btnHapusBrg.setEnabled(true);
            }
        }else{
            if(evt.getClickCount()==1){
                vid_barang = tbDetailPembelian.getValueAt(tbDetailPembelian.getSelectedRow(), 1).toString();
                vjumlah_barang = (long) tbDetailPembelian.getValueAt(tbDetailPembelian.getSelectedRow(), 5);
                getDataBarang();
                txtJumlah.setText(String.valueOf(vjumlah_barang));
                hitungSubtotal();
                btnHapusBrg.setEnabled(true);
            }
        }
    }//GEN-LAST:event_tbDetailPembelianMouseClicked

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        if(txtIdDis.getText().equals("")){
            JOptionPane.showMessageDialog(null, "KODE DISTRIBUTOR HARUS DIISI !",
                    "Error", JOptionPane.ERROR_MESSAGE); btnDis.requestFocus();
        }else if(Long.parseLong(txtTotalAkhir.getText()) + Long.parseLong(txtDiskon.getText()) != Long.parseLong(txtTotal.getText()) ){
            JOptionPane.showMessageDialog(null, "TEKAN ENTER SETELAH PENGISIAN FIELD DISKON !",
                    "Error", JOptionPane.ERROR_MESSAGE); txtDiskon.requestFocus();
        }else{
            aksiUbahPembelian(); lblRecordTrans.setText("RECORD : "+tblDetail_Pembelian.getRowCount());
        }
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        btnSimpan.setVisible(true); btnSimpan.setText("SIMPAN"); btnUbah.setVisible(false);
        IfrPembelian.setExtendedState(JFrame.MAXIMIZED_BOTH);
        IfrPembelian.setVisible(true);
        autoNoFaktur(); clearForm(); enableForm(); txtTotalAkhir.setText(""); setTabel_TransPembelian(); btnDis.setEnabled(true); lblRecordTrans.setText("RECORD : "+tblDetail_Pembelian.getRowCount()); 
}//GEN-LAST:event_btnTambahActionPerformed

    private void txtDaftarBrgKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDaftarBrgKeyPressed
       if (evt.getKeyCode() == evt.VK_DOWN){
            txtHargaBeli.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtDiskon.requestFocus();
        }
    }//GEN-LAST:event_txtDaftarBrgKeyPressed

    private void txtDaftarBrgKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDaftarBrgKeyTyped
        if ( txtDaftarBrg.getText().length() == 13 ) {
        evt.consume();
        }
    }//GEN-LAST:event_txtDaftarBrgKeyTyped

    private void txtHargaBeliKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHargaBeliKeyTyped
        if ( txtHargaBeli.getText().length() == 9 ) {
        evt.consume();
        }
    }//GEN-LAST:event_txtHargaBeliKeyTyped

    private void txtHargaJualKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHargaJualKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            txtJumlah.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_ENTER){
            txtJumlah.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtHargaBeli.requestFocus();
        }
    }//GEN-LAST:event_txtHargaJualKeyPressed

    private void txtHargaJualKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHargaJualKeyTyped
        if ( txtHargaJual.getText().length() == 9 ) {
        evt.consume();
        }
    }//GEN-LAST:event_txtHargaJualKeyTyped

    private void txtHargaBeliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHargaBeliKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            txtHargaJual.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_ENTER){
            txtHargaJual.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtDaftarBrg.requestFocus();
        }
    }//GEN-LAST:event_txtHargaBeliKeyPressed

    private void txtJumlahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            txtDiskon.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtHargaJual.requestFocus();
        }
    }//GEN-LAST:event_txtJumlahKeyPressed

    private void txtJumlahKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyTyped
        if ( txtJumlah.getText().length() == 2 ) {
        evt.consume();
        }
    }//GEN-LAST:event_txtJumlahKeyTyped

    private void txtDiskonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiskonKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            txtDaftarBrg.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtHargaJual.requestFocus();
        }
    }//GEN-LAST:event_txtDiskonKeyPressed

    private void txtDiskonKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiskonKeyTyped
        if ( txtDiskon.getText().length() == 10 ) {
        evt.consume();
        }
    }//GEN-LAST:event_txtDiskonKeyTyped

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        clearForm(); showDataPembelian(); showDetail();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void dtTglCariPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dtTglCariPropertyChange
        dtTglCariAkhir.setDate(dtTglCari.getDate());
        txtCari.setText("");
    }//GEN-LAST:event_dtTglCariPropertyChange

    private void dtTglCariAkhirPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dtTglCariAkhirPropertyChange
        tanggal_Pencarian();
    }//GEN-LAST:event_dtTglCariAkhirPropertyChange

    private void btnCariTglActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariTglActionPerformed
        cariTransPembelian();
    }//GEN-LAST:event_btnCariTglActionPerformed

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

    private void cmbKatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbKatActionPerformed
        txtCariBrg.setText(""); 
        setTabelBarang(); showDataBarang();
    }//GEN-LAST:event_cmbKatActionPerformed

    private void txtnoinvoiceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnoinvoiceKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            txtIdDis.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_ENTER){
            txtIdDis.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtDiskon.requestFocus();
        }
    }//GEN-LAST:event_txtnoinvoiceKeyPressed

    private void txtnoinvoiceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnoinvoiceKeyTyped
        if ( txtnoinvoice.getText().length() == 20 ) {
        evt.consume();
        }
    }//GEN-LAST:event_txtnoinvoiceKeyTyped

    private void btnTambahBrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahBrgActionPerformed
        tambahBarang();
    }//GEN-LAST:event_btnTambahBrgActionPerformed

    private void btnHapusBrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusBrgActionPerformed
        hapusBarang();
    }//GEN-LAST:event_btnHapusBrgActionPerformed

    private void txtJumlahKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyReleased
        if(txtJumlah.getText().equals("")){
           txtJumlah.setText("0"); txtSubtotal.setText("0"); 
           txtSubtotal.requestFocus(); txtJumlah.requestFocus();
        }else{
            hitungSubtotal();
        }
    }//GEN-LAST:event_txtJumlahKeyReleased

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
            toExcel(tbDataPembelian, new File(txtDir.getText()));
        }
    }//GEN-LAST:event_btnSimpanBackupActionPerformed

    private void btnBackUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackUpActionPerformed
        BackUp_Restore.setVisible(true);
        BackUp_Restore.setSize(500, 300);
        BackUp_Restore.setLocationRelativeTo(this);
    }//GEN-LAST:event_btnBackUpActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFrame BackUp_Restore;
    private javax.swing.JDialog Daftar_Barang;
    private javax.swing.JDialog Daftar_Distributor;
    private javax.swing.JFrame IfrPembelian;
    private javax.swing.JButton btnBackUp;
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnCariTgl;
    private javax.swing.JButton btnCariTglDet;
    private javax.swing.JButton btnDaftarBrg;
    private javax.swing.JButton btnDataPenjualan;
    private javax.swing.JButton btnDis;
    private javax.swing.JButton btnHapusBrg;
    private javax.swing.JButton btnPilihLokasi;
    private javax.swing.JButton btnRefresh;
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
    private com.toedter.calendar.JDateChooser dtTglPembelian;
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
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JLabel lblDetail;
    private javax.swing.JLabel lblJam;
    private javax.swing.JLabel lblNoFak;
    private javax.swing.JLabel lblRecordBarang;
    private javax.swing.JLabel lblRecordMember;
    private javax.swing.JLabel lblRecordPembelian;
    private javax.swing.JLabel lblRecordTrans;
    private javax.swing.JLabel lblUser;
    private javax.swing.JLabel lblUsersistem;
    private javax.swing.JPanel panelDetPenjualan;
    private javax.swing.JPanel panelPembelian;
    private javax.swing.JTable tbDataBarang;
    private javax.swing.JTable tbDataDistributor;
    private javax.swing.JTable tbDataPembelian;
    private javax.swing.JTable tbDetail;
    private javax.swing.JTable tbDetailPembelian;
    private Tool.Custom_TextField txtCari;
    private Tool.Custom_TextField txtCariBrg;
    private Tool.Custom_TextField txtCariDet;
    private Tool.Custom_TextField txtCariDistributor;
    private Tool.Custom_TextField txtDaftarBrg;
    private javax.swing.JTextArea txtDes;
    private javax.swing.JTextField txtDir;
    private Tool.NumberTextField txtDiskon;
    private Tool.NumberTextField txtHargaBeli;
    private Tool.NumberTextField txtHargaJual;
    private Tool.Custom_TextField txtIdDis;
    private Tool.NumberTextField txtJumlah;
    private Tool.Custom_TextField txtNamaBrg;
    private Tool.Custom_TextField txtNamaDis;
    private Tool.Custom_TextField txtNoFaktur;
    private Tool.NumberTextField txtStok;
    private Tool.NumberTextField txtSubtotal;
    private Tool.NumberTextField txtTotal;
    private Tool.NumberTextField txtTotalAkhir;
    private Tool.Custom_TextField txtnoinvoice;
    // End of variables declaration//GEN-END:variables
}
