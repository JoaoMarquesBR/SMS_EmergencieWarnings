import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainPage extends JFrame {
    private static ArrayList<JCheckBox> checkBoxList;
    public static TextField tfMessageInput;
    private static JButton btnSendMessage;


    private static HashMap<String , String> notesMap = new HashMap<>();
//    private static HashMap<String , String> selectedEmp = new HashMap<>();

    private static DefaultListModel demoList,demoListSelected;
    private static JList list,list_selected;



    public MainPage(){
        DatabaseCommands.queryResults();
        initializeObjs();

        JPanel mainFrame = new JPanel();
        mainFrame.setBorder(BorderFactory.createEmptyBorder(10,25,15,25));
        mainFrame.setLayout(new BorderLayout(10,10));

        this.setTitle("SMS App");
        this.setLayout(new BorderLayout() );
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//prevents memory leak
//        this.setLocationRelativeTo(null);//automatically centres our frame in the screen
        //Impleent some ables
        Panel checkBoxLayouts = new Panel();

        checkBoxLayouts.setLayout(new GridLayout(Main.myDepts.size()+1,1,0,0));
        checkBoxLayouts.setPreferredSize(new Dimension(100,100));
        JLabel titleDept = new JLabel("Select department");
        titleDept.setFont(new Font("Arial",Font.BOLD,18));
        checkBoxLayouts.add(titleDept);

        checkBoxLayouts.setPreferredSize(new Dimension(50,300));

        mainFrame.setSize(600,400);
        //loop responsible for determing the checkboxes of Groups and Jobs

        for(int i = 0; i<Main.myDepts.size(); i++){
            checkBoxLayouts.add(checkBoxList.get(i));
        }


        JMenuBar myMenuBar = new JMenuBar();

        JMenu jMenu = new JMenu("Maintenance");


        JMenuItem addEmpMenu = new JMenuItem("Add employee");
        jMenu.add(addEmpMenu);

        myMenuBar.add(jMenu);

        addEmpMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //once addEmp MenuItem is clicked, we need open a GUI for adding the employee information.
                try {
                    new AddEmployeeGui();
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JMenuItem viewEmpMenu = new JMenuItem("Edit employee");
        jMenu.add(viewEmpMenu);

        viewEmpMenu.setMargin(new Insets(2, 2, 2, 2));

//        viewEmpMenu.setLayout(new FlowLayout());
        viewEmpMenu.setSize(new Dimension(20,20));

        viewEmpMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //once addEmp MenuItem is clicked, we need open a GUI for adding the employee information.
                new ViewEmployeesGui();
            }
        });

        demoList = new DefaultListModel();
        demoListSelected = new DefaultListModel();

        //first List that they can choose.
        list = new JList(demoList);
        list.setFont(new Font("Arial",Font.BOLD,18));

        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(e.getValueIsAdjusting()){
                    int index = list.getSelectedIndex();
                    try {
                        Thread.sleep(80);
                    } catch (InterruptedException ex) {

                        ex.printStackTrace();
                    }
                    if(index>=0){
//                clearAfterEdited();
                        String s = (String) list.getSelectedValue();
                        String text = (notesMap.get(index));
//                updateEmpInfo(index);
//                emp_update = Main.employees.get(index);


                        demoList.remove(index);
                        demoListSelected.addElement(s);
                    }
                }

            }
        });
        list.setFixedCellWidth(200);
        list.setFixedCellHeight(30);

        populateEmpList();
        JScrollPane scrollableList = new JScrollPane(list);
        scrollableList.setPreferredSize(new Dimension(200,250));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Second list where accoutns are being passed to.
        list_selected = new JList(demoListSelected);
        list_selected.setFont(new Font("Arial",Font.BOLD,18));

        list_selected.setFixedCellWidth(200);
        list_selected.setFixedCellHeight(30);
//        populateEmpList();
        JScrollPane scrollableList_2selected = new JScrollPane(list);
        scrollableList_2selected.setPreferredSize(new Dimension(200,250));
        list_selected.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list_selected.setSelectedIndex(0);
        list_selected.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(e.getValueIsAdjusting()){
                    int index = list_selected.getSelectedIndex();
                    System.out.println("clicked");
                    if(index>=0){
                        String s = (String) list_selected.getSelectedValue();
                        demoListSelected.remove(index);
                        demoList.addElement(s);
                    }
                }
            }
        });


        Panel empInfoPanel = new Panel(new BorderLayout());


        Panel frame_list = new Panel();
        frame_list.setLayout(new GridLayout(2,2,0,2));
        // Panel frame_list_selected = new Panel();

        JLabel empLabel = new JLabel("Select");
        empLabel.setFont(new Font("Verdana",Font.PLAIN,16));


        JLabel forwardingLabel = new JLabel("Selected (click to remove)");
        forwardingLabel.setFont(new Font("Verdana",Font.PLAIN,16));

//        empLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//        forwardingLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        list.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        list_selected.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        frame_list.add(empLabel);
        frame_list.add(forwardingLabel);
        frame_list.add(list);
        frame_list.add(list_selected);

        frame_list.setPreferredSize(new Dimension(450,250));
//

        empInfoPanel.add(frame_list,BorderLayout.WEST);

        Panel textPanel = new Panel();
        textPanel.setLayout(new BoxLayout(textPanel,BoxLayout.Y_AXIS));
        Label messageLbl= new Label("Message");
        messageLbl.setFont(new Font("Verdana",Font.PLAIN,25));

        ///textPanel.setLayout(new BorderLayout());
        textPanel.add(messageLbl);
        textPanel.setPreferredSize(new Dimension(450,35));
        tfMessageInput.setPreferredSize(new Dimension(50,40));
        textPanel.add(tfMessageInput);
        textPanel.add(new Label(""),BorderLayout.SOUTH);

        //setMaximumSize(getMaximumSize());

        textPanel.add(btnSendMessage);
//        checkBoxLayouts.add(btnSendMessage);

        mainFrame.add(empInfoPanel,BorderLayout.WEST);
        mainFrame.add(checkBoxLayouts,BorderLayout.CENTER);
        mainFrame.add(textPanel,BorderLayout.EAST);
//        mainFrame.add(btnSendMessage,BorderLayout.EAST);


        this.setJMenuBar(myMenuBar);
        this.add(mainFrame);

        this.setSize(1280,400);
        this.setVisible(true);
    }

    void initializeObjs() {
        checkBoxList = new ArrayList<>();
        tfMessageInput = new TextField();
//        tfMessageInput.setPreferredSize(new Dimension(40,40));
        JCheckBox current;
        for (int i = 0; i < Main.myDepts.size(); i++) {
            current = new JCheckBox(Main.myDepts.get(i).name);
            current.setFont(new Font("Arial",Font.BOLD,18));
            checkBoxList.add(current);
        }

        btnSendMessage = new JButton("Send");
        btnSendMessage.setPreferredSize(new Dimension(150,75));

        btnSendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i=0;i<checkBoxList.size();i++){
                    if(checkBoxList.get(i).isSelected()){
                        DatabaseCommands.callDept(checkBoxList.get(i).getText());
                    }
                }

                System.out.println("size "+ demoListSelected.size());

                for(int i=0;i<demoListSelected.size();i++){
                    System.out.println("index "+ i  +" val "+ demoListSelected.get(i).toString());
                    DatabaseCommands.callNameEmp(demoListSelected.get(i).toString());
                }

                System.out.println("sending?");
                ExceptionsChecks.messageWasSent();
                tfMessageInput.setText("");

            }
        });

    }

    public static void reloadList(){
        demoList.clear();
        list.removeAll();
        demoList.removeAllElements();
        list.removeAll();

        demoListSelected.clear();
        list_selected.removeAll();
        demoListSelected.removeAllElements();
        list_selected.removeAll();

        Main.reset();
        populateEmpList();
    }

    public static void populateEmpList(){
        JLabel label = new JLabel("");
        for(int i=0;i<Main.employees.size();i++){
            Employees emp = Main.employees.get(i);
            notesMap.put(String.valueOf(emp.empID), emp.empName);
//            label.setText(emp.empName);
            demoList.addElement(emp.empName);
        }
    }

}
