/*
 * Copyright 2021 chami.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.minofhealth.sendjsondata;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;

/**
 *
 * @author chami
 */
public class SendJsonApi {

    public static void main(String[] args) throws IOException {

        String myDirectoryPath = "C:\\Users\\chami\\Desktop\\Synthea Dataset\\fhir\\";
        File dir = new File(myDirectoryPath);
        File[] directoryListing = dir.listFiles();

        if (directoryListing != null) {
            for (File child : directoryListing) {

                System.out.println("File name : " + child);

                String fileData = readFile(child, StandardCharsets.UTF_8);
                //System.out.println(readFile);

                jsonSend(fileData);
                // Do something with child
            }
        } else {
            // Handle the case where dir is not really a directory.
            // Checking dir.isDirectory() above would not be sufficient
            // to avoid race conditions with another process that deletes
            // directories.
        }

    }

    public static void jsonSend(String jsonString) throws MalformedURLException, IOException {

        URL url = new URL("http://localhost:8080/fhir/");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/fhir+json");
        con.setRequestProperty("accept", "application/fhir+json");
        con.setDoOutput(true);

        String jsonInputString = jsonString;
        //System.out.println("Json String : " + jsonString);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = con.getResponseCode();
        String responseMessage = con.getResponseMessage();

        System.out.println("Respose code : " + responseCode);
        System.out.println("Respose message: " + responseMessage);

    }

    static String readFile(File file, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(file + ""));
        return new String(encoded, encoding);
    }

}
