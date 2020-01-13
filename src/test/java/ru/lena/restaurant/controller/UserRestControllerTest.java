package ru.lena.restaurant.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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
import ru.lena.restaurant.model.Restaurant;
import ru.lena.restaurant.model.Role;
import ru.lena.restaurant.model.User;
import ru.lena.restaurant.service.UserService;
import ru.lena.restaurant.service.UserServiceImpl;
import ru.lena.restaurant.to.RestaurantTo;
import ru.lena.restaurant.to.UserTo;
import ru.lena.restaurant.utils.EntityUtil;
import ru.lena.restaurant.utils.JsonUtil;

import java.util.List;

import static org.mockito.Mockito.verify;
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
public class UserRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService service;


    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void getAll() throws Exception {
        List<UserTo> restList = EntityUtil.asToListUsers(service.getAll());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/rest/admin/users")
                .accept(MediaType.APPLICATION_JSON_VALUE);
        this.mvc.perform(requestBuilder).andExpect(status().isOk())
//                .andExpect(content().json(listAsJsonString(restList)))
                .andDo(print())
                .andReturn();
    }

    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void delete() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/rest/admin/users/{id}", 1L)
                .with(csrf());
        this.mvc.perform(requestBuilder).andExpect(status().isNoContent()).andReturn();
    }

    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void get() throws Exception {
        UserTo userTo = new UserTo(1L, "User", "user@yandex.ru", "password");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/rest/admin/users/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        this.mvc.perform(requestBuilder).andExpect(status().isOk())
//                .andExpect(content().json(asJsonString(userTo)))
                .andDo(print())
                .andReturn();
    }



    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void createWithLocation() throws Exception {
        User user = new User();
        user.setName("New User Name");
        user.setEmail("newuser@mail.ru");
        user.setPassword("newPassword");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/rest/admin/users")
                .with(csrf()).contentType(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeAdditionProps(user, "password", user.getPassword()));
        this.mvc.perform(requestBuilder).andExpect(status().isCreated());
    }

    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void update() throws Exception {
        User user = service.get(1L);
        user.setName("Vova");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/rest/admin/restaurants")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(user));
        this.mvc.perform(requestBuilder).andExpect(status().isNoContent())
                .andDo(print());
    }

    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void getByMail() throws Exception {
        UserTo userTo = EntityUtil.asTo(service.get(1L));
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/rest/admin/users")
                .param("email", "user@mail.ru")
                .with(csrf()).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(userTo));
        this.mvc.perform(requestBuilder).andExpect(status().isOk())
                .andDo(print());
    }

    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void enable() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/rest/admin/users/{id}", 1L)
                .param("enabled", "true")
                .with(csrf()).contentType(MediaType.APPLICATION_JSON_VALUE);
        this.mvc.perform(requestBuilder).andExpect(status().isNoContent());

    }
}
