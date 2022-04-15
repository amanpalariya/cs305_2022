# CS305 - Software Engineering

This repository contains code for assignments of CS305 - Software Engineering 2022.

- **Name**: Aman Palariya
- **Entry number**: 2019csb1068

## Organization
The code is split into 2 folders - `main` and `tests`.
`main` folder contains the program while the `tests` folder contains tests.

The program is divided into 5 modules
- `book_metadata_reader` for reading metadata from input
- `book_metadata_writer` for saving the metadata to a file
- `entities` for common dataclasses
- `libraries` for processing different filetypes (e.g. excel, PDF)
- `table_writer` for writing data in tabular form

## Compilation
Before running the program, install the required modules by running
```sh
pip3 install -r ./requirements.txt
```

To run the program, use
```sh
python3 app.py -h
```
This will print the help. Follow instructions in the help to run the program.

To test the program, use
```sh
coverage run --source main -m pytest tests
```

Then, check the coverage by running
```sh
coverage report
```