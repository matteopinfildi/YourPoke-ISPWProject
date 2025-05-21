package org.example.ispwproject;

import org.example.ispwproject.utils.dao.DAOFactory;
import org.example.ispwproject.utils.enumeration.PersistenceProvider;
import org.example.ispwproject.utils.enumeration.UI;

import java.util.InputMismatchException;
import java.util.Scanner;

public class SetUp {

    // mostra un menu per selezionare il provider di persistenza
    public boolean setUpPersistenceProvider() {
        Scanner scanner = new Scanner(System.in);
        PersistenceProvider persistenceProvider = null;
        boolean flag = false;

        while (!flag) {
            try {
                System.out.println("Select a Persistence Provider (1-4): \n1. File System\n2. DBMS\n3. In Memory\n4. Exit");
                int selection = scanner.nextInt();
                flag = true;

                switch (selection) {
                    case 1:
                        persistenceProvider = PersistenceProvider.FS;
                        break;
                    case 2:
                        persistenceProvider = PersistenceProvider.DB;
                        break;
                    case 3:
                        persistenceProvider = PersistenceProvider.IN_MEMORY;
                        break;
                    case 4:
                        System.out.println("Exit");
                        scanner.close();
                        return false;
                    default:
                        System.err.println("Invalid choice. Please try again.");
                        flag = false;
                }
            } catch (InputMismatchException _) {
                System.err.println("Invalid input! Please enter a valid number.");
                scanner.nextLine();  // Consume the invalid input
            }
        }

        System.out.println("Using " + persistenceProvider.name() + " Persistence Provider\n");
        DAOFactory.setPersistenceProvider(persistenceProvider);
        return true;
    }

    // mostra un menu per selezionare il tipo di interfaccia
    public UI setUI() {
        Scanner scanner = new Scanner(System.in);
        int uiFlag = -1;

        while (uiFlag == -1) {
            try {
                System.out.println("Select a UI (1-3): \n1. GUI\n2. CLI\n3. Exit");
                uiFlag = scanner.nextInt();

                switch (uiFlag) {
                    case 1:
                        System.out.println("Using GUI\n");
                        break;
                    case 2:
                        System.out.println("Using CLI\n");
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        return UI.NONE;
                    default:
                        System.err.println("Invalid choice. Please try again.");
                        uiFlag = -1;
                        break;
                }
            } catch (InputMismatchException _) {
                System.err.println("Invalid input! Please enter a valid number.");
                scanner.nextLine();  // Consume the invalid input
            }
        }

        return UI.fromInt(uiFlag); // ritorna il valore numerico relativo all'enum dell'interfaccia scelta
    }
}

