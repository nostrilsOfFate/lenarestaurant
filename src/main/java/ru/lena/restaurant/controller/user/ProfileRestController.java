package ru.lena.restaurant.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.lena.restaurant.model.AuthorizedUser;
import ru.lena.restaurant.model.User;
import ru.lena.restaurant.service.UserService;
import ru.lena.restaurant.utils.EntityUtil;
import ru.lena.restaurant.utils.UniqueMailValidator;
import ru.lena.restaurant.to.UserTo;
import ru.lena.restaurant.utils.SecurityUtil;

import javax.validation.Valid;
import java.net.URI;

import static ru.lena.restaurant.utils.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(ProfileRestController.REST_URL)
public class ProfileRestController {

    static final String REST_URL = "/rest/profile";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService service;

    @Autowired
    private UniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public User get(@AuthenticationPrincipal AuthorizedUser authUser) {
        Long userId = SecurityUtil.authUserId();
        log.info("get authorized user with id {} for user {}", authUser.getId(), userId);
        return service.get(authUser.getId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthorizedUser authUser) {
        Long userId = SecurityUtil.authUserId();
        log.info("delete authorized user with id {} for user {}", authUser.getId(), userId);
        service.delete(authUser.getId());
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
        Long userId = SecurityUtil.authUserId();
        log.info("register user {} for user {}", userTo, userId);
        User created = service.create(EntityUtil.createNewFromTo(userTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody UserTo userTo, @AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("update {} with id={}", userTo, authUser.getId());
        assureIdConsistent(userTo, authUser.getId());
        service.update(userTo);
    }

    @GetMapping(value = "/text")
    public String testUTF() {
        Long userId = SecurityUtil.authUserId();
        log.info("testUTF {}", userId);
        return "Русский текст";
    }

}
