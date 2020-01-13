package ru.lena.restaurant.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.lena.restaurant.service.VoteHistoryService;
import ru.lena.restaurant.to.VoteHistoryTo;
import ru.lena.restaurant.utils.EntityUtil;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = VoteHistoryRestController.REST_URL, produces = "application/json;charset=UTF-8")
public class VoteHistoryRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    static final String REST_URL = "/rest/admin/history";

    private final VoteHistoryService service;

    @Autowired
    public VoteHistoryRestController(VoteHistoryService service) {
        this.service = service;
    }

    @GetMapping       //методы для админа
    public List<VoteHistoryTo> getAllHistory() {
        log.debug("get all vote History");
        return EntityUtil.asToList(service.getAll());
    }


    @GetMapping("/{id}")  //методы для админа //МЕТОД ДЛЯ ЮЗЕРА ТОЖЕ -СМОТРЕТЬ ИНФУ
    public VoteHistoryTo getHistory(@PathVariable("id") Long id) {
        log.debug("get vote History by id {}", id);
        return EntityUtil.asTo(service.get(id));
    }

    @DeleteMapping("/{id}")  //методы для админа
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHistory(@PathVariable("id") Long id) {
        log.info("delete vote History by id  {}", id);
        service.delete(id);
    }

    @GetMapping("/restaurant/{id}")
    public List<VoteHistoryTo> findAllByRestaurantId(@PathVariable("id") Long id) {
        log.debug("get all History by restaurant id {}", id);
        return EntityUtil.asToList(service.findAllByRestaurantId(id));
    }

    @GetMapping("/sorted")
    public List<VoteHistoryTo> findAllSortedHistory() {
        log.debug("find all sorted history");
        return EntityUtil.asToList(service.findAllSorted());
    }

    @GetMapping("/sorted/{score}")
    public List<VoteHistoryTo> findAllSortedByScoreHistory(@PathVariable("score") Integer score) {
        log.debug("find all history sorted by score {}", score);
        return EntityUtil.asToList(service.findAllSortedByScore(score));
    }

    @GetMapping("/sorted/between")
    public List<VoteHistoryTo> findAllSortedBetweenHistory(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.debug("Find all history between {} and {}", startDate, endDate);
        return EntityUtil.asToList(service.findAllSortedBetween(startDate, endDate));
    }

}
