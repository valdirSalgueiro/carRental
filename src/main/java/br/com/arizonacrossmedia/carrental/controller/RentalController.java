package br.com.arizonacrossmedia.carrental.controller;

import br.com.arizonacrossmedia.carrental.model.Rental;
import br.com.arizonacrossmedia.carrental.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("rental")
public class RentalController {
    private final RentalService rentalService;

    @Autowired
    public RentalController(RentalService rentalService)
    {
        this.rentalService = rentalService;
    }

    @GetMapping
    public ResponseEntity<?> listAll()
    {
        return new ResponseEntity<>(rentalService.listAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getRentalByCustomer(@PathVariable("id") Long id)
    {
        return new ResponseEntity<>(rentalService.getById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/findByName/{name}")
    public ResponseEntity<?> findRentalByCustomerName(@PathVariable String name)
    {
        return new ResponseEntity<>(rentalService.getByCustomerName(name), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Rental rental)
    {
        return new ResponseEntity<>(rentalService.saveOrUpdate(rental), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id)
    {
        rentalService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Rental rental)
    {
        return new ResponseEntity<>(rentalService.saveOrUpdate(rental), HttpStatus.OK);
    }

    @PostMapping(path = "/calculateRemainingDays")
    public ResponseEntity<?> calculateRemainingDays(@RequestBody Long id)
    {
        return new ResponseEntity<>(rentalService.sendRemainingMessage(id),HttpStatus.OK);
    }
}
