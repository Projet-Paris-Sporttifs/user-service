package psp.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import psp.user.exception.PaginationParamsException;
import psp.user.payload.response.PaginationResponse;
import psp.user.exception.UserNotFoundException;
import psp.user.model.User;
import psp.user.repository.CustomProperties;
import psp.user.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    CustomProperties props;

    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
    @GetMapping("{id:[\\d]+}")
    public ResponseEntity<User> getUser(@PathVariable final Long id) throws UserNotFoundException {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
    @GetMapping
    public PaginationResponse<User> getUsers(
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String limit
    ) {
        if (null == page) page = "0";
        if (null == limit) limit = props.getPaginationLimit();

        try {
            Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(limit));
            return userService.getPaginatedData(pageable);
        } catch (NumberFormatException e) {
            throw new PaginationParamsException();
        }
    }
}
