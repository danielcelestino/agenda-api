package com.git.danielcelestino.agendaapi.model.api.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.git.danielcelestino.agendaapi.model.entity.Contato;
import com.git.danielcelestino.agendaapi.model.repository.ContatoRepository;

import jakarta.servlet.http.Part;

@RestController
@RequestMapping("/api/contatos")
@CrossOrigin("*")
public class ContatoController {

	@Autowired
	private ContatoRepository contatoRepository;

	@GetMapping
	public Page<Contato> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer pagina, 
			@RequestParam(value = "size", defaultValue = "10") Integer tamanhoPagina
			) {
		Sort sort = Sort.by(Sort.Direction.ASC, "nome");
		PageRequest pageRequest = PageRequest.of(pagina, tamanhoPagina, sort);
		return contatoRepository.findAll(pageRequest);
	}

	@GetMapping("{id}")
	public Contato findById(@PathVariable Integer id) {
		return contatoRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contato não encontrado!"));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Contato salvar(@RequestBody @Validated Contato contato) {
		return contatoRepository.save(contato);
	}

	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Integer id) {
		contatoRepository.findById(id).map(contato -> {
			contatoRepository.delete(contato);
			return Void.TYPE;
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contato não encontrado!"));
	}

	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizar(@PathVariable Integer id, @RequestBody @Validated Contato contatoAtualizado) {
		contatoRepository.findById(id).map(contato -> {
			contato.setNome(contatoAtualizado.getNome());
			contato.setEmail(contatoAtualizado.getEmail());
			contato.setFavorito(contatoAtualizado.getFavorito());
			return contatoRepository.save(contato);
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}
	
	@PatchMapping("{id}/favorito")
	public void favorite(@PathVariable Integer id) {
		Optional<Contato> contato = contatoRepository.findById(id);
		contato.ifPresent(c -> {
			boolean favorito = c.getFavorito() == Boolean.TRUE;
			c.setFavorito(!favorito);
			contatoRepository.save(c);
		});
		
	}
	
	@PutMapping("{id}/foto")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public byte[] addPhoto(@PathVariable Integer id, @RequestParam("foto") Part arquivo) {
		Optional<Contato> contato = contatoRepository.findById(id);
		return contato.map( c -> {
			try {
				InputStream is = arquivo.getInputStream();
				byte[] bytes = new byte[(int)arquivo.getSize()];
				IOUtils.readFully(is, bytes);
				c.setFoto(bytes);
				contatoRepository.save(c);
				is.close();
				return bytes;
			} catch (IOException e) {
				return null;
			}
		}).orElse(null);	
		
	}

}
