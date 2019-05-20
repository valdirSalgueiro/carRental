package br.com.arizonacrossmedia.carrental;

import br.com.arizonacrossmedia.carrental.model.Rental;
import br.com.arizonacrossmedia.carrental.repository.RentalRepository;
import br.com.arizonacrossmedia.carrental.service.RentalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Arrays;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RentalControllerTests
{
    @MockBean
    private RentalRepository rentalRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RentalService service;

    @Test
    public void registrationWorksThroughAllLayers() throws Exception {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();

        calendar.add(Calendar.DATE, -4);
        Date fiveDaysAgo = calendar.getTime();

        Rental rental = new Rental("Valdir", fiveDaysAgo, today);

        mockMvc.perform(MockMvcRequestBuilders.post("/rental/")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(rental)))
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteWhenRentalDoesNotExistShouldReturnStatusCode404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/rental/{id}", -1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void returnsAllRentals() throws Exception {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();

        calendar.add(Calendar.DATE, -4);
        Date fiveDaysAgo = calendar.getTime();

        Rental rental = new Rental("Valdir", fiveDaysAgo, today);

        List<Rental> rentals = new ArrayList<Rental>();
        rentals.add(rental);

        given(service.listAll()).willReturn(rentals);

        mockMvc.perform(get("/rental")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", IsCollectionWithSize.hasSize(1)))
                .andExpect(jsonPath("$[0].customer", Matchers.is(rental.getCustomer())));
    }

}
