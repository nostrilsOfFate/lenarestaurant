package ru.lena.restaurant.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.lena.restaurant.service.UserServiceImpl;
import ru.lena.restaurant.service.VoteHistoryService;
import ru.lena.restaurant.to.VoteHistoryTo;
import ru.lena.restaurant.utils.EntityUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.lena.restaurant.TestUtil.asJsonString;
import static ru.lena.restaurant.TestUtil.listAsJsonString;

@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class VoteHistoryRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private VoteHistoryService service;

    @WithMockUser(username = "user")
    @Test
    void getAll() throws Exception {
        List<VoteHistoryTo> historyList = EntityUtil.asToList(service.getAll());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/rest/admin/history")
                .accept(MediaType.APPLICATION_JSON_VALUE);
        this.mvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().json(listAsJsonString(historyList)))
                .andDo(print())
                .andReturn();
    }

    @WithMockUser(username = "user")
    @Test
    void get() throws Exception {
        VoteHistoryTo voteHistoryTo = EntityUtil.asTo(service.get(1L));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/rest/admin/history/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        this.mvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().json(asJsonString(voteHistoryTo)))
                .andDo(print())
                .andReturn();
    }

    @WithMockUser(username = "user")
    @Test
    void delete() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/rest/admin/history/{id}", 1L)
                .with(csrf());
        this.mvc.perform(requestBuilder).andExpect(status().isNoContent()).andReturn();
    }

    @WithMockUser(username = "user")
    @Test
    void findAllByRestaurantId() throws Exception {
        List<VoteHistoryTo> historyList = EntityUtil.asToList(service.findAllByRestaurantId(1L));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/rest/admin/history/restaurant/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        this.mvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().json(listAsJsonString(historyList)))
                .andDo(print())
                .andReturn();
    }

    @WithMockUser(username = "user")
    @Test
    void findAllSorted() throws Exception {
        List<VoteHistoryTo> historyList = EntityUtil.asToList(service.findAllSorted());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/rest/admin/history/sorted")
                .accept(MediaType.APPLICATION_JSON_VALUE);
        this.mvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().json(listAsJsonString(historyList)))
                .andDo(print())
                .andReturn();
    }

    @WithMockUser(username = "user")
    @Test
    void findAllSortedByScore() throws Exception {
        List<VoteHistoryTo> historyList = EntityUtil.asToList(service.findAllSortedByScore(15));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/rest/admin/history/sorted/{score}", 15)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        this.mvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().json(listAsJsonString(historyList)))
                .andDo(print())
                .andReturn();
    }

    @WithMockUser(username = "user")
    @Test
    void findAllSortedBetween() throws Exception {
        LocalDate startDate = LocalDate.of(2019, 2, 3);
        LocalDate endDate = LocalDate.of(2019, 2, 4);
        List<VoteHistoryTo> historyList = EntityUtil.asToList(service.getAll().stream()
                .filter(voteHistory -> {
                    LocalDate date = voteHistory.getVotTime();
                    return ((date.isAfter(startDate) || date.isEqual(startDate)) &&
                            (date.isBefore(endDate) || date.isEqual(endDate)));
                }).collect(Collectors.toList()));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/rest/admin/history/sorted/between")
                .param("startDate", "2019-02-03")
                .param("endDate", "2019-02-04")
                .accept(MediaType.APPLICATION_JSON_VALUE);
        this.mvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().json(listAsJsonString(historyList)))
                .andDo(print())
                .andReturn();
    }
}
