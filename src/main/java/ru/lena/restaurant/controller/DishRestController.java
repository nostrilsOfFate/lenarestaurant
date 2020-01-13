package ru.lena.restaurant.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.lena.restaurant.model.Dish;
import ru.lena.restaurant.service.DishService;
import ru.lena.restaurant.to.DishTo;
import ru.lena.restaurant.utils.EntityUtil;
import ru.lena.restaurant.utils.SecurityUtil;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = DishRestController.REST_URL, produces = "application/json;charset=UTF-8")
public class DishRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    static final String REST_URL = "/rest/admin/dishes";

    @Autowired
    private DishService service;

    @GetMapping       //методы для админа
    public List<DishTo> getAllDishes() {
        log.info("get all dishes");
        return EntityUtil.asToListDishes(service.getAll());
    }

    @GetMapping("/{id}")
    public DishTo getDish(@NonNull @PathVariable Long id) {
        log.info("get dish {}", id);
        return EntityUtil.asTo(service.get(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDish(@NonNull @PathVariable Long id) {
        log.info("delete dish {}", id);
        service.delete(id);
    }

    @PutMapping(value = "/{dishId}/choose", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void isTodayMenuDish(@NonNull @PathVariable("dishId") Long dishId, @RequestParam boolean isTodayDish) {
        log.info("set today menu dish with id {}", dishId);
        //@RequestParam е работает, можно использовать POST
//        передавать параметры в url
//        использовать HttpPutFormContentFilter фильтр
//        настроить Tomcat в обход спецификации.
        service.setTodayMenu(dishId, isTodayDish);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateDish(@NonNull @RequestBody DishTo dishTo) {
        log.info("update dish {}", dishTo);
        service.update(dishTo);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DishTo> createWithLocationDish(@NonNull @RequestBody DishTo dishTo) {
        log.info("createWithLocation dish {}", dishTo);
        Dish created = service.create(dishTo);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(EntityUtil.asTo(created));
    }

}
