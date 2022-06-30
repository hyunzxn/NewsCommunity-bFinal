package com.teamharmony.newscommunity;

import com.teamharmony.newscommunity.users.entity.Role;
import com.teamharmony.newscommunity.users.entity.RoleType;
import com.teamharmony.newscommunity.users.entity.User;
import com.teamharmony.newscommunity.users.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class NewsCommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsCommunityApplication.class, args);
    }
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// add a test user
	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
			userService.saveRole(new Role(RoleType.USER));
			userService.saveRole(new Role(RoleType.ADMIN));
			userService.saveUser(new User(null,"john","1234","",new ArrayList<>()));
			userService.addRoleToUser("john",RoleType.USER);
			userService.addRoleToUser("john",RoleType.ADMIN);
		};
	}
}
