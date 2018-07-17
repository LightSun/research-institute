import cv2
import sys


def isPython2():
    (major_ver, minor_ver, subminor_ver) = (cv2.__version__).split('.')
    return int(major_ver) < 3


class VideoHelper(object):

    def __init__(self):
        if isPython2():
           # self.CAP_PROP_POS_MSEC = cv2.cv.CV_CAP_PROP_POS_MSEC
            self.CAP_PROP_FRAME_COUNT = cv2.cv.CAP_PROP_FRAME_COUNT
            self.CAP_PROP_POS_FRAMES = cv2.cv.CAP_PROP_POS_FRAMES
            self.CAP_PROP_FPS = cv2.cv.CAP_PROP_FPS
        else:
           # self.CAP_PROP_POS_MSEC = cv2.CV_CAP_PROP_POS_MSEC
            self.CAP_PROP_FRAME_COUNT = cv2.CAP_PROP_FRAME_COUNT
            self.CAP_PROP_POS_FRAMES = cv2.CAP_PROP_POS_FRAMES
            self.CAP_PROP_FPS = cv2.CAP_PROP_FPS

    def getFrameCount(self, video_capture):
        return video_capture.get(self.CAP_PROP_FRAME_COUNT)

    def getFrameRate(self, video_capture):
        return video_capture.get(self.CAP_PROP_FPS)

    def getFrameStep(self, every_ms, video_capture):
        rate = self.getFrameRate(video_capture)
        return rate * every_ms / 1000

    def frame_iterator(self, filename, every_ms=1000):
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
        step = self.getFrameStep(every_ms, video_capture)
        frame_pos = 0
        num_retrieved = 0

        while True:
            video_capture.set(self.CAP_PROP_POS_FRAMES, frame_pos)
            has_frames, frame = video_capture.read()
            if not has_frames:
                break
            # cv2.imwrite('image_'+ str(num_retrieved) + '.jpg',frame)
            yield num_retrieved, frame
            frame_pos += step
            num_retrieved += 1
        video_capture.release()
