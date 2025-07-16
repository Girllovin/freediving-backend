package smallITgroup.post.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import smallITgroup.post.dto.DatePeriodDto;
import smallITgroup.post.dto.NewCommentDto;
import smallITgroup.post.dto.NewPostDto;
import smallITgroup.post.dto.PostDto;
import smallITgroup.post.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

@RestController
@RequiredArgsConstructor
@RequestMapping("/forum")
public class PostController {

	final PostService postService;

	@PostMapping("/post/{author}")
	public PostDto addNewPost(@PathVariable String author, @AuthenticationPrincipal UserDetails userDetails, @RequestBody NewPostDto newPostDto) {
		boolean isAdmin = userDetails.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.anyMatch(role -> role.equals("ROLE_ADMIN"));

		// Allow if admin, or if posting as self
		if (!isAdmin && !userDetails.getUsername().equalsIgnoreCase(author)) {
			throw new org.springframework.security.access.AccessDeniedException("You can only post as yourself.");
		}
		return postService.addNewPost(author, newPostDto);
	}

	@GetMapping("/post/{id}")
	public PostDto findPostById(@PathVariable String id) {
		return postService.findPostById(id);
	}

	@DeleteMapping("/post/{id}")
	public PostDto removePost(@PathVariable String id) {
		return postService.removePost(id);
	}

	@PutMapping("/post/{id}")
	public PostDto updatePost(@PathVariable String id, @RequestBody NewPostDto newPostDto) {
		return postService.updatePost(id, newPostDto);
	}

	@PutMapping("/post/{id}/comment/{author}")
	public PostDto addComment(@PathVariable String id, @PathVariable String author,
			@RequestBody NewCommentDto newCommentDto) {
		return postService.addComment(id, author, newCommentDto);
	}

	@PutMapping("/post/{id}/like")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void addLike(@PathVariable String id) {
		postService.addLike(id);

	}

	@GetMapping("/posts/author")
	public Iterable<PostDto> findPostByAuthor(@AuthenticationPrincipal UserDetails userDetails) {
		String author = userDetails.getUsername();
		return postService.findPostByAuthor(author);
	}

	@PostMapping("/posts/tags")
	public Iterable<PostDto> findPostsByTags(@RequestBody List<String> tags) {
		return postService.findPostsByTags(tags);
	}

	@PostMapping("/posts/period")
	public Iterable<PostDto> findPostsByPeriod(@RequestBody DatePeriodDto datePeriodDto) {
		return postService.findPostsByPeriod(datePeriodDto);
	}

}
