package smallITgroup.security;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import smallITgroup.accounting.dao.UserAccountRepository;


@Service("customSecurity")
@RequiredArgsConstructor
public class CustomWebSecurity {
	final UserAccountRepository userAccountRepository;

	public boolean checkPostAuthor(String postId, String userName) {
//		UserAccount post;
//		try {
//			post = UserAccountRepository.findById(postId).orElse(null);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return post != null && userName.equalsIgnoreCase(post.getAuthor());
		return false;
	}
}
