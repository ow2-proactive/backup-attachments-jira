package org.objectweb.proactive.core.util.converter;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;

import org.objectweb.proactive.core.mop.Utils;
import org.objectweb.proactive.core.util.ProActiveRandom;


public class TestFastDeepCopy {

    public static void main(String[] args) throws Exception {
        HashMap<String, URI> map = new HashMap<String, URI>();
        for (int i = 0; i < 100; i++) {
            byte[] buf = new byte[ProActiveRandom.nextInt(30)];
            ProActiveRandom.nextBytes(buf);
            String key = byteArrayToHexString(buf);

            buf = new byte[ProActiveRandom.nextInt(30)];
            ProActiveRandom.nextBytes(buf);
            String data = byteArrayToHexString(buf);
            URI value = URI.create("http://localhost/" + data);

            map.put(key, value);
        }

        long before;

        for (int i = 0; i < 10000; i++) {
            HashMap<String, URI> copy = (HashMap<String, URI>) Utils.makeDeepCopy(map);
        }

        before = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            HashMap<String, URI> copy = (HashMap<String, URI>) Utils.makeDeepCopy(map);
        }

        System.out.println("MOP took " + (System.currentTimeMillis() - before) + " ms");
        
        
        for (int i = 0; i < 10000; i++) {
            HashMap<String, URI> copy = deepCopy(map);
        }

        System.out.println("took TEEEEEEEST BEGIN ");

        before = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            HashMap<String, URI> copy = deepCopy(map);
        }
        System.out.println("Piped took " + (System.currentTimeMillis() - before) + " ms");

      
        System.exit(0);
    }

    static public <T> T deepCopy(T o) throws Exception {
        final FastByteArrayOutputStream fbos = new FastByteArrayOutputStream(32*1024);
        final ObjectOutputStream out = new ObjectOutputStream(fbos);
        out.writeObject(o);
        out.flush();
        out.close();

        final ObjectInputStream in = new ObjectInputStream(fbos.getInputStream());
        return (T)in.readObject();

    }

    final public static class FastByteArrayOutputStream extends OutputStream {
        private byte[] buf;
        private int size;

        public FastByteArrayOutputStream() {
            this(56 * 1024);
        }

        public FastByteArrayOutputStream(int initSize) {
            this.size = 0;
            this.buf = new byte[initSize];
        }

        final public int getSize() {
            return size;
        }

        final public byte[] getByteArray() {
            return buf;
        }

        public final void write(byte b[], int off, int len) {
            if ((off < 0) || (off > b.length) || (len < 0) ||
                    ((off + len) > b.length)) {
                    throw new IndexOutOfBoundsException();
                } else if (len == 0) {
                    return;
                }
                int newcount = size + len;
                if (newcount > buf.length) {
                    buf = Arrays.copyOf(buf, Math.max(buf.length << 2, newcount));
                }
                System.arraycopy(b, off, buf, size, len);
                size = newcount;
        }

        public final void write(int b) {
            int newcount = size + 1;
            if (newcount > buf.length) {
                buf = Arrays.copyOf(buf, Math.max(buf.length << 2, newcount));
            }
            buf[size] = (byte)b;
            size = newcount;
        }

        final public void reset() {
            size = 0;
        }

        final public InputStream getInputStream() {
            return new FastByteArrayInputStream(buf, size);
        }

    }

    final static public class FastByteArrayInputStream extends InputStream {
        final protected byte[] buf;
        final protected int count;
        protected int pos = 0;

        public FastByteArrayInputStream(byte[] buf, int count) {
            this.buf = buf;
            this.count = count;
        }

        public final int available() {
            return count - pos;
        }

        public final int read() {
            return (pos < count) ? (buf[pos++] & 0xff) : -1;
        }

        public final int read(byte[] b, int off, int len) {
            if (pos >= count)
                return -1;

            if ((pos + len) > count)
                len = (count - pos);

            System.arraycopy(buf, pos, b, off, len);
            pos += len;
            return len;
        }

        public final long skip(long n) {
            if ((pos + n) > count)
                n = count - pos;
            if (n < 0)
                return 0;
            pos += n;
            return n;
        }

    }


    static String byteArrayToHexString(byte in[]) {
        byte ch = 0x00;
        int i = 0;
        if (in == null || in.length <= 0)
            return null;

        String pseudo[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };

        StringBuffer out = new StringBuffer(in.length * 2);
        while (i < in.length) {
            ch = (byte) (in[i] & 0xF0);
            ch = (byte) (ch >>> 4);
            ch = (byte) (ch & 0x0F);
            out.append(pseudo[(int) ch]);
            ch = (byte) (in[i] & 0x0F);
            out.append(pseudo[(int) ch]);
            i++;
        }
        String rslt = new String(out);
        return rslt;
    }

}
