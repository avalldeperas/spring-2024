package edu.uoc.epcsd.user.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.uoc.epcsd.user.controllers.dtos.CreateUserRequest;
import edu.uoc.epcsd.user.controllers.dtos.GetUserResponse;
import edu.uoc.epcsd.user.entities.User;
import edu.uoc.epcsd.user.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        log.trace("getAllUsers");

        return userService.findAll();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GetUserResponse> getUserById(@PathVariable @NotNull Long userId) {
        log.trace("getUserById");

        return userService.findById(userId).map(user -> ResponseEntity.ok().body(GetUserResponse.fromDomain(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/toAlert")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GetUserResponse[]> getUsersToAlert(@RequestParam @NotNull Long productId, @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate availableOnDate) {
        log.trace("getUsersToAlert");

        return ResponseEntity.ok().body(userService.getUsersToAlert(productId, availableOnDate).stream().map(user -> GetUserResponse.fromDomain(user)).toArray(GetUserResponse[]::new));
    }

    @PostMapping
    public ResponseEntity<Long> createUser(@RequestBody CreateUserRequest createUserRequest) {
        log.trace("createUser");

        log.trace("Creating user " + createUserRequest);
        Long userId = userService.createUser(
                createUserRequest.getEmail(),
                createUserRequest.getPassword(),
                createUserRequest.getFullName(),
                createUserRequest.getPhoneNumber()).getId();
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userId)
                .toUri();

        return ResponseEntity.created(uri).body(userId);
    }

    // no more operations to add here! go away!
}
