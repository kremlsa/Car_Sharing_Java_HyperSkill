package carsharing;

public class Main {

    static String companyTable = "create table IF NOT EXISTS COMPANY" +
            "(id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
            " name VARCHAR(255) not NULL UNIQUE)";

    static String carTable = "create table IF NOT EXISTS CAR" +
            "(id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
            " name VARCHAR(255) not NULL UNIQUE," +
            "company_id INTEGER not NULL," +
            " foreign key (company_id) references COMPANY(id))";

    static String customerTable = "create table IF NOT EXISTS CUSTOMER" +
            "(id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
            " name VARCHAR(255) not NULL UNIQUE," +
            "rented_car_id INTEGER," +
            " foreign key (rented_car_id) references CAR(id)" +
            "ON UPDATE CASCADE)";



    public static void main(String[] args) {
        // write your code here
        DBClass db = new DBClass();
        String dbName;
        if (args.length == 0) {
            dbName = "sampleDB";
        } else {
            if (args[0].equals("-databaseFileName")) {
                dbName = args[1];
            } else {
                dbName = "sampleDB";
            }
        }
        db.createNewDatabase(dbName);
        //db.dropTable("COMPANY");
        //db.dropTable("CAR");
        //db.dropTable("CUSTOMER");
        db.createTable(companyTable);
        db.createTable(carTable);
        db.createTable(customerTable);
        Menu menu = new Menu(db);
        menu.loginMenu();
    }
}