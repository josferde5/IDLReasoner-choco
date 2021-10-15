# IDLReasoner-choco
## IDLReasoner-choco: 
IDLReasoner-choco is a Java tool that enables analysis of IDL specifications automatically and performs several analysis operations on them.

## Index:

1. [IDL Specification]()
2. [Quickstart guide]()
3. [How to use it?]()
4. [Analysis operations]()
5. [Licence]()


## IDL Specification:
## Quickstart guide:
To get started with IDLReasoner-choco, download the code and move to the parent directory:

        git clone https://github.com/ssegura/IDLReasoner-choco.git
        cd IDLReasoner-choco

**Installing local dependencies:**
IDLReasoner-choco relies on idl-choco.jar library located in the lib/ folder. To install it, simply run the following command from the parent directory.

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

    -	Input: (1) IDL specification. (2) API operation parameters. (3) Request to check.
    -	Output: True if the request is valid, otherwise false.
    -	Example:
    
            @Test
                public void one_dep_or_valid() throws IDLException {
                    Analyzer analyzer = new OASAnalyzer("oas", "./src/test/resources/OAS_test_suite.yaml", "/oneDependencyOr", "get");
                    Map<String, String> request = new HashMap<>();
                    request.put("p1", "true");
                    assertTrue(analyzer.isValidRequest(request), "The request should be VALID");  
                }

- **isValidPartialRequest:** This operation is analogous to the previous one but the input request is partial or incomplete, meaning that some other parameters should still be included to make it a full valid request.

    -	**Input:** (1) IDL specification. (2) API operation parameters. (3) Request to check.
    -	**Output: **True if the request is partially valid, otherwise false.
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

