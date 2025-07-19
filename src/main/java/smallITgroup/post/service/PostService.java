package smallITgroup.post.service;

import java.util.List;

import smallITgroup.post.dto.DatePeriodDto;
import smallITgroup.post.dto.NewCommentDto;
import smallITgroup.post.dto.NewPostDto;
import smallITgroup.post.dto.PostDto;

public interface PostService {
	PostDto addNewPost(String author, NewPostDto newPostDto);

	PostDto findPostById(String id);

	PostDto removePost(String id);

	PostDto updatePost(String id, NewPostDto newPostDto);

	PostDto addComment(String id, String author, NewCommentDto newCommentDto);

	void addLike(String id);

	Iterable<PostDto> findPostByAuthor(String author);

	Iterable<PostDto> findPostsByTags(List<String> tags);

	Iterable<PostDto> findPostsByPeriod(DatePeriodDto datePeriodDto);

}
