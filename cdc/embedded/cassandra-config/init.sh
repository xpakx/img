#!/usr/bin/env bash

echo "Creating keyspace and table..."
cqlsh cassandra -u admin -p admin -e "CREATE KEYSPACE IF NOT EXISTS images_cassandra WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};"
cqlsh cassandra -u admin -p admin -e "CREATE TABLE IF NOT EXISTS images_cassandra.event (id uuid, name text, user_id bigint, PRIMARY KEY(id));"
