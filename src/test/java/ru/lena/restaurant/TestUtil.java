package ru.lena.restaurant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import ru.lena.restaurant.model.AuthorizedUser;
import ru.lena.restaurant.model.User;
import ru.lena.restaurant.to.BaseTo;
import ru.lena.restaurant.to.UserTo;
import ru.lena.restaurant.to.VoteHistoryTo;
import ru.lena.restaurant.utils.JsonUtil;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class TestUtil {

    public static String getContent(MvcResult result) throws UnsupportedEncodingException {
        return result.getResponse().getContentAsString();
    }

    public static <T> T readFromJson(ResultActions action, Class<T> clazz) throws UnsupportedEncodingException {
        return readFromJsonMvcResult(action.andReturn(), clazz);
    }

    public static <T> T readFromJsonMvcResult(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValue(getContent(result), clazz);
    }

    public static <T> List<T> readListFromJsonMvcResult(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValues(getContent(result), clazz);
    }

    public static void mockAuthorize(User user) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new AuthorizedUser(user), null, user.getRoles()));
    }

    public static String asJsonString(final Object obj) {
        Gson gson =  new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        return gson.toJson(obj);
    }

    public static String listAsJsonString(final Collection obj) {
        Gson gson =  new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        return gson.toJson(obj);
    }
}
