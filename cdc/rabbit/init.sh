curl -i -u rabbit:password -H "content-type:application/json" \
    -XPUT -d'{"type":"topic","durable":true}' \
    http://rabbitmq:15672/api/exchanges/%2f/images.cdc.topic