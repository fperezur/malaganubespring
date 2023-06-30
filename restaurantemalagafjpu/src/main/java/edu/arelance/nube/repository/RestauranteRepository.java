package edu.arelance.nube.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.arelance.nube.repository.entity.Restaurante;

@Repository
public interface RestauranteRepository extends CrudRepository<Restaurante, Long>{

	//1 KEY WORDS QUERYS - Consultas por palabras claves SPRING DATA JPA 
	//Apartado 5.1.3 Query Methods
	//obtener restaurantes en un rango de precios
	Iterable<Restaurante>  findByPrecioBetween(int preciomin, int preciomax);
	
	Iterable<Restaurante> findByEspecialidad1OrEspecialidad2OrEspecialidad3IgnoreCase(
			String especialidad1, String especialidad2, String especialidad3 );
	
	
	
	//2 JPQL - HQL - Pseudo SQL pero de Java - Trabajar con la BBDD con independencia del tipo de BBDD
	//3 NATIVAS - SQL
	@Query(value = "SELECT * FROM bdrestaurantes.restaurantes WHERE"
			+ "barrio LIKE %?1%"
			+ "OR nombre LIKE %?1%"
			+ "OR especialidad1 LIKE %?1%"
			+ "OR especialidad2 LIKE %?1%"
			+ "OR especialidad3 LIKE %?1%",
			nativeQuery = true)
	Iterable<Restaurante> buscarPorBarrioNombreOEspecialidad(String clave);	//4 STORED PROCEDURES - Procedimientos almacenados
	//5 CRITERIA API - SQL con métodos de Java - Ver en www.arquitecturajava.com
}
