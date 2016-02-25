package everteam;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.xswingx.PromptSupport;
import org.opencv.core.Core;
import org.opencv.core.Point;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class FrmEnrollment extends javax.swing.JFrame {

    private DefaultTableModel model;
    private JTable tblPerson;
    private JScrollPane scrollPane;
    private PersonBO person;
    private OriginalImage originalImage;
    private int selectedID;
    private DrawPanel pnlImage = new DrawPanel();
    
    /**
     * Creates new form FrmContacts
     */
    public FrmEnrollment() throws Exception {
        initComponents();
        
        selectedID = -1;
        
        //Initialize the JTable
        person = new PersonBO();
        originalImage = new OriginalImage();
        String[] columnNames = new String[]{
            "ID", "First Name", "Last Name", "Gender", "Race", "Hair Color", "Eye Color", "Height", "Weight", "Date Of Birth", "Nationality", "Remarks"
        };
        
        model = new DefaultTableModel(new Object[0][0], columnNames){
            //Make the cells uneditable
            public boolean isCellEditable(int row, int column) 
            {return false;}
        };
        
        List<PersonBO> listPersons = person.getPersons(-1);
        
        for (PersonBO p : listPersons){
            model.addRow(p.getRow(p));
        }
        
        //Fill Person Table
        tblPerson = new JTable(model);
        tblPerson.setFont(new Font("Arial", Font.BOLD, 12));
        tblPerson.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPerson.getTableHeader().setBackground(new Color(204, 222, 240));
        scrollPane = new JScrollPane(tblPerson);
        scrollPane.getViewport().setBackground(new Color(232, 238, 244));
        tabView.add(scrollPane);
        scrollPane.setBounds(0, 75, tabView.getWidth() - 76, tabView.getHeight() -76);
        arrangeTable();
        
        //Initialize Button Group(bgGender)
        bgGender = new ButtonGroup();
        bgGender.add(rbMale);
        bgGender.add(rbFemale);
        rbMale.setSelected(true);
        bgGender1 = new ButtonGroup();
        bgGender1.add(rbMale1);
        bgGender1.add(rbFemale1);
        rbMale1.setSelected(true);

        //Initialize Date Comboboxes
        cmbDay.setModel(new DefaultComboBoxModel(getDay()));
        cmbMonth.setModel(new DefaultComboBoxModel(getMonth()));
        cmbYear.setModel(new DefaultComboBoxModel(getYear()));
        cmbDay1.setModel(new DefaultComboBoxModel(getDay()));
        cmbMonth1.setModel(new DefaultComboBoxModel(getMonth()));
        cmbYear1.setModel(new DefaultComboBoxModel(getYear()));

        //add a prompt to Search TextFields
        PromptSupport.setPrompt("Search", txtSearch);

        //Add icons to the tabs
        ImageIcon tabViewIcon = new ImageIcon(this.getClass().getResource("tabViewIcon.png"));
        tabPerson.setIconAt(0, tabViewIcon);
        ImageIcon tabNewIcon = new ImageIcon(this.getClass().getResource("tabNewIcon.png"));
        tabPerson.setIconAt(1, tabNewIcon);
        ImageIcon tabEditIcon = new ImageIcon(this.getClass().getResource("tabEditIcon.png"));
        tabPerson.setIconAt(2, tabEditIcon);

        //Disable tabEdit
        resetTabEdit();

        //Add Table Listener
        tblPerson.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            //Fill tabEdit from selected row in the person table
            public void valueChanged(ListSelectionEvent e) {
                if (tblPerson.getSelectedRow() == -1) {
                    selectedID = -1;
                    return;
                }

                selectedID = Integer.parseInt(model.getValueAt(tblPerson.getSelectedRow(), 0).toString());
                if (model.getValueAt(tblPerson.getSelectedRow(), 1) != null) {
                    txtFirstName1.setText(model.getValueAt(tblPerson.getSelectedRow(), 1).toString());
                } else {
                    txtFirstName1.setText("");
                }
                if (model.getValueAt(tblPerson.getSelectedRow(), 2) != null) {
                    txtLastName1.setText(model.getValueAt(tblPerson.getSelectedRow(), 2).toString());
                } else {
                    txtLastName1.setText("");
                }
                if (model.getValueAt(tblPerson.getSelectedRow(), 3) != null) {
                    //rbMale1.setSelected((model.getValueAt(tblPerson.getSelectedRow(), 3).toString().equals("true") ? true : false));
                    rbFemale1.setSelected(false);
                } else {
                    rbMale1.setSelected(true);
                }
                if (model.getValueAt(tblPerson.getSelectedRow(), 4) != null) {
                    txtRace1.setText(model.getValueAt(tblPerson.getSelectedRow(), 4).toString());
                } else {
                    txtRace1.setText("");
                }
                if (model.getValueAt(tblPerson.getSelectedRow(), 5) != null) {
                    txtHairColor1.setText(model.getValueAt(tblPerson.getSelectedRow(), 5).toString());
                } else {
                    txtHairColor1.setText("");
                }
                if (model.getValueAt(tblPerson.getSelectedRow(), 6) != null) {
                    txtEyeColor1.setText(model.getValueAt(tblPerson.getSelectedRow(), 6).toString());
                } else {
                    txtEyeColor1.setText("");
                }
                if (model.getValueAt(tblPerson.getSelectedRow(), 7) != null) {
                    txtHeight1.setText(model.getValueAt(tblPerson.getSelectedRow(), 7).toString());
                } else {
                    txtHeight1.setText("");
                }
                if (model.getValueAt(tblPerson.getSelectedRow(), 8) != null) {
                    txtWeight1.setText(model.getValueAt(tblPerson.getSelectedRow(), 8).toString());
                } else {
                    txtWeight1.setText("");
                }
                if (model.getValueAt(tblPerson.getSelectedRow(), 9) != null) {
                    String[] dateOfBirth = model.getValueAt(tblPerson.getSelectedRow(), 9).toString().split("-");
                    cmbYear1.setSelectedIndex(0);
                    cmbDay1.setSelectedIndex(Integer.parseInt(dateOfBirth[1]) - 1);
                    cmbMonth1.setSelectedIndex(Integer.parseInt(dateOfBirth[2]) - 1);
                } else {
                    cmbYear1.setSelectedIndex(0);
                    cmbDay1.setSelectedIndex(0);
                    cmbMonth1.setSelectedIndex(0);
                }
                if (model.getValueAt(tblPerson.getSelectedRow(), 10) != null) {
                    txtNationality1.setText(model.getValueAt(tblPerson.getSelectedRow(), 10).toString());
                } else {
                    txtNationality1.setText("");
                }
                if (model.getValueAt(tblPerson.getSelectedRow(), 11) != null) {
                    txtRemarks1.setText(model.getValueAt(tblPerson.getSelectedRow(), 11).toString());
                } else {
                    txtRemarks1.setText("");
                }

                tabPerson.setEnabledAt(2, true);
            }
        });

        //Handle the Form Closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int reply = JOptionPane.showConfirmDialog(null, "Are You Sure You Want To Exit The Application?", "Exit", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgGender = new javax.swing.ButtonGroup();
        bgGender1 = new javax.swing.ButtonGroup();
        tabPerson = new javax.swing.JTabbedPane();
        tabView = new javax.swing.JPanel();
        pnlSearch = new javax.swing.JPanel();
        btnSearch = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        cmbAttribute = new javax.swing.JComboBox();
        tabNew = new javax.swing.JPanel();
        pnlName = new javax.swing.JPanel();
        lblFirstName = new javax.swing.JLabel();
        txtFirstName = new javax.swing.JTextField();
        txtLastName = new javax.swing.JTextField();
        lblLastName = new javax.swing.JLabel();
        pnlImages = new javax.swing.JPanel();
        btnBrowse = new javax.swing.JButton();
        lblImage = new javax.swing.JLabel();
        pnlImageContainer = new javax.swing.JPanel();
        pnlInfo = new javax.swing.JPanel();
        lblRace = new javax.swing.JLabel();
        txtRace = new javax.swing.JTextField();
        lblWeight = new javax.swing.JLabel();
        txtWeight = new javax.swing.JTextField();
        lblHairColor = new javax.swing.JLabel();
        txtHairColor = new javax.swing.JTextField();
        txtEyeColor = new javax.swing.JTextField();
        lblEyeColor = new javax.swing.JLabel();
        lblHeight = new javax.swing.JLabel();
        txtHeight = new javax.swing.JTextField();
        txtNationality = new javax.swing.JTextField();
        lblNationality = new javax.swing.JLabel();
        pnlGender = new javax.swing.JPanel();
        rbMale = new javax.swing.JRadioButton();
        rbFemale = new javax.swing.JRadioButton();
        pnlDateOfBirth = new javax.swing.JPanel();
        lblDay = new javax.swing.JLabel();
        cmbDay = new javax.swing.JComboBox();
        lblMonth = new javax.swing.JLabel();
        cmbMonth = new javax.swing.JComboBox();
        cmbYear = new javax.swing.JComboBox();
        lblYear = new javax.swing.JLabel();
        pnlAddress = new javax.swing.JPanel();
        lblRemarks = new javax.swing.JLabel();
        txtRemarks = new javax.swing.JTextField();
        btnCreate = new javax.swing.JButton();
        tabEdit = new javax.swing.JPanel();
        pnlName1 = new javax.swing.JPanel();
        lblFirstName1 = new javax.swing.JLabel();
        txtFirstName1 = new javax.swing.JTextField();
        txtLastName1 = new javax.swing.JTextField();
        lblLastName1 = new javax.swing.JLabel();
        pnlImages1 = new javax.swing.JPanel();
        btnBrowse1 = new javax.swing.JButton();
        lblImage1 = new javax.swing.JLabel();
        pnlInfo1 = new javax.swing.JPanel();
        lblRace1 = new javax.swing.JLabel();
        txtRace1 = new javax.swing.JTextField();
        lblWeight1 = new javax.swing.JLabel();
        txtWeight1 = new javax.swing.JTextField();
        lblHairColor1 = new javax.swing.JLabel();
        txtHairColor1 = new javax.swing.JTextField();
        txtEyeColor1 = new javax.swing.JTextField();
        lblEyeColor1 = new javax.swing.JLabel();
        lblHeight1 = new javax.swing.JLabel();
        txtHeight1 = new javax.swing.JTextField();
        lblNationality1 = new javax.swing.JLabel();
        txtNationality1 = new javax.swing.JTextField();
        pnlGender1 = new javax.swing.JPanel();
        rbMale1 = new javax.swing.JRadioButton();
        rbFemale1 = new javax.swing.JRadioButton();
        pnlDateOfBirth1 = new javax.swing.JPanel();
        lblDay1 = new javax.swing.JLabel();
        cmbDay1 = new javax.swing.JComboBox();
        lblMonth1 = new javax.swing.JLabel();
        cmbMonth1 = new javax.swing.JComboBox();
        cmbYear1 = new javax.swing.JComboBox();
        lblYear1 = new javax.swing.JLabel();
        pnlAddress1 = new javax.swing.JPanel();
        lblRemarks1 = new javax.swing.JLabel();
        txtRemarks1 = new javax.swing.JTextField();
        btnCreate1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("EverTeam");
        setBackground(javax.swing.UIManager.getDefaults().getColor("InternalFrame.inactiveTitleGradient"));
        setResizable(false);

        tabPerson.setBackground(new java.awt.Color(232, 238, 244));
        tabPerson.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        tabPerson.setPreferredSize(new java.awt.Dimension(1200, 700));

        tabView.setBackground(new java.awt.Color(232, 238, 244));
        tabView.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        pnlSearch.setBackground(new java.awt.Color(232, 238, 244));
        pnlSearch.setBorder(javax.swing.BorderFactory.createTitledBorder("Search"));

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/everteam/search.gif"))); // NOI18N
        btnSearch.setPreferredSize(new java.awt.Dimension(53, 25));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        txtSearch.setPreferredSize(new java.awt.Dimension(6, 25));
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        cmbAttribute.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "First Name", "Last Name", "Mobile Phone", "TelePhone", "Gender", "Email", "Date of Birth", "Address" }));

        javax.swing.GroupLayout pnlSearchLayout = new javax.swing.GroupLayout(pnlSearch);
        pnlSearch.setLayout(pnlSearchLayout);
        pnlSearchLayout.setHorizontalGroup(
            pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSearchLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbAttribute, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlSearchLayout.setVerticalGroup(
            pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSearchLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbAttribute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout tabViewLayout = new javax.swing.GroupLayout(tabView);
        tabView.setLayout(tabViewLayout);
        tabViewLayout.setHorizontalGroup(
            tabViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabViewLayout.createSequentialGroup()
                .addContainerGap(737, Short.MAX_VALUE)
                .addComponent(pnlSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        tabViewLayout.setVerticalGroup(
            tabViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabViewLayout.createSequentialGroup()
                .addComponent(pnlSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 625, Short.MAX_VALUE))
        );

        tabPerson.addTab("      View        ", tabView);

        tabNew.setBackground(new java.awt.Color(232, 238, 244));

        pnlName.setBackground(new java.awt.Color(232, 238, 244));
        pnlName.setBorder(javax.swing.BorderFactory.createTitledBorder("Name"));

        lblFirstName.setText("First Name");

        lblLastName.setText("Last Name");

        javax.swing.GroupLayout pnlNameLayout = new javax.swing.GroupLayout(pnlName);
        pnlName.setLayout(pnlNameLayout);
        pnlNameLayout.setHorizontalGroup(
            pnlNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLastName, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblFirstName, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtFirstName, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                    .addComponent(txtLastName))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlNameLayout.setVerticalGroup(
            pnlNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFirstName)
                    .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLastName))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pnlImages.setBackground(new java.awt.Color(232, 238, 244));
        pnlImages.setBorder(javax.swing.BorderFactory.createTitledBorder("Images"));

        btnBrowse.setText("Browse");
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });

        pnlImageContainer.setPreferredSize(new java.awt.Dimension(200, 200));

        javax.swing.GroupLayout pnlImageContainerLayout = new javax.swing.GroupLayout(pnlImageContainer);
        pnlImageContainer.setLayout(pnlImageContainerLayout);
        pnlImageContainerLayout.setHorizontalGroup(
            pnlImageContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        pnlImageContainerLayout.setVerticalGroup(
            pnlImageContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlImagesLayout = new javax.swing.GroupLayout(pnlImages);
        pnlImages.setLayout(pnlImagesLayout);
        pnlImagesLayout.setHorizontalGroup(
            pnlImagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlImagesLayout.createSequentialGroup()
                .addGroup(pnlImagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlImagesLayout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(pnlImageContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(lblImage))
                    .addGroup(pnlImagesLayout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addComponent(btnBrowse)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlImagesLayout.setVerticalGroup(
            pnlImagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlImagesLayout.createSequentialGroup()
                .addGroup(pnlImagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlImagesLayout.createSequentialGroup()
                        .addGap(157, 157, 157)
                        .addComponent(lblImage))
                    .addGroup(pnlImagesLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlImageContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15)
                .addComponent(btnBrowse)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlInfo.setBackground(new java.awt.Color(232, 238, 244));
        pnlInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Info"));

        lblRace.setText("Race");

        lblWeight.setText("Weight");

        lblHairColor.setText("Hair Color");

        lblEyeColor.setText("Eye Color");

        lblHeight.setText("Height");

        lblNationality.setText("Nationality");

        javax.swing.GroupLayout pnlInfoLayout = new javax.swing.GroupLayout(pnlInfo);
        pnlInfo.setLayout(pnlInfoLayout);
        pnlInfoLayout.setHorizontalGroup(
            pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInfoLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNationality)
                    .addComponent(lblRace)
                    .addComponent(lblHeight)
                    .addComponent(lblWeight)
                    .addComponent(lblHairColor)
                    .addComponent(lblEyeColor))
                .addGap(4, 4, 4)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtNationality, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRace, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtWeight, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHairColor, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEyeColor, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        pnlInfoLayout.setVerticalGroup(
            pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNationality, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNationality))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInfoLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblRace))
                    .addComponent(txtRace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInfoLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblHeight))
                    .addComponent(txtHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInfoLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblWeight))
                    .addComponent(txtWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInfoLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblHairColor))
                    .addComponent(txtHairColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInfoLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblEyeColor))
                    .addComponent(txtEyeColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlGender.setBackground(new java.awt.Color(232, 238, 244));
        pnlGender.setBorder(javax.swing.BorderFactory.createTitledBorder("Gender"));

        rbMale.setBackground(new java.awt.Color(232, 238, 244));
        rbMale.setText("Male");

        rbFemale.setBackground(new java.awt.Color(232, 238, 244));
        rbFemale.setText("Female");

        javax.swing.GroupLayout pnlGenderLayout = new javax.swing.GroupLayout(pnlGender);
        pnlGender.setLayout(pnlGenderLayout);
        pnlGenderLayout.setHorizontalGroup(
            pnlGenderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGenderLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(rbMale)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(rbFemale)
                .addGap(23, 23, 23))
        );
        pnlGenderLayout.setVerticalGroup(
            pnlGenderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGenderLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(pnlGenderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbFemale)
                    .addComponent(rbMale))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pnlDateOfBirth.setBackground(new java.awt.Color(232, 238, 244));
        pnlDateOfBirth.setBorder(javax.swing.BorderFactory.createTitledBorder("Date of Birth"));

        lblDay.setText("Day");

        lblMonth.setText("Month");

        lblYear.setText("Year");

        javax.swing.GroupLayout pnlDateOfBirthLayout = new javax.swing.GroupLayout(pnlDateOfBirth);
        pnlDateOfBirth.setLayout(pnlDateOfBirthLayout);
        pnlDateOfBirthLayout.setHorizontalGroup(
            pnlDateOfBirthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDateOfBirthLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDateOfBirthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbDay, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlDateOfBirthLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(lblDay)))
                .addGroup(pnlDateOfBirthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDateOfBirthLayout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addComponent(lblMonth)
                        .addGap(115, 115, 115)
                        .addComponent(lblYear))
                    .addGroup(pnlDateOfBirthLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(cmbMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(cmbYear, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlDateOfBirthLayout.setVerticalGroup(
            pnlDateOfBirthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDateOfBirthLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDateOfBirthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlDateOfBirthLayout.createSequentialGroup()
                        .addGroup(pnlDateOfBirthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMonth)
                            .addComponent(lblYear))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlDateOfBirthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlDateOfBirthLayout.createSequentialGroup()
                        .addComponent(lblDay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pnlAddress.setBackground(new java.awt.Color(232, 238, 244));
        pnlAddress.setBorder(javax.swing.BorderFactory.createTitledBorder("Remarks"));

        lblRemarks.setText("Remarks");

        javax.swing.GroupLayout pnlAddressLayout = new javax.swing.GroupLayout(pnlAddress);
        pnlAddress.setLayout(pnlAddressLayout);
        pnlAddressLayout.setHorizontalGroup(
            pnlAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAddressLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblRemarks)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRemarks)
                .addContainerGap())
        );
        pnlAddressLayout.setVerticalGroup(
            pnlAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAddressLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(pnlAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRemarks)
                    .addComponent(txtRemarks, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        btnCreate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnCreate.setText("Create");
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tabNewLayout = new javax.swing.GroupLayout(tabNew);
        tabNew.setLayout(tabNewLayout);
        tabNewLayout.setHorizontalGroup(
            tabNewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabNewLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(tabNewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabNewLayout.createSequentialGroup()
                        .addGroup(tabNewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pnlInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(30, 30, 30)
                        .addComponent(pnlImages, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tabNewLayout.createSequentialGroup()
                        .addGroup(tabNewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(pnlAddress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, tabNewLayout.createSequentialGroup()
                                .addComponent(pnlDateOfBirth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(49, 49, 49)
                                .addComponent(pnlGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(btnCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 272, Short.MAX_VALUE))
        );
        tabNewLayout.setVerticalGroup(
            tabNewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabNewLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabNewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabNewLayout.createSequentialGroup()
                        .addComponent(pnlName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlImages, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(75, 75, 75)
                .addGroup(tabNewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlDateOfBirth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tabNewLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                        .addComponent(pnlGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(tabNewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabNewLayout.createSequentialGroup()
                        .addComponent(pnlAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabNewLayout.createSequentialGroup()
                        .addComponent(btnCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57))))
        );

        tabPerson.addTab("       New        ", tabNew);

        tabEdit.setBackground(new java.awt.Color(232, 238, 244));

        pnlName1.setBackground(new java.awt.Color(232, 238, 244));
        pnlName1.setBorder(javax.swing.BorderFactory.createTitledBorder("Name"));

        lblFirstName1.setText("First Name");

        lblLastName1.setText("Last Name");

        javax.swing.GroupLayout pnlName1Layout = new javax.swing.GroupLayout(pnlName1);
        pnlName1.setLayout(pnlName1Layout);
        pnlName1Layout.setHorizontalGroup(
            pnlName1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlName1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlName1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLastName1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblFirstName1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlName1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtFirstName1, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                    .addComponent(txtLastName1))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        pnlName1Layout.setVerticalGroup(
            pnlName1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlName1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlName1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFirstName1)
                    .addComponent(txtFirstName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlName1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLastName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLastName1))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pnlImages1.setBackground(new java.awt.Color(232, 238, 244));
        pnlImages1.setBorder(javax.swing.BorderFactory.createTitledBorder("Images"));

        btnBrowse1.setText("Browse");
        btnBrowse1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowse1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlImages1Layout = new javax.swing.GroupLayout(pnlImages1);
        pnlImages1.setLayout(pnlImages1Layout);
        pnlImages1Layout.setHorizontalGroup(
            pnlImages1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlImages1Layout.createSequentialGroup()
                .addContainerGap(158, Short.MAX_VALUE)
                .addComponent(btnBrowse1)
                .addGap(131, 131, 131))
            .addGroup(pnlImages1Layout.createSequentialGroup()
                .addGap(129, 129, 129)
                .addComponent(lblImage1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlImages1Layout.setVerticalGroup(
            pnlImages1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlImages1Layout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addComponent(lblImage1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 207, Short.MAX_VALUE)
                .addComponent(btnBrowse1)
                .addGap(19, 19, 19))
        );

        pnlInfo1.setBackground(new java.awt.Color(232, 238, 244));
        pnlInfo1.setBorder(javax.swing.BorderFactory.createTitledBorder("Info"));

        lblRace1.setText("Race");

        lblWeight1.setText("Weight");

        lblHairColor1.setText("Hair Color");

        lblEyeColor1.setText("Eye Color");

        lblHeight1.setText("Height");

        lblNationality1.setText("Nationality");

        javax.swing.GroupLayout pnlInfo1Layout = new javax.swing.GroupLayout(pnlInfo1);
        pnlInfo1.setLayout(pnlInfo1Layout);
        pnlInfo1Layout.setHorizontalGroup(
            pnlInfo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInfo1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(pnlInfo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNationality1)
                    .addComponent(lblRace1)
                    .addComponent(lblHeight1)
                    .addComponent(lblWeight1)
                    .addComponent(lblHairColor1)
                    .addComponent(lblEyeColor1))
                .addGroup(pnlInfo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInfo1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(pnlInfo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(txtRace1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHeight1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtWeight1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHairColor1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEyeColor1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlInfo1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNationality1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlInfo1Layout.setVerticalGroup(
            pnlInfo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInfo1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInfo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNationality1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNationality1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInfo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInfo1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblRace1))
                    .addComponent(txtRace1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInfo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInfo1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblHeight1))
                    .addComponent(txtHeight1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(pnlInfo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInfo1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblWeight1))
                    .addComponent(txtWeight1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInfo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInfo1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblHairColor1))
                    .addComponent(txtHairColor1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInfo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInfo1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblEyeColor1))
                    .addComponent(txtEyeColor1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlGender1.setBackground(new java.awt.Color(232, 238, 244));
        pnlGender1.setBorder(javax.swing.BorderFactory.createTitledBorder("Gender"));

        rbMale1.setBackground(new java.awt.Color(232, 238, 244));
        rbMale1.setText("Male");

        rbFemale1.setBackground(new java.awt.Color(232, 238, 244));
        rbFemale1.setText("Female");

        javax.swing.GroupLayout pnlGender1Layout = new javax.swing.GroupLayout(pnlGender1);
        pnlGender1.setLayout(pnlGender1Layout);
        pnlGender1Layout.setHorizontalGroup(
            pnlGender1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGender1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(rbMale1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(rbFemale1)
                .addGap(23, 23, 23))
        );
        pnlGender1Layout.setVerticalGroup(
            pnlGender1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGender1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(pnlGender1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbFemale1)
                    .addComponent(rbMale1))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pnlDateOfBirth1.setBackground(new java.awt.Color(232, 238, 244));
        pnlDateOfBirth1.setBorder(javax.swing.BorderFactory.createTitledBorder("Date of Birth"));

        lblDay1.setText("Day");

        lblMonth1.setText("Month");

        lblYear1.setText("Year");

        javax.swing.GroupLayout pnlDateOfBirth1Layout = new javax.swing.GroupLayout(pnlDateOfBirth1);
        pnlDateOfBirth1.setLayout(pnlDateOfBirth1Layout);
        pnlDateOfBirth1Layout.setHorizontalGroup(
            pnlDateOfBirth1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDateOfBirth1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDateOfBirth1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbDay1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlDateOfBirth1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(lblDay1)))
                .addGroup(pnlDateOfBirth1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDateOfBirth1Layout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addComponent(lblMonth1)
                        .addGap(115, 115, 115)
                        .addComponent(lblYear1))
                    .addGroup(pnlDateOfBirth1Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(cmbMonth1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(cmbYear1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlDateOfBirth1Layout.setVerticalGroup(
            pnlDateOfBirth1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDateOfBirth1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDateOfBirth1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlDateOfBirth1Layout.createSequentialGroup()
                        .addGroup(pnlDateOfBirth1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMonth1)
                            .addComponent(lblYear1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlDateOfBirth1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbMonth1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbYear1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlDateOfBirth1Layout.createSequentialGroup()
                        .addComponent(lblDay1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbDay1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pnlAddress1.setBackground(new java.awt.Color(232, 238, 244));
        pnlAddress1.setBorder(javax.swing.BorderFactory.createTitledBorder("Remarks"));

        lblRemarks1.setText("Remarks");

        javax.swing.GroupLayout pnlAddress1Layout = new javax.swing.GroupLayout(pnlAddress1);
        pnlAddress1.setLayout(pnlAddress1Layout);
        pnlAddress1Layout.setHorizontalGroup(
            pnlAddress1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAddress1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblRemarks1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRemarks1)
                .addContainerGap())
        );
        pnlAddress1Layout.setVerticalGroup(
            pnlAddress1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAddress1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(pnlAddress1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRemarks1)
                    .addComponent(txtRemarks1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        btnCreate1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnCreate1.setText("Edit");
        btnCreate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreate1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tabEditLayout = new javax.swing.GroupLayout(tabEdit);
        tabEdit.setLayout(tabEditLayout);
        tabEditLayout.setHorizontalGroup(
            tabEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabEditLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(tabEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabEditLayout.createSequentialGroup()
                        .addGroup(tabEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pnlName1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlInfo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(33, 33, 33)
                        .addComponent(pnlImages1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tabEditLayout.createSequentialGroup()
                        .addGroup(tabEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(pnlAddress1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, tabEditLayout.createSequentialGroup()
                                .addComponent(pnlDateOfBirth1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(49, 49, 49)
                                .addComponent(pnlGender1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(btnCreate1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 272, Short.MAX_VALUE))
        );
        tabEditLayout.setVerticalGroup(
            tabEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabEditLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabEditLayout.createSequentialGroup()
                        .addComponent(pnlName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pnlInfo1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlImages1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlDateOfBirth1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tabEditLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                        .addComponent(pnlGender1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabEditLayout.createSequentialGroup()
                        .addComponent(pnlAddress1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabEditLayout.createSequentialGroup()
                        .addComponent(btnCreate1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57))))
        );

        tabPerson.addTab("      Edit        ", tabEdit);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabPerson, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabPerson, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tabPerson.getAccessibleContext().setAccessibleName("tabContacts");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased

    }//GEN-LAST:event_txtSearchKeyReleased

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed

    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnCreate1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreate1ActionPerformed
        boolean isMale = true;
        if (rbFemale1.isSelected()) {
            isMale = false;
        }

        String[] values = new String[]{
            txtFirstName1.getText()
                , txtLastName1.getText()
                , (isMale == true ? "false" : "true")
                , txtRace1.getText()
                , txtHairColor1.getText()
                , txtEyeColor1.getText()
                , txtHeight1.getText()
                , txtWeight1.getText()
                , cmbDay1.getSelectedItem() + "-" + cmbMonth1.getSelectedItem() + "-" + cmbYear1.getSelectedItem()
                , txtNationality1.getText()
                , txtRemarks1.getText()
        };

        try {
            if (person.updatePerson(values, selectedID)) {
                JOptionPane.showMessageDialog(null, "Person Updated Successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Error Updating Person!", "Edit Person", ERROR);
            }

        } catch (Exception ex) {

        }
    }//GEN-LAST:event_btnCreate1ActionPerformed

    private void btnBrowse1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowse1ActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new java.io.File(".")); // start at application current directory
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {

        }
        String name = fc.getSelectedFile().getName();
        System.out.println(fc.getSelectedFile());
        System.out.println(name);
    }//GEN-LAST:event_btnBrowse1ActionPerformed

    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
        lblImage.setIcon(null);
        lblImage.setSize(200, 200);
        lblImage.setBounds(0,2,200,200);
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Choose a image");
        fc.setCurrentDirectory(new java.io.File("."));
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG, JPEG, GIF, PNG Images", "jpg", "gif", "JPEG", "png");
        fc.setFileFilter(filter);

        int returnVal = fc.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {

        }
        String name=fc.getSelectedFile().getName();
        File Path = new File(fc.getSelectedFile().getAbsolutePath());
        System.out.println(fc.getSelectedFile().getAbsolutePath());
        System.out.println(name);
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("\nRunning FaceDetector");
        CascadeClassifier faceDetector = new CascadeClassifier("C:\\Users\\James\\Desktop\\EverTeam\\Resources\\haarcascade_frontalface_alt.xml");
        Mat image = Imgcodecs.imread(fc.getSelectedFile().getAbsolutePath());
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);
        if(faceDetections.empty())
        {
           JOptionPane.showMessageDialog(null, "No face detected", "", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            int rx=0;
            int ry=0;
            int rw=0;
            int rh=0;
            System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
            for (Rect rect : faceDetections.toArray())
            {
               Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                       new Scalar(0, 255, 0));
               rx=rect.x;
               ry=rect.y;
               rw=rect.width;
               rh=rect.height;
            }
            String filename = "output.png";
            System.out.println(String.format("Writing %s", filename));
            //Apply modification to output image
            Imgcodecs.imwrite(filename, image);

            File file = new File(filename);
            try {
                pnlImage.setImage(file);
            } catch (MalformedURLException ex) {
                Logger.getLogger(FrmEnrollment.class.getName()).log(Level.SEVERE, null, ex);
            }
            pnlImage.setBounds(0, 0, 200, 200);
            pnlImageContainer.add(pnlImage);
            pnlImage.revalidate();
            pnlImage.repaint();

            //crop image and convert to binary
            File path1=new File("C:\\Users\\elie_\\Documents\\NetBeansProjects\\EverTeam\\cropped.jpg");
            BufferedImage image3 = null;
            try {
                image3 = ImageIO.read(Path);
            } catch (IOException ex) {
                Logger.getLogger(FrmEnrollment.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                BufferedImage croppedImage = image3.getSubimage(rx, ry, rw, rh);
                int width = croppedImage.getWidth();
                int height = croppedImage.getHeight();
                for(int i=0; i<height; i++)
                {
                    for(int j=0; j<width; j++)
                    {
                        Color c = new Color(croppedImage.getRGB(j, i));
                        int red = (int)(c.getRed() * 0.21);
                        int green = (int)(c.getGreen() * 0.72);
                        int blue = (int)(c.getBlue() *0.07);
                        int sum = red + green + blue;
                        Color newColor = new Color(sum,sum,sum);
                        croppedImage.setRGB(j,i,newColor.getRGB());
                    }
                }

                ImageIO.write(croppedImage, "JPG", path1);
                System.out.println("Cropped Picture Created");
            } catch (IOException ex) {
                Logger.getLogger(FrmEnrollment.class.getName()).log(Level.SEVERE, null, ex);
            }

            //convert to binary
            /*
            ImageToBinary imgtob = new ImageToBinary();
            try {
               
                imgtob.ConvertImageToBinary("cropped.jpg");
            } catch (IOException ex) {
               Logger.getLogger(FrmEnrollment.class.getName()).log(Level.SEVERE, null, ex);
            }*/
            
            //Insert Binary to Database
            ImageToBinary imgtob = new ImageToBinary();
            String value = "";
            try
            {
                value = imgtob.ConvertImageToBinary("output.png");
            } catch (IOException ex) {
               Logger.getLogger(FrmEnrollment.class.getName()).log(Level.SEVERE, null, ex);
            }

        try {
            if (originalImage.insertOriginalImage(value)) {
                JOptionPane.showMessageDialog(null, "Person Inserted Successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Error Inserting New Person!", "New Person", ERROR);
            }

        } catch (Exception ex) {

        }
        }
    }//GEN-LAST:event_btnBrowseActionPerformed

    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        boolean isMale = true;
        if (rbFemale.isSelected()) {
            isMale = false;
        }

        String[] values = new String[]{
            (txtFirstName.getText().equals("") ? null : txtFirstName.getText())
                , (txtLastName.getText().equals("") ? null : txtLastName.getText())
                , (isMale == true ? "false" : "true")
                , (txtRace.getText().equals("") ? null : txtRace.getText())
                , (txtHairColor.getText().equals("") ? null : txtHairColor.getText())
                , (txtEyeColor.getText().equals("") ? null : txtEyeColor.getText())
                , (txtHeight.getText().equals("") ? "-1" : txtHeight.getText())
                , (txtWeight.getText().equals("") ? "-1" : txtWeight.getText())
                , cmbDay.getSelectedItem() + "-" + cmbMonth.getSelectedItem() + "-" + cmbYear.getSelectedItem()
                , (txtNationality.getText().equals("") ? null : txtNationality.getText())
                , (txtRemarks.getText().equals("") ? null : txtRemarks.getText())
        };

        try {
            if (person.insertPerson(values)) {
                JOptionPane.showMessageDialog(null, "Person Inserted Successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Error Inserting New Person!", "New Person", ERROR);
            }

        } catch (Exception ex) {

        }
    }//GEN-LAST:event_btnCreateActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgGender;
    private javax.swing.ButtonGroup bgGender1;
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnBrowse1;
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnCreate1;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox cmbAttribute;
    private javax.swing.JComboBox cmbDay;
    private javax.swing.JComboBox cmbDay1;
    private javax.swing.JComboBox cmbMonth;
    private javax.swing.JComboBox cmbMonth1;
    private javax.swing.JComboBox cmbYear;
    private javax.swing.JComboBox cmbYear1;
    private javax.swing.JLabel lblDay;
    private javax.swing.JLabel lblDay1;
    private javax.swing.JLabel lblEyeColor;
    private javax.swing.JLabel lblEyeColor1;
    private javax.swing.JLabel lblFirstName;
    private javax.swing.JLabel lblFirstName1;
    private javax.swing.JLabel lblHairColor;
    private javax.swing.JLabel lblHairColor1;
    private javax.swing.JLabel lblHeight;
    private javax.swing.JLabel lblHeight1;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblImage1;
    private javax.swing.JLabel lblLastName;
    private javax.swing.JLabel lblLastName1;
    private javax.swing.JLabel lblMonth;
    private javax.swing.JLabel lblMonth1;
    private javax.swing.JLabel lblNationality;
    private javax.swing.JLabel lblNationality1;
    private javax.swing.JLabel lblRace;
    private javax.swing.JLabel lblRace1;
    private javax.swing.JLabel lblRemarks;
    private javax.swing.JLabel lblRemarks1;
    private javax.swing.JLabel lblWeight;
    private javax.swing.JLabel lblWeight1;
    private javax.swing.JLabel lblYear;
    private javax.swing.JLabel lblYear1;
    private javax.swing.JPanel pnlAddress;
    private javax.swing.JPanel pnlAddress1;
    private javax.swing.JPanel pnlDateOfBirth;
    private javax.swing.JPanel pnlDateOfBirth1;
    private javax.swing.JPanel pnlGender;
    private javax.swing.JPanel pnlGender1;
    private javax.swing.JPanel pnlImageContainer;
    private javax.swing.JPanel pnlImages;
    private javax.swing.JPanel pnlImages1;
    private javax.swing.JPanel pnlInfo;
    private javax.swing.JPanel pnlInfo1;
    private javax.swing.JPanel pnlName;
    private javax.swing.JPanel pnlName1;
    private javax.swing.JPanel pnlSearch;
    private javax.swing.JRadioButton rbFemale;
    private javax.swing.JRadioButton rbFemale1;
    private javax.swing.JRadioButton rbMale;
    private javax.swing.JRadioButton rbMale1;
    private javax.swing.JPanel tabEdit;
    private javax.swing.JPanel tabNew;
    private javax.swing.JTabbedPane tabPerson;
    private javax.swing.JPanel tabView;
    private javax.swing.JTextField txtEyeColor;
    private javax.swing.JTextField txtEyeColor1;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtFirstName1;
    private javax.swing.JTextField txtHairColor;
    private javax.swing.JTextField txtHairColor1;
    private javax.swing.JTextField txtHeight;
    private javax.swing.JTextField txtHeight1;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtLastName1;
    private javax.swing.JTextField txtNationality;
    private javax.swing.JTextField txtNationality1;
    private javax.swing.JTextField txtRace;
    private javax.swing.JTextField txtRace1;
    private javax.swing.JTextField txtRemarks;
    private javax.swing.JTextField txtRemarks1;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtWeight;
    private javax.swing.JTextField txtWeight1;
    // End of variables declaration//GEN-END:variables

    /**
     * Set the width of each column in table tblContacts
     */
    public void arrangeTable() {
        tblPerson.getColumnModel().getColumn(0).setPreferredWidth(40);  // ID
        tblPerson.getColumnModel().getColumn(1).setPreferredWidth(100); // First Name
        tblPerson.getColumnModel().getColumn(2).setPreferredWidth(100); // Last Name
        tblPerson.getColumnModel().getColumn(3).setPreferredWidth(30);  // Gender
        tblPerson.getColumnModel().getColumn(4).setPreferredWidth(60);  // Race
        tblPerson.getColumnModel().getColumn(5).setPreferredWidth(60);  // Hair Color
        tblPerson.getColumnModel().getColumn(6).setPreferredWidth(60);  // Eye Color
        tblPerson.getColumnModel().getColumn(7).setPreferredWidth(30);  // Height
        tblPerson.getColumnModel().getColumn(8).setPreferredWidth(30);  // Weight
        tblPerson.getColumnModel().getColumn(9).setPreferredWidth(50);  // Date of Birth
        tblPerson.getColumnModel().getColumn(10).setPreferredWidth(70); // Nationality
        tblPerson.getColumnModel().getColumn(11).setPreferredWidth(100); // Remarks
    }

    /**
     * Reset the Edit Tab
     */
    public void resetTabEdit() {
        tabPerson.setEnabledAt(2, false);
        txtFirstName1.setText("");
        txtLastName1.setText("");
        txtNationality1.setText("");
        txtRace1.setText("");
        txtHeight1.setText("");
        txtWeight1.setText("");
        txtHairColor1.setText("");
        txtEyeColor1.setText("");
        rbMale1.setSelected(true);
        cmbDay1.setSelectedIndex(0);
        cmbMonth1.setSelectedIndex(0);
        cmbYear1.setSelectedIndex(0);
        txtRemarks1.setText("");
    }

    /**
     * return an array of 31 days
     */
    public String[] getDay() {
        String[] arr = new String[31];
        for (int i = 0; i < 31; i++) {
            arr[i] = String.valueOf(i + 1);
        }
        return arr;
    }

    /**
     * return an array of 12 months
     */
    public String[] getMonth() {
        String[] arr = new String[12];
        for (int i = 0; i < 12; i++) {
            arr[i] = String.valueOf(i + 1);
        }
        return arr;
    }

    /**
     * return an array of 100 years till current year
     */
    public String[] getYear() {
        String[] arr = new String[100];
        Calendar rightNow = Calendar.getInstance();
        int year = rightNow.get(Calendar.YEAR);
        for (int i = 0; i < 100; i++) {
            arr[i] = String.valueOf(year--);
        }
        return arr;
    }
}
