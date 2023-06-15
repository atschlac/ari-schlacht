package edu.yu.cs.com1320.project.stage5.impl;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.PersistenceManager;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * created by the document store and given to the BTree via a call to BTree.setPersistenceManager
 */
public class DocumentPersistenceManager implements PersistenceManager<URI, Document> {
    File baseDir;

    public DocumentPersistenceManager(File baseDir)
    {
        if(baseDir != null)
            this.baseDir = baseDir;
        else
            this.baseDir = new File(System.getProperty("user.dir"));
    }

    @Override
    public void serialize(URI uri, Document val) throws IOException
    {
        Gson gson = new Gson();

        JsonSerializer<Document> jsonSerializer = new JsonSerializer<Document>()
        {
            @Override
            public JsonElement serialize(Document currentDoc, Type typeOfSrc, JsonSerializationContext context)
            {
                JsonObject jsonDoc = new JsonObject();
                //Add the uri
                jsonDoc.addProperty("uri", currentDoc.getKey().toString());

                //Add the wordCountMap + Add either the text or binary[]
                if (currentDoc.getDocumentTxt() != null)
                {
                    Gson gson = new Gson();
                    jsonDoc.addProperty("text", currentDoc.getDocumentTxt());
                    // add the HashMap to the JsonObject
                    JsonObject mapObject = new JsonObject();
                    for (Map.Entry<String, Integer> entry : currentDoc.getWordMap().entrySet())
                    {
                        mapObject.addProperty(entry.getKey(), entry.getValue());
                    }
                    jsonDoc.add("wordCountMap", mapObject);
                }
                else
                {
                    String encodedBytes = Base64.getEncoder().encodeToString(currentDoc.getDocumentBinaryData());
                    jsonDoc.addProperty("bytes", encodedBytes);
                }
                return jsonDoc;
            }
        };
        JsonElement jsonElement = jsonSerializer.serialize(val, null, null);
        String jsonDoc = gson.toJson(jsonElement);

        //add jsonString to disk

        // create a FileWriter object to write to a file
        String uriPath = this.getURIPath(uri);
        File filePath = new File(this.baseDir, uriPath + ".json"); // Add ".json" to the end of the file name

        // Create any necessary directories
        filePath.getParentFile().mkdirs();

        // Write to that file
        FileWriter fileWriter = new FileWriter(filePath);
        fileWriter.write(jsonDoc);
        //System.out.println("Successfully wrote JSON file to " + filePath.getAbsolutePath());

        // close the FileWriter object
        fileWriter.close();
    }

        private String getURIPath(URI currentURI)
        {
            String uriString = "";
            if(currentURI.getScheme() != null)
            {
                uriString += "/" + currentURI.getHost() + currentURI.getPath();
            }
            else
            {
                uriString += currentURI.toString();
            }
            return uriString;
        }

    @Override
    public Document deserialize(URI uri) throws IOException
    {
        Gson gson = new Gson();
        JsonDeserializer<Document> jsonDeserializer = new JsonDeserializer<Document>()
        {
            @Override
            public Document deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
            {
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                // Extract the URI field from the JSON object
                String uriString = jsonObject.get("uri").getAsString();
                URI uri = null;
                try
                {
                    uri = new URI(uriString);
                } catch (URISyntaxException e)
                {
                    throw new RuntimeException(e);
                }

                // Extract the wordCountMap field from the JSON object
                // Extract the text or bytes field from the JSON object
                HashMap<String, Integer> wordMap = null;
                String text = null;
                byte[] bytes = null;

                if (jsonObject.has("text"))
                {
                    text = jsonObject.get("text").getAsString();
                    Gson gson = new Gson();
                    // get the mapObject from the JsonObject
                    JsonObject mapObject = jsonObject.getAsJsonObject("wordCountMap");
                    // deserialize the map back to a HashMap<String, Integer>
                    wordMap = new Gson().fromJson(mapObject, new TypeToken<HashMap<String, Integer>>() {}.getType());

                }

                else if (jsonObject.has("bytes")) {
                    String encodedBytes = jsonObject.get("bytes").getAsString();
                    bytes = Base64.getDecoder().decode(encodedBytes);
                }


                //Make the doc
                DocumentImpl currentDoc = null;
                //System.out.println(wordMap);
                // Create a new Document object with the extracted values
                if (text != null)
                    currentDoc = new DocumentImpl(uri, text, wordMap);
                else if (bytes != null)
                    currentDoc = new DocumentImpl(uri, bytes);

                return currentDoc;
            }
        };

        // specify the file path + create a FileReader object to read the file
        String uriPath = this.getURIPath(uri);
        File filePath = new File(baseDir, uriPath + ".json"); // Add ".json" to the end of the file name

        // create a BufferReader object to read the file
        BufferedReader reader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
        {
            stringBuilder.append(line);
        }
        String fileContent = stringBuilder.toString();
        reader.close();

        JsonElement jsonElement = gson.fromJson(fileContent, JsonElement.class);

        DocumentImpl currentDoc = (DocumentImpl) jsonDeserializer.deserialize(jsonElement, null, null);

        return currentDoc;
    }

    @Override
    public boolean delete(URI uri) throws IOException
    {
     // create a FileWriter object to write to a file
      String uriPath = this.getURIPath(uri);
      File fileToDelete = new File(baseDir, uriPath + ".json"); // Add ".json" to the end of the file name

      return fileToDelete.delete();
    }
}
