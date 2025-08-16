package controllers;

import entities.DTO.TransactionDTO;
import entities.enums.TypeTransaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import services.interfaces.TransactionService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/transactions")
@Tag(name = "Transaction Controller", description = "Управление транзакциями")
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Создать новую транзакцию")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Транзакция успешно создана"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные транзакции")
    })
    @PostMapping("/")
    public ResponseEntity<Void> create(@RequestBody @NotNull(message = "Данные транзакции обязательны") TransactionDTO transaction) {
        transactionService.create(
                transaction.getFromAccountId(),
                transaction.getToAccountId(),
                transaction.getAmount(),
                transaction.getTypeTransaction()
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Получить транзакцию по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Транзакция найдена",
                    content = @Content(schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Некорректный ID транзакции"),
            @ApiResponse(responseCode = "404", description = "Транзакция не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> read(
            @PathVariable @NotNull(message = "ID транзакции обязателен")
            @Positive(message = "ID транзакции должен быть больше 0") Long id) {
        return ResponseEntity.ok(transactionService.read(id));
    }

    @Operation(summary = "Удалить транзакцию")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Транзакция удалена"),
            @ApiResponse(responseCode = "400", description = "Некорректный ID транзакции"),
            @ApiResponse(responseCode = "404", description = "Транзакция не найдена")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @NotNull(message = "ID транзакции обязателен")
            @Positive(message = "ID транзакции должен быть больше 0") Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить транзакции счёта с фильтрацией по типу операции")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список транзакций найден",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TransactionDTO.class)))),
            @ApiResponse(responseCode = "204", description = "Транзакций по указанным фильтрам не найдено")
    })
    @GetMapping("/all")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByAccountIdAndTypeTransaction(
            @RequestParam @NotNull(message = "ID счёта обязателен")
            @Positive(message = "ID счёта должен быть больше 0") Long id,
            @RequestParam(required = false) TypeTransaction typeTransaction) {

        List<TransactionDTO> transactions = transactionService.getTransactionsByAccountIdAndTypeTransaction(id, typeTransaction);

        if (transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(transactions);
    }
}
