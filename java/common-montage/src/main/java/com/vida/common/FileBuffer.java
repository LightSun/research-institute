package com.vida.common;

import java.io.*;

/**
 * the file buffer used to cache a buffer and later write to file
 *
 * @author heaven7
 */
public class FileBuffer {

    /**
     * the limit size of file
     */
    private static final int LIMIT_SIZE = 4 * 1024 * 1024; //4 mb
    private final StringBuilder sb = new StringBuilder();
    private final String mDir;
    private final String mBaseFilename;
    private int mOrder;

    /**
     * the flush size of buffer to file
     */
    private int mFlushSize = 4 * 1024;
    /**
     * the file limit size
     */
    private int mFileLimitSize = LIMIT_SIZE;
    /**
     * already write size of current file
     */
    private int mWriteSize;

    /**
     * create file buffer
     *
     * @param dir      the dir of file
     * @param filename the file name .which contains extension.
     */
    public FileBuffer(String dir, String filename) {
        this.mDir = dir;
        this.mBaseFilename = filename;
        File file = new File(dir);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                System.err.println("mkdirs failed. path = " + dir);
            }
        } else {
            assert file.isDirectory();
        }
        assert filename.contains(".");
    }

    public void clearBuffer(){
        sb.setLength(0);
    }

    /**
     * set the flush size .which used to flush to file. default is '4KB'
     *
     * @param mFlushSize the flush size
     */
    public void setFlushSize(int mFlushSize) {
        this.mFlushSize = mFlushSize;
    }

    /**
     * set limit size of every file
     *
     * @param mFileLimitSize the file limit size
     */
    public void setFileLimitSize(int mFileLimitSize) {
        this.mFileLimitSize = mFileLimitSize;
    }

    public boolean write(String str) {
        sb.append(str);
        if (!writeIfFull()) {
            return false;
        }
        return true;
    }

    public boolean writeNewline(String str) {
        return write(str + "\n");
    }

    /**
     * force flush buffer to the file without limit
     *
     * @return true if ok . false if exception occurs.
     */
    public boolean forceFlush() {
        final int length = sb.length();
        if (length == 0) {
            return true;
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter(getFilePath(), true);
            writer.write(sb.toString());
            writer.flush();
            mWriteSize += length;
            if (mWriteSize >= mFileLimitSize) {
                mWriteSize = 0;
                mOrder++;
            }
            sb.setLength(0);
        } catch (IOException e) {
            return false;
        } finally {
            IOUtils.closeQuietly(writer);
        }
        return true;
    }

    /**
     * just write the buffer to target writer.
     *
     * @param writer the out writer
     * @param close  true to close the write after write.
     * @return true if write success . or else failed.
     */
    public boolean writeTo(Writer writer, boolean close) {
        try {
            writer.write(sb.toString());
            writer.flush();
        } catch (IOException e) {
            return false;
        } finally {
            if (close) {
                IOUtils.closeQuietly(writer);
            }
        }
        return true;
    }

    public boolean readFrom(Reader reader, boolean close) {
        BufferedReader br = reader instanceof BufferedReader ?
                (BufferedReader) reader : new BufferedReader(reader);
        try {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                writeIfFull();
            }
        } catch (IOException e) {
            return false;
        } finally {
            if (close) {
                IOUtils.closeQuietly(br);
            }
        }
        return true;
    }

    /**
     * write to file if full
     *
     * @return true if write success.
     */
    private boolean writeIfFull() {
        final int length = sb.length();
        if (length == 0) {
            return true;
        }
        if (length >= mFlushSize) {
            String filePath = getFilePath();
            FileWriter writer = null;
            try {
                writer = new FileWriter(filePath, true);
                writer.write(sb.toString());
                writer.flush();
                mWriteSize += length;
                if (mWriteSize >= mFileLimitSize) {
                    mWriteSize = 0;
                    mOrder++;
                }
                //clear buffer
                sb.setLength(0);
            } catch (IOException e) {
                return false;
            } finally {
                IOUtils.closeQuietly(writer);
            }
        }
        return true;
    }

    private String getFilePath() {
        int index = mBaseFilename.indexOf(".");
        String filename = mBaseFilename.substring(0, index);
        String extension = mBaseFilename.substring(index + 1);
        return mDir + File.separator + filename + "__" + mOrder + "." + extension;
    }

}
