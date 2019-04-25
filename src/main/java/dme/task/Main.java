package dme.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
/*
* INN = 7728073720
* KPP = -
* DATE = current
*/
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите 0, если вы явлвяетесь ФЛ");
        System.out.println("Введите 1, если вы явлвяетесь ЮЛ");
        System.out.print("Ввод числа: ");
        int formAgent = in.nextInt();
        System.out.print("Введите свой ИНН: ");
        String INN = in.hasNext() ? in.next() : "";
        String KPP = null;
        if(formAgent == 1) {
            System.out.print("Введите свой КПП (только для ЮЛ): ");
             KPP = in.hasNext() ? in.next() : "";
        }
        SimpleDateFormat sdFormat = new SimpleDateFormat("dd.MM.yyyy");
        System.out.print("Введите дату (дд.мм.гггг): ");
        Date DATE = new Date();
        try {
            DATE = sdFormat.parse(in.next());
        } catch (Exception e) {
            System.out.println("Exception from parse: " + e.getMessage());
        }
        in.close();
        if (KPP != null) {
            SOAPClient soapclient = new SOAPClient(INN, KPP, sdFormat.format(DATE));
            System.out.println("\nРезультат запроса: " + soapclient.getState() +
                    " - " + soapclient.getStateInfo());
        } else {
            SOAPClient soapclient = new SOAPClient(INN, sdFormat.format(DATE));
            System.out.println("\nРезультат запроса: " + soapclient.getState() +
                    " - " + soapclient.getStateInfo());
        }
    }
}
