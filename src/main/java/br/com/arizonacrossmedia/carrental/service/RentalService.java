package br.com.arizonacrossmedia.carrental.service;

import br.com.arizonacrossmedia.carrental.model.Rental;

import java.util.List;

public interface RentalService
{
    List<Rental> listAll();
    Rental getById(Long id);
    List<Rental> getByCustomerName(String name);
    Rental saveOrUpdate(Rental rental);
    void delete(Long id);
    String sendRemainingMessage(long id);
}
