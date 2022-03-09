# CS305 - Software Engineering

This repository contains code for assignments of CS305 - Software Engineering 2022.

- **Name**: Aman Palariya
- **Entry number**: 2019csb1068

## Working
The code is divided into 2 top-level directories - `main` and `test`.
`main` directory contains the actual code, while `test` contains the corresponding tests.

The main code is divided into four directories - `database_reader`, `entities`, `face_detector`, and `face_storage`.

`database_reader` contains
- `SqlDatabaseReader`: An abstract class for reading SQL databases
- `PostgresDatabaseReader`: A realization of `SqlDatabaseReader` for reading Postgres databases. It uses the `psycopg2` library.

`entities` contains simple data classes
- `Person`: The details of a person (for now it contains only the name of the person)
- `Image`: For reading, and saving image
- `FaceImage`: For storing the image and encoding of the face in the image
- `FaceRecord`: For storing a face image and corresponding person

`face_detector` contains
- `FaceDetector`: An abstract class for finding the encoding of faces from an image
- `FaceNotFoundError`: Raised when no face is found in an image
- `DlibFaceDetector`: A realization of `FaceDetector` using the `face_recognition` library

`face_storage` contains
- `FaceStorage`: An abstract class for storing and retrieving `FaceRecord`
- `PostgresFaceStorage`: A realization of `FaceStorage` that uses Postgres database for persistence

## Decisions
To make the storage and retrieval of facial data efficient, only the encoding is stored in the database.
Since Postgres supports ARRAY datatype, it is the best choice for the assignment.
The "distance" of two faces is the Euclidean distance of their encodings, and in this project the "similarity" has be defined as "1 - distance".
To calculate the distance in Postgres, a function `double precision get_distance(array1 double precision[], array2 double precision[])` has been created in Postgres.
This helps in efficient storage and search of images.

## Compilation

**Note**: The Postgres database should have CUBE extension. To create CUBE extension, use the database and run `create extension cube` in `psql`.

To run the app, first, setup the configuration in `app.py` and install FastAPI (https://fastapi.tiangolo.com/).
Then execute

```sh
uvicorn app:app --reload
```

To test the app, change the configuration in `app.py` and install `pytest` and `coverage`
Then execute

```sh
coverage run --source main -m pytest tests
```

Then, check the coverage by running

```sh
coverage report
```

For adding faces in bulk, the zip file should have folder at level 0 and images inside those folders.
The name of the folder will be taken as the name of the person in the images inside those folders.