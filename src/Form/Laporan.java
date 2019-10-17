package Form;

import Tool.KoneksiDB;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class Laporan extends javax.swing.JInternalFrame {

    KoneksiDB getCnn = new KoneksiDB();
    Connection Conn;
    String sqlselect;
    String vkd_kat, vnm_kat, vtglAwal, vtglAkhir;
    String id = FrMenu.getU_id();
    
    public Laporan() {
        initComponents();
        Locale locale =new Locale ("id", "ID");
        locale.setDefault(locale);
        listKategori();
        clearForm();
    }
    private void clearForm(){
        dtTglAwal.setDate(new Date());
        dtTglAkhir.setDate(new Date());
    }
    String[] KeyKategori;
    private void listKategori(){
        try{
            Conn = null;
            Conn = getCnn.getConnection();
            sqlselect = "SELECT * FROM tb_kategori order by nama_kategori asc";
            Statement stat = Conn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            cmbKategori.removeAllItems();
            cmbKategori.repaint();
            cmbKategori.addItem("-- PILIH KATEGORI --");
            int i = 1;
            while(res.next()){
                cmbKategori.addItem(res.getString("nama_kategori"));
                i++;
            }
            res.first();
            KeyKategori = new String[i+1];
            for(Integer x =1;x < i;x++){
                KeyKategori[x] = res.getString(1);
                res.next();
            }
        } catch (SQLException ex){
            JOptionPane.showMessageDialog(this, "Error Method listKategori " +ex);
        }
    }
    private void cetakAll(){
        String pth = System.getProperty("user.dir") + "/Laporan/LapSemuaBarang.jrxml";
        try{
            Map<String, Object> parameters = new HashMap<>();
            Conn = null;
            Conn = getCnn.getConnection();
            JasperReport jrpt = JasperCompileManager.compileReport(pth);
            JasperPrint jprint = JasperFillManager.fillReport(jrpt, parameters, Conn);
            
            JasperViewer.viewReport(jprint, false);
            
        }catch (SQLException | JRException ex){
            JOptionPane.showConfirmDialog(null, "Error method cetakAll : " + ex,
            "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void cetakPerKategori(){
        vkd_kat = KeyKategori[cmbKategori.getSelectedIndex()];
        String pth = System.getProperty("user.dir") + "/Laporan/LapPerKategori.jrxml";
        try{
            Map<String, Object> parameters = new HashMap<>();
            Conn = null;
            Conn = getCnn.getConnection();
            parameters.put("parKategori", vkd_kat);
            JasperReport jrpt = JasperCompileManager.compileReport(pth);
            JasperPrint jprint = JasperFillManager.fillReport(jrpt, parameters, Conn);
            
            JasperViewer.viewReport(jprint, false);
            
        }catch (SQLException | JRException ex){
            JOptionPane.showConfirmDialog(null, "Error method cetakPerKategori : " + ex,
            "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void tanggal(){
        try{
        if(dtTglAwal.getDate() !=null && dtTglAkhir.getDate()!=null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            vtglAwal=format.format(dtTglAwal.getDate());
            vtglAkhir=format.format(dtTglAkhir.getDate());
            Date Tanggal1 = format.parse(vtglAwal);
            Date Tanggal2 = format.parse(vtglAkhir);
            long Hari1 = Tanggal1.getTime();
            long Hari2 = Tanggal2.getTime();
            long diff = Hari2 - Hari1;
            long Lama = diff / (24 * 60 * 60 * 1000);
            if(Lama<0){
                JOptionPane.showMessageDialog(null, "TANGGAL AKHIR TIDAK BISA KURANG DARI TANGGAL AWAL ! ");
                clearForm();
            }
        }
        } catch(Exception e){
            
        }
    }
    private void cetakLapPembelianPerTgl(){
        String pth = System.getProperty("user.dir") + "/Laporan/lapPembelian.jrxml";
        try{
            Map<String, Object> parameters = new HashMap<>();
            Conn = null;
            Conn = getCnn.getConnection();
            parameters.put("parTglAwal",dtTglAwal.getDate());
            parameters.put("parTglAkhir",dtTglAkhir.getDate());
            parameters.put("parUser",id);
            JasperReport jrpt = JasperCompileManager.compileReport(pth);
            JasperPrint jprint = JasperFillManager.fillReport(jrpt, parameters, Conn);
            JasperViewer.viewReport(jprint, false);
        }catch (SQLException | JRException ex){
            JOptionPane.showConfirmDialog(null, "Error method cetakLapPembelianPerTgl : " + ex,
            "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void cetakLapPenjualanPerTgl(){
        String pth = System.getProperty("user.dir") + "/Laporan/lapPenjualan.jrxml";
        try{
            Map<String, Object> parameters = new HashMap<>();
            Conn = null;
            Conn = getCnn.getConnection();
            parameters.put("parAwal",dtTglAwal.getDate());
            parameters.put("parAkhir",dtTglAkhir.getDate());
            JasperReport jrpt = JasperCompileManager.compileReport(pth);
            JasperPrint jprint = JasperFillManager.fillReport(jrpt, parameters, Conn);
            JasperViewer.viewReport(jprint, false);
        }catch (SQLException | JRException ex){
            JOptionPane.showConfirmDialog(null, "Error method cetakLaporanperTanggal : " + ex,
            "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void cetakLapDetPembelian(){
        String pth = System.getProperty("user.dir") + "/Laporan/lapDetailPembelian.jrxml";
        try{
            Map<String, Object> parameters = new HashMap<>();
            Conn = null;
            Conn = getCnn.getConnection();
            parameters.put("parAwal",dtTglAwal.getDate());
            parameters.put("parAkhir",dtTglAkhir.getDate());
            parameters.put("parUser",id);
            JasperReport jrpt = JasperCompileManager.compileReport(pth);
            JasperPrint jprint = JasperFillManager.fillReport(jrpt, parameters, Conn);
            JasperViewer.viewReport(jprint, false);
        }catch (SQLException | JRException ex){
            JOptionPane.showConfirmDialog(null, "Error method cetakLaporanperTanggal : " + ex,
            "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void cetakLapDetPenjualan(){
        String pth = System.getProperty("user.dir") + "/Laporan/lapDetailPenjualan.jrxml";
        try{
            Map<String, Object> parameters = new HashMap<>();
            Conn = null;
            Conn = getCnn.getConnection();
            parameters.put("parAwal",dtTglAwal.getDate());
            parameters.put("parAkhir",dtTglAkhir.getDate());
            JasperReport jrpt = JasperCompileManager.compileReport(pth);
            JasperPrint jprint = JasperFillManager.fillReport(jrpt, parameters, Conn);
            JasperViewer.viewReport(jprint, false);
        }catch (SQLException | JRException ex){
            JOptionPane.showConfirmDialog(null, "Error method cetakLaporanperTanggal : " + ex,
            "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void cetakLapReturPembelian(){
        String pth = System.getProperty("user.dir") + "/Laporan/lapReturPembelian.jrxml";
        try{
            Map<String, Object> parameters = new HashMap<>();
            Conn = null;
            Conn = getCnn.getConnection();
            parameters.put("parAwal",dtTglAwal.getDate());
            parameters.put("parAkhir",dtTglAkhir.getDate());
            parameters.put("parUser",id);
            JasperReport jrpt = JasperCompileManager.compileReport(pth);
            JasperPrint jprint = JasperFillManager.fillReport(jrpt, parameters, Conn);
            JasperViewer.viewReport(jprint, false);
        }catch (SQLException | JRException ex){
            JOptionPane.showConfirmDialog(null, "Error method cetakLapReturPembelian : " + ex,
            "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void cetakLapReturPenjualan(){
        String pth = System.getProperty("user.dir") + "/Laporan/lapReturPenjualan.jrxml";
        try{
            Map<String, Object> parameters = new HashMap<>();
            Conn = null;
            Conn = getCnn.getConnection();
            parameters.put("parAwal",dtTglAwal.getDate());
            parameters.put("parAkhir",dtTglAkhir.getDate());
            JasperReport jrpt = JasperCompileManager.compileReport(pth);
            JasperPrint jprint = JasperFillManager.fillReport(jrpt, parameters, Conn);
            JasperViewer.viewReport(jprint, false);
        }catch (SQLException | JRException ex){
            JOptionPane.showConfirmDialog(null, "Error method cetakLapReturPembelian : " + ex,
            "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void cetakLapLabaRugi(){
        String pth = System.getProperty("user.dir") + "/Laporan/lapLabaRugi.jrxml";
        try{
            Map<String, Object> parameters = new HashMap<>();
            Conn = null;
            Conn = getCnn.getConnection();
            parameters.put("parAwal",dtTglAwal.getDate());
            parameters.put("parAkhir",dtTglAkhir.getDate());
            JasperReport jrpt = JasperCompileManager.compileReport(pth);
            JasperPrint jprint = JasperFillManager.fillReport(jrpt, parameters, Conn);
            
            JasperViewer.viewReport(jprint, false);
            
        }catch (SQLException | JRException ex){
            JOptionPane.showConfirmDialog(null, "Error method lapLabaRugi : " + ex,
            "Informasi", JOptionPane.INFORMATION_MESSAGE);
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnCetakAll = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnCetakPerKategori = new javax.swing.JButton();
        cmbKategori = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btnBeli = new javax.swing.JButton();
        btnJual = new javax.swing.JButton();
        btnRinciBeli = new javax.swing.JButton();
        btnRinciJual = new javax.swing.JButton();
        btnReturBeli = new javax.swing.JButton();
        btnReturJual = new javax.swing.JButton();
        btnLaba = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        dtTglAwal = new com.toedter.calendar.JDateChooser();
        dtTglAkhir = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();

        setClosable(true);
        setTitle("CETAK LAPORAN - SILUB");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true), "Cetak (+)"));
        jPanel3.setOpaque(false);

        jLabel1.setText("CETAK SEMUA BARANG");

        btnCetakAll.setText("CETAK ");
        btnCetakAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 206, Short.MAX_VALUE)
                .addComponent(btnCetakAll)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCetakAll)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true), "Cetak Per Kategori (+)"));
        jPanel4.setOpaque(false);

        btnCetakPerKategori.setText("CETAK ");
        btnCetakPerKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakPerKategoriActionPerformed(evt);
            }
        });

        cmbKategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
                .addComponent(btnCetakPerKategori)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCetakPerKategori)
                    .addComponent(cmbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(94, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Master Barang", jPanel1);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true), "Pilihan Laporan : "));
        jPanel5.setOpaque(false);

        btnBeli.setText("PEMBELIAN BARANG");
        btnBeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBeliActionPerformed(evt);
            }
        });

        btnJual.setText("PENJUALAN BARANG");
        btnJual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJualActionPerformed(evt);
            }
        });

        btnRinciBeli.setText("PERINCIAN PEMBELIAN BARANG");
        btnRinciBeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRinciBeliActionPerformed(evt);
            }
        });

        btnRinciJual.setText("PERINCIAN PENJUALAN BARANG");
        btnRinciJual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRinciJualActionPerformed(evt);
            }
        });

        btnReturBeli.setText("RETUR PEMBELIAN");
        btnReturBeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReturBeliActionPerformed(evt);
            }
        });

        btnReturJual.setText("RETUR PENJUALAN");
        btnReturJual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReturJualActionPerformed(evt);
            }
        });

        btnLaba.setText("LAPORAN LABA / RUGI ");
        btnLaba.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLabaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnReturBeli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnBeli, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnRinciBeli, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnRinciJual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnJual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnReturJual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(btnLaba, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBeli)
                    .addComponent(btnJual))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRinciBeli)
                    .addComponent(btnRinciJual))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReturBeli)
                    .addComponent(btnReturJual))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLaba))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true), "Periode : "));
        jPanel7.setOpaque(false);

        dtTglAwal.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dtTglAwalPropertyChange(evt);
            }
        });

        dtTglAkhir.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dtTglAkhirPropertyChange(evt);
            }
        });

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("S.D");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dtTglAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dtTglAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dtTglAkhir, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(dtTglAwal, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Transaksi", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCetakAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakAllActionPerformed
        cetakAll();
    }//GEN-LAST:event_btnCetakAllActionPerformed

    private void btnCetakPerKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakPerKategoriActionPerformed
        if(cmbKategori.getSelectedIndex()<=0){
            JOptionPane.showMessageDialog(null, "KATEGORI BELUM DIPILIH !",
                "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }else {
            cetakPerKategori();
        }
    }//GEN-LAST:event_btnCetakPerKategoriActionPerformed

    private void btnBeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBeliActionPerformed
        cetakLapPembelianPerTgl();
    }//GEN-LAST:event_btnBeliActionPerformed

    private void btnJualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJualActionPerformed
        cetakLapPenjualanPerTgl();
    }//GEN-LAST:event_btnJualActionPerformed

    private void btnRinciBeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRinciBeliActionPerformed
        cetakLapDetPembelian();
    }//GEN-LAST:event_btnRinciBeliActionPerformed

    private void btnRinciJualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRinciJualActionPerformed
        cetakLapDetPenjualan();
    }//GEN-LAST:event_btnRinciJualActionPerformed

    private void btnLabaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLabaActionPerformed
        cetakLapLabaRugi();
    }//GEN-LAST:event_btnLabaActionPerformed

    private void dtTglAwalPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dtTglAwalPropertyChange
        dtTglAkhir.setDate(dtTglAwal.getDate());
    }//GEN-LAST:event_dtTglAwalPropertyChange

    private void dtTglAkhirPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dtTglAkhirPropertyChange
        tanggal();
    }//GEN-LAST:event_dtTglAkhirPropertyChange

    private void btnReturBeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReturBeliActionPerformed
        cetakLapReturPembelian();
    }//GEN-LAST:event_btnReturBeliActionPerformed

    private void btnReturJualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReturJualActionPerformed
        cetakLapReturPenjualan();
    }//GEN-LAST:event_btnReturJualActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBeli;
    private javax.swing.JButton btnCetakAll;
    private javax.swing.JButton btnCetakPerKategori;
    private javax.swing.JButton btnJual;
    private javax.swing.JButton btnLaba;
    private javax.swing.JButton btnReturBeli;
    private javax.swing.JButton btnReturJual;
    private javax.swing.JButton btnRinciBeli;
    private javax.swing.JButton btnRinciJual;
    private javax.swing.JComboBox<String> cmbKategori;
    private com.toedter.calendar.JDateChooser dtTglAkhir;
    private com.toedter.calendar.JDateChooser dtTglAwal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
