package com.git.danielcelestino.agendaapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.git.danielcelestino.agendaapi.model.entity.Contato;
import com.git.danielcelestino.agendaapi.model.repository.ContatoRepository;

@SpringBootApplication
public class AgendaApiApplication {
	
	@Bean
	public CommandLineRunner commandLineRunner(@Autowired ContatoRepository contatoRepository) {
		return args -> {
			Contato c = new Contato();
			c.setNome("Fulano");
			c.setEmail("fulano@gmail.com");
			c.setFavorito(false);
			contatoRepository.save(c);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(AgendaApiApplication.class, args);
	}

}
