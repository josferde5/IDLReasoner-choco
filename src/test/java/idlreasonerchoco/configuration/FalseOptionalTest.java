package idlreasonerchoco.configuration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import idlreasonerchoco.analyzer.Analyzer;
import idlreasonerchoco.analyzer.OASAnalyzer;


public class FalseOptionalTest {

//    // If there are no parameters in the specification, isFalseOptional cannot be tested
//    @Test
//    public void no_params() throws IDLException {
//        System.out.println("Test passed: no_params.");
//    }

    @Test
    public void one_param_boolean_no_deps() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneParamBoolean", "get");
        assertFalse(analyzer.isFalseOptional("p1"), "The parameter p1 should NOT be false optional");
        System.out.println("Test passed: one_param_boolean_no_deps.");
    }

    @Test
    public void one_param_string_no_deps() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneParamString", "get");
        assertFalse(analyzer.isFalseOptional("p1"), "The parameter p1 should NOT be false optional");
        System.out.println("Test passed: one_param_string_no_deps.");
    }

    @Test
    public void one_param_int_no_deps() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneParamInt", "get");
        assertFalse(analyzer.isFalseOptional("p1"), "The parameter p1 should NOT be false optional");
        System.out.println("Test passed: one_param_int_no_deps.");
    }

    @Test
    public void one_param_enum_string_no_deps() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneParamEnumString", "get");
        assertFalse(analyzer.isFalseOptional("p1"), "The parameter p1 should NOT be false optional");
        System.out.println("Test passed: one_param_enum_string_no_deps.");
    }

    @Test
    public void one_param_enum_int_no_deps() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneParamEnumInt", "get");
        assertFalse(analyzer.isFalseOptional("p1"), "The parameter p1 should NOT be false optional");
        System.out.println("Test passed: one_param_enum_int_no_deps.");
    }

    @Test
    public void one_dep_requires() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyRequires", "get");
        assertFalse(analyzer.isFalseOptional("p1"), "The parameter p1 should NOT be false optional");
        System.out.println("Test passed: one_dep_requires.");
    }

    @Test
    public void one_dep_or() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyOr", "get");
        assertFalse(analyzer.isFalseOptional("p1"), "The parameter p1 should NOT be false optional");
        System.out.println("Test passed: one_dep_or.");
    }

    @Test
    public void one_dep_onlyone() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyOnlyOne", "get");
        assertFalse(analyzer.isFalseOptional("p1"), "The parameter p1 should NOT be false optional");
        System.out.println("Test passed: one_dep_onlyone.");
    }

    @Test
    public void one_dep_allornone() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyAllOrNone", "get");
        assertFalse(analyzer.isFalseOptional("p1"), "The parameter p1 should NOT be false optional");
        System.out.println("Test passed: one_dep_allornone.");
    }

    @Test
    public void one_dep_zeroorone() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyZeroOrOne", "get");
        assertFalse(analyzer.isFalseOptional("p1"), "The parameter p1 should NOT be false optional");
        System.out.println("Test passed: one_dep_zeroorone.");
    }

    @Test
    public void one_dep_arithrel() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyArithRel", "get");
        assertFalse(analyzer.isFalseOptional("p1"), "The parameter p1 should NOT be false optional");
        System.out.println("Test passed: one_dep_arithrel.");
    }

    @Test
    public void one_dep_complex() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyComplex", "get");
        assertFalse(analyzer.isFalseOptional("p1"), "The parameter p1 should NOT be false optional");
        System.out.println("Test passed: one_dep_complex.");
    }

    @Test
    public void combinatorial1() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial1", "get");
        assertFalse(analyzer.isFalseOptional("p1"), "The parameter p1 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p2"), "The parameter p2 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p3"), "The parameter p3 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p4"), "The parameter p4 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p5"), "The parameter p5 should NOT be false optional");
        System.out.println("Test passed: combinatorial1.");
    }

    @Test
    public void combinatorial2() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial2", "get");
        assertTrue(analyzer.isFalseOptional("p1"), "The parameter p1 SHOULD be false optional");
        assertFalse(analyzer.isFalseOptional("p2"), "The parameter p2 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p3"), "The parameter p3 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p4"), "The parameter p4 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p5"), "The parameter p5 should NOT be false optional");
        assertTrue(analyzer.isFalseOptional("p6"), "The parameter p6 SHOULD be false optional");
        assertFalse(analyzer.isFalseOptional("p7"), "The parameter p7 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p8"), "The parameter p8 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p9"), "The parameter p9 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p10"), "The parameter p10 should NOT be false optional");
        System.out.println("Test passed: combinatorial2.");
    }

    @Test
    public void combinatorial3() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial3", "get");
        assertFalse(analyzer.isFalseOptional("p1"), "The parameter p1 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p2"), "The parameter p2 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p3"), "The parameter p3 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p4"), "The parameter p4 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p5"), "The parameter p5 should NOT be false optional");
        System.out.println("Test passed: combinatorial3.");
    }

    @Test
    public void combinatorial4() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial4", "get");
        assertFalse(analyzer.isFalseOptional("p1"), "The parameter p1 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p2"), "The parameter p2 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p3"), "The parameter p3 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p4"), "The parameter p4 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p5"), "The parameter p5 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p6"), "The parameter p6 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p7"), "The parameter p7 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p8"), "The parameter p8 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p9"), "The parameter p9 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p10"), "The parameter p10 should NOT be dead");
        System.out.println("Test passed: combinatorial4.");
    }

    @Test
    public void combinatorial5() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial5", "get");
        assertFalse(analyzer.isFalseOptional("p1"), "The parameter p1 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p2"), "The parameter p2 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p3"), "The parameter p3 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p4"), "The parameter p4 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p5"), "The parameter p5 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p6"), "The parameter p6 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p7"), "The parameter p7 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p8"), "The parameter p8 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p9"), "The parameter p9 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p10"), "The parameter p10 should NOT be false optional");
        System.out.println("Test passed: combinatorial5.");
    }

    @Test
    public void combinatorial6() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial6", "get");
        assertFalse(analyzer.isFalseOptional("p1"), "The parameter p1 SHOULD be false optional");
        assertFalse(analyzer.isFalseOptional("p2"), "The parameter p2 SHOULD be false optional");
        assertFalse(analyzer.isFalseOptional("p3"), "The parameter p3 SHOULD be false optional");
        assertFalse(analyzer.isFalseOptional("p4"), "The parameter p4 SHOULD be false optional");
        assertFalse(analyzer.isFalseOptional("p5"), "The parameter p5 SHOULD be false optional");
        System.out.println("Test passed: combinatorial6.");
    }

    @Test
    public void combinatorial7() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial7", "get");
        assertFalse(analyzer.isFalseOptional("p1"), "The parameter p1 SHOULD be false optional");
        assertFalse(analyzer.isFalseOptional("p2"), "The parameter p2 SHOULD be false optional");
        assertFalse(analyzer.isFalseOptional("p3"), "The parameter p3 SHOULD be false optional");
        assertFalse(analyzer.isFalseOptional("p4"), "The parameter p4 SHOULD be false optional");
        assertFalse(analyzer.isFalseOptional("p5"), "The parameter p5 SHOULD be false optional");
        System.out.println("Test passed: combinatorial7.");
    }

    @Test
    public void combinatorial8() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial8", "get");
        assertFalse(analyzer.isFalseOptional("p1"), "The parameter p1 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p2"), "The parameter p2 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p3"), "The parameter p3 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p4"), "The parameter p4 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p5"), "The parameter p5 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p6"), "The parameter p6 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p7"), "The parameter p7 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p8"), "The parameter p8 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p9"), "The parameter p9 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p10"), "The parameter p10 should NOT be false optional");
        System.out.println("Test passed: combinatorial8.");
    }

    @Test
    public void combinatorial9() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial9", "get");
        assertFalse(analyzer.isFalseOptional("p1"), "The parameter p1 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p2"), "The parameter p2 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p3"), "The parameter p3 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p4"), "The parameter p4 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p5"), "The parameter p5 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p6"), "The parameter p6 should NOT be false optional");
        assertTrue(analyzer.isFalseOptional("p7"), "The parameter p7 SHOULD be false optional");
        assertFalse(analyzer.isFalseOptional("p8"), "The parameter p8 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p9"), "The parameter p9 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p10"), "The parameter p10 should NOT be false optional");
        System.out.println("Test passed: combinatorial9.");
    }

    @Test
    public void combinatorial10() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/combinatorial10", "get");
        assertFalse(analyzer.isFalseOptional("p1"), "The parameter p1 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p2"), "The parameter p2 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p3"), "The parameter p3 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p4"), "The parameter p4 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p5"), "The parameter p5 should NOT be false optional");
        System.out.println("Test passed: combinatorial10.");
    }
}
