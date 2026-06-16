package bank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Assistant {
    List<Account> accounts = new ArrayList<>();
    List<Operation> operations = new ArrayList<>();
    int nextAccountId = 1;
    int nextOperationId = 1;

    public static void main(String[] args) {
        Assistant app = new Assistant();
        app.loadData();

        Scanner sc = new Scanner(System.in);
        System.out.println("Commands: create, deposit, withdraw, list, exit");

        while (true) {
            System.out.print("> ");
            String command = sc.nextLine().trim();
            if (command.equals("exit")) {
                break;
            }
            Tools.manipulate(command, app);
        }

        app.saveData();
        sc.close();
    }

    void loadData() {
        try {
            accounts = CsvStorage.loadAccounts();
            operations = CsvStorage.loadOperations();

            for (Account account : accounts) {
                if (account.id >= nextAccountId) {
                    nextAccountId = account.id + 1;
                }
            }
            for (Operation operation : operations) {
                if (operation.id >= nextOperationId) {
                    nextOperationId = operation.id + 1;
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load data.");
        }
    }

    void saveData() {
        try {
            CsvStorage.saveAccounts(accounts);
            CsvStorage.saveOperations(operations);
        } catch (IOException e) {
            System.out.println("Could not save data.");
        }
    }

    Account findAccount(int id) {
        for (Account account : accounts) {
            if (account.id == id) {
                return account;
            }
        }
        return null;
    }
}
