package br.com.arizonacrossmedia.carrental.endpoint;

import br.com.arizonacrossmedia.carrental.model.Rental;
import br.com.arizonacrossmedia.carrental.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("rental")
public class RentalEndpoint {
    private final RentalRepository rentalRepository;

    @Autowired
    public RentalEndpoint(RentalRepository rentalRepository)
    {
        this.rentalRepository = rentalRepository;
    }

    @GetMapping
    public ResponseEntity<?> listAll()
    {
        return new ResponseEntity<>(rentalRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getRentalByCustomer(@PathVariable("id") Long id)
    {
        Optional<Rental> rental = rentalRepository.findById(id);
        if(!rental.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found");
        }
        return new ResponseEntity<>(rental.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Rental rental)
    {
        if(rental.getStart().compareTo(rental.getEnd()) > 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rental start date must be before end date");
        }
        if(rentalRepository.findByCustomer(rental.getCustomer()).size() > 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer already has a rental");
        }
        return new ResponseEntity<>(rentalRepository.save(rental), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id)
    {
        rentalRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Rental rental)
    {
        rentalRepository.save(rental);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
