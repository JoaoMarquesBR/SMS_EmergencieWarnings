import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;

class AddEmployeeGui extends JFrame {


    private static TextField nameTF,employeeIdTF;
    private static JPhoneNumberFormattedTextField phoneNumberTF;
    private static Label nameLbl, phoneNumberLbl;
    private static Button addEmp;

    private static ArrayList<JCheckBox> checkBoxList;
    public static  ArrayList<ArrayList<JCheckBox>>listOfBlockLists = new ArrayList<>();

    public static void main(String[] args) {
        try {
            new AddEmployeeGui();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public AddEmployeeGui()throws ParseException {
        super();
        this.setSize(700,450);//width, height...change to desired dimensions.
        this.setLocationRelativeTo(null);//automatically centres our frame in the screen
        this.setLayout(new BorderLayout());
        this.setTitle("Add Employee");
        initializeVariables();

        Container myContainer = this.getContentPane();
        myContainer.setLayout(new BorderLayout(5,5));

        JPanel frame = new JPanel();
        frame.setLayout(new GridLayout(4,2,3,3));

        frame.add(new Label("Employee ID: "));
        frame.add(employeeIdTF);
        frame.add(nameLbl);
        frame.add(nameTF);
        frame.add(phoneNumberLbl);
        frame.add(phoneNumberTF );

        JPanel checkBoxesLayout = new JPanel();
        checkBoxesLayout.setLayout(new GridLayout(1,Main.myDepts.size(),0,0));

        Panel panelLine = new Panel();
        for(int i = 0; i<Main.myDepts.size(); i++){
            ArrayList<JCheckBox>deptList= new ArrayList<>();
            panelLine.setLayout(new GridLayout( 20,1 ));
            JCheckBox dept = new JCheckBox(Main.myDepts.get(i).name);
            checkBoxList.add(dept);
            panelLine.add(dept);
        }
        checkBoxesLayout.add(panelLine);

        frame.add(addEmp);

        addEmp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTF.getText();
                String phoneNumber = phoneNumberTF.getText();
                String idString = employeeIdTF.getText();
                String department ="";
                int departCounter=0;
                ArrayList<String>sectors = new ArrayList<>();
                int sectorCounter=0;
                sectors.clear();
                for(int i=0;i<checkBoxList.size();i++){
                    if(checkBoxList.get(i).isSelected()){
                        sectorCounter++;
                        sectors.add(checkBoxList.get(i).getText());
                        department+=checkBoxList.get(i).getText();
                    }
                }

                int idNumber=0;
                if(employeeIdTF.getText().length()==0 || !onlyDigits(idString,idString.length())){
                    ExceptionsChecks.empIDCheck();
                    return;
                }else{
                    idNumber = Integer.parseInt(idString);
                }



                if(!checkPhoneNumber(phoneNumber)) {
                    ExceptionsChecks.phoneCheck();
                    return;
                }

                if(nameTF.getText().length()<3){
                    ExceptionsChecks.nameCheck();
                    return;
                }

                //first check if number digits has been full filled.

                if(sectors.size()!=1){
                    ExceptionsChecks.sectionCheck();
                    return;
                }

                //confirmation pops up for check.
                //provides data needed of the employee.

                new AddEmpConfirmation(idNumber,name,phoneNumber,false,department);

            }
        });
        myContainer.add(frame,BorderLayout.CENTER);
        myContainer.add(checkBoxesLayout,BorderLayout.WEST);
        this.setVisible(true);
    }
    private boolean  checkPhoneNumber(String inputPhoneNumber){
        String number  ="";
        for(int i=0;i<inputPhoneNumber.length();i++){
            char c = inputPhoneNumber.charAt(i);
            if( Character.isDigit(c) ){
                number+=c;
            }
        }
        if(number.length()==11){
            return true;
        }
        return false;
    }

    public static void clearFields(){
        nameTF.setText("");
        phoneNumberTF.setText("");
        employeeIdTF.setText("");
        for(JCheckBox box : checkBoxList){
            box.setSelected(false);
        }
    }


    private static final class JPhoneNumberFormattedTextField extends JFormattedTextField{
        private static final long serialVersionUID = 8997075146338662662L;
        public JPhoneNumberFormattedTextField() throws ParseException {
            super(new MaskFormatter("1(###) ###-####"));
            setColumns(9);
        }
    }

    public static boolean onlyDigits(String str, int n)
    {

        for (int i = 0; i < n; i++) {
            if (str.charAt(i) >= '0'
                    && str.charAt(i) <= '9') {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }
    private static void initializeVariables() throws ParseException {
        nameTF = new TextField("");
        employeeIdTF = new TextField();

        phoneNumberTF = new JPhoneNumberFormattedTextField();
        nameLbl = new Label("Name: ");
        phoneNumberLbl = new Label("Phone Number");

        addEmp = new Button("Add");
        checkBoxList = new ArrayList<>();
    }
}



        class AddEmpConfirmation extends JFrame {

    static JFrame frame;

    Button confirm,cancel;

    boolean isUpdating;
    int emp_update_id;

    public AddEmpConfirmation(Employees emp_update){
        isUpdating=true;
        emp_update_id = emp_update.empID;
        new AddEmpConfirmation(emp_update.empID,emp_update.empName,emp_update.empPhoneNumber,true,emp_update.dept);


    }

    public AddEmpConfirmation(int emp_id, String empName, String empPhoneNumber,boolean isUpdate, String dept){
        super("Confirmation");
        this.setSize(330,200);//width, height...change to desired dimensions.
        this.setLocationRelativeTo(null);//automatically centres our frame in the screen
        this.setLayout(new GridLayout(3,1));


        Container mainContainer = this.getContentPane();
        mainContainer.setLayout(new BorderLayout(5,5));

        JPanel pane1 = new JPanel();
        Label empIdLbl, empNameLbl,empPhoneNumberLbl,empSection,departmentLbl;

        empIdLbl = new Label("ID: "+ emp_id);
        empNameLbl = new Label("Name: "+empName);
        empPhoneNumberLbl = new Label("PhoneNumber: "+empPhoneNumber);
        departmentLbl = new Label("Department: "+dept);

        empSection =  new Label(dept);
        pane1.add(empIdLbl);
        pane1.add(empNameLbl);
        pane1.add(empPhoneNumberLbl);
        pane1.add(departmentLbl);

        JPanel pane2 = new JPanel();
        confirm = new Button("Confirm");
        cancel = new Button("Cancel");
        pane2.add(confirm);
        pane2.add(cancel);

        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isUpdate) {
                    String jobSections = empSection.getText()+" $";
                    DatabaseCommands.removeEMP(emp_id);
                    DatabaseCommands.addEmployee(emp_id,empName,empPhoneNumber,dept);
                }else{
                    if(DatabaseCommands.checkCount("empPhoneNumber",empPhoneNumber)){
                        JOptionPane.showMessageDialog(new Frame(),"Number already been used.");
                        AddEmpConfirmation.this.dispose();
                        return;
                    }
                    if(DatabaseCommands.checkCount("id", String.valueOf(emp_id))){
                        JOptionPane.showMessageDialog(new Frame(),"Emp ID already been used.");
                        AddEmpConfirmation.this.dispose();
                        return;
                    }
                    DatabaseCommands.addEmployee(emp_id,empName,empPhoneNumber,dept);
                    AddEmployeeGui.clearFields();
                    JOptionPane.showMessageDialog(new Frame(),"Employee has been added.");
                }
                //adding employee.
                //isUpdating=false;
                MainPage.reloadList();
                AddEmpConfirmation.this.dispose();
            }
        });
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddEmpConfirmation.this.dispose();
            }
        });
        mainContainer.add(pane2,BorderLayout.SOUTH);
        mainContainer.add(pane1,BorderLayout.CENTER);
        this.setVisible(true);
    }

}


