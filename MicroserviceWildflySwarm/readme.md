Para executar o projeto

mvn clean wildfly-swarm:run



Para empacotar o projeto

mvn clean package -Pdocker

Para iniciar o projeto

sudo docker run -e CONSUL_SERVER="http://172.17.0.1:8500" -e DATABASE_URI="jdbc:sqlserver://172.17.0.1:1433;DatabaseName=rec" -e DATABASE_USER="tenant2" -e DATABASE_PASSWORD="tenant212345@!" microservice





Quando o wildfly swarm inicia, ele busca as configurações no arquivo project-defaults.yml