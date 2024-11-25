# API REST - IDV-WEB4

## Description
The objective of this API is to be able to manage the features of the site.

## Requirements
node `>=16.13.1`
npm `>=8.1.2`

---

## Running Unit Tests for the API

To ensure the API is functioning correctly and to validate the implemented features, unit tests are included in the project. Follow these steps to run the tests:

### Prerequisites
- Make sure you have the required versions of `Java` and `Maven` installed, as the project uses Spring Boot.
- Verify that you have set up the project and dependencies correctly.

### Steps to Run the Tests
1. Navigate to the root directory of the project (where the `pom.xml` file is located).
2. Use the following Maven command to run all tests:
   ```bash
   mvn test
   ```

### Test Report
- After running the tests, a summary will be displayed in the terminal indicating how many tests passed or failed.

If you encounter any issues, ensure that the environment variables and database configurations (if any) are properly set up for testing.

---

## MacOS, Linux and Windows Installation

Start the api in the `quest_web_jar` folder like this:
```bash
java -jar quest_web-0.0.1-SNAPSHOT.jar
```

Once the Spring API started, you can start the frontend with:
```bash
npm install
npm start
```