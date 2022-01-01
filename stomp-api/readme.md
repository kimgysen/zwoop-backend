
Run rabbitmq on docker locally: 

Pull container:  

https://github.com/quangthe/docker-rabbitmq-stomp

    docker pull rabbitmq 
    docker container run -it --name rabbitmq-stomp -p 15672:15672 -p 5672:5672 -p 61613:61613 pcloud/rabbitmq-stomp:3

Run admin panel: 

    http://localhost:15672/

with credentials: 

    user: admin 
    password: admin  

