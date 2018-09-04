from PIL import Image
import face_recognition

# Load the jpg file into a numpy array
# file = "E:\\test\\lfw\\lfw\\Abdul_Majeed_Shobokshi\\Abdul_Majeed_Shobokshi_0001.jpg"
# file = "E:\\test\\lfw\\lfw\\Abdel_Nasser_Assidi\\Abdel_Nasser_Assidi_0001.jpg"
# file = "E:\\test\\lfw\\lfw\\Abdel_Aziz_Al-Hakim\\Abdel_Aziz_Al-Hakim_0001.jpg"
# file = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\黑色满襟衫\\1-11.jpg"
file = "F:\\videos\\ClothingWhite\\temp\\LM0A0199\\img_00001.jpg"
image = face_recognition.load_image_file(file)

print("shape: ", image.shape)
print("shape: ", image.shape[0]) # height
print("shape: ", image.shape[1]) # width

# Find all the faces in the image using the default HOG-based model.
# This method is fairly accurate, but not as accurate as the CNN model and not GPU accelerated.
# See also: find_faces_in_picture_cnn.py
face_locations = face_recognition.face_locations(image)

print("I found {} face(s) in this photograph.".format(len(face_locations)))

for face_location in face_locations:

    # Print the location of each face in this image
    top, right, bottom, left = face_location
    print("A face is located at pixel location Top: {}, Left: {}, Bottom: {}, Right: {}".format(top, left, bottom, right))

    # You can access the actual face itself like this:
    face_image = image[top:bottom, left:right]
    pil_image = Image.fromarray(face_image)
    pil_image.show()
