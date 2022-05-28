from flask import Flask
from flask import request
import os
import requests
from bs4 import BeautifulSoup, Tag
import re
import csv
from datetime import datetime, timedelta

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

@app.route("/kursy")
def courses():

    schedule = get_schedule()

    if schedule["status"] != "sukces":
        return schedule

    courses = {
        "status": "sukces",
        "kursy": []
    }

    for lesson in schedule["grafik"]:
        if lesson["kurs"] in courses["kursy"]:
            continue
        courses["kursy"].append(lesson["kurs"])

    courses["kursy"].sort()

    return courses

def get_schedule(day: str=None, time: str=None, course: str=None, level: str=None, notes: str=None, instructor: str=None, enrollment: str=None) -> dict:

    schedule = {
        "status": "sukces",
        "grafik": []
    }

    data = get_from_cache(day, time, course, level, notes, instructor, enrollment)

    if data:
        schedule["grafik"] = data
        return schedule

    try:
        response = requests.get("https://www.dancefusion.com.pl/grafik")

        if response.status_code != 200:
            return get_error(status_code=response.status_code, info="Błąd odpowiedzi serwera")

        bs = BeautifulSoup(response.content, 'html.parser')
        table: Tag = bs.find("table", id="table_1")
        trs: 'list[Tag]' = table.tbody.find_all("tr")

        with open("cache.csv", "w") as cache:
            cache_writer = csv.writer(cache)
            for tr in trs:
                tds: 'list[Tag]' = tr.find_all("td")
                lesson = Lesson(tds)
                cache_writer.writerow(lesson.as_list())

                if not match_filter(lesson, day, time, course, level, notes, instructor, enrollment):
                    continue

                schedule["grafik"].append(lesson.as_dict())
    except requests.exceptions.ConnectionError:
        return get_error(info="Nie można nawiązać połączenia z serwerem")
    except:
        return get_error(info="Nie można sparsować strony")

    return schedule

def get_from_cache(day: str, time: str, course: str, level: str, notes: str, instructor: str, enrollment: str) -> list:
    if not os.path.exists("cache.csv"):
        return None

    cache_mtime = os.path.getmtime("cache.csv")
    now = datetime.now().timestamp()
    delta = timedelta(seconds=(now - cache_mtime))
    hours = delta.seconds // 3600

    if hours >= 1:
        return None

    try:
        data = []
        with open("cache.csv", "r") as cache:
            cache_reader = csv.reader(cache)
            for record in cache_reader:
                lesson = Lesson.from_list(record)

                if not match_filter(lesson, day, time, course, level, notes, instructor, enrollment):
                    continue

                data.append(lesson.as_dict())
        return data
    except:
        return None

def match_filter(lesson: 'Lesson', day: str, time: str, course: str, level: str, notes: str, instructor: str, enrollment: str) -> bool:

    if day:
        day = re.escape(day)
        if not re.match(day, lesson.day, re.IGNORECASE):
            return False

    if time:
        time = re.sub(" ", "", time)
        time = re.escape(time)

        try:
            time = int(time)
            if time < 10:
                time = f"0{time}"
            time = str(time)
        except:
            pass

        if not (re.search(f"{time}:", lesson.time) or re.match(time, re.sub(" ", "", lesson.time))):
            return False

    if course:
        course = re.escape(course)
        if not re.search(course, lesson.course, re.IGNORECASE):
            return False

    if level:
        level = re.escape(level)
        if not re.search(level, lesson.level, re.IGNORECASE):
            return False

    if notes:
        notes = re.escape(notes)
        if not re.search(notes, ";".join(lesson.notes), re.IGNORECASE):
            return False

    if instructor:
        instructor = re.escape(instructor)
        if not re.search(instructor, lesson.instructor, re.IGNORECASE):
            return False

    if enrollment:
        enrollment = re.escape(enrollment)
        if not re.match(enrollment, lesson.enrollment, re.IGNORECASE):
            return False

    return True

def get_error(status_code: int=None, info: str=None) -> dict:
    error = {
        "status": "błąd",
    }

    if status_code is not None:
        error["kod"] = status_code

    if info is not None:
        error["info"] = info

    return error

class Lesson:

    def __init__(self, tds: 'list[Tag]'=None):

        if not tds:
            return

        self.day = tds[1].text
        self.time = tds[2].text
        self.course = tds[3].text.title()
        self.level = tds[4].text
        self.notes = [ s for s in tds[5].strings ]
        self.instructor = tds[7].text
        self.enrollment = " ".join([ s for s in tds[8].strings ])

        if self.enrollment == "ZAPISZ SIĘ":
            self.enrollment = "WOLNE MIEJSCA"

    @staticmethod
    def from_list(_list: list) -> 'Lesson':
        lesson = Lesson()
        lesson.day = _list[0]
        lesson.time = _list[1]
        lesson.course = _list[2]
        lesson.level = _list[3]
        lesson.notes = _list[4][2:-2].split("', '")
        lesson.instructor = _list[5]
        lesson.enrollment = _list[6]

        return lesson

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

    def as_list(self) -> list:
        return [
            self.day,
            self.time,
            self.course,
            self.level,
            self.notes,
            self.instructor,
            self.enrollment
        ]
