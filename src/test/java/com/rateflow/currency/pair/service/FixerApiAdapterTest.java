package com.rateflow.currency.pair.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rateflow.currency.pair.entity.CurrencyPair;
import com.rateflow.currency.pair.service.fetch.pairs.feature.FixerApiAdapter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class FixerApiAdapterTest {

    private static final String JSON = "{" +
            "\"success\": true," +
            "\"timestamp\": 1519296206," +
            "\"base\": \"EUR\"," +
            "\"date\": \"2021-03-17\"," +
            "\"rates\": {" +
            "    \"AUD\": 1.566015," +
            "    \"CAD\": 1.560132," +
            "    \"CHF\": 1.154727," +
            "    \"CNY\": 7.827874," +
            "    \"GBP\": 0.882047," +
            "    \"JPY\": 132.360679," +
            "    \"USD\": 1.23396" +
            "}" +
            "}";

    @Test
    public void testCorrectNumberOfPairs() {
        JsonObject jsonObject = JsonParser.parseString(JSON).getAsJsonObject();
        FixerApiAdapter adapter = new FixerApiAdapter();
        List<CurrencyPair> result = new ArrayList<>();
        adapter.parsePairs(jsonObject).subscribe(
                result::add
        );
        assertNotNull(result);
        assertFalse(result.isEmpty());

        int expectedPairsCount = 56;
        assertEquals(expectedPairsCount, result.size());
    }

    @Test
    public void testBaseCurrencyPairsExistence() {
        JsonObject jsonObject = JsonParser.parseString(JSON).getAsJsonObject();
        FixerApiAdapter adapter = new FixerApiAdapter();
        List<CurrencyPair> result = new ArrayList<>();
        adapter.parsePairs(jsonObject).subscribe(
                result::add
        );

        assertTrue(result.stream().anyMatch(pair ->
                pair.getFrom().equals("EUR") && pair.getTo().equals("USD")
        ));
        assertTrue(result.stream().anyMatch(pair ->
                pair.getFrom().equals("USD") && pair.getTo().equals("EUR")
        ));
    }

    @Test
    public void testRateCalculation() {
        JsonObject jsonObject = JsonParser.parseString(JSON).getAsJsonObject();
        FixerApiAdapter adapter = new FixerApiAdapter();
        List<CurrencyPair> result = new ArrayList<>();
        adapter.parsePairs(jsonObject).subscribe(
                result::add
        );

        CurrencyPair eurToUsdPair = result.stream()
                .filter(pair -> pair.getFrom().equals("EUR") && pair.getTo().equals("USD"))
                .findFirst().orElseThrow();
        assertEquals(1.23396, eurToUsdPair.getRate(), 0.00001);

        CurrencyPair usdToEurPair = result.stream()
                .filter(pair -> pair.getFrom().equals("USD") && pair.getTo().equals("EUR"))
                .findFirst().orElseThrow();
        assertEquals(1 / 1.23396, usdToEurPair.getRate(), 0.00001);
    }

    @Test
    public void testNoDuplicatePairs() {
        JsonObject jsonObject = JsonParser.parseString(JSON).getAsJsonObject();
        FixerApiAdapter adapter = new FixerApiAdapter();
        List<CurrencyPair> result = new ArrayList<>();
        adapter.parsePairs(jsonObject).subscribe(
                result::add
        );

        Set<String> uniquePairs = result.stream()
                .map(pair -> pair.getFrom() + "/" + pair.getTo())
                .collect(Collectors.toSet());
        assertEquals(result.size(), uniquePairs.size());
    }

}