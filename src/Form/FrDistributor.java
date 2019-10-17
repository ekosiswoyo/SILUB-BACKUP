package Form;

import Tool.KoneksiDB;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FrDistributor extends javax.swing.JInternalFrame {
    KoneksiDB getCnn = new KoneksiDB();
    Connection _Cnn;
    
    //deklarasi variabel
    private DefaultTableModel tblDistributor;
    String sqlselect, sqlinsert, sqldelete;
    String vid_dis, vnm_dis, vnm_manager, valamat, vno_telp, vemail;
    
    public FrDistributor() {
        initComponents();
        
        setTabelDistributor();
        showTabelDistrbutor();
        clearForm();
        disableForm();
    }
    
    private void clearForm(){
        txtNamaDis.setText("");
        txtNamaManager.setText("");
        txtAlamat.setText("");
        txtNoTelp.setText("");
        txtNamaDis.requestFocus();
        btnSimpan.setText("SIMPAN");
    }
    
    private void disableForm(){
        txtNamaDis.setEnabled(false);
        txtNamaManager.setEnabled(false);
        txtAlamat.setEnabled(false);
        txtNoTelp.setEnabled(false);
        panelCrud.setVisible(false);
        
    }
    
    private void enableForm(){
        txtNamaDis.setEnabled(true);
        txtNamaManager.setEnabled(true);
        txtAlamat.setEnabled(true);
        txtNoTelp.setEnabled(true);
        panelCrud.setVisible(true);
        btnSimpan.setText("SIMPAN");
    }
    
    private void setTabelDistributor(){
        String[] kolom1 = {"ID. DISTRIBUTOR", "NAMA DISTRIBUTOR", "NAMA MANAGER", "NO. TELEPON", "ALAMAT"};
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
        tbDataDistributor.getColumnModel().getColumn(1).setPreferredWidth(200);
        tbDataDistributor.getColumnModel().getColumn(2).setPreferredWidth(200);
        tbDataDistributor.getColumnModel().getColumn(3).setPreferredWidth(200);
        tbDataDistributor.getColumnModel().getColumn(4).setPreferredWidth(300);
    }
    
    private void clearTabelDistributor(){
        int row  = tblDistributor.getRowCount();
        for(int i = 0; i <row; i++){
            tblDistributor.removeRow(0);
        }
    }
    
    private void showTabelDistrbutor(){
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            clearTabelDistributor();
            sqlselect = "select * from tb_distributor order by id_distributor";
            
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);// mengeksekusi query select
             
            while(res.next()){
                vid_dis = res.getString("id_distributor");
                vnm_dis = res.getString("nama_distributor");
                vnm_manager = res.getString("nama_manager");
                valamat = res.getString("alamat");
                vno_telp = res.getString("no_telp");
                Object[]data = {vid_dis, vnm_dis, vnm_manager, vno_telp, valamat};
                tblDistributor.addRow(data);
            }
            lblRecord.setText("RECORD : "+tbDataDistributor.getRowCount());
            btnHapus.setEnabled(false);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method showTabelDistributor() : "+ex);
        }
    }
    
    private void getDataDistributor(){
        enableForm();
        try{
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            sqlselect = "select * from tb_distributor where id_distributor='"+vid_dis+"'"
                    + " order by id_distributor asc ";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            if(res.first()){
                txtIdDis.setText(res.getString("id_distributor"));
                txtNamaDis.setText(res.getString("nama_distributor"));
                txtNamaManager.setText(res.getString("nama_manager"));
                txtAlamat.setText(res.getString("alamat"));
                txtNoTelp.setText(res.getString("no_telp"));
                txtNamaDis.requestFocus();
            }
            }catch (SQLException ex){
                JOptionPane.showMessageDialog(this, "Error mthod getDataDistributor : " + ex);
            }
    }
    
    private void aksiSimpan(){
        vid_dis=txtIdDis.getText();
        vnm_dis=txtNamaDis.getText();
        vnm_manager=txtNamaManager.getText();
        valamat=txtAlamat.getText();
        vno_telp=txtNoTelp.getText();
        if(btnSimpan.getText().equals("SIMPAN")){
            sqlinsert = "insert into tb_distributor (id_distributor, nama_distributor, nama_manager, alamat, no_telp) values "
                    + " ('"+vid_dis+"', '"+vnm_dis+"', '"+vnm_manager+"', '"+valamat+"', '"+vno_telp+"')";
            JOptionPane.showMessageDialog(null, "DATA BERHASIL DISIMPAN !");
        }else{
            sqlinsert = "update tb_distributor set nama_distributor='"+vnm_dis+"', nama_manager='"+vnm_manager+"', "
                    + " alamat='"+valamat+"', no_telp='"+vno_telp+"' where id_distributor='"+vid_dis+"'";
            JOptionPane.showMessageDialog(null, "DATA BERHASIL DIUBAH !");
        }
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            Statement state = _Cnn.createStatement();
            state.executeUpdate(sqlinsert);
            
            clearForm(); enableForm(); auto_IdDistributor(); showTabelDistrbutor();
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method aksiSimpan() : "+ex);
        }
    }
    private void aksiCek(){
        vnm_dis=txtNamaDis.getText();
        try{
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select * from tb_distributor where nama_distributor='"+vnm_dis+"' ";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            res.next();
            if(res.getRow()!=0){
                JOptionPane.showMessageDialog(null, "DATA '"+vnm_dis+"' SUDAH TERDAFTAR !");
                txtNamaDis.setText(""); txtNamaDis.requestFocus();
            }
            }catch (SQLException ex){
                JOptionPane.showMessageDialog(this, "Error mthod CEK : " + ex);
            }
    }
    private void aksiHapus(){
        int jawab = JOptionPane.showConfirmDialog(null, 
                "ANDA YAKIN AKAN MENGHAPUS DATA INI ? KODE : "+vid_dis,
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if(jawab==JOptionPane.YES_OPTION){
            try{
                _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
                _Cnn = getCnn.getConnection();// membuka koneksi
                sqldelete = "delete from tb_distributor where id_distributor = '"+vid_dis+"'";
                java.sql.Statement state = _Cnn.createStatement();
                state.executeUpdate(sqldelete);
                JOptionPane.showMessageDialog(null, "DATA BERHASIL DIHAPUS !");
                showTabelDistrbutor(); clearForm();
            }catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "DATA SEDANG DIPAKAI !\nDATA TIDAK BISA DIHAPUS !");
            }
        }
    }
    
    private void cariDistributor(){
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            clearTabelDistributor();
            sqlselect = "select * from tb_distributor where nama_distributor like '"+txtCari.getText()+"%%' "
                    + " or id_distributor like '"+txtCari.getText()+"%%'";
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);// mengeksekusi query select
            while(res.next()){
                vid_dis = res.getString("id_distributor");
                vnm_dis = res.getString("nama_distributor");
                vnm_manager = res.getString("nama_manager");
                valamat = res.getString("alamat");
                vno_telp = res.getString("no_telp");
                Object[]data = {vid_dis, vnm_dis, vnm_manager, vno_telp, valamat};
                tblDistributor.addRow(data);
            }
            lblRecord.setText("RECORD : "+tbDataDistributor.getRowCount());
            btnHapus.setEnabled(false);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method cariDistributor() : "+ex);
        }
    }
    private void auto_IdDistributor(){
        try{
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select right(id_distributor,4) as id_distributor "
                    + " from tb_distributor order by id_distributor desc";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            if (res.next()) {
                String kode = res.getString("id_distributor");
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
                txtIdDis.setText("DIS-" + Nol + AN);
            }else{
                int kode = 1;
                txtIdDis.setText("DIS-" +"000"+Integer.toString(kode));//sesuaikan dengan variable namenya
            }
            }catch (SQLException ex){
                JOptionPane.showMessageDialog(this,"Error Method auto_IdDistributor : " + ex);
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
        tbDataDistributor = new javax.swing.JTable();
        btnHapus = new Tool.Custom_Button();
        btnTambah = new Tool.Custom_Button();
        lblRecord = new javax.swing.JLabel();
        panelCrud = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        txtIdDis = new Tool.Custom_TextField();
        txtNamaDis = new Tool.Custom_TextField();
        txtNamaManager = new Tool.Custom_TextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAlamat = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        btnSimpan = new Tool.Custom_Button();
        btnBatal = new Tool.Custom_Button();
        txtNoTelp = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();

        setClosable(true);
        setPreferredSize(new java.awt.Dimension(1205, 585));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "(+) DATA DISTRIBUTOR", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 12))); // NOI18N

        txtCari.setPlaceholder("PENCARIAN : NAMA DISTRIBUTOR");
        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCariKeyTyped(evt);
            }
        });

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "KLIK 2X --> MENGUBAH / MENGHAPUS DATA", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 12))); // NOI18N

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
        jScrollPane1.setViewportView(tbDataDistributor);

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 818, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCari, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(lblRecord)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRecord))
                .addContainerGap())
        );

        panelCrud.setBackground(new java.awt.Color(0, 0, 0));

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Suplier.png"))); // NOI18N

        jLabel9.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("DATA DISTRIBUTOR");

        jPanel7.setBackground(new java.awt.Color(0, 0, 0));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "(+)", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 12))); // NOI18N

        txtIdDis.setEditable(false);
        txtIdDis.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtIdDis.setPlaceholder("ID. DISTRIBUTOR");

        txtNamaDis.setPlaceholder("NAMA DISTRIBUTOR");
        txtNamaDis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNamaDisKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNamaDisKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNamaDisKeyTyped(evt);
            }
        });

        txtNamaManager.setPlaceholder("NAMA MANAGER");
        txtNamaManager.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNamaManagerKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNamaManagerKeyTyped(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
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

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("NO. TELEPON");

        btnSimpan.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/save_blue.png"))); // NOI18N
        btnSimpan.setText("SIMPAN");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
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

        txtNoTelp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNoTelpKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoTelpKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNoTelp)
                    .addComponent(jScrollPane2)
                    .addComponent(txtNamaDis, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNamaManager, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIdDis, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtIdDis, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNamaDis, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNamaManager, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNoTelp, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setFont(new java.awt.Font("Agency FB", 3, 14)); // NOI18N
        jLabel3.setForeground(java.awt.Color.red);
        jLabel3.setText("* DISARANKAN MENGGUNKAN HURUF KAPITAL !");
        jLabel3.setOpaque(true);

        javax.swing.GroupLayout panelCrudLayout = new javax.swing.GroupLayout(panelCrud);
        panelCrud.setLayout(panelCrudLayout);
        panelCrudLayout.setHorizontalGroup(
            panelCrudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelCrudLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCrudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(panelCrudLayout.createSequentialGroup()
                        .addGroup(panelCrudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
                            .addGroup(panelCrudLayout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        panelCrudLayout.setVerticalGroup(
            panelCrudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCrudLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCrudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelCrudLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel9)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelCrud, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        enableForm(); auto_IdDistributor(); clearForm(); 
    }//GEN-LAST:event_btnTambahActionPerformed

    private void txtCariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyTyped
        cariDistributor();
    }//GEN-LAST:event_txtCariKeyTyped

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        aksiHapus();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void tbDataDistributorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDataDistributorMouseClicked
        if(evt.getClickCount()==2){
            vid_dis = tbDataDistributor.getValueAt(tbDataDistributor.getSelectedRow(), 0).toString();
            btnHapus.setEnabled(true);
            getDataDistributor();
            btnSimpan.setText("UBAH DATA");
       }
    }//GEN-LAST:event_tbDataDistributorMouseClicked

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        clearForm(); disableForm();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if(txtNamaDis.getText().equals("")){
            JOptionPane.showMessageDialog(null, "NAMA DISTRIBUTOR HARUS DIISI !",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            txtNamaDis.requestFocus();
        }else if(txtNamaManager.getText().equals("")){
            JOptionPane.showMessageDialog(null, "NAMA MANAGER HARUS DIISI !",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            txtNamaManager.requestFocus();
        }else if(txtAlamat.getText().equals("")){
            JOptionPane.showMessageDialog(null, "ALAMAT HARUS DIISI !",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            txtAlamat.requestFocus();
        }else if(txtNoTelp.getText().equals("")){
            JOptionPane.showMessageDialog(null, "NO TELEPON HARUS DIISI !",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            txtNoTelp.requestFocus();
        }else if ( txtNoTelp.getText().length() < 6 ) {
            JOptionPane.showMessageDialog(null, "NO TELEPON HARUS LEBIH DARI 5 !",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            txtNoTelp.requestFocus();
        }else{
            aksiSimpan(); btnSimpan.setText("SIMPAN");
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void txtNamaDisKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNamaDisKeyTyped
        if ( txtNamaDis.getText().length() == 40 ) {
        evt.consume();
        }
    }//GEN-LAST:event_txtNamaDisKeyTyped

    private void txtNamaManagerKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNamaManagerKeyTyped
        if ( txtNamaManager.getText().length() == 40 ) {
        evt.consume();
        }
    }//GEN-LAST:event_txtNamaManagerKeyTyped

    private void txtNamaDisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNamaDisKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            txtNamaManager.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtNoTelp.requestFocus();
        }
    }//GEN-LAST:event_txtNamaDisKeyPressed

    private void txtNamaManagerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNamaManagerKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            txtAlamat.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtNamaDis.requestFocus();
        }
    }//GEN-LAST:event_txtNamaManagerKeyPressed

    private void txtAlamatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAlamatKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            txtNoTelp.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtNamaManager.requestFocus();
        }
    }//GEN-LAST:event_txtAlamatKeyPressed

    private void txtNoTelpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoTelpKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            txtAlamat.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            btnSimpan.requestFocus();
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

    private void txtNamaDisKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNamaDisKeyReleased
        aksiCek();
    }//GEN-LAST:event_txtNamaDisKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Tool.Custom_Button btnBatal;
    private Tool.Custom_Button btnHapus;
    private Tool.Custom_Button btnSimpan;
    private Tool.Custom_Button btnTambah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblRecord;
    private javax.swing.JPanel panelCrud;
    private javax.swing.JTable tbDataDistributor;
    private javax.swing.JTextArea txtAlamat;
    private Tool.Custom_TextField txtCari;
    private Tool.Custom_TextField txtIdDis;
    private Tool.Custom_TextField txtNamaDis;
    private Tool.Custom_TextField txtNamaManager;
    private javax.swing.JTextField txtNoTelp;
    // End of variables declaration//GEN-END:variables
}
