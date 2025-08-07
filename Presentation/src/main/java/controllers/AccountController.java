package controllers;

import entities.DTO.AccountDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.interfaces.AccountService;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Account Controller", description = "Управление счетами пользователей")

public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Создать новый счёт")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Счет успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректный id пользователя"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PostMapping("/create/{id}")
    public ResponseEntity<Void> create(@PathVariable Long id) {
        accountService.create(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Получить счёт по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Счёт найден",
                    content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "404", description = "Счёт не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> read(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.read(id));
    }

    @Operation(summary = "Обновить данные счёта")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные счёта обновлены"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Счёт не найден")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody AccountDTO account) {
        accountService.update(
                id,
                account.getUserId()
        );
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить счёт")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Счёт удален"),
            @ApiResponse(responseCode = "404", description = "Счёт не найден")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить баланс счёта по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Счёт найден",
                    content = @Content(schema = @Schema(implementation = Double.class))),
            @ApiResponse(responseCode = "404", description = "Счёт не найден")
    })
    @GetMapping("/balance/{id}")
    public ResponseEntity<Double> getBalanceByAccountId(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getBalanceByAccountId(id));
    }

    @Operation(summary = "Получить все счета по ID пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь найден",
                    content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/all/{id}")
    public ResponseEntity<List<AccountDTO>> getAccountsByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountsByUserId(id));
    }
}
