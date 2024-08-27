package com.rodaaron.security_service;

import com.rodaaron.security_service.persistence.entities.RoleEntity;
import com.rodaaron.security_service.persistence.entities.RoleEnum;
import com.rodaaron.security_service.persistence.entities.UserEntity;
import com.rodaaron.security_service.persistence.repository.IRoleRepository;
import com.rodaaron.security_service.persistence.repository.IUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class SecurityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner init(IUserRepository userRepository, IRoleRepository roleRepository){
		return args -> {

			RoleEntity roleAdmin = RoleEntity.builder()
					.role_name(RoleEnum.ADMIN)
					.build();

			RoleEntity roleEmployee = RoleEntity.builder()
					.role_name(RoleEnum.EMPLOYEE)
					.build();

			RoleEntity roleGuest = RoleEntity.builder()
					.role_name(RoleEnum.GUEST)
					.build();

			roleRepository.saveAll(List.of(roleAdmin,roleEmployee,roleGuest));

			///////////////////////////////////////////////

			UserEntity userAdmin = UserEntity.builder()
					.username("$admin")
					.password("1234")
					.roleEntity(roleAdmin)
					.build();


			UserEntity userEmployee = UserEntity.builder()
					.username("$employee")
					.password("1234")
					.roleEntity(roleEmployee)
					.build();


			UserEntity userGuest = UserEntity.builder()
					.username("$guest")
					.password("1234")
					.roleEntity(roleGuest)
					.build();


			userRepository.saveAll(List.of(userAdmin,userEmployee,userGuest));
		};
	}

}
