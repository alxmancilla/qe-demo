# Queryable Encryption Demo

This project demonstrates the use of MongoDB's Queryable Encryption feature with a sample medical records application. The application uses various KMS providers to encrypt sensitive data fields and perform queries on the encrypted data.

## Prerequisites

- Java 17
- Maven
- MongoDB instance
- `.env` file with the following variables:
  - `MONGODB_URI`: Your MongoDB connection URI
  - `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_KEY_ARN`, `AWS_KEY_REGION` (for AWS KMS)
  - `AZURE_TENANT_ID`, `AZURE_CLIENT_ID`, `AZURE_CLIENT_SECRET`, `AZURE_KEY_NAME`, `AZURE_KEY_VAULT_ENDPOINT` (for Azure KMS)
  - `GCP_EMAIL`, `GCP_PRIVATE_KEY`, `GCP_PROJECT_ID`, `GCP_LOCATION`, `GCP_KEY_RING`, `GCP_KEY_NAME` (for GCP KMS)
  - `KMIP_KMS_ENDPOINT` (for KMIP KMS)
  - `SHARED_LIB_PATH`: Path to your Automatic Encryption Shared Library

## Setup

1. Clone the repository:
    ```sh
    git clone <repository-url>
    cd <repository-directory>
    ```

2. Create a `.env` file in the root directory with the required environment variables.

3. Build the project using Maven:
    ```sh
    mvn clean install
    ```

## Running the Application

To run the `QueryableEncryptionTutorial` application, use the following command:
```sh
mvn exec:java -Dexec.mainClass="demo.qe.QueryableEncryptionTutorial"
```

## Project Components

- **Models**:
  - `Patient`: Represents a patient with an ID, name, date of birth, and patient record.
  - `PatientBilling`: Represents billing information with card type and card number.
  - `PatientRecord`: Represents a patient's record with SSN, billing information, and bill amount.

- **Utilities**:
  - `QueryableEncryptionHelpers`: Contains helper methods for loading KMS provider credentials, customer master key credentials, and auto-encryption settings.

- **Main Application**:
  - `QueryableEncryptionTutorial`: The main class that sets up the MongoDB client with queryable encryption, creates an encrypted collection, inserts sample documents, and performs queries on the encrypted data.


## License

This project is licensed under the MIT License.