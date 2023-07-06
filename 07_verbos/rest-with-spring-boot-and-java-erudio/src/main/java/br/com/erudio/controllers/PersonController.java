package br.com.erudio.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import br.com.erudio.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.data.vo.v2.PersonVOV2;
import br.com.erudio.services.PersonServices;

@RestController
@RequestMapping("/person")
@Tag(name = "Pessoa", description = "Endpoints para gerenciamento de pessoas")
public class PersonController {
    
	@Autowired
	private PersonServices service;
	
    @GetMapping(produces = {MediaType.APPLICATION_JSON, 
    		MediaType.APPLICATION_XML,
    		MediaType.APPLICATION_YML})
    @Operation(summary = "Busca todas as pessoas", description = "Busca todas as pessoas",
    	tags = {"Pessoa"},
    	responses = {
    			@ApiResponse(description = "Success", responseCode = "200", 
    					content = {
    							@Content(
    									mediaType = "application/json",
    									array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))
    									)
    					}),
    			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
    			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
    			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
    			@ApiResponse(description = "Internal", responseCode = "500", content = @Content)
    	})

	public ResponseEntity<PagedModel<EntityModel<PersonVO>>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "12") Integer size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction
	) {

		var sortDirection = "desc".equalsIgnoreCase(direction)
				? Sort.Direction.DESC : Sort.Direction.ASC;

		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
		return ResponseEntity.ok(service.findAll(pageable));
	}

	@GetMapping(value = "/findPersonByName/{firstName}",
			produces = {MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML})
	@Operation(summary = "Busca todas as pessoas pelo nome", description = "Busca todas as pessoas pelo nome",
			tags = {"Pessoa"},
			responses = {
					@ApiResponse(description = "Success", responseCode = "200",
							content = {
									@Content(
											mediaType = "application/json",
											array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))
									)
							}),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal", responseCode = "500", content = @Content)
			})

	public ResponseEntity<PagedModel<EntityModel<PersonVO>>> findPersonByName(
			@PathVariable(value = "firstName") String firstName,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "12") Integer size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction
	) {

		var sortDirection = "desc".equalsIgnoreCase(direction)
				? Sort.Direction.DESC : Sort.Direction.ASC;

		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
		return ResponseEntity.ok(service.findPersonByName(firstName, pageable));
	}

    @GetMapping(value="/{id}", produces = {MediaType.APPLICATION_JSON, 
    		MediaType.APPLICATION_XML,
    		MediaType.APPLICATION_YML})
    
    @Operation(summary = "Busca uma pessoa", description = "Busca uma pessoa",
		tags = {"Pessoa"},
		responses = {
			@ApiResponse(description = "Success", responseCode = "200", 
					content = @Content(schema = @Schema(implementation = PersonVO.class))),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal", responseCode = "500", content = @Content)
	})
    public PersonVO findById(@PathVariable("id") Long id){
    	return service.findByKey(id);
    }       
    
    @PostMapping(consumes = {MediaType.APPLICATION_JSON, 
    		MediaType.APPLICATION_XML,
    		MediaType.APPLICATION_YML},
    		produces = {MediaType.APPLICATION_JSON, 
    				MediaType.APPLICATION_XML,
    				MediaType.APPLICATION_YML})
    @Operation(summary = "Adiciona uma pessoa", description = "Adiciona uma pessoa",
		tags = {"Pessoa"},
		responses = {
			@ApiResponse(description = "Success", responseCode = "200", 
					content = @Content(schema = @Schema(implementation = PersonVO.class))),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Internal", responseCode = "500", content = @Content)
	})
    public PersonVO create(@RequestBody PersonVO person){
    	return service.create(person);
    } 
    
    @PostMapping(value = "/v2", consumes = {MediaType.APPLICATION_JSON, 
    		MediaType.APPLICATION_XML,
    		MediaType.APPLICATION_YML},
    		produces = {MediaType.APPLICATION_JSON, 
    				MediaType.APPLICATION_XML,
    				MediaType.APPLICATION_YML})
    public PersonVOV2 createV2(@RequestBody PersonVOV2 person){
    	return service.createV2(person);
    } 
    
    @PutMapping(consumes = {MediaType.APPLICATION_JSON, 
    		MediaType.APPLICATION_XML, 
    		MediaType.APPLICATION_YML},
			produces = {MediaType.APPLICATION_JSON, 
					MediaType.APPLICATION_XML,
					MediaType.APPLICATION_YML})
    @Operation(summary = "Atualiza uma pessoa", description = "Atualiza uma pessoa",
		tags = {"Pessoa"},
		responses = {
			@ApiResponse(description = "Success", responseCode = "200", 
					content = @Content(schema = @Schema(implementation = PersonVO.class))),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal", responseCode = "500", content = @Content)
	})
    public PersonVO update(@RequestBody PersonVO person){
    	return service.update(person);
    }

	@PatchMapping(value="/{id}",
			produces = {MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML})

	@Operation(summary = "Desabilita uma pessoa", description = "Desabilita uma pessoa",
			tags = {"Pessoa"},
			responses = {
					@ApiResponse(description = "Success", responseCode = "200",
							content = @Content(schema = @Schema(implementation = PersonVO.class))),
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal", responseCode = "500", content = @Content)
			})
	public PersonVO disablePerson(@PathVariable("id") Long id){
		return service.disablePerson(id);
	}

	@DeleteMapping(value="/{id}")
    @Operation(summary = "Deleta uma pessoa", description = "Deleta uma pessoa",
		tags = {"Pessoa"},
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