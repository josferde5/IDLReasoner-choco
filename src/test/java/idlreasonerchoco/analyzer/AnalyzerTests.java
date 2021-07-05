package idlreasonerchoco.analyzer;

import idlreasonerchoco.configuration.model.IDLException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AnalyzerTests {

    @ParameterizedTest
    @ValueSource(strings = {"noParams", "oneParamBoolean", "oneDependencyRequires", "oneDependencyOr", "oneDependencyOnlyOne",
            "oneDependencyAllOrNone", "oneDependencyZeroOrNone", "oneDependencyArithRel", "oneDependencyComplex"})
    void randomValidRequestTest(String operation) throws IDLException {
        Analyzer analyzer = new Analyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/" + operation, "get");
        Map<String, String> validRequest = analyzer.getRandomRequest();
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: " + operation);
    }

    @Test
    void youtubeValidRequest() throws IDLException {
        Analyzer analyzer = new Analyzer("oas", "./src/test/resources/youtube_search.yaml", "/search", "get");
        Map<String, String> validRequest = analyzer.getRandomRequest();
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");

        validRequest = analyzer.getRandomRequest();
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");

        validRequest = analyzer.getRandomRequest();
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");

        validRequest = analyzer.getRandomRequest();
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");

        validRequest = analyzer.getRandomRequest();
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");

        System.out.println("Test passed: youtube_search");
    }

}
