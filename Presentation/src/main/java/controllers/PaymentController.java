package controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.interfaces.PaymentService;

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
            @ApiResponse(responseCode = "400", description = "Неверная сумма или некорректный ID счёта"),
            @ApiResponse(responseCode = "404", description = "Счёт не найден")
    })
    @PostMapping("/replenish/{accountId}")
    public ResponseEntity<Void> replenishAmount(@PathVariable Long accountId, @RequestParam Double amount) {
        paymentService.replenishAmount(accountId, amount);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Снять средства со счёта")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Средства успешно сняты"),
            @ApiResponse(responseCode = "400", description = "Неверная сумма, недостаточно средств или некорректный ID счёта"),
            @ApiResponse(responseCode = "404", description = "Счёт не найден")
    })
    @PostMapping("/withdraw/{accountId}")
    public ResponseEntity<Void> withdrawAmount(@PathVariable Long accountId, @RequestParam Double amount) {
        paymentService.withdrawAmount(accountId, amount);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Перевести средства между счетами")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Перевод выполнен успешно"),
            @ApiResponse(responseCode = "400", description = "Неверная сумма, недостаточно средств или некорректные ID счетов"),
            @ApiResponse(responseCode = "404", description = "Один из счетов не найден")
    })
    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestParam Long fromAccountId, @RequestParam Long toAccountId, @RequestParam Double amount) {
        paymentService.transfer(fromAccountId, toAccountId, amount);
        return ResponseEntity.ok().build();
    }
}
