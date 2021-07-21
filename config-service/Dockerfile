
FROM openjdk:11-jre-slim
LABEL maintainer="husheart@naver.com"

WORKDIR /root

ARG buildDir=build/unpack

COPY ${buildDir}/lib BOOT-INF/lib
COPY ${buildDir}/app .

ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

CMD java org.springframework.boot.loader.JarLauncher
