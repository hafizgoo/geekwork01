package com.hafigoo.geek;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 自定义一个 Classloader，加载Hello.xlass 文件，执行 hello 方法
 */
public class HellClassLoad extends ClassLoader {


    public static void main(String[] args) throws Exception {

        final String className = "Hello";
        final String methodName = "hello";
        Class<?> helloClass = new HellClassLoad().findClass(className);
        Method helloMethod = helloClass.getMethod(methodName);
        helloMethod.invoke(helloClass.newInstance());
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String filePath = name.replace(".", "/");
        String postfix = ".xlass";
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filePath + postfix);
        byte[] hellobytes = null;
        try {
            hellobytes = HellClassLoad.toByteArray(ins);
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        } finally {
            close(ins);
        }
        for (int i = 0; i < hellobytes.length; i++) {
            hellobytes[i] = (byte) (255 - hellobytes[i]);
        }
        return defineClass(name, hellobytes, 0, hellobytes.length);
    }

    /**
     * 将inputstream输入流转换成byte[]字节数组
     *
     * @param input
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    // 关闭流
    private static void close(Closeable res) {
        if (null != res) {
            try {
                res.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}




