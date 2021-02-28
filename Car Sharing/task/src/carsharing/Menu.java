package carsharing;

import javafx.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class Menu {
    Scanner sc = new Scanner(System.in);
    DBClass db;

    public Menu (DBClass db) {
        this.db = db;
    }

    public void loginMenu () {

        boolean isRun = true;

        while (isRun) {
            System.out.println("1. Log in as a manager\n" +
                    "2. Log in as a customer\n" +
                    "3. Create a customer\n" +
                    "0. Exit");
            String input = sc.nextLine();
            switch (input) {
                case "1":
                    managerMenu();
                    break;
                case "2":
                    customerList();
                    break;
                case "3":
                    createCustomer();
                    break;
                case "0":
                    isRun = false;
                    break;
                default :
                    System.out.println("Incorrect input");
                    break;
            }
        }
    }

    public void customerList() {
        boolean isRun = true;
        System.out.println("Choose a customer:");
        String query = "SELECT id, name FROM customer";
        ResultSet rs = db.executeResultQuery(query);
        String menuString = "";
        boolean isFind = false;
        int order = 1;
        Map<String, Pair> pairs = new LinkedHashMap<>();
        try {
            while (rs.next()) {
                menuString += "" + order + ". " +
                        rs.getString("name") + "\n";
                pairs.put(String.valueOf(order), new Pair<>(rs.getInt("id"), rs.getString("name")));
                order++;
                //System.out.println(rs.getInt("id") + ". " +
                //        rs.getString("name"));
                isFind =true;
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        if(!isFind) {
            menuString = "The customer list is empty!" + "\n";
            System.out.print(menuString);
            isRun = false;
        }


        while (isRun) {
            System.out.println(menuString +
                    "0. Back");
            String input = sc.nextLine();
            switch (input) {
                case "0":
                    isRun = false;
                    break;
                default :
                    customerMenu(String.valueOf(pairs.get(input).getKey()));
                    isRun = false;
                    break;
            }
        }
    }

    public void customerMenu (String customerID) {

        boolean isRun = true;

        while (isRun) {
            System.out.println("1. Rent a car\n" +
                    "2. Return a rented car\n" +
                    "3. My rented car\n" +
                    "0. Back");
            String input = sc.nextLine();
            switch (input) {
                case "1":
                    rentCar(customerID);
                    break;
                case "2":
                    returnCar(customerID);
                    break;
                case "3":
                    rentedCar(customerID);
                    break;
                case "0":
                    isRun = false;
                    break;
                default :
                    System.out.println("Incorrect input");
                    break;
            }
        }
    }

    public void returnCar(String customerID) {
        String query = "SELECT name, rented_car_id FROM CUSTOMER WHERE id=" + customerID;
        int companyId = 0;
        int rentedCarId = -1;
        String carName = "";
        String companyName = "";
        ResultSet rs = db.executeResultQuery(query);
        boolean isFind = false;
        try {
            while (rs.next()) {
                rentedCarId = rs.getInt("rented_car_id");
            }
            if (rentedCarId > 0) {
                isFind =true;
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }

        if(!isFind) {
            System.out.println("You didn't rent a car!");
        } else {
            String query2 = "UPDATE CUSTOMER SET rented_car_id = NULL " +
                    "WHERE id = " + customerID;
            db.executeQuery(query2);
            System.out.println("You've returned a rented car!");
        }

    }

    public void rentCar(String customerID) {
        boolean isRun = true;
        String query = "SELECT id, name FROM company";
        String query2 = "SELECT rented_car_id FROM customer WHERE id = " + customerID;
        ResultSet rs = db.executeResultQuery(query);
        ResultSet rs2 = db.executeResultQuery(query2);
        String menuString = "";
        boolean isFind = false;
        boolean isRented = false;
        int order = 1;
        Map<String, Pair> pairs = new LinkedHashMap<>();
        try {
            while (rs.next()) {
                menuString += "" + order + ". " +
                        rs.getString("name") + "\n";
                pairs.put(String.valueOf(order), new Pair<>(rs.getInt("id"), rs.getString("name")));
                order++;
                isFind =true;
            }

            while (rs2.next()) {
                if (rs2.getInt("rented_car_id") > 0) {
                    isRented = true;
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        if(!isFind) {
            menuString = "The company list is empty!" + "\n";
            System.out.print(menuString);
            isRun = false;
        }

        if(isRented) {
            menuString = "You've already rented a car!" + "\n";
            System.out.print(menuString);
            isRun = false;
        }


        while (isRun) {
            System.out.println("Choose the company:");
            System.out.println(menuString +
                    "0. Back");
            String input = sc.nextLine();
            switch (input) {
                case "0":
                    isRun = false;
                    break;
                default :
                    customerCarMenu(String.valueOf(pairs.get(input).getKey()), customerID);
                    isRun = false;
                    break;
            }
        }

    }

    public void customerCarMenu(String companyID, String customerID) {
        int order = 1;
        System.out.println("Car list:");
        String query = "SELECT id, name FROM car WHERE company_id = " + companyID;
        ResultSet rs = db.executeResultQuery(query);
        Map<String, Pair> pairs = new LinkedHashMap<>();
        boolean isFind = false;
        String menuString = "Choose a car:\n";
        try {
            while (rs.next()) {
                menuString += "" + order + ". " +
                        rs.getString("name");
                pairs.put(String.valueOf(order), new Pair<>(rs.getInt("id"), rs.getString("name")));
                order++;
                isFind =true;
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        if(!isFind) {
            System.out.println("The car list is empty!");
        } else {
            System.out.println(menuString);
            String choice = sc.nextLine();
            query = "UPDATE customer SET rented_car_id = " + pairs.get(choice).getKey() +
            "WHERE id = " + customerID;
            db.executeQuery(query);
            System.out.println("You rented '" + pairs.get(choice).getValue() + "'");
        }
        System.out.println();
    }

    public void rentedCar(String customerID) {
        String query = "SELECT name, rented_car_id FROM CUSTOMER WHERE id=" + customerID;
        int companyId = 0;
        int rentedCarId = -1;
        String carName = "";
        String companyName = "";
        ResultSet rs = db.executeResultQuery(query);
        boolean isFind = false;
        try {
            while (rs.next()) {
                rentedCarId = rs.getInt("rented_car_id");
            }
            if (rentedCarId > 0) {
                isFind =true;
                String query2 = "SELECT name, company_id FROM CAR where id=" + rentedCarId;
                rs = db.executeResultQuery(query2);
                while (rs.next()) {
                    companyId = rs.getInt("company_id");
                    carName = rs.getString("name");
                }
                String query3 = "SELECT name FROM COMPANY where id=" + companyId;
                rs = db.executeResultQuery(query3);
                while (rs.next()) {
                    companyName = rs.getString("name");
                }
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }
        if(!isFind) {
            System.out.println("You didn't rent a car!");
        } else {
            System.out.println("Your rented a car:\n" +
                     carName + "\n" +
                    "Company:\n" +
                    companyName);
        }

    }

    public void createCustomer() {
        System.out.println("Enter the customer name:");
        String customerName = sc.nextLine();
        String query = "INSERT INTO customer (name) VALUES('" + customerName + "')";
        db.executeQuery(query);
        System.out.println("The customer was created!");
    }

    public void managerMenu() {

        boolean isRun = true;

        while (isRun) {
            System.out.println("1. Company list\n" +
                    "2. Create a company\n" +
                    "0. Back");
            String input = sc.nextLine();
            switch (input) {
                case "1":
                    listCompany();
                    break;
                case "2":
                    createCompany();
                    break;
                case "0":
                    isRun = false;
                    break;
                default :
                    System.out.println("Incorrect input");
                    break;
            }
        }
    }

    public void createCompany() {
        System.out.println("Enter the company name:");
        String companyName = sc.nextLine();
        String query = "INSERT INTO company (name) VALUES('" + companyName + "')";
        db.executeQuery(query);
        System.out.println("The company was created!");
    }

    public void listCompany() {
        boolean isRun = true;
        int order = 1;
        System.out.println("Choose the company:");
        String query = "SELECT id, name FROM company";
        ResultSet rs = db.executeResultQuery(query);
        String menuString = "";
        Map<String, Pair> pairs = new LinkedHashMap<>();
        boolean isFind = false;
        try {
            while (rs.next()) {
                menuString += "" + order + ". " +
                        rs.getString("name") + "\n";
                pairs.put(String.valueOf(order), new Pair<>(rs.getInt("id"), rs.getString("name")));
                order++;
                //menuString += rs.getInt("id") + ". " +
                //        rs.getString("name") + "\n";
                //System.out.println(rs.getInt("id") + ". " +
                //        rs.getString("name"));
                isFind =true;
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        if(!isFind) {
            menuString = "The company list is empty!" + "\n";
            System.out.print(menuString);
            isRun = false;
        }


        while (isRun) {
            System.out.println(menuString +
                    "0. Back");
            String input = sc.nextLine();
            switch (input) {
                case "0":
                    isRun = false;
                    break;
                default :
                    companyMenu(String.valueOf(pairs.get(input).getKey()));
                    isRun = false;
                    break;
            }
        }
    }

    public void companyMenu (String number) {

        boolean isRun = true;

        while (isRun) {
            System.out.println("1. Car list\n" +
                    "2. Create a car\n" +
                    "0. Back");
            String input = sc.nextLine();
            switch (input) {
                case "1":
                    listCar(number);
                    break;
                case "2":
                    createCar(number);
                    break;
                case "0":
                    isRun = false;
                    break;
                default :
                    System.out.println("Incorrect input");
                    break;
            }
        }
    }

    public void createCar(String id) {
        System.out.println("Enter the car name:");
        String carName = sc.nextLine();
        String query = "INSERT INTO CAR (name, company_id) VALUES('" + carName + "', " + id + ")";
        db.executeQuery(query);
        System.out.println("The car was created!");
    }

    public void listCar(String id) {
        int order = 1;
        System.out.println("Car list:");
        String query = "SELECT id, name FROM car WHERE company_id = " + id;
        ResultSet rs = db.executeResultQuery(query);
        boolean isFind = false;
        try {
            while (rs.next()) {
                System.out.println("" + order + ". " +
                        rs.getString("name"));
                order++;
                isFind =true;
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        if(!isFind) {
            System.out.println("The car list is empty!");
        }
        System.out.println();

    }
}
