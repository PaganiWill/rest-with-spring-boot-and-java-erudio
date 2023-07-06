package br.com.erudio.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.services.BookServices;
import br.com.erudio.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/book")
@Tag(name = "Livros", description = "Endpoints para gerenciamento de livros")
public class BookController {
    
	@Autowired
	private BookServices service;
	
    @GetMapping(produces = {MediaType.APPLICATION_JSON, 
    		MediaType.APPLICATION_XML,
    		MediaType.APPLICATION_YML})
    @Operation(summary = "Busca todas os livros", description = "Busca todos os livros",
    	tags = {"Livros"},
    	responses = {
    			@ApiResponse(description = "Success", responseCode = "200", 
    					content = {
    							@Content(
    									mediaType = "application/json",
    									array = @ArraySchema(schema = @Schema(implementation = BookVO.class))
    									)
    					}),
    			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
    			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
    			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
    			@ApiResponse(description = "Internal", responseCode = "500", content = @Content)
    	})
	public List<BookVO> findAll() {
        return service.findAll();
    }
    
    @GetMapping(value="/{id}", produces = {MediaType.APPLICATION_JSON, 
    		MediaType.APPLICATION_XML,
    		MediaType.APPLICATION_YML})
    
    @Operation(summary = "Busca uma pessoa", description = "Busca uma pessoa",
		tags = {"Livros"},
		responses = {
			@ApiResponse(description = "Success", responseCode = "200", 
					content = @Content(schema = @Schema(implementation = BookVO.class))),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal", responseCode = "500", content = @Content)
	})
    public BookVO findById(@PathVariable("id") Long id){
    	return service.findByKey(id);
    }       
    
    @PostMapping(consumes = {MediaType.APPLICATION_JSON, 
    		MediaType.APPLICATION_XML,
    		MediaType.APPLICATION_YML},
    		produces = {MediaType.APPLICATION_JSON, 
    				MediaType.APPLICATION_XML,
    				MediaType.APPLICATION_YML})
    @Operation(summary = "Adiciona uma pessoa", description = "Adiciona uma pessoa",
		tags = {"Livros"},
		responses = {
			@ApiResponse(description = "Success", responseCode = "200", 
					content = @Content(schema = @Schema(implementation = BookVO.class))),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Internal", responseCode = "500", content = @Content)
	})
	public BookVO create(@RequestBody BookVO book){
    	return service.create(book);
    } 
    
    @PutMapping(consumes = {MediaType.APPLICATION_JSON, 
    		MediaType.APPLICATION_XML, 
    		MediaType.APPLICATION_YML},
			produces = {MediaType.APPLICATION_JSON, 
					MediaType.APPLICATION_XML,
					MediaType.APPLICATION_YML})
    @Operation(summary = "Atualiza uma pessoa", description = "Atualiza uma pessoa",
		tags = {"Livros"},
		responses = {
			@ApiResponse(description = "Success", responseCode = "200", 
					content = @Content(schema = @Schema(implementation = BookVO.class))),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal", responseCode = "500", content = @Content)
	})
    public BookVO update(@RequestBody BookVO book){
    	return service.update(book);
    }    
    
    @DeleteMapping(value="/{id}")
    @Operation(summary = "Deleta uma pessoa", description = "Deleta uma pessoa",
		tags = {"Livros"},
		responses = {
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal", responseCode = "500", content = @Content)
	})
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
    	service.delete(id);
    	return ResponseEntity.noContent().build();
    }   
}