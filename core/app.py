from flask import Flask
from flask import request
import os
import requests
from bs4 import BeautifulSoup, Tag

app = Flask(__name__)

@app.route("/")
def index():
    # http://127.0.0.1:5000/?dzien=&godziny=&kurs=&poziom=&uwagi=&instruktor=&zapisy=
    
    day = request.args.get("dzien")
    time = request.args.get("godziny")
    course = request.args.get("kurs")
    level = request.args.get("poziom")
    notes = request.args.get("uwagi")
    instructor = request.args.get("instruktor")
    enrollment = request.args.get("zapisy")

    if os.getenv("FLASK_ENV") == "development":
        print(f"dzien={day}")
        print(f"godziny={time}")
        print(f"kurs={course}")
        print(f"poziom={level}")
        print(f"uwagi={notes}")
        print(f"instruktor={instructor}")
        print(f"zapisy={enrollment}")
    
    return get_schedule(day, time, course, level, notes, instructor, enrollment)

def get_schedule(day: str, time: str, course: str, level: str, notes: str, instructor: str, enrollment: str) -> dict:

    response = requests.get("https://www.dancefusion.com.pl/grafik/")

    if response.status_code != 200:
        return get_error(response.status_code)

    bs = BeautifulSoup(response.content, 'html.parser')
    table: Tag = bs.find("table", id="table_1")
    trs: list[Tag] = table.tbody.find_all("tr")
    
    schedule = {
        "status": "sukces",
        "grafik": []
    }

    for tr in trs:
        tds: list[Tag] = tr.find_all("td")
        lesson = Lesson(tds)
        schedule["grafik"].append(lesson.as_dict())
    
    return schedule

def get_error(status_code: int) -> dict:
    error = {
        "status": "błąd",
    }

    if status_code is not None:
        error["kod"] = status_code

    return error

class Lesson:

    def __init__(self, tds: list[Tag]):
        self.day = tds[1].text
        self.time = tds[2].text
        self.course = tds[3].text
        self.level = tds[4].text
        self.notes = [ s for s in tds[5].strings ]
        self.instructor = tds[7].text
        self.enrollment = " ".join([ s for s in tds[8].strings ])

        if self.enrollment == "ZAPISZ SIĘ":
            self.enrollment = "WOLNE MIEJSCA"

    def as_dict(self) -> dict:
        return {
            "dzien": self.day,
            "godziny": self.time,
            "kurs": self.course,
            "poziom": self.level,
            "uwagi": self.notes,
            "instruktor": self.instructor,
            "zapisy": self.enrollment
        }
