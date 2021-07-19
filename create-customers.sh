#!/usr/bin/env bash

echo "***********************************"
echo "Creating Salesforce Customers..."
echo "***********************************"

curl -X POST 'http://localhost:8080/customers' \
-H 'Content-Type: application/json' \
-d '{
    "username": "${your_email_1}",
    "clientId": "${your_client_id_1}"
}'

curl -X POST 'http://localhost:8080/customers' \
-H 'Content-Type: application/json' \
-d '{
    "username": "${your_email_2}",
    "clientId": "${your_client_id_2}"
}'
