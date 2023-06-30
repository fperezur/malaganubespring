 package edu.arelance.nube.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.arelance.nube.repository.RestauranteRepository;
import edu.arelance.nube.repository.entity.Restaurante;

@Service
public class RestauranteServiceImpl implements RestauranteService{
	
	@Autowired
	RestauranteRepository restauranteRepository;

	@Override
	@Transactional(readOnly = true)
	public Iterable<Restaurante> consultarTodos() {
		return this.restauranteRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Restaurante> consultarRestaurante(Long id) {
		 return this.restauranteRepository.findById(id);
	}

	@Override
	@Transactional
	public Restaurante altaRestaurante(Restaurante restaurante) {
		return this.restauranteRepository.save(restaurante);
	}

	@Override
	@Transactional
	public void borrarRestaurante(Long id) {
		this.restauranteRepository.deleteById(id);
		
	}

	@Override
	@Transactional
	public Optional<Restaurante> modificarRestaurante(Long id, Restaurante restaurante) {
		Optional<Restaurante> opRest = Optional.empty();
		//LEER
		opRest = this.restauranteRepository.findById(id);
		
		if(opRest.isPresent()) {
			//Al estar dentro de una transacción, restauranteLeido está asociado
			//a un registro de la tabla.  Si modifico un campo, estoy modificando
			//la columna asociada(Estado "Persistent" - JPA
			Restaurante restauranteLeido = opRest.get();
			
			BeanUtils.copyProperties(restaurante, restauranteLeido, "id", "creadoEn");
			opRest = Optional.of(restauranteLeido);//relleno el Optional
		}
		//ACTUALIZAR
		
		return opRest;
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<Restaurante> consultarPrecioMinMax(int preciomin, int preciomax) {
		Iterable<Restaurante> itRest = null;
		itRest = this.restauranteRepository.findByPrecioBetween(preciomin, preciomax);
		
		return itRest;
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<Restaurante> listarPorEspecialidad(String especialidad1, String especialidad2,
			String especialidad3) {
		Iterable<Restaurante> listaPorEspecialidad = null;
		listaPorEspecialidad = this.restauranteRepository.findByEspecialidad1OrEspecialidad2OrEspecialidad3IgnoreCase(especialidad1, especialidad2, especialidad3);
		return listaPorEspecialidad;
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<Restaurante> buscarPorBarrioNombreOEspecialidad(String clave) {
		Iterable<Restaurante> listarPorClave = null;
		listarPorClave = this.restauranteRepository.buscarPorBarrioNombreOEspecialidad(clave);
		return listarPorClave;
	}

}
