package controllers;

import io.swagger.v3.oas.annotations.Operation;
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
import services.interfaces.FriendManagementService;

@Validated
@RestController
@RequestMapping("/friends")
@Tag(name = "Friend Management Controller", description = "Управление друзьями пользователей")
public class FriendManagementController {
    private final FriendManagementService friendManagementService;

    @Autowired
    public FriendManagementController(FriendManagementService friendManagementService) {
        this.friendManagementService = friendManagementService;
    }

    @Operation(summary = "Добавить друга")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Друг успешно добавлен"),
            @ApiResponse(responseCode = "400", description = "Некорректный ID пользователя или друга"),
            @ApiResponse(responseCode = "404", description = "Пользователь или друг не найден"),
            @ApiResponse(responseCode = "409", description = "Пользователь уже является другом")
    })
    @PostMapping("/add")
    public ResponseEntity<Void> add(
            @RequestParam @NotNull(message = "ID пользователя обязателен")
            @Positive(message = "ID пользователя должен быть больше 0") Long userId,
            @RequestParam @NotNull(message = "ID друга обязателен")
            @Positive(message = "ID друга должен быть больше 0") Long friendId) {
        friendManagementService.add(userId, friendId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Удалить друга")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Друг удалён"),
            @ApiResponse(responseCode = "400", description = "Некорректный ID пользователя или друга"),
            @ApiResponse(responseCode = "404", description = "Пользователь или друг не найден")
    })
    @DeleteMapping("/remove")
    public ResponseEntity<Void> remove(
            @RequestParam @NotNull(message = "ID пользователя обязателен")
            @Positive(message = "ID пользователя должен быть больше 0") Long userId,
            @RequestParam @NotNull(message = "ID друга обязателен")
            @Positive(message = "ID друга должен быть больше 0") Long friendId) {
        friendManagementService.remove(userId, friendId);
        return ResponseEntity.noContent().build();
    }
}
