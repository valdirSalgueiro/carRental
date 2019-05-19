package br.com.arizonacrossmedia.carrental.repository;

import br.com.arizonacrossmedia.carrental.model.Rental;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RentalRepository extends CrudRepository<Rental, Long>
{
    List<Rental> findByCustomerContaining(String customer);
    List<Rental> findByCustomer(String customer);
}
