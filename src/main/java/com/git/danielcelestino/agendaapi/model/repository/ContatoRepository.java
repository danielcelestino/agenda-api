package com.git.danielcelestino.agendaapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.git.danielcelestino.agendaapi.model.entity.Contato;

public interface ContatoRepository extends JpaRepository<Contato, Integer>{

}
