import urllib, json
import calendar
import time
import asyncio
import concurrent.futures
import aiohttp

import pymysql
import pymysql.cursors

interval=120
days=2
url="https://bittrex.com/api/v1.1/public/getticker?market="
ts = calendar.timegm(time.gmtime())  

def dbconnect():
    try:
        db = pymysql.connect(
            host='127.0.0.1',
            user='cointrade',
            passwd='cointrade',
            db='cointrade'
        )
    except Exception as e:
        sys.exit("Can't connect to database")
    return db


def getMarkets():
    markets =[]   
    cursor = db.cursor()  
    try:  
        cursor.execute("SELECT market_name FROM markets")
        row = cursor.fetchone()
        while row is not None:
            markets.append(row[0])
            row = cursor.fetchone()
    except Exception as e:
        print (e)
    finally:    
        if cursor:
            cursor.close()
    return markets

def deleteOldData():
    cursor = db.cursor()  
    try:  
        cursor.execute("delete from market_prices where timestamp<"+str(ts-days*86400))
        db.commit()
    except Exception as e:
        print (e)
    finally:    
        if cursor:
            cursor.close()
            
async def get(market):
    conn = aiohttp.TCPConnector(limit=100)
    async with aiohttp.ClientSession(connector=conn) as session:
        async with session.get(url+market , verify_ssl=False) as response:
            addToDb(market,await response.json())



def addToDb(market,data):
    cursor = db.cursor()  
    try :
        values=[market,
                data['result']['Bid'],
                data['result']['Ask'], 
                data['result']['Last'], 
                ts-ts%interval]
        cursor.execute("""
        INSERT INTO market_prices(market_name,bid,ask,last,timestamp)\
        VALUES (%s,%s,%s,%s,%s) """, values)
        db.commit()
    except Exception as e:
            if "Duplicate" not in str(e):
                print(url+market)
                print(e)
    finally :
        if cursor:
            cursor.close()

    
db = dbconnect()
deleteOldData()
markets= getMarkets()

for i in  range(int(len(markets)/100)+1):
    loop = asyncio.get_event_loop()
    tasks = [
        asyncio.ensure_future(get(market))
        for market in markets[i*100:(i+1)*100]
        ]
    loop.run_until_complete(asyncio.wait(tasks))

                  
