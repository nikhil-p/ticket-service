# ticket-service
Assumptions
1. git client and java already installed


To checkout the project do the following
1. git clone https://github.com/nikhil-p/ticket-service.git
2. cd into ticket-service
3. run ./gradlew build
4. The above command compiles the classes, runs tests and creates a jar under build/libs.

Handling user errors by throwing runtime exceptions.

To start using the service
put the jar under your applications classpath, create an instance of TicketServiceImpl  by passing the following params
     * totalRows            - total the event space has.
     * maxSeatsPerRow       - maxseats under each row.
     * maxHoldTimeInSeconds - max time in seconds to hold the reservation.
     
 




