FROM maven:3.8.3-openjdk-11
RUN mkdir -p /usr/src/app
#RUN apt-get update && apt-get install -y libx11-dev
#RUN apt-get update && apt-get install -y libgl1-mesa-glx
RUN apt-get update && \
    apt-get install -y openjfx && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package
COPY appConfig.json /usr/src/app/target
WORKDIR /usr/src/app/target
CMD java -jar client-v1.0-shaded.jar