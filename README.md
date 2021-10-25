# IDLAnalyzer
 
IDLAnalyzer is a Java tool that enables analysis of IDL specifications automatically and performs several analysis operations on them.

## Index:

1. [IDL Specification](#inter-parameter-dependency-language)
2. [Quickstart guide](#quickstart-guide)
3. [How to use it?](#how-to-use-it)
4. [Analysis operations](#analysis-operations)
5. [Licence](#licence)


## Inter-parameter Dependency Language:
Inter-parameter Dependency Language (IDL) is a textual domain-specific language for the specification of dependencies among input parameters in web APIs. IDL is designed to express the seven types of inter-parameter dependencies (Requires, Or, OnlyOne, AllOrNone, ZeroOrOne, Arithmetic/Relational, and Complex). Following are some examples of dependencies described in IDL. To know more, visit the [IDL repository](https://github.com/isa-group/IDL) and the [sample IDL specifications](https://github.com/isa-group/IDL/tree/master/es.us.isa.interparamdep/resources/expressiveness_evaluation).

    IF p1 THEN p2=='A';
    IF p1 AND p2 THEN NOT (p3 OR p4);
    IF videoDefinition THEN type=='video';                           // Requires
    Or(query, type);                                                 // Or
    ZeroOrOne(radius, rankby=='distance');                           // ZeroOrOne
    AllOrNone(location, radius);                                     // AllOrNone
    OnlyOne(amount_off, percent_off);                                // OnlyOne
    publishedAfter >= publishedBefore;                               // Relational
    limit + offset <= 1000;                                          // Arithmetic
    IF intent=='browse' THEN OnlyOne(ll AND radius, sw AND ne);      // Complex


## Quickstart guide:
To get started with IDLAnalyzer, download the code and move to the parent directory:

        git clone https://github.com/ssegura/IDLAnalyzer.git
        cd IDLAnalyzer

**Installing local dependencies:**

IDLAnalyzer relies on idl.jar library located in the lib/ folder. To install it, simply run the following command from the parent directory.

        ./scripts/install_dependencies.sh

## How to use it?

-	**Step 1:** We need to configure the Analyzer component as following:

    -	Set the API type (i.e., "oas").
    -	Set the API path (i.e., "./src/test/resources/OAS_test_suite.yaml").
    -	Set the operation path (i.e., "/oneDependencyRequires")
    -	Set the operation (i.e., "get").
  
- **Step 2:**	Then, we perform an analysis operation (i.e., analyzer.isDeadParameter("p1")).
- **Step 3:** Finally, we obtain the result of analysis operation.


## Analysis operations:

- **isConsistent:** This operation receives as input the IDL specification of an API operation and its list of parameters, and returns a Boolean indicating whether the specification is consistent or not. An IDL specification is consistent if there exists at least one request satisfying all the dependencies of the specification.

   - **Input:** IDL specification.
   - **Output:** True if the IDL is consistent, otherwise false.
   - **Example:**
          
          @Test
           public void one_dep_or() throws IDLException {
              Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyOr", "get");
              assertTrue(analyzer. isConsistent (), "The IDL should be consistent ");
          } 
          
- **isValidIDL:** This operation receives as input the IDL specification of an API operation and its list of parameters, and returns a Boolean indicating whether the specification is valid or not. An IDL specification is valid if it is consistent and it does not contain any dead or false optional parameters.

    -	**Input:** (1) IDL specification. (2) API operation parameters.
    -	**Output:** True if the IDL is valid, otherwise false.
    -	**Example:**
    
            @Test
                public void one_dep_or() throws IDLException {
                    Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyOr", "get");
                    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
                }

- **isDeadParameter:** This operation receives as input the IDL specification of an API operation, its list of parameters, and the name of a parameter, and it returns a Boolean indicating whether the parameter is dead or not. A parameter is dead if it cannot be included in any valid call to the service.

    -	**Input:** (1) IDL specification. (2) API operation parameters. (3) Parameter to check.
    -	**Output:** True if the parameter is dead, otherwise false.
    -	**Example:**
    
            @Test
                public void one_dep_or() throws IDLException {
                    Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyOr", "get");
                    assertFalse(analyzer.isDeadParameter("p1"), "The parameter p1 should NOT be dead");
                }

- **isFalseOptional:** This operation assumes that the specification of each parameter indicates, as in OAS, whether the parameter is required or optional. This operation takes as input the IDL specification of an API operation, its list of parameters, and the name of a parameter specified as optional, and returns a Boolean indicating whether the parameter is false optional or not.

    -	**Input:** (1) IDL specification. (2) API operation parameters. (3) Parameter to check.
    -	**Output:** True if the parameter is false optional, otherwise false.
    -	**Example:**
    
            @Test
                public void one_dep_or() throws IDLException {
                    Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyOr", "get");
                    assertFalse(analyzer.isFalseOptional("p1"), "The parameter p1 should NOT be false optional");
                }
                
- **isValidRequest:** This operation takes as input the IDL specification of an API operation, its list of parameters, and a service request and returns a Boolean indicating whether the request is valid or not.

    -	**Input:** (1) IDL specification. (2) API operation parameters. (3) Request to check.
    -	**Output:** True if the request is valid, otherwise false.
    -	**Example:**
    
            @Test
                public void one_dep_or_valid() throws IDLException {
                    Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyOr", "get");
                    Map<String, String> request = new HashMap<>();
                    request.put("p1", "true");
                    assertTrue(analyzer.isValidRequest(request), "The request should be VALID");  
                }

- **isValidPartialRequest:** This operation is analogous to the previous one but the input request is partial or incomplete, meaning that some other parameters should still be included to make it a full valid request.

    -	**Input:** (1) IDL specification. (2) API operation parameters. (3) Request to check.
    -	**Output:** True if the request is partially valid, otherwise false.
    -	**Example:**
    
              @Test
                public void one_dep_requires_valid() throws IDLException {
                    Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyRequires", "get");
                    Map<String, String> partialRequest = new HashMap<>();
                    partialRequest.put("p1", "true");
                    assertTrue(analyzer.isValidPartialRequest(partialRequest), "The partial request should be VALID");
                }

- **getRandomValidRequest:** This operation receives as input the IDL specification of an API operation and its list of parameters, and returns a random valid request for the operation.

    -	**Input:** (1) IDL specification. (2) API operation parameters.
    -	**Output:** Returns a random valid request.
    -	**Example:**

              @Test
                public void one_dep_or() throws IDLException {
                    Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyOr", "get");
                    Map<String, String> validRequest = analyzer.getRandomValidRequest();
                    assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
                }

- **getRandomInvalidRequest:** This operation receives as input the IDL specification of an API operation and its list of parameters, and returns a random invalid request for the operation.
    -	**Input:** (1) IDL specification. (2) API operation parameters.
    -	**Output:** Returns a random invalid request.
    -	**Example:**
    
              @Test
                public void one_dep_or() throws IDLException {
                    Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyOr", "get");
                    Map<String, String> invalidRequest = analyzer.getRandomInvalidRequest();
                    assertFalse(analyzer.isValidRequest(invalidRequest), "The request should be NOT  VALID");
                }

    
## **Licence**

