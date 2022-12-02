package com.git.danielcelestino.agendaapi.model.api.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.git.danielcelestino.agendaapi.model.entity.Contato;
import com.git.danielcelestino.agendaapi.model.repository.ContatoRepository;

@RestController
@RequestMapping("/api/contatos")
@CrossOrigin("*")
public class ContatoController {

	@Autowired
	private ContatoRepository contatoRepository;

	@GetMapping
	public List<Contato> findAll() {
		return contatoRepository.findAll();
	}

	@GetMapping("/{id}")
	public Contato findById(@PathVariable Integer id) {
		return contatoRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contato não encontrado!"));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Contato salvar(@RequestBody @Validated Contato contato) {
		return contatoRepository.save(contato);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Integer id) {
		contatoRepository.findById(id).map(contato -> {
			contatoRepository.delete(contato);
			return Void.TYPE;
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contato não encontrado!"));
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizar(@PathVariable Integer id, @RequestBody @Validated Contato contatoAtualizado) {
		contatoRepository.findById(id).map(contato -> {
			contato.setNome(contatoAtualizado.getNome());
			contato.setEmail(contatoAtualizado.getEmail());
			contato.setFavorito(contatoAtualizado.getFavorito());
			return contatoRepository.save(contato);
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}
	
	@PatchMapping("/{id}/favorito")
	public void favorite(@PathVariable Integer id) {
		Optional<Contato> contato = contatoRepository.findById(id);
		contato.ifPresent(c -> {
			boolean favorito = c.getFavorito() == Boolean.TRUE;
			c.setFavorito(!favorito);
			contatoRepository.save(c);
		});
		
	}

}
