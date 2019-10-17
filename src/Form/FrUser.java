package Form;

import Tool.KoneksiDB;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FrUser extends javax.swing.JInternalFrame {
    KoneksiDB getCnn = new KoneksiDB();
    Connection _Cnn;
    
    //deklarasi variabel
    private DefaultTableModel tblUser;
    String sqlselect, sqlinsert, sqldelete;
    String vid_user, vusername, vnm_user, vjns_kel, vtgl_lahir, valamat, vno_telp, vpassword, vhak_akses;
    SimpleDateFormat tglview = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat tglinput = new SimpleDateFormat("yyyy-MM-dd");
    String id = FrMenu.getU_id();
    
    public FrUser() {
        initComponents();
        
        setTabelUser();
        showTabelUser();
        clearForm();
        disableForm();
    }
    
    private void clearForm(){
        txtNamaUser.requestFocus();
        btnSimpan.setText("SIMPAN");
        txtNamaUser.setText("");
        buttonGroup1.clearSelection();
        dtTglLahir.setDate(new Date());
        txtAlamat.setText("");
        txtNoTelp.setText("");
        txtPassword.setText("");
        txtKonfirmPassword.setText("");
        cmbHakAkses.setSelectedIndex(0);
    }
    
    private void disableForm(){
        txtNamaUser.setEnabled(false);
        rbLaki.setEnabled(false);
        rbPerempuan.setEnabled(false);
        dtTglLahir.setEnabled(false);
        txtAlamat.setEnabled(false);
        txtNoTelp.setEnabled(false);
        txtPassword.setEnabled(false);
        cmbHakAkses.setEnabled(false);
        panelCrud.setVisible(false);
        
    }
    
    private void enableForm(){
        txtNamaUser.setEnabled(true);
        rbLaki.setEnabled(true);
        rbPerempuan.setEnabled(true);
        dtTglLahir.setEnabled(true);
        txtAlamat.setEnabled(true);
        txtNoTelp.setEnabled(true);
        txtPassword.setEnabled(true);
        cmbHakAkses.setEnabled(true);
        panelCrud.setVisible(true);
        btnSimpan.setText("SIMPAN");
    }
    
    private void setTabelUser(){
        String[] kolom1 = {"ID. USER", "NAMA USER", "L/P", "TGL LAHIR","HAK AKSES", "NO. TELEPON", "PASSWORD"};
        tblUser = new DefaultTableModel(null, kolom1){
            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
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
                int cola = tblUser.getColumnCount();
                return (col < cola) ? false:true;
            }
        };
        tbDataUser.setModel(tblUser);
        tbDataUser.getColumnModel().getColumn(0).setPreferredWidth(100);
        tbDataUser.getColumnModel().getColumn(1).setPreferredWidth(200);
        tbDataUser.getColumnModel().getColumn(2).setPreferredWidth(50);
        tbDataUser.getColumnModel().getColumn(3).setPreferredWidth(200);
        tbDataUser.getColumnModel().getColumn(4).setPreferredWidth(200);
        tbDataUser.getColumnModel().getColumn(5).setPreferredWidth(200);
        tbDataUser.getColumnModel().getColumn(6).setPreferredWidth(200);
    }
    
    private void clearTabelUser(){
        int row  = tblUser.getRowCount();
        for(int i = 0; i <row; i++){
            tblUser.removeRow(0);
        }
    }
    
    private void showTabelUser(){
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            clearTabelUser();
            sqlselect = "select * from tb_user order by id_user";
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);// mengeksekusi query select
            while(res.next()){
                vid_user = res.getString("id_user");
                vnm_user = res.getString("nama_lengkap");
                vjns_kel = res.getString("jns_kel");
                vtgl_lahir = tglview.format(res.getDate("tgl_lahir"));
                vhak_akses = res.getString("hak_akses");
                vno_telp = res.getString("no_telp");
                vpassword = res.getString("password");
                Object[]data = {vid_user, vnm_user, vjns_kel, vtgl_lahir, vhak_akses, vno_telp, vpassword};
                tblUser.addRow(data);
            }
            lblRecord.setText("RECORD : "+tbDataUser.getRowCount());
            btnHapus.setEnabled(false);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method showTabelUser() : "+ex);
        }
    }
    
    private void getDataUser(){
        enableForm();
        try{
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            sqlselect = "select * from tb_user where id_user='"+vid_user+"'"
                    + " order by id_user asc ";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            if(res.first()){
                txtIdUser.setText(res.getString("id_user"));
                txtNamaUser.setText(res.getString("nama_lengkap"));
                vjns_kel = res.getString("jns_kel");
                if(vjns_kel.equals("L")){
                    rbLaki.setSelected(true);
                }else{
                    rbPerempuan.setSelected(true);
                }
                dtTglLahir.setDate(res.getDate("tgl_lahir"));
                txtAlamat.setText(res.getString("alamat"));
                txtNoTelp.setText(res.getString("no_telp"));
                txtPassword.setText(res.getString("password"));
                cmbHakAkses.setSelectedItem(res.getString("hak_akses"));
                txtNamaUser.requestFocus();
                txtKonfirmPassword.setText("");
            }
            }catch (SQLException ex){
                JOptionPane.showMessageDialog(this, "Error mthod getDataUser : " + ex);
            }
    }
    
    private void aksiSimpan(){
        vid_user=txtIdUser.getText();
        vnm_user=txtNamaUser.getText();
        if(rbLaki.isSelected()){
            vjns_kel = "L";
        }else {
            vjns_kel = "P";
        }
        vtgl_lahir = tglinput.format(dtTglLahir.getDate());
        valamat=txtAlamat.getText();
        vno_telp=txtNoTelp.getText();
        vpassword=txtKonfirmPassword.getText();
        vhak_akses=cmbHakAkses.getSelectedItem().toString();
        
        if(btnSimpan.getText().equals("SIMPAN")){
            sqlinsert = "insert into tb_user (id_user, nama_lengkap, jns_kel, tgl_lahir, alamat, "
                    + " no_telp, password, hak_akses) values ('"+vid_user+"', '"+vnm_user+"', "
                    + " '"+vjns_kel+"', '"+vtgl_lahir+"', '"+valamat+"', '"+vno_telp+"', "
                    + " '"+vpassword+"', '"+vhak_akses+"')";
            JOptionPane.showMessageDialog(null, "DATA BERHASIL DISIMPAN !");
        }else{
            sqlinsert = "update tb_user set nama_lengkap='"+vnm_user+"', jns_kel='"+vjns_kel+"', "
                    + " tgl_lahir='"+vtgl_lahir+"', alamat='"+valamat+"', no_telp='"+vno_telp+"', password='"+vpassword+"', "
                    + " hak_akses='"+vhak_akses+"' where id_user='"+vid_user+"'";
            JOptionPane.showMessageDialog(null, "DATA BERHASIL DIUBAH !");
        }
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            Statement state = _Cnn.createStatement();
            state.executeUpdate(sqlinsert);
            
            showTabelUser(); clearForm(); enableForm(); auto_IdUser();
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method aksiSimpan() : "+ex);
        }
    }
    private void aksiCek(){
        vpassword=txtPassword.getText();
        try{
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select * from tb_user where password='"+vpassword+"' ";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            res.next();
            if(res.getRow()!=0){
                JOptionPane.showMessageDialog(null, "PASSWORD INI SUDAH TERDAFTAR !");
                txtPassword.setText(""); txtPassword.requestFocus();
            }
            }catch (SQLException ex){
                JOptionPane.showMessageDialog(this, "Error mthod CEK : " + ex);
            }
    }
    private void aksiHapus(){
        int jawab = JOptionPane.showConfirmDialog(null, 
                "ANDA YAKIN INGIN MENGHAPUS DATA INI ? ID. USER : "+vid_user,
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if(jawab==JOptionPane.YES_OPTION){
            if(txtIdUser.getText().equals(id)){
                JOptionPane.showMessageDialog(null, "USER INI SEDANG LOGIN !");
            }else{
                try{
                    _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
                    _Cnn = getCnn.getConnection();// membuka koneksi
                    sqldelete = "delete from tb_user where id_user = '"+vid_user+"'";
                    java.sql.Statement state = _Cnn.createStatement();
                    state.executeUpdate(sqldelete);
                    JOptionPane.showMessageDialog(null, "DATA BERHASIL DIHAPUS !");
                    showTabelUser(); clearForm();
                }catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "DATA SEDANG DIPAKAI !\nDATA TIDAK BISA DIHAPUS !");
                }
            }
        }
    }
    
    private void cariUser(){
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            clearTabelUser();
            sqlselect= "select * from tb_user where nama_lengkap like '"+txtCari.getText()+"%%' "
                    + " or id_user like '"+txtCari.getText()+"%%'";
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);// mengeksekusi query select
            while(res.next()){
                vid_user = res.getString("id_user");
                vnm_user = res.getString("nama_lengkap");
                vjns_kel = res.getString("jns_kel");
                vtgl_lahir = tglview.format(res.getDate("tgl_lahir"));
                vhak_akses = res.getString("hak_akses");
                vno_telp = res.getString("no_telp");
                vpassword = res.getString("password");
                Object[]data = {vid_user, vnm_user, vjns_kel, vtgl_lahir, vhak_akses, vno_telp, vpassword};
                tblUser.addRow(data);
            }
            lblRecord.setText("RECORD : "+tbDataUser.getRowCount());
            btnHapus.setEnabled(false);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method showTabelUser() : "+ex);
        }
    }
    private void auto_IdUser(){
        try{
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select right(id_user,3) as id_user "
                    + " from tb_user order by id_user desc";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            if (res.next()) {
                String kode = res.getString("id_user");
                String AN = "" + (Integer.parseInt(kode) + 1);
                String Nol = "";

                if(AN.length()==1)
                {Nol = "00";}
                else if(AN.length()==2)
                {Nol = "0";}
                else if(AN.length()==3)
                {Nol = "";}
                txtIdUser.setText("ID" + Nol + AN);
            }else{
                int kode = 1;
                txtIdUser.setText("ID" +"00"+Integer.toString(kode));//sesuaikan dengan variable namenya
            }
            }catch (SQLException ex){
                JOptionPane.showMessageDialog(this,"Error Method auto_IdUser : " + ex);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        txtCari = new Tool.Custom_TextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbDataUser = new javax.swing.JTable();
        btnHapus = new Tool.Custom_Button();
        btnTambah = new Tool.Custom_Button();
        lblRecord = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        panelCrud = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        txtIdUser = new Tool.Custom_TextField();
        txtNamaUser = new Tool.Custom_TextField();
        dtTglLahir = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        lblPass = new javax.swing.JLabel();
        cmbHakAkses = new javax.swing.JComboBox<>();
        rbLaki = new javax.swing.JRadioButton();
        rbPerempuan = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAlamat = new javax.swing.JTextArea();
        txtKonfirmPassword = new javax.swing.JPasswordField();
        lblKonfPass = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNoTelp = new javax.swing.JTextField();
        btnSimpan = new Tool.Custom_Button();
        btnBatal = new Tool.Custom_Button();

        setClosable(true);
        setPreferredSize(new java.awt.Dimension(1205, 585));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "(+) DATA PENGGUNA", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 12))); // NOI18N

        txtCari.setPlaceholder("PENCARIAN : NAMA PEGAWAI");
        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCariKeyTyped(evt);
            }
        });

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "KLIK 2X --> MENGUBAH / MENGHAPUS DATA", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 12))); // NOI18N

        tbDataUser.setModel(new javax.swing.table.DefaultTableModel(
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
        tbDataUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDataUserMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbDataUser);

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

        jLabel6.setFont(new java.awt.Font("Agency FB", 3, 14)); // NOI18N
        jLabel6.setForeground(java.awt.Color.red);
        jLabel6.setText("* DISARANKAN MENGGUNKAN HURUF KAPITAL !");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lblRecord)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6))
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRecord))
                .addContainerGap())
        );

        panelCrud.setBackground(new java.awt.Color(0, 0, 0));

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "(+)", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 12))); // NOI18N

        txtIdUser.setEditable(false);
        txtIdUser.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtIdUser.setPlaceholder("ID. USER");

        txtNamaUser.setPlaceholder("NAMA LENGKAP");
        txtNamaUser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNamaUserKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNamaUserKeyTyped(evt);
            }
        });

        dtTglLahir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dtTglLahirKeyPressed(evt);
            }
        });

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("TANGGAL LAHIR");

        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPasswordKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPasswordKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPasswordKeyTyped(evt);
            }
        });

        lblPass.setForeground(new java.awt.Color(255, 255, 255));
        lblPass.setText("PASSWORD");

        cmbHakAkses.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cmbHakAkses.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- PILIH HAK AKSES --", "PEMILIK TOKO", "KASIR" }));
        cmbHakAkses.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbHakAksesKeyPressed(evt);
            }
        });

        rbLaki.setBackground(new java.awt.Color(0, 0, 0));
        buttonGroup1.add(rbLaki);
        rbLaki.setForeground(new java.awt.Color(255, 255, 255));
        rbLaki.setText("LAKI-LAKI");
        rbLaki.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                rbLakiKeyPressed(evt);
            }
        });

        rbPerempuan.setBackground(new java.awt.Color(0, 0, 0));
        buttonGroup1.add(rbPerempuan);
        rbPerempuan.setForeground(new java.awt.Color(255, 255, 255));
        rbPerempuan.setText("PEREMPUAN");
        rbPerempuan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                rbPerempuanKeyPressed(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("ALAMAT");

        txtAlamat.setColumns(20);
        txtAlamat.setRows(5);
        txtAlamat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAlamatKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(txtAlamat);

        txtKonfirmPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKonfirmPasswordKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtKonfirmPasswordKeyTyped(evt);
            }
        });

        lblKonfPass.setForeground(new java.awt.Color(255, 255, 255));
        lblKonfPass.setText("ULANGI PASSWORD");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("NO. TELP");

        txtNoTelp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNoTelpKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoTelpKeyTyped(evt);
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
        btnSimpan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSimpanKeyPressed(evt);
            }
        });

        btnBatal.setForeground(new java.awt.Color(255, 255, 255));
        btnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/refresh_1.png"))); // NOI18N
        btnBatal.setText("BATAL");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNoTelp)
                    .addComponent(jScrollPane2)
                    .addComponent(txtNamaUser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtKonfirmPassword)
                    .addComponent(txtPassword, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbHakAkses, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(txtIdUser, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(dtTglLahir, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbLaki)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbPerempuan))
                            .addComponent(jLabel2)
                            .addComponent(lblPass)
                            .addComponent(lblKonfPass))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(txtIdUser, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNamaUser, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dtTglLahir, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rbLaki)
                        .addComponent(rbPerempuan)))
                .addGap(0, 0, 0)
                .addComponent(jLabel1)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel2)
                .addGap(0, 0, 0)
                .addComponent(txtNoTelp, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(lblPass)
                .addGap(0, 0, 0)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblKonfPass)
                .addGap(0, 0, 0)
                .addComponent(txtKonfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(cmbHakAkses, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40))
        );

        javax.swing.GroupLayout panelCrudLayout = new javax.swing.GroupLayout(panelCrud);
        panelCrud.setLayout(panelCrudLayout);
        panelCrudLayout.setHorizontalGroup(
            panelCrudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCrudLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );
        panelCrudLayout.setVerticalGroup(
            panelCrudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCrudLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelCrud, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        enableForm(); auto_IdUser(); clearForm(); 
    }//GEN-LAST:event_btnTambahActionPerformed

    private void txtCariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyTyped
        cariUser();
    }//GEN-LAST:event_txtCariKeyTyped

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        aksiHapus();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void tbDataUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDataUserMouseClicked
        if(evt.getClickCount()==2){
            vid_user = tbDataUser.getValueAt(tbDataUser.getSelectedRow(), 0).toString();
            btnHapus.setEnabled(true);
            getDataUser();
            btnSimpan.setText("UBAH DATA");
        }
    }//GEN-LAST:event_tbDataUserMouseClicked

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if(btnSimpan.getText().equals("SIMPAN")){
            if(txtNamaUser.getText().equals("")){
                JOptionPane.showMessageDialog(null, "NAMA LENGKAP HARUS DIISI !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                txtNamaUser.requestFocus();
            }else if(buttonGroup1.isSelected(null)){
                JOptionPane.showMessageDialog(null, "JENIS KELAMIN BELUM DIPILIH !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }else if(dtTglLahir.getDate().equals("")){
                JOptionPane.showMessageDialog(null, "TANGGAL LAHIR HARUS DIISI !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                dtTglLahir.requestFocus();
            }else if(txtAlamat.getText().equals("")){
                JOptionPane.showMessageDialog(null, "ALAMAT HARUS DIISI !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                txtAlamat.requestFocus();
            }else if(txtNoTelp.getText().equals("")){
                JOptionPane.showMessageDialog(null, "NO. TELEPON HARUS DIISI !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                txtNoTelp.requestFocus();
            }else if ( txtNoTelp.getText().length() < 6 ) {
            JOptionPane.showMessageDialog(null, "NO TELEPON HARUS LEBIH DARI 5 !",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            txtNoTelp.requestFocus();
            }else if(txtPassword.getText().equals("")){
                JOptionPane.showMessageDialog(null, "PASSWORD HARUS DIISI !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                txtPassword.requestFocus();
            }else if(txtKonfirmPassword.getText().equals("")){
                JOptionPane.showMessageDialog(null, "KONFIRMASI PASSWORD HARUS DIISI !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                txtKonfirmPassword.requestFocus();
            }else if( txtPassword.getText().length() < 7 ){
                JOptionPane.showMessageDialog(null, "PASSWORD HARUS LEBIH DARI 6 KARAKTER !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                txtPassword.requestFocus();
            }else if( txtKonfirmPassword.getText().length() < 7 ){
                JOptionPane.showMessageDialog(null, "KONFIRMASI PASSWORD HARUS LEBIH DARI 6 KARAKTER !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                txtKonfirmPassword.requestFocus();
            }else if(txtPassword.getText() == null ? txtKonfirmPassword.getText() != null : !txtPassword.getText().equals(txtKonfirmPassword.getText())){
                JOptionPane.showMessageDialog(null, "PASSWORD TIDAK SAMA, PERIKSA KEMBALI !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                txtKonfirmPassword.requestFocus();
            }else if(cmbHakAkses.getSelectedIndex()<=0){
                JOptionPane.showMessageDialog(null, "HAK AKSES BELUM DIPILIH !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                cmbHakAkses.requestFocus();
            }else{
                aksiSimpan(); btnSimpan.setText("SIMPAN");
            }
        }else {
            if(txtNamaUser.getText().equals("")){
                JOptionPane.showMessageDialog(null, "NAMA LENGKAP HARUS DIISI !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                txtNamaUser.requestFocus();
            }else if(buttonGroup1.isSelected(null)){
                JOptionPane.showMessageDialog(null, "JENIS KELAMIN BELUM DIPILIH !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }else if(dtTglLahir.getDate().equals("")){
                JOptionPane.showMessageDialog(null, "TANGGAL LAHIR HARUS DIISI !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                dtTglLahir.requestFocus();
            }else if(txtAlamat.getText().equals("")){
                JOptionPane.showMessageDialog(null, "ALAMAT HARUS DIISI !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                txtAlamat.requestFocus();
            }else if(txtNoTelp.getText().equals("")){
                JOptionPane.showMessageDialog(null, "NO. TELEPON HARUS DIISI !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                txtNoTelp.requestFocus();
            }else if ( txtNoTelp.getText().length() < 6 ) {
            JOptionPane.showMessageDialog(null, "NO TELEPON HARUS LEBIH DARI 5 !",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            txtNoTelp.requestFocus();
            }else if(txtPassword.getText().equals("")){
                JOptionPane.showMessageDialog(null, "PASSWORD HARUS DIISI !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                txtPassword.requestFocus();
            }else if(txtKonfirmPassword.getText().equals("")){
                JOptionPane.showMessageDialog(null, "KONFIRMASI PASSWORD HARUS DIISI !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                txtKonfirmPassword.requestFocus();
            }else if( txtPassword.getText().length() < 7 ){
                JOptionPane.showMessageDialog(null, "PASSWORD HARUS LEBIH DARI 6 KARAKTER !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                txtPassword.requestFocus();
            }else if( txtKonfirmPassword.getText().length() < 7 ){
                JOptionPane.showMessageDialog(null, "KONFIRMASI PASSWORD HARUS LEBIH DARI 6 KARAKTER !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                txtKonfirmPassword.requestFocus();
            }else if(txtPassword.getText() == null ? txtKonfirmPassword.getText() != null : !txtPassword.getText().equals(txtKonfirmPassword.getText())){
                JOptionPane.showMessageDialog(null, "PASSWORD TIDAK SAMA, PERIKSA KEMBALI !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                txtKonfirmPassword.requestFocus();
            }else if(cmbHakAkses.getSelectedIndex()<=0){
                JOptionPane.showMessageDialog(this, "HAK AKSES BELUM DIPILIH !",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                cmbHakAkses.requestFocus();
            }else{
                aksiSimpan();
            }
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        clearForm(); disableForm();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void txtNamaUserKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNamaUserKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            dtTglLahir.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            btnSimpan.requestFocus();
        }
    }//GEN-LAST:event_txtNamaUserKeyPressed

    private void txtNamaUserKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNamaUserKeyTyped
        if ( txtNamaUser.getText().length() == 40 ) {
        evt.consume();
    }
    }//GEN-LAST:event_txtNamaUserKeyTyped

    private void dtTglLahirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dtTglLahirKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            rbLaki.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtNamaUser.requestFocus();
        }
    }//GEN-LAST:event_dtTglLahirKeyPressed

    private void rbLakiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rbLakiKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            rbPerempuan.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            dtTglLahir.requestFocus();
        }
    }//GEN-LAST:event_rbLakiKeyPressed

    private void rbPerempuanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rbPerempuanKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            txtAlamat.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            rbLaki.requestFocus();
        }
    }//GEN-LAST:event_rbPerempuanKeyPressed

    private void txtAlamatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAlamatKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            txtNoTelp.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            rbPerempuan.requestFocus();
        }
    }//GEN-LAST:event_txtAlamatKeyPressed

    private void txtPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            txtKonfirmPassword.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtNoTelp.requestFocus();
        }
    }//GEN-LAST:event_txtPasswordKeyPressed

    private void txtPasswordKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyTyped
        if ( txtPassword.getText().length() == 40 ) {
        evt.consume();
        }
    }//GEN-LAST:event_txtPasswordKeyTyped

    private void txtKonfirmPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKonfirmPasswordKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            cmbHakAkses.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtPassword.requestFocus();
        }
    }//GEN-LAST:event_txtKonfirmPasswordKeyPressed

    private void txtKonfirmPasswordKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKonfirmPasswordKeyTyped
        if ( txtKonfirmPassword.getText().length() == 40 ) {
        evt.consume();
        }
    }//GEN-LAST:event_txtKonfirmPasswordKeyTyped

    private void cmbHakAksesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbHakAksesKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            btnSimpan.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtKonfirmPassword.requestFocus();
        }
        if(evt.getKeyCode() == evt.VK_ENTER)
        btnSimpan.doClick();
    }//GEN-LAST:event_cmbHakAksesKeyPressed

    private void btnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSimpanKeyPressed
        if (evt.getKeyCode() == evt.VK_UP){
            cmbHakAkses.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_DOWN){
            txtNamaUser.requestFocus();
        }
        if(evt.getKeyCode() == evt.VK_ENTER)
        btnSimpan.doClick();
    }//GEN-LAST:event_btnSimpanKeyPressed

    private void txtNoTelpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoTelpKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            txtPassword.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtAlamat.requestFocus();
        }
    }//GEN-LAST:event_txtNoTelpKeyPressed

    private void txtNoTelpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoTelpKeyTyped
        char karakter = evt.getKeyChar();
        if(!(((karakter >= '0') && (karakter <= '9') || (karakter == KeyEvent.VK_BACK_SPACE) || (karakter == KeyEvent.VK_DELETE)))){
            getToolkit().beep();
            evt.consume();
        }
        if ( txtNoTelp.getText().length() == 13 ) {
            evt.consume();
        }
    }//GEN-LAST:event_txtNoTelpKeyTyped

    private void txtPasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyReleased
        aksiCek();
    }//GEN-LAST:event_txtPasswordKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Tool.Custom_Button btnBatal;
    private Tool.Custom_Button btnHapus;
    private Tool.Custom_Button btnSimpan;
    private Tool.Custom_Button btnTambah;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cmbHakAkses;
    private com.toedter.calendar.JDateChooser dtTglLahir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblKonfPass;
    private javax.swing.JLabel lblPass;
    private javax.swing.JLabel lblRecord;
    private javax.swing.JPanel panelCrud;
    private javax.swing.JRadioButton rbLaki;
    private javax.swing.JRadioButton rbPerempuan;
    private javax.swing.JTable tbDataUser;
    private javax.swing.JTextArea txtAlamat;
    private Tool.Custom_TextField txtCari;
    private Tool.Custom_TextField txtIdUser;
    private javax.swing.JPasswordField txtKonfirmPassword;
    private Tool.Custom_TextField txtNamaUser;
    private javax.swing.JTextField txtNoTelp;
    private javax.swing.JPasswordField txtPassword;
    // End of variables declaration//GEN-END:variables
}
