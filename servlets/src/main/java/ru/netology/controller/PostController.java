package ru.netology.controller;

import com.google.gson.Gson;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import org.springframework.stereotype.Controller;

@Controller
public class PostController {
  public static final String APPLICATION_JSON = "application/json";
  private static final Gson GSON = new Gson();

  private final PostService service;

  public PostController(PostService service) {
    this.service = service;
  }

  public void all(HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    final var data = service.all();
    response.getWriter().print(GSON.toJson(data));
  }

  public void getById(long id, HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    final var data = service.getById(id);
    response.getWriter().print(GSON.toJson(data));
  }

  public void save(Reader body, HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    final var post = GSON.fromJson(body, Post.class);
    final var data = service.save(post);
    response.getWriter().print(GSON.toJson(data));
  }

  public void removeById(long id, HttpServletResponse response) {
    service.removeById(id);
    response.setStatus(HttpServletResponse.SC_OK);
  }
}

