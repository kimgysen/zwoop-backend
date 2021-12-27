
Run rabbitmq on docker locally: 

Pull container:  

https://hub.docker.com/_/rabbitmq

    docker pull rabbitmq 
    docker run --rm -it -p 15672:15672 -p 5672:5672 rabbitmq:3-management

Run admin panel: 

    http://localhost:15672/

with credentials: 

    user: guest 
    password: guest 

