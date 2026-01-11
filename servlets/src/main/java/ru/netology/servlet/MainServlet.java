package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.exception.NotFoundException;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.config.AppConfig;

public class MainServlet extends HttpServlet {
  private static final String API_POSTS = "/api/posts";
  private static final Pattern API_POSTS_ID = Pattern.compile("^/api/posts/(\\d+)$");

  private PostController controller;

  @Override
  public void init() {
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    controller = context.getBean(PostController.class);
  }


  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();

      if ("GET".equals(method) && API_POSTS.equals(path)) {
        controller.all(resp);
        return;
      }

      if ("GET".equals(method)) {
        Long id = extractId(path);
        if (id != null) {
          controller.getById(id, resp);
          return;
        }
      }

      if ("POST".equals(method) && API_POSTS.equals(path)) {
        controller.save(req.getReader(), resp);
        return;
      }

      if ("DELETE".equals(method)) {
        Long id = extractId(path);
        if (id != null) {
          controller.removeById(id, resp);
          return;
        }
      }

      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (NotFoundException e) {
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private Long extractId(String path) {
    Matcher matcher = API_POSTS_ID.matcher(path);
    if (!matcher.matches()) return null;
    return Long.parseLong(matcher.group(1));
  }
}
