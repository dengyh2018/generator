set MAVEN_OPTS=-server -Ddubbo.registry.file=web.cache -Xms256m -Xmx768m -XX:PermSize=128m -XX:MaxPermSize=256M -Dfile.encoding=UTF-8
mvn spring-boot:run