package com.ning.arecibo.util.timeline.persistent;

import com.fasterxml.util.membuf.StreamyBytesMemBuffer;
import com.google.common.io.Files;
import org.codehaus.jackson.smile.SmileConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class StreamyBytesPersistentOutputStream extends OutputStream
{
    private static final Logger log = LoggerFactory.getLogger(StreamyBytesPersistentOutputStream.class);
    private static final int BUF_SIZE = 0x1000; // 4K

    private final String basePath;
    private final String prefix;
    private final StreamyBytesMemBuffer inputBuffer;
    private final List<String> createdFiles = new ArrayList<String>();

    public StreamyBytesPersistentOutputStream(String basePath, final String prefix, final StreamyBytesMemBuffer inputBuffer)
    {
        if (!basePath.endsWith("/")) {
            basePath += "/";
        }
        this.basePath = basePath;

        this.prefix = prefix;
        this.inputBuffer = inputBuffer;
    }

    @Override
    public void write(final int b) throws IOException
    {
        final byte data = (byte) b;
        write(new byte[]{data}, 0, 1);
    }

    @Override
    public void write(final byte[] data, final int off, final int len) throws IOException
    {
        if (!inputBuffer.tryAppend(data, off, len)) {
            // Buffer full - need to flush
            flushUnderlyingBufferAndReset();

            if (!inputBuffer.tryAppend(data, off, len)) {
                log.warn("Unable to append data: 1 byte lost");
            }
        }
    }

    private void flushUnderlyingBufferAndReset()
    {
        synchronized (inputBuffer) {
            if (inputBuffer.available() == 0) {
                // Somebody beat you to it
                return;
            }

            final String pathname = basePath + "arecibo." + prefix + "." + System.nanoTime() + ".bin";
            createdFiles.add(pathname);
            log.info("Flushing in-memory buffer to disk: {}", pathname);

            try {
                write(SmileConstants.TOKEN_LITERAL_END_ARRAY);
                final File out = new File(pathname);
                flushToFile(out);
            }
            catch (IOException e) {
                log.warn("Error flushing data", e);
            }
            finally {
                reset();
            }
        }
    }

    private void flushToFile(final File out) throws IOException
    {
        final byte[] buf = new byte[BUF_SIZE];
        FileOutputStream transfer = null;

        try {
            transfer = Files.newOutputStreamSupplier(out).getOutput();

            int bytesTransferred = 0;
            while (true) {
                final int r = inputBuffer.readIfAvailable(buf);
                if (r == 0) {
                    break;
                }
                transfer.write(buf, 0, r);
                bytesTransferred += r;
            }

            log.info("Saved {} bytes to disk", bytesTransferred);
        }
        finally {
            if (transfer != null) {
                transfer.flush();
            }
        }
    }

    public void reset()
    {
        inputBuffer.clear();
        try {
            write(SmileConstants.TOKEN_LITERAL_START_ARRAY);
        }
        catch (IOException e) {
            // Not sure how to recover?
        }
    }

    public List<String> getCreatedFiles()
    {
        return createdFiles;
    }
}