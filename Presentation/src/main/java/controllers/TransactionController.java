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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.interfaces.TransactionService;

import java.util.List;

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
            @ApiResponse(responseCode = "400", description = "Некорректные данные транзакции"),
    })
    @PostMapping("/create")
    // Тут небольшой логический момент. Как мне видится, api не должно иметь возможности создавать транзакцию.
    // Этим должен заниматься PaymentService. В таком случае, весь этот код надо переделать под утилиту.
    // --НО в текущей реализации оставим как есть--
    public ResponseEntity<Void> create(@RequestBody TransactionDTO transaction) {
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
            @ApiResponse(responseCode = "404", description = "Транзакция не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> read(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.read(id));
    }

    @Operation(summary = "Удалить транзакцию")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Транзакция удалена"),
            @ApiResponse(responseCode = "404", description = "Транзакция не найдена")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить транзакции счета с фильтрацией по типу операции")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список транзакций",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TransactionDTO.class))))
    })
    @GetMapping("/all")
    public List<TransactionDTO> getTransactionsByAccountIdAndTypeTransaction(@RequestParam Long Id, @RequestParam TypeTransaction typeTransaction) {
        return transactionService.getTransactionsByAccountIdAndTypeTransaction(Id, typeTransaction);
    }
}
