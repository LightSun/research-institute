package com.faendir.rhino_android;

import android.content.Context;
import android.os.Environment;

import com.faendir.rhino_android.utils.AssetsFileCopyUtils;
import com.heaven7.java.base.util.IOUtils;

import org.mozilla.javascript.GeneratedClassLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by heaven7 on 2019/3/18.
 */
public class ZipFileTest {

    private static final String TAG = "ZipFileTest";
    private String srcPath = "jar/aop.jar";
    private String dstPath = Environment.getExternalStorageDirectory() + "/vida/jar/aop.jar";
    private final Context context;
    private final GeneratedClassLoader classLoader;

    public ZipFileTest(Context context) {
        this.context = context;
        this.classLoader = new AndroidContextFactory(context.getCacheDir())
                .createClassLoader(getClass().getClassLoader());
    }
    //read class file from jar.then invoke.
    public void readAndInvoke(){
        AssetsFileCopyUtils.copy(context, srcPath, dstPath, false);
        String name = "com/heaven7/java/jartest/Test";
        InputStream in = null;
        ArchivePathElement ape = null;
        try {
            ape = new ArchivePathElement(new ZipFile(dstPath));
            in = ape.open(name + ".class");
            byte[] bytes = read(in);
            in.close();
            Class<?> clazz = classLoader.defineClass(name.replace("/", "."), bytes);
            Method method = clazz.getMethod("main", String[].class);
            method.invoke(null, (Object) new String[]{"heaven7"});
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(in);
            if(ape != null){
                try {
                    ape.close();
                } catch (IOException e) {
                }
            }
        }
    }
    public static byte[] read(InputStream in) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        do {
            int count = in.read(buffer);
            if(count == -1){
                break;
            }
            baos.write(buffer, 0, count);
        }while (true);
        return baos.toByteArray();
    }
    /**
     * An element of the class path in which class files can be found.
     */
    interface ClassPathElement {

        char SEPARATOR_CHAR = '/';

        /**
         * Open a "file" from this {@code ClassPathElement}.
         * @param path a '/' separated relative path to the wanted file.
         * @return an {@code InputStream} ready to read the requested file.
         * @throws IOException if the path can not be found or if an error occurred while opening it.
         */
        InputStream open(String path) throws IOException;

        void close() throws IOException;

        Iterable<String> list();

    }

    static class ArchivePathElement implements ClassPathElement {

        static class DirectoryEntryException extends IOException {
        }
        private final ZipFile archive;

        public ArchivePathElement(ZipFile archive) {
            this.archive = archive;
        }

        @Override
        public InputStream open(String path) throws IOException {
            ZipEntry entry = archive.getEntry(path);
            if (entry == null) {
                throw new FileNotFoundException("File \"" + path + "\" not found");
            } else if (entry.isDirectory()) {
                throw new DirectoryEntryException();
            } else {
                return archive.getInputStream(entry);
            }
        }

        @Override
        public void close() throws IOException {
            archive.close();
        }

        @Override
        public Iterable<String> list() {
            return new Iterable<String>() {

                @Override
                public Iterator<String> iterator() {
                    return new Iterator<String>() {
                        Enumeration<? extends ZipEntry> delegate = archive.entries();
                        ZipEntry next = null;

                        @Override
                        public boolean hasNext() {
                            while (next == null && delegate.hasMoreElements()) {
                                next = delegate.nextElement();
                                if (next.isDirectory()) {
                                    next = null;
                                }
                            }
                            return next != null;
                        }

                        @Override
                        public String next() {
                            if (hasNext()) {
                                String name = next.getName();
                                next = null;
                                return name;
                            } else {
                                throw new NoSuchElementException();
                            }
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException();
                        }
                    };
                }
            };
        }
    }
    static class FolderPathElement implements ClassPathElement {

        private final File baseFolder;

        public FolderPathElement(File baseFolder) {
            this.baseFolder = baseFolder;
        }

        @Override
        public InputStream open(String path) throws FileNotFoundException {
            return new FileInputStream(new File(baseFolder,
                    path.replace(SEPARATOR_CHAR, File.separatorChar)));
        }

        @Override
        public void close() {
        }

        @Override
        public Iterable<String> list() {
            ArrayList<String> result = new ArrayList<String>();
            collect(baseFolder, "", result);
            return result;
        }

        private void collect(File folder, String prefix, ArrayList<String> result) {
            for (File file : folder.listFiles()) {
                if (file.isDirectory()) {
                    collect(file, prefix + SEPARATOR_CHAR + file.getName(), result);
                } else {
                    result.add(prefix + SEPARATOR_CHAR + file.getName());
                }
            }
        }

    }


}
