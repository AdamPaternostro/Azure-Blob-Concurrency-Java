# Azure-Blob-Concurrency-Java
Blob concurrency test using Java and Docker

## To compile this code
1. Add com.microsoft.azure.storage
2. Build the JAR file
3. docker build -t javablob .
4. docker run javablob java -jar /app/JavaBlob.jar 40mb.txt
5. docker login
6. docker tag javablob adampaternostro/javablob:latest
7. docker push adampaternostro/javablob:latest


## To run with Shipward on Azure Batch
1. Create an Azure Storage Account
2. Create an Azure Batch Account 
3. Create some different sized blobs and place in your storage account (or use https://www.thinkbroadband.com/download)
4. Test the Python code locally
5. Build the Docker image
6. Upload image to a repository
7. Edit the Shipward config files
8. Issue the below commands to run the process.  You will need to change /home/shipyarduser/pythonblob to where the shipyard config files reside on your disk.

### Get Shipyard Docker image, Create the Batch Pool, Run the Job, Delete the Pool
```
sudo docker pull alfpark/batch-shipyard:cli-latest

sudo docker run --rm -it -v /home/shipyarduser/pythonblob:/configs -e SHIPYARD_CONFIGDIR=/configs alfpark/batch-shipyard:cli-latest pool add

sudo docker run --rm -it -v /home/shipyarduser/pythonblob:/configs -e SHIPYARD_CONFIGDIR=/configs alfpark/batch-shipyard:cli-latest jobs add --tail stdout.txt

sudo docker run --rm -it -v /home/shipyarduser/pythonblob:/configs -e SHIPYARD_CONFIGDIR=/configs alfpark/batch-shipyard:cli-latest pool del
```

## To run the test by hand
#### Create Ubuntu 16.04 TLS

#### Install Docker
```
sudo apt-get -y install apt-transport-https ca-certificates curl
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu  $(lsb_release -cs) stable"  
sudo apt-get update
sudo apt-get -y install docker-ce
sudo docker pull adampaternostro/javablob:latest
```

#### Create a script file
```
nano rundoc.sh
```

#### Paste Contents
```
sudo docker run -d adampaternostro/javablob java -jar /app/JavaBlob.jar 1mb.txt
sudo docker run -d adampaternostro/javablob java -jar /app/JavaBlob.jar 1mb.txt
sudo docker run -d adampaternostro/javablob java -jar /app/JavaBlob.jar 1mb.txt
sudo docker run -d adampaternostro/javablob java -jar /app/JavaBlob.jar 15mb.txt
sudo docker run -d adampaternostro/javablob java -jar /app/JavaBlob.jar 15mb.txt
sudo docker run -d adampaternostro/javablob java -jar /app/JavaBlob.jar 15mb.txt
sudo docker run -d adampaternostro/javablob java -jar /app/JavaBlob.jar 40mb.txt
sudo docker run -d adampaternostro/javablob java -jar /app/JavaBlob.jar 40mb.txt
sudo docker run -d adampaternostro/javablob java -jar /app/JavaBlob.jar 40mb.txt
```

#### Mark executable
```
chmod -x rundoc.sh
```

#### Run
```
sudo bash ./rundoc.sh
```

#### Monitor
```
sudo docker ps
```
