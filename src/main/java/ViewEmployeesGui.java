import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewEmployeesGui extends JFrame {
    private static HashMap<String , String> notesMap = new HashMap<>();
    static DefaultListModel demoList;
    static JList list;
    private static ArrayList<JCheckBox> checkBoxList;


    private static Button editBtn,updateBtn,deleteBtn, reloadBtn;

    private static TextField empName = new TextField();
    private static JPhoneNumberFormattedTextField phoneNumberTF;

    static {
        try {
            phoneNumberTF = new JPhoneNumberFormattedTextField();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static Employees emp_update;

    public ViewEmployeesGui(){
        super();
        this.setTitle("Employee List");
        this.setLayout(new BorderLayout(5,5));
        this.setSize(900,500);
        this.setBackground(Color.DARK_GRAY);
        this.setLocationRelativeTo(null);//automatically centres our frame in the screen

        Panel viewPanel = new Panel();
        viewPanel.setSize(500,100);

        demoList = new DefaultListModel();
        list = new JList(demoList);

        populateEmpList();
        JScrollPane scrollableList = new JScrollPane(list);
        scrollableList.setPreferredSize(new Dimension(200,400));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener((ListSelectionListener) e -> {
            int index = list.getSelectedIndex();

            if(index>=0){

                clearAfterEdited();
                String s = (String) list.getSelectedValue();
                String text = (notesMap.get(index));
                updateEmpInfo(index);
                emp_update = Main.employees.get(index);

            }

        });
        list.setVisibleRowCount(5);


        viewPanel.add(scrollableList);
//        viewPanel.add(reloadBtn);

        Panel empInfoPanel = new Panel();
        empInfoPanel.setLayout(new BoxLayout(empInfoPanel,BoxLayout.PAGE_AXIS));

        initializeCheckBoxList();
        JPanel checkBoxesLayout = new JPanel();
        checkBoxesLayout.setLayout(new GridLayout(1,Main.employees.size(),0,0));


        for(int i = 0; i<Main.myDepts.size(); i++){
            JCheckBox deptBox = new JCheckBox(Main.myDepts.get(i).name);
            checkBoxList.add(deptBox);
            deptBox.setEnabled(false);
        }

        for(int i=0;i<checkBoxList.size();i++){
            empInfoPanel.add(checkBoxList.get(i));
        }
        empInfoPanel.add(phoneNumberTF);
        empInfoPanel.add(empName);
        empName.setEditable(false);
        phoneNumberTF.setEditable(false);


        editBtn = new Button("Edit");
        deleteBtn = new Button("Delete");
        updateBtn = new Button("Update")  ;
        reloadBtn = new Button("Reload");

        Panel buttonPanel = new Panel();
        buttonPanel.add(editBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(reloadBtn);


        updateBtn.setEnabled(false);

        //setting actions listeners for buttons.
        updateBtn.addActionListener(this::actionPerformed);
        deleteBtn.addActionListener(this::actionPerformed);
        editBtn.addActionListener(this::actionPerformed);
        reloadBtn.addActionListener(this::actionPerformed);

        this.add(viewPanel,BorderLayout.WEST);
        this.add(checkBoxesLayout,BorderLayout.EAST);
        this.add(empInfoPanel,BorderLayout.CENTER);
        this.add(buttonPanel,BorderLayout.SOUTH);
        this.setVisible(true);

    }

    public static void populateEmpList(){
        for(int i=0;i<Main.employees.size();i++){
                Employees emp = Main.employees.get(i);
                notesMap.put(String.valueOf(emp.empID), emp.empName);
                demoList.addElement(emp.empName);
            }
    }

    public void actionPerformed(ActionEvent ev) {

        switch (ev.getActionCommand()){
            case "Reload":
                resetting();
                break;
            case "Edit":
               edit();
                break;

            case "Update":
                ArrayList<String>jobsSelected = new ArrayList<>();
                String deptName ="";
                int count=0;
                boolean ok=true;
                for(JCheckBox box : checkBoxList){
                    if(box.isSelected()){
                        deptName = box.getText();
                        count++;
                    }
                }
                if(count!=1){
                    ExceptionsChecks.sectionCheck2();
                    ok=false;
                    break;
                }

                if (ok) {

                    emp_update = new Employees(empName.getText(),phoneNumberTF.getText(),deptName,emp_update.empID);

                    System.out.println("Updating "+ emp_update.empID+" to deptID "+ deptName);
                    new AddEmpConfirmation(emp_update);
                    resetting();
                }
                break;

            case "Delete":
                int indexSelected = list.getSelectedIndex();
                demoList.remove(indexSelected);
                System.out.println("deleting "+ emp_update.empID);
                DatabaseCommands.removeEMP(emp_update.empID);
                resetting();
                list.setSelectedIndex(0);
                break;
        }


    }

    public static void resetting(){
        demoList.clear();
        list.removeAll();
        demoList.removeAllElements();
        list.removeAll();

//
        Main.reset();
        clearAfterEdited();
        populateEmpList();

    }

    public static void edit(){
        empName.setEditable(true);
        phoneNumberTF.setEditable(true);
        updateBtn.setEnabled(true);
        for(int i=0;i<checkBoxList.size();i++){
            checkBoxList.get(i).setEnabled(true);
        }
    }

        public static void clearAfterEdited(){
            empName.setEditable(false);
             phoneNumberTF.setEditable(false);
            updateBtn.setEnabled(false);
            for(int i=0;i<checkBoxList.size();i++){
                checkBoxList.get(i).setEnabled(false);
            }
        }

        private static final class JPhoneNumberFormattedTextField extends JFormattedTextField{
        private static final long serialVersionUID = 8997075146338662662L;
        public JPhoneNumberFormattedTextField() throws ParseException {
            super(new MaskFormatter("1(###) ###-####"));
                setColumns(17);
        }
    }



    public void updateEmpInfo(int index){
        Employees selectedEmp = Main.employees.get(index);
        phoneNumberTF.setText(selectedEmp.empPhoneNumber);
        empName.setText(selectedEmp.empName);
        for(int i=0;i<checkBoxList.size();i++){
            checkBoxList.get(i).setSelected(false);//important to clear past results
            if(selectedEmp.dept.contains(checkBoxList.get(i).getText())){
                checkBoxList.get(i).setSelected(true);
            }
        }
    }

    public void initializeCheckBoxList(){
        checkBoxList = new ArrayList<>();
    }

}
