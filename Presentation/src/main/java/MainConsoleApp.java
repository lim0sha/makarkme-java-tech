import entities.enums.HairColor;
import repositories.TransactionRepository;
import repositories.UserRepository;
import repositories.AccountRepository;
import services.*;
import services.interfaces.*;
import utilities.FriendshipUtilityImpl;
import utilities.interfaces.FriendshipUtility;

import java.util.*;
import java.util.stream.Collectors;

public class MainConsoleApp {
    private final String ID_ERROR = "Ошибка: ID должен быть числом.";
    private final String NUMBER_ERROR = "Ошибка: введены некорректные числа.";

    private final Scanner scanner = new Scanner(System.in);
    private final interfaces.UserRepository userRepository = new UserRepository();
    private final interfaces.AccountRepository accountRepository = new AccountRepository();
    private final interfaces.TransactionRepository transactionRepository = new TransactionRepository();
    private final FriendshipUtility friendshipUtility = new FriendshipUtilityImpl(userRepository);
    private final UserService userService = new UserServiceImpl(userRepository);
    private final AccountService accountService = new AccountServiceImpl(userRepository, accountRepository);
    private final TransactionService transactionService = new TransactionServiceImpl(accountRepository, transactionRepository);
    private final FriendManagementService friendManagementService = new FriendManagementServiceImpl(userRepository);
    private final PaymentService paymentService = new PaymentServiceImpl(accountRepository, friendshipUtility, transactionService);

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            try {
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
                    case 0 -> {
                        running = false;
                        System.out.println("Выход из программы.");
                    }
                    default -> System.out.println("Неверный выбор, попробуйте снова.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число от 0 до 9.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\nМеню:");
        System.out.println("1 - Создание пользователя");
        System.out.println("2 - Вывод информации о пользователе");
        System.out.println("3 - Добавить друга");
        System.out.println("4 - Удалить друга");
        System.out.println("5 - Создать счет");
        System.out.println("6 - Просмотр баланса счета");
        System.out.println("7 - Снять со счета");
        System.out.println("8 - Пополнить счет");
        System.out.println("9 - Перевод денег с одного счета на другой");
        System.out.println("0 - Выход");
        System.out.print("Выберите действие: ");
    }

    private void createUser() {
        try {
            System.out.print("Введите логин: ");
            String login = scanner.nextLine().trim();

            System.out.print("Введите имя: ");
            String name = scanner.nextLine().trim();

            System.out.print("Введите возраст: ");
            int age = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Введите пол: ");
            String gender = scanner.nextLine().trim();

            System.out.print("Введите цвет волос (RED, ORANGE, YELLOW, GREEN, BLUE, VIOLET, BLACK, BLONDE): ");
            String colorInput = scanner.nextLine().trim();

            HairColor hairColor;
            try {
                hairColor = HairColor.valueOf(colorInput.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: допустимые значения: " + Arrays.stream(HairColor.values()).map(Enum::name).collect(Collectors.joining(", ")));
                return;
            }

            userService.createUser(login, name, age, gender, hairColor);
            System.out.println("Пользователь создан успешно.");

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: возраст должен быть числом");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }

    private void printUserInfo() {
        try {
            System.out.print("Введите ID пользователя: ");
            long userId = Long.parseLong(scanner.nextLine());

            Map<String, Object> userData = userService.getUser(userId);
            printUserDetails(userData);
            printUserFriends(userData);

        } catch (NumberFormatException e) {
            System.out.println(ID_ERROR);
        } catch (NoSuchElementException e) {
            System.out.println("Пользователь не найден.");
        } catch (Exception e) {
            System.out.println("Ошибка при получении информации: " + e.getMessage());
        }
    }

    private void printUserDetails(Map<String, Object> userData) {
        System.out.println("\nИнформация о пользователе:");
        System.out.println("ID: " + userData.get("userId"));
        System.out.println("Логин: " + userData.get("login"));
        System.out.println("Имя: " + userData.get("name"));
        System.out.println("Возраст: " + userData.get("age"));
        System.out.println("Пол: " + userData.get("gender"));
        System.out.println("Цвет волос: " + userData.get("hairColor"));
    }

    private void printUserFriends(Map<String, Object> userData) {
        @SuppressWarnings("unchecked")
        List<Long> friends = (List<Long>) userData.get("friendsId");

        System.out.println("\nДрузья:");
        if (friends == null || friends.isEmpty()) {
            System.out.println("  Нет друзей");
            return;
        }

        friends.forEach(friendId -> {
            try {
                Map<String, Object> friendData = userService.getUser(friendId);
                System.out.printf("  ID: %d, Имя: %s%n", friendId, friendData.get("name"));
            } catch (Exception e) {
                System.out.printf("  ID: %d, Имя: Неизвестный друг%n", friendId);
            }
        });
    }

    private void addFriend() {
        try {
            System.out.print("Введите ID пользователя: ");
            long userId = Long.parseLong(scanner.nextLine());

            System.out.print("Введите ID друга: ");
            long friendId = Long.parseLong(scanner.nextLine());

            if (friendManagementService.addFriend(userId, friendId)) {
                System.out.println("Друг успешно добавлен.");
            }
        } catch (NumberFormatException e) {
            System.out.println(ID_ERROR);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void removeFriend() {
        try {
            System.out.print("Введите ID пользователя: ");
            long userId = Long.parseLong(scanner.nextLine());

            System.out.print("Введите ID друга: ");
            long friendId = Long.parseLong(scanner.nextLine());

            if (friendManagementService.removeFriend(userId, friendId)) {
                System.out.println("Друг успешно удалён.");
            }
        } catch (NumberFormatException e) {
            System.out.println(ID_ERROR);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void createAccount() {
        try {
            System.out.print("Введите ID пользователя: ");
            long userId = Long.parseLong(scanner.nextLine());

            accountService.createAccount(userId);
            System.out.println("Счёт успешно создан.");
        } catch (NumberFormatException e) {
            System.out.println(ID_ERROR);
        } catch (Exception e) {
            System.out.println("Ошибка при создании счёта: " + e.getMessage());
        }
    }

    private void printAccountBalance() {
        try {
            System.out.print("Введите ID счёта: ");
            long accountId = Long.parseLong(scanner.nextLine());

            accountRepository.findById(accountId)
                    .ifPresentOrElse(
                            account -> System.out.printf("Баланс счёта: %.2f%n", account.getBalance()),
                            () -> System.out.println("Счёт не найден.")
                    );
        } catch (NumberFormatException e) {
            System.out.println(ID_ERROR);
        } catch (Exception e) {
            System.out.println("Ошибка при получении баланса: " + e.getMessage());
        }
    }

    private void withdrawFromAccount() {
        try {
            System.out.print("Введите ID счёта: ");
            long accountId = Long.parseLong(scanner.nextLine());

            System.out.print("Введите сумму для снятия: ");
            double amount = Double.parseDouble(scanner.nextLine());

            paymentService.withdrawAmount(accountId, amount);
            System.out.println("Средства успешно сняты.");
        } catch (NumberFormatException e) {
            System.out.println(NUMBER_ERROR);
        } catch (Exception e) {
            System.out.println("Ошибка при снятии средств: " + e.getMessage());
        }
    }

    private void replenishAccount() {
        try {
            System.out.print("Введите ID счёта: ");
            long accountId = Long.parseLong(scanner.nextLine());

            System.out.print("Введите сумму для пополнения: ");
            double amount = Double.parseDouble(scanner.nextLine());

            paymentService.replenishAmount(accountId, amount);
            System.out.println("Счёт успешно пополнен.");
        } catch (NumberFormatException e) {
            System.out.println(NUMBER_ERROR);
        } catch (Exception e) {
            System.out.println("Ошибка при пополнении счёта: " + e.getMessage());
        }
    }

    private void transferMoney() {
        try {
            System.out.print("Введите ID счёта отправителя: ");
            long fromAccountId = Long.parseLong(scanner.nextLine());

            System.out.print("Введите ID счёта получателя: ");
            long toAccountId = Long.parseLong(scanner.nextLine());

            System.out.print("Введите сумму для перевода: ");
            double amount = Double.parseDouble(scanner.nextLine());

            paymentService.transfer(fromAccountId, toAccountId, amount);
            System.out.println("Перевод выполнен успешно.");
        } catch (NumberFormatException e) {
            System.out.println(NUMBER_ERROR);
        } catch (Exception e) {
            System.out.println("Ошибка при переводе: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new MainConsoleApp().run();
    }
}