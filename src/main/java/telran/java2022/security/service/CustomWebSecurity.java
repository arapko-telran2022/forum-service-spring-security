package telran.java2022.security.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java2022.accounting.dao.UserAccountRepository;
import telran.java2022.post.dao.PostRepository;
import telran.java2022.post.model.Post;

@Service
@RequiredArgsConstructor
public class CustomWebSecurity {
	private final PostRepository postRepository;

	public Boolean checkPostAuthor(String id, String userName) {
		Post post = postRepository.findById(id).get();
		return post != null && userName.equals(post.getAuthor());
	}
}
