package edu.arelance.nube.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.arelance.nube.repository.entity.Restaurante;
import edu.arelance.nube.service.RestauranteService;
import io.swagger.v3.oas.annotations.Operation;

/**
 * 
 * API WEB
 * HTTP -> Deriva en la ejecución de un método
 * 
 * GET - Consultar todos
 * GET - Consultar Uno (por ID)
 * 
 * POST - Insertar un Restaurante nuevo
 * 
 * PUT -> Modificar un Restaurante que ya existe
 * 
 * DELETE -> Borrar un restaurante (por ID)
 * 
 * GET Búsqueda -> Por barrio, por especialidad, por nombre, ect...
 */

//ESTA CAPA SÓLO SE DEDICA A RECIBIR(HTML) Y CONTESTAR JSON
//@Controller Devuelve una vista: html/jsp
@RestController //Devuelve JSON http://localhost:8081/restaurante
@RequestMapping("/restaurante")
public class RestauranteController {
	
	@Autowired
	RestauranteService restauranteService;
	
	Logger logger = LoggerFactory.getLogger(RestauranteController.class);
	

	@GetMapping("/test") // GET http://localhost:8081/restaurante/test
	public Restaurante obtenerRestauranteTest() {
		
		Restaurante restaurante = null;
		
		System.out.println("Llamando a restaurante-test");
		logger.debug("estoy en obtenerRestauranteTest");
		restaurante = new Restaurante(1L, "Martinete", "Carlos Haya, 33", "Carranque", "www.martinete.org", "asdafgetrghtrg",33.65f,-2.3f,10,"gazpachuelo","paella","sopa de marisco",LocalDateTime.now());
		
		return restaurante;
	}
	
	//GET - Consultar TODOS http://localhost:8081/restaurante
	@GetMapping
	public ResponseEntity<?> listarTodos(){
		ResponseEntity<?> responseEntity = null;
		Iterable<Restaurante> lista_Restaurante = null;
		
		lista_Restaurante = this.restauranteService.consultarTodos();
		responseEntity = ResponseEntity.ok(lista_Restaurante);
		
		return responseEntity;
	}
	//GET - Consultar Uno (por ID) http://localhost:8081/restaurante/id
	@GetMapping("/{id}")
	@Operation(description = "Este servicio permite consultar un restaurante por id")
	public ResponseEntity<?> listarPorId(@PathVariable Long id){
		ResponseEntity<?> responseEntity = null;
		Optional<Restaurante> or = null;
		logger.debug("En listarPorId " + id);
		
		or =this.restauranteService.consultarRestaurante(id);
		
		if(or.isPresent())
		{//La consulta ha recuperado un registro
			
			Restaurante restauranteLeido = or.get();
			responseEntity = ResponseEntity.ok(restauranteLeido);
			logger.debug("Recuperado el registro " + restauranteLeido.toString());
			
		}else
		{
		//La consulta NO ha recuperado un registro
			responseEntity =  ResponseEntity.noContent().build();
			logger.debug("El restaurante con el id: " + id + " no existe!!!");
		}
		logger.debug("Saliendo de listarPorId");
		
		return responseEntity;
	}
	
	//GET - Consultar la lista de Restaurantes entre un precio minimo y precio máximo
	@GetMapping("/buscarPorPrecio") // http://localhost:8081/restaurante/buscarPorPrecio?preciomin=10&preciomax=20
	public ResponseEntity<?> listarPrecioMinMax(
			@RequestParam(name = "preciomin") int preciomin,
			@RequestParam(name = "preciomax") int preciomax)
	{
		ResponseEntity<?> responseEntity = null;
		Iterable<Restaurante> itRest = null;
		
		itRest = this.restauranteService.consultarPrecioMinMax(preciomin, preciomax);
		
		responseEntity = ResponseEntity.ok(itRest);
		
		
		return responseEntity;
	}
	
	//GET - Listar por especialidades dadas tres especialidades
	@GetMapping("/buscarPorEspecialidades")
	// http://localhost:8081/restaurante/buscarPorEspecialidades?especialidad1=italiana&especialidad2=pescados&especialidad3=asados
	public ResponseEntity<?> listarPorEspecialidades(
								@RequestParam(name = "especialidad1") String especialidad1, 
								@RequestParam(name = "especialidad2") String especialidad2, 
								@RequestParam(name = "especialidad3") String especialidad3)
	{
		ResponseEntity<?> responseEntity = null;
		Iterable<Restaurante> listaEspecialidades = null;
		
		listaEspecialidades = this.restauranteService.listarPorEspecialidad(especialidad1, especialidad2, especialidad3);
		
		responseEntity = ResponseEntity.ok(listaEspecialidades);
		
		return responseEntity;
	}
	
	//GET - Listar por barrio o barrio o especialidades segun clave
	// http://localhost:8081/restaurante/buscarPorNombreBarrioEspecialidades?clave=p
	@GetMapping("/buscarPorNombreBarrioEspecialidades")
	public ResponseEntity<?> listarPorNombreBarrioEspecialidades(
									@RequestParam(name = "clave") String clave){
		ResponseEntity<?> responseEntity = null;
		Iterable<Restaurante> listaNombreBarrioEspecialidades = null;
		
		listaNombreBarrioEspecialidades = this.restauranteService.buscarPorBarrioNombreOEspecialidad(clave);
		
		responseEntity = ResponseEntity.ok(listaNombreBarrioEspecialidades);
		
		return responseEntity;
	}
	
	private ResponseEntity<?> generarRespuestaDeErroresValidacion (BindingResult bindingResult){
		ResponseEntity<?> responseEntity = null;
		List<ObjectError> listaDeErrores = null;
		
			listaDeErrores = bindingResult.getAllErrors();
			//Imprimir los errores por el log
			listaDeErrores.forEach(e -> logger.error(e.toString()));
			
			responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(listaDeErrores);
		
		return responseEntity;
	}
		
	//POST - Insertar un Restaurante nuevo http://localhost:8081/restaurante( Body Restaurante )
	@PostMapping
	public ResponseEntity<?> insertarRestaurante(@Valid @RequestBody Restaurante restaurante, BindingResult bindingResult)
	{
		ResponseEntity<?> responseEntity = null;
		Restaurante restauranteNuevo = null;
		
		if(bindingResult.hasErrors()) {
			logger.debug("Error en la entrada a POST");
			responseEntity = generarRespuestaDeErroresValidacion(bindingResult);
		}else {
			logger.debug("Sin error en la entrada POST");
			restauranteNuevo = this.restauranteService.altaRestaurante(restaurante);
			responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(restauranteNuevo);
		}
		
		return responseEntity;
	}
	
	
	//PUT -> Modificar un Restaurante que ya existe http://localhost:8081/restaurante/id( Body Restaurante )
	@PutMapping
	public ResponseEntity<?> modificarRestaurante(
						@Valid @RequestBody Restaurante restaurante,
						@PathVariable Long id, BindingResult bindingResult)
	{
		ResponseEntity<?> responseEntity = null;
		Optional<Restaurante> orRest = null;
		
		orRest = this.restauranteService.modificarRestaurante(id, restaurante);
		
		if(bindingResult.hasErrors()) {
			logger.debug("Error en la entrada a PUT");
			responseEntity = generarRespuestaDeErroresValidacion(bindingResult);
		}else {
			logger.debug("Sin error en la entrada PUT");
			if(orRest.isPresent()) {
				Restaurante rm = orRest.get();
				responseEntity = ResponseEntity.ok(rm);
			}else {
				responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		}
		
		
		
		return responseEntity;
	}
	
	//DELETE -> Borrar un restaurante (por ID) http://localhost:8081/restaurante/id
	@DeleteMapping("/{id}")
	public ResponseEntity<?> borrarPorId(@PathVariable Long id){
		ResponseEntity<?> responseEntity = null;
		
		this.restauranteService.borrarRestaurante(id);
		responseEntity = ResponseEntity.ok().build();
		
		
		return responseEntity;
	}
}
