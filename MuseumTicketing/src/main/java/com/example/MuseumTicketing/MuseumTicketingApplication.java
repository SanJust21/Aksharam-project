package com.example.MuseumTicketing;


import com.example.MuseumTicketing.Model.Role;
import com.example.MuseumTicketing.Model.Users;
import com.example.MuseumTicketing.Repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class MuseumTicketingApplication implements CommandLineRunner {

	@Autowired
	private UsersRepo usersRepo;
	public static void main(String[] args) {
		SpringApplication.run(MuseumTicketingApplication.class, args);
	}


	public void run(String... args) {

		Users adminAccount = usersRepo.findByRole(Role.ADMIN);

		if(null == adminAccount){
			Users users =  new Users();

			users.setName("admin");
			users.setEmail("museumaksharam@gmail.com");
			users.setEmployeeId("admin");
			users.setImage("admin");
			users.setPhoneNo("9999999999");
			users.setTempAddress("admin");
			users.setPermAddress("admin");
			users.setRole(Role.ADMIN);
			users.setPassword(new BCryptPasswordEncoder().encode("password"));
			usersRepo.save(users);
		}

		Users guestAccount = usersRepo.findByRole(Role.GUEST);

		if (guestAccount==null){
			Users users = new Users();
			users.setName("guest");
			users.setEmail("guest@gmail.com");
			users.setEmployeeId("guest");
			users.setImage("guest");
			users.setPhoneNo("9999999999");
			users.setTempAddress("guest");
			users.setPermAddress("guest");
			users.setRole(Role.GUEST);
			users.setPassword(new BCryptPasswordEncoder().encode("password"));
			usersRepo.save(users);
		}
	}
}
