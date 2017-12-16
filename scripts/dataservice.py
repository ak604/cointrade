import urllib, json
import MySQLdb

interval=300
pricemul=1000000
def dbconnect():
    try:
        db = MySQLdb.connect(
            host='cointrade.cq3ccmglzjnt.us-east-2.rds.amazonaws.com',
            user='cointrade',
            passwd='cointrade',
            db='cointrade'
        )
    except Exception as e:
        sys.exit("Can't connect to database")
    return db

def insertDb(data):
    try:
        db = dbconnect()
        for i in range(len(data)):
            try :
                cursor = db.cursor()
                timestamp = long(data[i]['last_updated'])
                values=[data[i]['id'], 
                    long(pricemul*float(data[i]['price_usd'])),
                    timestamp-timestamp%interval]
                cursor.execute("""
                INSERT INTO coin_prices(coin_id,price,timestamp)\
                VALUES (%s,%s,%s) """, values)
                cursor.close()
                db.commit()
            except Exception as e:
                print e
            finally :
                if cursor:
                    cursor.close()
    except Exception as e:
        print e
    finally:    
            if db:    
                db.close()

url="https://api.coinmarketcap.com/v1/ticker/"
response = urllib.urlopen(url)
data = json.loads(response.read())
insertDb(data)
             
                  
