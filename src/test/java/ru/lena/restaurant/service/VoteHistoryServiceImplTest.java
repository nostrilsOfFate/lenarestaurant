package ru.lena.restaurant.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.lena.restaurant.model.VoteHistory;
import ru.lena.restaurant.repository.VoteHistoryRepository;
import ru.lena.restaurant.utils.exсeption.NotFoundException;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
@SpringBootTest
class VoteHistoryServiceImplTest {
    @Autowired
    private VoteHistoryService service;

    @Autowired
    private VoteHistoryRepository repository;

    @Autowired
    private RestaurantService restaurantService;

    @Test
    void delete() {
        service.delete(1L);
        assertThrows(NotFoundException.class, () ->
                service.get(1L));
    }

    @Test
    void deletedNotFound() throws Exception {
        service.delete(1L);
        assertThrows(NotFoundException.class, () ->
                service.delete(1));
    }

    @Test
    void getAll() {
        List<VoteHistory> expected = repository.findAll();
        assertNotNull(expected);
        List<VoteHistory> actual = service.getAll();
        assertNotNull(actual);
        expected.sort(Comparator.comparing(VoteHistory::getId));
        actual.sort(Comparator.comparing(VoteHistory::getId));
        assertEquals(expected, actual);
    }

    @Test
    void get() {
        VoteHistory expected = repository.findById(1L).orElse(null);
        assertNotNull(expected);
        VoteHistory actual = service.get(1L);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.get(22));
    }


//    @Test
    void update() {
        int firstSize = service.getAll().size();
        service.updateVoteHistory();
        int secondSize = service.getAll().size();
        assertEquals(firstSize + 3, secondSize);
    }

    @Test
    void findAllByRestaurantId() {
        List<VoteHistory> expected = repository.findAll()
                .stream()
                .filter(voteHistory -> voteHistory.getRestaurant().getId().equals(1L))
                .collect(Collectors.toList());
        assertNotNull(expected);
        List<VoteHistory> actual = service.findAllByRestaurantId(1L);
        assertNotNull(actual);
        expected.sort(Comparator.comparing(VoteHistory::getVotTime));
        actual.sort(Comparator.comparing(VoteHistory::getVotTime));
        assertEquals(expected, actual);
    }

    @Test
    void findAllSorted() {
        List<VoteHistory> expected = repository.findAll();
        assertNotNull(expected);
        List<VoteHistory> actual = service.findAllSorted();
        assertNotNull(actual);
        expected.sort(Comparator.comparing(VoteHistory::getVotTime));
        actual.sort(Comparator.comparing(VoteHistory::getVotTime));
        assertEquals(expected, actual);
    }

    @Test
    void findAllSortedByScore() {
        List<VoteHistory> expected = repository.findAll()
                .stream()
                .filter(voteHistory -> voteHistory.getRestaurant().getScore().equals(10))
                .collect(Collectors.toList());
        assertNotNull(expected);
        List<VoteHistory> actual = service.findAllSortedByScore(10);
        assertNotNull(actual);
        expected.sort(Comparator.comparing(VoteHistory::getId));
        actual.sort(Comparator.comparing(VoteHistory::getId));
        assertEquals(expected, actual);
    }

    @Test
    void findAllSortedBetween() {
        LocalDate start = LocalDate.of(2019,2,3);
        LocalDate end = LocalDate.of(2019,2,4);
        List<VoteHistory> expected = repository.findAll()
                .stream()
                .filter(voteHistory -> {
                    LocalDate date = voteHistory.getVotTime();
                    return ((date.isAfter(start) || date.isEqual(start)) &&
                            (date.isBefore(end) || date.isEqual(end)));
                }).collect(Collectors.toList());
        assertNotNull(expected);
        List<VoteHistory> actual = service.findAllSortedBetween(start, end);
        assertNotNull(actual);
        expected.sort(Comparator.comparing(VoteHistory::getVotTime));
        actual.sort(Comparator.comparing(VoteHistory::getVotTime));
        assertEquals(expected, actual);
    }
}