package idlanalyzer.test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import idlanalyzer.analyzer.Analyzer;
import idlanalyzer.analyzer.OASAnalyzer;
import idlanalyzer.configuration.IDLException;

public class ValidPartialRequestTest {

    @Test
    public void no_params_valid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/noParams", "get");
        Map<String, String> partialRequest = new HashMap<>();
        assertTrue(analyzer.isValidPartialRequest(partialRequest), "The partial request should be VALID");
        System.out.println("Test passed: no_params_valid.");
    }

//    // It is impossible to create an invalid partial request for an operation without parameters
//    @Test
//    public void no_params_invalid() throws IDLException {
//        System.out.println("Test passed: no_params_invalid.");
//    }

    @Test
    public void one_param_boolean_no_deps_valid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneParamBoolean", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p1", "false");
        assertTrue(analyzer.isValidPartialRequest(partialRequest), "The partial request should be VALID");
        System.out.println("Test passed: one_param_boolean_no_deps_valid.");
    }

    @Test
    public void one_param_boolean_no_deps_invalid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneParamBoolean", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p1", "not boolean");
        assertFalse(analyzer.isValidPartialRequest(partialRequest), "The partial request should be NOT valid");
        System.out.println("Test passed: one_param_boolean_no_deps_invalid.");
    }

    @Test
    public void one_param_string_no_deps_valid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneParamString", "get");
        Map<String, String> partialRequest = new HashMap<>();
        assertTrue(analyzer.isValidPartialRequest(partialRequest), "The partial request should be VALID");
        System.out.println("Test passed: one_param_string_no_deps_valid.");
    }

//    // It is impossible to create an invalid partial request for an operation with only one required string parameter
//    @Test
//    public void one_param_string_no_deps_invalid() throws IDLException {
//        System.out.println("Test passed: one_param_string_no_deps_invalid.");
//    }

    @Test
    public void one_param_int_no_deps_valid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneParamInt", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p1", "10");
        assertTrue(analyzer.isValidPartialRequest(partialRequest), "The partial request should be VALID");
        System.out.println("Test passed: one_param_int_no_deps_valid.");
    }

    @Test
    public void one_param_int_no_deps_invalid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneParamInt", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p1", "not an integer");
        assertFalse(analyzer.isValidPartialRequest(partialRequest), "The partial request should be NOT valid");
        System.out.println("Test passed: one_param_int_no_deps_invalid.");
    }

    @Test
    public void one_param_enum_string_no_deps_valid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneParamEnumString", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p1", "value1");
        assertTrue(analyzer.isValidPartialRequest(partialRequest), "The partial request should be VALID");
        System.out.println("Test passed: one_param_enum_string_no_deps_valid.");
    }

    @Test
    public void one_param_enum_string_no_deps_invalid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneParamEnumString", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p1", "string not in enum alternatives");
        assertFalse(analyzer.isValidPartialRequest(partialRequest), "The partial request should be NOT valid");
        System.out.println("Test passed: one_param_enum_string_no_deps_invalid.");
    }

    @Test
    public void one_param_enum_int_no_deps_valid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneParamEnumInt", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p1", "1");
        assertTrue(analyzer.isValidPartialRequest(partialRequest), "The partial request should be VALID");
        System.out.println("Test passed: one_param_enum_int_no_deps_valid.");
    }

    @Test
    public void one_param_enum_int_no_deps_invalid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneParamEnumInt", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p1", "6");
        assertFalse(analyzer.isValidPartialRequest(partialRequest), "The partial request should be NOT valid");
        System.out.println("Test passed: one_param_enum_int_no_deps_invalid.");
    }

    @Test
    public void one_dep_requires_valid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyRequires", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p1", "true");
        assertTrue(analyzer.isValidPartialRequest(partialRequest), "The partial request should be VALID");
        System.out.println("Test passed: one_dep_requires_valid.");
    }

    @Test
    public void one_dep_requires_invalid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyRequires", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p5", "6");
        assertFalse(analyzer.isValidPartialRequest(partialRequest), "The partial request should be NOT valid");
        System.out.println("Test passed: one_dep_requires_invalid.");
    }

    @Test
    public void one_dep_or_valid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyOr", "get");
        Map<String, String> partialRequest = new HashMap<>();
        assertTrue(analyzer.isValidPartialRequest(partialRequest), "The partial request should be VALID");
        System.out.println("Test passed: one_dep_or_valid.");
    }

    @Test
    public void one_dep_or_invalid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyOr", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p4", "value6");
        assertFalse(analyzer.isValidPartialRequest(partialRequest), "The partial request should be NOT valid");
        System.out.println("Test passed: one_dep_or_invalid.");
    }

    @Test
    public void one_dep_onlyone_valid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyOnlyOne", "get");
        Map<String, String> partialRequest = new HashMap<>();
        assertTrue(analyzer.isValidPartialRequest(partialRequest), "The partial request should be VALID");
        System.out.println("Test passed: one_dep_onlyone_valid.");
    }

    @Test
    public void one_dep_onlyone_invalid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyOnlyOne", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p1", "true");
        partialRequest.put("p2", "a string");
        assertFalse(analyzer.isValidPartialRequest(partialRequest), "The partial request should be NOT valid");
        System.out.println("Test passed: one_dep_onlyone_invalid.");
    }

    @Test
    public void one_dep_allornone_valid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyAllOrNone", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p3", "-5");
        partialRequest.put("p4", "value5");
        partialRequest.put("p5", "1");
        assertTrue(analyzer.isValidPartialRequest(partialRequest), "The partial request should be VALID");
        System.out.println("Test passed: one_dep_allornone_valid.");
    }

    @Test
    public void one_dep_allornone_invalid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyAllOrNone", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p1", "true");
        partialRequest.put("p3", "a string, not an int");
        partialRequest.put("p4", "value5");
        partialRequest.put("p5", "1");
        assertFalse(analyzer.isValidPartialRequest(partialRequest), "The partial request should be NOT valid");
        System.out.println("Test passed: one_dep_allornone_invalid.");
    }

    @Test
    public void one_dep_zeroorone_valid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyZeroOrOne", "get");
        Map<String, String> partialRequest = new HashMap<>();
        assertTrue(analyzer.isValidPartialRequest(partialRequest), "The partial request should be VALID");
        System.out.println("Test passed: one_dep_zeroorone_valid.");
    }

    @Test
    public void one_dep_zeroorone_invalid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyZeroOrOne", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p1", "true");
        partialRequest.put("p2", "1");
        partialRequest.put("p3", "1");
        partialRequest.put("p4", "1");
        partialRequest.put("p5", "1");
        assertFalse(analyzer.isValidPartialRequest(partialRequest), "The partial request should be NOT valid");
        System.out.println("Test passed: one_dep_zeroorone_invalid.");
    }

    @Test
    public void one_dep_arithrel_valid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyArithRel", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p3", "1");
        partialRequest.put("p5", "1");
        assertTrue(analyzer.isValidPartialRequest(partialRequest), "The partial request should be VALID");
        System.out.println("Test passed: one_dep_arithrel_valid.");
    }

    @Test
    public void one_dep_arithrel_invalid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyArithRel", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p3", "2");
        partialRequest.put("p5", "1");
        assertFalse(analyzer.isValidPartialRequest(partialRequest), "The partial request should be NOT valid");
        System.out.println("Test passed: one_dep_arithrel_invalid.");
    }

    @Test
    public void one_dep_complex_valid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyComplex", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p1", "false");
        assertTrue(analyzer.isValidPartialRequest(partialRequest), "The partial request should be VALID");
        System.out.println("Test passed: one_dep_complex_valid.");
    }

    @Test
    public void one_dep_complex_invalid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyComplex", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p1", "false");
        partialRequest.put("p2", "string");
        partialRequest.put("p3", "-1000");
        assertFalse(analyzer.isValidPartialRequest(partialRequest), "The partial request should be NOT valid");
        System.out.println("Test passed: one_dep_complex_invalid.");
    }

    @Test
    public void combinatorial1_valid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial1", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p1", "value1");
        partialRequest.put("p2", "value2");
        partialRequest.put("p3", "value3");
        partialRequest.put("p4", "value4");
        assertTrue(analyzer.isValidPartialRequest(partialRequest), "The partial request should be VALID");
        System.out.println("Test passed: combinatorial1_valid.");
    }

    @Test
    public void combinatorial1_invalid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial1", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p1", "value1");
        partialRequest.put("p2", "value2");
        partialRequest.put("p3", "value3");
        partialRequest.put("p4", "value4");
        partialRequest.put("p5", "value5"); // Violates this dependency: p1==p5;
        assertFalse(analyzer.isValidPartialRequest(partialRequest), "The partial request should be NOT valid");
        System.out.println("Test passed: combinatorial1_invalid.");
    }

//    // The operations whose IDL specification is invalid cannot be tested
//    @Test
//    public void combinatorial2_valid() throws IDLException {
//        System.out.println("Test passed: combinatorial2_valid.");
//    }
//
//    @Test
//    public void combinatorial2_invalid() throws IDLException {
//        System.out.println("Test passed: combinatorial2_invalid.");
//    }

    @Test
    public void combinatorial3_valid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial3", "get");
        Map<String, String> partialRequest = new HashMap<>();
        assertTrue(analyzer.isValidPartialRequest(partialRequest), "The partial request should be VALID");
        System.out.println("Test passed: combinatorial3_valid.");
    }

    @Test
    public void combinatorial3_invalid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial3", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p1", "true");
        partialRequest.put("p3", "value4");
        partialRequest.put("p4", "1");
        partialRequest.put("p5", "3"); // Violates this dependency: p4>=p5;
        assertFalse(analyzer.isValidPartialRequest(partialRequest), "The partial request should be NOT valid");
        System.out.println("Test passed: combinatorial3_invalid.");
    }

    @Test
    public void combinatorial4_valid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial4", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p2", "1000000");
        partialRequest.put("p3", "100000");
        partialRequest.put("p4", "10000");
        partialRequest.put("p5", "1000");
        assertTrue(analyzer.isValidPartialRequest(partialRequest), "The partial request should be VALID");
        System.out.println("Test passed: combinatorial4_valid.");
    }

    @Test
    public void combinatorial4_invalid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial4", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p2", "1000000");
        partialRequest.put("p3", "100000");
        partialRequest.put("p4", "10000");
        partialRequest.put("p5", "1000");
        partialRequest.put("p8", "value5"); // If this parameter was removed, the request would be partially valid, since including p6 AND p7 would make it fully valid. As p8 is present, including p6 AND p7 would violate: ZeroOrOne(p7 AND p8, p7 AND p8 OR p9=='value5');
        assertFalse(analyzer.isValidPartialRequest(partialRequest), "The partial request should be NOT valid");
        System.out.println("Test passed: combinatorial4_invalid.");
    }

    /*
     * Is fully valid: false
     * Dependency violated: -
     * Explanation: The request is invalid because all parameters are required.
     * It is though partially valid, since including the 10 parameters would make
     * it fully valid.
     */
    @Test
    public void combinatorial5_valid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial5", "get");
        Map<String, String> partialRequest = new HashMap<>();
        assertTrue(analyzer.isValidPartialRequest(partialRequest), "The partial request should be VALID");
        System.out.println("Test passed: combinatorial5_valid.");
    }

    /*
     * Dependency violated: Or(p1==p6, p4==p7);
     * Explanation: Removing p7 would make the request partially valid.
     */
    @Test
    public void combinatorial5_invalid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial5", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p1", "true");
        partialRequest.put("p2", "something");
        partialRequest.put("p3", "5");
        partialRequest.put("p4", "value5");
        partialRequest.put("p5", "-300");
        partialRequest.put("p6", "false");
        partialRequest.put("p7", "another example");
        partialRequest.put("p8", "1");
        partialRequest.put("p9", "value1");
        partialRequest.put("p10", "value2");
        assertFalse(analyzer.isValidPartialRequest(partialRequest), "The partial request should be NOT valid");
        System.out.println("Test passed: combinatorial5_invalid.");
    }

//    // The operations whose IDL specification is invalid cannot be tested
//    @Test
//    public void combinatorial6_valid() throws IDLException {
//        System.out.println("Test passed: combinatorial6_valid.");
//    }
//
//    @Test
//    public void combinatorial6_invalid() throws IDLException {
//        System.out.println("Test passed: combinatorial6_invalid.");
//    }

//    // The operations whose IDL specification is invalid cannot be tested
//    @Test
//    public void combinatorial7_valid() throws IDLException {
//        System.out.println("Test passed: combinatorial7_valid.");
//    }
//
//    @Test
//    public void combinatorial7_invalid() throws IDLException {
//        System.out.println("Test passed: combinatorial7_invalid.");
//    }

    /*
     * Is fully valid: false
     * Dependency violated: AllOrNone(p1, p5)
     * Explanation: It is partially valid, since including p5 would make it fully valid.
     */
    @Test
    public void combinatorial8_valid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial8", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p1", "false");
        partialRequest.put("p2", "false");
        partialRequest.put("p4", "true");
        partialRequest.put("p6", "one string");
        partialRequest.put("p8", "something");
        partialRequest.put("p9", "fixed string");
        partialRequest.put("p10", "something");
        assertTrue(analyzer.isValidPartialRequest(partialRequest), "The partial request should be VALID");
        System.out.println("Test passed: combinatorial8_valid.");
    }

    /*
     * Dependency violated: IF p1 THEN (p3==true OR (NOT p3)) AND NOT p7 AND p9=='fixed string';
     * Explanation: Removing p7 would make the request partially valid.
     */
    @Test
    public void combinatorial8_invalid() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial8", "get");
        Map<String, String> partialRequest = new HashMap<>();
        partialRequest.put("p1", "false");
        partialRequest.put("p7", "a string"); // Violates this dependency: AllOrNone(p6!=p8, p8==p10);
        assertFalse(analyzer.isValidPartialRequest(partialRequest), "The partial request should be NOT valid");
        System.out.println("Test passed: combinatorial8_invalid.");
    }

//    // The operations whose IDL specification is invalid cannot be tested
//    @Test
//    public void combinatorial9_valid() throws IDLException {
//        System.out.println("Test passed: combinatorial9_valid.");
//    }
//
//    @Test
//    public void combinatorial9_invalid() throws IDLException {
//        System.out.println("Test passed: combinatorial9_invalid.");
//    }

//    // The operations whose IDL specification is invalid cannot be tested
//    @Test
//    public void combinatorial10_valid() throws IDLException {
//        System.out.println("Test passed: combinatorial10_valid.");
//    }
//
//    @Test
//    public void combinatorial10_invalid() throws IDLException {
//        System.out.println("Test passed: combinatorial10_invalid.");
//    }
}
