from __future__ import print_function
import boto3
import codecs
import json

dynamodb = boto3.resource('dynamodb', region_name='ap-south-1', endpoint_url="http://localhost:8000")

table = dynamodb.Table('orders')

with open("C:/Users/rajes/Downloads/orders1.json", "r", encoding='utf-8_sig') as json_file:
    items = json.load(json_file)
    print(items[5])
    for item in items:
        orderId = item['orderId']
        customerId = item['customerId']
        dateRange = item['dateRange']
        print("Adding detail:", orderId, customerId)
        table.put_item(
        Item={
            'orderId': orderId,
            'customerId': customerId,
            'dateRange': dateRange
        }
    )
        # image_url = item['image_url'] if item['image_url'] else None
        # keywords = item['keywords'] if item['keywords'] else None

    

    