package controllers;

import entities.DTO.AccountDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import services.interfaces.AccountService;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Account Controller", description = "Управление счетами пользователей")
@Validated
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Создать новый счёт")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Счёт успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректный ID пользователя"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PostMapping("/{id}")
    public ResponseEntity<Void> create(
            @PathVariable @NotNull(message = "ID пользователя обязателен")
            @Positive(message = "ID пользователя должен быть больше 0") Long id) {
        accountService.create(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Получить счёт по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Счёт найден",
                    content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Некорректный ID счёта"),
            @ApiResponse(responseCode = "404", description = "Счёт не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> read(
            @PathVariable @NotNull(message = "ID счёта обязателен")
            @Positive(message = "ID счёта должен быть больше 0") Long id) {
        return ResponseEntity.ok(accountService.read(id));
    }

    @Operation(summary = "Обновить данные счёта")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные счёта обновлены"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные или ID счёта"),
            @ApiResponse(responseCode = "404", description = "Счёт не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable @NotNull(message = "ID счёта обязателен")
            @Positive(message = "ID счёта должен быть больше 0") Long id,
            @RequestBody AccountDTO account) {
        accountService.update(id, account.getUserId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить счёт")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Счёт удалён"),
            @ApiResponse(responseCode = "400", description = "Некорректный ID счёта"),
            @ApiResponse(responseCode = "404", description = "Счёт не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @NotNull(message = "ID счёта обязателен")
            @Positive(message = "ID счёта должен быть больше 0") Long id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить баланс счёта по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Счёт найден",
                    content = @Content(schema = @Schema(implementation = Double.class))),
            @ApiResponse(responseCode = "400", description = "Некорректный ID счёта"),
            @ApiResponse(responseCode = "404", description = "Счёт не найден")
    })
    @GetMapping("/balance/{id}")
    public ResponseEntity<Double> getBalanceByAccountId(
            @PathVariable @NotNull(message = "ID счёта обязателен")
            @Positive(message = "ID счёта должен быть больше 0") Long id) {
        return ResponseEntity.ok(accountService.getBalanceByAccountId(id));
    }
}
