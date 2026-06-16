package bank;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CsvStorage {
    static final String ACCOUNTS_FILE = "accounts.csv";
    static final String OPERATIONS_FILE = "operations.csv";

    public static void saveAccounts(List<Account> accounts) throws IOException {
        FileWriter writer = new FileWriter(ACCOUNTS_FILE);
        writer.write("id,name,balance\n");
        for (Account account : accounts) {
            writer.write(account.id + "," + account.name + "," + account.balance + "\n");
        }
        writer.close();
    }

    public static List<Account> loadAccounts() throws IOException {
        List<Account> accounts = new ArrayList<>();
        File file = new File(ACCOUNTS_FILE);
        if (!file.exists()) {
            return accounts;
        }

        Scanner reader = new Scanner(file);
        reader.nextLine();
        while (reader.hasNextLine()) {
            String[] parts = reader.nextLine().split(",");
            int id = Integer.parseInt(parts[0]);
            String name = parts[1];
            double balance = Double.parseDouble(parts[2]);
            Account account = new Account(id, name);
            account.balance = balance;
            accounts.add(account);
        }
        reader.close();
        return accounts;
    }

    public static void saveOperations(List<Operation> operations) throws IOException {
        FileWriter writer = new FileWriter(OPERATIONS_FILE);
        writer.write("id,accountId,type,amount\n");
        for (Operation operation : operations) {
            writer.write(operation.id + ","
                    + operation.accountId + ","
                    + operation.type + ","
                    + operation.amount + "\n");
        }
        writer.close();
    }

    public static List<Operation> loadOperations() throws IOException {
        List<Operation> operations = new ArrayList<>();
        File file = new File(OPERATIONS_FILE);
        if (!file.exists()) {
            return operations;
        }

        Scanner reader = new Scanner(file);
        reader.nextLine();
        while (reader.hasNextLine()) {
            String[] parts = reader.nextLine().split(",");
            int id = Integer.parseInt(parts[0]);
            int accountId = Integer.parseInt(parts[1]);
            String type = parts[2];
            double amount = Double.parseDouble(parts[3]);
            operations.add(new Operation(id, accountId, type, amount));
        }
        reader.close();
        return operations;
    }
}
