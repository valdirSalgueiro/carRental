package br.com.arizonacrossmedia.carrental.service;

import br.com.arizonacrossmedia.carrental.exceptions.RentalNotFoundException;
import br.com.arizonacrossmedia.carrental.exceptions.RentalServiceException;
import br.com.arizonacrossmedia.carrental.model.Rental;
import br.com.arizonacrossmedia.carrental.repository.RentalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RentalServiceImpl implements RentalService {
    private RentalRepository rentalRepository;
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public RentalServiceImpl(RentalRepository rentalRepository, RabbitTemplate rabbitTemplate) {
        this.rentalRepository = rentalRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    private void verifyByRentalById(Long id) {
        Optional<Rental> rental = rentalRepository.findById(id);
        if (!rental.isPresent()) {
            throw new RentalNotFoundException("Rental not found");
        }
    }

    @Override
    public List<Rental> listAll() {
        List<Rental> rentals = new ArrayList<>();
        rentalRepository.findAll().forEach(rentals::add);
        return rentals;
    }

    @Override
    public Rental getById(Long id) {
        verifyByRentalById(id);
        Optional<Rental> rental = rentalRepository.findById(id);
        return rental.get();
    }

    @Override
    public List<Rental> getByCustomerName(String name) {
        List<Rental> rentals = new ArrayList<>();
        rentalRepository.findByCustomerContaining(name).forEach(rentals::add);
        return rentals;
    }

    @Override
    public Rental saveOrUpdate(Rental rental) {
        if (rental.getStart().compareTo(rental.getEnd()) > 0) {
            throw new RentalServiceException("Rental start date must be before end date");
        }
        if (rental.getId() == null && rentalRepository.findByCustomer(rental.getCustomer()).size() > 0) {
            throw new RentalServiceException("Customer already has a rental");
        }
        return rentalRepository.save(rental);
    }

    @Override
    public void delete(Long id) {
        verifyByRentalById(id);
        rentalRepository.deleteById(id);
    }

    @Override
    public String sendRemainingMessage(long id) {
        verifyByRentalById(id);
        Optional<Rental> rental = rentalRepository.findById(id);
        long diffMilliseconds = rental.get().getEnd().getTime() - new Date().getTime();
        long diff = TimeUnit.DAYS.convert(diffMilliseconds, TimeUnit.MILLISECONDS);
        return Long.toString(diff);
    }
}
