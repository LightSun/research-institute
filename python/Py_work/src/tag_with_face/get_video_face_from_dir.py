import time
import sys
import face_recognition
import os

IMG_FORMATS = ['.jpg', '.jpeg', '.png', '.JPG', '.JPEG', '.PNG']


# for face no need urlEncode
def writeFaceData(imgFile, index, fileWriter, model='hog'):
    print(imgFile + ", time = ", index)
    rgb = face_recognition.load_image_file(imgFile)
    face_locations = face_recognition.face_locations(rgb, model=model)
    height = rgb.shape[0]
    width = rgb.shape[1]
    fileWriter.write(str(index))
    if face_locations:
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

def getTimeInMills():
    return int(round(time.time() * 1000))


def printError(msg):
    print(msg, file=sys.stderr)


#################################################

# MAIN support gen for multi image files which all from one video.
# py simple-video-filename input-dir outDir img-format bit-count

print("args: len = ", len(sys.argv))
# print("0000%d" % 1)


def getTemplate(left):
    if left == 1:
        return "0%d"
    elif left == 2:
        return "00%d"
    elif left == 3:
        return "000%d"
    elif left == 4:
        return "0000%d"
    else:
        raise NotImplementedError("called getTemplate(). left = " + left)

def formatNumber(i, bit_count):
    if bit_count != 5:
        raise RuntimeError("current only support 5 bit for img")
    size = len(str(i))
    tem = getTemplate(bit_count - size)
    return tem % i

if len(sys.argv) < 6:
    printError(" Error! argument is not enough.")
else:
    filename = sys.argv[1].strip()
    inputDir = sys.argv[2].strip()
    outDir = sys.argv[3].strip()
    img_format = sys.argv[4].strip()
    bit_count = int(sys.argv[5].strip())

    # filename_rects.csv
    csv_name = '%s_rects.csv' % filename
    csvPath = '%s\%s' % (outDir, csv_name)
    # delete if exists
    if os.path.exists(csvPath):
        os.remove(csvPath)
    csvWriter = open(csvPath, "wt")

    count = 0
    for file in os.listdir(inputDir):
        file_path = os.path.join(inputDir, file)
        if not os.path.isfile(file_path):
            continue
        extension = os.path.splitext(file_path)[1]  # get the extension of file
        if extension not in IMG_FORMATS:
            continue
        count += 1
    # get faces
    for i in range(0, count):
        param = formatNumber(i + 1, bit_count)
        file_path = os.path.join(inputDir, img_format % param)
        # write face data
        writeFaceData(file_path, i, csvWriter, model='hog')
    csvWriter.close()
