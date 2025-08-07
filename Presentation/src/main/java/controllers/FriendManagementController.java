package controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.interfaces.FriendManagementService;

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
            @ApiResponse(responseCode = "400", description = "Некорректные данные пользователя(ей)"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "409", description = "Пользователь уже является другом")
    })
    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestParam Long userId, @RequestParam Long friendId) {
        friendManagementService.create(userId, friendId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Удалить друга")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Друг удален"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные пользователя(ей)"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam Long userId, @RequestParam Long friendId) {
        friendManagementService.delete(userId, friendId);
        return ResponseEntity.noContent().build();
    }
}
