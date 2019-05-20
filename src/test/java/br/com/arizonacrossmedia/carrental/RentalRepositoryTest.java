package br.com.arizonacrossmedia.carrental;

import br.com.arizonacrossmedia.carrental.model.Rental;
import br.com.arizonacrossmedia.carrental.repository.RentalRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class RentalRepositoryTest {
        @Autowired
        private RentalRepository rentalRepository;
        @Rule
        public ExpectedException thrown = ExpectedException.none();

        Date today;
        Date yesterday;
        Date fiveDaysAgo;

        @Before
        public void setUp()
        {
            today = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);

            calendar.add(Calendar.DATE, -1);
            yesterday = calendar.getTime();

            calendar.add(Calendar.DATE, -4);
            fiveDaysAgo = calendar.getTime();
        }

        @Test
        public void createShouldPersistData() {
            Rental rental = new Rental("Valdir", today, fiveDaysAgo);
            this.rentalRepository.save(rental);
            assertThat(rental.getId()).isNotNull();
            assertThat(rental.getCustomer()).isEqualTo("Valdir");
            assertThat(rental.getStart()).isEqualTo(today);
            assertThat(rental.getEnd()).isEqualTo(fiveDaysAgo);
        }

    @Test
    public void deleteShouldRemoveData() {
        Rental rental = new Rental("Valdir", today, fiveDaysAgo);
        this.rentalRepository.save(rental);
        rentalRepository.delete(rental);
        assertThat(rentalRepository.findById(rental.getId())).isNull();
    }

    @Test
    public void updateShouldChangeAndPersistData() {
        Rental rental = new Rental("Valdir", today, fiveDaysAgo);
        this.rentalRepository.save(rental);
        rental.setCustomer("Valdirrrr");
        this.rentalRepository.save(rental);
        rental = this.rentalRepository.findById(rental.getId()).get();
        assertThat(rental.getCustomer()).isEqualTo("Valdirrrr");
    }

    @Test
    public void findByNameIgnoreCaseContainingShouldIgnoreCase() {
        Rental rental = new Rental("Valdir", today, fiveDaysAgo);
        Rental rental2 = new Rental("Valdir2", yesterday, fiveDaysAgo);
        this.rentalRepository.save(rental);
        this.rentalRepository.save(rental2);
        List<Rental> studentList = rentalRepository.findByCustomerContaining("valdir");
        assertThat(studentList.size()).isEqualTo(2);
    }


    @Test
    public void createWhenStartDateIsNullShouldThrowConstraintViolationException() {
        thrown.expect(ConstraintViolationException.class);
        Rental rental = new Rental();
        rental.setCustomer("Valdir");
        this.rentalRepository.save(rental);
    }

    @Test
    public void createWhenCustomerIsNullShouldThrowConstraintViolationException() {
        thrown.expect(ConstraintViolationException.class);
        Rental rental = new Rental();
        this.rentalRepository.save(rental);
    }


}
