# payment-service-food-order
The PaymentService in food ordering microservice allows the user to make payment for their orders. The order event is communicated from Order Service to Payment Service using Kafka. The microservice consumes Expiration event, which consits of information of expiration of order and hence cancels the order.
