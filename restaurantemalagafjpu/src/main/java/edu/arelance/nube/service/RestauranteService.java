package edu.arelance.nube.service;

import java.util.Optional;

import edu.arelance.nube.repository.entity.Restaurante;

public interface RestauranteService {
	
	Iterable<Restaurante> consultarTodos();
	
	//Utilizamos Optional me devuelve un dato o no, siempore devuelve algo y evita
	//El NullPointerException
	Optional<Restaurante> consultarRestaurante (Long id);
	
	Restaurante altaRestaurante(Restaurante restaurante);
	
	void borrarRestaurante(Long id);
	
	Optional<Restaurante> modificarRestaurante(Long id, Restaurante restaurante);
	
	Iterable<Restaurante> consultarPrecioMinMax(int preciomin, int preciomax);
	
	Iterable<Restaurante> listarPorEspecialidad(String especialidad1, String especialidad2, String especialidad3);
	
	Iterable<Restaurante> buscarPorBarrioNombreOEspecialidad(String clave);

}
