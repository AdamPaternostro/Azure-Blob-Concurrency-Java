# Azure-Blob-Concurrency-Java
Blob concurrency test using Java and Docker


## To run the test by hand
#### Create Ubuntu 16.04 TLS

#### Install Docker
sudo apt-get -y install apt-transport-https ca-certificates curl
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu  $(lsb_release -cs) stable"  
sudo apt-get update
sudo apt-get -y install docker-ce
sudo docker pull adampaternostro/javablob:latest

#### Create a script file
nano rundoc.sh

#### Paste Contents
sudo docker run -d adampaternostro/javablob java -jar /app/JavaBlob.jar 1mb.txt
sudo docker run -d adampaternostro/javablob java -jar /app/JavaBlob.jar 1mb.txt
sudo docker run -d adampaternostro/javablob java -jar /app/JavaBlob.jar 1mb.txt
sudo docker run -d adampaternostro/javablob java -jar /app/JavaBlob.jar 15mb.txt
sudo docker run -d adampaternostro/javablob java -jar /app/JavaBlob.jar 15mb.txt
sudo docker run -d adampaternostro/javablob java -jar /app/JavaBlob.jar 15mb.txt
sudo docker run -d adampaternostro/javablob java -jar /app/JavaBlob.jar 40mb.txt
sudo docker run -d adampaternostro/javablob java -jar /app/JavaBlob.jar 40mb.txt
sudo docker run -d adampaternostro/javablob java -jar /app/JavaBlob.jar 40mb.txt

#### Mark executable
chmod -x rundoc.sh

#### Run
sudo bash ./rundoc.sh

#### Monitor
sudo docker ps
