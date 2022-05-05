from flask import Flask
from flask import request
import os
import json

app = Flask(__name__)

@app.route("/")
def index():
    # http://127.0.0.1:5000/?dzien=&godziny=&instruktor=&kurs=&poziom=&uwagi=&zapisy=
    
    dzien = request.args.get("dzien")
    godziny = request.args.get("godziny")
    instruktor = request.args.get("instruktor")
    kurs = request.args.get("kurs")
    poziom = request.args.get("poziom")
    uwagi = request.args.get("uwagi")
    zapisy = request.args.get("zapisy")

    if os.getenv("FLASK_ENV") == "development":
        print(f"dzien={dzien}")
        print(f"godziny={godziny}")
        print(f"instruktor={instruktor}")
        print(f"kurs={kurs}")
        print(f"poziom={poziom}")
        print(f"uwagi={uwagi}")
        print(f"zapisy={zapisy}")
    
    return get_schedule(dzien, godziny, instruktor, kurs, poziom, uwagi, zapisy)

def get_schedule(dzien: str, godziny: str, instruktor: str, kurs: str, poziom: str, uwagi: str, zapisy: str) -> dict:
    
    # Return example response
    with open("../response.example.json", "r") as f:
        schedule = json.load(f)
    
    return schedule
