package pl.dpotyralski.videorentalstore

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import pl.dpotyralski.videorentalstore.film.protocol.response.FilmsResponse
import pl.dpotyralski.videorentalstore.rental.protocol.request.FilmDetailsRequest
import pl.dpotyralski.videorentalstore.rental.protocol.request.RentRequest
import pl.dpotyralski.videorentalstore.rental.protocol.request.RentalPriceCalculateRequest
import pl.dpotyralski.videorentalstore.rental.protocol.response.RentalPriceResponse
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.dpotyralski.videorentalstore.AcceptanceSpec.Fields.MATRIX_FILM_ID
import static pl.dpotyralski.videorentalstore.AcceptanceSpec.Fields.SPIDER_MAN_FILM_ID

@SpringBootTest
@AutoConfigureMockMvc
class AcceptanceSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    private JacksonTester<FilmsResponse> filmsResponseJson
    private JacksonTester<RentalPriceCalculateRequest> rentalPriceCalculateRequestJson
    private JacksonTester<RentRequest> rentRequestJson
    private JacksonTester<RentalPriceResponse> rentalPriceResponseJson

    void setup() {
        ObjectMapper objectMapper = new ObjectMapper()
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        JacksonTester.initFields(this, objectMapper)
    }

    def 'Should be able to rent a film, add bonus points and return it back without additional charge'() {
        when: "we aks for films"
        ResultActions getFilms = mockMvc.perform(get("/films"))

        then: "we expect to get list of films"
        getFilms.andExpect(status().isOk())
                .andExpect(content().json(filmsResponseJson.write(createFilmsResponse()).getJson()))

        and: "we are able to check rental price for Matrix and Spider Man"
        ResultActions checkRentalPrice = mockMvc.perform(post("/rentals/price-calculation")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(rentalPriceCalculateRequestJson.write(createRentalPriceCalculateRequest()).getJson()))

        and: "the price for given films is equal to 250"
        checkRentalPrice.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(rentalPriceResponseJson.write(createRentalPriceResponse()).getJson()))

        and: "we should be able to rent those films"
        ResultActions rentalAction = mockMvc.perform(post("/rentals")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(rentRequestJson.write(createRentRequest()).getJson()))

        and: "price for rental should be equal to 250"
        rentalAction.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(rentalPriceResponseJson.write(createRentalPriceResponse()).getJson()))

        and: "we are able to ask for rented films"
        ResultActions rentedFilms = mockMvc.perform(get("/rentals")
                .param("byCustomerId", Fields.TEST_CUSTOMER_ID.toString())
                .param("onlyActive", "TRUE"))

        and: "list should contain 2 active rentals"
        rentedFilms.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("\$.rentals", Matchers.hasSize(2)))

        and: "we are able to ask for bonus points"
        ResultActions customerBonusPoints = mockMvc.perform(get("/customers/$Fields.TEST_CUSTOMER_ID/bonus-points"))

        and: "three bonus points should be added"
        customerBonusPoints
                .andExpect(status().isOk())
                .andExpect(jsonPath("\$.points",).value(Matchers.equalTo(3)))

        and: "we can return film"
        ResultActions returnAction = mockMvc.perform(post("/rentals/$Fields.FIRST_RENTAL_ID/return/")
                .contentType(MediaType.APPLICATION_JSON_UTF8))

        and: "no additional charge should be calculated"
        returnAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("\$.lateReturnCharge",).value(Matchers.equalTo(0)))

        and: "we are able to ask for rented films"
        ResultActions rentedFilmsAfterReturn = mockMvc.perform(get("/rentals")
                .param("byCustomerId", Fields.TEST_CUSTOMER_ID.toString())
                .param("onlyActive", "TRUE"))

        and: "list should contain 1 active rental"
        rentedFilmsAfterReturn.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("\$.rentals", Matchers.hasSize(1)))
    }

    RentRequest createRentRequest() {
        RentRequest request = new RentRequest()
        request.setCustomerId(Fields.TEST_CUSTOMER_ID)
        request.setDetails(getTestRentalDetails())
        request
    }

    RentalPriceCalculateRequest createRentalPriceCalculateRequest() {
        RentalPriceCalculateRequest request = new RentalPriceCalculateRequest()
        request.setDetails(getTestRentalDetails())
        request
    }

    private static Set<FilmDetailsRequest> getTestRentalDetails() {
        [new FilmDetailsRequest(filmId: MATRIX_FILM_ID, days: 4), new FilmDetailsRequest(filmId: SPIDER_MAN_FILM_ID, days: 5)] as Set
    }

    RentalPriceResponse createRentalPriceResponse() {
        return new RentalPriceResponse(Fields.RENTAL_PRICE)
    }

    FilmsResponse createFilmsResponse() {
        new FilmsResponse([
                new FilmsResponse.FilmResponse(MATRIX_FILM_ID, "Matrix 11"),
                new FilmsResponse.FilmResponse(SPIDER_MAN_FILM_ID, "Spider Man"),
                new FilmsResponse.FilmResponse(3, "Spider Man 2"),
                new FilmsResponse.FilmResponse(4, "Out of Africa")
        ])
    }

    static class Fields {
        public static final int FIRST_RENTAL_ID = 1
        public static final int TEST_CUSTOMER_ID = 100
        public static final int MATRIX_FILM_ID = 1
        public static final int SPIDER_MAN_FILM_ID = 2
        public static final BigDecimal RENTAL_PRICE = 250
    }

}
