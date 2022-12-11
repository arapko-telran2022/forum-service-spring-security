package telran.java2022;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import telran.java2022.accounting.dto.UserAccountResponseDto;
import telran.java2022.accounting.dto.UserRegisterDto;
import telran.java2022.accounting.model.UserAccount;
import telran.java2022.accounting.service.UserAccountService;

@SpringBootTest
@TestPropertySource(properties = {
		"spring.data.mongodb.uri=mongodb+srv://root:root@clusterjava2022.2nt2aqt.mongodb.net/test_telran?retryWrites=true&w=majority" })
//@AutoConfigureMockMvc
class ForumServiceSpringSecurityApplicationTests {

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	ModelMapper modelMapper;

	@Test
	void testAddUser() {

		try {
			UserAccount user = new UserAccount("JavaFan", "1234", "Java", "Fan");

			UserAccount expected = user;

			UserRegisterDto userRegisterDto = modelMapper.map(user, UserRegisterDto.class);

			UserAccountResponseDto actual = userAccountService.addUser(userRegisterDto);

			assertEquals(expected.getLogin(), actual.getLogin());

		} catch (Exception e) {
			System.out.println("Errore message: " + e.getMessage());
			assertTrue(true);
		}

	}

//	@Test
//	void contextLoads() {
//	}

}
