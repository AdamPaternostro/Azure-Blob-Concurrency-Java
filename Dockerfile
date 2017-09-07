FROM alpine:3.6

# Default to UTF-8 file.encoding
ENV LANG C.UTF-8

RUN { \
		echo '#!/bin/sh'; \
		echo 'set -e'; \
		echo; \
		echo 'dirname "$(dirname "$(readlink -f "$(which javac || which java)")")"'; \
	} > /usr/local/bin/docker-java-home \
	&& chmod +x /usr/local/bin/docker-java-home
ENV JAVA_HOME /usr/lib/jvm/java-1.8-openjdk
ENV PATH $PATH:/usr/lib/jvm/java-1.8-openjdk/jre/bin:/usr/lib/jvm/java-1.8-openjdk/bin

ENV JAVA_VERSION 8u131
ENV JAVA_ALPINE_VERSION 8.131.11-r2

RUN set -x \
	&& apk add --no-cache \
		openjdk8="$JAVA_ALPINE_VERSION" \
	&& [ "$JAVA_HOME" = "$(docker-java-home)" ]

# Python code    
WORKDIR /app
ADD JavaBlob.jar /app
ADD azure-storage-3.1.0.jar /app
ADD commons-lang3-3.4.jar /app
ADD jackson-core-2.6.0.jar /app
ADD slf4j-api-1.7.12.jar /app

# Comment out for Shipyard
# CMD ["1mb.txt"]