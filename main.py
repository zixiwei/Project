# This is a sample Python script.
import zipfile
import os
import shutil
import sys

import scenedetect
from scenedetect import split_video_ffmpeg

if len(sys.argv) > 1:
    input_path: str = sys.argv[1]
    path_scene: str = sys.argv[2]
else:
    exit(1)


def file_name_generator(level):
    if level == 0:
        return "$VIDEO_NAME - Scene $SCENE_NUMBER.mp4"
    elif level == 1:
        return "$VIDEO_NAME - Shot $SCENE_NUMBER.mp4"
    elif level == 2:
        return "$VIDEO_NAME - Subshot $SCENE_NUMBER.mp4"


def result_generator(directory_path, input_file, threshold, level):
    # if the directory exists ,delete it and all contents
    try:
        shutil.rmtree(directory_path)
        print(f"Directory {directory_path} and all its contents have been deleted successfully.")
    except OSError as e:
        print(f"deleting directory")
    # Use the os.makedirs() function to create the directory.
    os.makedirs(directory_path)
    # Check if the directory was created successfully.
    if not os.path.isdir(directory_path):
        print(f"Directory '{directory_path}' created successfully.")
    output_directory = os.path.join(directory_path, file_name_generator(level))
    detector = scenedetect.detectors.content_detector.ContentDetector(threshold=threshold)
    scene_list = scenedetect.detect(input_file, detector, stats_file_path=os.path.join(directory_path, "result.csv"))
    split_video_ffmpeg(
        input_file,
        scene_list=scene_list,
        output_file_template=output_directory,
        show_progress=True
    )


result_generator(path_scene, input_path, 63, 0)

for filename in enumerate(os.listdir(path_scene)):
    if filename[1].endswith(".mp4"):
        path_shot = os.path.join(path_scene, filename[1])
        # Check if the file is a file (not a directory)
        if os.path.isfile(path_shot):
            # Perform any operation on the file
            print(f"Processing file {path_shot}")
            path_shot_result = os.path.join(path_scene, str(filename[0] + 1)) + "\\"
            result_generator(path_shot_result, path_shot, 27, 1)
            for file_name in enumerate(os.listdir(path_shot_result)):
                if file_name[1].endswith(".mp4"):
                    path_sub_shot = os.path.join(path_shot_result, file_name[1])
                    if os.path.isfile(path_sub_shot):
                        print(f"Processing file {path_sub_shot}")
                        path_sub_shot_result = os.path.join(path_shot_result, str(file_name[0] + 1)) + "\\"
                        result_generator(path_sub_shot_result, path_sub_shot, 14, 2)

with zipfile.ZipFile("output", mode='w') as zip_file:
    # Walk through the directory and compress all the files and subdirectories
    for root, dirs, files in os.walk(path_scene):
        for file in files:
            zip_file.write(os.path.join(root, file))
