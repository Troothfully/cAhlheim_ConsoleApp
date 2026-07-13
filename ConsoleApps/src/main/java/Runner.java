import java.nio.file.StandardWatchEventKinds;
import java.util.*;

//Admin credentials: admin, admin123
//User credentials: .....................

public class Runner {
    static Scanner sc = new Scanner(System.in);

    static List<User> users = new ArrayList<>();

    static{
        User admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("admin123");

        Customer customer1 = new Customer();
        customer1.setUsername("rohit");
        customer1.setPassword("rohit123");

        Customer customer2 = new Customer();
        customer2.setUsername("mohit");
        customer2.setPassword("mohit123");

        Customer customer3 = new Customer();
        customer3.setUsername("shobhit");
        customer3.setPassword("shobhit123");

        users.add(admin);
        users.add(customer1);
        users.add(customer2);
        users.add(customer3);

        CheckingAccount c1 = new CheckingAccount(1001, 500);
        SavingsAccount s1 = new SavingsAccount(1002, 1000);

        customer1.getAccounts().add(c1);
        customer1.getAccounts().add(s1);

        customer2.getAccounts().add(new CheckingAccount(1003, 2000));
        customer3.getAccounts().add(new SavingsAccount(1004, 3000));
    }

    public static void main(String[] args) {
        printMessage("Welcome to ABC Digital Bank");


        boolean flag = true;
        while(flag){
            User loggedInUser = login();

            if (loggedInUser == null) {
                System.out.println("Invalid Credentials");
            }
            else if (loggedInUser instanceof Admin) {
                adminDashboard(loggedInUser);
            }
            else if (loggedInUser instanceof Customer) {
                customerDashboard((Customer) loggedInUser);
            }

            System.out.println("Do you want to continue? Press y/n");
            String mainLoopUserResponse = sc.nextLine();
            if(mainLoopUserResponse.equalsIgnoreCase("n")){
                flag = false;
            }
        }

    }



    private static void viewAccounts(Customer customer){
        for(Account account : customer.getAccounts()){
            System.out.println(
                    "Account: " +
                    account.getAccountId() +
                    " Balance: $" +
                    account.getBalance()
            );

        }
    }

    private static Account findAccount(Customer customer, int accountId){
        for(Account account : customer.getAccounts()){
            if(account.getAccountId() == accountId){
                return account;
            }
        }
        return null;
    }

    private static void deposit(Customer customer){
        viewAccounts(customer);

        System.out.print("Enter account ID: ");
        int id = sc.nextInt();

        Account account = findAccount(customer, id);

        if(account == null){
            System.out.println("Account not found.");
            return;
        }

        System.out.print("Enter amount: ");
        double amount = sc.nextDouble();

        ((AccountOperations)account).deposit(amount);

        System.out.println("Deposit successful.");
    }

    private static void withdraw(Customer customer){
        viewAccounts(customer);

        System.out.print("Enter account ID: ");
        int id = sc.nextInt();

        Account account = findAccount(customer, id);

        if(account == null){
            System.out.println("Account not found.");
            return;
        }

        System.out.print("Enter amount: ");
        double amount = sc.nextDouble();

        ((AccountOperations)account).withdraw(amount);
    }

    private static void transfer(Customer customer){
        viewAccounts(customer);

        System.out.print("Transfer FROM account ID: ");
        int fromId = sc.nextInt();

        System.out.print("Transfer TO account ID: ");
        int toId = sc.nextInt();

        Account from = findAccount(customer, fromId);
        Account to = findAccount(customer, toId);

        if(from == null || to == null){
            System.out.println("Invalid account.");
            return;
        }

        System.out.print("Enter amount: ");
        double amount = sc.nextDouble();

        ((AccountOperations)from).transfer(to, amount);

        System.out.println("Transfer complete.");
    }

    private static void viewAllCustomers(){
        for(User user : users){
            if(user instanceof Customer){
                Customer customer = (Customer) user;

                System.out.println("\nCustomer: " + customer.getUsername());

                for(Account account : customer.getAccounts()){
                    System.out.println(
                            "Account ID: " +
                                    account.getAccountId() +
                                    " Balance: $" +
                                    account.getBalance()
                    );
                }
            }
        }
    }

    private static void addCustomer(){
        sc.nextLine();

        Customer customer = new Customer();

        System.out.print("Username: ");
        customer.setUsername(sc.nextLine());

        System.out.print("Password: ");
        customer.setPassword(sc.nextLine());

        users.add(customer);

        System.out.println("Customer added.");
    }

    private static void removeCustomer(){
        sc.nextLine();

        System.out.print("Enter username: ");
        String username = sc.nextLine();

        Iterator<User> iterator = users.iterator();

        while(iterator.hasNext()){
            User user = iterator.next();

            if(user instanceof Customer &&
                    user.getUsername().equals(username)){
                iterator.remove();
                System.out.println("Customer removed.");
                return;
            }
        }

        System.out.println("Customer not found.");
    }

    private static void addAccount(){
        sc.nextLine();

        System.out.print("Customer username: ");
        String username = sc.nextLine();

        Customer customer = null;

        for(User user : users){
            if(user instanceof Customer &&
                    user.getUsername().equals(username)){
                customer = (Customer) user;
                break;
            }
        }

        if(customer == null){
            System.out.println("Customer not found.");
            return;
        }

        System.out.print("1. Checking 2. Savings: ");
        int type = sc.nextInt();

        System.out.print("Account ID: ");
        int id = sc.nextInt();

        System.out.print("Initial Balance: ");
        double balance = sc.nextDouble();

        if(type == 1){
            customer.getAccounts().add(new CheckingAccount(id, balance));
        }
        else{
            customer.getAccounts().add(new SavingsAccount(id, balance));
        }

        System.out.println("Account added.");
    }

    private static void removeAccount(){
        sc.nextLine();

        System.out.print("Customer username: ");
        String username = sc.nextLine();

        Customer customer = null;

        for(User user : users){
            if(user instanceof Customer &&
                    user.getUsername().equals(username)){
                customer = (Customer) user;
                break;
            }
        }

        if(customer == null){
            System.out.println("Customer not found.");
            return;
        }

        System.out.print("Account ID: ");
        int id = sc.nextInt();

        Iterator<Account> iterator = customer.getAccounts().iterator();

        while(iterator.hasNext()){
            Account account = iterator.next();

            if(account.getAccountId() == id){
                iterator.remove();
                System.out.println("Account removed.");
                return;
            }
        }

        System.out.println("Account not found.");
    }

    private static void customerDashboard(Customer customer)
    {
        System.out.println("Welcome customer, " + customer.getUsername());
        boolean done = false;
        while(!done)
        {
            System.out.println("What would you like to do?");
            System.out.println("1. View accounts");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");
            int choice = sc.nextInt();

            switch(choice){

                case 1:
                    viewAccounts(customer);
                    break;

                case 2:
                    withdraw(customer);
                    break;

                case 3:
                    deposit(customer);
                    break;

                case 4:
                    transfer(customer);
                    break;

                case 5:
                    done = true;
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }

        sc.nextLine();
    }
    private static void adminDashboard(User user) {
        System.out.println("Welcome admin!");
        boolean done = false;
        while (!done) {
            System.out.println("What would you like to do?");
            System.out.println("1. View all customers & accounts");
            System.out.println("2. Add customer");
            System.out.println("3. Remove customer");
            System.out.println("4. Add account");
            System.out.println("5. Remove account");
            System.out.println("6. Exit");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    viewAllCustomers();
                    break;
                case 2:
                    addCustomer();
                    break;
                case 3:
                    removeCustomer();
                    break;
                case 4:
                    addAccount();
                    break;
                case 5:
                    removeAccount();
                    break;
                case 6:
                    done = true;
                    break;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        }

        sc.nextLine();
    }

    static User login() {
        System.out.println("Please enter your username and password separated by a space");

        String enteredUsernamePassword = sc.nextLine();
        String[] parts = enteredUsernamePassword.split(" ");

        String enteredUsername = parts[0];
        String enteredPassword = parts[1];

        for (User user : users) {
            if (enteredUsername.equals(user.getUsername()) &&
                    enteredPassword.equals(user.getPassword())) {

                return user;
            }
        }

        return null;   // login failed
    }

    static void printMessage(String message){
        System.out.println(message);
    }
}

class Bank{
    private int id;
    private String name;
    private List<Customer> customers = new ArrayList<>();

    public Bank(int id, String name, List<Customer> customers) {
        this.id = id;
        this.name = name;
        this.customers = customers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
}

abstract class User{
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    abstract String getUserType();
}

class Admin extends User{
    String getUserType() {
        return "admin";
    }
}

class Customer extends User
{
    private List<Account> accounts = new ArrayList<>();

    String getUserType() {
        return "customer";
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}



abstract class Account
{
    private int accountId;
    private double balance;

    public Account(int accountId, double balance)
    {
        this.accountId = accountId;
        this.balance = balance;
    }

    public int getAccountId()
    {
        return accountId;
    }
    public void setAccountId(int accountId)
    {
        this.accountId = accountId;
    }
    public double getBalance()
    {
        return balance;
    }
    public void setBalance(double balance)
    {
        this.balance = balance;
    }

    abstract double getInterestRate();
}

class CheckingAccount extends Account implements AccountOperations
{
    public CheckingAccount(int accountId, double balance) {
        super(accountId, balance);
    }

    @Override
    public void deposit(double amount) {
        setBalance(getBalance() + amount);
    }

    @Override
    public void withdraw(double amount) {
        if(getBalance() >= amount){
            setBalance(getBalance() - amount);
        }
        else{
            System.out.println("Insufficient Balance");
        }
    }

    @Override
    public void transfer(Account destination, double amount) {
        if(getBalance() >= amount){
            setBalance(getBalance() - amount);

            destination.setBalance(destination.getBalance() + amount);
        }
        else{
            System.out.println("Insufficient Balance");
        }
    }

    @Override
    public double getInterestRate()
    {
        return .01;
    }
}

class SavingsAccount extends Account implements AccountOperations
{
    public SavingsAccount(int accountId, double balance)
    {
        super(accountId, balance);
    }

    @Override
    public void deposit(double amount) {
        setBalance(getBalance() + amount);
    }

    @Override
    public void withdraw(double amount) {
        if(getBalance() >= amount){
            setBalance(getBalance() - amount);
        }
        else{
            System.out.println("Insufficient Balance");
        }
    }

    @Override
    public void transfer(Account destination, double amount) {
        if(getBalance() >= amount){
            setBalance(getBalance() - amount);

            destination.setBalance(destination.getBalance() + amount);
        }
        else{
            System.out.println("Insufficient Balance");
        }
    }

    @Override
    public double getInterestRate()
    {
        return .02;
    }
}

interface AccountOperations{
    void deposit(double amount);
    void withdraw(double amount);
    void transfer(Account destination, double amount);
}

