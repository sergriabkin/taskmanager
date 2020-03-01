FROM java:8
WORKDIR /
ADD target/elontask-0.0.2.jar //
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/elontask-0.0.2.jar"]
