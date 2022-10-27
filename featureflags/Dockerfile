ARG ENV_TARGET

ARG ARG_AWS_ACCESS_KEY_ID
ARG ARG_AWS_SECRET_ACCESS_KEY

FROM amazoncorretto:11 AS base
WORKDIR /workspace/app
COPY gradle gradle
COPY build.gradle settings.gradle gradlew ./
COPY src src
RUN ./gradlew build -x test
ENV VARBASE=FALSE

FROM base AS env-local
RUN echo "this is the stage that sets VAR=TRUE"
ENV VAR=TRUE

FROM base AS env-dev
RUN echo "this is the stage that sets VAR=TRUE"
ENV VAR=TRUE

FROM base AS env-stg
RUN echo "this is the stage that sets VAR=FALSE"
ENV VAR=FALSE

FROM base AS env-prd
RUN echo "this is the stage that sets VAR=FALSE"
ENV VAR=FALSE

FROM env-${ENV_TARGET} AS final
EXPOSE 8080

ENV AWS_ACCESS_KEY_ID=${ARG_AWS_ACCESS_KEY_ID}
ENV AWS_SECRET_ACCESS_KEY=${ARG_AWS_SECRET_ACCESS_KEY}

ARG JAR_FILE=/workspace/app/build/libs/*.jar
COPY --from=base ${JAR_FILE} /payment.jar
# COPY --from=base /payment.jar /payment.jar
RUN echo "VAR is equal to ${VAR}"
ENTRYPOINT [ "java", "-jar", "/payment.jar" ]

#docker build --build-arg=IMAGE_ONE=debian:7 --build-arg=IMAGE_TWO=debian:8 --build-arg=TAG_ONE=test .
#docker build --build-arg=my_arg=1 .

# FROM 320665947228.dkr.ecr.ap-northeast-2.amazonaws.com/sck-sp-tc-ecr/corretto:11

# MAINTAINER starbucks.co.kr

#### Install some utils ####
#RUN yum install -y iputils bind-utils unzip jq

# install aws-cli v2
#RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" && \
#	unzip awscliv2.zip && \
#	./aws/install

# # To solve ALAS2-2021-1693 vulnerabilities
# RUN yum update -y

# ARG JAR_FILE=/build/libs/*.jar
# COPY ${JAR_FILE} payment.jar

# #Add PG key file and log directory
# ADD ./pg /pgstub
# ENTRYPOINT ["/bin/sh", "-c" , "echo 10.105.48.54 dev-msr.istarbucks.co.kr >> /etc/hosts && exec java -Djava.security.egd=file:/dev/.urandom -Duser.timezone=Asia/Seoul -jar /payment.jar " ]

