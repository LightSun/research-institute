import sys
import cv2
import face_recognition

# In OpenCV3.X, this is available as cv2.CAP_PROP_POS_MSEC
# In OpenCV2.X, this is available as cv2.cv.CV_CAP_PROP_POS_MSEC
import os

CAP_PROP_POS_MSEC = 0


def frame_iterator(filename, every_ms=1000):
    """Uses OpenCV to iterate over all frames of filename at a given frequency.

    Args:
      filename: Path to video file (e.g. mp4)
      every_ms: The duration (in milliseconds) to skip between frames.
    # max_num_frames: Maximum number of frames to process, taken from the
        beginning of the video.

    Yields:
      RGB frame with shape (image height, image width, channels)
    """
    print("every_ms: ", every_ms)
    video_capture = cv2.VideoCapture()
    if not video_capture.open(filename):
        print('Error: Cannot open video file ' + filename, file=sys.stderr)
        return
    last_ts = -99999  # The timestamp of last retrieved frame.
    num_retrieved = 0

    while True:
        # Skip frames
        while video_capture.get(CAP_PROP_POS_MSEC) < every_ms + last_ts:
            if not video_capture.read()[0]:
                return

        last_ts = video_capture.get(CAP_PROP_POS_MSEC)
        has_frames, frame = video_capture.read()
        if not has_frames:
            break
        # cv2.imwrite('image_'+ str(num_retrieved) + '.jpg',frame)
        yield num_retrieved, frame
        num_retrieved += 1


def getFace(videofile, tagfile, model='hog'):
    # print("tagfile: ", tagfile)
    fileWriter = open(tagfile, "wt")
    for num_retrieved, rgb in frame_iterator(videofile, every_ms=1000):
        print("num_retrieved: ", num_retrieved)
        face_locations = face_recognition.face_locations(rgb, model=model)
        height = rgb.shape[0]
        width = rgb.shape[1]
        fileWriter.write(str(num_retrieved))
        if (face_locations):
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


#################################################
if len(sys.argv) < 2:
    printError("argument is not enough Error!")
else:
    video_file = sys.argv[1].strip()
    if (len(sys.argv) >= 3):
        outTagfile = sys.argv[2].strip()
    else:
        outTagfile = ""
    # F:\videos\story1\churchOut\character_C0200.mp4 F:\\videos\\story1\\churchOut\\test.csv
    if os.path.isdir(video_file):
        for file in os.listdir(video_file):
            file_path = os.path.join(video_file, file)
            if file_path.startswith("."):
                continue
            tmp = os.path.splitext(file_path)[0]
            getFace(file_path, tmp + ".csv")
    else:
        if len(sys.argv) < 3:
             printError("for single file , need a out path, Error!")
        getFace(video_file, outTagfile)
