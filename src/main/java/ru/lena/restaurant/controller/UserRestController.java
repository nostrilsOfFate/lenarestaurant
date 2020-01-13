package ru.lena.restaurant.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.lena.restaurant.model.User;
import org.springframework.web.bind.annotation.RestController;
import ru.lena.restaurant.service.UserService;
import ru.lena.restaurant.to.UserTo;
import ru.lena.restaurant.utils.EntityUtil;
import ru.lena.restaurant.utils.UniqueMailValidator;

import java.net.URI;
import java.util.List;

import static ru.lena.restaurant.utils.ValidationUtil.assureIdConsistent;
import static ru.lena.restaurant.utils.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = UserRestController.REST_URL, produces = "application/json;charset=UTF-8")
public class UserRestController {

    static final String REST_URL = "/rest/admin/users";
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService service;

    @Autowired
    private UniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    @GetMapping
    public List<User> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable("id") Long id) {
        log.info("get user with {}", id);
        return service.get(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createWithLocation(@RequestBody UserTo userTo) {
        log.info("create new user {}", userTo);
        checkNew(userTo);
        User created = service.create(EntityUtil.createNewFromTo(userTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        log.info("delete user by id {}", id);
        service.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update( @RequestBody User user) {
            log.info("update user {}",user);
        assureIdConsistent(user, user.getId());
        service.update(user);
    }

    @GetMapping("/by")
    public User getByMail(@RequestParam("email") String email) {
        log.info("get user by email {}", email);
        return service.getByEmail(email);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void enable(@PathVariable("id") Long id, @RequestParam("enabled") boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        service.enable(id, enabled);
    }

}
