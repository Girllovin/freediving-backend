package smallITgroup.post.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;

import smallITgroup.post.model.Post;

public interface PostRepository extends MongoRepository<Post, String> {
	Stream<Post> findByAuthorIgnoreCase(String author);

	Stream<Post> findByTagsInIgnoreCase(List<String> tags);

	Stream<Post> findByDateCreatedBetween(LocalDate from, LocalDate to);
}
