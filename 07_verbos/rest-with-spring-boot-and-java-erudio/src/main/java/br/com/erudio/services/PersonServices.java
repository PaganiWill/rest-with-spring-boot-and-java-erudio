package br.com.erudio.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import br.com.erudio.controllers.PersonController;
import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.data.vo.v2.PersonVOV2;
import br.com.erudio.exceptions.RequiredObjectsIsNullException;
import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.mapper.DozerMapper;
import br.com.erudio.mapper.custom.PersonMapper;
import br.com.erudio.model.Person;
import br.com.erudio.repositories.PersonRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonServices {

	private Logger logger = Logger.getLogger(PersonServices.class.getName()); 

	@Autowired
	PersonRepository personRepository;
	
	@Autowired
	PersonMapper mapper;

	@Autowired
	PagedResourcesAssembler<PersonVO> assembler;

	public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) {

		logger.info("Finding all people!");

		var personPage = personRepository.findAll(pageable);

		var personVosPage = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class));
		personVosPage.map(
				p -> p.add(
						linkTo(methodOn(PersonController.class)
								.findById(p.getKey())).withSelfRel()));

		Link link = linkTo(
				methodOn(PersonController.class)
						.findAll(pageable.getPageNumber(),
								pageable.getPageSize(),
								"asc")).withSelfRel();

		return assembler.toModel(personVosPage, link);
	}

	public PagedModel<EntityModel<PersonVO>> findPersonByName(String firstName, Pageable pageable) {

		logger.info("Finding all people!");

		var personPage = personRepository.findPersonByName(firstName, pageable);

		var personVosPage = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class));
		personVosPage.map(
				p -> p.add(
						linkTo(methodOn(PersonController.class)
								.findById(p.getKey())).withSelfRel()));

		Link link = linkTo(
				methodOn(PersonController.class)
						.findAll(pageable.getPageNumber(),
								pageable.getPageSize(),
								"asc")).withSelfRel();

		return assembler.toModel(personVosPage, link);
	}

	public PersonVO findByKey(Long key){
		logger.info("Buscando uma pessoa!");
		
		var entity = personRepository.findById(key).orElseThrow(() -> 
			new ResourceNotFoundException("Nenhum registro encontrado para este key!"));
		
		var vo =  DozerMapper.parseObject(entity, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(key)).withSelfRel());
		
		return vo;
	}

	public PersonVO create(PersonVO person) {
		
		if(person == null) throw new RequiredObjectsIsNullException();
		logger.info("Criando uma pessoa!");
		
		var entity = DozerMapper.parseObject(person, Person.class);
		
		var vo = DozerMapper.parseObject(personRepository.save(entity), PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		
		return vo;
	}
	
	public PersonVOV2 createV2(PersonVOV2 person) {
		logger.info("Criando uma pessoa com V2!");
		
		var entity = mapper.convertVoToEntity(person);
		
		var vo = mapper.convertEntityToVo(personRepository.save(entity));
		
		return vo;
	}
	
	public PersonVO update(PersonVO person) {
		if(person == null) throw new RequiredObjectsIsNullException();
		logger.info("Atualizando uma pessoa!");
		
		var entity = personRepository.findById(person.getKey()).orElseThrow(() -> 
			new ResourceNotFoundException("Nenhum registro encontrado para este key!"));
		
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		
		var vo = DozerMapper.parseObject(personRepository.save(entity), PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		
		return vo;
	}

	@Transactional
	public PersonVO disablePerson(Long key){
		logger.info("Desabilitando uma pessoa!");

		personRepository.disablePerson(key);

		var entity = personRepository.findById(key).orElseThrow(() ->
				new ResourceNotFoundException("Nenhum registro encontrado para este key!"));

		var vo =  DozerMapper.parseObject(entity, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(key)).withSelfRel());

		return vo;
	}

	public void delete(Long key) {
		logger.info("Deletando pessoa!");
		
		var entity = personRepository.findById(key).orElseThrow(() -> new ResourceNotFoundException("Nenhum registro encontrado para este key!"));
		
		personRepository.delete(entity);
	}
}
