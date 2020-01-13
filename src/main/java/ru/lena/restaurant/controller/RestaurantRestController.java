package ru.lena.restaurant.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.lena.restaurant.model.Restaurant;
import ru.lena.restaurant.service.RestaurantService;
import ru.lena.restaurant.to.RestaurantTo;
import ru.lena.restaurant.utils.EntityUtil;
import ru.lena.restaurant.utils.SecurityUtil;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantRestController.REST_URL, produces = "application/json;charset=UTF-8")
public class RestaurantRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    static final String REST_URL = "/rest/admin/restaurants";

    @Autowired
    private RestaurantService service;

    @GetMapping       //методы для админа
    public List<RestaurantTo> getAllRests() {
        log.info("get all restaurants");
        return EntityUtil.asToListRest(service.getAll());
    }

    @GetMapping("/{id}")  //методы для админа
    public RestaurantTo getRest(@PathVariable("id") Long id) {
        log.info("get restaurant by id {}", id);
        return EntityUtil.asTo(service.get(id));
    }

    @GetMapping("/byname")  //методы для админа
    public List<RestaurantTo> findAllByOrderByNameDesc() {
        log.info("find all restaurants sorted by name");
        return EntityUtil.asToListRest(service.findAllSortedByName());
    }

    @DeleteMapping("/{id}")  //методы для админа
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRest(@PathVariable("id") Long id) {
        log.info("delete restaurant by id {}", id);
        service.delete(id);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)   //методы для админа
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateRest(@RequestBody RestaurantTo restaurantTo) {
        log.info("update restaurant by id {}", restaurantTo.getId());
        service.update(restaurantTo);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)    //методы для админа
    public ResponseEntity<RestaurantTo> createWithLocationRest(@RequestBody RestaurantTo restaurantTo) {
        log.info("create with location new restaurant");
        Restaurant created = service.create(restaurantTo);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(EntityUtil.asTo(created));
    }


    @GetMapping("/by") //методы для админа
    public RestaurantTo getByNameRest(@RequestParam("name") String name) {
        log.info("get restaurant by name {}", name);
        return EntityUtil.asTo(service.getByName(name));
    }

    @PostMapping("/{id}/vote")
    @ResponseStatus(value = HttpStatus.OK)
    public void voteForRestaurant(@PathVariable("id") Long restaurantId){
        log.info("vote for restaurant with id {}", restaurantId);
        long userId = SecurityUtil.authUserId();
        service.voteForRestaurant(userId, restaurantId);
    }
}
