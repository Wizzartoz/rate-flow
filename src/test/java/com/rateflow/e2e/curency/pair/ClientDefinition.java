package com.rateflow.e2e.curency.pair;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rateflow.dto.currency.pair.response.CurrencyPairResponseDto;
import com.rateflow.currency.pair.entity.CurrencyPair;
import com.rateflow.currency.pair.repository.CurrencyPairRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

import java.util.List;

import static io.restassured.RestAssured.given;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientDefinition {

    private static final MongoDBContainer MONGO_DB_CONTAINER = new MongoDBContainer("mongo:4.4.6");

    static {
        MONGO_DB_CONTAINER.start();
    }

    @DynamicPropertySource
    static void overrideMongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", MONGO_DB_CONTAINER::getReplicaSetUrl);
    }


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CurrencyPairRepository currencyPairRepository;

    @LocalServerPort
    private Integer port;
    private Response lastResponse;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Given("the storage has been cleared")
    public void clearStorage() {
        currencyPairRepository.deleteAll().subscribe();
    }

    @And("the following currency pairs are available as JSON")
    @SneakyThrows
    public void insertProducts(String json) {
        List<CurrencyPair> products = objectMapper.readValue(json, new TypeReference<>(){});
        currencyPairRepository.saveAll(products).subscribe();
    }

    @When("the client requests for currency pairs")
    public void request() {
        this.lastResponse = given().get("/currency-pairs");
    }

    @When("the client requests for currency pairs with base {string}")
    public void request(String base) {
        this.lastResponse = given().get("/currency-pairs/base/" + base);
    }

    @When("the client requests for currency pairs with from {string}, to {string}")
    public void request(String from, String to) {
        this.lastResponse = given().param("from", from).param("to", to).get("/currency-pairs/rate");
    }

    @Then("the client receives status code of {int}")
    public void compareCodeStatuses(int statusCode) {
        int  currentStatusCode = lastResponse.getStatusCode();
        Assertions.assertEquals(currentStatusCode, statusCode);
    }

    @And("the user should receive the following data as JSON")
    @SneakyThrows
    public void checkBodyResponse(String json) {
        List<CurrencyPairResponseDto> currencyPairsFromStorage = objectMapper.readValue(json, new TypeReference<>(){});
        List<CurrencyPairResponseDto> currencyPairsFromBody = lastResponse.getBody().as(new TypeRef<>() {});
        Assert.assertEquals(currencyPairsFromBody.toString(), currencyPairsFromStorage.toString());
    }

    @And("the user should receive the following single data as JSON")
    @SneakyThrows
    public void checkSingleBodyResponse(String json) {
        CurrencyPairResponseDto currencyPairsFromStorage = objectMapper.readValue(json, new TypeReference<>(){});
        CurrencyPairResponseDto currencyPairsFromBody = lastResponse.getBody().as(new TypeRef<>() {});
        Assert.assertEquals(currencyPairsFromBody.toString(), currencyPairsFromStorage.toString());
    }

}
