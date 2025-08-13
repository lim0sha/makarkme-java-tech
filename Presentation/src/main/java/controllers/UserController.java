package controllers;

import entities.DTO.AccountDTO;
import entities.DTO.UserDTO;
import entities.enums.HairColor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import services.interfaces.AccountService;
import services.interfaces.UserService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/users")
@Tag(name = "User Controller", description = "Управление пользователями")
public class UserController {
    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public UserController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @Operation(summary = "Создать нового пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные пользователя"),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким логином уже существует")
    })
    @PostMapping("/")
    public ResponseEntity<Void> create(@RequestBody UserDTO user) {
        userService.create(
                user.getLogin(),
                user.getName(),
                user.getAge(),
                user.getGender(),
                user.getHairColor()
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Получить пользователя по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь найден",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь с указанным ID не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> read(@PathVariable @NotNull(message = "ID пользователя обязателен") Long id) {
        return ResponseEntity.ok(userService.read(id));
    }

    @Operation(summary = "Обновить данные пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные пользователя обновлены"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные пользователя"),
            @ApiResponse(responseCode = "404", description = "Пользователь с указанным ID не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable @NotNull(message = "ID пользователя обязателен") Long id,
            @RequestBody UserDTO user
    ) {
        userService.update(
                id,
                user.getLogin(),
                user.getName(),
                user.getAge(),
                user.getGender(),
                user.getHairColor()
        );
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Пользователь удален"),
            @ApiResponse(responseCode = "404", description = "Пользователь с указанным ID не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull(message = "ID пользователя обязателен") Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить всех пользователей с фильтрацией")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список пользователей найден",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))),
            @ApiResponse(responseCode = "204", description = "Пользователи с указанными фильтрами не найдены")
    })
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsersFiltered(
            @RequestParam(required = false) HairColor hairColor,
            @RequestParam(required = false) String gender
    ) {
        List<UserDTO> users = userService.getAllUsersFiltered(hairColor, gender);
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }

    @Operation(summary = "Получить друзей пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список друзей пользователя найден",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))),
            @ApiResponse(responseCode = "204", description = "У пользователя нет друзей"),
            @ApiResponse(responseCode = "404", description = "Пользователь с указанным ID не найден")
    })
    @GetMapping("/{id}/friends")
    public ResponseEntity<List<UserDTO>> getUserFriends(@PathVariable @NotNull(message = "ID пользователя обязателен") Long id) {
        List<UserDTO> friends = userService.getUserFriends(id);
        return friends.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(friends);
    }

    @Operation(summary = "Получить все счета пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список счетов пользователя найден",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AccountDTO.class)))),
            @ApiResponse(responseCode = "204", description = "У пользователя нет счетов"),
            @ApiResponse(responseCode = "404", description = "Пользователь с указанным ID не найден")
    })
    @GetMapping("/{id}/accounts")
    public ResponseEntity<List<AccountDTO>> getAccountsByUserId(@PathVariable @NotNull(message = "ID пользователя обязателен") Long id) {
        List<AccountDTO> accounts = accountService.getAccountsByUserId(id);
        return accounts.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(accounts);
    }
}
