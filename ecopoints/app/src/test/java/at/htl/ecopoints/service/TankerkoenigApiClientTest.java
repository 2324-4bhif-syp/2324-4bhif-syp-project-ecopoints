package at.htl.ecopoints.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TankerkoenigApiClientTest {

    @Test
    void test() {
        TankerkoenigApiClient tankerkoenigApiClient = new TankerkoenigApiClient();
        String result = tankerkoenigApiClient.callApi();
        System.out.println(result);
        assertTrue(result.contains("status"));
    }
}
