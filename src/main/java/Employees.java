import java.util.ArrayList;

public class Employees {

    String empName;
    String empPhoneNumber;
    int empID;
    String dept;
    int deptID;

    public Employees(String empName, String empPhoneNumber, String empDept,int empID) {
        this.empName = empName;
        this.empPhoneNumber = empPhoneNumber;
        this.empID = empID;
        this.dept = empDept;

    }

    @Override
    public String toString() {
        return "Employees{" +
                "empName='" + empName + '\'' +
                ", empPhoneNumber='" + empPhoneNumber + '\'' +
                ", empID=" + empID +
                ", dept='" + dept + '\'' +
                '}';
    }
}
