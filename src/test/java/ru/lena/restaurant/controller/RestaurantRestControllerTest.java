package ru.lena.restaurant.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.lena.restaurant.model.Restaurant;
import ru.lena.restaurant.service.RestaurantService;
import ru.lena.restaurant.to.DishTo;
import ru.lena.restaurant.to.RestaurantTo;
import ru.lena.restaurant.utils.EntityUtil;

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
public class RestaurantRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RestaurantService service;

    @WithMockUser(username = "user")
    @Test
    void getAll() throws Exception {
        List<RestaurantTo> restList = EntityUtil.asToListRest(service.getAll());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/rest/admin/restaurants")
                .accept(MediaType.APPLICATION_JSON_VALUE);
        this.mvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().json(listAsJsonString(restList)))
                .andDo(print())
                .andReturn();
    }

    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void delete() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/rest/admin/restaurants/{id}", 1L)
                .with(csrf());
        this.mvc.perform(requestBuilder).andExpect(status().isNoContent()).andReturn();
    }

    @WithMockUser(username = "user")
    @Test
    void deleteForbidden() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/rest/admin/restaurants/{id}", 1L)
                .with(csrf());
        this.mvc.perform(requestBuilder).andExpect(status().isForbidden());
    }

    @WithMockUser(username = "user")
    @Test
    void get() throws Exception {
        RestaurantTo restaurantTo = EntityUtil.asTo(service.get(1L));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/rest/admin/restaurants/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        this.mvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().json(asJsonString(restaurantTo)))
                .andDo(print())
                .andReturn();
    }

    @WithMockUser(username = "user")
    @Test
    void findAllByOrderByNameDesc() throws Exception {
        List<RestaurantTo> restList = EntityUtil.asToListRest(service.findAllSortedByName());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/rest/admin/restaurants/byname")
                .accept(MediaType.APPLICATION_JSON_VALUE);
        this.mvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().json(listAsJsonString(restList)))
                .andDo(print())
                .andReturn();
    }

    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void update() throws Exception {
        RestaurantTo restaurantTo = EntityUtil.asTo(service.get(1L));
        restaurantTo.setName("Шаурмячная");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/rest/admin/restaurants")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(restaurantTo));
        this.mvc.perform(requestBuilder).andExpect(status().isNoContent())
                .andDo(print());
    }

    @WithMockUser(username = "user")
    @Test
    void updateForbidden() throws Exception {
        RestaurantTo restaurantTo = EntityUtil.asTo(service.get(1L));
        restaurantTo.setName("Шаурмячная");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/rest/admin/restaurants")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(restaurantTo));
        this.mvc.perform(requestBuilder).andExpect(status().isForbidden())
                .andDo(print());
    }

    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void createWithLocation() throws Exception {
        RestaurantTo restaurantTo = EntityUtil.asTo(new Restaurant(null, "asdcsdfweac"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/rest/admin/restaurants")
                .with(csrf()).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(restaurantTo));
        this.mvc.perform(requestBuilder).andExpect(status().is2xxSuccessful())
                .andExpect(content().json(asJsonString(restaurantTo))).andReturn();
    }

    @WithMockUser(username = "user")
    @Test
    void createWithLocationForbidden() throws Exception {
        RestaurantTo restaurantTo = EntityUtil.asTo(new Restaurant(null, "asdcsdfweac"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/rest/admin/restaurants")
                .with(csrf()).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(restaurantTo));
        this.mvc.perform(requestBuilder).andExpect(status().isForbidden());
    }

    @WithMockUser(username = "user")
    @Test
    void getByNameRest() throws Exception {
        RestaurantTo restaurantTo = EntityUtil.asTo(service.getByName("Столовая"));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/rest/admin/restaurants/by")
                .param("name", "Столовая")
                .accept(MediaType.APPLICATION_JSON_VALUE);
        this.mvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().json(asJsonString(restaurantTo)))
                .andDo(print())
                .andReturn();
    }

    @WithMockUser(username = "user")
    @Test
    void voteForRestaurant() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/rest/admin/restaurants/{id}/vote", 1L)
                .with(csrf());
        this.mvc.perform(requestBuilder).andExpect(status().isOk());
    }
}
