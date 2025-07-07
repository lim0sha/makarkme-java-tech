import entities.HairColor;
import entities.User;
import entities.Account;
import interfaces.TransactionRepository;
import interfaces.UserRepository;
import interfaces.AccountRepository;
import repositories.InMemoryTransactionRepository;
import repositories.InMemoryUserRepository;
import repositories.InMemoryAccountRepository;
import services.FriendManagementServiceImpl;
import services.PaymentServiceImpl;
import services.RegistrationServiceImpl;
import services.interfaces.FriendManagementService;
import services.interfaces.PaymentService;
import services.interfaces.RegistrationService;

import java.util.*;

/**
 * Основной класс консольного приложения для управления пользователями, счетами и транзакциями.
 * Предоставляет меню для взаимодействия с пользователями, счетами и операциями:
 * создание пользователя, просмотр информации, управление друзьями, операции со счетами (создание, пополнение, снятие, перевод).
 */

public class MainConsoleApp {

    private static final Scanner scanner = new Scanner(System.in);

    private static final UserRepository userRepository = new InMemoryUserRepository();
    private static final AccountRepository accountRepository = new InMemoryAccountRepository();
    private static final TransactionRepository transactionRepository = new InMemoryTransactionRepository();

    private static final RegistrationService registrationService = new RegistrationServiceImpl(userRepository);
    private static final FriendManagementService friendService = new FriendManagementServiceImpl(userRepository);
    private static final PaymentService paymentService = new PaymentServiceImpl(userRepository, accountRepository, transactionRepository);

    /**
     * Точка входа в приложение.
     * Запускает основной цикл с меню для выбора операций.
     *
     * @param args аргументы командной строки (не используются)
     */

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> createUser();
                case 2 -> printUserInfo();
                case 3 -> addFriend();
                case 4 -> removeFriend();
                case 5 -> createAccount();
                case 6 -> printAccountBalance();
                case 7 -> withdrawFromAccount();
                case 8 -> replenishAccount();
                case 9 -> transferMoney();
                case 0 -> running = false;
                default -> System.out.println("Неверный выбор, попробуйте снова.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\nМеню:");
        System.out.println("1 - Cоздание юзера");
        System.out.println("2 - Вывод информации о юзере");
        System.out.println("3 - Добавить друга");
        System.out.println("4 - Удалить друга");
        System.out.println("5 - Создать счет");
        System.out.println("6 - Просмотр баланса счёта");
        System.out.println("7 - Снять со счета");
        System.out.println("8 - Пополнить счет");
        System.out.println("9 - Перевод денег с одного счёта на другой");
        System.out.println("0 - Выход");
        System.out.print("Выберите действие: ");
    }

    /**
     * Создаёт нового пользователя, запрашивая данные через консоль.
     */
    private static void createUser() {
        try {
            System.out.print("Введите логин: ");
            String login = scanner.nextLine();

            System.out.print("Введите имя: ");
            String name = scanner.nextLine();

            System.out.print("Введите возраст: ");
            int age = Integer.parseInt(scanner.nextLine());

            System.out.print("Введите пол: ");
            String gender = scanner.nextLine();

            System.out.print("Введите цвет волос (RED, ORANGE, YELLOW, GREEN, BLUE, VIOLET, BLACK, BLONDE): ");
            HairColor hairColor = HairColor.valueOf(scanner.nextLine().toUpperCase());

            User user = registrationService.createUser(login, name, age, gender, new ArrayList<>(), hairColor);
            System.out.println("Пользователь создан с ID: " + user.getUserId());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: неверный формат ввода (например, цвет волос или число).");
        } catch (Exception e) {
            System.out.println("Ошибка при создании пользователя: " + e.getMessage());
        }
    }

    /**
     * Выводит информацию о пользователе по его ID.
     */
    private static void printUserInfo() {
        try {
            System.out.print("Введите ID пользователя: ");
            long userId = Long.parseLong(scanner.nextLine());

            var about = userRepository.aboutUser(userId);
            if (about.isEmpty()) {
                System.out.println("Пользователь не найден.");
            } else {
                about.forEach((k, v) -> System.out.println(k + ": " + v));
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом.");
        } catch (Exception e) {
            System.out.println("Ошибка при выводе информации о пользователе: " + e.getMessage());
        }
    }

    /**
     * Добавляет друга для пользователя по введённым ID.
     */
    private static void addFriend() {
        try {
            System.out.print("Введите ID пользователя: ");
            long userId = Long.parseLong(scanner.nextLine());

            System.out.print("Введите ID друга: ");
            long friendId = Long.parseLong(scanner.nextLine());

            if (friendService.addFriend(userId, friendId)) {
                System.out.println("Друг добавлен.");
            } else {
                System.out.println("Не удалось добавить друга.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Удаляет друга у пользователя по введённым ID.
     */
    private static void removeFriend() {
        try {
            System.out.print("Введите ID пользователя: ");
            long userId = Long.parseLong(scanner.nextLine());

            System.out.print("Введите ID друга: ");
            long friendId = Long.parseLong(scanner.nextLine());

            if (friendService.removeFriend(userId, friendId)) {
                System.out.println("Друг удалён.");
            } else {
                System.out.println("Не удалось удалить друга.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Создаёт новый счёт для пользователя по ID.
     */
    private static void createAccount() {
        try {
            System.out.print("Введите ID пользователя для создания счета: ");
            long userId = Long.parseLong(scanner.nextLine());

            Account account = paymentService.createAccount(userId);
            System.out.println("Счёт создан с ID: " + account.getAccountId());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом.");
        } catch (Exception e) {
            System.out.println("Ошибка при создании счета: " + e.getMessage());
        }
    }

    /**
     * Выводит баланс счёта по его ID.
     */
    private static void printAccountBalance() {
        try {
            System.out.print("Введите ID счета: ");
            long accountId = Long.parseLong(scanner.nextLine());

            var accountOpt = accountRepository.findById(accountId);
            if (accountOpt.isPresent()) {
                System.out.println("Баланс: " + accountOpt.get().getBalance());
            } else {
                System.out.println("Счёт не найден.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом.");
        } catch (Exception e) {
            System.out.println("Ошибка при выводе баланса: " + e.getMessage());
        }
    }

    /**
     * Выполняет снятие средств со счёта.
     */
    private static void withdrawFromAccount() {
        try {
            System.out.print("Введите ID счета: ");
            long accountId = Long.parseLong(scanner.nextLine());

            System.out.print("Введите сумму для снятия: ");
            double amount = Double.parseDouble(scanner.nextLine());

            if (paymentService.withdrawalAccount(accountId, amount)) {
                System.out.println("Снятие прошло успешно.");
            } else {
                System.out.println("Недостаточно средств или ошибка.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введены некорректные числа.");
        } catch (Exception e) {
            System.out.println("Ошибка при снятии средств: " + e.getMessage());
        }
    }

    /**
     * Выполняет пополнение счёта.
     */
    private static void replenishAccount() {
        try {
            System.out.print("Введите ID счета: ");
            long accountId = Long.parseLong(scanner.nextLine());

            System.out.print("Введите сумму для пополнения: ");
            double amount = Double.parseDouble(scanner.nextLine());

            if (paymentService.replenishmentAccount(accountId, amount)) {
                System.out.println("Пополнение прошло успешно.");
            } else {
                System.out.println("Ошибка пополнения.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введены некорректные числа.");
        } catch (Exception e) {
            System.out.println("Ошибка при пополнении счета: " + e.getMessage());
        }
    }

    /**
     * Выполняет перевод денег с одного счёта на другой.
     */
    private static void transferMoney() {
        try {
            System.out.print("Введите ID счёта отправителя: ");
            long fromAccountId = Long.parseLong(scanner.nextLine());

            System.out.print("Введите ID счёта получателя: ");
            long toAccountId = Long.parseLong(scanner.nextLine());

            System.out.print("Введите сумму для перевода: ");
            double amount = Double.parseDouble(scanner.nextLine());

            if (paymentService.transfer(fromAccountId, toAccountId, amount)) {
                System.out.println("Перевод выполнен успешно.");
            } else {
                System.out.println("Ошибка перевода.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введены некорректные числа.");
        } catch (Exception e) {
            System.out.println("Ошибка при переводе: " + e.getMessage());
        }
    }
}
