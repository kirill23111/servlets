package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class PostRepository {
  private final ConcurrentMap<Long, Post> storage = new ConcurrentHashMap<>();
  private final AtomicLong nextId = new AtomicLong(0);

  public List<Post> all() {
    return new ArrayList<>(storage.values());
  }

  public Optional<Post> getById(long id) {
    return Optional.ofNullable(storage.get(id));
  }

  public Post save(Post post) {
    if (post.getId() == 0) {
      long id = nextId.incrementAndGet();
      Post created = new Post(id, post.getContent());
      storage.put(id, created);
      return created;
    }

    // update
    long id = post.getId();
    Post updated = new Post(id, post.getContent());
    Post existing = storage.replace(id, updated);
    if (existing == null) {
      throw new NotFoundException("Post with id=" + id + " not found");
    }
    return updated;
  }

  public void removeById(long id) {
    Post removed = storage.remove(id);
    if (removed == null) {
      throw new NotFoundException("Post with id=" + id + " not found");
    }
  }
}

