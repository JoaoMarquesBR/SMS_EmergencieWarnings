import java.util.ArrayList;

public class Main {

    //last few things modified
    //I added a feature to read the Dept table with their names
    //made a table DeptDetails to connect EMP to the Dept
    //OK GOOD

    //What needs to be done
    //When adding the employee, make a List of Depts they can work at
    //when emp works more in one dept, Adds It to the list

    //after that makes sure when populating for the view, all of them are marked.


    static ArrayList<Dept> myDepts = new ArrayList<>();
    static ArrayList<Employees>employees = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("start");
        MyMobile.setUpAccount();
        MyMobile.setUpAuth_Token();
        DatabaseCommands.setUp();

        new MainPage();
    }

    public static void reset(){
        //myDepts.clear();
        employees.clear();
        DatabaseCommands.queryResults();
    }
}
