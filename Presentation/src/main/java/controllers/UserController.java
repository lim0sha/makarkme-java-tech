package controllers;

import entities.DTO.UserDTO;
import entities.enums.HairColor;
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
import services.interfaces.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "User Controller", description = "Управление пользователями")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Создать нового пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные пользователя"),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким логином уже существует")
    })
    @PostMapping("/create")
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
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> read(@PathVariable Long id) {
        return ResponseEntity.ok(userService.read(id));
    }

    @Operation(summary = "Обновить данные пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные пользователя обновлены"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody UserDTO user) {
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
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить всех пользователей с фильтрацией")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список пользователей",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDTO.class))))
    })
    @GetMapping("/all")
    public List<UserDTO> getAllUsersFiltered(@RequestParam(required = false) HairColor hairColor, @RequestParam(required = false) String gender) {
        return userService.getAllUsersFiltered(hairColor, gender);
    }

    @Operation(summary = "Получить друзей пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список друзей",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}/friends")
    public List<UserDTO> getUserFriends(@PathVariable Long id) {
        return userService.getUserFriends(id);
    }
}
