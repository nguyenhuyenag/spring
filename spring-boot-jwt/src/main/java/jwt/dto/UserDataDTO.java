package jwt.dto;

import java.util.List;

import jwt.entity.Role;
import lombok.Data;

@Data
public class UserDataDTO {

	private String username;
	private String password;
	private String email;
	List<Role> roles;

}
