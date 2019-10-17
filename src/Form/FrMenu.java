package Form;

import Tool.KoneksiDB;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class FrMenu extends javax.swing.JFrame {
    
    KoneksiDB getCnn = new KoneksiDB();
    Connection _Cnn;
    private static String u_id;
    private static String u_username;
    private static String u_hakakses;
    
    public static String getU_id() {
        return u_id;
    }
    public static void setU_id(String u_id) {
        FrMenu.u_id = u_id;
    }
    
    public static String getU_username() {
        return u_username;
    }
    public static void setU_username(String u_username) {
        FrMenu.u_username = u_username;
    }
    
    public static String getU_hakakses() {
        return u_hakakses;
    }
    public static void setU_hakakses(String u_hakakses) {
        FrMenu.u_hakakses = u_hakakses;
    }
    public FrMenu() {
        initComponents();
        TanggalOtomatis();
        setJam();
        this.setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);
        FrBantuan internal;
        internal = new FrBantuan(); 
        internal.setVisible(true);
        desktopPane1.add(internal);
        panelLogin.setVisible(false);
        mnPegawai.setEnabled(false);
        mnTentang.setEnabled(true);
        btnBarang.setEnabled(false);
        btnCetakLaporan.setEnabled(false);
        btnDistributor.setEnabled(false);
        btnBackRes.setEnabled(false);
        btnKategoriBarang.setEnabled(false);
        btnMember.setEnabled(false);
        btnReturPembelian.setEnabled(false);
        btnReturPenjalan.setEnabled(false);
        btnTransPembelian.setEnabled(false);
        btnTransPenjualan.setEnabled(false);
        txtUsername.setText("ID001");
        txtPassword.setText("ADMIN12345");
    }
    private void aksi_login(){
        try{
            Connection _Cnn;
            KoneksiDB getCnn = new KoneksiDB();
            _Cnn = getCnn.getConnection();
            String sql = " select id_user, nama_lengkap, password, hak_akses "
                    + " from tb_user WHERE id_user='"+txtUsername.getText().replaceAll("'", "")+"' "
                    + " and password = '"+txtPassword.getText().replaceAll("'", "")+"'";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sql);
            if(res.next()){
                u_id = res.getString("id_user");
                u_username = res.getString("nama_lengkap");
                u_hakakses = res.getString("hak_akses");
                panelLogin.setVisible(false);
                switch (u_hakakses) {
            case "PEMILIK TOKO":
                loginAdmin();
                break;
            case "KASIR":
                loginKasir();
                break;
            }  
            lblId.setText("ID. USER : "+u_id+"     ||");
            lblUsername.setText("NAMA : "+u_username);
            lblHak_akses.setText("HAK AKSES : " +u_hakakses);
            desktopPane1.removeAll();
            desktopPane1.repaint();
            }
            else{
                JOptionPane.showMessageDialog(this, "PERIKSA KEMBALI !");
            }
        }catch(Exception ex){}
    }
    public void TanggalOtomatis(){
        Date tanggal = new Date();
        tgl.setText(""+ (String.format("%1$td/%1$tm/%1$tY",tanggal)));
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

        lbljam.setText(jam + ":" + menit + ":" + detik + " ");
        }
      };
      new Timer(1000, taskPerformer).start();
    }
    public void loginAdmin(){
        mnPegawai.setEnabled(true);
        mnTentang.setEnabled(true);
        btnBarang.setEnabled(true);
        btnCetakLaporan.setEnabled(true);
        btnDistributor.setEnabled(true);
        btnKategoriBarang.setEnabled(true);
        btnMember.setEnabled(true);
        btnBackRes.setEnabled(true);
        btnReturPembelian.setEnabled(true);
        btnReturPenjalan.setEnabled(true);
        btnTransPembelian.setEnabled(true);
        btnTransPenjualan.setEnabled(true);
        btnLogin.setVisible(false);
        mnLogin.setText("LOGOUT");
        
    }
    public void loginKasir(){
        mnPegawai.setEnabled(false);
        mnTentang.setEnabled(true);
        btnBarang.setEnabled(true);
        btnDistributor.setEnabled(true);
        btnKategoriBarang.setEnabled(true);
        btnMember.setEnabled(true);
        btnReturPembelian.setEnabled(true);
        btnReturPenjalan.setEnabled(true);
        btnTransPembelian.setEnabled(true);
        btnTransPenjualan.setEnabled(true);
        btnCetakLaporan.setEnabled(false);
        btnLogin.setVisible(false);
        mnLogin.setText("LOGOUT");
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        btnDistributor = new Tool.Custom_Button();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        btnBarang = new Tool.Custom_Button();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        btnKategoriBarang = new Tool.Custom_Button();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        btnMember = new Tool.Custom_Button();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        btnCetakLaporan = new Tool.Custom_Button();
        jSeparator12 = new javax.swing.JToolBar.Separator();
        btnBackRes = new Tool.Custom_Button();
        jSeparator13 = new javax.swing.JToolBar.Separator();
        jToolBar2 = new javax.swing.JToolBar();
        btnTransPenjualan = new Tool.Custom_Button();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnReturPenjalan = new Tool.Custom_Button();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        btnTransPembelian = new Tool.Custom_Button();
        jSeparator10 = new javax.swing.JToolBar.Separator();
        btnReturPembelian = new Tool.Custom_Button();
        jSeparator11 = new javax.swing.JToolBar.Separator();
        EXIT = new Tool.Custom_Button();
        jPanel1 = new javax.swing.JPanel();
        tgl = new javax.swing.JLabel();
        lbljam = new javax.swing.JLabel();
        btnLogin = new Tool.Custom_Button();
        jPanel2 = new javax.swing.JPanel();
        lblHak_akses = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        desktopPane1 = new Tool.BG_Silub();
        panelLogin = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        btnBatal = new javax.swing.JButton();
        btnLoginSesion = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mnLogin = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        mnPegawai = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        mnTentang = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jToolBar1.setBackground(new java.awt.Color(204, 204, 255));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnDistributor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Suplier.png"))); // NOI18N
        btnDistributor.setText("DISTRIBUTOR");
        btnDistributor.setFocusable(false);
        btnDistributor.setFont(new java.awt.Font("Arial Narrow", 0, 12)); // NOI18N
        btnDistributor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDistributor.setPreferredSize(new java.awt.Dimension(75, 89));
        btnDistributor.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDistributor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDistributorActionPerformed(evt);
            }
        });
        jToolBar1.add(btnDistributor);
        jToolBar1.add(jSeparator1);
        jToolBar1.add(jSeparator7);

        btnBarang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/2 - Master.png"))); // NOI18N
        btnBarang.setText("BARANG");
        btnBarang.setFocusable(false);
        btnBarang.setFont(new java.awt.Font("Arial Narrow", 0, 12)); // NOI18N
        btnBarang.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBarang.setPreferredSize(new java.awt.Dimension(70, 89));
        btnBarang.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBarangActionPerformed(evt);
            }
        });
        jToolBar1.add(btnBarang);
        jToolBar1.add(jSeparator8);

        btnKategoriBarang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/2 - Master.png"))); // NOI18N
        btnKategoriBarang.setText("KATEGORI");
        btnKategoriBarang.setFocusable(false);
        btnKategoriBarang.setFont(new java.awt.Font("Arial Narrow", 0, 12)); // NOI18N
        btnKategoriBarang.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnKategoriBarang.setPreferredSize(new java.awt.Dimension(70, 89));
        btnKategoriBarang.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnKategoriBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKategoriBarangActionPerformed(evt);
            }
        });
        jToolBar1.add(btnKategoriBarang);
        jToolBar1.add(jSeparator2);
        jToolBar1.add(jSeparator6);

        btnMember.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/2.png"))); // NOI18N
        btnMember.setText("MEMBER");
        btnMember.setFont(new java.awt.Font("Arial Narrow", 0, 12)); // NOI18N
        btnMember.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMember.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemberActionPerformed(evt);
            }
        });
        jToolBar1.add(btnMember);
        jToolBar1.add(jSeparator4);

        btnCetakLaporan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/print2.png"))); // NOI18N
        btnCetakLaporan.setText("CETAK LAPORAN");
        btnCetakLaporan.setFont(new java.awt.Font("Arial Narrow", 0, 12)); // NOI18N
        btnCetakLaporan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCetakLaporan.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCetakLaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakLaporanActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCetakLaporan);
        jToolBar1.add(jSeparator12);

        btnBackRes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/folder_search.png"))); // NOI18N
        btnBackRes.setText("BACKUP AND RESTORE");
        btnBackRes.setFocusable(false);
        btnBackRes.setFont(new java.awt.Font("Arial Narrow", 0, 12)); // NOI18N
        btnBackRes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBackRes.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBackRes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackResActionPerformed(evt);
            }
        });
        jToolBar1.add(btnBackRes);
        jToolBar1.add(jSeparator13);

        jToolBar2.setBackground(new java.awt.Color(204, 204, 255));
        jToolBar2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToolBar2.setFloatable(false);
        jToolBar2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jToolBar2.setRollover(true);

        btnTransPenjualan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Shopping_Cart.png"))); // NOI18N
        btnTransPenjualan.setText("TRANSAKSI PENJUALAN");
        btnTransPenjualan.setToolTipText("MENGOLAH DATA PENJUALAN");
        btnTransPenjualan.setFont(new java.awt.Font("Arial Narrow", 0, 12)); // NOI18N
        btnTransPenjualan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTransPenjualan.setPreferredSize(new java.awt.Dimension(135, 89));
        btnTransPenjualan.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnTransPenjualan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransPenjualanActionPerformed(evt);
            }
        });
        jToolBar2.add(btnTransPenjualan);

        jSeparator3.setPreferredSize(new java.awt.Dimension(3, 6));
        jToolBar2.add(jSeparator3);

        btnReturPenjalan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/box-in.png"))); // NOI18N
        btnReturPenjalan.setText("RETUR PENJUALAN");
        btnReturPenjalan.setFocusable(false);
        btnReturPenjalan.setFont(new java.awt.Font("Arial Narrow", 0, 12)); // NOI18N
        btnReturPenjalan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReturPenjalan.setPreferredSize(new java.awt.Dimension(127, 89));
        btnReturPenjalan.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReturPenjalan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReturPenjalanActionPerformed(evt);
            }
        });
        jToolBar2.add(btnReturPenjalan);
        jToolBar2.add(jSeparator5);

        btnTransPembelian.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Shopping_Cart.png"))); // NOI18N
        btnTransPembelian.setText("TRANSAKSI PEMBELIAN");
        btnTransPembelian.setFont(new java.awt.Font("Arial Narrow", 0, 12)); // NOI18N
        btnTransPembelian.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTransPembelian.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnTransPembelian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransPembelianActionPerformed(evt);
            }
        });
        jToolBar2.add(btnTransPembelian);
        jToolBar2.add(jSeparator10);

        btnReturPembelian.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/box-out.png"))); // NOI18N
        btnReturPembelian.setText("RETUR PEMBELIAN");
        btnReturPembelian.setFocusable(false);
        btnReturPembelian.setFont(new java.awt.Font("Arial Narrow", 0, 12)); // NOI18N
        btnReturPembelian.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReturPembelian.setPreferredSize(new java.awt.Dimension(127, 89));
        btnReturPembelian.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReturPembelian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReturPembelianActionPerformed(evt);
            }
        });
        jToolBar2.add(btnReturPembelian);
        jToolBar2.add(jSeparator11);

        EXIT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/close.png"))); // NOI18N
        EXIT.setText("EXIT");
        EXIT.setFocusable(false);
        EXIT.setFont(new java.awt.Font("Arial Narrow", 0, 12)); // NOI18N
        EXIT.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        EXIT.setPreferredSize(new java.awt.Dimension(127, 89));
        EXIT.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        EXIT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EXITActionPerformed(evt);
            }
        });
        jToolBar2.add(EXIT);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        tgl.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        tgl.setForeground(new java.awt.Color(255, 0, 0));
        tgl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tgl.setText("jLabel1");
        tgl.setOpaque(true);

        lbljam.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        lbljam.setForeground(new java.awt.Color(255, 0, 0));
        lbljam.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbljam.setText("jLabel2");
        lbljam.setOpaque(true);

        btnLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnLogin.setText("LOGIN");
        btnLogin.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));

        lblHak_akses.setFont(new java.awt.Font("Agency FB", 1, 16)); // NOI18N
        lblHak_akses.setForeground(java.awt.SystemColor.window);

        lblId.setFont(new java.awt.Font("Agency FB", 1, 16)); // NOI18N
        lblId.setForeground(java.awt.SystemColor.window);

        lblUsername.setFont(new java.awt.Font("Agency FB", 1, 16)); // NOI18N
        lblUsername.setForeground(java.awt.SystemColor.window);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator9)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblHak_akses)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblId, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblHak_akses)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblId)
                    .addComponent(lblUsername))
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbljam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tgl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tgl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbljam)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panelLogin.setBackground(new java.awt.Color(0, 153, 153));
        panelLogin.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true), "(+) SISTEM LOGIN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("ID. USER");

        txtUsername.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUsernameKeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("PASSWORD");

        txtPassword.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPasswordKeyPressed(evt);
            }
        });

        btnBatal.setText("BATAL");

        btnLoginSesion.setText("LOGIN");
        btnLoginSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginSesionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLoginLayout = new javax.swing.GroupLayout(panelLogin);
        panelLogin.setLayout(panelLoginLayout);
        panelLoginLayout.setHorizontalGroup(
            panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLoginLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelLoginLayout.createSequentialGroup()
                        .addComponent(btnLoginSesion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBatal))
                    .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2)
                        .addComponent(txtUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                        .addComponent(txtPassword)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelLoginLayout.setVerticalGroup(
            panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLoginLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLoginSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout desktopPane1Layout = new javax.swing.GroupLayout(desktopPane1);
        desktopPane1.setLayout(desktopPane1Layout);
        desktopPane1Layout.setHorizontalGroup(
            desktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, desktopPane1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        desktopPane1Layout.setVerticalGroup(
            desktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(desktopPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenu1.setMnemonic('s');
        jMenu1.setText("Sistem");
        jMenu1.setFont(new java.awt.Font("Agency FB", 1, 16)); // NOI18N

        mnLogin.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK));
        mnLogin.setText("LOGIN");
        mnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnLoginActionPerformed(evt);
            }
        });
        jMenu1.add(mnLogin);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Data Personal");
        jMenu3.setFont(new java.awt.Font("Agency FB", 1, 16)); // NOI18N

        mnPegawai.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.SHIFT_MASK));
        mnPegawai.setText("Data Pegawai");
        mnPegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPegawaiActionPerformed(evt);
            }
        });
        jMenu3.add(mnPegawai);

        jMenuBar1.add(jMenu3);

        jMenu5.setText("Tentang");
        jMenu5.setFont(new java.awt.Font("Agency FB", 1, 16)); // NOI18N

        mnTentang.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mnTentang.setText("Tentang Kami");
        mnTentang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnTentangActionPerformed(evt);
            }
        });
        jMenu5.add(mnTentang);

        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(desktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(desktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTransPembelianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransPembelianActionPerformed
        FrTrans_Pembelian internal;
        
        desktopPane1.removeAll();
        desktopPane1.repaint();
        internal = new FrTrans_Pembelian();
        internal.setVisible(true);
        try{
            internal.setMaximum(true);
        }catch(Exception e) {
            
        }
            desktopPane1.add(internal);
    }//GEN-LAST:event_btnTransPembelianActionPerformed

    private void btnTransPenjualanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransPenjualanActionPerformed
        FrTrans_Penjualan internal;
        
        desktopPane1.removeAll();
        desktopPane1.repaint();
        internal = new FrTrans_Penjualan();
        internal.setVisible(true);
        try{
            internal.setMaximum(true);
        }catch(Exception e) {
            
        }
            desktopPane1.add(internal);
    }//GEN-LAST:event_btnTransPenjualanActionPerformed

    private void btnMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMemberActionPerformed
        FrMember internal;
        desktopPane1.removeAll();
        desktopPane1.repaint();
        internal = new FrMember();
        internal.setVisible(true);
        try{
            internal.setMaximum(true);
        }catch(Exception e) {
            
        }
            desktopPane1.add(internal);
    }//GEN-LAST:event_btnMemberActionPerformed

    private void btnCetakLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakLaporanActionPerformed
        Laporan internal;
        desktopPane1.removeAll();
        desktopPane1.repaint();
        internal = new Laporan();
        internal.setVisible(true);
        try{
            internal.setMaximum(true);
        }catch(Exception e) {
            
        }
            desktopPane1.add(internal);
    }//GEN-LAST:event_btnCetakLaporanActionPerformed

    private void btnReturPembelianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReturPembelianActionPerformed
        FrRetur_Pembelian internal;
        
        desktopPane1.removeAll();
        desktopPane1.repaint();
        internal = new FrRetur_Pembelian();
        internal.setVisible(true);
        try{
            internal.setMaximum(true);
        }catch(Exception e) {
            
        }
            desktopPane1.add(internal);
    }//GEN-LAST:event_btnReturPembelianActionPerformed

    private void EXITActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EXITActionPerformed
        int jawab = JOptionPane.showConfirmDialog(this,"APAKAH ANDA AKAN KELUAR DARI APLIKASI ? ", "Konfirmasi",JOptionPane.YES_NO_OPTION);
            if(jawab==JOptionPane.YES_OPTION){
            System.exit(0);
            }
    }//GEN-LAST:event_EXITActionPerformed

    private void btnDistributorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDistributorActionPerformed
        FrDistributor internal;
        
        desktopPane1.removeAll();
        desktopPane1.repaint();
        internal = new FrDistributor();
        internal.setVisible(true);
        try{
            internal.setMaximum(true);
        }catch(Exception e) {
            
        }
            desktopPane1.add(internal);
    }//GEN-LAST:event_btnDistributorActionPerformed

    private void btnBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBarangActionPerformed
        FrBarang internal;
        
        desktopPane1.removeAll();
        desktopPane1.repaint();
        internal = new FrBarang();
        internal.setVisible(true);
        try{
            internal.setMaximum(true);
        }catch(Exception e) {
            
        }
            desktopPane1.add(internal);
    }//GEN-LAST:event_btnBarangActionPerformed

    private void btnKategoriBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKategoriBarangActionPerformed
        FrKategori internal;
        
        desktopPane1.removeAll();
        desktopPane1.repaint();
        internal = new FrKategori();
        internal.setVisible(true);
        try{
            internal.setMaximum(true);
        }catch(Exception e) {
            
        }
            desktopPane1.add(internal);
    }//GEN-LAST:event_btnKategoriBarangActionPerformed

    private void mnPegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPegawaiActionPerformed
        FrUser internal;
        
        desktopPane1.removeAll();
        desktopPane1.repaint();
        internal = new FrUser();
        internal.setVisible(true);
        try{
            internal.setMaximum(true);
        }catch(Exception e) {
            
        }
            desktopPane1.add(internal);
    }//GEN-LAST:event_mnPegawaiActionPerformed

    private void btnReturPenjalanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReturPenjalanActionPerformed
        FrRetur_Penjualan internal;
        
        desktopPane1.removeAll();
        desktopPane1.repaint();
        internal = new FrRetur_Penjualan();
        internal.setVisible(true);
        try{
            internal.setMaximum(true);
        }catch(Exception e) {
            
        }
            desktopPane1.add(internal);
    }//GEN-LAST:event_btnReturPenjalanActionPerformed

    private void mnTentangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnTentangActionPerformed
        FrBantuan internal;
        
        desktopPane1.removeAll();
        desktopPane1.repaint();
        internal = new FrBantuan();
        internal.setVisible(true);
        try{
            internal.setMaximum(true);
        }catch(Exception e) {
            
        }
            desktopPane1.add(internal);
    }//GEN-LAST:event_mnTentangActionPerformed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        if(btnLogin.getText().equals("LOGIN")){
            panelLogin.setVisible(true);
            txtUsername.requestFocus();
        }else{
            int jawab = JOptionPane.showConfirmDialog(this,"APAKAH ANDA AKAN KELUAR DARI APLIKASI ? ", "Konfirmasi",JOptionPane.YES_NO_OPTION);
            if(jawab==JOptionPane.YES_OPTION){
            dispose();
            FrMenu m = new FrMenu();
            m.setVisible(true);
        }
        } 
    }//GEN-LAST:event_btnLoginActionPerformed

    private void mnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnLoginActionPerformed
        if(mnLogin.getText().equals("LOGIN")){
            panelLogin.setVisible(true);
            txtUsername.requestFocus();
        }else{
            int jawab = JOptionPane.showConfirmDialog(this,"APAKAH ANDA AKAN KELUAR DARI APLIKASI ? ", "Konfirmasi",JOptionPane.YES_NO_OPTION);
            if(jawab==JOptionPane.YES_OPTION){
            dispose();
            FrMenu m = new FrMenu();
            m.setVisible(true);
            }
        }
    }//GEN-LAST:event_mnLoginActionPerformed

    private void btnBackResActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackResActionPerformed
        BackupRestore internal;
        desktopPane1.removeAll();
        desktopPane1.repaint();
        internal = new BackupRestore();
        internal.setVisible(true);
        try{
            internal.setMaximum(true);
        }catch(Exception e) {

        }
        desktopPane1.add(internal);
    }//GEN-LAST:event_btnBackResActionPerformed

    private void btnLoginSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginSesionActionPerformed
        aksi_login();
    }//GEN-LAST:event_btnLoginSesionActionPerformed

    private void txtPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER)
        btnLoginSesion.doClick();
    }//GEN-LAST:event_txtPasswordKeyPressed

    private void txtUsernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsernameKeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER){
            txtPassword.requestFocus();
        }
    }//GEN-LAST:event_txtUsernameKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Tool.Custom_Button EXIT;
    private Tool.Custom_Button btnBackRes;
    private Tool.Custom_Button btnBarang;
    private javax.swing.JButton btnBatal;
    private Tool.Custom_Button btnCetakLaporan;
    private Tool.Custom_Button btnDistributor;
    private Tool.Custom_Button btnKategoriBarang;
    private Tool.Custom_Button btnLogin;
    private javax.swing.JButton btnLoginSesion;
    private Tool.Custom_Button btnMember;
    private Tool.Custom_Button btnReturPembelian;
    private Tool.Custom_Button btnReturPenjalan;
    private Tool.Custom_Button btnTransPembelian;
    private Tool.Custom_Button btnTransPenjualan;
    private Tool.BG_Silub desktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator10;
    private javax.swing.JToolBar.Separator jSeparator11;
    private javax.swing.JToolBar.Separator jSeparator12;
    private javax.swing.JToolBar.Separator jSeparator13;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JLabel lblHak_akses;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JLabel lbljam;
    private javax.swing.JMenuItem mnLogin;
    private javax.swing.JMenuItem mnPegawai;
    private javax.swing.JMenuItem mnTentang;
    private javax.swing.JPanel panelLogin;
    private javax.swing.JLabel tgl;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
