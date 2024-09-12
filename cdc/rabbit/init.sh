#!/usr/bin/env bash
until curl -u rabbit:password -f -s http://rabbitmq:15672/api/overview >/dev/null; do 
    sleep 5;
    echo "Waiting for rabbitMQ management API...";
done

echo "Creating CDC topic..."
curl -i -u rabbit:password -H "content-type:application/json" \
    -XPUT -d'{"type":"topic","durable":true}' \
    http://rabbitmq:15672/api/exchanges/%2f/images.cdc.topic
