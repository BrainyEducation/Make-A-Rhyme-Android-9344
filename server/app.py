from flask import *
import time
import random

app = Flask(__name__)

base63 = "abcdefghijkmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ2346789"
base = len(base63)

def toBase(num):
	res = []
	while num > 0:
		t = ""
		for i in range(4):
			t = t + base63[num%base]
			num = num//base
		res.append(t)
	return "-".join(res)

def adjust(uuid):
	return uuid.replace("0","O").replace("1","I").replace("l","I").replace("5","S").replace("-","")

@app.route('/')
def generateID():
	uuid = toBase(int(time.time()*1000)) + "-"+toBase(int(random.random()*base**4))
	open("data/"+("".join([i for i in uuid if i in base63])), 'w+').close()
	return uuid
	

@app.route('/<uuid>/data.txt')
def read(uuid):
	try:
		return send_from_directory('data',adjust(uuid))
	except:
		return "Request failed"

@app.route('/<uuid>/<data>')
def write(uuid, data):
	try:
		sanitized = adjust("".join([i for i in uuid if i in base63]))
		open("data/"+sanitized).close()
		with open("data/"+sanitized, 'a') as f:
			f.write(data)
			f.write('\n')
		return "success"
	except:
		return "Request failed"

if __name__ == '__main__':
	app.run(debug=True, host='127.0.0.1', port=8000)
