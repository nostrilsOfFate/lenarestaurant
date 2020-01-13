package ru.lena.restaurant.controller;

import org.junit.jupiter.api.BeforeEach;
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
import ru.lena.restaurant.model.Dish;
import ru.lena.restaurant.service.DishService;
import ru.lena.restaurant.service.UserServiceImpl;
import ru.lena.restaurant.to.DishTo;
import ru.lena.restaurant.utils.EntityUtil;

import java.math.BigDecimal;
import java.util.List;

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
public class DishRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private DishService service;

    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void getAll() throws Exception {
        List<DishTo> dishList = EntityUtil.asToListDishes(service.getAll());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/rest/admin/dishes")
                .accept(MediaType.APPLICATION_JSON_VALUE);
        this.mvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().json(listAsJsonString(dishList)))
                .andDo(print())
                .andReturn();
    }

    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void delete() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/rest/admin/dishes/{id}", 1L)
                .with(csrf());
        this.mvc.perform(requestBuilder).andExpect(status().isNoContent()).andReturn();
    }

    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void get() throws Exception {
        DishTo dishTo = EntityUtil.asTo(service.get(1L));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/rest/admin/dishes/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        this.mvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().json(asJsonString(dishTo)))
                .andDo(print())
                .andReturn();
    }

    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void isTodayMenuDish() throws Exception {
        //    @ResponseStatus(value = HttpStatus.NO_CONTENT)
        //    public void isTodayMenuDish(@NonNull @PathVariable("dishId") Long dishId, @RequestParam boolean isTodayDish) {
        //        log.info("set today menu dish with id {}", dishId);
        //        service.setTodayMenu(dishId, isTodayDish);

    }

    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void update() throws Exception {
        DishTo dishTo = EntityUtil.asTo(service.get(1L));
        dishTo.setName("Шашлык");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/rest/admin/dishes/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(dishTo));
        this.mvc.perform(requestBuilder).andExpect(status().isNoContent())
                .andDo(print());
    }

    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void createWithLocation() throws Exception {
        DishTo dishTo = EntityUtil.asTo(new Dish("голубцы", BigDecimal.valueOf(210), true));
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/rest/admin/dishes")
                .with(csrf()).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(dishTo));
        this.mvc.perform(requestBuilder).andExpect(status().is2xxSuccessful())
                .andExpect(content().json(asJsonString(dishTo))).andReturn();
    }
}
