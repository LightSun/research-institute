import sys
import cv2
import face_recognition
import cv_video_helper

# In OpenCV3.X, this is available as cv2.CAP_PROP_POS_MSEC
# In OpenCV2.X, this is available as cv2.cv.CV_CAP_PROP_POS_MSEC
import os

VIDEO_FORMAT = ['.mp4', '.MP4']

def getFace(videofile, tagfile, model='hog'):
    print("tagfile: ", tagfile)
    fileWriter = open(tagfile, "wt")
    vh = cv_video_helper.VideoHelper()

    for num_retrieved, frame in vh.frame_iterator(videofile, every_ms=1000):
        print("num_retrieved: ", num_retrieved)
        # Convert the image from BGR color (which OpenCV uses) to RGB color (which face_recognition uses)
        rgb = frame[:, :, ::-1]

        face_locations = face_recognition.face_locations(rgb, model=model)
        height = rgb.shape[0]
        width = rgb.shape[1]
        fileWriter.write(str(num_retrieved))
        if len(face_locations) > 0:
            fileWriter.write(",")
            first = True
            for face_location in face_locations:
                # Print the location of each face in this image
                top, right, bottom, left = face_location
                # x ,y, width, height
                tem = '%s %s %s %s' % ('%.16f' % (float(left) / width),
                                       '%.16f' % (float(bottom) / height),
                                       '%.16f' % (float(right - left) / width),
                                       '%.16f' % (float(bottom - top) / height))
                if first:
                   fileWriter.write(tem)
                   first = False
                else:
                    fileWriter.write(" " + tem)

        fileWriter.write("\n")
    fileWriter.close()


def printError(msg):
    print(msg, file=sys.stderr)


################### temp_images_dir, outDir, image_count  ##############################
if len(sys.argv) < 3:
    printError("argument is not enough Error!")
else:
    video_file = sys.argv[1].strip()
    outTagfile = sys.argv[2].strip() # if for dir, this is out dir
    # F:\videos\story1\churchOut\character_C0200.mp4 F:\\videos\\story1\\churchOut\\test.csv
    if os.path.isdir(video_file):
        # dir , dir
        for file in os.listdir(video_file):
            file_path = os.path.join(video_file, file)
            if file_path.startswith("."):
                continue
            if not os.path.isfile(file_path):
                continue
            extension = os.path.splitext(file_path)[1]  # get the extension of file
            if extension not in VIDEO_FORMAT:
                continue
            filename = os.path.basename(file_path).split(".")[0]
            abs_path = '%s/%s_rects.csv' % (outTagfile, filename)
            getFace(file_path, abs_path)
    else:
        # video file, dir
        filename = os.path.basename(video_file).split(".")[0]
        abs_path = '%s/%s_rects.csv' % (outTagfile, filename)
        getFace(video_file, abs_path)
