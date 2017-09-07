package com.paternostro;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;
import com.microsoft.azure.storage.table.*;

// To run via command line
// java -jar JavaBlob.jar
// docker build -t javablob .
// docker run javablob java -jar /app/JavaBlob.jar 40mb.txt

// docker login
// docker tag javablob adampaternostro/javablob:latest
// docker push adampaternostro/javablob:latest

public class Main {

    public static void main(String[] args) {

        String blobName = "1mb.txt";
        System.out.println("args.length: " + String.valueOf(args.length));
        for (int i = 0; i < args.length; i++)
        {
            System.out.println("args[" + String.valueOf(i) + "]: " + String.valueOf(args[i]));
        }

        // args.length: 4
        // args[0]: java
        // args[1]: -jar
        // args[2]: /app/JavaBlob.jar
        // args[3]: 40mb.txt
        // blobName: 1mb.txt

        if (args.length == 1)
        {
            blobName = args[0];
        }
        if (args.length == 4)
        {
            blobName = args[3];
        }
        System.out.println("blobName: " + blobName);

        // Define the connection-string with your values
        String storageConnectionString =
                "DefaultEndpointsProtocol=http;" +
                        "AccountName=samplebase;" +
                        "AccountKey=<<REMOVED>>";

        String storageConnectionString2 =
                "DefaultEndpointsProtocol=http;" +
                        "AccountName=adamshipyardstorage;" +
                        "AccountKey=<<REMOVED>>";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS");

        try {
            // Retrieve storage account from connection-string.
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

            RetryExponentialRetry retryExponentialRetry  = new RetryExponentialRetry(50,10);

            BlobRequestOptions blobRequestOptions= new BlobRequestOptions();
            blobRequestOptions.setRetryPolicyFactory(retryExponentialRetry);

            TableRequestOptions tableRequestOptions= new TableRequestOptions();
            tableRequestOptions.setRetryPolicyFactory(retryExponentialRetry);

            // Create the blob client.
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
            blobClient.setDefaultRequestOptions(blobRequestOptions);

            // Retrieve reference to a previously created container.
            CloudBlobContainer container = blobClient.getContainerReference("concurrencytest");
            CloudBlob blob = container.getBlockBlobReference(blobName);

            CloudStorageAccount storageAccount2 = CloudStorageAccount.parse(storageConnectionString2);
            CloudTableClient tableClient = storageAccount2.createCloudTableClient();
            tableClient.setDefaultRequestOptions(tableRequestOptions);
            CloudTable table = tableClient.getTableReference("TimingEntry");
            table.createIfNotExists();

            Timestamp startTime;
            Timestamp stopTime;
            long diffMilliseconds;
            boolean insert = false;
            TableOperation insertTimingEntry;
            int loop = 0;

            //# Run for a certain amount of time (5 minutes for 300 seconds)
            Timestamp endLoop = new Timestamp(System.currentTimeMillis() + 300 * 1000);
            while (endLoop.getTime() - System.currentTimeMillis() > 0) {
                startTime = new Timestamp(System.currentTimeMillis());
                blob.download(new java.io.FileOutputStream(blob.getName()));
                stopTime = new Timestamp(System.currentTimeMillis());

                System.out.println(sdf.format(startTime));
                System.out.println(sdf.format(stopTime));

                diffMilliseconds = stopTime.getTime() - startTime.getTime();
                System.out.println(diffMilliseconds);

                TimingEntry timingEntry = new TimingEntry(sdf.format(startTime));

                timingEntry.setFileName(blobName);
                timingEntry.setStartTime(sdf.format(startTime));
                timingEntry.setStopTime(sdf.format(stopTime));
                timingEntry.setElapsedTime(String.valueOf(diffMilliseconds));

                insertTimingEntry = TableOperation.insert(timingEntry);
                table.execute(insertTimingEntry);
            }
        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
        }
    }
}
