package demo.qe.util;

import java.util.Arrays;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonNull;
import org.bson.BsonString;

public class SchemaMapper {

    private SchemaMapper() {
    }

    public static BsonDocument getEncryptedFieldsMap() { 
        return new BsonDocument().append("fields",
                    new BsonArray(Arrays.asList(
                        new BsonDocument()
                                .append("keyId", new BsonNull())
                                .append("path", new BsonString("dateOfBirth"))
                                .append("bsonType", new BsonString("date")),
                            new BsonDocument()
                                    .append("keyId", new BsonNull())
                                    .append("path", new BsonString("patientRecord.ssn"))
                                    .append("bsonType", new BsonString("string"))
                                    .append("queries", new BsonDocument()
                                            .append("queryType", new BsonString("equality"))),
                            new BsonDocument()
                                    .append("keyId", new BsonNull())
                                    .append("path", new BsonString("patientRecord.billing"))
                                    .append("bsonType", new BsonString("object")),
                            new BsonDocument()
                                    .append("keyId", new BsonNull())
                                    .append("path", new BsonString("patientRecord.billAmount"))
                                    .append("bsonType", new BsonString("int"))
                                    .append("queries", new BsonDocument()
                                        .append("queryType", new BsonString("range"))
                                        .append("sparsity", new BsonInt32(2))
                                        .append("trimFactor", new BsonInt32(4))
                                        .append("contention", new BsonInt32(5))
                                        .append("min", new BsonInt32(0))
                                        .append("max", new BsonInt32(100000))
                ))));
    }


}
