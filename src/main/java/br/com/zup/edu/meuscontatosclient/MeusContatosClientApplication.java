package br.com.zup.edu.meuscontatosclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MeusContatosClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeusContatosClientApplication.class, args);
	}

}
