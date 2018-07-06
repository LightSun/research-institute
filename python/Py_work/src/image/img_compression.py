# /home/user/Downloads/datas/jpg_compression.py
from PIL import Image
import os
import sys

# Define images type to detect
valid_file_type = ['.jpg','.jpeg']
# Define compression ratio
SIZE_normal = 1.0
SIZE_small = 1.5
SIZE_more_small = 2.0
SIZE_much_more_small = 3.0

def make_directory(directory):
    """Make dir"""
    os.makedirs(directory)


def directory_exists(directory):
    """If this dir exists"""
    if os.path.exists(directory):
        return True
    else:
        return False


def list_img_file(directory):
    """List all the files, choose and return jpg files"""
    old_list = os.listdir(directory)
    # print old_list
    new_list = []
    for filename in old_list:
        f, e = os.path.splitext(filename)
        if e in valid_file_type:
            new_list.append(filename)
        else:
            pass
    # print new_list
    return new_list


def print_help():
    print("""
    This program helps compress many image files
    you can choose which scale you want to compress your img(jpg/etc)
    1) normal compress(4M to 1M around)
    2) small compress(4M to 500K around)
    3) smaller compress(4M to 300K around)
    4) much smaller compress(4M to ...)
    """)


def compress(choose, src_dir, des_dir, file_list):
    """Compression Algorithm,img.thumbnail"""
    if choose == '1':
        scale = SIZE_normal
    if choose == '2':
        scale = SIZE_small
    if choose == '3':
        scale = SIZE_more_small
    if choose == '4':
        scale = SIZE_much_more_small
    for infile in file_list:
        filename = os.path.join(src_dir, infile)
        img = Image.open(filename)
        # size_of_file = os.path.getsize(infile)
        w, h = img.size
        img.thumbnail((int(w/scale), int(h/scale)))
        img.save(des_dir + '/' + infile)


if __name__ == "__main__":
    src_dir, des_dir = sys.argv[1], sys.argv[2]
    if directory_exists(src_dir):
        if not directory_exists(des_dir):
            make_directory(des_dir)
        # business logic
        file_list = list_img_file(src_dir)
        # print file_list
        if file_list:
            print_help()
            # choose = raw_input("enter your choice:") # py2
            choose = input("enter your choice:")       # py3
            compress(choose, src_dir, des_dir, file_list)
        else:
            pass
    else:
        print("source directory not exist!")