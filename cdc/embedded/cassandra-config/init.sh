#!/usr/bin/env bash
until printf "" 2>>/dev/null >>/dev/tcp/cassandra/9042; do 
    sleep 5;
    echo "Waiting for cassandra...";
done

echo "Creating keyspace and table..."
cqlsh cassandra -u admin -p admin -e "CREATE KEYSPACE IF NOT EXISTS images_cassandra WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};"
cqlsh cassandra -u admin -p admin -e "CREATE TABLE IF NOT EXISTS images_cassandra.event (id uuid, name text, user_id bigint, created_at timestamp, PRIMARY KEY((user_id), created_at, id)) WITH CLUSTERING ORDER BY (created_at DESC);"
