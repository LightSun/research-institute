package com.heaven7.ve;

public class MediaResourceItem {

        private String title;
        private long time; //time of photo/video .the last modified
        private String filePath;
        private String mime;
        private int width;
        private int height;

        private float ratio; //height / width in album
        private long duration; //in mill seconds

        public boolean isImage() {
            return mime.startsWith("image");
        }
        public boolean isVideo() {
            return mime.startsWith("video");
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getMime() {
            return mime;
        }

        public void setMime(String mime) {
            this.mime = mime;
        }

        /** in mills */
        public long getTime() {
            return time;
        }
        public void setTime(long time) {
            this.time = time;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public String toString() {
            return "MediaResourceItem{" +
                    ", title='" + title + '\'' +
                    ", time='" + time + '\'' +
                    ", duration='" + duration + '\'' +
                    ", filePath='" + filePath + '\'' +
                    ", mime='" + mime + '\'' +
                    ", width=" + width +
                    ", height=" + height +
                    '}';
        }

        public MediaResourceItem() {
        }
        public MediaResourceItem(MediaResourceItem item) {
            title = item.title;
            time = item.time;
            filePath = item.filePath;
            mime = item.mime;
            width = item.width;
            height = item.height;
            ratio = item.ratio;
            duration = item.duration;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MediaResourceItem item = (MediaResourceItem) o;

            return filePath != null ? filePath.equals(item.filePath) : item.filePath == null;
        }
        @Override
        public int hashCode() {
            return filePath.hashCode();
        }
        /** in mill seconds */
        public void setDuration(long duration) {
            this.duration = duration;
        }
        /** in mill seconds */
        public long getDuration() {
            return duration;
        }

    }