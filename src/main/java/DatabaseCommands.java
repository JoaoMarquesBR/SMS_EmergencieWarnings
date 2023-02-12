import java.sql.*;

public class DatabaseCommands {
    private static String url = "jdbc:sqlite:C:\\MyDatabases\\thefactoryEMP.db";

    private static Connection conn;
    private static int refreshCount=0;

    public static void setUp(){
        queryDept();
    }

    static String myMsg = "testing app ";

    //a
    public static void queryResults(){
        try{
            //The Factory|Nash|+1 519 615-5242|520
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            String deptName="";
            String empName="";
            String empPhoneNumber="";
            String empID="";
            int deptID;
            int timesMesseged=0;

            try(Statement statement = conn.createStatement();

                ResultSet results = statement.executeQuery(" select deptName,empName,empPhoneNumber,empID,deptID from emp inner join deptDetails\n" +
                        "on emp.id = deptdetails.empID\n" +
                        "inner join dept \n" +
                        "on deptdetails.deptID = dept.id order by empName")) {
                while(results.next()) {
                    deptName = results.getString(1);
                    empName = results.getString(2);
                    empPhoneNumber = results.getString(3);
                    empID = results.getString(4);
                    deptID = results.getInt(4);

                    Main.employees.add(new Employees(empName,empPhoneNumber,deptName,Integer.parseInt(empID)));
                    boolean exist=false;
                    for(int i = 0; i<Main.myDepts.size(); i++){
                        if(Main.myDepts.get(i).name.equals(deptName)){
                            exist=true;
                        }
                    }

                    if(exist==false){
                        Main.myDepts.add(new Dept(deptName,deptID));
                      }

                }
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //b
    public static void queryDept(){
        try{
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            int deptID=-1;
            String deptName="";


            try(Statement statement = conn.createStatement();
                ResultSet results = statement.executeQuery(" select * from dept;")) {
                while(results.next()) {
                    deptID = results.getInt(1);
                    deptName = results.getString(2);
                    boolean exist=false;
                    for(int i = 0; i<Main.myDepts.size(); i++){
                        if(Main.myDepts.get(i).equals(deptName)){
                            exist=true;
                        }
                    }
                    if(exist==false){
                        Main.myDepts.add(new Dept(deptName,deptID));
                    }
                }
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void callNameEmp(String empName){

        try{
            //The Factory|Nash|+1 519 615-5242|520
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            String empPhoneNumber="";

            String sqlCommand = "select empPhoneNumber from emp inner join deptDetails\n" +
                    "on emp.id = deptDetails.empID where empName = '"+ empName+"';";
            System.out.println("sql command = " + sqlCommand);
            try(Statement statement = conn.createStatement();
                ResultSet results = statement.executeQuery(sqlCommand)) {
                while(results.next()) {
                    empPhoneNumber = results.getString(1);
                    MyMobile.sendMessageToEmp(MainPage.tfMessageInput.getText(),empPhoneNumber);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void callDept(String deptName){
        try{
            //The Factory|Nash|+1 519 615-5242|520
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            String empName="";
            String empPhoneNumber="";
            int deptID=-1;

            for(int i=0;i<Main.myDepts.size();i++){
                if(Main.myDepts.get(i).name.equals(deptName))
                    deptID = Main.myDepts.get(i).id;
            }

            try(Statement statement = conn.createStatement();
                ResultSet results = statement.executeQuery(
                        "select empPhoneNumber from emp inner join deptDetails\n" +
                        "on emp.id = deptDetails.empID where deptid = "+ deptID+";")) {
                while(results.next()) {
                    empPhoneNumber = results.getString(1);
                    MyMobile.sendMessageToEmp(MainPage.tfMessageInput.getText(),empPhoneNumber);
                }
            } catch (SQLException ex) {
            ex.printStackTrace();
        }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkCount(String column,String compare) {

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }


        String  sqlCommand = ""   ;


        try{
            int empID = Integer.parseInt(compare);
            sqlCommand = "select  count(*) from emp inner join deptdetails on emp.id = deptdetails.empID where "+column+" = " +  empID + " ;";
        }catch (Exception e){
           sqlCommand = "select  count(*) from emp inner join deptdetails on emp.id = deptdetails.empID where "+column+" like '%" + compare + "%' ;";
        }

        int deptCount = 0;
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sqlCommand)) {
            while (results.next()) {
                deptCount = Integer.parseInt(results.getString(1));
                if (deptCount != 0) {
                    return true;
                } else {
                    return false;
                }
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }


    //Add emp to EMP and DeptDetails.
    public static void addEmployee(int idNumber,String name,String phoneNumber,String department){

       executeCommand("insert into emp('empName','empPhoneNumber','id') values ('"+name+"', '"+phoneNumber+"','"+idNumber  +"');");
        //getting the ID of the dept wished.
        int deptId=-1;
        for(int i=0;i<Main.myDepts.size();i++){
            //System.out.println("Checking "+ Main.myDepts.get(i).name+" - "+Main.myDepts.get(i).id);
            if(Main.myDepts.get(i).name.equals(department)){
                deptId=Main.myDepts.get(i).id;
            }
        }
        executeCommand("insert into deptdetails('empID','deptID') values ("+idNumber+" ,"+deptId+"  );");
    }


    //remove EMP from EMP table and DeptDetails
    //that's all we need to remove from an EMP.
    public static void removeEMP(int id){
        executeCommand("delete from emp where id = "+id+" ; ");
        executeCommand("delete from deptdetails where empID = "+id+" ; ");

    }

    //run whatever SQL Query here.
    public static void executeCommand(String sqlQuery){
        try{
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);

            Statement statement = conn.createStatement();
            statement.executeUpdate(sqlQuery);

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
