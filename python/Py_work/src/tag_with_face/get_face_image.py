import time
import sys
import face_recognition
import os

IMG_FORMATS = ['.jpg', '.jpeg', '.png', '.JPG', '.JPEG', '.PNG']

# for face no need urlEncode
def writeFaceData(imgFile, fileWriter, model='hog'):
    rgb = face_recognition.load_image_file(imgFile)
    face_locations = face_recognition.face_locations(rgb, model=model)
    height = rgb.shape[0]
    width = rgb.shape[1]
    fileWriter.write(imgFile)
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


# single file
def getFace(imgFile, tagfile, model='hog'):
    # print("tagfile: ", tagfile)
    fileWriter = open(tagfile, "wt")
    writeFaceData(imgFile, fileWriter, model)
    fileWriter.close()

def getTimeInMills():
    return int(round(time.time() * 1000))

def printError(msg):
    print(msg, file=sys.stderr)
#################################################
print("args: len = ", len(sys.argv))
if len(sys.argv) < 3:
    printError(" Error! argument is not enough.")
else:
    img_file = sys.argv[1].strip()
    outTagfile = sys.argv[2].strip()  # if img_file is dir . this is dir
    
    if os.path.isdir(img_file):
        if not os.path.exists(outTagfile):
            os.makedirs(outTagfile)
        # face_time.csv
        csv_name = 'face_%s_rects.csv' % str(getTimeInMills())
        csvPath = '%s\%s' % (outTagfile, csv_name)
        # face_config.txt
        faceConfigWriter = open('%s%sface_config.txt' % (outTagfile, "\\"), "at")
        # delete if exists
        if os.path.exists(csvPath):
            os.remove(csvPath)
        csvWriter = open(csvPath, "wt")

        first = True
        for file in os.listdir(img_file):
            file_path = os.path.join(img_file, file)
            if file_path.startswith("."):
                continue
            if not os.path.isfile(file_path):
                continue
            extension = os.path.splitext(file_path)[1] # get the extension of file
            if extension not in IMG_FORMATS:
                continue
            # write face data
            writeFaceData(file_path, csvWriter, model='hog')
            # write config file.
            if first:
                faceConfigWriter.write(csvPath)
                faceConfigWriter.write(",")
                faceConfigWriter.write(file_path)
                first = False
            else:
                faceConfigWriter.write(" " + file_path)

        faceConfigWriter.write("\n")
        faceConfigWriter.close()
        csvWriter.close()
    else:
        getFace(img_file, outTagfile)
