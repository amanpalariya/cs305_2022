from typing import List, Tuple
from fastapi import FastAPI, File, UploadFile, Form
from zipfile import ZipFile
import os
import io
import tempfile
from pathlib import Path
import random

from soupsieve import match
from database_reader.postgres_database_reader import PostgresDatabaseReader
from entities.face_record import FaceRecord

from entities.person import Person
from entities.image import Image
from face_detector.dlib_face_detector import DlibFaceDetector
from face_detector.face_detector import FaceNotFoundError
from face_storage.no_entry_with_given_id_error import NoEntryWithGivenIdError
from face_storage.postgres_face_storage import PostgresFaceStorage

app = FastAPI()

databaseReader = PostgresDatabaseReader('', '', '')
faceStorage = PostgresFaceStorage(databaseReader)
faceDetector = DlibFaceDetector()

def generate_random_directory_name():
   return ''.join([random.choice('abcdefghijklmnopqrstuvwxyz-') for _ in range(32)])

def merge_matches(matches: List[Tuple[int, Person, float]], k: int):
   person_dict = {}
   for match in matches:
      id, person, similarity = match
      if (person_dict.get(person) is None) or (similarity>person_dict.get(person)[2]):
         person_dict[person] = match
   return sorted(list(person_dict.values()), key=lambda x: -x[2])[:k]

@app.post("/search_faces/")
async def search_faces(k: int = Form(...), confidence: float = Form(...), file: UploadFile =
                      File(..., description="An image file, possible containing multiple human faces.")):
   # return {"status": "OK", "body": {"matches": [{"id": 112, "person_name": "JK Lal"}]}}
   image = Image.from_file(file.file)
   matches = []
   for face_image in faceDetector.get_all_face_images_from_image(image):
      matches += faceStorage.get_top_k_matches(face_image, k, confidence, faceDetector.get_similarity_of_features)
   matches = merge_matches(matches, k)
   match_to_dict = lambda match: {"id": match[0], "person_name": match[1].getName(), "similarity": match[2]}
   return {"status": "OK", "body": {"matches": list(map(match_to_dict, matches))}}


@app.post("/add_face/")
async def add_face(name: str = Form(...), file: UploadFile =
                  File(..., description="An image file having a single human face.")):
   image = Image.from_file(file.file)
   try:
      face_image = faceDetector.create_face_image_from_image(image)
      person =Person(name)
      faceStorage.add_new_face_record(FaceRecord(face_image, person))
   except FaceNotFoundError:
      return {"status": "ERROR", "body": "Could not detect face"}
   return {"status": "OK", "body": "Face added"}


@app.post("/add_faces_in_bulk/")
async def add_faces_in_bulk(file: UploadFile =
                           File(..., description="A ZIP file containing multiple face images.")):
   with ZipFile(io.BytesIO(file.file.read())) as archive:
      tempdir = Path(tempfile.gettempdir())/generate_random_directory_name()
      print(f"Extracting to {tempdir}")
      archive.extractall(tempdir)
      for dir in os.listdir(tempdir):
         if Path(tempdir/dir).is_file():
            continue
         name = dir.replace("_", " ")
         print(f"Adding {name}")
         for image_filename in os.listdir(tempdir/dir):
            try:
                  image = Image.from_filepath(tempdir/dir/image_filename)
                  face_image = faceDetector.create_face_image_from_image(image)
                  person = Person(name)
                  faceStorage.add_new_face_record(FaceRecord(face_image, person))
            except FaceNotFoundError:
                  print(f"Face not found in {image_filename}")
   return {"status": "OK", "body": ""}


@app.post("/get_face_info/")
async def get_face_info(api_key: str = Form(...), face_id: str = Form(...)):
   try:
      id = int(face_id)
      person = faceStorage.get_person_by_id(id)
      return {"status": "OK", "body": {"id": id, "person_name": person.getName()}}
   except ValueError:
      return {"status": "ERROR", "body": f"Cannot parse ID from face_id: \"{face_id}\""}
   except NoEntryWithGivenIdError:
      return {"status": "ERROR", "body": f"No entry with face_id: {face_id}"}
   

