package telran.java2022.security.service;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java2022.accounting.dao.UserAccountRepository;
import telran.java2022.accounting.model.UserAccount;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	
	final UserAccountRepository userAccountRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserAccount userAccount = userAccountRepository.findById(username).orElseThrow(()-> new UsernameNotFoundException(username));
		
	
//		Boolean enabled = true; 
//		Boolean accountNonExpired = true;
//		Boolean credentialsNonExpired = userAccount.getExpirePassworddDate().isAfter(LocalDate.now()); 
//		Boolean accountNonLocked = true;
		
		boolean passwordNonExpired = userAccount.getExpirePassworddDate().isAfter(LocalDate.now());		
		Set<String> rolesSet = userAccount.getRoles();
		
		if(!passwordNonExpired) {
			rolesSet.add("CHANGE");
		}
		String[] roles = rolesSet
				.stream()
				.map(r->"ROLE_"+r.toUpperCase())
				.toArray(String[]:: new);
//		return new User(username, userAccount.getPassword(), AuthorityUtils.createAuthorityList(roles));
//		return new User(username, userAccount.getPassword(), 
//				enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, AuthorityUtils.createAuthorityList(roles));		
		return new UserProfile(username, userAccount.getPassword(), 
				AuthorityUtils.createAuthorityList(roles), passwordNonExpired);
	}

}
