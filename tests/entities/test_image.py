import pytest
from main.entities.image import Image
import numpy as np
import os
from pathlib import Path

@pytest.fixture
def test_image_filepath():
    return 'tests/resources/test.png'

@pytest.fixture
def new_image_filepath():
    return 'tests/resources/new.png'

def test_from_array():
    try:
        Image.from_array(np.array([[[1, 2, 3]]]).astype('uint8'))
        assert True
    except:
        pytest.fail("Cannot create image from array")

def test_from_filepath(test_image_filepath):
    try:
        Image.from_filepath(test_image_filepath)
        assert True
    except:
        pytest.fail("Cannot create image from filepath")

def test_from_file(test_image_filepath):
    try:
        with open(test_image_filepath, 'rb') as file:
            Image.from_file(file)
        assert True
    except:
        pytest.fail("Cannot create image from file")

def test_get_array(test_image_filepath):
    assert type(Image.from_filepath(test_image_filepath).get_array())==np.ndarray

def test_save_image(test_image_filepath, new_image_filepath):
    Image.from_filepath(test_image_filepath).save(new_image_filepath)
    assert Path(new_image_filepath).exists()
    os.remove(new_image_filepath)