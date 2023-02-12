import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MyMobile {
    public static  String ACCOUNT_SID ;
        public static String AUTH_TOKEN ;

    public static void setUpAccount(){
        ACCOUNT_SID=fileToString("C:\\TheFactoryApps\\TheFactory_SMS\\SID.txt");
    }
    public static void setUpAuth_Token(){
        AUTH_TOKEN=fileToString("C:\\TheFactoryApps\\TheFactory_SMS\\TOKEN.txt");
    }

    private static String fileToString(String url){
        File file = new File(url);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String txt="";

        while (scanner.hasNext()){
            txt+= scanner.next();
        }

        return txt;
    }

    public static void sendMessageToEmp(String message,String phoneNumber){
        sendSMS(phoneNumber,message+", DON'T REPLY.");
    }

    public static void main(String[] args) {
        sendSMS("15196154641","test");
    }

    public static void sendSMS(String phoneNumber,String msg) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        System.out.println("Message sent for " + phoneNumber);

        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber(phoneNumber),
                        "MGbbe115a7ebff842c88b2ba55d2b3c275",
                        msg)
                .create();

        System.out.println(message.getSid());
    }

}
