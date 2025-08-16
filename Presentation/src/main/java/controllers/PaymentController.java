package controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import services.interfaces.PaymentService;

@Validated
@RestController
@RequestMapping("/payments")
@Tag(name = "Payment Controller", description = "Управление платежами")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(summary = "Пополнить баланс счёта")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Баланс успешно пополнен"),
            @ApiResponse(responseCode = "400", description = "Некорректный ID счёта"),
            @ApiResponse(responseCode = "422", description = "Неверная сумма (должна быть больше 0)"),
            @ApiResponse(responseCode = "404", description = "Счёт не найден")
    })
    @PostMapping("/replenish/{accountId}")
    public ResponseEntity<Void> replenishAmount(
            @PathVariable @NotNull(message = "ID счёта обязателен") Long accountId,
            @RequestParam @NotNull(message = "Сумма обязательна") @Positive(message = "Сумма должна быть больше 0") Double amount) {
        paymentService.replenishAmount(accountId, amount);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Снять средства со счёта")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Средства успешно сняты"),
            @ApiResponse(responseCode = "400", description = "Некорректный ID счёта"),
            @ApiResponse(responseCode = "422", description = "Неверная сумма (должна быть больше 0) или недостаточно средств"),
            @ApiResponse(responseCode = "404", description = "Счёт не найден")
    })
    @PostMapping("/withdraw/{accountId}")
    public ResponseEntity<Void> withdrawAmount(
            @PathVariable @NotNull(message = "ID счёта обязателен") Long accountId,
            @RequestParam @NotNull(message = "Сумма обязательна") @Positive(message = "Сумма должна быть больше 0") Double amount) {
        paymentService.withdrawAmount(accountId, amount);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Перевести средства между счетами")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Перевод выполнен успешно"),
            @ApiResponse(responseCode = "400", description = "Некорректный ID исходного или целевого счёта"),
            @ApiResponse(responseCode = "422", description = "Неверная сумма (должна быть больше 0) или недостаточно средств"),
            @ApiResponse(responseCode = "404", description = "Один или оба счёта не найдены")
    })
    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(
            @RequestParam @NotNull(message = "ID исходного счёта обязателен") Long fromAccountId,
            @RequestParam @NotNull(message = "ID целевого счёта обязателен") Long toAccountId,
            @RequestParam @NotNull(message = "Сумма обязательна") @Positive(message = "Сумма должна быть больше 0") Double amount) {
        paymentService.transfer(fromAccountId, toAccountId, amount);
        return ResponseEntity.ok().build();
    }
}
