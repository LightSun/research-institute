import sys
import os
import argument_parser


# cmd: python cmd_proxy.py [ -input=xx -output=xxx ]
#####################
# print("len: ", len(sys.argv))
parser = argument_parser.ArgumentParser()
result = parser.parse(sys.argv)
print("parse result: ", result)
if result == argument_parser.PARSE_SUCCESS:
    cmd_template = parser.get_extra_cmd()
    for index, aio in parser.iterate_arguments():
        cmd = aio.format_template(cmd_template)
        print(cmd + "\n")
        os.system(cmd)

"""
cmd = "python D:\\tensorflow\\youtube-8m\\inference.py 
--output_file=D:\\tmp\\frame_level_logistic_model\\%s --input_data_pattern=\"D:\\%s\" 
--train_dir=D:\\tmp\\frame_level_logistic_model 
--frame_features=True 
--model=FrameLevelLogisticModel 
--feature_names=\"rgb\" 
--feature_sizes=\"1024\"" % (output_file, input_file)

python cmd_proxy.py python+get_face_image.py+%s+%s -input=E:\BaiduNetdiskDownload\taobao_service\照片\女装\真丝吊带裙 -output=E:\BaiduNetdiskDownload\taobao_service\照片\女装\真丝吊带裙
"""
