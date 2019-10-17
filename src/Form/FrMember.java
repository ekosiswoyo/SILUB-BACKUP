 package Form;

import Tool.KoneksiDB;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class FrMember extends javax.swing.JInternalFrame {
    KoneksiDB getCnn = new KoneksiDB();
    Connection _Cnn;
    private DefaultTableModel tblMember;
    String sqlselect, sqlinsert, sqldelete;
    String vid_member, vnm_member, vjns_kel, vtgl, vno_telp, valamat;
    SimpleDateFormat tglview = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat tglinput = new SimpleDateFormat("yyyy-MM-dd");
    public FrMember() {
        initComponents();
        setTabelMember();
        showTabelMember();
        clearForm();
        disableForm();
    }
    private void clearForm(){
        txtNamaMember.setText("");
        buttonGroup1.clearSelection();
        dtTglDaftar.setDate(new Date());
        txtAlamat.setText("");
        txtNoTelp.setText("");
        txtNamaMember.requestFocus();
        btnSimpan.setText("SIMPAN");
        btnCetak.setVisible(false);
    }
    private void disableForm(){
        txtNamaMember.setEnabled(false);
        rbLaki.setEnabled(false);
        rbPerempuan.setEnabled(false);
        txtAlamat.setEnabled(false);
        txtNoTelp.setEnabled(false);
        panelCrud.setVisible(false);
    }
    private void enableForm(){
        txtNamaMember.setEnabled(true);
        rbLaki.setEnabled(true);
        rbPerempuan.setEnabled(true);
        txtAlamat.setEnabled(true);
        txtNoTelp.setEnabled(true);
        panelCrud.setVisible(true);
        btnSimpan.setText("SIMPAN");
    }
    private void setTabelMember(){
        String[] kolom1 = {"ID. MEMBER", "NAMA MEMBER", "L/P", "TGL DAFTAR", "NO. TELEPON", "ALAMAT"};
        tblMember = new DefaultTableModel(null, kolom1){
            Class[] types = new Class[]{
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
                int cola = tblMember.getColumnCount();
                return (col < cola) ? false:true;
            }
        };
        tbDataMember.setModel(tblMember);
        tbDataMember.getColumnModel().getColumn(0).setPreferredWidth(150);
        tbDataMember.getColumnModel().getColumn(1).setPreferredWidth(200);
        tbDataMember.getColumnModel().getColumn(2).setPreferredWidth(75);
        tbDataMember.getColumnModel().getColumn(3).setPreferredWidth(150);
        tbDataMember.getColumnModel().getColumn(4).setPreferredWidth(200);
        tbDataMember.getColumnModel().getColumn(5).setPreferredWidth(350);
    }
    private void clearTabelMember(){
        int row  = tblMember.getRowCount();
        for(int i = 0; i <row; i++){
            tblMember.removeRow(0);
        }
    }
    private void showTabelMember(){
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            clearTabelMember();
            sqlselect = "select * from tb_member where id_member != '' order by tgl_daftar desc, id_member desc";
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);// mengeksekusi query select
            while(res.next()){
                vid_member = res.getString("id_member");
                vnm_member = res.getString("nama_member");
                vjns_kel = res.getString("jns_kel");
                vtgl = tglview.format(res.getDate("tgl_daftar"));
                vno_telp = res.getString("no_telp");
                valamat = res.getString("alamat");
                Object[]data = {vid_member, vnm_member, vjns_kel, vtgl, vno_telp, valamat};
                tblMember.addRow(data);
            }
            lblRecord.setText("RECORD : "+tbDataMember.getRowCount());
            btnHapus.setEnabled(false);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method showTabelMember() : "+ex);
        }
    }
    private void getDataMember(){
        enableForm();
        try{
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            sqlselect = "select * from tb_member where id_member='"+vid_member+"'"
                    + " order by id_member asc ";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            if(res.first()){
                txtIdMember.setText(res.getString("id_member"));
                txtNamaMember.setText(res.getString("nama_member"));
                vjns_kel = res.getString("jns_kel");
                if(vjns_kel.equals("L")){
                    rbLaki.setSelected(true);
                }else{
                    rbPerempuan.setSelected(true);
                }
                dtTglDaftar.setDate(res.getDate("tgl_daftar"));
                txtNoTelp.setText(res.getString("no_telp"));
                txtAlamat.setText(res.getString("alamat"));
                txtNamaMember.requestFocus();
            }
            }catch (SQLException ex){
                JOptionPane.showMessageDialog(this, "Error mthod getDataMember : " + ex);
            }
    }
    private void aksiSimpan(){
        vid_member=txtIdMember.getText();
        vnm_member=txtNamaMember.getText();
        if(rbLaki.isSelected()){
            vjns_kel = "L";
        }else {
            vjns_kel = "P";
        }
        vtgl = tglinput.format(dtTglDaftar.getDate());
        vno_telp=txtNoTelp.getText();
        valamat=txtAlamat.getText();
        if(btnSimpan.getText().equals("SIMPAN")){
            sqlinsert = "insert into tb_member values ('"+vid_member+"', '"+vnm_member+"', "
                    + " '"+vjns_kel+"', '"+vtgl+"', '"+vno_telp+"', '"+valamat+"')";
            JOptionPane.showMessageDialog(null, "DATA BERHASIL DISIMPAN !");
        }else{
            sqlinsert = "update tb_member set nama_member='"+vnm_member+"', jns_kel='"+vjns_kel+"', "
                    + " tgl_daftar='"+vtgl+"', no_telp='"+vno_telp+"', alamat='"+valamat+"' "
                    + " where id_member='"+vid_member+"'";
            JOptionPane.showMessageDialog(null, "DATA BERHASIL DIUBAH!");
        }
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            Statement state = _Cnn.createStatement();
            state.executeUpdate(sqlinsert);
            
            clearForm(); enableForm(); auto_IdMember(); showTabelMember();
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method aksiSimpan() : "+ex);
        }
    }
    private void aksiCek(){
        vno_telp = txtNoTelp.getText();
        try{
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select * from tb_member where no_telp='"+vno_telp+"' ";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            res.next();
            if(res.getRow()!=0){
                JOptionPane.showMessageDialog(null, "NO. TELEPON '"+vno_telp+"' SUDAH TERDAFTAR !");
                txtNoTelp.setText(""); txtNoTelp.requestFocus();
            }
            }catch (SQLException ex){
                JOptionPane.showMessageDialog(this, "Error mthod CEK : " + ex);
            }
    }
    private void aksiHapus(){
        int jawab = JOptionPane.showConfirmDialog(null, 
                "ANDA YAKIN INGIN MENGHAPUS DATA INI ? ID. MEMBER : "+vid_member,
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if(jawab==JOptionPane.YES_OPTION){
            try{
                _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
                _Cnn = getCnn.getConnection();// membuka koneksi
                sqldelete = "delete from tb_member where id_member = '"+vid_member+"'";
                java.sql.Statement state = _Cnn.createStatement();
                state.executeUpdate(sqldelete);
                JOptionPane.showMessageDialog(null, "DATA BERHASIL DIHAPUS !");
                showTabelMember(); clearForm();
            }catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "DATA SEDANG DIPAKAI !\nDATA TIDAK BISA DIHAPUS !");
            }
        }
    }
    private void cariMember(){
        try {
            _Cnn = null;//mengkosongkan variabel objek koneksi (menutup koneksi)
            _Cnn = getCnn.getConnection();// membuka koneksi
            clearTabelMember();
            sqlselect = "select * from tb_member where nama_member like '"+txtCari.getText()+"%%' and id_member != '' "
                    + " or id_member like '"+txtCari.getText()+"%%'";
            Statement stat = _Cnn.createStatement();//membuat statement
            ResultSet res = stat.executeQuery(sqlselect);// mengeksekusi query select
            while(res.next()){
                vid_member = res.getString("id_member");
                vnm_member = res.getString("nama_member");
                vjns_kel = res.getString("jns_kel");
                vtgl = tglview.format(res.getDate("tgl_daftar"));
                vno_telp = res.getString("no_telp");
                valamat = res.getString("alamat");
                Object[]data = {vid_member, vnm_member, vjns_kel, vno_telp, valamat};
                tblMember.addRow(data);
            }
            lblRecord.setText("RECORD : "+tbDataMember.getRowCount());
            btnHapus.setEnabled(false);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error method showTabelMember() : "+ex);
        }
    }
    private void auto_IdMember(){
        Date sk = new Date();
        SimpleDateFormat format1=new SimpleDateFormat("ddMMyy");
        String time = format1.format(sk);
        try{
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select right(id_member,4) as id_member "
                    + " from tb_member order by id_member desc";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            if (res.next()) {
                String kode = res.getString("id_member");
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
                txtIdMember.setText(time+ Nol + AN);
            }else{
                int kode = 1;
                txtIdMember.setText(time + "000" + Integer.toString(kode));//sesuaikan dengan variable namenya
            }
            }catch (SQLException ex){
                JOptionPane.showMessageDialog(this,"Error Method autoIdMember : " + ex);
            }
    }
    private void cetakKartu(){
        vid_member = txtIdMember.getText();
        String pth = System.getProperty("user.dir") + "/Laporan/MemberCard.jrxml";
        try{
            Map<String, Object> parameters = new HashMap<>();
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            parameters.put("parId", vid_member);
            JasperReport jrpt = JasperCompileManager.compileReport(pth);
            JasperPrint jprint = JasperFillManager.fillReport(jrpt, parameters,  _Cnn);
            JasperViewer.viewReport(jprint, false);
        }catch (SQLException | JRException ex){
            JOptionPane.showConfirmDialog(null, "Error method cetakLaporanMahasiswa2 : " + ex,
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        txtCari = new Tool.Custom_TextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbDataMember = new javax.swing.JTable();
        btnHapus = new Tool.Custom_Button();
        btnTambah = new Tool.Custom_Button();
        lblRecord = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        panelCrud = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        txtIdMember = new Tool.Custom_TextField();
        txtNamaMember = new Tool.Custom_TextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAlamat = new javax.swing.JTextArea();
        rbLaki = new javax.swing.JRadioButton();
        rbPerempuan = new javax.swing.JRadioButton();
        btnSimpan = new Tool.Custom_Button();
        btnBatal = new Tool.Custom_Button();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        dtTglDaftar = new com.toedter.calendar.JDateChooser();
        txtNoTelp = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        btnCetak = new Tool.Custom_Button();

        setClosable(true);
        setPreferredSize(new java.awt.Dimension(1205, 585));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "(+) DATA MEMBER", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 12))); // NOI18N

        txtCari.setPlaceholder("PENCARIAN :  NAMA MEMBER");
        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCariKeyTyped(evt);
            }
        });

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "KLIK 2X --> MENGUBAH / MENGHAPUS DATA", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 12))); // NOI18N

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
        jScrollPane1.setViewportView(tbDataMember);

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

        jLabel5.setFont(new java.awt.Font("Agency FB", 3, 14)); // NOI18N
        jLabel5.setForeground(java.awt.Color.red);
        jLabel5.setText("* DISARANKAN MENGGUNKAN HURUF KAPITAL !");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 828, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(3, 3, 3)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRecord))
                .addContainerGap())
        );

        panelCrud.setBackground(new java.awt.Color(0, 0, 0));

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/2.png"))); // NOI18N

        jLabel9.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("DATA MEMBER");

        jPanel7.setBackground(new java.awt.Color(0, 0, 0));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "(+)", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 12))); // NOI18N

        txtIdMember.setEditable(false);
        txtIdMember.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtIdMember.setPlaceholder("ID. MEMBER");

        txtNamaMember.setPlaceholder("NAMA MEMBER");
        txtNamaMember.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNamaMemberKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNamaMemberKeyTyped(evt);
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

        rbLaki.setBackground(new java.awt.Color(0, 0, 0));
        buttonGroup1.add(rbLaki);
        rbLaki.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        rbLaki.setForeground(new java.awt.Color(255, 255, 255));
        rbLaki.setText("LAKI-LAKI");
        rbLaki.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                rbLakiKeyPressed(evt);
            }
        });

        rbPerempuan.setBackground(new java.awt.Color(0, 0, 0));
        buttonGroup1.add(rbPerempuan);
        rbPerempuan.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        rbPerempuan.setForeground(new java.awt.Color(255, 255, 255));
        rbPerempuan.setText("PEREMPUAN");
        rbPerempuan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                rbPerempuanKeyPressed(evt);
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
        btnBatal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnBatalKeyPressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("NO. TELEPON");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("TANGGAL DAFTAR");

        dtTglDaftar.setEnabled(false);

        txtNoTelp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNoTelpKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNoTelpKeyReleased(evt);
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
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtNamaMember, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIdMember, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(dtTglDaftar, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(rbPerempuan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(rbLaki, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtIdMember, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNamaMember, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dtTglDaftar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(rbLaki)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbPerempuan)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNoTelp, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addGap(1, 1, 1)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        btnCetak.setForeground(new java.awt.Color(255, 255, 255));
        btnCetak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/print_preview.png"))); // NOI18N
        btnCetak.setText("CETAK KARTU");
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCrudLayout = new javax.swing.GroupLayout(panelCrud);
        panelCrud.setLayout(panelCrudLayout);
        panelCrudLayout.setHorizontalGroup(
            panelCrudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelCrudLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCrudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCetak, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addGroup(panelCrudLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelCrudLayout.setVerticalGroup(
            panelCrudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCrudLayout.createSequentialGroup()
                .addGroup(panelCrudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCrudLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel9))
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(btnCetak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        enableForm(); auto_IdMember(); clearForm(); 
    }//GEN-LAST:event_btnTambahActionPerformed

    private void txtCariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyTyped
        cariMember();
    }//GEN-LAST:event_txtCariKeyTyped

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        aksiHapus();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void tbDataMemberMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDataMemberMouseClicked
        if(evt.getClickCount()==2){
            vid_member = tbDataMember.getValueAt(tbDataMember.getSelectedRow(), 0).toString();
            btnHapus.setEnabled(true);
            getDataMember();
            btnSimpan.setText("UBAH DATA");
            btnCetak.setVisible(true);
       }
    }//GEN-LAST:event_tbDataMemberMouseClicked

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        clearForm(); disableForm();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if(txtNamaMember.getText().equals("")){
            JOptionPane.showMessageDialog(null, "NAMA MEMBER HARUS DIISI !",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            txtNamaMember.requestFocus();
        }else if(buttonGroup1.isSelected(null)){
            JOptionPane.showMessageDialog(this, "JENIS KELAMIN BELUM DIPILIH !",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtNoTelp.getText().equals("")){
            JOptionPane.showMessageDialog(null, "NO TELEPON HARUS DIISI !",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            txtNoTelp.requestFocus();
        }else if ( txtNoTelp.getText().length() < 6 ) {
            JOptionPane.showMessageDialog(null, "NO TELEPON HARUS LEBIH DARI 5 !",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            txtNoTelp.requestFocus();
        }else if(txtAlamat.getText().equals("")){
            JOptionPane.showMessageDialog(null, "ALAMAT HARUS DIISI !",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            txtAlamat.requestFocus();
        }else{
            aksiSimpan(); btnSimpan.setText("SIMPAN");
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakActionPerformed
        cetakKartu();
    }//GEN-LAST:event_btnCetakActionPerformed

    private void txtNamaMemberKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNamaMemberKeyTyped
        if ( txtNamaMember.getText().length() == 40 ) {
        evt.consume();
    }
    }//GEN-LAST:event_txtNamaMemberKeyTyped

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

    private void txtNamaMemberKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNamaMemberKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            rbLaki.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtAlamat.requestFocus();
        }
    }//GEN-LAST:event_txtNamaMemberKeyPressed

    private void rbLakiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rbLakiKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            rbPerempuan.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtNamaMember.requestFocus();
        }
    }//GEN-LAST:event_rbLakiKeyPressed

    private void rbPerempuanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rbPerempuanKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            txtNoTelp.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            rbLaki.requestFocus();
        }
    }//GEN-LAST:event_rbPerempuanKeyPressed

    private void txtNoTelpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoTelpKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            txtAlamat.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            rbPerempuan.requestFocus();
        }
    }//GEN-LAST:event_txtNoTelpKeyPressed

    private void txtAlamatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAlamatKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            btnSimpan.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtNoTelp.requestFocus();
        }
    }//GEN-LAST:event_txtAlamatKeyPressed

    private void btnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSimpanKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            btnBatal.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            txtAlamat.requestFocus();
        }
        if(evt.getKeyCode() == evt.VK_ENTER)
        btnSimpan.doClick();
    }//GEN-LAST:event_btnSimpanKeyPressed

    private void btnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnBatalKeyPressed
        if (evt.getKeyCode() == evt.VK_DOWN){
            txtNamaMember.requestFocus();
        }
        if (evt.getKeyCode() == evt.VK_UP){
            btnSimpan.requestFocus();
        }
        if(evt.getKeyCode() == evt.VK_ENTER)
        btnBatal.doClick();
    }//GEN-LAST:event_btnBatalKeyPressed

    private void txtNoTelpKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoTelpKeyReleased
        aksiCek();
    }//GEN-LAST:event_txtNoTelpKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Tool.Custom_Button btnBatal;
    private Tool.Custom_Button btnCetak;
    private Tool.Custom_Button btnHapus;
    private Tool.Custom_Button btnSimpan;
    private Tool.Custom_Button btnTambah;
    private javax.swing.ButtonGroup buttonGroup1;
    private com.toedter.calendar.JDateChooser dtTglDaftar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblRecord;
    private javax.swing.JPanel panelCrud;
    private javax.swing.JRadioButton rbLaki;
    private javax.swing.JRadioButton rbPerempuan;
    private javax.swing.JTable tbDataMember;
    private javax.swing.JTextArea txtAlamat;
    private Tool.Custom_TextField txtCari;
    private Tool.Custom_TextField txtIdMember;
    private Tool.Custom_TextField txtNamaMember;
    private javax.swing.JTextField txtNoTelp;
    // End of variables declaration//GEN-END:variables
}
