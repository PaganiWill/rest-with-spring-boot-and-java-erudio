package br.com.erudio.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.erudio.controllers.BookController;
import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.exceptions.RequiredObjectsIsNullException;
import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.mapper.DozerMapper;
import br.com.erudio.model.Book;
import br.com.erudio.repositories.BookRepository;

@Service
public class BookServices {

	private Logger logger = Logger.getLogger(BookServices.class.getName()); 

	@Autowired
	BookRepository bookRepository;
	
	public List<BookVO> findAll(){
		logger.info("Buscando varias pessoas!");
		
		var books = DozerMapper.parseListObjects(bookRepository.findAll(), BookVO.class);
		books.stream().forEach(p -> p.add(linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel()));
		return books;
	}

	public BookVO findByKey(Long key){
		logger.info("Buscando uma pessoa!");
		
		var entity = bookRepository.findById(key).orElseThrow(() -> 
			new ResourceNotFoundException("Nenhum registro encontrado para este key!"));
		
		var vo =  DozerMapper.parseObject(entity, BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(key)).withSelfRel());
		
		return vo;
	}

	public BookVO create(BookVO book) {
		
		if(book == null) throw new RequiredObjectsIsNullException();
		logger.info("Criando uma pessoa!");
		
		var entity = DozerMapper.parseObject(book, Book.class);
		
		var vo = DozerMapper.parseObject(bookRepository.save(entity), BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
		
		return vo;
	}
	
	public BookVO update(BookVO book) {
		if(book == null) throw new RequiredObjectsIsNullException();
		logger.info("Atualizando uma pessoa!");
		
		var entity = bookRepository.findById(book.getKey()).orElseThrow(() -> 
			new ResourceNotFoundException("Nenhum registro encontrado para este key!"));
		
		entity.setAuthor(book.getAuthor());
		entity.setLaunchDate(book.getLaunchDate());
		entity.setPrice(book.getPrice());
		entity.setTitle(book.getTitle());
		
		var vo = DozerMapper.parseObject(bookRepository.save(entity), BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
		
		return vo;
	}
	
	public void delete(Long key) {
		logger.info("Deletando pessoa!");
		
		var entity = bookRepository.findById(key).orElseThrow(() -> new ResourceNotFoundException("Nenhum registro encontrado para este key!"));
		
		bookRepository.delete(entity);
	}
}
