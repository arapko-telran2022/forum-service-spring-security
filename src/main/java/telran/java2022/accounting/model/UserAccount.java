package telran.java2022.accounting.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(of = {"login"})
@Document(collection = "user")
@ManagedResource
public class UserAccount {
	@Id
	String login;
	@Setter
	String password;
	@Setter
	String firstName;
	@Setter
	String lastName;
	@Setter
	Set<String> roles;
	
	@Value("${password.expirePassworddDays:30}")
	Integer expirePassworddDays;
	
	@Setter
	LocalDate expirePassworddDate = LocalDate.now().plusDays(expirePassworddDays);
	
	public UserAccount() {
		roles = new HashSet<>();
		roles.add("USER");
	}
	
	public UserAccount(String login, String password, String firstName, String lastName) {
		this();
		this.login = login;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public boolean addRole(String role) {
		return roles.add(role);
	}

	public boolean removeRole(String role) {
		return roles.remove(role);
	}
	
	@ManagedAttribute
	public void setExpirePassworddDays(Integer expirePassworddDays) {
		this.expirePassworddDays = expirePassworddDays;
	}

}
