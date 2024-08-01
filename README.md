# RestAssured Test Suite for Automate API Requests and Verify Responses Task by Jon Paulo Ojon
## Overview
This test suite includes automated API tests for testing Order ESIMs And Verify Details

## Author
Jon Paulo Ojon

## Prerequisites
* Java: (v22 recommended), find installer on https://www.oracle.com/java/technologies/downloads/ site
* Maven: (optional)
* IDE: Optional but recommended (e.g., IntelliJ IDEA, Eclipse).

## Installation
1. Clone the repository and go to project directory
- git clone https://github.com/jp-ojon/apitestautoesimjon.git
- change directory to root folder apitestautoesimjon

## Project Structure
- src/test/java: Contains the test code.
- src/test/resources: Contains test resources (e.g., configuration files, testdata).
- pom.xml: Maven build configuration file.
- testng.xml: Configuration of tests

## Test Cases
- Test Case 1: Test Order ESIMs And Verify Details. Submit an order and verify details on the ESIMs list and ESIMs package history.

## Test Data
- Please update the testdata.csv file under testdata folder accordingly. 

Sample .csv test data
packageid,type,data,quantity,price,currency,description
merhaba-7days-1gb,sim,1 gb,6,4.5,usd,example description

packageid,type,data,quantity,price,currency,description
change-7days-1gb,sim,1 gb,3,4.5,Usd,example description

packageid,type,data,quantity,price,currency,description
guay-mobile-30days-20gb,sim,20 GB,12,26,USD,example description

## Running Tests
Use the following commands in any terminal or cmd line to run tests
1. mvnw.cmd test    : run all tests for Windows, no Maven installation needed
2. ./mvnw test      : run all tests for Unix/Linux/Mac, no Maven installation needed
3. mvn test         : run all tests for any OS, Maven installation required

## Configuration
**Important Information**
- Create a config.properties file under src/test/resources. A sample config.example.properties is provided as template.
- On the created config.properties, enter API Key and API Secret. Just paste it straight, no quotes and no spaces.

* Example:
* apiKey=12345abc
* apiSecret=6789xyz

## Links to Documentation
- RestAssured: https://rest-assured.io/
- TestNG: https://testng.org/
- Maven: https://maven.apache.org/guides/index.html