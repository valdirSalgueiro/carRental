#!/usr/bin/env python
import pika
import mysql.connector

db = mysql.connector.connect(
  host="localhost",
  user="root",
  passwd="root",
  database="rental",
  auth_plugin='mysql_native_password'
)

connection = pika.BlockingConnection(
    pika.ConnectionParameters(host='localhost'))
	
channel = connection.channel()
channel.queue_declare(queue='hello')


def callback(ch, method, properties, body):
    print(" [x] Received %r" % body)
    cursor = db.cursor()
    data = body.split(",")
    cursor.execute("insert into remaining (customer, days) VALUES('"+data[1]+"','"+data[0]+"')")
    db.commit()


channel.basic_consume(
    queue='rental-message-queue', on_message_callback=callback, auto_ack=True)

print(' [*] Waiting for messages. To exit press CTRL+C')
channel.start_consuming()