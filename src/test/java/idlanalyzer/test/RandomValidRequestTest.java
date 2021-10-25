package idlanalyzer.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

import idlanalyzer.analyzer.Analyzer;
import idlanalyzer.analyzer.OASAnalyzer;
import idlanalyzer.configuration.IDLException;

public class RandomValidRequestTest {

    @Test
    public void no_params() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/noParams", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: no_params.");
    }

    @Test
    public void one_param_boolean_no_deps() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneParamBoolean", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: one_param_boolean_no_deps.");
    }

    @Test
    public void one_param_string_no_deps() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneParamString", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: one_param_string_no_deps.");
    }

    @Test
    public void one_param_int_no_deps() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneParamInt", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: one_param_int_no_deps.");
    }

    @Test
    public void one_param_enum_string_no_deps() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneParamEnumString", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: one_param_enum_string_no_deps.");
    }

    @Test
    public void one_param_enum_int_no_deps() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneParamEnumInt", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: one_param_enum_int_no_deps.");
    }

    @Test
    public void one_dep_requires() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyRequires", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: one_dep_requires.");
    }

    @Test
    public void one_dep_or() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyOr", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: one_dep_or.");
    }

    @Test
    public void one_dep_onlyone() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyOnlyOne", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: one_dep_onlyone.");
    }

    @Test
    public void one_dep_allornone() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyAllOrNone", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: one_dep_allornone.");
    }

    @Test
    public void one_dep_zeroorone() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyZeroOrOne", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: one_dep_zeroorone.");
    }

    @Test
    public void one_dep_arithrel() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyArithRel", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: one_dep_arithrel.");
    }

    @Test
    public void one_dep_complex() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyComplex", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: one_dep_complex.");
    }

    @Test
    public void combinatorial1() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial1", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: combinatorial1.");
    }

//    // The operations whose IDL specification is invalid cannot be tested
//    @Test
//    public void combinatorial2() throws IDLException {
//        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial2", "get");
//        System.out.println("Test passed: combinatorial2.");
//    }

    @Test
    public void combinatorial3() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial3", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: combinatorial3.");
    }

    @Test
    public void combinatorial4() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial4", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: combinatorial4.");
    }

    @Test
    public void combinatorial5() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial5", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: combinatorial5.");
    }

//    // The operations whose IDL specification is invalid cannot be tested
//    @Test
//    public void combinatorial6() throws IDLException {
//        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial6", "get");
//        System.out.println("Test passed: combinatorial6.");
//    }

//    // The operations whose IDL specification is invalid cannot be tested
//    @Test
//    public void combinatorial7() throws IDLException {
//        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial7", "get");
//        System.out.println("Test passed: combinatorial7.");
//    }

    @Test
    public void combinatorial8() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial8", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: combinatorial8.");
    }

//    // The operations whose IDL specification is invalid cannot be tested
//    @Test
//    public void combinatorial9() throws IDLException {
//        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial9", "get");
//        System.out.println("Test passed: combinatorial9.");
//    }

//    // The operations whose IDL specification is invalid cannot be tested
//    @Test
//    public void combinatorial10() throws IDLException {
//        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial10", "get");
//        System.out.println("Test passed: combinatorial10.");
//    }
}
