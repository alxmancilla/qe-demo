package demo.qe;

import com.mongodb.AutoEncryptionSettings;
import com.mongodb.ClientEncryptionSettings;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.CreateEncryptedCollectionParams;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.vault.ClientEncryption;
import com.mongodb.client.vault.ClientEncryptions;
import demo.qe.models.Patient;
import demo.qe.models.PatientBilling;
import demo.qe.models.PatientRecord;
import demo.qe.util.DataGenerator;
import demo.qe.util.QueryableEncryptionHelpers;
import demo.qe.util.SchemaMapper;

import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class QueryableEncryptionTutorial {
    public static void main(String[] args) throws Exception {
        // start-setup-application-variables
        String kmsProviderName = "local";

        String uri = QueryableEncryptionHelpers.getEnv("MONGODB_URI"); // Your connection URI
        System.out.println("MONGODB_URI: " + uri);

        String keyVaultDatabaseName = "encryption";
        String keyVaultCollectionName = "__keyVault";
        String keyVaultNamespace = keyVaultDatabaseName + "." + keyVaultCollectionName;
        String encryptedDatabaseName = "medicalRecords";
        String encryptedCollectionName = "patients";
        // end-setup-application-variables

        // start-setup-application-pojo
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        // end-setup-application-pojo

        Map<String, Map<String, Object>> kmsProviderCredentials = QueryableEncryptionHelpers.getKmsProviderCredentials(kmsProviderName);
        BsonDocument customerMasterKeyCredentials = QueryableEncryptionHelpers.getCustomerMasterKeyCredentials(kmsProviderName);
       // System.out.println("customerMasterKeyCredentials: " + customerMasterKeyCredentials);
        AutoEncryptionSettings autoEncryptionSettings = QueryableEncryptionHelpers.getAutoEncryptionOptions(keyVaultNamespace, kmsProviderCredentials);

        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .autoEncryptionSettings(autoEncryptionSettings)
                .build();

        try (MongoClient encryptedClient = MongoClients.create(clientSettings)) {
            encryptedClient.getDatabase(keyVaultDatabaseName).getCollection(keyVaultCollectionName).drop();
            encryptedClient.getDatabase(encryptedDatabaseName).getCollection(encryptedCollectionName).drop();

            BsonDocument encryptedFieldsMap = SchemaMapper.getEncryptedFieldsMap();

            // start-client-encryption
            ClientEncryptionSettings clientEncryptionSettings = ClientEncryptionSettings.builder()
                    .keyVaultMongoClientSettings(MongoClientSettings.builder()
                            .applyConnectionString(new ConnectionString(uri))
                            .build())
                    .keyVaultNamespace(keyVaultNamespace)
                    .kmsProviders(kmsProviderCredentials)
                    .build();
            ClientEncryption clientEncryption = ClientEncryptions.create(clientEncryptionSettings);
            // end-client-encryption

            // start-create-encrypted-collection
            CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions().encryptedFields(encryptedFieldsMap);

            CreateEncryptedCollectionParams encryptedCollectionParams = new CreateEncryptedCollectionParams(kmsProviderName);
            encryptedCollectionParams.masterKey(customerMasterKeyCredentials);

            try {
                clientEncryption.createEncryptedCollection(
                        encryptedClient.getDatabase(encryptedDatabaseName),
                        encryptedCollectionName,
                        createCollectionOptions,
                        encryptedCollectionParams);
            } 
            // end-create-encrypted-collection
            catch (Exception e) {
                throw new Exception("Unable to create encrypted collection due to the following error: " + e.getMessage());
            }

            // start-insert-document
            MongoDatabase encryptedDb = encryptedClient.getDatabase(encryptedDatabaseName).withCodecRegistry(pojoCodecRegistry);
            MongoCollection<Patient> collection = encryptedDb.getCollection(encryptedCollectionName, Patient.class);
            
            List<Patient> patients = new LinkedList<Patient>();
            Patient patient10th = null;
            int counter= 100;
            for (int i=0; i<100; i++){
                PatientBilling patientBilling = new PatientBilling("Visa", DataGenerator.genCCN());
                PatientRecord patientRecord = new PatientRecord(DataGenerator.genSSN(), patientBilling, 400 + (i%10)*(i));
                Patient patientDocument = new Patient(DataGenerator.genFullName(), patientRecord);
                patients.add(patientDocument);
            }
            patient10th = patients.get(10);
            System.out.println("patient10th: " + patient10th);


            InsertManyResult result = collection.insertMany(patients);
            // end-insert-document
            if (result.wasAcknowledged()) {
                System.out.println("Successfully inserted 100 documents.");
            }

            // start-find-document
            Patient findResult = collection.find(
                new BsonDocument()
                        .append("patientRecord.ssn", new BsonString(patient10th.getPatientRecord().getSsn())))
                        .first();
             
            System.out.println(findResult);
            // end-find-document

            // start-query-range
                Document filter = new Document("patientRecord.billAmount",
                new Document("$gt", patient10th.getPatientRecord().billAmount - 10).append("$lt", patient10th.getPatientRecord().billAmount + 10));
                findResult = collection.find(filter).first();

                System.out.println(findResult);
                // end-query-range

        }
    }
}

