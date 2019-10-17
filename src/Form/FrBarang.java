package Form;

import Tool.KoneksiDB;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FrBarang extends javax.swing.JInternalFrame {

    KoneksiDB getCnn = new KoneksiDB();
    Connection _Cnn;
    
    String sqlselect, sqlinsert, sqldelete;
    String vid_kat, vnm_kat, vid_barang, vnm_barang, vdeskripsi;
    Double vhrg_beli, vhrg_jual;
    int vstok;
    
    DefaultTableModel tblBarang;
    
    public FrBarang() {
        initComponents();
        
        setTabelBarang();
        showDataBarang();
        listKategori();
        clearForm();
        disableForm();
    }
    
    private void clearForm(){
        txtKdBarang.setText("");
        txtNmBarang.setText("");
        cmbKategori.setSelectedIndex(0);
        txtDeskripsi.setText("");
        txtHargaBeli.setText("");
        txtHargaJual.setText("");
        txtStok.setText("");
        txtKdBarang.requestFocus();
        btnSimpan.setText("SIMPAN");
    }
    
    private void disableForm(){
        txtKdBarang.setEnabled(false);
        txtNmBarang.setEnabled(false);
        cmbKategori.setEnabled(false);
        txtDeskripsi.setEnabled(false);
        txtHargaBeli.setEnabled(false);
        txtHargaJual.setEnabled(false);
        txtStok.setEnabled(false);
        panelCrud.setVisible(false);
    }
    
    private void enableForm(){
        txtKdBarang.setEnabled(true);
        txtNmBarang.setEnabled(true);
        cmbKategori.setEnabled(true);
        txtDeskripsi.setEnabled(true);
        txtHargaBeli.setEnabled(true);
        txtHargaJual.setEnabled(true);
        txtStok.setEnabled(true);
        panelCrud.setVisible(true);
        btnSimpan.setText("SIMPAN");
    }
    
    private void setTabelBarang(){
        String[]kolom1 = {"KODE BARANG", "KATEGORI", "NAMA BARANG", "HARGA BELI", "HARGA JUAL", "STOK"};
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
        tbDataBarang.getColumnModel().getColumn(0).setPreferredWidth(150);
        tbDataBarang.getColumnModel().getColumn(1).setPreferredWidth(200);
        tbDataBarang.getColumnModel().getColumn(2).setPreferredWidth(200);
        tbDataBarang.getColumnModel().getColumn(3).setPreferredWidth(200);
        tbDataBarang.getColumnModel().getColumn(4).setPreferredWidth(200);
        tbDataBarang.getColumnModel().getColumn(5).setPreferredWidth(150);
        
    }
    
    private void clearTabelBarang(){
        int row  = tblBarang.getRowCount();
        for(int i = 0; i <row; i++){
            tblBarang.removeRow(0);
        }
    } 
    
    private void showDataBarang(){
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            clearTabelBarang();
            sqlselect = " select a.id_barang, b.nama_kategori, a.nama_barang, a.harga_beli, a.harga_jual, "
                    + " a.stok from tb_barang a, tb_kategori b "
                    + " where a.id_kategori=b.id_kategori order by nama_barang asc ";
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);// mengeksekusi query select
            while(res.next()){
                vid_barang = res.getString("id_barang");
                vnm_kat = res.getString("nama_kategori");
                vnm_barang = res.getString("nama_barang");
                vhrg_beli = res.getDouble("harga_beli");
                vhrg_jual = res.getDouble("harga_jual");
                vstok = res.getInt("stok");
                Object[]data = {vid_barang, vnm_kat, vnm_barang,  
                                vhrg_beli, vhrg_jual, vstok};
                tblBarang.addRow(data);
            }
            lblRecord.setText("RECORD : "+tbDataBarang.getRowCount()); 
            btnHapus.setEnabled(false);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method showDataBarang() : "+ex);
        }
    }
    
    private void getDataBarang(){
        enableForm();
        try{
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select * from tb_barang a, tb_kategori b"
                    + " where a.id_kategori=b.id_kategori and id_barang='"+vid_barang+"'"
                    + " order by a.nama_barang asc ";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            if(res.first()){
                txtKdBarang.setText(res.getString("id_barang"));
                cmbKategori.setSelectedItem(res.getString("nama_kategori"));
                txtDeskripsi.setText(res.getString("deskripsi"));
                txtNmBarang.setText(res.getString("nama_barang"));
                txtHargaBeli.setText(res.getString("harga_beli"));
                txtHargaJual.setText(res.getString("harga_jual"));
                txtStok.setText(res.getString("stok"));
                txtNmBarang.requestFocus();
            }
            }catch (SQLException ex){
                JOptionPane.showMessageDialog(this, "Error mthod getDataBarang : " + ex);
            }
    }
    private void aksiSimpan(){
        vid_barang = txtKdBarang.getText();
        vid_kat = KeyKategori[cmbKategori.getSelectedIndex()];
        vnm_barang = txtNmBarang.getText();
        vhrg_beli = Double.valueOf(txtHargaBeli.getText());
        vhrg_jual = Double.valueOf(txtHargaJual.getText());
        vstok = Integer.parseInt(txtStok.getText());
        vdeskripsi = txtDeskripsi.getText();
        if(btnSimpan.getText().equals("SIMPAN")){
            sqlinsert = "insert into tb_barang values('"+vid_barang+"','"+vid_kat+"', '"+vnm_barang+"', "
                    + " '"+vhrg_beli+"', '"+vhrg_jual+"', '"+vstok+"', '"+vdeskripsi+"')";
            JOptionPane.showMessageDialog(null, "DATA BERHASIL DISIMPAN !");
        }else{
            sqlinsert = "update tb_barang set id_kategori='"+vid_kat+"', nama_barang='"+vnm_barang+"', "
                    + " harga_beli='"+vhrg_beli+"', harga_jual='"+vhrg_jual+"',  stok='"+vstok+"', deskripsi='"+vdeskripsi+"' "
                    + " where id_barang='"+vid_barang+"'";
            JOptionPane.showMessageDialog(null, "DATA BERHASIL DIUBAH !");
        }
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            Statement state = _Cnn.createStatement();
            state.executeUpdate(sqlinsert);
            clearForm(); enableForm(); showDataBarang();
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method aksiSimpan() : "+ex);
        }
    }
    private void aksiCek(){
        vid_barang = txtKdBarang.getText();
        try{
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select * from tb_barang where id_barang='"+vid_barang+"' ";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            res.next();
            if(res.getRow()!=0){
                txtKdBarang.setText(""); txtKdBarang.requestFocus();
            }
            }catch (SQLException ex){
                JOptionPane.showMessageDialog(this, "Error mthod CEK : " + ex);
            }
    }
    private void aksiHapus(){
        int jawab = JOptionPane.showConfirmDialog(null, 
                "ANDA YAKIN INGIN MENGHAPUS DATA INI ? ID. BARANG : "+vid_barang,
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if(jawab==JOptionPane.YES_OPTION){
            try{
                _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
                _Cnn = getCnn.getConnection();// membuka koneksi
                sqldelete = "delete from tb_barang where id_barang = '"+vid_barang+"'";
                Statement state = _Cnn.createStatement();
                state.executeUpdate(sqldelete);
                JOptionPane.showMessageDialog(null, "DATA BERHASIL DIHAPUS !");
                showDataBarang(); clearForm();
            }catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "DATA SEDANG DIPAKAI ! DATA TIDAK BISA DIHAPUS !");
            }
        }
    }
    private void cariBarang(){
        try{
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            clearTabelBarang();
            sqlselect = "select * from tb_barang a, tb_kategori b"
                    + " where a.id_kategori=b.id_kategori and nama_barang like '%"+txtCari.getText()+"%' "
                    + " order by nama_barang asc";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            while(res.next()){
                vid_barang = res.getString("id_barang");
                vnm_kat = res.getString("nama_kategori");
                vnm_barang = res.getString("nama_barang");
                vhrg_beli = res.getDouble("harga_beli");
                vhrg_jual = res.getDouble("harga_jual");
                vstok = res.getInt("stok");
                Object[]data = {vid_barang, vnm_kat, vnm_barang, vhrg_beli, vhrg_jual, vstok };
                tblBarang.addRow(data);
            }
            lblRecord.setText("RECORD : "+tblBarang.getRowCount());
            btnHapus.setEnabled(false);
            } catch (SQLException ex){
                JOptionPane.showMessageDialog(this, "Error Method cariBarang : "+ ex);
            }
    }
    
    String[] KeyKategori;
    private void listKategori(){
        try{
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "SELECT * FROM tb_kategori order by nama_kategori asc";
            Statement stat = _Cnn.createStatement();
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtCari = new Tool.Custom_TextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbDataBarang = new javax.swing.JTable();
        btnHapus = new Tool.Custom_Button();
        btnTambah = new Tool.Custom_Button();
        lblRecord = new javax.swing.JLabel();
        panelCrud = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        txtKdBarang = new Tool.Custom_TextField();
        txtNmBarang = new Tool.Custom_TextField();
        cmbKategori = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtHargaBeli = new Tool.NumberTextField();
        txtHargaJual = new Tool.NumberTextField();
        txtStok = new Tool.NumberTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtDeskripsi = new javax.swing.JTextArea();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        btnBatal = new Tool.Custom_Button();
        btnSimpan = new Tool.Custom_Button();
        jLabel2 = new javax.swing.JLabel();

        setClosable(true);
        setPreferredSize(new java.awt.Dimension(1205, 585));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "(+) DATA BARANG", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 12))); // NOI18N

        txtCari.setPlaceholder("PENCARIAN : NAMA BARANG");
        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCariKeyTyped(evt);
            }
        });

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "KLIK --> MENGUBAH / MENGHAPUS DATA", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 12))); // NOI18N

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
        jScrollPane1.setViewportView(tbDataBarang);

        btnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/btn_delete.png"))); // NOI18N
        btnHapus.setText("HAPUS");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/insert.png"))); // NOI18N
        btnTambah.setText("TAMBAH");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        lblRecord.setFont(new java.awt.Font("Arial Narrow", 1, 12)); // NOI18N
        lblRecord.setText("RECORD : 0");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 779, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lblRecord)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRecord))
                .addContainerGap())
        );

        panelCrud.setBackground(new java.awt.Color(0, 0, 0));

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/2 - Master.png"))); // NOI18N

        jLabel10.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("MASTER DATA BARANG");

        jPanel5.setBackground(new java.awt.Color(0, 0, 0));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "(+)", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 12))); // NOI18N

        txtKdBarang.setBackground(new java.awt.Color(240, 240, 240));
        txtKdBarang.setForeground(java.awt.Color.red);
        txtKdBarang.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtKdBarang.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        txtKdBarang.setPlaceholder("KODE BARANG");
        txtKdBarang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKdBarangKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKdBarangKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtKdBarangKeyTyped(evt);
            }
        });

        txtNmBarang.setPlaceholder("NAMA BARANG");
        txtNmBarang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNmBarangKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNmBarangKeyTyped(evt);
            }
        });

        cmbKategori.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmbKategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- PILIH KATEGORI BARANG --" }));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("HARGA BELI");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("HARGA JUAL");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("STOK");

        txtHargaBeli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtHargaBeliKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHargaBeliKeyTyped(evt);
            }
        });

        txtHargaJual.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtHargaJualKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHargaJualKeyTyped(evt);
            }
        });

        txtStok.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtStok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtStokKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStokKeyTyped(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("DESKRIPSI");

        txtDeskripsi.setColumns(20);
        txtDeskripsi.setRows(5);
        txtDeskripsi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDeskripsiKeyPressed(evt);
            }
        });
        jScrollPane4.setViewportView(txtDeskripsi);

        jLabel1.setFont(new java.awt.Font("Agency FB", 3, 14)); // NOI18N
        jLabel1.setForeground(java.awt.Color.red);
        jLabel1.setText("* TEKAN ENTER SETIAP  SPESIFIKASI !");
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNmBarang, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbKategori, javax.swing.GroupLayout.Alignment.LEADING, 0, 329, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel14))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtHargaJual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtHargaBeli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(txtStok, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtKdBarang, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(txtKdBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(txtNmBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(cmbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                .addGap(17, 17, 17)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHargaBeli, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtStok, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        btnBatal.setForeground(new java.awt.Color(255, 255, 255));
        btnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/refresh_1.png"))); // NOI18N
        btnBatal.setText("BATAL");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });

        btnSimpan.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/save_blue.png"))); // NOI18N
        btnSimpan.setText("SIMPAN");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Agency FB", 3, 14)); // NOI18N
        jLabel2.setForeground(java.awt.Color.red);
        jLabel2.setText("* DISARANKAN MENGGUNKAN HURUF KAPITAL !");
        jLabel2.setOpaque(true);

        javax.swing.GroupLayout panelCrudLayout = new javax.swing.GroupLayout(panelCrud);
        panelCrud.setLayout(panelCrudLayout);
        panelCrudLayout.setHorizontalGroup(
            panelCrudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCrudLayout.createSequentialGroup()
                .addGroup(panelCrudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelCrudLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelCrudLayout.createSequentialGroup()
                        .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 11, Short.MAX_VALUE))
            .addGroup(panelCrudLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelCrudLayout.setVerticalGroup(
            panelCrudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCrudLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCrudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCrudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(panelCrud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(panelCrud, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyTyped
        cariBarang();
    }//GEN-LAST:event_txtCariKeyTyped

    private void tbDataBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDataBarangMouseClicked
        if(evt.getClickCount()==2){
            vid_barang = tbDataBarang.getValueAt(tbDataBarang.getSelectedRow(), 0).toString();
            btnHapus.setEnabled(true);
            getDataBarang();
            btnSimpan.setText("UBAH DATA");
       }
    }//GEN-LAST:event_tbDataBarangMouseClicked

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        enableForm(); clearForm();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        aksiHapus();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        clearForm(); disableForm();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if(txtKdBarang.getText().equals("")){
            JOptionPane.showMessageDialog(null, "KODE BARANG HARUS DIISI !",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            txtKdBarang.requestFocus();
        }else if(txtNmBarang.getText().equals("")){
            JOptionPane.showMessageDialog(null, "NAMA BARANG HARUS DIISI !",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            txtNmBarang.requestFocus();
        }else if(cmbKategori.getSelectedIndex()<=0){
            JOptionPane.showMessageDialog(null, "KATEGORI BELUM DIPILIH !",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            cmbKategori.requestFocus();
        }else{
            aksiSimpan(); btnSimpan.setText("SIMPAN");
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void txtNmBarangKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNmBarangKeyTyped
        if ( txtNmBarang.getText().length() == 40 ) {
        evt.consume();
    }
    }//GEN-LAST:event_txtNmBarangKeyTyped

    private void txtHargaBeliKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHargaBeliKeyTyped
        if ( txtHargaBeli.getText().length() == 9 ) {
        evt.consume();
    }
    }//GEN-LAST:event_txtHargaBeliKeyTyped

    private void txtHargaJualKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHargaJualKeyTyped
        if ( txtHargaJual.getText().length() == 9 ) {
        evt.consume();
        }
    }//GEN-LAST:event_txtHargaJualKeyTyped

    private void txtStokKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStokKeyTyped
        if ( txtStok.getText().length() == 3 ) {
        evt.consume();
        }
    }//GEN-LAST:event_txtStokKeyTyped

    private void txtNmBarangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNmBarangKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN || evt.getKeyCode() == evt.VK_ENTER){
            txtDeskripsi.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtStok.requestFocus();
        }
    }//GEN-LAST:event_txtNmBarangKeyPressed

    private void txtDeskripsiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDeskripsiKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN || evt.getKeyCode() == evt.VK_ENTER){
            txtHargaBeli.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtNmBarang.requestFocus();
        }
    }//GEN-LAST:event_txtDeskripsiKeyPressed

    private void txtHargaBeliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHargaBeliKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN || evt.getKeyCode() == evt.VK_ENTER){
            txtHargaJual.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtDeskripsi.requestFocus();
        }
    }//GEN-LAST:event_txtHargaBeliKeyPressed

    private void txtHargaJualKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHargaJualKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN || evt.getKeyCode() == evt.VK_ENTER){
            txtStok.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtHargaBeli.requestFocus();
        }
    }//GEN-LAST:event_txtHargaJualKeyPressed

    private void txtStokKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStokKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            txtNmBarang.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtHargaJual.requestFocus();
        }
        if(evt.getKeyCode() == evt.VK_ENTER)
        btnSimpan.doClick();
    }//GEN-LAST:event_txtStokKeyPressed

    private void txtKdBarangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKdBarangKeyReleased
        vid_barang = txtKdBarang.getText();
        txtKdBarang.setText(vid_barang);
        aksiCek();
    }//GEN-LAST:event_txtKdBarangKeyReleased

    private void txtKdBarangKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKdBarangKeyTyped
        if ( txtKdBarang.getText().length() == 13 ) {
             evt.consume();
        }
    }//GEN-LAST:event_txtKdBarangKeyTyped

    private void txtKdBarangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKdBarangKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN || evt.getKeyCode() == evt.VK_ENTER){
            txtNmBarang.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtStok.requestFocus();
        }
    }//GEN-LAST:event_txtKdBarangKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Tool.Custom_Button btnBatal;
    private Tool.Custom_Button btnHapus;
    private Tool.Custom_Button btnSimpan;
    private Tool.Custom_Button btnTambah;
    private javax.swing.JComboBox<String> cmbKategori;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblRecord;
    private javax.swing.JPanel panelCrud;
    private javax.swing.JTable tbDataBarang;
    private Tool.Custom_TextField txtCari;
    private javax.swing.JTextArea txtDeskripsi;
    private Tool.NumberTextField txtHargaBeli;
    private Tool.NumberTextField txtHargaJual;
    private Tool.Custom_TextField txtKdBarang;
    private Tool.Custom_TextField txtNmBarang;
    private Tool.NumberTextField txtStok;
    // End of variables declaration//GEN-END:variables
}
