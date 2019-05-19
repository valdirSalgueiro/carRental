FROM rabbitmq:3.6.12-management-alpine

RUN rabbitmq-plugins enable --offline rabbitmq_management