package telran.java2022.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import telran.java2022.post.dao.PostRepository;
import telran.java2022.post.model.Post;

@Component
@RequiredArgsConstructor
public class OwnerChecker {
	
    private final PostRepository postRepository;

    public Boolean checkPostOwner(Authentication authentication, String id) {
		Post post = postRepository.findById(id).get();
		if (post == null) {
			return false;
		}
		return authentication.getName().equals(post.getAuthor());
	}

	public Boolean checkUserOwner(Authentication authentication, String user) {
	    return authentication.getName().equals(user);
	}
}
